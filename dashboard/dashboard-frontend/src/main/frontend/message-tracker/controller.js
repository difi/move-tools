var angular = require("angular");
require("angular-route");

angular.module("test").controller("Test2Controller", [
    "$scope", 
    "TrackService", 
    "$routeParams",
    "$location",
    function ($scope, TrackService, $routeParams, $location) {
        $scope.res = null;

        $scope.update = function (res) {
            console.log(res);
            $scope.res = res;
            $scope.logs = res.content; 
            $scope.header = $scope.logs[0];
            $location.url("/track/" + $scope.form.conversationId);
        }; 
        
        $scope.find = function (search) {
            TrackService.get(search).then($scope.update);
        };
        

        TrackService.get($routeParams).then($scope.update);

        $scope.follow = function (link) {
            $scope.res.resource(link).get().$promise.then($scope.update);
        };

    }]);