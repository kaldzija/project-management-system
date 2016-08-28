package com.nwt.dao;

import com.nwt.dao.interfaces.IUserPhotoDao;
import com.nwt.dao.model.User;
import com.nwt.dao.model.UserPhoto;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Repository(value = UserPhotoDao.BEAN_NAME)
public class UserPhotoDao extends BaseDao<UserPhoto> implements IUserPhotoDao
{
    public static final String BEAN_NAME = "userPhotoDao";

    public UserPhotoDao()
    {
        super(UserPhoto.class);
    }

    @Override
    public UserPhoto getUserPhoto(User user)
    {
        Criteria criteria = getSession().createCriteria(UserPhoto.class);
        criteria.add(Restrictions.eq("user", user));
        return (UserPhoto) criteria.uniqueResult();
    }
}
