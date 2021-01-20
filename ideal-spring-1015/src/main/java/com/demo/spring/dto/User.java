package com.demo.spring.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class User {

    private String username;

    private String age;

    public User(){}

    public User(String username,String age){
        this.username = username;
        this.age = age;
    }

    // 对象排序规则
    @Override
    public boolean equals(Object obj){
        if(this == obj) return false;
        if(!(obj instanceof User)) return false;
        User user = (User)obj;
        if (this.username.equals(user.username)){
            return this.age.equals(user.age);
        }
        return this.username.equals(user.username);
    }

    @Override
    public int hashCode(){
        return this.username.hashCode();
    }


    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }


}
