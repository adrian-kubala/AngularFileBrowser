(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('PlikDeleteController',PlikDeleteController);

    PlikDeleteController.$inject = ['$uibModalInstance', 'entity', 'Plik'];

    function PlikDeleteController($uibModalInstance, entity, Plik) {
        var vm = this;

        vm.plik = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Plik.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
