(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('PlikController', PlikController);

    PlikController.$inject = ['Plik'];

    function PlikController(Plik) {

        var vm = this;

        vm.pliks = [];

        loadAll();

        function loadAll() {
            Plik.query(function(result) {
                vm.pliks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
