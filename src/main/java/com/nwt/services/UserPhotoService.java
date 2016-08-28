package com.nwt.services;

import com.nwt.dao.interfaces.IUserPhotoDao;
import com.nwt.dao.model.User;
import com.nwt.dao.model.UserPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Service
public class UserPhotoService extends BaseService<UserPhoto>
{
    @Autowired
    private IUserPhotoDao userPhotoDao;

    @Override
    public IUserPhotoDao getDao()
    {
        return userPhotoDao;
    }

    @Transactional
    public UserPhoto getUserPhoto(User user)
    {
        return userPhotoDao.getUserPhoto(user);
    }
}
