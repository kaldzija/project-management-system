package com.nwt.social.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 27.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUser
{
    private String sub;
    private String gender;
    private String given_name;
    private String family_name;

    public GoogleUser()
    {
    }

    public String getSub()
    {
        return sub;
    }

    public void setSub(String sub)
    {
        this.sub = sub;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGiven_name()
    {
        return given_name;
    }

    public void setGiven_name(String given_name)
    {
        this.given_name = given_name;
    }

    public String getFamily_name()
    {
        return family_name;
    }

    public void setFamily_name(String family_name)
    {
        this.family_name = family_name;
    }
}
