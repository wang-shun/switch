package com.bozhong.myswitch.core;

/**
 * Created by renyueliang on 17/4/10.
 */
public enum FieldType {

    STRING("string","字符串"),
    INT("int","整型"),
    LONG("long","长整型"),
    DOUBLE("double","双精度"),
    BOOLEAN("boolean","布尔型"),
    //DATE("date","日期类型")
    ;


    private String name;
    private String desc;

    private FieldType(String name,String desc){
        this.name=name;
        this.desc=desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
