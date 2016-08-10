package com.nwt.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spark.utils.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@RestController
@RequestMapping(value = "api/image")
public class ImageController
{
    public static final String BLANK_PROFILE_IMAGE = "/images/default-profile.png";

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public void getUserPhoto(@PathVariable(value = "userId") Integer userId, HttpServletResponse response) throws IOException
    {
        InputStream in = new FileInputStream(getClass().getClassLoader().getResource(BLANK_PROFILE_IMAGE).getFile());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
        in.close();
    }

}
