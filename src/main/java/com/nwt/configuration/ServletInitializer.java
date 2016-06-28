package com.nwt.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by Jasmin Kaldzija on 28.06.2016..
 */
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class<?>[]{ServletConfiguration.class};
    }

    protected Class<?>[] getServletConfigClasses()
    {
        return new Class<?>[0];
    }

    protected String[] getServletMappings()
    {
        return new String[]{"/"};
    }
}
