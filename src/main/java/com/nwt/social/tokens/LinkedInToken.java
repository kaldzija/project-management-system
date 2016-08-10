package com.nwt.social.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 27.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInToken
{
    private String access_token;
    private String expires_in;

    public LinkedInToken()
    {
    }

    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public String getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(String expires_in)
    {
        this.expires_in = expires_in;
    }
}
