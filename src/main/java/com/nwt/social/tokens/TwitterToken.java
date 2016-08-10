package com.nwt.social.tokens;

/**
 * Created by Jasmin Kaldzija on 18.06.2016..
 */
public class TwitterToken
{
    private String oauth_token;

    public TwitterToken(String oauth_token)
    {
        this.oauth_token = oauth_token;
    }

    public String getOauth_token()
    {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token)
    {
        this.oauth_token = oauth_token;
    }
}
