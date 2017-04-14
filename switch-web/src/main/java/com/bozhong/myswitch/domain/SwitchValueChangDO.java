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
    private String fieldValue;

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
     * 同步结果（成功或者事变）
     */
    private String syncResult;

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

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
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

    public String getSyncResult() {
        return syncResult;
    }

    public void setSyncResult(String syncResult) {
        this.syncResult = syncResult;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
