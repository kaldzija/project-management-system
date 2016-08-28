package com.nwt.dao.interfaces;

import com.nwt.dao.model.Project;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.User;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface ITaskDao extends IBaseDao
{

    List<Task> getProjectTasks(Project project);

    List<Task> getUserProjectTasks(Project project, User user);
}
