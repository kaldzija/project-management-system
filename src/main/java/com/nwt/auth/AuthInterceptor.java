package com.nwt.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Created by Jasmin Kaldzija on 28.05.2016..
 */
public class AuthInterceptor extends HandlerInterceptorAdapter
{
    private static final String AUTH_ERROR_MSG = "Please make sure your request has an Authorization header", EXPIRE_ERROR_MSG = "Token has expired", JWT_ERROR_MSG = "Unable to parse JWT", JWT_INVALID_MSG = "Invalid JWT token";

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception
    {
        String authHeader = httpRequest.getHeader(AuthUtils.AUTH_HEADER_KEY);
        if (StringUtils.isBlank(authHeader))
        {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTH_ERROR_MSG);
        } else
        {
            JWTClaimsSet claimSet = null;
            try
            {
                claimSet = (JWTClaimsSet) AuthUtils.decodeToken(authHeader);
            } catch (ParseException e)
            {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, JWT_ERROR_MSG);
                return false;
            } catch (JOSEException e)
            {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, JWT_INVALID_MSG);
                return false;
            }

            if (new DateTime(claimSet.getExpirationTime()).isBefore(DateTime.now()))
            {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, EXPIRE_ERROR_MSG);
            } else
            {
                return super.preHandle(httpRequest, httpResponse, handler);
            }
        }
        return false;
    }
}
