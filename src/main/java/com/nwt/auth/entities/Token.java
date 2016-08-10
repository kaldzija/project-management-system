package com.nwt.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nwt.dao.model.User;

/**
 * Created by Jasmin Kaldzija on 27.05.2016..
 */

public class Token
{
    private String token;
    private User user;

    public Token(@JsonProperty("token") String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}