package com.bozhong.myswitch.dao.impl;


import com.bozhong.myswitch.dao.AppDao;
import com.bozhong.myswitch.dao.BaseDao;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.EnvTypeDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 16/12/29.
 */
public class AppDaoImpl extends BaseDao implements AppDao {

    @Override
    public List<AppDO> getAppsByUid(String uId) {
        return findForList("AppDaoImpl_getAppsByUid",uId);
    }

    @Override
    public AppDO getAppDOByAppId(String appId) {
        return (AppDO) findForObject("AppDaoImpl_getAppDOByAppId", appId);
    }

    @Override
    public List<EnvTypeDO> getAllEnvTypes() {
        return findForList("AppDaoImpl_getAllEnvTypes");
    }

    @Override
    public EnvTypeDO getEnvTypeDOByPrivateIpAndAppId(String privateIp, String appId) {
        Map<String, String> filter = new HashMap<>();
        filter.put("privateIp", privateIp);
        filter.put("appId", appId);
        return (EnvTypeDO) findForObject("AppDaoImpl_getEnvTypeDOByPrivateIpAndAppId", filter);
    }

    @Override
    public List<String> getPrivateIpListByAppIdAndEnvType(String appId, String envType) {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("appId", appId);
        filter.put("envType", envType);
        return findForList("AppDaoImpl_getPrivateIpByAppIdAndEnvType", filter);
    }
}
