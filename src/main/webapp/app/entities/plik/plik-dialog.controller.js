(function() {
    'use strict';

    angular
        .module('angularFileBrowserApp')
        .controller('PlikDialogController', PlikDialogController);

    PlikDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Plik', 'Katalog'];

    function PlikDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Plik, Katalog) {
        var vm = this;

        vm.plik = entity;
        vm.clear = clear;
        vm.save = save;
        vm.katalogs = Katalog.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.plik.id !== null) {
                Plik.update(vm.plik, onSaveSuccess, onSaveError);
            } else {
                Plik.save(vm.plik, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('angularFileBrowserApp:plikUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
