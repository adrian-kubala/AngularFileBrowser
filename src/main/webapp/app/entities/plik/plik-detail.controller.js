(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('PlikDetailController', PlikDetailController);

    PlikDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Plik', 'Katalog'];

    function PlikDetailController($scope, $rootScope, $stateParams, previousState, entity, Plik, Katalog) {
        var vm = this;

        vm.plik = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('angularFileBrowserApp:plikUpdate', function(event, result) {
            vm.plik = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
