package com.nwt.controllers;

import com.nwt.dao.model.*;
import com.nwt.services.CommentService;
import com.nwt.services.ProjectService;
import com.nwt.services.TaskService;
import com.nwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jasmin Kaldzija on 12.08.2016..
 */
@RestController
@RequestMapping(value = "api/tasks")
public class TaskController extends BaseController
{
    @Autowired
    ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Task getTask(@PathVariable(value = "id") Integer id)
    {
        return taskService.get(id);
    }

    @ResponseBody
    @RequestMapping
    public List<Task> getProjectTasks(@RequestParam(required = true, value = "projectId") Integer projectId)
    {
        Project project = projectService.get(projectId);
        return taskService.getProjectTasks(project);
    }

    @ResponseBody
    @RequestMapping(value = "/user")
    public List<Task> getUserProjectTasks(@RequestParam(required = true, value = "projectId") Integer projectId)
    {
        Project project = projectService.get(projectId);
        return taskService.getUserProjectTasks(project, getCurrentUser());
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Task saveOrUpdate(@RequestBody Task task)
    {
        if (task.getId() != null)
        {
            task.setUpdated(getCurrentTimestamp());
            taskService.saveOrUpdate(task);
        } else
        {
            task.setUser(getCurrentUser());
            task.setCreatedBy(getCurrentUser());
            task.setCreated(getCurrentTimestamp());
            task.setUpdated(getCurrentTimestamp());
            task.setStatus(TaskStatusEnum.TODO);
            taskService.saveOrUpdate(task);
        }
        return task;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/comments")
    public TaskComment saveOrUpdateComment(@RequestBody Comment comment, @PathVariable(value = "id") Integer taskId)
    {
        comment.setUser(getCurrentUser());
        comment.setCreated(getCurrentTimestamp());
        commentService.saveOrUpdate(comment);

        TaskComment taskComment = new TaskComment();
        taskComment.setComment(comment);
        taskComment.setTask(taskService.get(taskId));
        commentService.saveOrUpdate(taskComment);

        return taskComment;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/comments")
    public List<TaskComment> getTaskComments(@PathVariable(value = "id") Integer taskId)
    {
        return commentService.getTaskComments(taskService.get(taskId));
    }
}
