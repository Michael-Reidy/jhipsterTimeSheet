/* globals $ */
'use strict';

angular.module('employeeRecordsApp')
    .directive('employeeRecordsAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
