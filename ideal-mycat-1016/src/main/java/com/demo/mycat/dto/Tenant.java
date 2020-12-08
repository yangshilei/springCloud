package com.demo.mycat.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Tenant {

    private Long id;
    private Long tenantId;
    private String tenantName;
    private Long phone;
    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
