package com.nwt.dao.interfaces;

import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectFile;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 01.07.2016..
 */
public interface IProjectFileDao extends IBaseDao
{
    List<ProjectFile> getProjectFiles(Project project);
}
