package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.OptRecordDO;
import com.bozhong.myswitch.service.AppService;
import com.bozhong.myswitch.service.ManagerService;
import com.bozhong.myswitch.service.MongoService;
import com.yx.eweb.main.EWebServletContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ManagerServiceImpl implements ManagerService {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private MongoService mongoService;

    private AppService appService;

    @Override
    public void recordOpt(OptRecordDO optRecordDO) {
        SwitchLogger.getLogger().warn(" ManagerServiceImpl.recordOpt has excute ! ");

        AppDO appDO = appService.getAppDOByAppId(optRecordDO.getAppId());
        optRecordDO.setAppName(appDO.getAppName());
        optRecordDO.setCreateBy((String) EWebServletContext.getRequest().getAttribute("uId"));
        optRecordDO.setCreateDt(SIMPLE_DATE_FORMAT.format(new Date()));
        mongoService.insertOne(optRecordDO);

    }

    @Override
    public void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO) {

        changeSwitchDTO.verification();

        //插入一条数据到 开关数据变更记录表 默认初始化
        

        //更新switch value 到zk
        SwitchUtil.changeValue(changeSwitchDTO.getPath(),changeSwitchDTO.getFieldName(),changeSwitchDTO.getVal(),changeSwitchDTO.getOptId());


    }


    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
