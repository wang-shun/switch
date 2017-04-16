package com.bozhong.myswitch.dao.impl;

import com.bozhong.config.common.MongoDBConfig;
import com.bozhong.config.domain.JqPage;
import com.bozhong.myswitch.dao.MongoDao;
import com.bozhong.myswitch.domain.AppDO;
import com.bozhong.myswitch.service.AppService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.yx.eweb.main.EWebServletContext;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
public class MongoDaoImpl implements MongoDao {
    @Autowired
    private MongoDBConfig mongoDBConfig;

    @Autowired
    private AppService appService;

    @Override
    public <T> void insertOne(T t) {
        Gson gson = new Gson();
        Document document = gson.fromJson(t.toString(), Document.class);
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(t.getClass());
        mongoCollection.insertOne(document);
    }

    @Override
    public <T> void insertMany(List<T> tlist, Class<T> tClass) {
        Gson gson = new Gson();
        List<Document> documentList = gson.fromJson(gson.toJson(tlist), new TypeToken<List<Document>>() {
        }.getType());
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        mongoCollection.insertMany(documentList);
    }

    @Override
    public <T> T findOneByOptId(String optId, Class<T> tClass) {
        Gson gson = new Gson();
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        Document document = mongoCollection.find(eq("optId", optId)).first();
        if (document != null) {
            return gson.fromJson(document.toJson(), tClass);
        }
        return null;
    }

    @Override
    public <T> List<T> findListByOptId(String optId, Class<T> tClass) {
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        FindIterable<Document> findIterable = null;
        findIterable = mongoCollection.find(eq("optId", optId));
        Iterator<Document> iterator = findIterable.iterator();
        List<T> rows = new ArrayList<T>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            rows.add(gson.fromJson(document.toJson(), tClass));
        }
        return rows;
    }

    @Override
    public <T> T findOneByAppIdEnvOptId(String appId, String env, String optId, Class<T> tClass) {
        Gson gson = new Gson();
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        Document document = mongoCollection.find(and(eq("appId", appId), eq("env", env),
                eq("optId", optId))).first();
        if (document != null) {
            return gson.fromJson(document.toJson(), tClass);
        }
        return null;
    }

    @Override
    public <T> T findOneByOptIdFieldNameIp(String optId, String fieldName, String ip, Class<T> tClass) {
        Gson gson = new Gson();
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        Document document = mongoCollection.find(and(eq("optId", optId), eq("fieldName", fieldName),
                eq("ip", ip))).first();
        if (document != null) {
            return gson.fromJson(document.toJson(), tClass);
        }
        return null;
    }

    @Override
    public <T> void updateOneByOptId(String optId, T t) {
        Gson gson = new Gson();
        Document document = gson.fromJson(t.toString(), Document.class);
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(t.getClass());
        mongoCollection.updateOne(eq("optId", optId), new Document("$set", document));
    }

    @Override
    public <T> void updateOneByOptIdFieldNameIp(String optId, String fieldName, String ip, T t) {
        Gson gson = new Gson();
        Document document = gson.fromJson(t.toString(), Document.class);
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(t.getClass());
        mongoCollection.updateOne(and(eq("optId", optId), eq("fieldName", fieldName),
                eq("ip", ip)), new Document("$set", document));
    }

    @Override
    public <T> JqPage<T> getJqPage(JqPage<T> jqPage, Class<T> tClass) {
        List<AppDO> appDOList = appService.getAppsByUid((String) EWebServletContext.getRequest().getAttribute("uId"));
        if (CollectionUtils.isEmpty(appDOList)) {
            return jqPage;
        }

        String[] appIdArray = new String[appDOList.size()];
        int i = 0;
        for (AppDO appDO : appDOList) {
            appIdArray[i++] = appDO.getAppId();
        }

        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        FindIterable<Document> findIterable = null;
        findIterable = mongoCollection.find(in("appId", appIdArray)).sort(descending("createDt")).
                skip(jqPage.getFromIndex()).limit(jqPage.getPageSize());
        Iterator<Document> iterator = findIterable.iterator();
        List<T> rows = new ArrayList<T>(jqPage.getPageSize());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            rows.add(gson.fromJson(document.toJson(), tClass));
        }
        jqPage.setRecords((int) mongoCollection.count(in("appId", appIdArray)));
        jqPage.setRows(rows);
        return jqPage;
    }

    @Override
    public <T> JqPage<T> getJqPage(String appId, String fieldName, JqPage<T> jqPage, Class<T> tClass) {
        List<AppDO> appDOList = appService.getAppsByUid((String) EWebServletContext.getRequest().getAttribute("uId"));
        if (CollectionUtils.isEmpty(appDOList)) {
            return jqPage;
        }

        String[] appIdArray = new String[appDOList.size()];
        int i = 0;
        for (AppDO appDO : appDOList) {
            appIdArray[i++] = appDO.getAppId();
        }

        Bson bson = in("appId", appIdArray);
        if (StringUtils.hasText(appId)) {
            bson = and(bson, eq("appId", appId));
        }

        if (StringUtils.hasText(fieldName)) {
            bson = and(bson, regex("fieldName", "^.*" + fieldName + ".*$"));
        }

        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        FindIterable<Document> findIterable = null;
        findIterable = mongoCollection.find(bson).sort(descending("createDt")).
                skip(jqPage.getFromIndex()).limit(jqPage.getPageSize());
        Iterator<Document> iterator = findIterable.iterator();
        List<T> rows = new ArrayList<T>(jqPage.getPageSize());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            rows.add(gson.fromJson(document.toJson(), tClass));
        }
        jqPage.setRecords((int) mongoCollection.count(bson));
        jqPage.setRows(rows);
        return jqPage;
    }
}
