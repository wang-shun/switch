package com.bozhong.myswitch.view.manager.module.screen;

import com.yx.eweb.main.EWebContext;
import com.yx.eweb.main.ScreenInter;
import org.springframework.stereotype.Controller;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
@Controller
public class OperationView implements ScreenInter {
    @Override
    public void excute(EWebContext eWebContext) {
        eWebContext.put("menu", this.getClass().getSimpleName());
    }
}
