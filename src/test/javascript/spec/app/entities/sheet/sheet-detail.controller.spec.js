'use strict';

describe('Sheet Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSheet, MockLineItem;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSheet = jasmine.createSpy('MockSheet');
        MockLineItem = jasmine.createSpy('MockLineItem');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Sheet': MockSheet,
            'LineItem': MockLineItem
        };
        createController = function() {
            $injector.get('$controller')("SheetDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'employeeRecordsApp:sheetUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
