package com.nwt.services;

import com.nwt.dao.interfaces.IProjectDao;
import com.nwt.dao.model.Project;
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
    public Project getProjectWithMembers(Integer id)
    {
        return projectDao.getProjectWithMembers(id);
    }

    @Transactional
    public List<Project> getUserProjects(Integer id)
    {
        return projectDao.getUserProjects(id);
    }
}
