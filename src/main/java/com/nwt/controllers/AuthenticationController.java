package com.nwt.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.auth.AuthUtils;
import com.nwt.auth.Payload;
import com.nwt.auth.entities.Token;
import com.nwt.dao.model.*;
import com.nwt.mail.Mailer;
import com.nwt.services.ProjectService;
import com.nwt.services.UserService;
import com.nwt.social.SocialTypeEnum;
import com.nwt.social.entities.FacebookUser;
import com.nwt.social.tokens.FacebookToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseController
{
    public static final String REGISTRATION_TOKEN_DURATION = "security.registration.token.duration";
    public static final String FACEBOOK_SECRET_PROPERTY = "security.facebook.secret";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CODE_KEY = "code";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String AUTH_CODE = "authorization_code";

    @Resource(name = "mailer")
    private Mailer mailer;

    @Resource(name = "userService")
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST, value = "/facebook")
    public ResponseEntity<Token> loginFacebook(@RequestBody Payload payload) throws Exception
    {

        final String accessTokenUrl = "https://graph.facebook.com/v2.6/oauth/access_token";
        final String graphApiUrl = "https://graph.facebook.com/v2.6/me";
        final String facebookSecret = environment.getRequiredProperty(FACEBOOK_SECRET_PROPERTY);

        URI targetUrl = UriComponentsBuilder.fromUriString(accessTokenUrl).queryParam(CLIENT_ID_KEY, payload.getClientId()).queryParam(REDIRECT_URI_KEY, payload.getRedirectUri()).queryParam(CLIENT_SECRET, facebookSecret).queryParam(CODE_KEY, payload.getCode()).build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        FacebookToken facebookToken = restTemplate.getForObject(targetUrl, FacebookToken.class);

        targetUrl = UriComponentsBuilder.fromUriString(graphApiUrl).queryParam("access_token", facebookToken.getAccess_token()).queryParam("fields", "id,gender,first_name,last_name,picture").queryParam("expires_in", facebookToken.getExpires_in()).build().toUri();

        FacebookUser facebookUser = restTemplate.getForObject(targetUrl, FacebookUser.class);

        User user = userService.getSocialUser(facebookUser.getId(), SocialTypeEnum.FACEBOOK);
        if (user == null)
        {
            user = createUser(facebookUser.getFirstName() + " " + facebookUser.getLastName(), facebookUser.getGender(), facebookUser.getId(), SocialTypeEnum.FACEBOOK);
        }
        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody Object newUser) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(newUser));
        String email = node.get("email").asText();
        String displayName = node.get("displayName").asText();
        String password = node.get("password").asText();
        // TODO: 02.08.2016. Check email exists
        if (userService.getRegistredByEmail(email) != null) return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

        User user = new User();
        user.setDisplayName(displayName);
        user.setPasswordHash(password);
        user.setEmail(email);
        user.setRegisteredDate(new Timestamp(System.currentTimeMillis()));
        user.setRole(RoleEnum.CLIENT);
        user.setState(UserStateEnum.CONFIRMATION_REQUIRED);
        userService.saveOrUpdate(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setValue(UUID.randomUUID().toString());
        confirmationToken.setUser(user);
        confirmationToken.setTypeEnum(ConfirmationTokenTypeEnum.ACTIVATION);

        Integer days = Integer.valueOf(environment.getRequiredProperty(REGISTRATION_TOKEN_DURATION));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);

        confirmationToken.setExpirationDate(new Timestamp(calendar.getTimeInMillis()));
//        mailer.sendActivationMail(user.getEmail(),confirmationToken.getValue());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity login(@RequestBody Object loginData) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(loginData));
        String email = node.get("email").asText();
        User user = userService.getRegistredByEmail(email);
        if (user == null || !node.get("password").asText().equals(user.getPasswordHash()))
        {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getState().equals(UserStateEnum.AWAITING_VERIFICATION))
        {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    private User createUser(String diplayName, String gender, String socialId, SocialTypeEnum socialType)
    {
        User user = new User();
        user.setDisplayName(diplayName);
        user.setSocialId(socialId);
        user.setRegisteredDate(new Timestamp(System.currentTimeMillis()));
        user.setSocialType(socialType);
        user.setState(UserStateEnum.ACTIVE);
        user.setRole(RoleEnum.CLIENT);
        userService.saveOrUpdate(user);
        return user;
    }
}
