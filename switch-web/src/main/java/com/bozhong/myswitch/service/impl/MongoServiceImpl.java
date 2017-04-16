package com.bozhong.myswitch.service.impl;

import com.bozhong.config.domain.JqPage;
import com.bozhong.myswitch.dao.MongoDao;
import com.bozhong.myswitch.service.MongoService;

import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
public class MongoServiceImpl implements MongoService {
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
    public <T> List<T> findListByOptId(String optId, Class<T> tClass) {
        return mongoDao.findListByOptId(optId, tClass);
    }

    @Override
    public <T> T findOneByAppIdEnvOptId(String appId, String env, String optId, Class<T> tClass) {
        return mongoDao.findOneByAppIdEnvOptId(appId, env, optId, tClass);
    }

    @Override
    public <T> T findOneByOptIdFieldNameIp(String optId, String fieldName, String ip, Class<T> tClass) {
        return mongoDao.findOneByOptIdFieldNameIp(optId, fieldName, ip, tClass);
    }

    @Override
    public <T> void updateOneByOptId(String optId, T t) {
        mongoDao.updateOneByOptId(optId, t);
    }

    @Override
    public <T> void updateOneByOptIdFieldNameIp(String optId, String fieldName, String ip, T t) {
        mongoDao.updateOneByOptIdFieldNameIp(optId, fieldName, ip, t);
    }

    @Override
    public <T> JqPage<T> getJqPage(JqPage<T> jqPage, Class<T> tClass) {
        return mongoDao.getJqPage(jqPage, tClass);
    }

    @Override
    public <T> JqPage<T> getJqPage(String appId, String fieldName, JqPage<T> jqPage, Class<T> tClass) {
        return mongoDao.getJqPage(appId, fieldName, jqPage, tClass);
    }


    public void setMongoDao(MongoDao mongoDao) {
        this.mongoDao = mongoDao;
    }
}
