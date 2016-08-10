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
    }]);