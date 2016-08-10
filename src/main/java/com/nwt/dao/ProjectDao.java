package com.nwt.dao;

import com.nwt.dao.interfaces.IProjectDao;
import com.nwt.dao.model.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Repository(ProjectDao.BEAN_NAME)
public class ProjectDao extends BaseDao<Project> implements IProjectDao
{
    public static final String BEAN_NAME = "projectDao";

    public ProjectDao()
    {
        super(Project.class);
    }

    @Override
    public Project getProjectWithMembers(Integer id)
    {
        String SQL = "Select p from Project p FETCH ALL PROPERTIES where p.id=:id";
        return ((Project) getSession().createQuery(SQL).setParameter("id", 1).uniqueResult());
    }

    @Override
    public List<Project> getUserProjects(Integer id)
    {
        String SQL = "Select p from Project p ,ProjectMember pm where pm.project=p and pm.user.id=:id";
        return ((List<Project>) getSession().createQuery(SQL).setParameter("id", id).list());
    }

//    @Override
//    public void saveOrUpdate(ProjectMember projectMember)
//    {
//        getSession().saveOrUpdate(projectMember);
//    }

}
