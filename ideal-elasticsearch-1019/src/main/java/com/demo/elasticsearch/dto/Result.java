package com.demo.elasticsearch.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description：统一接口返回体模板
 * @Author yangshilei
 * @Date 2019-05-15 14:16
 */
@Data
@Getter
public class Result<T> implements Serializable {

  private static final Integer SUCCESS_CODE = 200;
  private static final Integer REEOR_CODE = 400;
  private static final String SUCCESS_MESSAGE = "ok";

  private Integer code;

  private String message;

  private T data;

  protected Result(){}

  protected Result(Integer code, String message, T data){
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> Result<T> ok(T data){
    return new Result<>(SUCCESS_CODE,SUCCESS_MESSAGE,data);
  }

  /**
   * 正确信息描述
   * @param message
   * @param <T>
   * @return
   */
  public static <T> Result<T> ok(String message) {
    return new Result(SUCCESS_CODE,message,null);
  }

  /**
   * 正确信息描述，同时返回错误信息
   * @param message
   * @param data
   * @param <T>
   * @return
   */
  public static <T> Result<T> ok(String message,T data){
    return new Result<T>(SUCCESS_CODE,message,data);
  }

  /**
   * 错误信息描述，适用于不需要规范化错误码的场景
   * @param message
   * @param <T>
   * @return
   */
  public static <T> Result<T> err (String message){
    return new Result<T>(REEOR_CODE,message,null);
  }

  /**
   * 错误信息描述，适用于需要规范错误码的场景
   * @param errMessage
   * @param <T>
   * @return
   */
  public static <T> Result<T> err(@NotNull  ErrMessage errMessage){
    return new Result<>(errMessage.code(),errMessage.desc(),null);
  }


}
