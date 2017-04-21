package com.bozhong.myswitch.zookeeper.watcher;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ChildrenChangeWacther implements Watcher {

    private static ChildrenChangeWacther CHILDREN_CHANGE_WACTHER = null;

    private ChildrenChangeWacther() {

    }

    public static final ChildrenChangeWacther getInstance() {
        if (CHILDREN_CHANGE_WACTHER == null) {
            synchronized (ChildrenChangeWacther.class) {
                if (CHILDREN_CHANGE_WACTHER == null) {
                    CHILDREN_CHANGE_WACTHER = new ChildrenChangeWacther();
                }
            }
        }
        return CHILDREN_CHANGE_WACTHER;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        SwitchLogger.getSysLogger().warn("watchedEvent :" +
                "path :" + watchedEvent.getPath() + " type :" + watchedEvent.getType().name() + " stateName :" + watchedEvent.getState().name());

        try {
            if(watchedEvent.getType().name().equals(Event.EventType.NodeChildrenChanged.name())
                    && StringUtil.isNotBlank(watchedEvent.getPath())){
                ZkClient.getInstance().addChildrenChangeWacther(watchedEvent.getPath(), this);
            }

        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(e.getMessage(), e);
        }

    }
}
