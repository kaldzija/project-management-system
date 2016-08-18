/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl($scope, $rootScope, $auth, $location, $state, $stateParams) {
    $rootScope.currentUser = JSON.parse(localStorage.getItem("currentUser"));

    $scope.logout = function () {
        localStorage.removeItem("currentUser");
        $rootScope.isAuthorized = false;
        $rootScope.constructor = null;
        $auth.logout();
        // $location.path('/login');
        $state.go('login');
    };
    
    this.userName = 'Example user';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

};

function RegisterController($scope, $auth, $translate, toastr, $state) {
    $scope.user = {
        displayName: null,
        email: null,
        password: null
    };
    $scope.exists = false;
    $scope.signUp = function () {
        $scope.exists = false;
        if ($scope.registrationForm.$valid) {
            $auth.signup($scope.user).then(function (response) {
                if (response.status == 201) {
                    $state.go('login');
                    $translate('REGISTRATION_SUCCESS').then(function (value) {
                        toastr.success(value);
                    });
                }
            }).catch(function (error) {
                if (error.status == 406) {
                    $scope.exists = true;
                }
            });
        }
        else {
            $scope.registrationForm.submitted = true;
        }
    }
}

function AuthenticationController($scope, $auth, $rootScope, $location, $state) {
    $scope.loginData = {
        email: null,
        password: null
    };

    $scope.authenticate = function (provider) {
        $auth.authenticate(provider).then(function (provider) {
            $rootScope.currentUser = provider.data.user;
            localStorage.setItem("currentUser", JSON.stringify($rootScope.currentUser));
            $rootScope.isAuthorized = true;
            $state.go('index.projects');
        });
    };

    $scope.login = function () {
        $auth.login($scope.loginData)
            .then(function (response) {
                $rootScope.currentUser = response.data.user;
                localStorage.setItem("currentUser", JSON.stringify($rootScope.currentUser));
                $rootScope.isAuthorized = true;
                // $location.path('/index/main');
                $state.go('index.projects');
            })
            .catch(function (error) {
                if (error.status == 406)
                    return;
                else if (error.status == 403)
                    return;
            });
    };
}

function TranslateController($translate, $scope) {
    $scope.changeLanguage = function (langKey) {
        $translate.use(langKey);
    };
}

function ProjectController($scope, $modal, projectService) {
    $scope.projects = projectService.getProjects().query();
    $scope.searchBox = null;

    var modalInstance = {
        templateUrl: 'public/views/projects/modals/create_project.html',
        controller: ModalCreateProjectController
    };

    $scope.filterProjects = function (element) {
        if ($scope.searchBox == null)
            return true;
        return (element.name.toUpperCase().match($scope.searchBox.toUpperCase()) ? true : false);
    };

    $scope.open = function () {

        $modal.open(modalInstance).result.then(function (result) {
            //Fake parameter
            $scope.fake = '?' + new Date().getTime();
        }, function (result) {
            //Fake parameter
            $scope.projects = projectService.getProjects().query();
        });
    };

    $scope.refresh = function () {
        $scope.projects = projectService.getProjects().query();
        $scope.searchBox = null;
    }
}

function ModalCreateProjectController($scope, $modalInstance, projectService) {
    $scope.project = {
        name: null,
        description: null,
        client: null,
        version: null
    };


    $scope.ok = function () {
        //This call first function on callback
        // $modalInstance.close(); 

        //This call second function on callback with value 'save' as resul parameter
        projectService.createProject().save($scope.project,
            function (resp, headers) {
                //success callback
                $modalInstance.dismiss('save');
            },
            function (error, status) {
                // error callback
                $modalInstance.dismiss('save');
            });

    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}
function NavigationHelpController($scope, $stateParams) {
    $scope.projectId = $stateParams.projectId;
    $scope.taskId = $stateParams.taskId;
}
function ProjectDetailsController($scope, $stateParams, projectService, $modal) {
    projectService.getProject().get({id: $stateParams.projectId}).$promise.then(function (result) {
        $scope.project = result;
        $scope.project.members.forEach(function (member) {
            if (member.role == 'OWNER') {
                $scope.projectOwner = member.user;
                return;
            }
        });
    });
    $scope.editProject = function () {
        var editProjectModal = {
            templateUrl: 'public/views/projects/modals/edit_project.html',
            controller: ModalEditProjectController,
            resolve: {
                project: function () {
                    return $scope.project;
                }
            }
        };

        $modal.open(editProjectModal).result.then(function (result) {
            //Fake parameter
        }, function (result) {
            //Fake parameter
            if (result == 'save') {
                $scope.project = projectService.getProject().get({id: $stateParams.projectId});
            }
        });
    }
}

function ModalEditProjectController($scope, $modalInstance, projectService, project) {
    $scope.project = jQuery.extend(true, {}, project);
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.update = function () {
        projectService.editProject().save($scope.project,
            function (resp, headers) {
                //success callback
                $modalInstance.dismiss('save');
            },
            function (error, status) {
                // error callback
            });
    };
}

function ModalCreateTaskController($scope, $modalInstance, taskService, project) {
    $scope.project = project;
    $scope.task = {
        name: null,
        description: null
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.save = function () {
        $scope.task.project = $scope.project;
        taskService.saveOrUpdate().save($scope.task,
            function (resp, headers) {
                //success callback
                $modalInstance.dismiss('save');
            },
            function (error, status) {
                // error callback
                $modalInstance.dismiss('save');
            });

    };
};

function TasksController($scope, $modal, $stateParams, projectService, taskService) {
    $scope.project = projectService.getProject().get({id: $stateParams.projectId});
    $scope.tasks = taskService.getProjectTasks().query({projectId: $stateParams.projectId});
    var createTaskModal = {
        templateUrl: 'public/views/tasks/modals/create_task.html',
        controller: ModalCreateTaskController,
        resolve: {
            project: function () {
                return $scope.project;
            }
        }
    };

    $scope.openCreateTaskModal = function () {
        if ($scope.tasks)
            $modal.open(createTaskModal).result.then(function (result) {
                //Fake parameter
            }, function (result) {
                //Fake parameter
                if (result == 'save') {
                    $scope.tasks = taskService.getProjectTasks().query({projectId: $stateParams.projectId});
                }
            });
    };

    $scope.editTask = function (taskId) {
        taskService.getTask().get({id: taskId}).$promise.then(function (result) {
            var editTaskModal = {
                templateUrl: 'public/views/tasks/modals/edit_task.html',
                controller: ModalEditTaskController,
                resolve: {
                    task: function () {
                        return result;
                    }
                }
            };

            $modal.open(editTaskModal).result.then(function (result) {
                //Fake parameter
            }, function (result) {
                //Fake parameter
                if (result == 'save') {
                    // taskService.getTask().get({id:taskId}).$promise.then(function (result) {
                    //     for(i=0;i<$scope.tasks.length;i++)
                    //     {
                    //         if($scope.tasks[i].id==taskId)
                    //         {
                    //             $scope.tasks[i]==result;
                    //             return;
                    //         }
                    //     }
                    // })
                    $scope.tasks = taskService.getProjectTasks().query({projectId: $stateParams.projectId});
                }
            });
        });
    }
}


function TaskDetailController($scope, $modal, $stateParams, projectService, taskService) {
    $scope.project = projectService.getProject().get({id: $stateParams.projectId});
    $scope.task = taskService.getTask().get({id: $stateParams.taskId});

    $scope.editTask = function () {
        var editTaskModal = {
            templateUrl: 'public/views/tasks/modals/edit_task.html',
            controller: ModalEditTaskController,
            resolve: {
                task: function () {
                    return $scope.task;
                }
            }
        };

        $modal.open(editTaskModal).result.then(function (result) {
            //Fake parameter
        }, function (result) {
            //Fake parameter
            if (result == 'save') {
                $scope.task = taskService.getTask().get({id: $stateParams.taskId});
            }
        });
    }

}

function ModalEditTaskController($scope, $modalInstance, taskService, task) {
    $scope.task = jQuery.extend(true, {}, task);
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.update = function () {
        taskService.saveOrUpdate().save($scope.task,
            function (resp, headers) {
                //success callback
                $modalInstance.dismiss('save');
            },
            function (error, status) {
                // error callback
            });
    };
}
function TaskCommentsController($scope, $stateParams, taskService) {
    $scope.taskId = $stateParams.taskId;
    $scope.newComment = {content: null};
    $scope.taskComments = taskService.getTaskComments().query({id: $scope.taskId});
    $scope.createComment = function () {
        taskService.saveOrUpdateComment($scope.taskId).save($scope.newComment,
            function (resp, headers) {
                // $scope.taskComments=taskService.getTaskComments().query({id:$scope.taskId});
                $scope.taskComments.push(resp);
                $scope.newComment = {content: null};
            },
            function (error, status) {

            });
    }
}
angular
    .module('inspinia')
    .controller('RegisterController', ['$scope', '$auth', '$translate', 'toastr', '$state', RegisterController])
    .controller('AuthenticationController', ['$scope', '$auth', '$rootScope', '$location', '$state', AuthenticationController])
    .controller('TranslateController', ['$translate', '$scope', TranslateController])
    .controller('ProjectController', ProjectController)
    .controller('ProjectDetailsController', ProjectDetailsController)
    .controller('NavigationHelpController', NavigationHelpController)
    .controller('TasksController', TasksController)
    .controller('TaskDetailController', TaskDetailController)
    .controller('TaskCommentsController', TaskCommentsController)
    .controller('ModalCreateTaskController', ModalCreateTaskController)
    .controller('ModalEditTaskController', ModalEditTaskController)
    .controller('MainCtrl', ['$scope', '$rootScope', '$auth', '$location', '$state', '$stateParams', MainCtrl]);