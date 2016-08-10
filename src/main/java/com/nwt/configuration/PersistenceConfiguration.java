package com.nwt.configuration;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.nwt.configuration"})
@PropertySource(value = {"classpath:application.properties"})
public class PersistenceConfiguration
{

    @Bean
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.nwt.dao.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        dataSource.setUrl("jdbc:postgresql://localhost:5432/test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");

//        dataSource.setUrl(dbUrl);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);

        return dataSource;
    }


    public Properties hibernateProperties()
    {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setSessionFactory(sessionFactory);
        return manager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
    {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
