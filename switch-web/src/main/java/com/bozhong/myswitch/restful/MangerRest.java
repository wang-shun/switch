package com.bozhong.myswitch.restful;

import com.alibaba.fastjson.JSON;
import com.bozhong.config.domain.JqPage;
import com.bozhong.myswitch.common.SwitchLogger;
import com.bozhong.myswitch.domain.OptRecordDO;
import com.bozhong.myswitch.domain.SwitchValueChangDO;
import com.bozhong.myswitch.service.MongoService;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;
import com.yx.eweb.main.EWebRequestDTO;
import com.yx.eweb.main.EWebServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 17/4/12.
 */
@Controller
@Singleton
@Path("mangerRest")
public class MangerRest {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MongoService mongoService;

    @POST
    @Path("doChange")
    public String change(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeader) {
        return "doChange";
    }


    @POST
    @Path("doChangeAll")
    public String changeAll(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeader) {
        return "doChangeAll";
    }

    @POST
    @Path("callBack")
    public String callBack(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeader) {

        String optId = (String) EWebServletContext.getEWebContext().get("optId");
        String fieldName = (String) EWebServletContext.getEWebContext().get("fieldName");
        String ip = (String) EWebServletContext.getEWebContext().get("ip");
        SwitchLogger.getSysLogger().warn("MangerRest.callBack has excute ! optId: " + optId + " fieldName:" + fieldName + " ip:" + ip);
        SwitchValueChangDO switchValueChangDO = new SwitchValueChangDO();
        switchValueChangDO.setSyncResult(true);
        switchValueChangDO.setCallbackDT(SIMPLE_DATE_FORMAT.format(new Date()));
        mongoService.updateOneByOptIdFieldNameIp(optId, fieldName, ip, switchValueChangDO);
        System.out.println("回调");
        return "callBack";
    }

    @POST
    @Path("getOptRecordList")
    public String getOptRecordList(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        Map<String, Object> param = ((EWebRequestDTO) EWebServletContext.getEWebContext().getParam()).getRequestParam();
        String callBack = param.get("callback").toString();
        Integer page = Integer.valueOf(param.get("page").toString());
        Integer rows = Integer.valueOf(param.get("rows").toString());
        String appId = (String) param.get("appId");
        String fieldName = (String) param.get("fieldName");
        Gson gson = new Gson();
        JqPage<OptRecordDO> jqPage = new JqPage<OptRecordDO>();
        jqPage.setPage(page);
        jqPage.setPageSize(rows);
        return callBack + "(" + gson.toJson(mongoService.getJqPage(appId, fieldName, jqPage, OptRecordDO.class)) + ")";
    }

    @POST
    @Path("getSwitchValueChangeList")
    public String getSwitchValueChangeList(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        Map<String, Object> param = ((EWebRequestDTO) EWebServletContext.getEWebContext().getParam()).getRequestParam();
        String callBack = param.get("callback").toString();
        Integer page = Integer.valueOf(param.get("page").toString());
        Integer rows = Integer.valueOf(param.get("rows").toString());
        String appId = (String) param.get("appId");
        String ip = (String) param.get("ip");
        String fieldName = (String) param.get("fieldName");
        Gson gson = new Gson();
        JqPage<SwitchValueChangDO> jqPage = new JqPage<SwitchValueChangDO>();
        jqPage.setPage(page);
        jqPage.setPageSize(rows);
        return callBack + "(" + gson.toJson(mongoService.getJqPage(appId, fieldName, ip, jqPage, SwitchValueChangDO.class)) + ")";
    }

    @POST
    @Path("getOptRecord")
    public String getOptRecord(@Context Request request, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        Map<String, Object> param = ((EWebRequestDTO) EWebServletContext.getEWebContext().getParam()).getRequestParam();
        String appId = (String) param.get("appId");
        String env = (String) param.get("env");
        String optId = (String) param.get("optId");
        OptRecordDO optRecordDO = mongoService.findOneByAppIdEnvOptId(appId, env, optId, OptRecordDO.class);
        return optRecordDO.toString();
    }

    @POST
    @Path("getSwitchValueChange")
    public String getSwitchValueChange(@Context Request request, @Context UriInfo uriInfo,
                                       @Context HttpHeaders httpHeaders) {
        String optId = (String) EWebServletContext.getEWebContext().get("optId");
        String fieldName = (String) EWebServletContext.getEWebContext().get("fieldName");
        String ip = (String) EWebServletContext.getEWebContext().get("ip");
        SwitchValueChangDO switchValueChangDO = mongoService.findOneByOptIdFieldNameIp(optId, fieldName, ip,
                SwitchValueChangDO.class);
        return switchValueChangDO.toString();
    }

    @POST
    @Path("getSwitchValueChangeByListOptId")
    public String getSwitchValueChangeByListOptId(@Context Request request, @Context UriInfo uriInfo,
                                                  @Context HttpHeaders httpHeaders) {
        String optId = (String) EWebServletContext.getEWebContext().get("optId");
        List<SwitchValueChangDO> switchValueChangDOList = mongoService.findListByOptId(optId, SwitchValueChangDO.class);
        return JSON.toJSONString(switchValueChangDOList);

    }

}
