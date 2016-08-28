package com.nwt.controllers;

import com.nwt.dao.model.Contact;
import com.nwt.dao.model.Message;
import com.nwt.dao.model.User;
import com.nwt.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 27.08.2016..
 */
@RestController
@RequestMapping(value = "api/messages")
public class MessageController extends BaseController
{
    @Autowired
    MessageService messageService;


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Message save(@RequestBody Message message)
    {
        message.setCreated(getCurrentTimestamp());
        message.setRead(false);
        message.setFromUser(getCurrentUser());
        messageService.saveOrUpdate(message);
        return message;
    }


    @ResponseBody
    @RequestMapping(value = "/users/{id}")
    public List<Message> getMessages(@PathVariable(value = "id") Integer userId)
    {
        List<Message> messages = messageService.getMessages(getCurrentUser(), getUserService().getUser(userId));
        for (Message message : messages)
        {
            message.setRead(true);
            messageService.saveOrUpdate(message);
        }
        return messageService.getMessages(getCurrentUser(), getUserService().getUser(userId));
    }

    @ResponseBody
    @RequestMapping(value = "/unread")
    public UnreadCount getCount()
    {
        UnreadCount unreadCount = new UnreadCount();
        unreadCount.setCount(messageService.getUnreadMessages(getCurrentUser()).size());
        return unreadCount;
    }

    @ResponseBody
    @RequestMapping(value = "/users")
    public List<User> getContacts()
    {
        List<Contact> contacts = getUserService().getApprovedUserContacts(getCurrentUser());
        List<User> users = new ArrayList<>();

        for (Contact contact : contacts)
        {
            if (contact.getSender().getId().equals(getCurrentUserId())) users.add(contact.getReceiver());
            else users.add(contact.getSender());
        }

        return users;
    }

    public class UnreadCount
    {
        Integer count;

        public Integer getCount()
        {
            return count;
        }

        public void setCount(Integer count)
        {
            this.count = count;
        }
    }
}
