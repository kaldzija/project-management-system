package com.nwt.dao;

import com.nwt.dao.interfaces.IProjectFileDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectFile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Repository(value = ProjectFileDao.BEAN_NAME)
public class ProjectFileDao extends BaseDao<ProjectFile> implements IProjectFileDao
{
    public static final String BEAN_NAME = "projectFileDao";

    public ProjectFileDao()
    {
        super(ProjectFile.class);
    }


    @Override
    public List<ProjectFile> getProjectFiles(Project project)
    {
        Criteria criteria = getSession().createCriteria(ProjectFile.class);
        criteria.add(Restrictions.eq("project", project));
        return criteria.list();
    }
}
