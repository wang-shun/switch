package com.bozhong.myswitch.util;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.core.SwitchRegister;

import java.lang.reflect.Field;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchLoad {

    private String appId;
    private String zkHosts;
    private Class dynamicClass;

    public void init() throws Throwable {
        if (dynamicClass == null) {
            dynamicClass = SettingParam.class;
        }

        SwitchRegister.getSwitchRegister().init(this.appId, dynamicClass, this.zkHosts);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000l);
                        Field[] fields = dynamicClass.getDeclaredFields();
                        SwitchLogger.getSysLogger().warn("-------start----------");
                        for (Field field : fields) {
                            System.out.println(field.getName() + ":" + field.get(dynamicClass));
                            SwitchLogger.getSysLogger().warn(field.getName() + ":" + field.get(dynamicClass));
                        }
                        SwitchLogger.getSysLogger().warn("-------end----------");
                    } catch (Throwable e) {
                        SwitchLogger.getSysLogger().error(e.getMessage(), e);
                    }

                }
            }
        });

        thread.start();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public Class getDynamicClass() {
        return dynamicClass;
    }

    public void setDynamicClass(Class dynamicClass) {
        this.dynamicClass = dynamicClass;
    }
}
