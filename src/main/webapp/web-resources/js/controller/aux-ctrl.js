app.controller('auxCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', auxCtrl] );

function auxCtrl($scope, $http, $timeout, $interval, config, flashService) {
	
	$scope.init = function(matterId) {;
	};
	
	$scope.updateAuxData = function() {
		showWaitingModal("Actualizando entidades...");
		$http.get(GLOBAL_AUX_URL + '/updateauxdata').
	    	success(function(data) {
	    		hideWaitingModal();
	    		angular.forEach(data.messages, function(value, key) {
	  	    	   flashService.flash(value.message, value.type);	 
	  	        });	  
		    });
	};
	
	
	$scope.updateCenters = function() {
		showWaitingModal("Actualizando centros...");
		$http.get(GLOBAL_AUX_URL + '/updatecenters').
	    	success(function(data) {
	    		hideWaitingModal();
	    		if(data == 1) {
	    			flashService.flash("Actualizaci&oacute;n realizada correctamente", "success");
	    		} else {
	    			flashService.flash("Se ha producido un error al cargar los centros educativos", "danger");
	    		}
		    });
	};
}