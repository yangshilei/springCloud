package com.demo.syn.constants;

public class CommConstants {

    public interface RESULT_CODE {
        // 成功
        int SUCCESS = 200;
        // 查询成功, 但结果为空
        int IS_NULL = 204;
        //请先登录
        int LOGIN_IN_FIRST = 205;
        // 凭证为空
        int AUTHORIZATION_IS_NULL = 206;

        // 请求参数错误
        int PARAM_NULL = 400;

        int REQUEST_ERROR = 401;
        // 请求资源不存在
        int CLIENT = 404;
        // 处理请求时发生冲突,必须返回有关冲突的信息
        int CONFILICT = 409;
        // 异常
        int ERROR = 500;
        // 超时
        int TIME_OUT = 504;
    }

}
