package com.bozhong.myswitch.watcher;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.util.SwitchServerRegister;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/21.
 */
public class SyncServerIpWatcher implements Watcher {


    private static SyncServerIpWatcher syncServerIpWatcher = null;

    private SyncServerIpWatcher() {

    }

    public static SyncServerIpWatcher getSyncServerIpWatcher() {
        if (syncServerIpWatcher == null) {
            synchronized ("dsflhsh_sfhsihfs") {
                if (syncServerIpWatcher == null) {
                    syncServerIpWatcher = new SyncServerIpWatcher();
                }
            }
        }

        return syncServerIpWatcher;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {


        SwitchLogger.getSysLogger().warn("SyncServerIpWatcher  watchedEvent :" +
                "path :" + watchedEvent.getPath() + " type :" + watchedEvent.getType().name() + " stateName :" + watchedEvent.getState().name());


        if (Event.KeeperState.SyncConnected.name().equals(watchedEvent.getState().name())) {
            try {
                SwitchServerRegister.registSerer();
            } catch (Throwable e) {
                SwitchLogger.getSysLogger().error("SyncServerIpWatcher registSerer error" + e.getMessage(), e);
            }
        }


        if(Event.KeeperState.Expired.name().equals(watchedEvent.getState().name())
                ||
                Event.KeeperState.Disconnected.name().equals(watchedEvent.getState().name())   ){
            try {
                ZkClient.getInstance().connect();
                SwitchServerRegister.registSerer();
            }catch (Throwable  e){
                SwitchLogger.getSysLogger().error("SyncServerIpWatcher ZkClient.getInstance().connect error" + e.getMessage(), e);
            }
        }


    }
}
