package com.nwt.social;

/**
 * Created by Jasmin Kaldzija on 29.05.2016..
 */
public enum SocialTypeEnum
{
    FACEBOOK("facebook"),
    GOOGLE("google"),
    TWITTER("twitter"),
    LINKEDIN("linkedin");

    String id;

    SocialTypeEnum(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public SocialTypeEnum getById(String id)
    {
        for (SocialTypeEnum type : values())
        {
            if (type.getId().equals(id)) return type;
        }
        return null;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
