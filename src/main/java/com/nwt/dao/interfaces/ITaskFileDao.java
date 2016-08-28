package com.nwt.dao.interfaces;

import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskFile;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public interface ITaskFileDao extends IBaseDao
{
    List<TaskFile> getTaskFiles(Task task);
}
