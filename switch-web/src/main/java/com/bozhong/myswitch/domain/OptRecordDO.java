package com.bozhong.myswitch.domain;

import com.alibaba.fastjson.JSON;

/**
 * Created by renyueliang on 17/4/12.
 * 操作记录实体类
 */
public class OptRecordDO extends BaseDO {

    /**
     * 操作ID
     */
    private String optId;
    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 环境（开发、测试、UAT、线上）
     */
    private String env;

    /**
     * 路径
     */
    private String path;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 值
     */
    private String val;

    /**
     * 操作人
     */
    private String createBy;

    /**
     * 操作时间
     */
    private String createDt;

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
