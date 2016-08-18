package com.nwt.dao;

import com.nwt.dao.interfaces.ICommentDao;
import com.nwt.dao.model.Comment;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskComment;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Repository(value = CommentDao.BEAN_NAME)
public class CommentDao extends BaseDao<Comment> implements ICommentDao
{
    public static final String BEAN_NAME = "commentDao";

    public CommentDao()
    {
        super(Comment.class);
    }

    @Override
    public List<TaskComment> getTaskComments(Task task)
    {
        return getSession().createCriteria(TaskComment.class).add(Restrictions.eq("task", task)).list();
    }
}
