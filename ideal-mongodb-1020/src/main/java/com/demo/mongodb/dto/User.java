package com.demo.mongodb.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class User {

    @Id
    private Long id;

    private String username;

    private String message;

    private String company;

    private String hobby;

    private Date createDate;

    private Date updateDate;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
