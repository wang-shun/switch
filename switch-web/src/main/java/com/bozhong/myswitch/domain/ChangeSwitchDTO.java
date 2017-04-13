package com.bozhong.myswitch.domain;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.exception.SwitchException;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ChangeSwitchDTO extends BaseDTO {

    private String fieldName;

    private String path;

    private String val;

    private String optId;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public void verification(){

        if(StringUtil.isBlank(fieldName)
                ||
                StringUtil.isBlank(path)
                ||
                StringUtil.isBlank(val)
                ||
                StringUtil.isBlank(optId)){

            throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError());
        }

    }
}
