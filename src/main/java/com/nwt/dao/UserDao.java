package com.nwt.dao;

import com.nwt.dao.interfaces.IUserDao;
import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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
}
