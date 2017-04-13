package com.bozhong.myswitch.core;

/**
 * Created by renyueliang on 17/4/10.
 */
public enum Environ {

    DEV("dev", "开发环境"),
    SIT("sit", "测试环境"),
    UAT("uat", "预发环境"),
    ONLINE("online", "线上环境")
    ;


    private String name;

    private String desc;

    private Environ(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
