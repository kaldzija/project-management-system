package com.nwt.dao.interfaces;

import com.nwt.dao.model.Message;
import com.nwt.dao.model.User;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface IMessageDao extends IBaseDao
{
    List<Message> getMessages(User u1, User u2);

    List<Message> getUnreadMessages(User u1);
}
