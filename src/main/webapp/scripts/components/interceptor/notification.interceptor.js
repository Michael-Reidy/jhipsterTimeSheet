 'use strict';

angular.module('employeeRecordsApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-employeeRecordsApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-employeeRecordsApp-params')});
                }
                return response;
            }
        };
    });
