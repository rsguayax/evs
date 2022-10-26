app.controller('mattersCtrl', ['$scope', '$http', '$timeout', '$interval', 'filterFilter', 'config', 'flashService', mattersCtrl] );

function mattersCtrl($scope, $http, $timeout, $interval, filterFilter, config, flashService) {
	
	$scope.init = function() {
		$scope.currentPage = 1;
        $scope.pageSize = 10;  
        $scope.loading = true;
        $scope.getMatters();
	};
	
	$scope.getMatters = function() {
		$http.get(GLOBAL_MATTER_URL + '/matters').
	    	success(function(data) {
	    		$scope.matters = data;
	    		$scope.filterMatters = data;
	    		$scope.checkCountMattersWithoutBank();
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
	$scope.checkCountMattersWithoutBank = function() {
		$http.get(GLOBAL_MATTER_URL + '/countmatterswithoutbank').success(function(data) {
    		if (data > 0) {
    			alertify.alert('Hay ' + data + ' asignaturas que no tienen un banco de preguntas asignado');
    		} 
	    });
	};
	
	$scope.loadMatters = function() {    	
    	alertify.confirm("\u00bfSeguro que quiere actualizar los asignaturas?", function(e) {
        	if(e) {
	    		showWaitingModal("Actualizando asignaturas...");
	        	$http.get(GLOBAL_MATTER_URL + '/loadmatters').
	        	success(function(responseMessage) {
	        		hideWaitingModal();

	        		if (responseMessage.type == 'success') {
	        			$scope.getMatters();
					}
					
					alertify.alert(responseMessage.message);
	        	});
	    	}
		});
    };
    
    $scope.assignBanks = function() {    	
    	alertify.confirm("Se asignar&aacute;n a las asignaturas los bancos de preguntas que tengan el mismo nombre que la asignatura.  \u00bfSeguro que quiere asignar los bancos de preguntas  a las asignaturas?", function(e) {
        	if(e) {
	    		showWaitingModal("Asignando bancos de preguntas a las asignaturas...");
	        	$http.get(GLOBAL_MATTER_URL + '/assignbanks').
	        	success(function(responseMessage) {
	        		hideWaitingModal();

	        		if (responseMessage.type == 'success') {
	        			$scope.getMatters();
					}
					
					alertify.alert(responseMessage.message);
	        	});
	    	}
		});
    };
	
   $scope.$watch('search.name', function (term) {
	   var obj = { name: term }

       $scope.filterMatters = filterFilter($scope.matters, obj);
       
       $scope.currentPage = 1;
  });
	
}