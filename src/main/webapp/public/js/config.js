/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/index/projects");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider

        .state('index', {
            abstract: true,
            url: "/index",
            templateUrl: "public/views/common/content.html"
        }).state('users', {
            abstract: true,
            url: "/index",
            templateUrl: "public/views/common/content.html"
        })
        .state('index.main', {
            url: "/main",
            templateUrl: "public/views/main.html",
            data: {pageTitle: 'Example view'}
        })
        .state('index.minor', {
            url: "/minor",
            templateUrl: "public/views/minor.html",
            data: {pageTitle: 'Example view'}
        })
        .state('index.projects', {
            url: "/projects",
            templateUrl: "public/views/projects/projects.html",
            data: {pageTitle: 'Projects view'}
        })
        .state('index.project-details', {
            url: "/projects/:projectId",
            templateUrl: "public/views/projects/project_detail.html",
            data: {pageTitle: 'Project details'}
        })
        .state('index.project-participants', {
            url: "/projects/:projectId/participants",
            templateUrl: "public/views/projects/project_team.html",
            data: {pageTitle: 'Participants'}
        })
        .state('index.task-details', {
            url: "/projects/:projectId/tasks/:taskId",
            templateUrl: "public/views/tasks/task_detail.html",
            data: {pageTitle: 'Task details'}
        })
        .state('index.issue-tracker', {
            url: "/projects/:projectId/tasks",
            templateUrl: "public/views/projects/issue_tracker.html",
            data: {pageTitle: 'Task list'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: ['public/js/footable/footable.all.min.js', 'public/css/footable/footable.core.css']
                        },
                        {
                            name: 'ui.footable',
                            files: ['public/js/footable/angular-footable.js']
                        }
                    ]);
                }
            }
        })
        .state('login', {
            url: "/login",
            templateUrl: "public/views/login.html",
            data: {pageTitle: 'Login', specialClass: 'gray-bg'}
        })
        .state('register', {
            url: "/register",
            templateUrl: "public/views/register/register.html",
            data: {pageTitle: 'Register', specialClass: 'gray-bg'}
        })
        .state('users.contacts', {
            url: "/users/contacts",
            templateUrl: "public/views/users/contacts_2.html",
            data: {pageTitle: 'My contacts'}
        })
        .state('users.chat', {
            url: "/users/messages",
            templateUrl: "public/views/users/chat_view.html",
            data: {pageTitle: 'Messages'}
        })
        .state('index.project-agile', {
            url: "/projects/:projectId/agile",
            templateUrl: "public/views/agile/agile_board.html",
            data: {pageTitle: 'Agile'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'ui.sortable',
                            files: ['public/js/ui-sortable/sortable.js']
                        }
                    ]);
                }
            }
        })
        .state('users.other-users', {
            url: "/users/all",
            templateUrl: "public/views/users/other_users.html",
            data: {pageTitle: 'Other users'}
        })
        .state('users.profile', {
            url: "/users/veiw/:userId",
            templateUrl: "public/views/users/user-profile.html",
            data: {pageTitle: 'Other users'}
        })
        .state('users.edit', {
            url: "/users/edit",
            templateUrl: "public/views/users/edit-user-profile.html",
            data: {pageTitle: 'Edit user data'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'datePicker',
                            files: ['public/css/datapicker/angular-datapicker.css', 'public/js/datapicker/angular-datepicker.js']
                        },
                        {
                            serie: true,
                            files: ['public/js/moment/moment.min.js', 'public/js/daterangepicker/daterangepicker.js', 'public/css/daterangepicker/daterangepicker-bs3.css']
                        }
                    ]);
                }
            }
        })
}
angular
    .module('inspinia')
    .config(function ($authProvider) {
        $authProvider.linkedin({
            clientId: '77xho5nl8rffye'
        });
        $authProvider.facebook({
            clientId: '1780516158860280'
        });
        $authProvider.google({
            clientId: '301961595404-acb1crr9g3lctiheb4ku1u9ts1hablak.apps.googleusercontent.com'
        });
        $authProvider.twitter({
            url: '/auth/twitter'
        });
    })
    .config(config)
    .run(function ($rootScope, $state, $location) {
        $rootScope.$state = $state;

        $rootScope.$on('$stateChangeStart', function (e, toState, toParams, fromState, fromParams) {
            if (toState.name == 'register' && $rootScope.currentUser == null)
                return;
              
            if (toState.name != 'login' && $rootScope.currentUser == null) {
                $location.path('login');
            }
            if (toState.name == 'login' && $rootScope.currentUser != null)
                $location.path('/index/main');
        });
    });
