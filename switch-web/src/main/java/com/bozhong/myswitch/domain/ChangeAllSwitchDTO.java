package com.bozhong.myswitch.domain;

import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.core.Environ;
import com.bozhong.myswitch.exception.SwitchException;

/**
 * Created by renyueliang on 17/4/14.
 */
public class ChangeAllSwitchDTO extends BaseDTO {

    private String fieldName;

    private String val;

    private String appId;

    private String optId;

    private String env;

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public void verification(){

     if(Environ.getEnviron(getEnv())==null)   {
         throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError());
     }

    }
}
