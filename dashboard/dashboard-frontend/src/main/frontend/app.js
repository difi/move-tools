var angular = require("angular");
require("angular-route");
require("angular-resource"); 
require("angular-hateoas");
require("angular-animate");
require("./message-tracker");
require("jquery");
require("bootstrap-webpack");

angular.module('app', ['ngRoute', 'ngAnimate', "ngResource", "hateoas", "test"])
.config(['$routeProvider', '$locationProvider', "HateoasInterceptorProvider", "HateoasInterfaceProvider",
  function($routeProvider, $locationProvider, HateoasInterceptorProvider, HateoasInterfaceProvider) {
    $routeProvider  
      .when('/', {
        template: require('./message-tracker/latest.html'),
        controller: 'TestController'
      })
      .when('/track/:orgNumber/:conversationId', {
        template: require('./message-tracker/view.html'),
        controller: 'Test2Controller'
      }).otherwise({redirectTo:'/'});

    HateoasInterceptorProvider.transformAllResponses();
    $locationProvider.html5Mode(true);
}]);