package com.nwt.dao.interfaces;

import com.nwt.dao.model.User;
import com.nwt.social.SocialTypeEnum;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface IUserDao extends IBaseDao
{
    User getRegistredByEmail(String email);

    User getSocialUser(String socialId, SocialTypeEnum socialType);
}
