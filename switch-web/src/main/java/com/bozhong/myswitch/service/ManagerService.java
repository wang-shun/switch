package com.bozhong.myswitch.service;

import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.OptRecordDO;
import com.bozhong.myswitch.domain.SwitchValueChangDO;

/**
 * Created by renyueliang on 17/4/12.
 */
public interface ManagerService {

    void recordOpt(OptRecordDO optRecordDO);

    void recordSwitchValueChange(SwitchValueChangDO switchValueChangDO);

    void updateSwitchValueChange(SwitchValueChangDO switchValueChangDO);

    void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO);


}
