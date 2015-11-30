'use strict';

angular.module('employeeRecordsApp')
	.controller('EmployeeDeleteController', function($scope, $modalInstance, entity, Employee) {

        $scope.employee = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });