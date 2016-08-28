package com.nwt.dao.interfaces;

import com.nwt.dao.model.User;
import com.nwt.dao.model.UserPhoto;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public interface IUserPhotoDao extends IBaseDao
{

    UserPhoto getUserPhoto(User user);
}
