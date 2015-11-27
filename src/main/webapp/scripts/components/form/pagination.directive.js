/* globals $ */
'use strict';

angular.module('employeeRecordsApp')
    .directive('employeeRecordsAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
