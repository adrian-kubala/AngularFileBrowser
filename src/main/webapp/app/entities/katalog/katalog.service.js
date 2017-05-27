(function() {
    'use strict';
    angular
        .module('angularFileBrowserApp')
        .factory('Katalog', Katalog);

    Katalog.$inject = ['$resource'];

    function Katalog ($resource) {
        var resourceUrl =  'api/katalogs/:id';

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
