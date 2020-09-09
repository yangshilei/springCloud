package com.demo.shiro.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Permissions {

    private String id;

    private String permissionsName;


    public Permissions(){

    }

    public Permissions(String id, String permissionsName) {
        this.id = id;
        this.permissionsName = permissionsName;
    }

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
