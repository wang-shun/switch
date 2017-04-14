package com.bozhong.myswitch.domain;

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
     * 字段名称
     */
    private String fieldName;

    /**
     * 之前的字段值
     */
    private String oldFieldValue;

    /**
     * 新的字段值
     */
    private String newFieldValue;

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

    public String getOldFieldValue() {
        return oldFieldValue;
    }

    public void setOldFieldValue(String oldFieldValue) {
        this.oldFieldValue = oldFieldValue;
    }

    public String getNewFieldValue() {
        return newFieldValue;
    }

    public void setNewFieldValue(String newFieldValue) {
        this.newFieldValue = newFieldValue;
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
}
