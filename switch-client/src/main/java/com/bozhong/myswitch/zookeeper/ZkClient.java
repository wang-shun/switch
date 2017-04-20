package com.bozhong.myswitch.zookeeper;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.domain.SwitchNodeDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.zookeeper.watcher.ChildrenChangeWacther;
import com.bozhong.myswitch.zookeeper.watcher.ConnectWacther;
import com.bozhong.myswitch.zookeeper.watcher.DataChangeWacther;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by renyueliang on 17/4/11.
 */
public class ZkClient {


    private static ZkClient zkClient = null;

    private String hosts;

    private ZkClient() {

    }

    public static ZkClient getInstance() {

        if (zkClient == null) {
            synchronized ("lock_zk_sync") {
                if (zkClient == null) {
                    zkClient = new ZkClient();
                }
            }
        }

        return zkClient;
    }


    private static final int SESSION_TIMEOUT = 10000;//会话延时

    private ZooKeeper zk = null;

    private CountDownLatch countDownLatch = new CountDownLatch(1);//同步计数器


    public void connect() throws Exception {
        connect(this.hosts);
    }

    public void connect(String hosts) throws Exception {

        if (StringUtil.isBlank(hosts)) {
            throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError(),
                    SwitchErrorEnum.ILL_ARGMENT.getMsg());
        }

        this.hosts = hosts;

        SwitchLogger.getSysLogger().warn(" ZKClient connect is start ! ");

        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                SwitchLogger.getSysLogger().warn("ZkClient - process excute ! " + "watchEvent path: " + event.getPath() + "  !  stateName:"
                        + event.getState().name() + " eventType:" + event.getType().name());

                if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
                    countDownLatch.countDown();//计数器减一
                    countDownLatch = new CountDownLatch(1);
                    SwitchLogger.getSysLogger().warn("ZkClient - countDownLatch.countDown excute ! ");
                    ConnectWacther.unlock();
                }
            }
        });

        SwitchLogger.getSysLogger().warn("ZkClient - connect await in ! ");
        countDownLatch.await();//阻塞程序继续执行
        SwitchLogger.getSysLogger().warn("ZkClient - connect await out ! ZkClient connect zk is success ! ");
        createPersistent(SwitchConstants.SWITCH_ROOT_PATH);

        zk.exists(SwitchConstants.SWITCH_ROOT_PATH, new ConnectWacther());
    }

    private void create(String groupName, CreateMode createMode) throws KeeperException, InterruptedException, Exception {

        String path = SwitchUtil.firstAddChar(groupName);
        Stat stat = zk.exists(path, true);
        String createPath = path;
        if (stat == null) {
            createPath = zk.create(path,
                    null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE/*允许任何客户端对该znode进行读写*/,
                    createMode /*持久化的znode*/);

            /*  CreateMode.PERSISTENT 持久化的*/
            /*  CreateMode.EPHEMERAL 持久化的*/
            SwitchLogger.getSysLogger().warn(" ZkClient regist server Created :" + createPath);
        } else {
            SwitchLogger.getSysLogger().warn(" ZkClient regist server exists :" + createPath);
        }

        zk.getChildren(createPath, ChildrenChangeWacther.getInstance());

        Stat stat1 = new Stat();
        stat1.setAversion(-1);
        zk.getData(createPath, DataChangeWacther.getInstance(), stat);
    }

    public void createPersistentSequential(String groupName) throws KeeperException, InterruptedException, Exception {
        create(groupName, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    /**
     * 创建group
     *
     * @param groupName 组名
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createPersistent(String groupName) throws KeeperException, InterruptedException, Exception {
        create(groupName, CreateMode.PERSISTENT);
    }

    public void createEphemeral(String groupName) throws KeeperException, InterruptedException, Exception {
        create(groupName, CreateMode.EPHEMERAL);
    }

    public void createEphemeralSequential(String groupName) throws KeeperException, InterruptedException, Exception {
        create(groupName, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void addChildrenChangeWacther(String groupName, Watcher watcher) throws KeeperException, InterruptedException, Exception {
        zk.getChildren(groupName, watcher);
    }

    public boolean has(String path) throws KeeperException, InterruptedException {

        if (!SwitchUtil.firstCharCheck(path, SwitchConstants.SLASH)) {
            path = SwitchConstants.SLASH + path;
        }
        return zk.exists(path, false) != null;
    }

    public void addDataChangeWacther(String groupName, Watcher watcher) throws KeeperException, InterruptedException, Exception {
        groupName = SwitchUtil.firstAddCharDefualt(groupName);

        Stat stat = new Stat();
        stat.setAversion(-1);

        zk.getData(groupName, watcher, stat);
    }

    public void setData(String path, byte[] data, int version) throws Exception {
        path = SwitchUtil.firstAddCharDefualt(path);

        zk.setData(path, data, version);
    }


    public byte[] getData(String path, int version) throws Exception {
        path = SwitchUtil.firstAddCharDefualt(path);

        Stat stat = new Stat();
        stat.setAversion(version);

        return zk.getData(path, DataChangeWacther.getInstance(), stat);
    }

    public String getDataForStr(String path, int version) throws Exception {
        byte[] bytes = getData(path, version);

        if (bytes == null) {
            return "";
        }

        return new String(bytes, "utf-8").toString();

    }

    public void setDataForStr(String path, String data, int version) throws Exception {

        path = SwitchUtil.firstAddCharDefualt(path);

        if (StringUtil.isBlank(data)) {
            return;
        }

        byte[] bytes = data.getBytes("utf-8");

        setData(path, bytes, version);

    }


    public List<SwitchNodeDTO> getFirstNode() throws KeeperException, InterruptedException, Exception {

        Stat stat = zk.exists(
                SwitchConstants.SWITCH_ROOT_PATH +
                        SwitchConstants.SLASH +
                        SwitchRegister.getSwitchRegister().getEnviron(), false);
        if (stat == null) {
            return new ArrayList<SwitchNodeDTO>();
        }
        List<SwitchNodeDTO> list = new ArrayList<>();
        List<String> children = zk.getChildren(SwitchConstants.SWITCH_ROOT_PATH, false);
        Collections.sort(children);

        for (String str : children) {
            SwitchNodeDTO realTimeNodeDTO = new SwitchNodeDTO();
            realTimeNodeDTO.setNodeName(str);
            realTimeNodeDTO.setAllPath(SwitchConstants.SWITCH_ROOT_PATH +
                    SwitchConstants.SLASH + str);
            list.add(realTimeNodeDTO);
        }

        return list;

    }

    public List<SwitchNodeDTO> getAllServer() throws Throwable {

        String path = SwitchConstants.SWITCH_ROOT_PATH + SwitchConstants.SWITCH_SERVER_PATH;

        Stat stat = zk.exists(path, false);

        if (stat == null) {
            return new ArrayList<SwitchNodeDTO>();
        }

        List<SwitchNodeDTO> list = new ArrayList<>();
        List<String> children = zk.getChildren(path, false);
        Collections.sort(children);

        for (String str : children) {
            SwitchNodeDTO realTimeNodeDTO = new SwitchNodeDTO();
            realTimeNodeDTO.setNodeName(str);
            realTimeNodeDTO.setAllPath(path +
                    SwitchConstants.SLASH + str);
            list.add(realTimeNodeDTO);
        }

        return list;

    }

    public List<SwitchNodeDTO> getChildrenNode(String path) throws KeeperException, InterruptedException, Exception {

        path = SwitchUtil.firstAddCharDefualt(path);

        Stat stat = zk.exists(path, false);

        if (stat == null) {
            return new ArrayList<SwitchNodeDTO>();
        }

        List<SwitchNodeDTO> list = new ArrayList<>();
        List<String> children = zk.getChildren(path, false);
        Collections.sort(children);
        for (String str : children) {
            SwitchNodeDTO realTimeNodeDTO = new SwitchNodeDTO();
            realTimeNodeDTO.setNodeName(str);
            realTimeNodeDTO.setAllPath(path + SwitchConstants.SLASH + str);
            String dataJson = getDataForStr(realTimeNodeDTO.getAllPath(), -1);
            realTimeNodeDTO.setDataJson(dataJson);
            list.add(realTimeNodeDTO);
        }

        return list;

    }
}
