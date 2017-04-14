package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.domain.*;
import com.bozhong.myswitch.exception.SwitchException;
import com.bozhong.myswitch.service.AppService;
import com.bozhong.myswitch.service.ManagerService;
import com.bozhong.myswitch.service.MongoService;
import com.bozhong.myswitch.zookeeper.ZkClient;
import com.yx.eweb.main.EWebServletContext;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void recordSwitchValueChange(SwitchValueChangDO switchValueChangDO) {
        switchValueChangDO.setCreateBy((String) EWebServletContext.getRequest().getAttribute("uId"));
        switchValueChangDO.setCreateDt(SIMPLE_DATE_FORMAT.format(new Date()));
        switchValueChangDO.setCallbackDT(switchValueChangDO.getCreateDt());
        mongoService.insertOne(switchValueChangDO);
    }

    @Override
    public void updateSwitchValueChange(SwitchValueChangDO switchValueChangDO) {
        mongoService.updateOneByOptId(switchValueChangDO.getOptId(), switchValueChangDO);
    }

    @Override
    public void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO) {

        changeSwitchDTO.verification();

        //插入一条数据到 开关数据变更记录表 默认初始化
        SwitchValueChangDO switchValueChangDO = new SwitchValueChangDO();
        BeanUtils.copyProperties(changeSwitchDTO, switchValueChangDO);
        try {
            switchValueChangDO.setIp(changeSwitchDTO.getPath().substring(changeSwitchDTO.getPath().lastIndexOf("/")+1));
        } catch (Throwable e) {
            SwitchLogger.getLogger().error(e.getMessage());
        }
        switchValueChangDO.setSyncResult(false);
        recordSwitchValueChange(switchValueChangDO);

        //更新switch value 到zk
        SwitchUtil.changeValue(changeSwitchDTO.getPath(), changeSwitchDTO.getFieldName(), changeSwitchDTO.getVal(), changeSwitchDTO.getOptId());
        //switchValueChangDO.setSyncResult(true);
        //switchValueChangDO.setCallbackDT(SIMPLE_DATE_FORMAT.format(new Date()));
        //updateSwitchValueChange(switchValueChangDO);
    }


    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
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


    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
