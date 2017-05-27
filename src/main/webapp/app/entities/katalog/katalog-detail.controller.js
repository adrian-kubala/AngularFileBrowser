(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('KatalogDetailController', KatalogDetailController);

    KatalogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Katalog', 'Plik'];

    function KatalogDetailController($scope, $rootScope, $stateParams, previousState, entity, Katalog, Plik) {
        var vm = this;

        vm.katalog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('angularFileBrowserApp:katalogUpdate', function(event, result) {
            vm.katalog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
