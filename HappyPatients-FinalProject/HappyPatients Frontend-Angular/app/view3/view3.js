'use strict';

angular.module('myApp.view3', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view3', {
    templateUrl: 'view3/view3.html',
    controller: 'View3Ctrl'
  });
}])

.controller('View3Ctrl', ['$scope', '$http', function($scope, $http) {
    $scope.flag = false;
    $scope.getFilterResult = function() {
        $http({
            method: 'POST',
            url: '/Crew/PatientInfoFilter',
            data: {
                "Filter Type":$scope.FilterType,
                "Filter Value": $scope.FilterValue
            }
        }).then(function(response){
            $scope.flag = true;
            $scope.result = response.data;
            console.log("got");
        });
    }




}]);