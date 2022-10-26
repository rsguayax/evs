app.controller('scheduleCtrl', ['$scope', '$http', '$timeout', '$interval', '$compile', 'config', 'flashService', scheduleCtrl] );

function scheduleCtrl($scope, $http, $timeout, $interval, $compile, config, flashService) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.getSchedules();
	};

	$scope.getSchedules = function() {
		$scope.loadingSchedules = true;
		
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/schedule/list').success(function(data) {
    		$scope.schedules = data;
    		$scope.loadingSchedules = false;
	    });
	};
	
	$scope.showAddSchedule = function() {
	   $("#add-schedule-modal .modal-content").load(GLOBAL_AJAX_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/schedule/new', function() {
		   $compile($("#add-schedule-modal .modal-content"))($scope);
		   $('#add-schedule-modal').modal('toggle');
	   });
	};
	
	$scope.submitScheduleForm = function() {
	   var url = $('#scheduleForm').attr('action');
	   var data = $('#scheduleForm').serialize();
	   
	   $http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data, status, headers, config) {
		   var contentType = headers('content-type') || "";
		   if (contentType.indexOf('json') > -1) {
			   $scope.getSchedules();
			   $('#add-schedule-modal').modal('toggle');
			   if (data.hasOwnProperty('created')) {
				   alertify.confirm("Horario creado correctamente. ¿Desea configurar el horario ahora?", function (e) {
					   if (e) {
						   window.location = data.editUrl;
					   }
				   });
			   }
		   } else {
			   $("#add-schedule-modal .modal-content").html(data);
			   $compile($("#add-schedule-modal .modal-content"))($scope);
		   }
	   });  
   	};
	
	$scope.deleteSchedule = function(schedule) {
		alertify.confirm('¿Está seguro que desea eliminar el horario "' + schedule.name + '"?', function (e) {
		    if (e) {
		    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/schedule/delete/' + schedule.id).success(function(data) {
		    		var index = $scope.schedules.indexOf(schedule);
		    		$scope.schedules.splice(index, 1);     
			    	flashService.flash('Horario "' + schedule.name + '" eliminado correctamente', 'success');	 
			    });
		    }
		});
   };
}