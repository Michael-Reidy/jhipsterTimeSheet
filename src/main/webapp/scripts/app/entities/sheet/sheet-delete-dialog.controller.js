'use strict';

angular.module('employeeRecordsApp')
	.controller('SheetDeleteController', function($scope, $modalInstance, entity, Sheet) {

        $scope.sheet = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Sheet.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });