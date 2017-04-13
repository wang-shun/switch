package com.bozhong.myswitch.util;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.zookeeper.ZkClient;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchServerRegister {


    public void init() throws  Throwable{

        String path= SwitchConstants.SWITCH_ROOT_PATH+SwitchConstants.SWITCH_SERVER_PATH;
        if(!ZkClient.getInstance().has(path)){
            ZkClient.getInstance().createPersistent(path);
        }

        String ip= SwitchUtil.getIp();


        path=path+SwitchConstants.SLASH+ip+":"+ConfigUtil.getPORT();


        if(ZkClient.getInstance().has(path)){
            ZkClient.getInstance().createEphemeral(path);
        }

    }


}
