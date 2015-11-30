'use strict';

angular.module('employeeRecordsApp').controller('EmployeeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Employee', 'Sheet',
        function($scope, $stateParams, $modalInstance, entity, Employee, Sheet) {

        $scope.employee = entity;
        $scope.sheets = Sheet.query();
        $scope.load = function(id) {
            Employee.get({id : id}, function(result) {
                $scope.employee = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('employeeRecordsApp:employeeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employee.id != null) {
                Employee.update($scope.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save($scope.employee, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
