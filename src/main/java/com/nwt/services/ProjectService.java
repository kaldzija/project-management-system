package com.nwt.services;

import com.nwt.dao.interfaces.IProjectDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectMember;
import com.nwt.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
@Service
public class ProjectService
{
    @Autowired
    private IProjectDao projectDao;

    @Transactional
    public Project get(Integer id)
    {
        return (Project) projectDao.get(id);
    }

    @Transactional(readOnly = true)
    public List<Project> getAll()
    {
        return projectDao.getAll();
    }

    @Transactional
    public void saveOrUpdate(Object object)
    {
        projectDao.saveOrUpdate(object);
    }

    @Transactional
    public List<ProjectMember> getProjectMembers(Integer id)
    {
        return projectDao.getProjectMembers(id);
    }

    @Transactional
    public ProjectMember getProjectMember(Integer projectId, Integer userId)
    {
        List<ProjectMember> projectMembers = getProjectMembers(projectId);
        for (ProjectMember member : projectMembers)
        {
            if (member.getUser().getId().equals(userId)) return member;
        }
        return null;
    }

    @Transactional
    public void delete(Object o)
    {
        projectDao.delete(o);
    }
    @Transactional
    public List<Project> getUserProjects(Integer id)
    {
        return projectDao.getUserProjects(id);
    }

    @Transactional
    public User getProjectOwner(Integer projectId)
    {
        return projectDao.getProjectOwner(projectId);
    }
}
