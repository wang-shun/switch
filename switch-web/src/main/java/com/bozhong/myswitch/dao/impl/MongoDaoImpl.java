package com.bozhong.myswitch.dao.impl;

import com.bozhong.config.common.MongoDBConfig;
import com.bozhong.config.domain.JqPage;
import com.bozhong.myswitch.dao.MongoDao;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

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
        MongoCollection<Document> mongoCollection = mongoDBConfig.getCollection(tClass);
        FindIterable<Document> findIterable = null;
        findIterable = mongoCollection.find().sort(descending("createDt")).skip(jqPage.getFromIndex()).limit(jqPage.getPageSize());
        Iterator<Document> iterator = findIterable.iterator();
        List<T> rows = new ArrayList<T>(jqPage.getPageSize());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            rows.add(gson.fromJson(document.toJson(), tClass));
        }
        jqPage.setRecords((int) mongoCollection.count());
        jqPage.setRows(rows);
        return jqPage;
    }
}
