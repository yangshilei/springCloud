package com.demo.spring.test.ThreadLocalUser;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Tenant {

    private String tenantId;

    private String tenantName;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
