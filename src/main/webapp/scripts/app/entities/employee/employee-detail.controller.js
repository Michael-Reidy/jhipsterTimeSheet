'use strict';

angular.module('employeeRecordsApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, Sheet) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        var unsubscribe = $rootScope.$on('employeeRecordsApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
