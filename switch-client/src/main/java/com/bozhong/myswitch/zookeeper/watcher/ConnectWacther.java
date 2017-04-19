package com.bozhong.myswitch.zookeeper.watcher;

import com.alibaba.fastjson.JSON;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/11.
 */
public class ConnectWacther implements Watcher {

    private static String lock = "jka_dsqeqe_sfsg";

    public static void unlock() {
        try {
            if (isLock) {
                lock.notifyAll();
            }
        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(e.getMessage());
        } finally {
            isLock = false;
        }
    }

    private static boolean isLock = false;

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("luotian:" + JSON.toJSONString(watchedEvent));

        SwitchLogger.getSysLogger().warn("ConnectWacther  watchedEvent :" +
                "path :" + watchedEvent.getPath() + " type :" + watchedEvent.getType().name() + " stateName :" + watchedEvent.getState().name());

        if (isLock) {
            return;
        }

        if (Watcher.Event.KeeperState.Disconnected.name().equals(watchedEvent.getState().name())) {
            try {
                synchronized (lock) {
                    isLock = true;
                    ZkClient.getInstance().connect();
                    SwitchRegister.getSwitchRegister().restartInit();
                    lock.wait();
                }

                SwitchLogger.getSysLogger().warn(" ConnectWacther  SwitchRegister.restartInit success !");
            } catch (Throwable e) {
                SwitchLogger.getSysLogger().error(e.getMessage(), e);
            }
        }

    }
}
