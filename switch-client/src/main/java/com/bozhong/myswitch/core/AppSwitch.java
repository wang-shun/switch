package com.bozhong.myswitch.core;

import java.lang.annotation.ElementType;

/**
 * Created by renyueliang on 17/4/10.
 */
@java.lang.annotation.Target({ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AppSwitch {
    String desc() default "";
    String type()  default "";
    String format() default "";
}
