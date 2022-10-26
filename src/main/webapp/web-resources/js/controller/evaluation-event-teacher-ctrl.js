app.controller('evaluationEventTeacherCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', evaluationEventTeacherCtrl] );

function evaluationEventTeacherCtrl($scope, $http, $timeout, $interval, config, flashService) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.getTeachers();
	};
	
	$scope.showAddTeacher = function() {
		$scope.selectedTeachers = {};
		$scope.selectedTeachers.selected = [];
		$scope.getUnselectedTeachers();
		$('#add-teacher-modal').modal('toggle');
	};
	
	$scope.getTeachers = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/teacher/list').
	    	success(function(data) {
	    		$scope.evaluationEventTeachers = data;
		    });
	};
	
	$scope.getUnselectedTeachers = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/teacher/unselectedlist').
	    	success(function(data) {
	    		$scope.unselectedTeachers = data;
		    });
	};
	
	$scope.addTeachers = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/teacher/add',
		    data: $scope.selectedTeachers.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getTeachers();
	       $('#add-teacher-modal').modal('toggle');
		   flashService.flash("Docentes asignados correctamente", "success");	 
	   });
	};
	
	$scope.deleteTeacher = function(evaluationEventTeacher) {
		alertify.confirm("¿Está seguro que desea eliminar el docente?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/teacher/delete/' + evaluationEventTeacher.id).success(function(data) {
		    		var index = $scope.evaluationEventTeachers.indexOf(evaluationEventTeacher);
			    	$scope.evaluationEventTeachers.splice(index, 1);     
			    	flashService.flash("Docente eliminado correctamente", "success");	 
			    });
		    }
		});
   };
}