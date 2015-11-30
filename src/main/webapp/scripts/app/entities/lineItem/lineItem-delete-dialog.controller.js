'use strict';

angular.module('employeeRecordsApp')
	.controller('LineItemDeleteController', function($scope, $modalInstance, entity, LineItem) {

        $scope.lineItem = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            LineItem.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });