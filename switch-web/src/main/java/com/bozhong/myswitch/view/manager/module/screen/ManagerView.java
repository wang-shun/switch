package com.bozhong.myswitch.view.manager.module.screen;

import com.bozhong.common.util.StringUtil;
import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.common.SwitchUtil;
import com.bozhong.myswitch.core.Environ;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.domain.SwitchNodeDTO;
import com.bozhong.myswitch.zookeeper.ZkClient;
import com.yx.eweb.main.EWebContext;
import com.yx.eweb.main.ScreenInter;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by renyueliang on 17/4/12.
 */
@Controller
public class ManagerView implements ScreenInter {

    @Override
    public void excute(EWebContext eWebContext) {
        eWebContext.put("menu", this.getClass().getSimpleName());

        String appId = (String) eWebContext.get("appId");
        if (!StringUtils.hasText(appId)){
            AppDO appDO = (AppDO) eWebContext.getRequest().getAttribute("appDO");
            if (appDO != null) {
                appId = appDO.getAppId();
            }
        }
        String env = (String) eWebContext.get("env");
        if (!StringUtils.hasText(env)){
            env = (String) eWebContext.getRequest().getAttribute("env");
        }

        if(StringUtil.isBlank(appId) ||
                StringUtil.isBlank(env)  ){

            eWebContext.put("errorCode",SwitchErrorEnum.ILL_ARGMENT.getError());

            return ;
        }

        if(Environ.getEnviron(env)==null){
            eWebContext.put("errorCode",SwitchErrorEnum.ENRION_CODE_ERROR.getError());
            return ;
        }

        String appPath = SwitchUtil.createPathForApp(appId, env);

        try {
           List<SwitchNodeDTO> list = ZkClient.getInstance().getChildrenNode(SwitchConstants.SWITCH_ROOT_PATH+
                    SwitchConstants.SLASH+env

            );

            eWebContext.put("list",list);

            List<SwitchNodeDTO> switchNodeDTOList = ZkClient.getInstance().getChildrenNode(appPath);

            eWebContext.put("switchNodeList",switchNodeDTOList);
        } catch (Throwable e) {
            SwitchLogger.getLogger().error(SwitchErrorEnum.GET_APP_ZK_NODE_ERROR.getError(),e);

        }



    }
}
