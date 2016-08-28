package com.nwt.dao.interfaces;

import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectMember;
import com.nwt.dao.model.User;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 02.08.2016..
 */
public interface IProjectDao extends IBaseDao
{

    List<ProjectMember> getProjectMembers(Integer id);

    List<Project> getUserProjects(Integer id);

    User getProjectOwner(Integer projectId);
}
