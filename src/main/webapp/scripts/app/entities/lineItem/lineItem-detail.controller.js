'use strict';

angular.module('employeeRecordsApp')
    .controller('LineItemDetailController', function ($scope, $rootScope, $stateParams, entity, LineItem) {
        $scope.lineItem = entity;
        $scope.load = function (id) {
            LineItem.get({id: id}, function(result) {
                $scope.lineItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('employeeRecordsApp:lineItemUpdate', function(event, result) {
            $scope.lineItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
