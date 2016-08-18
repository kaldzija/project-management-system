package com.nwt.dao.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@Entity
@Table(name = "projects")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Project
{
    private Integer id;
    private String name;
    private String description;
    private Timestamp createdDate;
    private ProjectStatusEnum status;
    private List<ProjectMember> members = new ArrayList<ProjectMember>();
    private String client;
    private String version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Basic
    @Column(name = "created_date", nullable = false)
    public Timestamp getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate)
    {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public ProjectStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(ProjectStatusEnum status)
    {
        this.status = status;
    }

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<ProjectMember> getMembers()
    {
        return members;
    }

    public void setMembers(List<ProjectMember> members)
    {
        this.members = members;
    }

    @Basic
    @Column(name = "client")
    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
    }

    @Basic
    @Column(name = "version")
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}
