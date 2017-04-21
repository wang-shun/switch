package com.bozhong.myswitch.util;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.watcher.SyncServerIpWatcher;
import com.bozhong.myswitch.zookeeper.ZkClient;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchServerRegister {


    public static void registSerer() throws  Throwable{
        String path= SwitchConstants.SWITCH_ROOT_PATH+SwitchConstants.SWITCH_SERVER_PATH;
        if(!ZkClient.getInstance().has(path)){
            ZkClient.getInstance().createPersistent(path);
        }

        String ip= SwitchUtil.getIp();


        path=path+SwitchConstants.SLASH+ip+":"+ConfigUtil.getPORT();


        if(!ZkClient.getInstance().has(path)){
            ZkClient.getInstance().createEphemeral(path);
        }

        ZkClient.getInstance().addExistsWacther(SwitchConstants.SWITCH_ROOT_PATH, SyncServerIpWatcher.getSyncServerIpWatcher());

        SwitchLogger.getSysLogger().warn(" SwitchServerRegister.registSerer success !  ");

    }


    public void init() throws  Throwable{
        SwitchServerRegister.registSerer();


    }


}
