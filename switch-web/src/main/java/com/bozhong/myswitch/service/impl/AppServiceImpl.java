package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.core.Environ;
import com.bozhong.myswitch.dao.AppDao;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.EnvTypeDO;
import com.bozhong.myswitch.service.AppService;
import com.bozhong.myswitch.util.ConfigUtil;
import com.bozhong.myswitch.util.SwitchHttpUtil;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 16/12/29.
 */
public class AppServiceImpl implements AppService {

    private AppDao appDao;

    @Override
    public List<AppDO> getAppsByUid(String uId) {
        if (Environ.DEV.getName().equals(ConfigUtil.getENRION()) || Environ.SIT.getName().equals(ConfigUtil.getENRION())) {
            try {
                return SwitchHttpUtil.getAppDOList(uId);
            } catch (IOException e) {
                return new ArrayList<>();
            }
        }
        return appDao.getAppsByUid(uId);
    }

    @Override
    public AppDO getAppDOByAppId(String appId) {
        return appDao.getAppDOByAppId(appId);
    }

    @Override
    public List<EnvTypeDO> getAllEnvTypes() {
        return appDao.getAllEnvTypes();
    }

    @Override
    public Map<String, String> getAllEnvTypeMap() {
        Map<String, String> envTypeMap = new HashMap<String, String>();
        List<EnvTypeDO> envTypeDOList = getAllEnvTypes();
        if (!CollectionUtils.isEmpty(envTypeDOList)) {
            for (EnvTypeDO envTypeDO : envTypeDOList) {
                envTypeMap.put(envTypeDO.getName(), envTypeDO.getCnName());
            }
        }

        return envTypeMap;
    }

    @Override
    public EnvTypeDO getEnvTypeDOByPrivateIpAndAppId(String privateIp, String appId) {
        return appDao.getEnvTypeDOByPrivateIpAndAppId(privateIp, appId);
    }

    @Override
    public List<String> getPrivateIpListByAppIdAndEnvType(String appId, String envType) {
        return appDao.getPrivateIpListByAppIdAndEnvType(appId, envType);
    }

    public void setAppDao(AppDao appDao) {
        this.appDao = appDao;
    }
}
