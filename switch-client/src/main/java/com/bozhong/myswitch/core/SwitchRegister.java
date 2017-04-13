package com.bozhong.myswitch.core;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.zookeeper.ZkClient;
import com.bozhong.myswitch.zookeeper.watcher.DataChangeWacther;

/**
 * Created by renyueliang on 17/4/10.
 */
public class SwitchRegister {

    private String localPath;

    private Class localClazz;

    private String appId;

    private String zkHosts;

    private String environ;

    private static SwitchRegister switchRegister = null;

    private SwitchRegister() {

    }

    public Class getLocalClazz() {
        return localClazz;
    }

    public static SwitchRegister getSwitchRegister() {

        if (switchRegister == null) {
            synchronized (SwitchRegister.class) {

                if (switchRegister == null) {
                    switchRegister = new SwitchRegister();
                }


            }
        }

        return switchRegister;
    }


    public void init(String appId, Class clazz, String zkHosts) throws Throwable{

        if (StringUtil.isBlank(appId) ||
                clazz == null ||
                StringUtil.isBlank(zkHosts)) {

            throw new SwitchException(
                    SwitchErrorEnum.ILL_ARGMENT.getError(),
                    SwitchErrorEnum.ILL_ARGMENT.getMsg());

        }

        this.appId = appId;
        this.localClazz = clazz;
        this.zkHosts = zkHosts;

        //
        this.localPath = SwitchUtil.getPath(appId, getEnviron());

        ZkClient.getInstance().connect(this.zkHosts);


        String environPath =SwitchConstants.SWITCH_ROOT_PATH+SwitchConstants.SLASH+environ;
        //创建appName
        if(!ZkClient.getInstance().has(environPath)){
            ZkClient.getInstance().createPersistent(environPath);
        }

        String appPath =environPath+SwitchConstants.SLASH+appId;
        //创建appName
        if(!ZkClient.getInstance().has(appPath)){
            ZkClient.getInstance().createPersistent(appPath);
        }


        if(!ZkClient.getInstance().has(localPath)){
            ZkClient.getInstance().createEphemeral(localPath);
        }
        String json = SwitchUtil.getJsonFromClazz(localClazz);
        ZkClient.getInstance().setDataForStr(localPath,json,-1);

        //监听
        ZkClient.getInstance().addDataChangeWacther(
                localPath,
                new DataChangeWacther()
        );




    }


    public void restartInit() throws Throwable {


        SwitchLogger.getSysLogger().warn("------------ SwitchRegister restartInit start ----------");

        if (!ZkClient.getInstance().has(localPath)) {
            ZkClient.getInstance().createEphemeral(localPath);
        }
        try {
            String json = SwitchUtil.getJsonFromClazz(localClazz);
            ZkClient.getInstance().setDataForStr(localPath, json, -1);
        } catch (Throwable e) {

            SwitchLogger.getSysLogger().error(" SwitchRegister.restartInit error ! "+e.getMessage(),e);

        } finally {
            //监听
            ZkClient.getInstance().addDataChangeWacther(
                    localPath,
                    new DataChangeWacther()
            );

        }

        SwitchLogger.getSysLogger().warn("------------ SwitchRegister restartInit end ----------");

    }

    public SwitchDataDTO change() throws Throwable {
        String json = ZkClient.getInstance().getDataForStr(localPath, -1);
       return SwitchUtil.setClazzDataForJson(json, localClazz);
    }




    public String getEnviron() {

        if(StringUtil.isBlank(this.environ)){
            /**
             * 这里根据
             */

            String ip = SwitchUtil.getIp();
            String appId = this.appId;

            this.environ = Environ.DEV.getName();

        }

        return this.environ;



    }


}
