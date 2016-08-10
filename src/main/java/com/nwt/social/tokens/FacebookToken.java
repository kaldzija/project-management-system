package com.nwt.social.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 26.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookToken
{
    private String access_token;
    private String token_type;
    private Integer expires_in;

    public FacebookToken()
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

    public String getToken_type()
    {
        return token_type;
    }

    public void setToken_type(String token_type)
    {
        this.token_type = token_type;
    }

    public Integer getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in)
    {
        this.expires_in = expires_in;
    }
}