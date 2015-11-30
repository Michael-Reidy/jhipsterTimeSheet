'use strict';

angular.module('employeeRecordsApp')
    .controller('SheetDetailController', function ($scope, $rootScope, $stateParams, entity, Sheet, LineItem, Employee) {
        $scope.sheet = entity;
        $scope.load = function (id) {
            Sheet.get({id: id}, function(result) {
                $scope.sheet = result;
            });
        };
        var unsubscribe = $rootScope.$on('employeeRecordsApp:sheetUpdate', function(event, result) {
            $scope.sheet = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
