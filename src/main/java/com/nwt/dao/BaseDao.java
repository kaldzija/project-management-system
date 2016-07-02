package com.nwt.dao;

import com.nwt.dao.interfaces.IBaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public abstract class BaseDao<T> implements IBaseDao
{
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public BaseDao(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    protected Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void persist(Object entity)
    {
        getSession().persist(entity);
    }

    @Override
    public void delete(Object entity)
    {
        getSession().delete(entity);
    }

    @Override
    public void saveOrUpdate(Object entity)
    {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public T get(Integer id)
    {
        return id != null ? getSession().get(clazz, id) : null;
    }

    @Override
    public List<T> getAll()
    {
        return getSession().createCriteria(clazz).list();
    }

}
