package com.nwt.dao.interfaces;

import com.nwt.dao.model.Project;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface IProjectDao extends IBaseDao
{

    Project getProjectWithMembers(Integer id);

    List<Project> getUserProjects(Integer id);
}
