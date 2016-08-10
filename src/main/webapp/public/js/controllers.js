/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl($scope, $rootScope, $auth, $location, $state) {
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
            $state.go('index.main');
        });
    };

    $scope.login = function () {
        $auth.login($scope.loginData)
            .then(function (response) {
                $rootScope.currentUser = response.data.user;
                localStorage.setItem("currentUser", JSON.stringify($rootScope.currentUser));
                $rootScope.isAuthorized = true;
                // $location.path('/index/main');
                $state.go('index.main');
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
        description: null
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
angular
    .module('inspinia')
    .controller('RegisterController', ['$scope', '$auth', '$translate', 'toastr', '$state', RegisterController])
    .controller('AuthenticationController', ['$scope', '$auth', '$rootScope', '$location', '$state', AuthenticationController])
    .controller('TranslateController', ['$translate', '$scope', TranslateController])
    .controller('ProjectController', ProjectController)
    .controller('MainCtrl', ['$scope', '$rootScope', '$auth', '$location', '$state', MainCtrl]);