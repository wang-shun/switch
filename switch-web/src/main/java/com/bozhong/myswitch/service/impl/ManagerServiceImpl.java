package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.ChangeAllSwitchDTO;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.SwitchDataDTO;
import com.bozhong.myswitch.domain.SwitchNodeDTO;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.service.ManagerService;
import com.bozhong.myswitch.zookeeper.ZkClient;

import java.util.List;
import java.util.UUID;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ManagerServiceImpl implements ManagerService {

    @Override
    public void recordOpt() {
        SwitchLogger.getLogger().warn(" ManagerServiceImpl.recordOpt has excute ! ");
    }

    @Override
    public void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO) {

        changeSwitchDTO.verification();

        //插入一条数据到 开关数据变更记录表 默认初始化
        //更新switch value 到zk
        SwitchUtil.changeValue(changeSwitchDTO.getPath(), changeSwitchDTO.getFieldName(), changeSwitchDTO.getVal(), changeSwitchDTO.getOptId());


    }

    @Override
    public void changeAllSwitchValue(ChangeAllSwitchDTO changeAllSwitchDTO)  {

        changeAllSwitchDTO.verification();

        String appPath = SwitchConstants.SWITCH_ROOT_PATH
                + SwitchConstants.SLASH +
                changeAllSwitchDTO.getEnv() +
                SwitchConstants.SLASH + changeAllSwitchDTO.getAppId();

        try {

            List<SwitchNodeDTO> list = ZkClient.getInstance().getChildrenNode(appPath);
            //插入mongoDB

            SwitchUtil.changeAllValue(list,
                    appPath,
                    changeAllSwitchDTO.getFieldName(),
                    changeAllSwitchDTO.getVal(),
                    changeAllSwitchDTO.getOptId());

        }catch (Throwable e){
            throw new SwitchException(e.getMessage(),e);
        }


    }
}
