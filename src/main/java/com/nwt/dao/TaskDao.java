package com.nwt.dao;

import com.nwt.dao.interfaces.ITaskDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 12.08.2016..
 */
@Repository(TaskDao.BEAN_NAME)
public class TaskDao extends BaseDao<Task> implements ITaskDao
{
    public static final String BEAN_NAME = "taskDao";

    public TaskDao()
    {
        super(Task.class);
    }

    @Override
    public List<Task> getProjectTasks(Project project)
    {
        Criteria criteria = getSession().createCriteria(Task.class);
        criteria.add(Restrictions.eq("project", project));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
