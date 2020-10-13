package com.demo.spring.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class User {

    private String username;

    private String age;

    public User(String username,String age){
        this.username = username;
        this.age = age;
    }

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
