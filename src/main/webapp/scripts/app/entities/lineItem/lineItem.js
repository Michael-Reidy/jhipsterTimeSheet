'use strict';

angular.module('employeeRecordsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lineItem', {
                parent: 'entity',
                url: '/lineItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'employeeRecordsApp.lineItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lineItem/lineItems.html',
                        controller: 'LineItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lineItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lineItem.detail', {
                parent: 'entity',
                url: '/lineItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'employeeRecordsApp.lineItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lineItem/lineItem-detail.html',
                        controller: 'LineItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lineItem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LineItem', function($stateParams, LineItem) {
                        return LineItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lineItem.new', {
                parent: 'lineItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/lineItem/lineItem-dialog.html',
                        controller: 'LineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    start: null,
                                    end: null,
                                    details: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('lineItem', null, { reload: true });
                    }, function() {
                        $state.go('lineItem');
                    })
                }]
            })
            .state('lineItem.edit', {
                parent: 'lineItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/lineItem/lineItem-dialog.html',
                        controller: 'LineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LineItem', function(LineItem) {
                                return LineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('lineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lineItem.delete', {
                parent: 'lineItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/lineItem/lineItem-delete-dialog.html',
                        controller: 'LineItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LineItem', function(LineItem) {
                                return LineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('lineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
