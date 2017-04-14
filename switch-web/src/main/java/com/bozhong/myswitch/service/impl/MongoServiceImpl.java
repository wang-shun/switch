package com.bozhong.myswitch.service.impl;

import com.bozhong.myswitch.dao.MongoDao;
import com.bozhong.myswitch.service.MongoService;

import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
public class MongoServiceImpl implements MongoService{
    private MongoDao mongoDao;

    @Override
    public <T> void insertOne(T t) {
        mongoDao.insertOne(t);
    }

    @Override
    public <T> void insertMany(List<T> list, Class<T> tClass) {
        mongoDao.insertMany(list, tClass);
    }

    @Override
    public <T> T findOneByOptId(String optId, Class<T> tClass) {
        return mongoDao.findOneByOptId(optId, tClass);
    }

    @Override
    public <T> void updateOneByOptId(String optId, T t) {
        mongoDao.updateOneByOptId(optId, t);
    }


    public void setMongoDao(MongoDao mongoDao) {
        this.mongoDao = mongoDao;
    }
}
