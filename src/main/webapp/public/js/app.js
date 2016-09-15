/**
 * INSPINIA - Responsive Admin Theme
 *
 */
(function () {
    angular.module('inspinia', [
        'ui.router',                    // Routing
        'oc.lazyLoad',                  // ocLazyLoad
        'ui.bootstrap',                 // Ui Bootstrap
        'pascalprecht.translate',       // Translations
        'ngSanitize',
        'ngResource',
        'angularFileUpload',
        '19degrees.ngSweetAlert2',
        'satellizer',
        'toastr',
        'smart-table'
    ])
})();

