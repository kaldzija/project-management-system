package com.nwt.dao.model;

import javax.persistence.*;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Entity
@Table(name = "test")
public class Test
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String date;
    private String ip;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Basic
    @Column(name = "date")
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Basic
    @Column(name = "ip")
    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
