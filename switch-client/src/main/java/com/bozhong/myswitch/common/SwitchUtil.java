package com.bozhong.myswitch.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bozhong.common.util.DateUtil;
import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.core.AppSwitch;
import com.bozhong.myswitch.core.FieldType;
import com.bozhong.myswitch.core.SwitchRegister;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.domain.SwitchNodeDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.server.SwitchServer;
import com.bozhong.myswitch.zookeeper.ZkClient;
import org.apache.commons.collections.map.HashedMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 17/4/10.
 */
public class SwitchUtil {

    private static String ip = null;

    public static String getLocalIp() {

        String localHostIp = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        localHostIp = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            SwitchLogger.getSysLogger().error(ex.getMessage(), ex);
        }

        return localHostIp;

    }

    public static void initIp() {
        if (ip == null) {
            ip = getLocalIp();
        }
    }

    public static String getIp() {
        initIp();

        return ip;
    }


    public static Map<String, SwitchDataDTO> realTimeDataDTOMap = new HashedMap();
    public static Map<String, Long> realTimeMap = new HashedMap();

    public static boolean firstCharCheck(String path, String hasThisChar) {

        if (path == null || "".equals(path)) {
            return false;
        }

        return path.indexOf(hasThisChar) == 0;


    }


    /**
     * @param path
     * @return
     */
    public static String firstAddChar(String path) {
        return firstAddChar(path, SwitchConstants.SLASH);
    }

    /**
     * @param path
     * @param addChar
     * @return
     */
    public static String firstAddChar(String path, String addChar) {

        if (!firstCharCheck(path, addChar)) {
            return addChar + path;
        }

        return path;
    }


    /**
     * @param path
     * @return
     */
    public static String firstAddCharDefualt(String path) {
        path = firstAddChar(path, SwitchConstants.SWITCH_ROOT_PATH);
        path = firstAddChar(path, SwitchConstants.SLASH);
        return path;
    }

    /**
     * @param path
     * @param addChar
     * @return
     */
    public static String firstAddChar(String path, String addChar, String intervalChar) {

        path = firstAddChar(path, intervalChar);
        path = firstAddChar(path, addChar);
        return path;
    }


    public static String getJsonFromClazz(Class clazz) {

        Map<String, SwitchDataDTO> jsonMap = new HashedMap();

        Field[] fields = clazz.getDeclaredFields();

        try {

            for (Field field : fields) {
                AppSwitch appSwitch = field.getAnnotation(AppSwitch.class);

                if (appSwitch != null) {
                    Long version = realTimeMap.get(field.getName()) != null ? realTimeMap.get(field.getName()) : 0l;
                    SwitchDataDTO realTimeDataDTO = new SwitchDataDTO();
                    realTimeDataDTO.setDesc(appSwitch.desc());
                    realTimeDataDTO.setType(appSwitch.type());
                    realTimeDataDTO.setFormat(appSwitch.format());
                    realTimeDataDTO.setValue(field.get(clazz));
                    realTimeDataDTO.setFieldName(field.getName());
                    realTimeDataDTO.setCurrentDateTime(DateUtil.getCurrentDate());
                    realTimeDataDTO.setVersion(version);
                    realTimeDataDTO.setFieldName(field.getName());

                    jsonMap.put(field.getName(), realTimeDataDTO);
                    realTimeDataDTOMap.put(field.getName(), realTimeDataDTO);
                    realTimeMap.put(field.getName(), version);
                }

            }

        } catch (Throwable e) {
            SwitchLogger.getSysLogger().error(e.getMessage(), e);
        }

        return JSON.toJSONString(jsonMap);
    }

    public static void setSwitchClassValue(String filedName, Object val, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();

        try {
            for (Field field : fields) {
                AppSwitch swtich = field.getAnnotation(AppSwitch.class);
                if (swtich != null) {

                    if (filedName.equals(field.getName())) {
                        if (swtich.type().equals(FieldType.STRING.getName())) {
                            field.set(clazz, val);
                        } else if (swtich.type().equals(FieldType.INT.getName())) {
                            if (val instanceof String) {
                                field.set(clazz, val == null ? 0 : Integer.valueOf((String) val));
                            } else if (val instanceof Integer) {
                                field.set(clazz, val == null ? 0 : (Integer) val);
                            }
                        } else if (swtich.type().equals(FieldType.BOOLEAN.getName())) {
                            if (val instanceof String) {
                                field.set(clazz, "true".equals(val) ? true : false);
                            } else {
                                field.set(clazz, val);
                            }
                        } else if (swtich.type().equals(FieldType.LONG.getName())) {
                            if (val instanceof String) {
                                field.set(clazz, val == null ? 0 : Long.valueOf((String) val));
                            } else {
                                field.set(clazz, val == null ? 0 : (Long) val);
                            }
                        } else if (swtich.type().equals(FieldType.DOUBLE.getName())) {
                            if (val instanceof BigDecimal) {
                                field.set(clazz, val == null ? 0 : ((BigDecimal) val).doubleValue());
                            } else if (val instanceof Double) {
                                field.set(clazz, val == null ? 0 : (Double) val);
                            } else if (val instanceof String) {
                                field.set(clazz, val == null ? 0 : Double.valueOf((String) val));
                            }

                        }
                        break;
                    }


                }
            }
        } catch (Throwable e) {
            throw new SwitchException("SwitchUtil.setSwitchClassValue " + e.getMessage(), e);
        }

    }


    public static SwitchDataDTO setClazzDataForJson(String json, Class clazz) {


        if (StringUtil.isBlank(json)) {
            return null;
        }


        try {
            Map<String, SwitchDataDTO> map = JSON.parseObject(
                    json, new TypeReference<Map<String, SwitchDataDTO>>() {
                    });

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                AppSwitch swtich = field.getAnnotation(AppSwitch.class);
                if (swtich != null) {

                    SwitchDataDTO newRealTime = map.get(field.getName());
                    Long version = realTimeMap.get(field.getName());

                    if (newRealTime == null) {
                        continue;
                    }

                    if (version != null && newRealTime.getVersion() == version) {
                        continue;
                    }

                    Object val = newRealTime.getValue();

                    try {

                        if (swtich.type().equals(FieldType.STRING.getName())) {
                            field.set(clazz, val);
                        } else if (swtich.type().equals(FieldType.INT.getName())) {
                            field.set(clazz, val == null ? 0 : (Integer) val);
                        } else if (swtich.type().equals(FieldType.BOOLEAN.getName())) {
                            field.set(clazz, val);
                        } else if (swtich.type().equals(FieldType.LONG.getName())) {
                            if (val instanceof Integer) {
                                field.set(clazz, val == null ? 0 : ((Integer) val).longValue());
                            } else if (val instanceof Long) {
                                field.set(clazz, val == null ? 0 : (Long) val);
                            }
                        } else if (swtich.type().equals(FieldType.DOUBLE.getName())) {
                            if (val instanceof BigDecimal) {
                                field.set(clazz, val == null ? 0 : ((BigDecimal) val).doubleValue());
                            } else if (val instanceof Double) {
                                field.set(clazz, val == null ? 0 : (Double) val);
                            } else if (val instanceof String) {
                                field.set(clazz, val == null ? 0 : Double.valueOf((String) val));
                            }
                        }

                        realTimeDataDTOMap.put(field.getName(), newRealTime);
                        realTimeMap.put(field.getName(), newRealTime.getVersion());
                        return newRealTime;

                    } catch (Throwable e) {
                        SwitchLogger.getLogger().error(
                                StringUtil.format("SwitchUtil.setClazzDataForJson error fieldName:%s val:%s errmsg:%s", field.getName(), val, e.getMessage())
                                , e);
                    }


                }


            }
        } catch (Throwable e) {
            SwitchLogger.getLogger().error("json:[" + json + "]" + e.getMessage(), e);
        }

        return null;

    }

    public static String createPathForApp(String appId, String environ) {
        return StringUtil.format("%s/%s/%s",
                SwitchConstants.SWITCH_ROOT_PATH,
                environ,
                appId);
    }


    public static void changeAllValue(List<SwitchNodeDTO> list, String appPath, String fieldName, String val, String optId) {
        for (SwitchNodeDTO switchNodeDTO : list) {
            try {
                changeValue(appPath + SwitchConstants.SLASH + switchNodeDTO.getNodeName(), fieldName, val, optId);
            } catch (Throwable e) {
                SwitchLogger.getLogger().error(
                        StringUtil.format("SwitchUtil.changeAllValue error ! appPath:%s nodeIp:%s fieldName:%s val%s optId:%s errorMsg:%s",
                                appPath, switchNodeDTO.getNodeName(), fieldName, val, optId, e.getMessage())
                        , e);
                SwitchDataDTO switchDataDTO = new SwitchDataDTO();
                switchDataDTO.setFieldName(fieldName);
                switchDataDTO.setOptId(optId);
                SwitchServer.sendChangeResult(switchNodeDTO.getNodeName(), switchDataDTO, 0, e.getMessage());
            }
        }
    }

    public static void changeAllValue(String appId, String env, String fieldName, String val, String optId) {

        if (StringUtil.isBlank(appId) || StringUtil.isBlank(fieldName) || StringUtil.isBlank(val) || StringUtil.isBlank(optId)) {
            throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError());
        }

        try {
            String appPath = SwitchConstants.SWITCH_ROOT_PATH + SwitchConstants.SLASH + env + SwitchConstants.SLASH + appId;

            List<SwitchNodeDTO> list = ZkClient.getInstance().getChildrenNode(appPath);
            changeAllValue(list, appPath, fieldName, val, optId);


        } catch (Throwable e) {
            SwitchLogger.getLogger().error(StringUtil.format("SwitchUtil.changeAllValue error ! appId:%s env:%s fieldName:%s val%s optId:%s errorMsg:%s",
                    appId, env, fieldName, val, optId, e.getMessage()), e);

            throw new SwitchException(e.getMessage(), e);
        }


    }

    public static void changeValue(String path, String fieldName, String val, String optId) {

        if (StringUtil.isBlank(path) || StringUtil.isBlank(fieldName) || StringUtil.isBlank(val) || StringUtil.isBlank(optId)) {
            throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError());
        }

        try {
            String json = ZkClient.getInstance().getDataForStr(path, -1);
            if (StringUtil.isBlank(json)) {
                //直接修改本地 然后重新加载
                setSwitchClassValue(fieldName, val, SwitchRegister.getSwitchRegister().getLocalClazz());
                SwitchRegister.getSwitchRegister().restartInit();
                return;
            }

            Map<String, SwitchDataDTO> map = JSON.parseObject(
                    json, new TypeReference<Map<String, SwitchDataDTO>>() {
                    });

            SwitchDataDTO switchDataDTO = map.get(fieldName);

            if (switchDataDTO == null) {
                throw new SwitchException("CANNOT_FIND_SwitchDataDTO BY fieldName");
            }

            if (switchDataDTO.getType().equals(FieldType.STRING.getName())) {
                switchDataDTO.setValue(val);
            } else if (switchDataDTO.getType().equals(FieldType.INT.getName())) {
                switchDataDTO.setValue(Integer.valueOf(val));
            } else if (switchDataDTO.getType().equals(FieldType.DOUBLE.getName())) {
                switchDataDTO.setValue(Double.valueOf(val).doubleValue());
            } else if (switchDataDTO.getType().equals(FieldType.LONG.getName())) {
                switchDataDTO.setValue(Long.valueOf(val));
            } else if (switchDataDTO.getType().equals(FieldType.BOOLEAN.getName())) {
                switchDataDTO.setValue("true".equals(val));
            }

            switchDataDTO.setCurrentDateTime(DateUtil.getCurrentDate());
            switchDataDTO.setVersion(switchDataDTO.getVersion() + 1);//改变值的时候版本号+1
            switchDataDTO.setOptId(optId);

            map.put(fieldName, switchDataDTO);
            json = JSON.toJSON(map).toString();

            ZkClient.getInstance().setDataForStr(path, json, -1);


        } catch (Throwable e) {
            throw new SwitchException(StringUtil.format(" SwitchUtil.changeValue path:%s fieldName:%s val:%s optId:%s " +
                    "errorMsg:%s", path, fieldName, val, optId, e.getMessage()), e);
        }

    }

    public static String getPath(String appId, String environ) {

        initIp();

        return StringUtil.format("%s/%s/%s/%s",
                SwitchConstants.SWITCH_ROOT_PATH,
                environ,
                appId,
                ip);
    }


}
