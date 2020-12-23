package com.demo.sftp.sftp;

import com.jcraft.jsch.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Data
public class SftpClient implements AutoCloseable {


    /**
     * 配置信息
     */
    private SftpConfig conf;
    /**
     * Sftp客户端对象
     */
    private ChannelSftp sftp = null;
    /**
     * 会话
     */
    private Session sshSession = null;
    /**
     * 通道
     */
    private Channel channel = null;
    /**
     * 最后一次使用的时间
     * 为便于计算, 使用long型时间戳记录
     */
    private long lastUseDate;

    public SftpClient(SftpConfig conf) {
        this(conf.getFtpName(), conf.getIp(), conf.getPort(), conf.getUserName(), conf.getPassword());
    }

    public SftpClient(String ip, int port, String username, String password) {
        this(null, ip, port, username, password);
    }

    public SftpClient(String sftpName,String ip, int port, String username, String password) {
        conf = new SftpConfig();
        if(StringUtils.isEmpty(sftpName)){
            conf.setFtpName(username + "@" + ip + ":" + port);
        }
        conf.setIp(ip);
        conf.setPort(port);
        conf.setUserName(username);
        conf.setPassword(password);
        this.connect();
    }

    public synchronized ChannelSftp connect(){
        if(!this.checkConf()){
            log.error("sftp连接失败，连接信息不全");
            int clientFailNum = conf.getClientFailNum() + 1;
            conf.setClientFailNum(clientFailNum);
            throw new RuntimeException("sftp连接失败，连接信息不全");
        }
        return connect(conf.getIp(), conf.getPort(), conf.getUserName(), conf.getPassword());
    }

    /**
     * 连接sftp服务器，连接失败就报异常；
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    private synchronized ChannelSftp connect(String ip, int port, String username, String password){
        JSch jsch = new JSch();
        Properties sshConfig = new Properties();

        try {
            sshSession = jsch.getSession(username, ip, port);
            sshSession.setPassword(password);

            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();

            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("sftp服务器连接成功!");
        }catch (Exception e){
            log.error("sftp 连接错误: {}@{}:{}; pwd:{}", username, ip, port, password);
            int num = conf.getClientFailNum() + 1;
            conf.setClientFailNum(num);
            throw new RuntimeException("sftp连接错误");
        }

        return sftp;
    }


    /**
     * 校验连接sftp的4个基本信息是否为空
     * 齐全：true；否则：false；
     */
    private boolean checkConf() {
        if (null != conf) {
            return (conf.getIp() != null &&
                    conf.getPort() != 0 &&
                    conf.getUserName() != null &&
                    conf.getPassword() != null);
        }
        return false;
    }


    @Override
    public synchronized void close() {
        if(null != channel){
            channel.disconnect();
        }
        if(null != sftp){
            sftp.disconnect();
        }
        if(null != sshSession){
            sshSession.disconnect();
        }
    }


    // *********************************下面是文件操作的方法******************************************************
    /**
     * 功能说明:打开指定目录
     */
    public synchronized boolean openDir(String directory) {
        checkClient();
        log.info("打开指定目录 sftp cd {}", directory);

        if (StringUtils.isEmpty(directory)) {
            log.error("sftp 打开目录失败: 目录名不能为空!");
            return false;
        }
        try {
            if (!isDirExist(directory)) {
                sftp.mkdir(directory);
            }
            sftp.cd(directory);
            return true;
        } catch (SftpException e) {
            log.error("sftp 打开目录错误: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查连接状态, 连接断开则自动重连,重连失败抛出异常
     */
    public synchronized void checkClient() {
        if (!this.isConnected()) {
            this.connect();
        }
    }

    /**
     * 检查连接状态 true: 连接正常 false: 连接断开
     */
    public synchronized boolean isConnected() {
        if (sftp != null && sftp.isConnected()) {
            return true;
        }
        log.info("SFTP {}@{} 连接被断开", conf.getUserName(), conf.getIp());
        return false;
    }

    /**
     * 判断目录是否存在
     */
    public synchronized boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if ("no such file".equals(e.getMessage().toLowerCase())) {
                isDirExistFlag = false;
            } else {
                this.close();
            }
        }
        return isDirExistFlag;
    }

    /**
     * 通过发送pwd命令模拟心跳, 用于维持长连接
     * 先检查连接状态, 连接断开时进行一次重连
     *
     * @return 连接正常或重连成功返回true <br/>
     * 连接错误且重连失败返回false
     */
    public synchronized boolean heartbeat() {
        checkClient();
        return (null != pwd());
    }

    /**
     * 查看当前所处目录
     */
    public synchronized String pwd() {
        String path = null;
        try {
            path = sftp.pwd();
        } catch (SftpException e) {
            log.error("sftp {} 密码错误:{}", conf.getFtpName(), e.getMessage());
        }
        return path;
    }

    /**
     * 重命名文件或者目录 ,移动文件或者目录
     *
     * @param oldpath 旧文件或目录
     * @param newpath 新文件或目录
     */
    public synchronized boolean rename(String oldpath, String newpath) {
        log.info("sftp 移动文件从旧目录 {}到新目录 {}", oldpath, newpath);
        try {
            sftp.rename(oldpath, newpath);
            return true;
        } catch (Exception e) {
            log.error("SFTP移动文件错误: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 上传文件
     * 上传本地文件
     *
     * @param directory 上传的目录
     * @param fileName  要上传的文件名
     */
    public synchronized boolean upload(String directory, String fileName, InputStream in) throws IOException {
        log.info("sftp upload file {} to {}", fileName, directory);

        if (null == in || null == fileName) {
            log.error("上传文件失败, 缺少重要参数!");
            return false;
        }
        boolean status = false;
        try {
            if (this.openDir(directory)) {
                sftp.put(in, fileName);
                status = true;
            }
        } catch (Exception e) {
            log.error("上传文件错误! {}", e.getMessage());
            this.close();
        } finally {
            in.close();
        }
        return status;
    }


}
