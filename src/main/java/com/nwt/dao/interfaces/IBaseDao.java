package com.nwt.dao.interfaces;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public interface IBaseDao<T>
{
    void persist(Object entity);

    void delete(Object entity);

    void saveOrUpdate(Object entity);

    T get(Integer id);

    List<T> getAll();
}
