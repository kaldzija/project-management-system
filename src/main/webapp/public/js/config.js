/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/index/main");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider

        .state('index', {
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
}
angular
    .module('inspinia')
    .config(function ($authProvider) {
        $authProvider.linkedin({
            clientId: ''
        });
        $authProvider.facebook({
            clientId: '1780516158860280'
        });
        $authProvider.google({
            clientId: ''
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
