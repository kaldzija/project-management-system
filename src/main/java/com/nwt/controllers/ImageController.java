package com.nwt.controllers;

import com.nwt.dao.model.User;
import com.nwt.dao.model.UserPhoto;
import com.nwt.services.UserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import spark.utils.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@RestController
@RequestMapping(value = "api/image")
public class ImageController extends BaseController
{
    public static final String BLANK_PROFILE_IMAGE = "/images/default-profile.png";

    @Autowired
    UserPhotoService userPhotoService;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public void getUserPhoto(@PathVariable(value = "userId") Integer userId, HttpServletResponse response) throws IOException
    {
        InputStream in;
        User user = getUserService().getUser(userId);
        UserPhoto userPhoto = userPhotoService.getUserPhoto(user);
        if (userPhoto == null)
        {
            in = new FileInputStream(getClass().getClassLoader().getResource(BLANK_PROFILE_IMAGE).getFile());
        } else
        {
            in = new ByteArrayInputStream(userPhotoService.getUserPhoto(getUserService().getUser(userId)).getContent());
        }
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
        in.close();
    }

    @ResponseBody
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public UserPhoto uploadUserPhoto(@PathVariable(value = "id") Integer userId, MultipartHttpServletRequest request)
    {
        CommonsMultipartFile multipartFile = (CommonsMultipartFile) ((DefaultMultipartHttpServletRequest) request).getFileMap().get("file");

        User user = getUserService().getUser(userId);
        UserPhoto userPhoto = userPhotoService.getUserPhoto(user);
        if (userPhoto == null)
        {
            userPhoto = new UserPhoto();
        }
        userPhoto.setUser(getCurrentUser());
        userPhoto.setCreated(getCurrentTimestamp());
        userPhoto.setContent(multipartFile.getBytes());
        userPhoto.setFileName(multipartFile.getOriginalFilename());
        userPhoto.setSize(multipartFile.getSize());
        userPhoto.setContentType(multipartFile.getContentType());

        userPhotoService.saveOrUpdate(userPhoto);
        return userPhoto;
    }

}
