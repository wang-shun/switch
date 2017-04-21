package com.bozhong.myswitch.server;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.CollectionUtil;
import com.bozhong.common.util.ResultMessageBuilder;
import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.domain.SwitchNodeDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.zookeeper.ZkClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.util.List;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchServer {

    private static int index = 0;

    private static Client client = null;


    public static Client getClient() {
        if (client == null) {

            //     方式一
            ClientConfig cc = new DefaultClientConfig();
            cc.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
            //      Client实例很消耗系统资源，需要重用
            //      创建web资源，创建请求，接受响应都是线程安全的
            //      所以Client实例和WebResource实例可以在多个线程间安全的共享
            client = Client.create(cc);
        }

        return client;

    }


    public static String getSendIpAndPort() {

        try {
            List<SwitchNodeDTO> list = ZkClient.getInstance().getAllServer();

            if (CollectionUtil.isEmpty(list)) {
                return null;
            }

            int size = list.size();

            if (size == 1) {
                return list.get(0).getNodeName();
            }

            if (index >= size) {
                index = 0;
            }

            return list.get(index).getNodeName();


        } catch (Throwable e) {

            SwitchLogger.getSysLogger().error(e.getMessage(), e);

        } finally {
            index++;
        }
        return "";
    }

    private static String getSendChangeToServerUrl() {
        String ipAndPort = getSendIpAndPort();
        if (StringUtil.isBlank(ipAndPort)) {
            throw new SwitchException("SERVER_IP_PORT_IS_NULL");
        }

        return "http://" + ipAndPort + "/switchweb/myswitch/mangerRest/callBack";

    }


    public static void sendChangeResult(String ip, SwitchDataDTO switchDataDTO, int callNum, String errorCode) {

        if (switchDataDTO == null) {
            SwitchLogger.getSysLogger().warn(" SwitchServer.sendChangeResult switchDataDTO is null");
            return;
        }

        if(callNum>0 && callNum<=3){
            try {
                SwitchLogger.getSysLogger().warn("SwitchServer.sendChangeResult sleep start ! ");
                Thread.sleep(1000 * callNum);
                SwitchLogger.getSysLogger().warn("SwitchServer.sendChangeResult sleep end ! ");
            }catch (Throwable sleepe){
                SwitchLogger.getSysLogger().error("SwitchServer.sendChangeResult error ! ",sleepe);
            }
        }

        if (callNum > 3) {
            SwitchLogger.getSysLogger().warn(" SwitchServer.sendChangeResult has excute " + callNum + " time ,cannot excute ! switchDataDTO:" + JSON.toJSONString(switchDataDTO));

            return;
        }

        SwitchLogger.getSysLogger().warn(" SwitchServer.sendChangeResult start switchDataDTO:" + JSON.toJSONString(switchDataDTO));

        try {

            String url = getSendChangeToServerUrl();

            WebResource resource = getClient().resource(url);

            MultivaluedMapImpl params = new MultivaluedMapImpl();
            params.add("optId", switchDataDTO.getOptId());
            params.add("fieldName", switchDataDTO.getFieldName());
            if (StringUtil.isBlank(ip)) {
                params.add("ip", SwitchUtil.getIp());
            } else {
                params.add("ip", ip);
            }

            if (StringUtil.isNotBlank(errorCode)) {
                params.add("errorCode", errorCode);
            }


            String result = resource.queryParams(params).post(String.class);
            ResultMessageBuilder.ResultMessage resultMessage = JSON.parseObject(result,
                    ResultMessageBuilder.ResultMessage.class);
            if (!resultMessage.isSuccess()) {
                throw new SwitchException(SwitchErrorEnum.RUNTIME_EXCEPTION.getError(),
                        SwitchErrorEnum.RUNTIME_EXCEPTION.getMsg());
            }

            SwitchLogger.getSysLogger().warn(" sendChangeResult callBack :" + result);
        } catch (Throwable e) {
            sendChangeResult(ip, switchDataDTO, ++callNum, errorCode);
        }

    }

}
