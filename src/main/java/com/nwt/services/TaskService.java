package com.nwt.services;

import com.nwt.dao.interfaces.IBaseDao;
import com.nwt.dao.interfaces.ITaskDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 12.08.2016..
 */
@Service
public class TaskService extends BaseService<Task>
{
    @Autowired
    private ITaskDao taskDao;

    @Override
    public IBaseDao getDao()
    {
        return taskDao;
    }

    @Transactional(readOnly = true)
    public List<Task> getProjectTasks(Project project)
    {
        return taskDao.getProjectTasks(project);
    }
}
