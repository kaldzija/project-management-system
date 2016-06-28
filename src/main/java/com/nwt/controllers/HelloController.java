package com.nwt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jasmin Kaldzija on 28.06.2016..
 */
@Controller
public class HelloController
{

    @RequestMapping("/")
    public String test()
    {
        return "test";
    }
}
