(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('KatalogController', KatalogController);

    KatalogController.$inject = ['Katalog'];

    function KatalogController(Katalog) {

        var vm = this;

        vm.katalogs = [];

        loadAll();

        function loadAll() {
            Katalog.query(function(result) {
                vm.katalogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
