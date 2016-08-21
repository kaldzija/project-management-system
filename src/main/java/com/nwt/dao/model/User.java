package com.nwt.dao.model;

import com.nwt.social.SocialTypeEnum;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
@Entity
@Table(name = "users")
public class User
{
    private Integer id;
    private String displayName;
    private String passwordHash;
    private Timestamp registeredDate;
    private String email;
    private SocialTypeEnum socialType;
    private String socialId;
    private RoleEnum role;
    private UserStateEnum state;
    private String aboutMe;
    private String phone;
    private String profession;

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
    @Column(name = "display_name")
    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "password_hash")
    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    @Basic
    @Column(name = "registered_date", nullable = false)
    public Timestamp getRegisteredDate()
    {
        return registeredDate;
    }

    public void setRegisteredDate(Timestamp registeredDate)
    {
        this.registeredDate = registeredDate;
    }

    @Basic
    @Column(name = "email", length = 255)
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Basic
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    public RoleEnum getRole()
    {
        return role;
    }

    public void setRole(RoleEnum role)
    {
        this.role = role;
    }

    @Basic
    @Column(name = "social")
    @Enumerated(EnumType.STRING)
    public SocialTypeEnum getSocialType()
    {
        return socialType;
    }

    public void setSocialType(SocialTypeEnum socialType)
    {
        this.socialType = socialType;
    }

    @Basic
    @Column(name = "social_id")
    public String getSocialId()
    {
        return socialId;
    }

    public void setSocialId(String socialId)
    {
        this.socialId = socialId;
    }

    @Basic
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    public UserStateEnum getState()
    {
        return state;
    }

    public void setState(UserStateEnum state)
    {
        this.state = state;
    }

    @Basic
    @Column(name = "about_me", length = 2048)
    public String getAboutMe()
    {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe)
    {
        this.aboutMe = aboutMe;
    }

    @Basic
    @Column(name = "phone", length = 20)
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Basic
    @Column(name = "profession")
    public String getProfession()
    {
        return profession;
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }
}
