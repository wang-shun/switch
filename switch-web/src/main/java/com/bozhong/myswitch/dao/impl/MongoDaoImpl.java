package com.bozhong.myswitch.dao.impl;

import com.bozhong.config.common.MongoDBConfig;
import com.bozhong.myswitch.dao.MongoDao;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

}
