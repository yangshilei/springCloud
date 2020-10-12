package com.demo.spring.dto;


import com.alibaba.fastjson.JSON;
import com.demo.spring.constants.CommConstants;
import com.demo.spring.constants.ResponseEnum;
import com.demo.spring.util.DateUtil;
import lombok.Data;

import java.io.Serializable;


/**
 * 接口调用返回结果对象
 *
 * @author 宋志宗
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4260730014392351320L;
    /**
     * 接口执行结果
     */
    private boolean success;
    /**
     * 返回消息（包括成功或错误消息）
     */
    private String message;
    /**
     * 接口返回代码
     */
    private int errorCode;
    /**
     * 交易流水号，与请求一致
     */
    private String transactionId;
    /**
     * 返回日期，格式：yyyyMMddHHmmssSSS
     */
    private String rspTime;
    /**
     *响应对象信息
     */
    private Object resultObject;

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, CommConstants.RESULT_CODE.SUCCESS, null, data);
    }

    public static <T> Result<T> ok(String message,T data){
        return new Result<>(true, CommConstants.RESULT_CODE.SUCCESS,message,data);
    }

    public static Result<String> ok(String message){
        return new Result<String>(true,CommConstants.RESULT_CODE.SUCCESS,message);
    }

    public static Result err(String errMes) {
        return new Result(false,CommConstants.RESULT_CODE.ERROR,errMes);
    }

    public static <T> Result<T> err(T data){
        return new Result<>(false, CommConstants.RESULT_CODE.ERROR, null, data);
    }

    public static <T> Result<T> err(String errMes, T data){
        return new Result<>(false, CommConstants.RESULT_CODE.ERROR, errMes, data);
    }

    public Result() {
        this.setResult(true);
    }

    public Result(boolean success, int errorCode) {
        this.success = success;
        this.errorCode = errorCode;
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
    }

    public Result(boolean result, int errorCode, String message) {
        this.success = result;
        this.errorCode = errorCode;
        this.message = message;
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
    }

    public Result(boolean success, int errorCode, String message, Object resultObject) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.resultObject = resultObject;
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
    }

    public Result(ResponseEnum responseEnum, Object resultObject) {
        this.success = true;
        this.errorCode = responseEnum.getErrorCode();
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
        this.message = responseEnum.getMsg();
        this.resultObject = resultObject;
    }

    public Result(Object resultObject) {
        this.success = true;
        this.errorCode = ResponseEnum.SUCCESS.getErrorCode();
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
        this.message = ResponseEnum.SUCCESS.getMsg();
        this.resultObject = resultObject;
    }

    public Result(ResponseEnum responseEnum) {
        this.success = true;
        this.errorCode = responseEnum.getErrorCode();
        this.rspTime = DateUtil.getyyyyMMddHHmmssSSS();
        this.message = responseEnum.getMsg();
    }

    /**
     * 获取结果对象, 根据接收的对象类型自动转型
     * 注: 使用前确保接收对象类型和实际类型相符
     * @param <T> 接收对象类型
     * @return 返回结果
     */
    public <T>T achieveResult(){
        @SuppressWarnings("unchecked")
        T obj = (T) this.resultObject;
        return obj;
    }

    public boolean success() {
        return success;
    }

    public void setResult(boolean success){
        this.success = success;
    }

    public boolean isResult(){
        return success;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
