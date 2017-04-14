package com.bozhong.myswitch.service;

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
}
