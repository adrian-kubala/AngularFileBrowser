(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('KatalogDeleteController',KatalogDeleteController);

    KatalogDeleteController.$inject = ['$uibModalInstance', 'entity', 'Katalog'];

    function KatalogDeleteController($uibModalInstance, entity, Katalog) {
        var vm = this;

        vm.katalog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Katalog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
