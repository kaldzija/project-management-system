package com.nwt.dao;

import com.nwt.dao.interfaces.ITestDao;
import com.nwt.dao.model.Test;
import org.springframework.stereotype.Repository;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Repository(TestDao.BEAN_NAME)
public class TestDao extends BaseDao<Test> implements ITestDao
{
    public static final String BEAN_NAME = "testDao";

    public TestDao()
    {
        super(Test.class);
    }
}
