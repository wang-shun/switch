package com.bozhong.myswitch.core;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.zookeeper.ZkClient;
import com.bozhong.myswitch.zookeeper.watcher.DataChangeWacther;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.HashMap;
import java.util.Map;

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


    public void init(String appId, Class clazz, String zkHosts) throws Throwable {

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


        String environPath = SwitchConstants.SWITCH_ROOT_PATH + SwitchConstants.SLASH + environ;
        //创建appName
        if (!ZkClient.getInstance().has(environPath)) {
            ZkClient.getInstance().createPersistent(environPath);
        }

        String appPath = environPath + SwitchConstants.SLASH + appId;
        //创建appName
        if (!ZkClient.getInstance().has(appPath)) {
            ZkClient.getInstance().createPersistent(appPath);
        }


        if (!ZkClient.getInstance().has(localPath)) {
            ZkClient.getInstance().createEphemeral(localPath);
        }
        String json = SwitchUtil.getJsonFromClazz(localClazz);
        ZkClient.getInstance().setDataForStr(localPath, json, -1);

        //监听
        ZkClient.getInstance().addDataChangeWacther(
                localPath,
                DataChangeWacther.getInstance()
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

            SwitchLogger.getSysLogger().error(" SwitchRegister.restartInit error ! " + e.getMessage(), e);

        } finally {
            //监听
            ZkClient.getInstance().addDataChangeWacther(
                    localPath,
                    DataChangeWacther.getInstance()
            );

        }

        SwitchLogger.getSysLogger().warn("------------ SwitchRegister restartInit end ----------");

    }

    public SwitchDataDTO change() throws Throwable {
        String json = ZkClient.getInstance().getDataForStr(localPath, -1);
        System.out.println("change()" + json);
        return SwitchUtil.setClazzDataForJson(json, localClazz);
    }


    public String getEnviron() {

        if (StringUtil.isBlank(this.environ)) {
            String appId = this.appId;
            try {
                switch (getEnvTypeWithAppIdAndHostIp(appId, SwitchUtil.getIp())) {
                    case "DEV":
                        this.environ = Environ.DEV.getName();
                        break;
                    case "SIT":
                        this.environ = Environ.SIT.getName();
                        break;
                    case "UAT":
                        this.environ = Environ.UAT.getName();
                        break;
                    case "PRD":
                        this.environ = Environ.ONLINE.getName();
                        break;
                    default:
                        this.environ = Environ.DEV.getName();
                }
            } catch (Throwable e) {
                SwitchLogger.getSysLogger().error(e.getMessage());
            }
        }

        return this.environ;


    }

    private String getEnvTypeWithAppIdAndHostIp(String appId, String hostIp) {
        try {
            String e = "http://config.317hu.com/configcenter/config/configSet/getEnvType";
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(e);
            NameValuePair appIdPair = new NameValuePair("appId", appId);
            NameValuePair appKeyPair = new NameValuePair("appKey", "c7b950537c5e48288923348cb61bfe75");
            NameValuePair ipPair = new NameValuePair("hostIp", hostIp);
            method.setRequestBody(new NameValuePair[]{appIdPair, appKeyPair, ipPair});
            client.executeMethod(method);
            String responseBodyString = new String(method.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
            Map map = new HashMap();
            if (method.getStatusCode() == 200) {
                map = JSON.parseObject(responseBodyString, Map.class);
            }
            return (String) map.get("name");
        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(e.getMessage());
        }

        return null;
    }


}
