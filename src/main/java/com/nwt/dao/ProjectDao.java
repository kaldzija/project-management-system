package com.nwt.dao;

import com.nwt.dao.interfaces.IProjectDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectMember;
import com.nwt.dao.model.User;
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
    public List<ProjectMember> getProjectMembers(Integer id)
    {
        String SQL = "Select p from ProjectMember p where p.project.id=:id";
        return getSession().createQuery(SQL).setParameter("id", id).list();
    }

    @Override
    public List<Project> getUserProjects(Integer id)
    {
        String SQL = "Select p from Project p ,ProjectMember pm where pm.project=p and pm.user.id=:id";
        return ((List<Project>) getSession().createQuery(SQL).setParameter("id", id).list());
    }

    @Override
    public User getProjectOwner(Integer projectId)
    {
        String SQL = "Select p from ProjectMember p where p.project.id=:id and p.role='OWNER'";
        return ((ProjectMember) getSession().createQuery(SQL).setParameter("id", projectId).uniqueResult()).getUser();
    }

//    @Override
//    public void saveOrUpdate(ProjectMember projectMember)
//    {
//        getSession().saveOrUpdate(projectMember);
//    }

}
