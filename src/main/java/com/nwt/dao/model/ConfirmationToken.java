package com.nwt.dao.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 31.05.2016..
 */
@Entity
@Table(name = "confirmation_tokens", catalog = "real_estate_db")
public class ConfirmationToken
{
    private Integer id;
    private ConfirmationTokenTypeEnum typeEnum;
    private Timestamp expirationDate;
    private String value;
    private User user;

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
    @Column(name = "type", nullable = false)
    public ConfirmationTokenTypeEnum getTypeEnum()
    {
        return typeEnum;
    }

    public void setTypeEnum(ConfirmationTokenTypeEnum typeEnum)
    {
        this.typeEnum = typeEnum;
    }

    @Basic
    @Column(name = "expiration_date", nullable = false)
    public Timestamp getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    @Basic
    @Column(name = "value", unique = true, nullable = false)
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_confirmation_token_users"))
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
