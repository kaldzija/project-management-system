package com.nwt.services;

import com.nwt.dao.interfaces.IMessageDao;
import com.nwt.dao.model.Message;
import com.nwt.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Service
public class MessageService extends BaseService<Message>
{
    @Autowired
    IMessageDao messageDao;

    @Override
    public IMessageDao getDao()
    {
        return messageDao;
    }

    @Transactional
    public List<Message> getMessages(User u1, User u2)
    {
        return getDao().getMessages(u1, u2);
    }

    @Transactional
    public List<Message> getUnreadMessages(User u1)
    {
        return getDao().getUnreadMessages(u1);
    }
}
