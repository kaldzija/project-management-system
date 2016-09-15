package com.nwt.services;

import com.nwt.dao.interfaces.IBaseDao;
import com.nwt.dao.interfaces.ITaskDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskStatusEnum;
import com.nwt.dao.model.User;
import com.nwt.other.ProjectCompletedPercentage;
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

    @Transactional(readOnly = true)
    public List<Task> getUserProjectTasks(Project project, User user)
    {
        return taskDao.getUserProjectTasks(project, user);
    }

    @Transactional(readOnly = true)
    public ProjectCompletedPercentage getPercentage(Project project)
    {
        Integer total;
        Integer completed = 0;
        List<Task> tasks = taskDao.getProjectTasks(project);
        ProjectCompletedPercentage projectCompletedPercentage = new ProjectCompletedPercentage();
        total = tasks.size();
        projectCompletedPercentage.setPercentage(100);

        if (total.equals(0)) return projectCompletedPercentage;

        for (Task task : tasks)
        {
            if (task.getStatus().equals(TaskStatusEnum.COMPLETED)) completed++;
        }

        projectCompletedPercentage.setPercentage(completed * 100 / total);
        return projectCompletedPercentage;
    }
}
