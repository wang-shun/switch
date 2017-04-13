package com.bozhong.myswitch.util;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.core.SwitchRegister;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchLoad {

    private String appId;
    private String zkHosts;

    public void init() throws Throwable{
        SwitchRegister.getSwitchRegister().init(this.appId,SettingParam.class,this.zkHosts);


        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(3000l);
                        SwitchLogger.getSysLogger().warn("-------start----------");
                        SwitchLogger.getSysLogger().warn("NAME:"+SettingParam.NAME);
                        SwitchLogger.getSysLogger().warn("AGE:"+SettingParam.AGE);
                        SwitchLogger.getSysLogger().warn("HEIGHT:"+SettingParam.HEIGHT);
                        SwitchLogger.getSysLogger().warn("SEX:"+SettingParam.SEX);
                        SwitchLogger.getSysLogger().warn("-------end----------");
                    }catch (Throwable e){
                        SwitchLogger.getSysLogger().error(e.getMessage(),e);
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
}
