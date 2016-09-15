package com.nwt.controllers;

import com.nwt.dao.model.*;
import com.nwt.other.ProjectCompletedPercentage;
import com.nwt.services.CommentService;
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
    @Autowired
    private CommentService commentService;

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
        project.setUpdated(getCurrentTimestamp());
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
        return projectService.getProjectMembers(projectId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/owner")
    public User getProjectOwner(@PathVariable(value = "id") Integer projectId)
    {
        return projectService.getProjectOwner(projectId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/comments")
    public ProjectComment saveOrUpdateComment(@RequestBody Comment comment, @PathVariable(value = "id") Integer projectId)
    {
        comment.setCreated(getCurrentTimestamp());
        comment.setUser(getCurrentUser());
        commentService.saveOrUpdate(comment);

        ProjectComment projectComment = new ProjectComment();
        projectComment.setComment(comment);
        projectComment.setProject(projectService.get(projectId));
        commentService.saveOrUpdate(projectComment);
        return projectComment;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/comments")
    public List<ProjectComment> getProjectComments(@PathVariable(value = "id") Integer projectId)
    {
        return commentService.getProjectComments(projectService.get(projectId));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/percentage")
    public ProjectCompletedPercentage getProjectCompletedPercentage(@PathVariable(value = "id") Integer projectId)
    {
        Project project = projectService.get(projectId);
        return taskService.getPercentage(project);
    }
}
