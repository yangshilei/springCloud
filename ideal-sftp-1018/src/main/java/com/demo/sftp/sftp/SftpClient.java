package com.demo.sftp.sftp;

import com.demo.sftp.utils.Base64Util;
import com.jcraft.jsch.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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


    /**
     * 上传图片
     * 上传base64Str 格式的图片
     *
     * @param base64Str base64编码必填（去掉"data:image/jpeg;base64,"头）
     * @param directory 目录，必填(device=设备图片目录，inspection=巡检图片目录)
     * @param fileName  文件名可以为空（扩展名建议jpg）
     */
    public synchronized String uploadFile(@NotNull String base64Str, String directory, String fileName) {
        if (StringUtils.isBlank(base64Str)) {
            throw new RuntimeException("base64Str不能为空");
        }

        try (InputStream inputStream = Base64Util.baseToInputStream(base64Str)) {

            if (this.openDir(directory)) {
                if (StringUtils.isEmpty(fileName)) {
                    fileName = this.getUUID() + ".jpg";
                }
                // 图片服务器目录：device=设备图片目录，inspection=巡检图片目录，文件名=UUID
                // 目标文件名
                String dst = FileUtil.unite(directory, fileName);
                sftp.put(inputStream, dst, ChannelSftp.OVERWRITE);

            }
        } catch (Exception e) {
            log.error("上传图片错误: {}", e);
            this.close();
        }
        return fileName;
    }

    /**
     * 获取宇宙唯一码.
     *
     * @version Revision 1.0.0
     * @see:
     * @功能说明：
     *
     * @return
     */
    private String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().toUpperCase(Locale.ENGLISH);
        if (StringUtils.isEmpty(uuidStr)) {
            return "";
        }
        return uuidStr;
    }

    /**
     * 下载文件
     *
     * @param directory    下载文件所在路径目录
     * @param downFileName 下载的文件名称
     * @param savePath     保存到本地的路径目录
     */
    public synchronized boolean download(String directory, String downFileName, String savePath) {

        log.info("sftp 下载 {} to {}", FileUtil.unite(directory, downFileName), savePath);
        boolean status = false;
        File file = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (openDir(directory)) {
                if (FileUtil.isExistDir(savePath)) {
                    String saveFile = FileUtil.unite(savePath, downFileName);
                    file = new File(saveFile);
                    fileOutputStream = new FileOutputStream(file);
                    sftp.get(downFileName, fileOutputStream);
                    status = true;
                }
            }
        } catch (Exception e) {
            log.error("sftp 下载 {} 失败: {}", downFileName, e);
            this.close();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error("sftp 下载文件, 关闭输出流失败");
                status = false;
            }
        }
        return status;
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名称
     * @param localFile    本地文件名称
     * @param saveDir      存在本地的目录
     */
    public synchronized boolean download(String directory, String downloadFile, String localFile, String saveDir)
            throws IOException {

        boolean status = false;
        File file = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (openDir(directory)) {
                File fileDir = new File(saveDir);

                if (isExistsDir(fileDir)) {

                    file = new File(saveDir + localFile);
                    fileOutputStream = new FileOutputStream(file);
                    sftp.get(downloadFile, fileOutputStream);
                    status = true;
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            this.close();
        } finally {
            if (null != fileOutputStream) {
                fileOutputStream.close();
            }
        }

        return status;
    }

    /**
     * 下载文件，下载过程中采用重命名防止被其他程序误处理
     *
     * @param downloadPath  下载目录
     * @param fileName      文件名
     * @param savePath      保存目录
     * @param suffixPattren 下载中文件后缀名
     * @return boolean
     */
    public synchronized boolean downloadAsnFile(String downloadPath, String fileName, String savePath, String suffixPattren) {
        boolean status = true;
        FileOutputStream fileOutputStream = null;
        try {
            File fileDir = new File(savePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File tempFile = new File(FileUtil.unite(savePath, fileName + suffixPattren));
            fileOutputStream = new FileOutputStream(tempFile);

            sftp.get(fileName, fileOutputStream);
            File file = new File(FileUtil.unite(savePath, fileName));
            tempFile.renameTo(file);
        } catch (Exception e) {
            status = false;
            log.error("下载文件异常，原因：{}", e.getMessage());
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("关闭文件{}出错", fileName);
            }
        }

        return status;
    }

    /**
     * 判断指定文件夹是否存在, 不存在则创建
     *
     * @param file 指定文件夹
     * @author RenZhengGuo 2016年8月13日 下午5:29:37
     */
    private synchronized boolean isExistsDir(File file) {
        boolean mkdir = false;
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            log.info("目录不存在，创建目录");
            mkdir = file.mkdirs();
        }
        return mkdir;
    }

    /**
     * @param file
     * @author RenZhengGuo 2016年8月13日 下午5:29:37
     */
    public synchronized void cd(String file) {
        // 如果文件夹不存在则创建
        try {
            sftp.cd(file);
        } catch (SftpException e) {
            createDir(file);
//            chmod(Integer.parseInt("777", 8), file);
        }

    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public synchronized boolean mkdir(String file) {
        if (isDirExist(file)) {
            return true;
        }
        try {
            sftp.mkdir(file);
            return true;
        } catch (SftpException e) {
            log.error("创建文件夹出错！{}", e);
            return false;
        }
    }

    /**
     * 给目录授权
     *
     * @param permsion
     * @param file
     */
    public synchronized void chmod(int permsion, String file) {
        try {
            sftp.chmod(permsion, file);
        } catch (SftpException e) {
            log.info("为文件：{}授权失败！", file);
        }
    }

    /**
     * 创建一个文件目录
     */
    public synchronized void createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
                return;
            }
            // mkdir 命令不能创建多级目录, 所以要先根据文件分隔符分割成单个目录组
            String[] pathArry = createpath.trim().split(FileUtil.DEF_LINE_SEPARATOR);
            StringBuilder filePath = null;
            // 如果是绝对路径会以"/"开头, 此时分割出的数组首位是空串应替换成"/"
            if (pathArry[0].isEmpty()) {
                filePath = new StringBuilder(FileUtil.DEF_LINE_SEPARATOR);
            } else {
                filePath = new StringBuilder();
            }
            for (String pathNode : pathArry) {
                if (StringUtils.isEmpty(pathNode)) {
                    continue;
                }
                filePath.append(pathNode);
                String path = filePath.toString();

                if (!isDirExist(path)) {
                    sftp.mkdir(path);
                }
                filePath.append(FileUtil.DEF_LINE_SEPARATOR);
            }
            this.sftp.cd(createpath);
        } catch (SftpException e) {
            log.info("创建路径错误：" + createpath);
        }
    }


    /**
     * 判断文件是否存在
     */
    public synchronized boolean fileExist(String directory, String fileName) {
        boolean isDirExistFlag = false;
        try {
            Vector<ChannelSftp.LsEntry> vector = sftp.ls(directory);
            if (vector != null && !vector.isEmpty()) {
                Iterator<ChannelSftp.LsEntry> iterator = vector.iterator();
                while (iterator.hasNext()) {
                    ChannelSftp.LsEntry f = iterator.next();
                    if (f.getAttrs().isDir()) {
                        continue;
                    }
                    if (fileName.equals(f.getFilename())) {
                        return true;
                    }
                }
            }
            return false;
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
     * 删除文件
     * 不能使用全路径删除, 先CD到文件所在目录, 删除完毕后在CD回原目录
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public synchronized boolean delete(String directory, String deleteFile) {

        checkClient();
        log.info("sftp del {}/{}", directory, deleteFile);
        String pwd = pwd();
        boolean status = false;
        try {

            sftp.cd(directory);
            sftp.rm(deleteFile);
            status = true;
            sftp.cd(pwd);

        } catch (Exception e) {

            log.error("sftp 删除文件错误: {}", e);
            this.close();
        }
        return status;
    }

    /**
     * 删除文件
     * 删除当前目录下的文件
     *
     * @param deleteFile 要删除的文件
     */
    public synchronized boolean delete(String deleteFile) {

        checkClient();
        log.info("sftp del {}", deleteFile);
        boolean status = false;
        try {
            sftp.rm(deleteFile);
            status = true;

        } catch (Exception e) {

            log.error("sftp 删除文件错误: {}", e);
            this.close();
        }
        return status;
    }

    /**
     * 列出指定目录下的文件
     * 失败时返回空list
     *
     * @param directory 要列出的目录
     * @return 文件名列表
     */
    public synchronized List<String> listFiles(String directory) {

        checkClient();
        log.info("sftp ls {}", directory);

        List<String> ftpFileNameList = new ArrayList<>();

        if (!StringUtils.isEmpty(directory)) {
            try {
                Vector<ChannelSftp.LsEntry> sftpFile = sftp.ls(directory);
                for (ChannelSftp.LsEntry item : sftpFile) {
                    ftpFileNameList.add(item.getFilename());
                }
            } catch (SftpException e) {
                log.error("sftp 获取文件列表错误! {}", e.getMessage());
                this.close();
            }
        }
        return ftpFileNameList;
    }

    /**
     * 改变目录用户组
     *
     * @param gid
     * @param path
     * @return boolean
     */
    public synchronized boolean chgrp(Integer gid, String path) {
        try {
            sftp.chgrp(gid, path);
            return true;
        } catch (SftpException e) {
            log.info("改变用户组失败:{}", e.getMessage());
            return false;
        }

    }

    /**
     * 打开指定文件名的文件, 返回InputStream
     * 失败返回 null
     *
     * @param filePath 要打开的文件名
     * @return io流
     */
    public synchronized InputStream openFile(String filePath) {
        log.info("sftp open file {}", filePath);
        InputStream inputStream = null;
        try {
            inputStream = sftp.get(filePath);
            return inputStream;
        } catch (SftpException e) {
            log.error("打开文件错误: {}", e);
            this.close();
            return null;
        }
    }



}
