package com.demo.sftp.sftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Sftp连接创建工厂
 */
@Slf4j
public class SftpFactory {

    private Map<String, SftpList> factory;

    private final int DEF_MAP_NUM = 16;
    /**
     * 默认连接数
     * 初始化连接池的时候默认创建的连接数
     */
    private int defClientNum = 1;
    /**
     * 连接池自动增长的维护时间间隔
     */
    private int growDistanceTime = 10;
    /**
     * 连接池自动清理空闲连接的维护时间间隔
     */
    private int strictionDistanceTime = 2;



    public SftpFactory() {
        factory = new HashMap<>(DEF_MAP_NUM);
    }

    public SftpFactory(int defClientNum) {
        if (defClientNum > 0){
            this.defClientNum = defClientNum;
        }
        factory = new HashMap<>(DEF_MAP_NUM);
    }

    /**
     * 通过工厂创建SFTP连接<br/>
     * 如果连接池里已经有同样的连接则直接从连接池里获取<br/>
     * 如果连接池里没有, 则创建新的
     *
     * @param config SFTP连接配置
     * @return SFTP连接对象
     */
    public SftpClient createSftp(SftpConfig config){
        return createSftp(config.getIp(),config.getPort(),config.getUserName(),config.getPassword());
    }

    public SftpClient createSftp(String ip, int port, String username, String password){
        String key = username + "@" + ip + ":" + port;
        SftpConfig conf = new SftpConfig();
        conf.setIp(ip);
        conf.setPort(port);
        conf.setUserName(username);
        conf.setPassword(password);
        conf.setFtpName(key);

        SftpList sftpList = factory.get(key);
        if (null == sftpList){
            sftpList = new SftpList(conf, defClientNum);
            factory.put(key, sftpList);
        }
        else if (sftpList.isEmpty()){
            sftpList.add(conf);
        }
        return sftpList.next();
    }



    private class SftpList{

        private List<SftpClient> sftpClients;

        private SftpClient tempClient;

        private SftpConfig conf;

        /**
         * 最大连接数
         */
        private final int MAX_CLIENT_NUM = 10;

        /**
         * 最小连接数
         */
        private final int MIN_CLIENT_NUM = 1;

        /**
         * 最大错误次数,
         */
        private final int MAX_ERROR_NUM = 3;

        private int index = 0;



        private SftpList(SftpConfig config) {
            this(config, defClientNum);
        }

        /**
         * 创建一个新的连接池
         * @param config sftp 连接配置
         * @param clientNum 指定连接数
         */
        private SftpList(SftpConfig config, int clientNum){

            if (clientNum < MIN_CLIENT_NUM){

                clientNum = MIN_CLIENT_NUM;
            }else if (clientNum > MAX_CLIENT_NUM){

                clientNum = MIN_CLIENT_NUM;
            }
            this.sftpClients = new ArrayList<>(clientNum);
            this.conf = config;
            init();
        }

        /**
         * 在已有连接池增加一个 sftp 连接,
         * 增加后的总连接数不能大于最大连接数
         *
         * @param config 连接配置
         */
        public void add(SftpConfig config){
            if (conf == null || conf.getFtpName() == null){
                conf = config;
            }
            add();
        }

        private void add(){
            if (size() < MAX_CLIENT_NUM){
                SftpClient sftp = new SftpClient(conf);
                sftpClients.add(sftp);
            }
        }

        /**
         * 初始化连接池
         * 如果连接池是空的则自动按默认连接数增加连接
         */
        public void init(){
            int size = sftpClients.size();

            if (size <= 0){
                size = defClientNum;
            }

            for (int i = 0; i < size; i++) {
                SftpClient sftp = new SftpClient(conf);
                sftp.setLastUseDate(System.currentTimeMillis());
                sftpClients.add(sftp);
            }
            this.heartbeat();
        }


        /**
         * 取出一个sftp连接
         * @return sftp连接
         */
        private synchronized SftpClient next(){
            if ((++index) >= size()){
                index = 0;
            }
            SftpClient sftp = sftpClients.get(index);
            if (sftp == null || !sftp.isConnected()){
                log.warn("SFTP---{}({}) 空连接尝试重连: {}", index, size(), conf.getFtpName());
                sftp = new SftpClient(conf);
                sftpClients.set(index, sftp);
            }
            sftp.setLastUseDate(System.currentTimeMillis());
            log.info("获取 SFTP-{}({}): {}", index, size(), conf.getFtpName());
            return sftp;
        }


        /**
         * 获取一条早期使用的 sftp 连接
         * @return sftp 连接
         */
        private SftpClient early(){
            int i = index + 2;
            if (i == sftpClients.size()){
                i = 0;
            }
            return sftpClients.get(i);
        }

        private void updateConf(SftpConfig config){
            if (null != config){
                conf.setIp(config.getIp());
                conf.setPort(config.getPort());
                conf.setUserName(config.getUserName());
                conf.setPassword(config.getPassword());
            }
            for (SftpClient ftp : sftpClients){
                ftp.setConf(conf);
            }
            log.info("更新 SFTP 连接: {}", conf.getFtpName());
        }

        /**
         * 删除最后一个连接
         * 为避免关闭使用中的连接, 先将最后一个连接放到缓存中, 等下一次检查连接时再删除
         */
        private void removeLast(){
            int lastIndex = sftpClients.size() - 1;
            if (this.tempClient != null){
                this.tempClient.close();
            }
            tempClient = sftpClients.get(lastIndex);
            sftpClients.remove(lastIndex);
            log.info("删除最后一条 SFTP 连接: {}", conf.getFtpName());
        }

        /**
         * 删除一个指定的连接, 删除的连接会直接关闭
         * @param index 需要删除的连接下标
         */
        private void removeSftp(int index){
            if (sftpClients.size() > index){
                SftpClient ftp = sftpClients.get(index);
                if (ftp != null){
                    ftp.close();
                }
                sftpClients.remove(index);
                log.info("删除 SFTP-{}({}) 连接: {}", index, size(), conf.getFtpName());
            }
        }

        private void removeAll(){
            for (SftpClient ftp : sftpClients){
                ftp.close();
            }
            sftpClients.clear();
            log.info("清除 SFTP 连接池: {}", conf.getFtpName());
        }


        private final long DELAY = 10L;

        private final long LONG_DISTANCE = TimeUnit.MINUTES.toMillis(growDistanceTime);

        private final long SHORT_DISTANCE = TimeUnit.MINUTES.toMillis(strictionDistanceTime);

        /**
         * 定时器, 定时检查连接状态;<be/>
         * 超过15分钟没有调用SFTP连接, 则关闭最后一个连接;<be/>
         * 总连接数等于最小连接数(MIN_CLIENT_NUM)时不再关闭连接;<be/>
         *
         * 所有SFTP连接的最后一次调用时间间隔小于2分钟时, 说明使用比较频繁;<be/>
         * SFTP连接调用频繁时, 自动增加一个新连接;<be/>
         * 总连接数等于最大连接数(MAX_CLIENT_NUM)时, 不再增加连接;<be/>
         */
        public void heartbeat(){
            ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                    .namingPattern("sftp-heartbeat-%d").daemon(false).build();
            ScheduledExecutorService executorService =
                    new ScheduledThreadPoolExecutor(1, threadFactory);

            executorService.scheduleWithFixedDelay(()->{
                // 关闭临时连接
                if (tempClient != null){
                    tempClient.close();
                    tempClient = null;
                }
                // sftp 连接心跳
                for (SftpClient f : sftpClients){
                    try{
                        f.heartbeat();
                    }catch(Exception e){
                        log.error("sftp心跳错误");
                        removeAll();
                    }
                }
                // 总连接数大于最小连接数时, 判断最后一次使用的连接距当前的使用间隔
                // 大于10分钟未使用的移到临时连接, 等待下一次自动关闭
                if (sftpClients.size() > MIN_CLIENT_NUM){
                    long nowDate = System.currentTimeMillis();
                    long da = nowDate - early().getLastUseDate();
                    if (da > LONG_DISTANCE){
                        removeLast();
                        log.info("清理空闲连接: {}", conf.getFtpName());
                    }
                }
                // 总连接数小于最大连接数时, 轮询所有连接
                // 如果使用间隔小于2分钟, 自动增加一个连接
                if (sftpClients.size() < MAX_CLIENT_NUM && checkClient()){
                    add();
                }

            }, DELAY, DELAY, TimeUnit.MINUTES);

        }

        private boolean checkClient(){
            long nowDate = System.currentTimeMillis();
            for (SftpClient ftp: sftpClients){
                long da = nowDate - ftp.getLastUseDate();
                if (da > SHORT_DISTANCE){
                    return false;
                }
            }
            log.info("增加连接: {}", conf.getFtpName());
            return true;
        }

        public boolean isEmpty(){
            return sftpClients.isEmpty();
        }

        public int size(){
            return sftpClients.size();
        }

    }

}
