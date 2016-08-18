package com.nwt.services;

import com.nwt.dao.interfaces.ICommentDao;
import com.nwt.dao.model.Comment;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Service
public class CommentService extends BaseService<Comment>
{
    @Autowired
    private ICommentDao commentDao;

    @Override
    public ICommentDao getDao()
    {
        return commentDao;
    }

    @Transactional(readOnly = true)
    public List<TaskComment> getTaskComments(Task task)
    {
        return getDao().getTaskComments(task);
    }
}
