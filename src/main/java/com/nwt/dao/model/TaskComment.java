package com.nwt.dao.model;

import javax.persistence.*;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Entity
@Table(name = "task_comments")
public class TaskComment
{
    private Integer id;
    private Task task;
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
    @JoinColumn(name = "task_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_comment_task"))
    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
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
