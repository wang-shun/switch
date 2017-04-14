package com.bozhong.myswitch.service;

import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.EnvTypeDO;

import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 16/12/29.
 */
public interface AppService {

    /**
     * @param uId
     * @return
     */
    List<AppDO> getAppsByUid(String uId);

    /**
     * @param appId
     * @return
     */
    AppDO getAppDOByAppId(String appId);

    /**
     * @return
     */
    List<EnvTypeDO> getAllEnvTypes();

    /**
     * @return
     */
    Map<String, String> getAllEnvTypeMap();

    /**
     * @param privateIp
     * @param appId
     * @return
     */
    EnvTypeDO getEnvTypeDOByPrivateIpAndAppId(String privateIp, String appId);

    /**
     * @param appId
     * @param envType
     * @return
     */
    List<String> getPrivateIpListByAppIdAndEnvType(String appId, String envType);
}
