package com.nwt.dao.interfaces;

import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskComment;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public interface ICommentDao extends IBaseDao
{
    List<TaskComment> getTaskComments(Task task);
}
