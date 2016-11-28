var angular = require("angular");

angular.module("test").factory('TrackService', ["$http", "$resource", function ($http, $resource) {
        if (process.env.ENV === 'production') {
            var Conversation = $resource('/sporing/api/track/:conversationId', {}, {query: {isArray:false}});
        } else {
            var Conversation = $resource('http://127.0.0.1:18080/api/track/:conversationId', {}, {query: {isArray:false}});
        }
        
        
        var service = {
            get: function (params) {
                return Conversation.get(params).$promise;
            }
        };
        return service;
    }]);