package com.bozhong.myswitch.service;

import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.OptRecordDO;

/**
 * Created by renyueliang on 17/4/12.
 */
public interface ManagerService {

    void recordOpt(OptRecordDO optRecordDO);

    void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO);


}
