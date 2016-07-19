package com.nwt.controllers;

import com.nwt.dao.model.Test;
import com.nwt.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@RestController
@RequestMapping("/test")
public class TestController
{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    @Autowired
    private TestService testService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Test test(HttpServletRequest request)
    {
        Test test = new Test();
        test.setDate(dateFormat.format(new Date()));
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null)
        {
            ipAddress = request.getRemoteAddr();
        }
        test.setIp(ipAddress);
        testService.create(test);
        return test;
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Test> all()
    {
        return testService.getAll();
    }
}
