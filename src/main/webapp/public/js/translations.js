(function () {
    function translateConfig($translateProvider) {
        $translateProvider
            .translations('en', {
                HOME_PAGE: "Home",
                DISAPPROVE: 'Disapprove',
                REGISTRATION_SUCCESS: 'You successfully created your account.',
                ERROR_MESSAGE_USER_EXIST: 'User with this email already exists.',
                ERROR_REQUIRED: '{{field}} field is required.',
                DISPLAY_NAME: 'Display name',
                PASSWORD: 'Password',
                EMAIL: 'Email',
                FULL_NAME: 'Full name',
                LANGUAGE: 'Language',
                LABEL_NEW_PROJECT: 'New project',
                LABEL_NEW_TASK: 'New task',
                LABEL_EDIT_TASK: 'Edit task',
                LABEL_COMMENTS: 'Comments',
                LABEL_ATTACHMENTS: 'Attachments',
                LABEL_EDIT_PROJECT: 'Edit project',
                LABEL_CREATED: 'Created',
                LABEL_ACTIVE: 'Active',
                LABEL_INACTIVE: 'Inactive',
                MESSAGE_FILL_ALL_FIELDS: 'Please fill all required fields'
            })
            .translations('bs', {
                HOME_PAGE: "Home",
                DISAPPROVE: 'Disapprove',
                REGISTRATION_SUCCESS: 'You successfully created your account.',
                ERROR_REQUIRED: '{{field}} field is required.',
                LANGUAGE: 'Jezik'
            });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useSanitizeValueStrategy('sanitize');
    }

    /**
     * ADD TRANSLATIONS TO MODULE
     */
    angular.module('inspinia').config(translateConfig);
})();