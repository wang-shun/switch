package com.bozhong.myswitch.zookeeper.watcher;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by renyueliang on 17/4/11.
 */
public class ConnectWatcher implements Watcher {

    private static String lock = "jka_dsqeqe_sfsg";

    private static ConnectWatcher connectWatcher=null;

    private ConnectWatcher(){

    }

    public static ConnectWatcher getConnectWatcher(){
        if(connectWatcher==null){
            synchronized (ConnectWatcher.lock){
                if(connectWatcher==null){
                    connectWatcher=new ConnectWatcher();
                }
            }
        }

        return connectWatcher;
    }

    private static volatile boolean connectSuccess = true;

    public static void unlock() {
        try {
            if (isLock) {
               // lock.notifyAll();
            }
        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(e.getMessage());
        } finally {
            isLock = false;
        }
    }

    private static volatile boolean isLock = false;

    @Override
    public void process(WatchedEvent watchedEvent) {

        SwitchLogger.getSysLogger().warn("ConnectWacther  watchedEvent :" +
                "path :" + watchedEvent.getPath() + " type :" + watchedEvent.getType().name() + " stateName :" + watchedEvent.getState().name());



        if (Event.KeeperState.Disconnected.name().equals(watchedEvent.getState().name())) {

            if (isLock) {
                return;
            }

            connectSuccess=false;
            try {
                synchronized (lock) {
                    isLock = true;
                    if(connectSuccess){
                        return ;
                    }
                    ZkClient.getInstance().connect();
                    connectSuccess=true;
                    SwitchRegister.getSwitchRegister().restartInit();
                }

                SwitchLogger.getSysLogger().warn("Disconnected_CONNECT_ERROR "+" ConnectWacther  SwitchRegister.restartInit success !");
            } catch (Throwable e) {
                SwitchLogger.getSysLogger().error(e.getMessage(), e);
            }
        }

        else if(Event.KeeperState.Expired.name().equals(watchedEvent.getState().name())){
            try {
                ZkClient.getInstance().connect();
                SwitchRegister.getSwitchRegister().restartInit();
            }catch (Throwable e){
                SwitchLogger.getSysLogger().error("Expired_CONNECT_ERROR "+e.getMessage(), e);

            }
        }
        else if(Event.KeeperState.SyncConnected.name().equals(watchedEvent.getState().name())){

        }

    }
}
