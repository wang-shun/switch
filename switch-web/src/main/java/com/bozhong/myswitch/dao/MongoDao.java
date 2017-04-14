package com.bozhong.myswitch.dao;

import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/4/14 0014.
 */
public interface MongoDao {
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
}
