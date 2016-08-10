package com.nwt.social.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 19.06.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterUser
{
    private String id;
    private String name;

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
