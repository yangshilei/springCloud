package com.demo.sftp.sftp;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class SftpConfig {

    /**
     * ftp连接名
     */
    private String ftpName;
    /**
     * SFTP IP地址
     */
    private String ip;
    /**
     * SFTP 端口
     */
    private Integer port;
    /**
     * SFTP 用户名
     */
    private String userName;
    /**
     * SFTP 密码
     */
    private String password;
    /**
     * 连接失败次数
     */
    private int clientFailNum;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
