package com.bozhong.myswitch.pipeline;

import com.yx.eweb.main.PipeLineInter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SecurityPipeLine implements PipeLineInter {

    @Override
    public boolean run(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return true;
    }
}
