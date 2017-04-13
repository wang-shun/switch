package com.bozhong.myswitch.zookeeper.watcher;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ChildrenChangeWacther implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {

        SwitchLogger.getSysLogger().warn("watchedEvent :" +
                "path :"+watchedEvent.getPath()+" type :"+watchedEvent.getType().name()+" stateName :"+watchedEvent.getState().name());

        try {
            ZkClient.getInstance().addChildrenChangeWacther(watchedEvent.getPath(), this);
        }catch (Throwable e){
            SwitchLogger.getSysLogger().error(e.getMessage(),e);
        }

    }
}
