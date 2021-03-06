package com.bozhong.myswitch.domain;

import com.alibaba.fastjson.JSON;


/**
 * Created by renyueliang on 17/4/12.
 * 数据更新实体类
 */
public class SwitchValueChangDO extends BaseDO {
    /**
     * 操作ID
     */
    private String optId;

    /**
     * IP
     */
    private String ip;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段值
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

    /**
     * 回调时间
     */
    private String callbackDT;

    /**
     * 同步结果（成功或者失败）
     */
    private boolean syncResult;

    /**
     * 路径
     */
    private String path;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 环境
     */
    private String env;

    /**
     * 错误码
     */
    private String errorCode;

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getCallbackDT() {
        return callbackDT;
    }

    public void setCallbackDT(String callbackDT) {
        this.callbackDT = callbackDT;
    }

    public boolean getSyncResult() {
        return syncResult;
    }

    public void setSyncResult(boolean syncResult) {
        this.syncResult = syncResult;
    }

    public boolean isSyncResult() {
        return syncResult;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
