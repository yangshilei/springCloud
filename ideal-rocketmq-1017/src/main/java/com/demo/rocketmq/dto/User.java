package com.demo.rocketmq.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class User {

    private String id ;

    private String username;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
