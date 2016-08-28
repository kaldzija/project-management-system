package com.nwt.services;

import com.nwt.dao.interfaces.IProjectFileDao;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 18.08.2016..
 */
@Service
public class ProjectFileService extends BaseService<ProjectFile>
{
    @Autowired
    private IProjectFileDao projectFileDao;

    @Override
    public IProjectFileDao getDao()
    {
        return projectFileDao;
    }

    @Transactional
    public List<ProjectFile> getProjectFiles(Project project)
    {
        return getDao().getProjectFiles(project);
    }
}
