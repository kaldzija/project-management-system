package com.nwt.dao.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@Entity
@Table(name = "contacts")
public class Contact
{
    private Integer id;
    private User sender;
    private User receiver;
    private Boolean approved;
    private Timestamp created;

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
    @JoinColumn(name = "sender_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_contact_sender_id"))
    public User getSender()
    {
        return sender;
    }

    public void setSender(User sender)
    {
        this.sender = sender;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_contact_receiver_id"))
    public User getReceiver()
    {
        return receiver;
    }

    public void setReceiver(User receiver)
    {
        this.receiver = receiver;
    }

    @Basic
    @Column(name = "approved")
    public Boolean getApproved()
    {
        return approved;
    }

    public void setApproved(Boolean approved)
    {
        this.approved = approved;
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
}
