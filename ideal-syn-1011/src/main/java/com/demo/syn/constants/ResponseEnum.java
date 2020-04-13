package com.demo.syn.constants;

/**
 * java类简单作用描述
 *
 * @ProjectName: si_dev
 * @Author: wuhua
 * @CreateDate: 2018/9/17 0017 下午 5:29
 */
public enum ResponseEnum {

    SUCCESS(200, "success"),

    LOGOUT(200, "退出登录"),

    LOGIN_IN_FIRST(205, "请先登录"),

    SESSION_TIMEOUT_LOGIN_IN_FIRST(205, "会话过期,请先登录"),

    NO_AUTHORIZATION(206, "凭证为空"),

    UNAUTHORIZED(207, "没有权限"),

    ERROR(500, "系统异常！"),

    LOGIN_FAIL(500, "登陆失败!"),

    ASYN_FAIL(500, "异步服务失败!"),

    IS_NOT_PERMITTED(500, "权限不足"),

    ACCOUNT_ALREADY_EXIST(500, "创建失败，帐号已存在"),

    CTGMQ_CONSUMER_CONNECT_FAIL(500, "ctgmq 消费者连接失败"),

    CTGMQ_PRODUCER_CONNECT_FAIL(500, "ctgmq 生产者连接失败"),

    CRM_ERROR_USERNAME(500, "用户名错误！"),

    UPLOAD_FILE_FAILED(500, "使用ftp上传文件失败"),

    PARAM_ERROR(500, "参数错误");

    private int errorCode;

    private String msg;

    ResponseEnum(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
