'use strict';

angular.module('employeeRecordsApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


