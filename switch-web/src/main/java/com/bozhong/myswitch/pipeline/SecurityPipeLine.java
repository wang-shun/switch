package com.bozhong.myswitch.pipeline;

import com.bozhong.common.util.StringUtil;
import com.bozhong.config.util.CookiesUtil;
import com.bozhong.myredis.MyRedisClusterForHessian;
import com.bozhong.myswitch.consts.SwitchConstants;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.service.AppService;
import com.bozhong.myswitch.util.ConfigUtil;
import com.yx.eweb.main.PipeLineInter;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by renyueliang on 16/12/29.
 */
public class SecurityPipeLine implements PipeLineInter {

    private static final Logger logger = Logger.getLogger(SecurityPipeLine.class);

    private MyRedisClusterForHessian myRedisClusterForHessian;

    private AppService appService;

    @Override
    public boolean run(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.warn("SecurityPipeLine has excute ! ");
        Cookie tokenCookie = CookiesUtil.getCookieByName(httpServletRequest, "token");
        if (tokenCookie == null) {
            try {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() +
                        "/manager/login.htm");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        String token = tokenCookie.getValue();
        String uId = myRedisClusterForHessian.getForStr(SwitchConstants.SWITCH_CENTER_USERNAME_PREFIX + token);
        if (StringUtil.isNotBlank(uId)) {
            httpServletRequest.setAttribute("uId", uId);
            List<AppDO> appDOList = appService.getAppsByUid(uId);
            String appId = httpServletRequest.getParameter("appId");
            if (!CollectionUtils.isEmpty(appDOList) && StringUtil.isNotBlank(appId)) {
                for (AppDO appDO : appDOList) {
                    if (appId.equals(appDO.getAppId())) {
                        appDO.setMainSelectType(true);
                        httpServletRequest.setAttribute("appDO", appDO);
                        break;
                    }
                }
            } else if (!CollectionUtils.isEmpty(appDOList)) {
                appDOList.get(0).setMainSelectType(true);
                httpServletRequest.setAttribute("appDO", appDOList.get(0));

            }

            httpServletRequest.setAttribute("appDOList", appDOList);
            httpServletRequest.setAttribute("isOnline", ConfigUtil.isOnline());
            return true;
        }

        try {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() +
                    "/manager/login.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public MyRedisClusterForHessian getMyRedisClusterForHessian() {
        return myRedisClusterForHessian;
    }

    public void setMyRedisClusterForHessian(MyRedisClusterForHessian myRedisClusterForHessian) {
        this.myRedisClusterForHessian = myRedisClusterForHessian;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
