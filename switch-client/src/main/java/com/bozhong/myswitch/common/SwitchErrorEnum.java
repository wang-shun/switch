package com.bozhong.myswitch.common;

/**
 * Created by renyueliang on 17/4/11.
 */
public enum SwitchErrorEnum {

    ILL_ARGMENT("ILL_ARGMENT","参数异常"),
    ZK_HOSTS_IS_NULL("ZK_HOSTS_IS_NULL","zkHosts 为空"),
    GET_APP_ZK_NODE_ERROR("GET_APP_ZK_NODE_ERROR","获取zk的节点出错"),
    ENRION_CODE_ERROR("ENRION_CODE_ERROR","环境配置错误")
    ;


    private String error;

    private String msg;


    private SwitchErrorEnum(String error ,String msg){
        this.error=error;
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
