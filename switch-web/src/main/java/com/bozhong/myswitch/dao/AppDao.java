package com.bozhong.myswitch.dao;

import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.EnvTypeDO;

import java.util.List;

/**
 * Created by renyueliang on 16/12/29.
 */
public interface AppDao {

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
