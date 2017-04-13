package com.bozhong.myswitch.common;

import org.apache.log4j.Logger;

/**
 * Created by renyueliang on 17/4/10.
 */
public class SwitchLogger {

    public static Logger getLogger(){
        return Logger.getLogger("switch");
    }

    public static  Logger getSysLogger(){
        return   Logger.getLogger(SwitchLogger.class);
    }
}
