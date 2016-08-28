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
    $scope.loadProjects = function () {
        projectService.getProjects().query().$promise.then(function (result) {
            $scope.projects = result;
            if ($scope.projects.length != null) {
                $scope.projects.forEach(function (entry) {
                    projectService.getProjectPercentage().get({id: entry.id}).$promise.then(function (result) {
                        entry.percentage = result.percentage;
                    });
                });
            }
        });
    };
    $scope.loadProjects();
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

        }, function (result) {
            $scope.projects = projectService.getProjects().query();
        });
    };

    $scope.refresh = function () {
        $scope.loadProjects();
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
    $scope.projectMembers = projectService.getProjectMembers().query({projectId: $stateParams.projectId});
    $scope.projectOwner = projectService.getProjectOwner().get({projectId: $stateParams.projectId});
    $scope.otherInformations = {};
    $scope.projectFiles = projectService.getProjectFiles().query({projectId: $stateParams.projectId});
    $scope.$on('newProjectDocumentUploaded', function (event, args) {
        $scope.projectFiles.push(args);
    });

    $scope.searchBox = {};
    $scope.searchBox.value = null;
    $scope.fileFilter = function (element) {
        if ($scope.searchBox.value == null)
            return true;
        return (element.fileName.toUpperCase().match($scope.searchBox.value.toUpperCase()) ? true : false);
    };

    $scope.convert = function (bytes) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
        var precision = 2;
        if (typeof precision === 'undefined') precision = 1;
        var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
            number = Math.floor(Math.log(bytes) / Math.log(1024));
        return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number]
    };
    
    projectService.getProject().get({id: $stateParams.projectId}).$promise.then(function (result) {
        $scope.project = result;
        projectService.getProjectPercentage().get({id: result.id}).$promise.then(function (resultPercentage) {
            $scope.otherInformations.percentage = resultPercentage.percentage;
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
    };

    $scope.changeStatus = function () {
        var editProjectModal = {
            templateUrl: 'public/views/projects/modals/project_status.html',
            controller: ModalEditProjectController,
            size: 'sm',
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
    $scope.projectOwner = projectService.getProjectOwner().get({projectId: $stateParams.projectId});
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
    $scope.projectOwner = projectService.getProjectOwner().get({projectId: $stateParams.projectId});
    $scope.task = taskService.getTask().get({id: $stateParams.taskId});
    $scope.taskFiles = taskService.getTaskFiles().query({taskId: $stateParams.taskId});
    $scope.$on('newTaskDocumentUploaded', function (event, args) {
        $scope.taskFiles.push(args);
    });

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
    };
    $scope.searchBox = {};
    $scope.searchBox.value = null;
    $scope.fileFilter = function (element) {
        if ($scope.searchBox.value == null)
            return true;
        return (element.fileName.toUpperCase().match($scope.searchBox.value.toUpperCase()) ? true : false);
    };
    
    $scope.changeStatus = function () {
        var editTaskModal = {
            templateUrl: 'public/views/tasks/modals/task_status.html',
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
    };

    $scope.changeAssignment = function () {
        var editTaskModal = {
            templateUrl: 'public/views/tasks/modals/task_assign.html',
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
    };

    $scope.convert = function (bytes) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
        var precision = 2;
        if (typeof precision === 'undefined') precision = 1;
        var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
            number = Math.floor(Math.log(bytes) / Math.log(1024));
        return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number]
    }
};

function ModalEditTaskController($scope, $modalInstance, userService, $stateParams, taskService, task) {
    $scope.task = jQuery.extend(true, {}, task);
    $scope.isSelected = function (id1, id2) {
        return id1 == id2;
    };
    $scope.projectUsers = userService.getProjectMembers().query({projectId: $stateParams.projectId});
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

function ProjectCommentsController($scope, $stateParams, projectService) {
    $scope.projectId = $stateParams.projectId;
    $scope.newComment = {content: null};
    $scope.projectComments = projectService.getProjectComments().query({id: $scope.projectId});
    $scope.createComment = function () {
        projectService.saveOrUpdateComment($scope.projectId).save($scope.newComment,
            function (resp, headers) {
                // $scope.taskComments=taskService.getTaskComments().query({id:$scope.taskId});
                $scope.projectComments.push(resp);
                $scope.newComment = {content: null};
            },
            function (error, status) {

            });
    }
}


function UsersDetailsController($scope, $stateParams, userService) {
    $scope.user = userService.getUser().get({userId: $stateParams.userId});
}

function EditUserDataController($scope, $rootScope, $stateParams, userService, $http) {
    $scope.user = userService.getUser().get({userId: $rootScope.currentUser.id});

    $scope.update = function () {
        userService.saveOrUpdate().save($scope.user,
            function (resp, headers) {
                $scope.user = resp;
            },
            function (error, status) {

            });
    };

    $scope.cancel = function () {
        $scope.user = userService.getUser().get({userId: $rootScope.currentUser.id});
    };


    $scope.uploadImage = function () {
        if ($scope.files == null)
            return;
        var fd = new FormData();
        console.log($scope.files);
        angular.forEach($scope.files, function (file) {
            fd.append('file', file);
        });
        $scope.files = null;
        $scope.showLoading = true;
        $http.post('/api/image/user/' + $scope.user.id, fd,
            {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (d) {
            angular.element("input[type='file']").val(null);
            $scope.showLoading = false;
        })


    }
}

function NotificationController($scope, userService) {

    $scope.notifications = userService.getNotifications().query();
    $scope.resolveContact = function (notificationId, accept) {
        userService.resolveContact(notificationId, accept).save({id: 0},
            function (resp, headers) {
                $scope.notifications = resp.result;
                $scope.showNoNotificationMessage = $scope.notifications.length == 0;
            },
            function (error, status) {

            });
    }
}
function FindContactController($scope, userService, $translate, toastr) {

    $scope.users = userService.getOtherUsers().query();
    $scope.searchBox = null;
    $scope.filterUsers = function (element) {
        if ($scope.searchBox == null)
            return true;
        var matchName = element.name.toUpperCase().match($scope.searchBox.toUpperCase());
        var matchEmail = element.email ? element.email.toUpperCase().match($scope.searchBox.toUpperCase()) : false;
        return matchEmail || matchName;
    };

    $scope.createContact = function (user) {
        userService.createContact().save({receiver: {id: user.id}},
            function (resp, headers) {
                user.hideSendRequest = true;
                $translate('LABEL_SENT_REQUEST').then(function (value) {
                    toastr.success(value);
                });
            },
            function (error, status) {

            });
    }
}
function ProjectUsersController($scope, $stateParams, userService, $translate, toastr) {
    $scope.projectId = $stateParams.projectId;
    $scope.users = userService.getUserWithProjectRole().query({projectId: $scope.projectId});
    $scope.searchBox = null;
    $scope.filterUsers = function (element) {
        if ($scope.searchBox == null)
            return true;
        var matchName = element.name.toUpperCase().match($scope.searchBox.toUpperCase());
        var matchEmail = element.email ? element.email.toUpperCase().match($scope.searchBox.toUpperCase()) : false;
        return matchEmail || matchName;
    };

    $scope.addUser = function (user) {
        userService.createProjectMember().save({user: {id: user.id}, project: {id: $scope.projectId}},
            function (resp, headers) {
                user.projectRole = 'MEMBER';
                $translate('LABEL_USER_ADDED_PROJECT').then(function (value) {
                    toastr.success(value);
                });
            },
            function (error, status) {

            });
    };

    $scope.removeUser = function (user) {
        userService.createProjectMember().remove({userId: user.id, projectId: $scope.projectId},
            function (resp, headers) {
                user.projectRole = '';
                $translate('LABEL_USER_ADDED_PROJECT').then(function (value) {
                    toastr.success(value);
                });
            },
            function (error, status) {

            });
    }
}

function ContactsController($scope, userService, $translate, toastr) {

    $scope.contacts = userService.getContacts().query();
}

function TaskFileUploadControllerTest($scope, userService, $translate, toastr, FileUploader, $auth, task, $http) {

    $scope.contacts = userService.getContacts().query();

    var uploader = $scope.uploader = new FileUploader({
        url: '/api/files/task',
        headers: {"Authorization": "Bearer " + $auth.getToken()},
        formData: [{}],
        type: 'post'
    });

    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function (item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });

    // CALLBACKS


    uploader.onCompleteItem = function (fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        console.log(response);
    };

    $scope.upload = function () {
        uploader.onBeforeUploadItem = function (item) {
            item.url = '/api/files/task';
        };

        uploader.url = '/api/files/task';
        uploader.uploadAll();
    };

    $scope.uploadFile = function () {
        var fd = new FormData();
        console.log($scope.files);
        angular.forEach($scope.files, function (file) {
            fd.append('file', file);
        });
        $http.post('http://localhost:8080/api/files/task', fd,
            {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (d) {
            console.log(d);
        })


    }
}

function FileUploadController($scope, taskService, $stateParams, $http) {
    $scope.uploadFile = function (type) {
        var typePath = '';
        var emiter = '';
        if (type == 'project') {
            typePath = 'project/' + $stateParams.projectId;
            emiter = 'newProjectDocumentUploaded';
        }
        else {
            emiter = 'newTaskDocumentUploaded';
            typePath = 'task/' + $stateParams.taskId;
        }

        if ($scope.files == null)
            return;
        var fd = new FormData();
        console.log($scope.files);
        angular.forEach($scope.files, function (file) {
            fd.append('file', file);
        });
        $scope.files = null;
        $scope.showLoading = true;
        $http.post('/api/files/' + typePath, fd,
            {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (d) {
            angular.element("input[type='file']").val(null);
            $scope.$emit(emiter, d);
            $scope.showLoading = false;
        })

    }
}

function AgileController($scope, projectService, taskService, $stateParams) {
    $scope.todoList = [];
    $scope.inProgressList = [];
    $scope.completedList = [];
    $scope.projectId = $stateParams.projectId;
    taskService.getUserProjectTasks().query({projectId: $stateParams.projectId}).$promise.then(function (result) {
        $scope.tasks = result;
        if ($scope.tasks.length != null) {
            $scope.tasks.forEach(function (entry) {
                switch (entry.status) {
                    case "TODO":
                        $scope.todoList.push(entry);
                        break;
                    case "IN_PROGRESS":
                        $scope.inProgressList.push(entry);
                        break;
                    case "COMPLETED":
                        $scope.completedList.push(entry);
                        break;
                }
            });
        }
    });

    projectService.getProject().get({id: $stateParams.projectId}).$promise.then(function (result) {
        $scope.project = result;
        $scope.sortableOptions = {
            connectWith: result.status == 'ACTIVE' ? ".connectList" : "",
            receive: function (event, ui) {
                $scope.target = $(this)[0].id;
            },
            stop: function (event, ui) {
                var list;
                switch ($scope.target) {
                    case "TODO":
                        list = $scope.todoList;
                        break;
                    case "IN_PROGRESS":
                        list = $scope.inProgressList;
                        break;
                    case "COMPLETED":
                        list = $scope.completedList;
                }
                var index;
                var task = ui.item.sortable.model;
                for (i = 0; i < list.length; i++) {
                    if (list[i].id == task.id) {
                        index = i;
                        break;
                    }
                }
                taskService.getTask().get({id: task.id}).$promise.then(function (result) {
                    result.status = $scope.target;
                    taskService.saveOrUpdate().save(result);
                });
            }
        };
    });
}

function MessagesController($scope, $rootScope, messageService) {
    $scope.newMessage = "";
    messageService.getUsers().query().$promise.then(function (result) {
        $scope.users = result;
        if ($scope.users.length > 0) {
            $scope.selectedUser = $scope.users[0];
            messageService.getMessages().query({userId: $scope.selectedUser.id}).$promise.then(function (messages) {
                $scope.messages = messages;
            });
        }

    });
    $scope.select = function (user) {
        $scope.selectedUser = user;
        messageService.getMessages().query({userId: $scope.selectedUser.id}).$promise.then(function (messages) {
            $scope.messages = messages;
        });
    };

    $scope.send = function () {
        var message = {content: $scope.newMessage, toUser: {id: $scope.selectedUser.id}};
        messageService.save().save(message,
            function (resp, headers) {
                $scope.messages.push(resp);
                $scope.newMessage = ""
            },
            function (error, status) {

            });
    }
}

function MessagesCountController($scope, $timeout, messageService) {
    $scope.unread = messageService.getUnread().get();

    (function refresh() {
        messageService.getUnread().get().$promise.then(function (result) {
            $scope.unread = result;
            $timeout(refresh, 5000);
        })
    })();

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
    .controller('ProjectCommentsController', ProjectCommentsController)
    .controller('TaskCommentsController', TaskCommentsController)
    .controller('ModalCreateTaskController', ModalCreateTaskController)
    .controller('ModalEditTaskController', ModalEditTaskController)
    .controller('NotificationController', NotificationController)
    .controller('ContactsController', ContactsController)
    .controller('FindContactController', FindContactController)
    .controller('ProjectUsersController', ProjectUsersController)
    .controller('ProjectUsersController', ProjectUsersController)
    .controller('FileUploadController', FileUploadController)
    .controller('UsersDetailsController', UsersDetailsController)
    .controller('EditUserDataController', EditUserDataController)
    .controller('AgileController', AgileController)
    .controller('MessagesController', MessagesController)
    .controller('MessagesCountController', MessagesCountController)
    .controller('MainCtrl', ['$scope', '$rootScope', '$auth', '$location', '$state', '$stateParams', MainCtrl]);