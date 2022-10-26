app.controller('degreesCtrl', ['$scope', '$http', '$timeout', '$interval', 'filterFilter', 'config', 'flashService', degreesCtrl]);

function degreesCtrl($scope, $http, $timeout, $interval, filterFilter, config, flashService) {

	$scope.init = function() {
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
		$scope.getDegrees();
	};

	$scope.getDegrees = function() {
		$http.get('/evs/degree/degrees').
			success(function(data) {
				$scope.degrees = data;
				$scope.filterDegrees = data;
			}).finally(function() {
				$scope.loading = false;
			});
	};
	
	$scope.$watch('search.name', function(term) {
		var obj = { name: term }
		$scope.filterDegrees = filterFilter($scope.degrees, obj);
		$scope.currentPage = 1;
	});

	$scope.delete = function(degreeID) {
		alertify.confirm('\u00BFEst\u00E1 seguro que desea eliminar la titulaci&oacute;n?', function(e) {
			if (e) {
				$http.get('/evs/degree/delete/' + degreeID).
					success(function(data) {
						$scope.degrees = data;
						$scope.filterDegrees = data;
						$scope.getDegrees();
						alertify.success("Se elimin\u00F3 correctamente");
					}).finally(function() {
						$scope.loading = false;
					});
			} else {
				alertify.error("No se elimin\u00F3");
			}
		});
		return false
	};
	
	$scope.loadDegrees = function() {    	
    	alertify.confirm("\u00bfSeguro que quiere actualizar las titulaciones?", function(e) {
        	if(e) {
	    		showWaitingModal("Actualizando titulaciones...");
	        	$http.get('/evs/degree/loaddegrees').
	        	success(function(responseMessage) {
	        		hideWaitingModal();

	        		if (responseMessage.type == 'success') {
	        			$scope.getDegrees();
					}
					
					alertify.alert(responseMessage.message);
	        	});
	    	}
		});
    };
}