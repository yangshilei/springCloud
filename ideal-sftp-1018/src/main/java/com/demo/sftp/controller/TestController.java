package com.demo.sftp.controller;

import com.demo.sftp.dto.Result;
import com.demo.sftp.sftp.FileUtil;
import com.demo.sftp.sftp.SftpClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Api("sftp学习接口")
@RestController
@RequestMapping(value = "/sftp")
public class TestController {

    @ApiOperation(value = "测试接口",notes = "测试接口")
    @PostMapping("/test")
    Result test(){
        log.info("请求正常");
        this.uploadFile();
        return Result.ok("success");
    }

    /**
     * 模拟上传文件到sftp服务器：
     * 代码生成数据，写入文件并上传到sftp服务器；
     */
    private void uploadFile(){
        String ip = "47.94.111.71";
        String port = "22";
        String userName = "mysftp";
        String passWord = "ysl13222031857";
        SftpClient sftp = new SftpClient(null, ip, Integer.parseInt(port), userName, passWord);

        log.info("sftp客户端创建成功");
        String content = this.getContent();
        ByteArrayInputStream bakInputStream = new ByteArrayInputStream(content.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        log.info("开始上传文件");
        String bakPath = "/upload/bakPath";
        String storePath = "/upload/storePath";
        String fileName = "YSL_" + 8888 + "_test" + System.currentTimeMillis();
        String tempFileName = "~" + fileName;
        boolean b = this.uploadFile(sftp, bakPath, storePath,
                fileName, tempFileName, bakInputStream, inputStream);
        log.info("文件上传结束");

        if(!b){
            log.info("文件上传失败了");
            return;
        }
        sftp.close();
    }

    /**
     * 文件内容封装
     */
    private String getContent(){
        StringBuilder outStr = new StringBuilder("STA|1"+"\r\n");
        outStr.append("数据编号,企业ID,企业名称,操作类型,账单类型,产品类型,产品唯一识别编码," +
                "账目项1,账目项1产品单价,账目项1产品个数,账目项2,账目项2产品单价,账目项2产品个数"+"\r\n");

        outStr.append(1+",")
                .append(1112233+",")// 数据编号
                .append(31803+",") // 企业ID
                .append(3+",") // 操作类型
                .append(2 + ",") // 账单类型
                .append(40+",") // 产品类型编号
                .append("SV3333333"+",") // 产品唯一识别编码
                .append("zmx111222"+",") // 账目项1
                .append(233+",") // 账目项1产品单价
                .append(1+",") // 账目项1产品个数
                .append("zmx111222"+",") // 账目项2
                .append(33+",") // 账目项2产品单价
                .append(1+",") // 账目项2产品个数
                .append("\r\n")
                .append("end");
        return outStr.toString();
    }
    /**
     * 上传文件
     */
    public static boolean uploadFile(SftpClient sftp, String bakPath, String storePath,
                                     String fileName, String tempFileName,
                                     InputStream bakInput, InputStream input) {
        try {
            //上传至备份目录
            log.info("开始上传文件fileName==={}",fileName);
            sftp.upload(bakPath, fileName, bakInput); // 文件上传到备份文件夹
            //上传临时文件至下发目录
            boolean b = sftp.upload(storePath, tempFileName, input);
            // 临时文件上传成功，修改临时文件名为正式名
            if (b) {
                sftp.rename(FileUtil.unite(storePath, tempFileName), FileUtil.unite(storePath, fileName));
            }
            return true;
        } catch (IOException e) {
            log.error("上传文件{}出错", fileName);
            log.error(e.getLocalizedMessage());
            return false;
        }
    }
}
