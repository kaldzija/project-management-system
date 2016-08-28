package com.nwt.services;

import com.nwt.dao.interfaces.ITaskFileDao;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Service
public class TaskFileService extends BaseService<TaskFile>
{
    @Autowired
    private ITaskFileDao taskFileDao;

    @Override
    public ITaskFileDao getDao()
    {
        return taskFileDao;
    }

    @Transactional
    public List<TaskFile> getTaskFiles(Task task)
    {
        return getDao().getTaskFiles(task);
    }
}
