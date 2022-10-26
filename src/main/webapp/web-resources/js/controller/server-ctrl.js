app.controller('serverCtrl', ['$scope', '$http', 'filterFilter', '$timeout', '$interval', '$filter', 'config', 'flashService', 'mainService', serverCtrl]);

function serverCtrl($scope, $http, filterFilter, $timeout, $interval, $filter, config, flashService, mainService) {

    $scope.init = function(serverId) {
		if (!serverId) {
		    $scope.currentPage = 1;
		    $scope.pageSize = 10;
		    $scope.loading = true;
		    $scope.getServers();
		} else {
		    $scope.serverId = serverId;
		    $scope.currentPeriodId = null;
		    $scope.getTests();
		}
		
		mainService.loadEvaluationTypes().then(function(result) {
			$scope.evaluationTypes = result;
		});
    };

    $scope.getServers = function() {
		$http.get(GLOBAL_SERVER_URL + '/servers').success(function(data) {
		    $scope.servers = data;
		    $scope.filterServers = data;
		}).finally(function() {
		    $scope.loading = false;
		});
    };
    
    
	$scope.getAllServers = function() {
		$http.get(GLOBAL_SERVER_URL + '/edit/' + $scope.evaluationEventId + '/allservers').
	    	success(function(data) {
	    		$scope.allservers = data;  
	    		$scope.selectLabel = "Seleccione servidor (limitado 50 primeros resultados)...";
		    });
	};

    
    $scope.$watch('search.name', function(term) {
		var obj = {name: term};
	
		$scope.filterServers = filterFilter($scope.servers, obj);
	
		$scope.currentPage = 1;
    });
}