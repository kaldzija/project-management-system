package com.nwt.dao;

import com.nwt.dao.interfaces.IUserDao;
import com.nwt.dao.model.Contact;
import com.nwt.dao.model.Notification;
import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
@Repository(UserDao.BEAN_NAME)
public class UserDao extends BaseDao<User> implements IUserDao
{
    public static final String BEAN_NAME = "userDao";

    public UserDao()
    {
        super(User.class);
    }

    @Override
    public User getRegistredByEmail(String email)
    {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("email", email)).add(Restrictions.isNull("socialType")).uniqueResult();
    }

    @Override
    public User getSocialUser(String socialId, SocialTypeEnum socialType)
    {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("socialId", socialId)).add(Restrictions.eq("socialType", socialType)).uniqueResult();
    }

    @Override
    public List<Contact> getUserContacts(User user)
    {
        Criteria criteria = getSession().createCriteria(Contact.class);
        criteria.add(Restrictions.or(Restrictions.eq("sender", user), Restrictions.eq("receiver", user)));
        return criteria.list();
    }

    @Override
    public List<Contact> getApprovedUserContacts(User user)
    {
        Criteria criteria = getSession().createCriteria(Contact.class);
        criteria.add(Restrictions.or(Restrictions.eq("sender", user), Restrictions.eq("receiver", user)));
        criteria.add(Restrictions.eq("approved", true));
        return criteria.list();
    }

    @Override
    public List<Notification> getUnreadNotifications(User user)
    {
        Criteria criteria = getSession().createCriteria(Notification.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("read", false));
        return criteria.list();
    }

    @Override
    public Notification getNotification(Integer id)
    {
        return getSession().get(Notification.class, id);
    }
}
