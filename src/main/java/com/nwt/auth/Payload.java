package com.nwt.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jasmin Kaldzija on 26.05.2016..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload
{
    String clientId;
    String redirectUri;
    String code;

    public Payload()
    {
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getRedirectUri()
    {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri)
    {
        this.redirectUri = redirectUri;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
