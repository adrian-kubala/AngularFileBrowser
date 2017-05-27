(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('katalog', {
            parent: 'entity',
            url: '/katalog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'angularFileBrowserApp.katalog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/katalog/katalogs.html',
                    controller: 'KatalogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('katalog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('katalog-detail', {
            parent: 'katalog',
            url: '/katalog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'angularFileBrowserApp.katalog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/katalog/katalog-detail.html',
                    controller: 'KatalogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('katalog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Katalog', function($stateParams, Katalog) {
                    return Katalog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'katalog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('katalog-detail.edit', {
            parent: 'katalog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/katalog/katalog-dialog.html',
                    controller: 'KatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Katalog', function(Katalog) {
                            return Katalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('katalog.new', {
            parent: 'katalog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/katalog/katalog-dialog.html',
                    controller: 'KatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nazwa: null,
                                nadrzedny: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('katalog', null, { reload: 'katalog' });
                }, function() {
                    $state.go('katalog');
                });
            }]
        })
        .state('katalog.edit', {
            parent: 'katalog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/katalog/katalog-dialog.html',
                    controller: 'KatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Katalog', function(Katalog) {
                            return Katalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('katalog', null, { reload: 'katalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('katalog.delete', {
            parent: 'katalog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/katalog/katalog-delete-dialog.html',
                    controller: 'KatalogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Katalog', function(Katalog) {
                            return Katalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('katalog', null, { reload: 'katalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
