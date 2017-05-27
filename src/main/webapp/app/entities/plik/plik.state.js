(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('plik', {
            parent: 'entity',
            url: '/plik',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'angularFileBrowserApp.plik.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/plik/pliks.html',
                    controller: 'PlikController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('plik');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('plik-detail', {
            parent: 'plik',
            url: '/plik/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'angularFileBrowserApp.plik.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/plik/plik-detail.html',
                    controller: 'PlikDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('plik');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Plik', function($stateParams, Plik) {
                    return Plik.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'plik',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('plik-detail.edit', {
            parent: 'plik-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/plik/plik-dialog.html',
                    controller: 'PlikDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Plik', function(Plik) {
                            return Plik.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('plik.new', {
            parent: 'plik',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/plik/plik-dialog.html',
                    controller: 'PlikDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nazwa: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('plik', null, { reload: 'plik' });
                }, function() {
                    $state.go('plik');
                });
            }]
        })
        .state('plik.edit', {
            parent: 'plik',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/plik/plik-dialog.html',
                    controller: 'PlikDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Plik', function(Plik) {
                            return Plik.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('plik', null, { reload: 'plik' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('plik.delete', {
            parent: 'plik',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/plik/plik-delete-dialog.html',
                    controller: 'PlikDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Plik', function(Plik) {
                            return Plik.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('plik', null, { reload: 'plik' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
