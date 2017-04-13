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
    public static String NAME;

    /**
     *
     */
    @AppSwitch(type = "INT",desc = "年龄")
    public static int AGE;

    /**
     *
     */
    @AppSwitch(type = "long",desc = "身高")
    public static long HEIGHT;

    /**
     *
     */
    @AppSwitch(type = "boolean",desc = "性别")
    public static boolean SEX;

}
