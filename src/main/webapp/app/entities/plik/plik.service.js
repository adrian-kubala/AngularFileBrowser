(function() {
    'use strict';
    angular
        .module('angularFileBrowserApp')
        .factory('Plik', Plik);

    Plik.$inject = ['$resource'];

    function Plik ($resource) {
        var resourceUrl =  'api/pliks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
