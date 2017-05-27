(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('KatalogDialogController', KatalogDialogController);

    KatalogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Katalog', 'Plik'];

    function KatalogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Katalog, Plik) {
        var vm = this;

        vm.katalog = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pliks = Plik.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.katalog.id !== null) {
                Katalog.update(vm.katalog, onSaveSuccess, onSaveError);
            } else {
                Katalog.save(vm.katalog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('angularFileBrowserApp:katalogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
