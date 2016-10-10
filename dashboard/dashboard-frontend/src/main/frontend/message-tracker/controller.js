var angular = require("angular");
require("angular-route");

angular.module("test").controller("TestController", ["$scope", "TrackService", function ($scope, TrackService) {

        $scope.context = null;
        $scope.conversations = [];
        
        $scope.update = function (res) {
            $scope.context = res;
            $scope.conversations = res.content;
        };

        $scope.find = function (search) {
            if (search.c !== undefined && search.c === "") {
                search.c = undefined;
            }
            TrackService.find(search).then($scope.update);
        };
        
        $scope.follow = function (link) {
            $scope.context.resource(link).get().$promise.then($scope.update);
        };

    }]);

angular.module("test").controller("Test2Controller", [
    "$scope", 
    "TrackService", 
    "$routeParams",
    function ($scope, TrackService, $routeParams) {
        $scope.res = null;

        $scope.update = function (res) {
            console.log(res);
            $scope.res = res;
            $scope.logs = res.content; 
            $scope.header = $scope.logs[0];
        }; 

        TrackService.get($routeParams).then($scope.update);

        $scope.follow = function (link) {
            $scope.res.resource(link).get().$promise.then($scope.update);
        };

    }]);