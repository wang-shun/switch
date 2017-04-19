package com.bozhong.myswitch.restful;

import com.bozhong.common.util.ResultMessageBuilder;
import com.bozhong.common.util.StringUtil;
import com.bozhong.config.common.LDAPConnectionConfig;
import com.bozhong.config.exception.ConfigCenterLoginCodeEnum;
import com.bozhong.myredis.MyRedisClusterForHessian;
import com.bozhong.myswitch.common.SwitchErrorEnum;
import com.bozhong.myswitch.common.SwitchConstants;
import com.bozhong.myswitch.util.RSAHelper;
import com.google.gson.Gson;
import com.novell.ldap.LDAPException;
import com.sun.jersey.spi.resource.Singleton;
import com.yx.eweb.main.EWebRequestDTO;
import com.yx.eweb.main.EWebServletContext;
import com.zhicall.core.util.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.*;

/**
 * Created by renyueliang on 16/12/29.
 */
@Controller
@Singleton
@Path("login")
public class loginRest {

    private static final Logger logger = Logger.getRootLogger();
    @Autowired
    private LDAPConnectionConfig ldapConnectionConfig;

    @Autowired
    private MyRedisClusterForHessian myRedisClusterForHessian;

    @POST
    @Path("generatePublicKey")
    public String generatePublicKey(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        if (!myRedisClusterForHessian.hasKey(SwitchConstants.SWITCH_CENTER_PUBLIC_PRIVATE_KEY)) {
            try {
                KeyPair keyPair = RSAHelper.generateKeyPair();
                Map<String, String> map = new HashMap<>();
                map.put(SwitchConstants.PUBLIC_KEY, RSAHelper.getKeyString(keyPair.getPublic()));
                map.put(SwitchConstants.PRIVATE_KEY, RSAHelper.getKeyString(keyPair.getPrivate()));
                myRedisClusterForHessian.put(SwitchConstants.SWITCH_CENTER_PUBLIC_PRIVATE_KEY, map);
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

        Map keyMap = myRedisClusterForHessian.get(SwitchConstants.SWITCH_CENTER_PUBLIC_PRIVATE_KEY, HashMap.class);

        return ResultMessageBuilder.build(keyMap.get(SwitchConstants.PUBLIC_KEY)).toJSONString();
    }

    @POST
    @Path("checkLogin")
    public String checkLogin(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        Map<String, Object> param = ((EWebRequestDTO) EWebServletContext.getEWebContext().getParam()).getRequestParam();
        Gson gson = new Gson();
        String userName = (String) param.get("userName");
        if (StringUtil.isBlank(userName)) {
            return gson.toJson(ConfigCenterLoginCodeEnum.LOGIN_FAIL_NO_USERNAME.toString());
        }

        String password = (String) param.get("password");

        if (StringUtil.isBlank(password)) {
            return gson.toJson(ConfigCenterLoginCodeEnum.LOGIN_FAIL_NO_PASSWORD.toString());
        }

        Map keyMap = myRedisClusterForHessian.get(SwitchConstants.SWITCH_CENTER_PUBLIC_PRIVATE_KEY, HashMap.class);
        if (keyMap == null) {
            return gson.toJson(SwitchErrorEnum.LOGIN_FAIL_PUBLIC_PRIVATE_KEY_EXPIRE.toString());
        }

        try {
            PrivateKey privateKey = RSAHelper.getPrivateKey((String) keyMap.get(SwitchConstants.PRIVATE_KEY));
            password = RSAHelper.decrypt(privateKey, (new BASE64Decoder()).decodeBuffer(password));
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }

        boolean flag = false;
        try {
            flag = ldapConnectionConfig.checkCanLDAPLogin(userName, password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (LDAPException e) {
            e.printStackTrace();
        }

        if (flag) {

            String token = MD5.sign(userName + password);
            int expireTimeSecond = 2 * 60 * 60;
            long expireTimeMilSecond = expireTimeSecond * 1000;
            myRedisClusterForHessian.putForStr(SwitchConstants.SWITCH_CENTER_USERNAME_PREFIX + token, userName, expireTimeMilSecond);
            Cookie cookie = new Cookie("switch_token", token);
            cookie.setMaxAge(expireTimeSecond);
            cookie.setPath("/");
            EWebServletContext.getEWebContext().getResponse().addCookie(cookie);

            return gson.toJson(ConfigCenterLoginCodeEnum.LOGIN_SUCCESS.toString());
        }

        return gson.toJson(ConfigCenterLoginCodeEnum.LOGIN_FAIL.toString());

    }

}
