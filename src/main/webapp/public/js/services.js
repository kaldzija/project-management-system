/**
 * Created by jkaldzij on 10.08.2016..
 */

function getApiUrl(path) {
    return '/api'.concat(path);
}

angular.module('inspinia')
    .service("projectService", ['$resource', function ($resource) {
        this.getProject = function () {
            return $resource(getApiUrl('/projects/:id'))
        };

        this.createProject = function () {
            return $resource(getApiUrl('/projects'))
        };

        this.getProjects = function () {
            return $resource(getApiUrl('/projects'))
        };

        this.getProjectMembers = function () {
            return $resource(getApiUrl('/projects/:projectId/members'))
        };
        
        this.editProject = function () {
            return $resource(getApiUrl('/projects'))
        };
    }])
    .service("userService", ['$resource', function ($resource) {
        this.getNotifications = function () {
            return $resource(getApiUrl('/users/notifications'))
        };
        this.createContact = function () {
            return $resource(getApiUrl('/users/contacts'))
        };
        this.createProjectMember = function () {
            return $resource(getApiUrl('/users/projects/members'))
        };
        this.getContacts = function () {
            return $resource(getApiUrl('/users/contacts'))
        };
        this.getProjectMembers = function () {
            return $resource(getApiUrl('/users/projects/:projectId'))
        };
        this.getOtherUsers = function () {
            return $resource(getApiUrl('/users/other'))
        };
        this.resolveContact = function (notificationId, accept) {
            return $resource(getApiUrl('/users/contacts/' + notificationId + "?accept=" + accept))
        }
    }])
    .service("taskService", ['$resource', function ($resource) {
        this.getTask = function () {
            return $resource(getApiUrl('/tasks/:id'))
        };

        this.saveOrUpdateComment = function (taskId) {
            return $resource(getApiUrl('/tasks/' + taskId + '/comments'))
        };

        this.getProjectTasks = function () {
            return $resource(getApiUrl('/tasks?projectId=:projectId'))
        };

        this.saveOrUpdate = function () {
            return $resource(getApiUrl('/tasks'))
        };

        this.getTaskComments = function () {
            return $resource(getApiUrl('/tasks/:id/comments'))
        };
    }]);