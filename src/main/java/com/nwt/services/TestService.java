package com.nwt.services;

import com.nwt.dao.interfaces.ITestDao;
import com.nwt.dao.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Service
public class TestService
{
    @Autowired
    private ITestDao testDao;

    @Transactional(readOnly = true)
    public List<Test> getAll()
    {
        return testDao.getAll();
    }

    @Transactional
    public void create(Test test)
    {
        testDao.saveOrUpdate(test);
    }
}
