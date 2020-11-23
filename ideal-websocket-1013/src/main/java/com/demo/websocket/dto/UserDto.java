package com.demo.websocket.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class UserDto {

    private String userId;

    private String userName;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
