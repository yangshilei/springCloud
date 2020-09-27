package com.demo.kafka.dto;

/**
* @Author: yangshilei
* @Date: 2019/5/15 15:04
*/
public interface ErrMessage {

  /**
   * 错误响应状态码
   * @return
   */
  int code();

  /**
   * 错误描述
   * @return
   */
  String desc();
}
