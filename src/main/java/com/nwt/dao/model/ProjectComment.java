package com.nwt.dao.model;

import javax.persistence.*;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Entity
@Table(name = "project_comments")
public class ProjectComment
{
    private Integer id;
    private Project project;
    private Comment comment;

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
    @JoinColumn(name = "project_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project"))
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_comment"))
    public Comment getComment()
    {
        return comment;
    }

    public void setComment(Comment comment)
    {
        this.comment = comment;
    }
}
