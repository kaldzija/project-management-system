package com.nwt.services;

import com.nwt.dao.interfaces.IUserDao;
import com.nwt.dao.model.Contact;
import com.nwt.dao.model.Notification;
import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
@Service
public class UserService
{
    @Autowired
    IUserDao userDao;

    @Transactional
    public void saveOrUpdate(Object object)
    {
        userDao.saveOrUpdate(object);
    }

    @Transactional(readOnly = true)
    public User getRegisteredByEmail(String email)
    {
        return userDao.getRegisteredByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email)
    {
        return userDao.getUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUser(Integer id)
    {
        return (User) userDao.get(id);
    }

    @Transactional
    public List<User> getAll()
    {
        return userDao.getAll();
    }

    @Transactional
    public User getSocialUser(String socialId, SocialTypeEnum socialType)
    {
        return userDao.getSocialUser(socialId, socialType);
    }

    @Transactional(readOnly = true)
    public List<Contact> getUserContacts(User user)
    {
        return userDao.getUserContacts(user);
    }

    @Transactional(readOnly = true)
    public List<Contact> getApprovedUserContacts(User user)
    {
        return userDao.getApprovedUserContacts(user);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(User user)
    {
        return userDao.getUnreadNotifications(user);
    }

    @Transactional(readOnly = true)
    public Notification getNotification(Integer id)
    {
        return userDao.getNotification(id);
    }

    @Transactional
    public void delete(Object o)
    {
        userDao.delete(o);
    }

    @Transactional
    public Contact getContact(Integer contactId)
    {
        return userDao.getContact(contactId);
    }
}
