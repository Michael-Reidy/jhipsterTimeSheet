'use strict';

angular.module('employeeRecordsApp')
    .controller('LineItemController', function ($scope, $state, $modal, LineItem, ParseLinks) {
      
        $scope.lineItems = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            LineItem.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.lineItems = result;
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
            $scope.lineItem = {
                start: null,
                end: null,
                details: null,
                id: null
            };
        };
    });
