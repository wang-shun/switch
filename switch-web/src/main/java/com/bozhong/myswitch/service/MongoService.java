package com.bozhong.myswitch.service;

import com.bozhong.config.domain.JqPage;

import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
public interface MongoService {

    /**
     * @param t
     * @param <T>
     */
    <T> void insertOne(T t);

    /**
     * @param tList
     * @param tClass
     * @param <T>
     */
    <T> void insertMany(List<T> tList, Class<T> tClass);

    /**
     * @param optId
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T findOneByOptId(String optId, Class<T> tClass);

    /**
     * @param optId
     * @param tClass
     * @param <T>
     * @return
     */
    <T> List<T> findListByOptId(String optId, Class<T> tClass);

    /**
     * @param appId
     * @param env
     * @param optId
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T findOneByAppIdEnvOptId(String appId, String env, String optId, Class<T> tClass);

    /**
     * @param optId
     * @param fieldName
     * @param ip
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T findOneByOptIdFieldNameIp(String optId, String fieldName, String ip, Class<T> tClass);

    /**
     * @param optId
     * @param t
     * @param <T>
     */
    <T> void updateOneByOptId(String optId, T t);


    /**
     * @param optId
     * @param fieldName
     * @param ip
     * @param t
     * @param <T>
     */
    <T> void updateOneByOptIdFieldNameIp(String optId, String fieldName, String ip, T t);

    /**
     * @param jqPage
     * @param tClass
     * @param <T>
     * @return
     */
    <T> JqPage<T> getJqPage(JqPage<T> jqPage, Class<T> tClass);
}
