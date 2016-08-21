package com.nwt.controllers;

import com.nwt.dao.model.*;
import com.nwt.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 19.08.2016..
 */
@RequestMapping(value = "api/users")
@RestController
public class UserController extends BaseController
{
    @Autowired
    private ProjectService projectService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/notifications")
    public List<Notification> getUnreadNotifications()
    {
        return getUserService().getUnreadNotifications(getCurrentUser());
    }

    @ResponseBody
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public List<Contact> getContacts()
    {
        return getUserService().getApprovedUserContacts(getCurrentUser());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contacts")
    public ResponseEntity createContact(@RequestBody Contact contact)
    {
        contact.setSender(getCurrentUser());
        contact.setApproved(false);
        contact.setCreated(getCurrentTimestamp());
        getUserService().saveOrUpdate(contact);

        Notification notification = new Notification();
        notification.setUser(contact.getReceiver());
        notification.setType(NotificationTypeEnum.CONTACT);
        notification.setRead(false);
        notification.setCreated(getCurrentTimestamp());
        notification.setContact(contact);
        getUserService().saveOrUpdate(notification);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/contacts/{notificationId}")
    public FakeObject handleContact(@RequestBody Object o, @PathVariable(value = "notificationId") Integer notificationId, @RequestParam(required = true, value = "accept") String accept)
    {
        Notification notification = getUserService().getNotification(notificationId);
        notification.setRead(true);
        getUserService().saveOrUpdate(notification);
        if (accept.equals("true"))
        {
            Contact contact = notification.getContact();
            contact.setApproved(true);
            getUserService().saveOrUpdate(contact);
        }
        FakeObject fakeObject = new FakeObject();
        fakeObject.setResult(getUserService().getUnreadNotifications(getCurrentUser()));
        return fakeObject;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/other")
    public List<SimpleUser> getOtherUsers()
    {
        List<SimpleUser> simpleUsers = new ArrayList<>();
        List<User> users = getUserService().getAll();
        List<Contact> contacts = getUserService().getUserContacts(getCurrentUser());
        for (User user : users)
        {
            if (user.getId().equals(getCurrentUserId())) continue;
            SimpleUser simpleUser = new SimpleUser();
            simpleUser.setName(user.getDisplayName());
            simpleUser.setEmail(user.getEmail());
            simpleUser.setId(user.getId());
            simpleUser.setPhone("032 456 852");

            for (Contact contact : contacts)
            {
                if (contact.getSender().getId().equals(user.getId()) || contact.getReceiver().getId().equals(user.getId()))
                {
                    simpleUser.setContact(true);
                    break;
                }
            }
            simpleUsers.add(simpleUser);
        }
        return simpleUsers;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/projects/{id}")
    public List<SimpleUser> getProjectMembers(@PathVariable(value = "id") Integer projectId)
    {
        List<SimpleUser> simpleUsers = new ArrayList<>();
        List<ProjectMember> projectMembers = projectService.getProjectWithMembers(projectId).getMembers();
        List<User> users = getUsersFromContact(getUserService().getUserContacts(getCurrentUser()), getCurrentUser());
        for (User user : users)
        {
            if (user.getId().equals(getCurrentUserId())) continue;
            SimpleUser simpleUser = new SimpleUser();
            simpleUser.setEmail(user.getEmail());
            simpleUser.setId(user.getId());
            simpleUser.setPhone("032 456 852");
            simpleUser.setName(user.getDisplayName());


            for (ProjectMember projectMember : projectMembers)
            {
                if (projectMember.getUser().getId().equals(user.getId()))
                {
                    simpleUser.setProjectRole(projectMember.getRole());
                }
            }
            simpleUsers.add(simpleUser);
        }
        return simpleUsers;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/projects/members")
    public ResponseEntity createProjectRole(@RequestBody ProjectMember projectMember)
    {
        projectMember.setRole(ProjectRoleEnum.MEMBER);
        getUserService().saveOrUpdate(projectMember);
        return new ResponseEntity(HttpStatus.OK);
    }

    ;

    @RequestMapping(method = RequestMethod.DELETE, value = "/projects/members")
    public ResponseEntity removeProjectRole(@RequestParam(required = true, name = "userId") Integer userId, @RequestParam(required = true, name = "projectId") Integer projectId)
    {
        ProjectMember member = projectService.getProjectMember(projectId, userId);
        projectService.delete(member);
        return new ResponseEntity(HttpStatus.OK);
    }

    ;

    private List<User> getUsersFromContact(List<Contact> contacts, User user)
    {
        List<User> users = new ArrayList<>();
        for (Contact contact : contacts)
        {
            if (contact.getReceiver().getId().equals(user.getId())) users.add(contact.getSender());
            else users.add(contact.getReceiver());
        }
        return users;
    }

    private class SimpleUser
    {
        private Integer id;
        private String name;
        private String email;
        private String phone;
        private boolean contact;
        private ProjectRoleEnum projectRole;

        public Integer getId()
        {
            return id;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getPhone()
        {
            return phone;
        }

        public void setPhone(String phone)
        {
            this.phone = phone;
        }

        public boolean isContact()
        {
            return contact;
        }

        public void setContact(boolean contact)
        {
            this.contact = contact;
        }

        public ProjectRoleEnum getProjectRole()
        {
            return projectRole;
        }

        public void setProjectRole(ProjectRoleEnum projectRole)
        {
            this.projectRole = projectRole;
        }
    }
}
