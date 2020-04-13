package com.demo.syn.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Joe
 * @create 2018-11-14 15:13
 **/
@Slf4j
@Component(value = "helloJob")
public class HelloJob {

    public void hello() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("你好，成功打印了测试语句=====" + format.format(new Date()));
    }

}
