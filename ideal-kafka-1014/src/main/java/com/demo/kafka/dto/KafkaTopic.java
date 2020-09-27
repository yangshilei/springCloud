package com.demo.kafka.dto;

public enum KafkaTopic {

    ONT(1,"demo");

    private int code;
    private String msg;

    KafkaTopic(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public static String msg(int code){
        for(KafkaTopic item : KafkaTopic.values()){
            if(item.getCode() == code){
                return item.getMsg();
            }
        }
        return "noTopic";
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
