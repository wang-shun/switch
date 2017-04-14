package com.bozhong.myswitch.service;

import com.bozhong.myswitch.domain.ChangeAllSwitchDTO;
import com.bozhong.myswitch.domain.ChangeSwitchDTO;

/**
 * Created by renyueliang on 17/4/12.
 */
public interface ManagerService {

    public void recordOpt();

    public void changeSwitchValue(ChangeSwitchDTO changeSwitchDTO);

    public void changeAllSwitchValue(ChangeAllSwitchDTO changeAllSwitchDTO ) ;



}
