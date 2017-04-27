package com.bozhong.myswitch.domain;

import java.util.UUID;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchDataDTO extends BaseDTO {

    private static final long serialVersionUID = -1386682195076068159L;

    private Object value;

    private String desc;

    private String type;

    private String format;

    private long currentDateTime;

    private String optId;

    private String fieldName;

    private String version = UUID.randomUUID().toString();

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(long currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
