'use strict';

angular.module('employeeRecordsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sheet', {
                parent: 'entity',
                url: '/sheets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'employeeRecordsApp.sheet.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sheet/sheets.html',
                        controller: 'SheetController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sheet');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sheet.detail', {
                parent: 'entity',
                url: '/sheet/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'employeeRecordsApp.sheet.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sheet/sheet-detail.html',
                        controller: 'SheetDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sheet');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Sheet', function($stateParams, Sheet) {
                        return Sheet.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sheet.new', {
                parent: 'sheet',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sheet/sheet-dialog.html',
                        controller: 'SheetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    projectname: null,
                                    weekending: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sheet', null, { reload: true });
                    }, function() {
                        $state.go('sheet');
                    })
                }]
            })
            .state('sheet.edit', {
                parent: 'sheet',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sheet/sheet-dialog.html',
                        controller: 'SheetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sheet', function(Sheet) {
                                return Sheet.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sheet', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sheet.delete', {
                parent: 'sheet',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sheet/sheet-delete-dialog.html',
                        controller: 'SheetDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Sheet', function(Sheet) {
                                return Sheet.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sheet', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
