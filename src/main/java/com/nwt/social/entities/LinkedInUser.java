package com.nwt.social.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 27.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInUser
{
    private String id;
    private String firstName;
    private String lastName;

    public LinkedInUser()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
