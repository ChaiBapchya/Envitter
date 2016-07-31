var app = angular.module('myApp', []);
app.controller('formCtrl', function($scope, $http,$rootScope) {
	$scope.searchName="";
	$scope.insertData = function() {
		console.log("PLease send data to servcie" , $scope.form);
		$http.post("http://192.168.100.88/envitter/index.php/retreiveController/insertdata",
				$scope.form ,{
			        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'}}
			).then(
				function(response) {
					console.log("PLease send data to servcie" + $scope.form);
					$scope.getAssignData();
				});
	}
	
	
	$scope.getAssignData = function() {
		//console.log("PLease send data to servcie" + $scope.form);
		$http.get("http://192.168.100.88/envitter/index.php/getassigned",$scope.form).then(
				function(response) {
					console.log(response.data.records);
					$rootScope.records = response.data.records;
					
					
				});
	}
	$scope.getAssignData();
});



app.controller('issueController', function($scope, $http) {
	$scope.searchName1="";
	$scope.getCatData = function() {
		//console.log("PLease send data to servcie" + $scope.form);
		$http.get("http://192.168.100.88/envitter/index.php/getcatname",$scope.form).then(
				function(response) {
					
					$scope.catData = response.data.records;
				});
		$scope.getRandomSpan = function(){
			  return Math.floor(Math.random()*10);
			}

	}
	$scope.getAssignData = function() {
		//console.log("PLease send data to servcie" + $scope.form);
		$http.get("http://192.168.100.88/envitter/index.php/getassigned",$scope.form).then(
				function(response) {
					console.log(response.data.records);
					$scope.assignData = response.data.records;
					
					
				});
	}
	$scope.getAssignData();
	$scope.getCatData();
});
