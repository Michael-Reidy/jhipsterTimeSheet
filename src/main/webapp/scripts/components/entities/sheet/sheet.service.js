'use strict';

angular.module('employeeRecordsApp')
    .factory('Sheet', function ($resource, DateUtils) {
        return $resource('api/sheets/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.weekending = DateUtils.convertDateTimeFromServer(data.weekending);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
