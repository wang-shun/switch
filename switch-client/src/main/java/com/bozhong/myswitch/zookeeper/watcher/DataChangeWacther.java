package com.bozhong.myswitch.zookeeper.watcher;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.server.SwitchServer;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/12.
 */
public class DataChangeWacther implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {

        SwitchLogger.getSysLogger().warn(" DataChangeWacther  watchedEvent :" +
                "path :" + watchedEvent.getPath() + " type :" + watchedEvent.getType().name() + " stateName :" + watchedEvent.getState().name());

        try {

            if (watchedEvent.getPath() != null &&
                    watchedEvent.getPath().indexOf(SwitchConstants.SWITCH_ROOT_PATH) == 0 &&
                    watchedEvent.getPath().split(SwitchConstants.SLASH).length == 5 &&
                    Event.EventType.NodeDataChanged.name().equals(watchedEvent.getType().name())
                    ) {

                SwitchLogger.getSysLogger().warn(" SwitchRegister change start ! ");

               SwitchDataDTO switchDataDTO = SwitchRegister.getSwitchRegister().change();

                SwitchLogger.getSysLogger().warn(" SwitchRegister change end ! ");

                //同步到服务端 告诉更新成功
                if(switchDataDTO!=null){
                    SwitchServer.sendChangeResult(switchDataDTO,0, null);
                }

            }

            if(watchedEvent.getPath() != null &&
                    watchedEvent.getPath().indexOf(SwitchConstants.SWITCH_ROOT_PATH) == 0 &&
                    watchedEvent.getPath().split(SwitchConstants.SLASH).length == 5 &&
                    Event.EventType.NodeDeleted.name().equals(watchedEvent.getType().name())){

                SwitchRegister.getSwitchRegister().restartInit();

            }

        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(" DataChangeWacther.process error ! "+e.getMessage(), e);
        } finally {
            try {
                if (watchedEvent.getPath() != null &&
                        watchedEvent.getPath().indexOf(SwitchConstants.SWITCH_ROOT_PATH) == 0 &&
                        watchedEvent.getPath().split(SwitchConstants.SLASH).length == 5 &&
                        ( Event.EventType.NodeDataChanged.name().equals(watchedEvent.getType().name())
                                ||  Event.EventType.NodeDeleted.name().equals(watchedEvent.getType().name()) )
                        ) {
                    ZkClient.getInstance().addDataChangeWacther(watchedEvent.getPath(), this);
                }
            } catch (Throwable e1) {
                SwitchLogger.getSysLogger().error(" DataChangeWacther.process addDataChangeWacther error ! "+e1.getMessage(), e1);
            }
        }
    }
}
