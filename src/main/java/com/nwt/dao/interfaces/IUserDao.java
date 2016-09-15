package com.nwt.dao.interfaces;

import com.nwt.dao.model.Contact;
import com.nwt.dao.model.Notification;
import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface IUserDao extends IBaseDao
{
    User getRegisteredByEmail(String email);

    User getUserByEmail(String email);

    User getSocialUser(String socialId, SocialTypeEnum socialType);

    List<Contact> getUserContacts(User user);

    List<Contact> getApprovedUserContacts(User user);

    Contact getContact(Integer contactId);

    List<Notification> getUnreadNotifications(User user);

    Notification getNotification(Integer id);
}
