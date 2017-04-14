package com.bozhong.myswitch.service;

import com.bozhong.myswitch.domain.ChangeAllSwitchDTO;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;
import com.bozhong.myswitch.domain.OptRecordDO;
import com.bozhong.myswitch.domain.SwitchValueChangDO;

/**
 * Created by renyueliang on 17/4/12.
 */
public interface ManagerService {

    public void recordOpt();

    public void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO);

    void updateSwitchValueChange(SwitchValueChangDO switchValueChangDO);

    void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO);
    public void changeAllSwitchValue(ChangeAllSwitchDTO changeAllSwitchDTO ) ;



}
