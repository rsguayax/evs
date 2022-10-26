app.controller('matterCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', matterCtrl] );

function matterCtrl($scope, $http, $timeout, $interval, config, flashService) {
	
	$scope.init = function(matterId) {
		$scope.matterId = matterId;
		$scope.selectedBanks = {};
		$scope.getBanks();
		$scope.getAllBanks();
		$scope.currentPage = 1;
        $scope.pageSize = 10;  
        $scope.loading = true;
	};
	
	$scope.showAddBank = function() {
		$('#add-banks-modal').modal('toggle');
	};
	
	$scope.getAllBanks = function() {
		$http.get(GLOBAL_MATTER_URL + '/edit/' + $scope.matterId + '/allbanks').
	    	success(function(data) {
	    		$scope.allbanks = data;  
	    		$scope.allbanksAsync = data;
		    });
	};
	
	$scope.getBanks = function() {
		$http.get(GLOBAL_MATTER_URL + '/edit/' + $scope.matterId + '/banks').
	    	success(function(data) {
	    		$scope.banks = data;	    		
	    		$scope.selectedBanks.selected = data;
		    });
	};
	
	$scope.deleteBank = function(bank) {
		$http.get(GLOBAL_MATTER_URL + '/edit/' + $scope.matterId + '/banks/delete?idBank=' + bank.id).
    	success(function(data) {
    		var index = $scope.banks.indexOf(bank);
	    	$scope.banks.splice(index, 1);     
	    	flashService.flash("Eliminado correctamente", "success");	 
	    });
	};
	
	$scope.addBanks = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_MATTER_URL + '/edit/' + $scope.matterId + '/banks/add',
		    data: $scope.selectedBanks.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getBanks();
	       $('#add-banks-modal').modal('toggle');
	       $scope.selectedBanks.selected = {};
		   flashService.flash("Asignados correctamente", "success");	 
	   });			
	};
}