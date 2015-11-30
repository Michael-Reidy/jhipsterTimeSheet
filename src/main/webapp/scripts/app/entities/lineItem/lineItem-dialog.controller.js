'use strict';

angular.module('employeeRecordsApp').controller('LineItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LineItem', 'Sheet',
        function($scope, $stateParams, $modalInstance, entity, LineItem, Sheet) {

        $scope.lineItem = entity;
        $scope.sheets = Sheet.query();
        $scope.load = function(id) {
            LineItem.get({id : id}, function(result) {
                $scope.lineItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('employeeRecordsApp:lineItemUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.lineItem.id != null) {
                LineItem.update($scope.lineItem, onSaveSuccess, onSaveError);
            } else {
                LineItem.save($scope.lineItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
