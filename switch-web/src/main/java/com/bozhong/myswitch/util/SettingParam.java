package com.bozhong.myswitch.util;

import com.bozhong.myswitch.core.AppSwitch;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SettingParam {

    /**
     *
     */
    @AppSwitch(type = "string",desc = "姓名")
    public static String NAME="RENYL";

    /**
     *
     */
    @AppSwitch(type = "int",desc = "年龄year")
    public static int AGE=34;

    /**
     *
     */
    @AppSwitch(type = "long",desc = "身高cm")
    public static long HEIGHT=175;

    /**
     *
     */
    @AppSwitch(type = "boolean",desc = "性别 true男 false 女")
    public static boolean SEX=true;

}
