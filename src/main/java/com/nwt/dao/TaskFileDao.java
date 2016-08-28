package com.nwt.dao;

import com.nwt.dao.interfaces.ITaskFileDao;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskFile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Repository(value = TaskFileDao.BEAN_NAME)
public class TaskFileDao extends BaseDao<TaskFile> implements ITaskFileDao
{
    public static final String BEAN_NAME = "taskFileDao";

    public TaskFileDao()
    {
        super(TaskFile.class);
    }

    @Override
    public List<TaskFile> getTaskFiles(Task task)
    {
        Criteria criteria = getSession().createCriteria(TaskFile.class);
        criteria.add(Restrictions.eq("task", task));
        return criteria.list();
    }
}
