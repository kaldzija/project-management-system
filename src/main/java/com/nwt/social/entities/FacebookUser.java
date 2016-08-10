package com.nwt.social.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jasmin Kaldzija on 26.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUser
{
    private String id;
    private String gender;
    private String first_name;
    private String last_name;

    public FacebookUser()
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

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @JsonProperty(value = "first_name")
    public String getFirstName()
    {
        return first_name;
    }

    public void setFirstName(String first_name)
    {
        this.first_name = first_name;
    }

    @JsonProperty(value = "last_name")
    public String getLastName()
    {
        return last_name;
    }

    public void setLastName(String last_name)

    {
        this.last_name = last_name;
    }
}
