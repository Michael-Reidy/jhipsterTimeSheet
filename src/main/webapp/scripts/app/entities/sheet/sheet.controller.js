'use strict';

angular.module('employeeRecordsApp')
    .controller('SheetController', function ($scope, $state, $modal, Sheet, ParseLinks) {
      
        $scope.sheets = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Sheet.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sheets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sheet = {
                projectname: null,
                weekending: null,
                id: null
            };
        };
    });
