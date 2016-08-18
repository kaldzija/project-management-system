package com.nwt.services;

import com.nwt.dao.interfaces.IBaseDao;
import com.nwt.dao.model.Project;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 12.08.2016..
 */
public abstract class BaseService<T>
{
    public abstract IBaseDao getDao();

    @Transactional(readOnly = true)
    public T get(Integer id)
    {
        return (T) getDao().get(id);
    }

    @Transactional(readOnly = true)
    public List<Project> getAll()
    {
        return getDao().getAll();
    }

    @Transactional
    public void saveOrUpdate(Object object)
    {
        getDao().saveOrUpdate(object);
    }

    @Transactional
    public void delete(Object object)
    {
        getDao().delete(object);
    }

    @Transactional
    public void persist(Object object)
    {
        getDao().persist(object);
    }
}
