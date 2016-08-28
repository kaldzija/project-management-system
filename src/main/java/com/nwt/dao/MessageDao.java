package com.nwt.dao;

import com.nwt.dao.interfaces.IMessageDao;
import com.nwt.dao.model.Message;
import com.nwt.dao.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 12.08.2016..
 */
@Repository(MessageDao.BEAN_NAME)
public class MessageDao extends BaseDao<Message> implements IMessageDao
{
    public static final String BEAN_NAME = "messageDao";

    public MessageDao()
    {
        super(Message.class);
    }

    @Override
    public List<Message> getMessages(User u1, User u2)
    {
        Criteria criteria = getSession().createCriteria(Message.class);
        LogicalExpression r1 = Restrictions.and(Restrictions.eq("fromUser", u1), Restrictions.eq("toUser", u2));
        LogicalExpression r2 = Restrictions.and(Restrictions.eq("fromUser", u2), Restrictions.eq("toUser", u1));
        criteria.add(Restrictions.or(r1, r2));
        return criteria.list();
    }

    @Override
    public List<Message> getUnreadMessages(User u1)
    {
        Criteria criteria = getSession().createCriteria(Message.class);
        criteria.add(Restrictions.eq("toUser", u1));
        criteria.add(Restrictions.eq("read", false));

        return criteria.list();
    }
}
