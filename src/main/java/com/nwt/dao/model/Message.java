package com.nwt.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 27.08.2016..
 */
@Entity
@Table(name = "messages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message
{
    private Integer id;
    private String content;
    private User fromUser;
    private User toUser;
    private Timestamp created;
    private Boolean read;

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
    @Column(name = "content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_user", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_message_from"))
    public User getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(User fromUser)
    {
        this.fromUser = fromUser;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_user", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_message_to"))
    public User getToUser()
    {
        return toUser;
    }

    public void setToUser(User toUser)
    {
        this.toUser = toUser;
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

    @Basic
    @Column(name = "read")
    public Boolean getRead()
    {
        return read;
    }

    public void setRead(Boolean read)
    {
        this.read = read;
    }
}
