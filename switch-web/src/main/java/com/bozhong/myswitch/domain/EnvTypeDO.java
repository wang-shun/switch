package com.bozhong.myswitch.domain;


/**
 * Created by xiezg@317hu.com on 2017/1/9 0009.
 */
public class EnvTypeDO extends BaseDO{
    private String id;
    private String name;
    private String domainId;
    private String cnName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
}
