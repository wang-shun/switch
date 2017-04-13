package com.bozhong.myswitch.restful;

import com.bozhong.myswitch.common.SwitchLogger;
import com.sun.jersey.spi.resource.Singleton;
import com.yx.eweb.main.EWebServletContext;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Created by renyueliang on 17/4/12.
 */
@Controller
@Singleton
@Path("mangerRest")
public class MangerRest {


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

        SwitchLogger.getSysLogger().warn("MangerRest.callBack has excute ! optId: "+optId+" fieldName:"+fieldName);
        return "callBack";
    }


}
