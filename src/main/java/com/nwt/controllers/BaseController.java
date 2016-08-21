package com.nwt.controllers;

import com.nwt.auth.AuthUtils;
import com.nwt.dao.model.User;
import com.nwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 04.06.2016..
 */

public class BaseController
{
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public UserService getUserService()
    {
        return userService;
    }

    public Integer getCurrentUserId()
    {
        try
        {
            return Integer.valueOf(AuthUtils.decodeToken(request.getHeader(AuthUtils.AUTH_HEADER_KEY)).getSubject());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public User getCurrentUser()
    {
        return getCurrentUserId() != null ? userService.getUser(getCurrentUserId()) : null;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public Timestamp getCurrentTimestamp()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    protected class FakeObject
    {
        private Object result;

        public Object getResult()
        {
            return result;
        }

        public void setResult(Object result)
        {
            this.result = result;
        }
    }
}
