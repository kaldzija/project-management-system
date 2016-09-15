package com.nwt.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.nwt.auth.AuthUtils;
import com.nwt.auth.Payload;
import com.nwt.auth.entities.Token;
import com.nwt.dao.model.*;
import com.nwt.mail.Mailer;
import com.nwt.services.ProjectService;
import com.nwt.services.UserService;
import com.nwt.social.SocialTypeEnum;
import com.nwt.social.entities.FacebookUser;
import com.nwt.social.entities.GoogleUser;
import com.nwt.social.entities.LinkedInUser;
import com.nwt.social.entities.TwitterUser;
import com.nwt.social.tokens.FacebookToken;
import com.nwt.social.tokens.GoogleToken;
import com.nwt.social.tokens.LinkedInToken;
import com.nwt.social.tokens.TwitterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
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
    public static final String GOOGLE_SECRET_PROPERTY = "security.google.secret";
    public static final String LINKEDIN_SECRET_PROPERTY = "security.linkedin.secret";
    public static final String TWITTER_SECRET_PROPERTY = "security.twitter.secret";
    public static final String TWITTER_CLIENTID_PROPERTY = "security.twitter.clientid";


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

        if (user.getState().equals(UserStateEnum.BLOCKED))
        {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        updateUserActivity(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/google")
    public ResponseEntity<Token> loginGoogle(@RequestBody Payload payload) throws Exception
    {
        final String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
        final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
        final String googleSecret = environment.getRequiredProperty(GOOGLE_SECRET_PROPERTY);
        final MultiValueMap<String, String> accessData = new LinkedMultiValueMap<>();
        accessData.add(CLIENT_ID_KEY, payload.getClientId());
        accessData.add(REDIRECT_URI_KEY, payload.getRedirectUri());
        accessData.add(CLIENT_SECRET, googleSecret);
        accessData.add(CODE_KEY, payload.getCode());
        accessData.add(GRANT_TYPE_KEY, AUTH_CODE);

        RestTemplate restTemplate = new RestTemplate();
        GoogleToken googleToken = restTemplate.postForObject(accessTokenUrl, accessData, GoogleToken.class);

        accessData.clear();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthUtils.AUTH_HEADER_KEY, String.format("Bearer %s", googleToken.getAccess_token()));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> exchange = restTemplate.exchange(peopleApiUrl, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleUser googleUser = objectMapper.readValue(exchange.getBody(), GoogleUser.class);

        User user = userService.getSocialUser(googleUser.getSub(), SocialTypeEnum.GOOGLE);
        if (user == null)
        {
            user = createUser(googleUser.getGiven_name() + ' ' + googleUser.getFamily_name(), googleUser.getGender(), googleUser.getSub(), SocialTypeEnum.GOOGLE);
        }

        if (user.getState().equals(UserStateEnum.BLOCKED))
        {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        updateUserActivity(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/linkedin")
    public ResponseEntity<Token> loginLinkedin(@RequestBody Payload payload) throws Exception
    {
        final String accessTokenUrl = "https://www.linkedin.com/oauth/v2/accessToken";
        final String peopleApiUrl = "https://api.linkedin.com/v1/people/~?format=json";
        final String linkedinSecret = environment.getRequiredProperty(LINKEDIN_SECRET_PROPERTY);

        final MultiValueMap<String, String> accessData = new LinkedMultiValueMap<String, String>();
        accessData.add(CLIENT_ID_KEY, payload.getClientId());
        accessData.add(REDIRECT_URI_KEY, payload.getRedirectUri());
        accessData.add(CLIENT_SECRET, linkedinSecret);
        accessData.add(CODE_KEY, payload.getCode());
        accessData.add(GRANT_TYPE_KEY, AUTH_CODE);

        RestTemplate restTemplate = new RestTemplate();
        LinkedInToken linkedInToken = restTemplate.postForObject(accessTokenUrl, accessData, LinkedInToken.class);

        accessData.clear();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthUtils.AUTH_HEADER_KEY, String.format("Bearer %s", linkedInToken.getAccess_token()));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> exchange = restTemplate.exchange(peopleApiUrl, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        LinkedInUser linkedInUser = objectMapper.readValue(exchange.getBody(), LinkedInUser.class);
        User user = userService.getSocialUser(linkedInUser.getId(), SocialTypeEnum.LINKEDIN);
        if (user == null)
        {
            user = createUser(linkedInUser.getFirstName() + " " + linkedInUser.getLastName(), null, linkedInUser.getId(), SocialTypeEnum.LINKEDIN);
        }
        if (user.getState().equals(UserStateEnum.BLOCKED))
        {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        updateUserActivity(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/twitter")
    public ResponseEntity loginTwitter(@RequestBody Object payload) throws Exception
    {
        final String twitterAPI = "https://api.twitter.com/1.1/account/verify_credentials.json";
        final String twitterSecret = environment.getProperty(TWITTER_SECRET_PROPERTY);
        final String clientId = environment.getProperty(TWITTER_CLIENTID_PROPERTY);

        final String oauth_token_label = "oauth_token";
        final String oauth_verifier_label = "oauth_verifier";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(payload));

        if (node.get(oauth_token_label) != null)
        {
            OAuthService service = new ServiceBuilder().provider(TwitterApi.class).apiKey(clientId).apiSecret(twitterSecret).build();

            com.github.scribejava.core.model.Token requestToken = new com.github.scribejava.core.model.Token(node.get(oauth_token_label).textValue(), node.get(oauth_verifier_label).textValue());
            Verifier verifier = new Verifier(node.get(oauth_verifier_label).textValue());

            com.github.scribejava.core.model.Token accessToken = service.getAccessToken(requestToken, verifier);

            OAuthRequest request = new OAuthRequest(Verb.GET, twitterAPI, service);
            service.signRequest(accessToken, request);

            com.github.scribejava.core.model.Response response = request.send();
            TwitterUser twitterUser = objectMapper.readValue(response.getBody(), TwitterUser.class);

            User user = userService.getSocialUser(twitterUser.getId(), SocialTypeEnum.TWITTER);
            if (user == null)
            {
                user = createUser(twitterUser.getName(), null, twitterUser.getId(), SocialTypeEnum.TWITTER);
            }

            if (user.getState().equals(UserStateEnum.BLOCKED))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
            token.setUser(user);
            updateUserActivity(user);

            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(clientId);
        builder.setOAuthConsumerSecret(twitterSecret);
        if (node.get(oauth_token_label) != null)
        {
            builder.setOAuthAccessToken(node.get(oauth_token_label).textValue());
            builder.setOAuthAccessTokenSecret(node.get(oauth_verifier_label).toString());
        }

        TwitterFactory factory = new TwitterFactory(builder.build());
        Twitter twitter = factory.getInstance();

        try
        {
            String callbackURL = getRequest().getRequestURL().toString().replace(getRequest().getRequestURI(), "");

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            return new ResponseEntity<>(new TwitterToken(requestToken.getToken()), HttpStatus.OK);

        } catch (TwitterException e)
        {
            throw new ServletException(e);
        }
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
        if (userService.getUserByEmail(email) != null) return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

        User user = new User();
        user.setDisplayName(displayName);
        user.setPasswordHash(password);
        user.setEmail(email);
        user.setRegisteredDate(new Timestamp(System.currentTimeMillis()));
        user.setRole(RoleEnum.CLIENT);
        user.setState(UserStateEnum.ACTIVE);
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
        User user = userService.getRegisteredByEmail(email);
        if (user == null || !node.get("password").asText().equals(user.getPasswordHash()))
        {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getState().equals(UserStateEnum.BLOCKED))
        {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }


        Token token = AuthUtils.createToken(getRequest().getRemoteHost(), user.getId().toString());
        token.setUser(user);
        updateUserActivity(user);
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

    private void updateUserActivity(User user)
    {
        user.setLastLogin(getCurrentTimestamp());
        getUserService().saveOrUpdate(user);
    }
}
