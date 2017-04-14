package com.bozhong.myswitch.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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

    public static final Map<String, String> DATA_MAP = new HashMap<>();

    static {
        for (Environ environ : EnumSet.allOf(Environ.class)) {
            DATA_MAP.put(environ.getName(), environ.getDesc());
        }
    }

    private Environ(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static Environ getEnviron(String name){

        for(Environ environ : Environ.values()){
            if(environ.getName().equals(name)){
                return environ;
            }
        }

        return null;
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
