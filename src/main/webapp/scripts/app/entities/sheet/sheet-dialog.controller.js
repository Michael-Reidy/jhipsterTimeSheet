'use strict';

angular.module('employeeRecordsApp').controller('SheetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Sheet', 'LineItem',
        function($scope, $stateParams, $modalInstance, entity, Sheet, LineItem) {

        $scope.sheet = entity;
        $scope.lineitems = LineItem.query();
        $scope.load = function(id) {
            Sheet.get({id : id}, function(result) {
                $scope.sheet = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('employeeRecordsApp:sheetUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sheet.id != null) {
                Sheet.update($scope.sheet, onSaveSuccess, onSaveError);
            } else {
                Sheet.save($scope.sheet, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
