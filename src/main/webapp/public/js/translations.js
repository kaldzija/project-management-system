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
                    LABEL_NAME: 'Name',
                PASSWORD: 'Password',
                EMAIL: 'Email',
                    PHONE: 'Phone',
                    BIRTH_DATE: 'Birth date',
                    LAST_LOGIN: 'Last login',
                    USER_PROFILE: 'User profile',
                    EDIT_USER_PROFILE: 'Edit user profile',
                    EDIT_USER_PROFILE: 'Edit user data',
                    USER_DATA: 'User data',
                    LABEL_REG_DATE: 'Registration date',
                FULL_NAME: 'Full name',
                LANGUAGE: 'Language',
                LABEL_NEW_PROJECT: 'New project',
                LABEL_CHANGE_SATUS: 'Change status',
                LABEL_STATUS: 'Status',
                LABEL_ACTIVE: 'Active',
                    LABEL_PROFESSION: 'Profession',
                LABEL_TODO: 'To Do',
                LABEL_INACTIVE: 'Inactive',
                LABEL_IN_PROGRESS: 'In progress',
                LABEL_COMPLETED: 'Completed',
                LABEL_PARTICIPANTS: 'Participants',
                LABEL_EDIT_PARTICIPANTS: 'Edit participants',
                LABEL_NEW_TASK: 'New task',
                LABEL_EDIT_TASK: 'Edit task',
                LABEL_COMMENTS: 'Comments',
                    LABEL_UPLOAD: 'Upload',
                    LABEL_LAST_UPDATED: 'Last Updated',
                    LABEL_ABOUT_ME: 'About me',
                    LABEL_PHOTO: 'Photo',
                LABEL_ATTACHMENTS: 'Attachments',
                LABEL_EDIT_PROJECT: 'Edit project',
                LABEL_SEARCH: 'Search',
                LABEL_CREATED: 'Created',
                LABEL_ADD: 'Add',
                LABEL_REMOVE: 'Remove',
                MESSAGE_FILL_ALL_FIELDS: 'Please fill all required fields',
                SENT_YOU_CONTACT_REQUEST: ' sent you contact request. ',
                LABEL_ACCEPT: ' Accept',
                LABEL_REJECT: ' Reject',
                LABEL_PHONE: ' Phone',
                LABEL_VIEW: ' View',
                LABEL_CONTACTS: 'Contacts',
                LABEL_MY_CONTACTS: 'My contacts',
                LABEL_SEND_REQUEST: 'Send request',
                LABEL_SENT_REQUEST: 'Your request has been sent.',
                LABEL_USER_ADDED_PROJECT: 'User successfully added to project.',
                LABEL_USER_REMOVED_PROJECT: 'User successfully removed from project.',
                LABEL_USERS: ' Users',
                LABEL_OTHER_USERS: ' Other users',
                LABEL_FIND_NEW_CONTACTS: ' Find new contacts',
                LABEL_NEW_CONTACTS: 'New contacts',
                LABEL_FIND_NEW_CONTACTS_MESSAGE_1: ' List contains all system users except administrators',
                LABEL_FIND_PARTICIPANTS_MESSAGE_1: ' List contains only your contacts',
                MESSAGE_NO_NOTIFICATIONS: 'There is no notifications.'
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