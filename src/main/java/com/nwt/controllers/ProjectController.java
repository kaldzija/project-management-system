package com.nwt.controllers;

import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectMember;
import com.nwt.dao.model.ProjectRoleEnum;
import com.nwt.dao.model.ProjectStatusEnum;
import com.nwt.services.ProjectService;
import com.nwt.services.TaskService;
import com.nwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 10.08.2016..
 */
@RestController
@RequestMapping(value = "api/projects")
public class ProjectController extends BaseController
{
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Project getProject(@PathVariable(value = "id") Integer id)
    {
        return projectService.get(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getUserProjects()
    {
        return projectService.getUserProjects(getCurrentUserId());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Project createProject(@RequestBody Project project)
    {

        if (project.getId() != null)
        {
            projectService.saveOrUpdate(project);
            return project;
        }

        project.setStatus(ProjectStatusEnum.ACTIVE);
        project.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        projectService.saveOrUpdate(project);

        ProjectMember member = new ProjectMember();
        member.setUser(getCurrentUser());
        member.setRole(ProjectRoleEnum.OWNER);
        member.setProject(project);

        projectService.saveOrUpdate(member);
        return project;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/members")
    public List<ProjectMember> getProjectMembers(@PathVariable(value = "id") Integer projectId)
    {
        return projectService.getProjectWithMembers(projectId).getMembers();
    }
}
