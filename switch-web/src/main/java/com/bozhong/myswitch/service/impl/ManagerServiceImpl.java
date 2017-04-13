package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.service.ManagerService;

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
        SwitchUtil.changeValue(changeSwitchDTO.getPath(),changeSwitchDTO.getFieldName(),changeSwitchDTO.getVal(),changeSwitchDTO.getOptId());


    }


}
