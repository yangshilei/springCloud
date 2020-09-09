package com.demo.shiro.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Set;

@Data
public class User {

    private String id;
    private String userName;
    private String password;

    // 用户对应的角色集合
    private Set<Role> roles;

    public User() {

    }

    public User(String id, String userName, String password, Set<Role> roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
