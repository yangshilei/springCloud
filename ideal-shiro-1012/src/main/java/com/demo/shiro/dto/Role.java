package com.demo.shiro.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.util.Set;

@Data
public class Role {
    private String id;
    private String roleName;

    // 角色对应的权限集合
    private Set<Permissions> permissions;

    public Role() {
    }

    public Role(String id, String roleName, Set<Permissions> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
