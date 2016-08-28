package com.nwt.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 22.08.2016..
 */
@Entity
@Table(name = "project_files")
public class ProjectFile
{
    private Integer id;
    private byte[] content;
    private Timestamp created;
    private User user;
    private long size;
    private String contentType;
    private String fileName;
    private Project project;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project_file_project"))
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", nullable = false)
    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated()
    {
        return created;
    }

    public void setCreated(Timestamp created)
    {
        this.created = created;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project_file_user"))
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Basic
    @Column(name = "size")
    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    @Basic
    @Column(name = "content_type")
    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    @Basic
    @Column(name = "file_name")
    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
