package com.demo.spring.test.baseThread.并行流水线;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 线程数据交换的载体，一个model；
 */
@Data
public class PSMsg {

    public Integer i;

    public Integer j;

    public String orgStr = null;

    @Override
    public String toString () {
        return JSONObject.toJSONString(this);
    }
}
