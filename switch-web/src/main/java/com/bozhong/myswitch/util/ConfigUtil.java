package com.bozhong.myswitch.util;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.core.Environ;
import com.bozhong.myswitch.exception.SwitchException;

/**
 * Created by renyueliang on 17/4/12.
 */
public class ConfigUtil {

    private static int PORT=8080;

    private static String ENRION;

    public static int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        ConfigUtil.PORT = PORT;
    }

    public static String getENRION(){
        return ENRION;
    }

    public void setENRION(String ENRION) {
        if(StringUtil.isBlank(ENRION)){
            throw new SwitchException(SwitchErrorEnum.ILL_ARGMENT.getError(),"ENRION IS NULL");
        }

        ConfigUtil.ENRION = ENRION;
    }


    public static boolean isOnline(){

        if(Environ.ONLINE.name().equals(ENRION) || Environ.UAT.name().equals(ENRION)){
            return true;
        }

        return false;

    }
}
