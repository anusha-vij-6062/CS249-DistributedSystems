'use strict';

angular.module('myApp.view2', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view2', {
    templateUrl: 'view2/view2.html',
    controller: 'View2Ctrl'
  });
}])

.controller('View2Ctrl', ['$scope', '$http', function($scope, $http) {
    $scope.flag = false;
    $scope.flagNoRecord = false;
    $scope.getPatientResult = function() {
        $http({
            method: 'POST',
            url: '/Crew/PatientInfo',
            data: {
                "Patient Name":$scope.PatientName
            }
        }).then(function(response){
          if(response.data.length > 0) {
              $scope.flag = true;
              $scope.result = response.data;
              $scope.flagNoRecord = false;
          }
          else
          {
            $scope.flagNoRecord = true;
            $scope.norecord = "no Record";
              $scope.flag = false;
          }
            console.log("got");
        });
    }

}]);