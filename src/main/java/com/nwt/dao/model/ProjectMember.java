package com.nwt.dao.model;

import javax.persistence.*;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@Entity
@Table(name = "project_members")
public class ProjectMember
{
    private Integer id;
    private User user;
    private Project project;
    private ProjectRoleEnum role;

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
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project_member_user"))
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }


    @Basic
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    public ProjectRoleEnum getRole()
    {
        return role;
    }

    public void setRole(ProjectRoleEnum role)
    {
        this.role = role;
    }
}
