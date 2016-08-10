package com.nwt.services;

import com.nwt.dao.interfaces.IUserDao;
import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
@Service
public class UserService
{
    @Autowired
    IUserDao userDao;

    @Transactional
    public void saveOrUpdate(User user)
    {
        userDao.saveOrUpdate(user);
    }

    @Transactional(readOnly = true)
    public User getRegistredByEmail(String email)
    {
        return userDao.getRegistredByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUser(Integer id)
    {
        return (User) userDao.get(id);
    }

    @Transactional
    public User getSocialUser(String socialId, SocialTypeEnum socialType)
    {
        return userDao.getSocialUser(socialId, socialType);
    }
}
