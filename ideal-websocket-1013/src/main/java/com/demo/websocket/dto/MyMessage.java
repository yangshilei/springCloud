package com.demo.websocket.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class MyMessage {

    private String userId;

    private String message;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }

}
