package com.nwt.dao.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 19.08.2016..
 */
@Entity
@Table(name = "notifications")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Notification
{
    private Integer id;
    private User user;
    private NotificationTypeEnum type;
    private Timestamp created;
    private Boolean read;
    private Contact contact;

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
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notification_user"))
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Basic
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public NotificationTypeEnum getType()
    {
        return type;
    }

    public void setType(NotificationTypeEnum type)
    {
        this.type = type;
    }

    @Basic
    @Column(name = "created", nullable = false)
    public Timestamp getCreated()
    {
        return created;
    }

    public void setCreated(Timestamp created)
    {
        this.created = created;
    }

    @Basic
    @Column(name = "read", nullable = false)
    public Boolean getRead()
    {
        return read;
    }

    public void setRead(Boolean read)
    {
        this.read = read;
    }

    @OneToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_notification_contact"))
    public Contact getContact()
    {
        return contact;
    }

    public void setContact(Contact contact)
    {
        this.contact = contact;
    }
}