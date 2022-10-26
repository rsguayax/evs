app.controller('evaluationEventAdminCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', evaluationEventAdminCtrl] );

function evaluationEventAdminCtrl($scope, $http, $timeout, $interval, config, flashService) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.getAdmins();
	};
	
	$scope.showAddAdmin = function() {
		$scope.selectedAdmins = {};
		$scope.selectedAdmins.selected = [];
		$scope.getUnselectedAdmins();
		$('#add-admin-modal').modal('toggle');
	};
	
	$scope.getAdmins = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/admin/list').
	    	success(function(data) {
	    		$scope.admins = data;
		    });
	};
	
	$scope.getUnselectedAdmins = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/admin/unselectedlist').
	    	success(function(data) {
	    		$scope.unselectedAdmins = data;
		    });
	};
	
	$scope.addAdmins = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/admin/add',
		    data: $scope.selectedAdmins.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getAdmins();
	       $('#add-admin-modal').modal('toggle');
		   flashService.flash("Administradores asignados correctamente", "success");	 
	   });
	};
	
	$scope.deleteAdmin = function(admin) {   
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/admin/delete?adminId=' + admin.id).
	    	success(function(data) {
	    		var index = $scope.admins.indexOf(admin);
		    	$scope.admins.splice(index, 1);     
		    	flashService.flash("Administrador eliminado correctamente", "success");	 
		    });
   };
}