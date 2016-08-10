package com.nwt.social.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 27.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleToken
{
    private String access_token;
    private String token_type;
    private String expires_in;
    private String id_token;

    public GoogleToken()
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

    public String getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(String expires_in)
    {
        this.expires_in = expires_in;
    }

    public String getId_token()
    {
        return id_token;
    }

    public void setId_token(String id_token)
    {
        this.id_token = id_token;
    }
}
