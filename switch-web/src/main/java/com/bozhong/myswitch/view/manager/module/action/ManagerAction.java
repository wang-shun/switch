package com.bozhong.myswitch.view.manager.module.action;

import com.bozhong.common.util.BeanUtil;
import com.bozhong.common.util.StringUtil;
import com.bozhong.common.util.UUIDUtil;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.domain.ChangeAllSwitchDTO;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.OptRecordDO;
import com.bozhong.myswitch.service.ManagerService;
import com.yx.eweb.main.EWebContext;
import com.yx.eweb.main.EWebRequestDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * Created by renyueliang on 17/4/12.
 */
@Controller
public class ManagerAction {

    @Autowired
    private ManagerService managerService;

    public void changeOneSwitchValue(EWebContext eWebContext){

      String  fieldName= (String) eWebContext.get("fieldName");
      String val=(String)eWebContext.get("val");
      String path = (String) eWebContext.get("path");
      String appId = (String) eWebContext.get("appId");
      String env = (String) eWebContext.get("env");

      if(StringUtil.isBlank(fieldName) || StringUtil.isBlank(val)
              || StringUtil.isBlank(path)){
          eWebContext.put("errorCode", SwitchErrorEnum.ILL_ARGMENT.getError());
          return ;
      }

        ChangeSwitchDTO changeSwitchDTO = new ChangeSwitchDTO();
        changeSwitchDTO.setFieldName(fieldName);
        changeSwitchDTO.setOptId(UUID.randomUUID().toString());
        changeSwitchDTO.setPath(path);
        changeSwitchDTO.setVal(val);

        OptRecordDO optRecordDO = new OptRecordDO();
        BeanUtils.copyProperties(changeSwitchDTO, optRecordDO);
        optRecordDO.setAppId(appId);
        optRecordDO.setEnv(env);
        managerService.recordOpt(optRecordDO);
        managerService.changeSwitchValue(changeSwitchDTO);

    }


    public void changeAllSwitchValue(EWebContext eWebContext){

        ChangeAllSwitchDTO changeAllSwitchDTO =  BeanUtil.mapToObject(((EWebRequestDTO)eWebContext.getParam()).getRequestParam(),ChangeAllSwitchDTO.class);
        changeAllSwitchDTO.setOptId(UUIDUtil.getUUID(true));

        OptRecordDO optRecordDO = new OptRecordDO();
        BeanUtils.copyProperties(changeAllSwitchDTO, optRecordDO);
        managerService.recordOpt(optRecordDO);
        managerService.changeAllSwitchValue(changeAllSwitchDTO);

    }


}
