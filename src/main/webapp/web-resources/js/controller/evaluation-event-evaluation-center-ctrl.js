app.controller('evaluationEventEvaluationCenterCtrl', ['$scope', '$http', '$timeout', '$interval', '$compile', 'config', 'flashService', evaluationEventEvaluationCenterCtrl] );

function evaluationEventEvaluationCenterCtrl($scope, $http, $timeout, $interval, $compile, config, flashService) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		getEvaluationCenters();
		getSchedules();
	};
	
	$scope.showAddEvaluationCenter = function() {
		$scope.selectedEvaluationCenters = {};
		$scope.selectedEvaluationCenters.selected = [];
		$scope.getUnselectedEvaluationCenters();
		$('#add-evaluation-center-modal').modal('toggle');
	};
	
	$scope.getUnselectedEvaluationCenters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/unselectedlist').
	    	success(function(data) {
	    		$scope.unselectedEvaluationCenters = data;
		    });
	};
	
	$scope.addEvaluationCenters = function() {
		showWaitingModal('Añadiendo centro de evaluación...');
		
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/add',
		    data: $scope.selectedEvaluationCenters.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   getEvaluationCenters();
	       $('#add-evaluation-center-modal').modal('toggle');
	       hideWaitingModal();
	       flashService.flash("Centros de evaluación asignados correctamente", "success");	 
	   });
	};
	
	$scope.deleteEvaluationCenter = function(evaluationEventEvaluationCenter) {
		alertify.confirm("¿Está seguro que desea eliminar el centro de evaluación?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando centro de evaluación...');
		    	
		    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/delete?evaluationEventEvaluationCenterId=' + evaluationEventEvaluationCenter.id).success(function(data) {
		    		if(data.error == 0) {
		    			var index = $scope.evaluationEventEvaluationCenters.indexOf(evaluationEventEvaluationCenter);
			    		$scope.evaluationEventEvaluationCenters.splice(index, 1);
		    		}
			    	hideWaitingModal();
			    	flashService.flash(data.message, data.type);
			    });
		    }
		});
	};
	
	function getEvaluationCenters() {
		$scope.loadingEvaluationEventEvaluationCenters = true;
		
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/list').success(function(data) {
    		$scope.evaluationEventEvaluationCenters = data;
    		$scope.loadingEvaluationEventEvaluationCenters = false;
	    });
	};
	
	function getSchedules() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/schedule/list').success(function(data) {
    		$scope.schedules = data;
	    });
   	}
   
	//****************//
	// 	CLASSROOMS   //
	//****************//
   
	$scope.addClassroom = function(evaluationEventEvaluationCenter) {
		if ($scope.schedules.length > 0) {
			$scope.currentClassroomScheduleId = null;
			showWaitingModal('Espere un momento...');
			
			$("#classroom-modal .modal-content").load(GLOBAL_AJAX_EVENT_CENTER_URL + '/' + $scope.evaluationEventId + '/' + evaluationEventEvaluationCenter.id + '/classroom/new', function() {
				$compile($("#classroom-modal .modal-content"))($scope);
				hideWaitingModal();
				$('#classroom-modal').modal('toggle');
			});
		} else {
			alertify.alert('Debe definir al menos un horario para poder configurar aulas');
		}
	};

	$scope.editClassroom = function(evaluationEventEvaluationCenter, evaluationEventClassroom) {
		$scope.currentClassroomScheduleId = evaluationEventClassroom.schedule.id;
		showWaitingModal('Espere un momento...');
	   
		$("#classroom-modal .modal-content").load(GLOBAL_AJAX_EVENT_CENTER_URL + '/' + $scope.evaluationEventId + '/' + evaluationEventEvaluationCenter.id + '/classroom/edit/' + evaluationEventClassroom.id, function() {
 		   	$compile($("#classroom-modal .modal-content"))($scope);
 		   	hideWaitingModal();
 		   	$('#classroom-modal').modal('toggle');
 	   	});
	};
   
    $scope.submitClassroomForm = function() {
    	var selectedClassroomScheduleId = $('#classroomForm #schedule').val();
	   
    	if($scope.currentClassroomScheduleId != null && $scope.currentClassroomScheduleId != selectedClassroomScheduleId) {
    		alertify.confirm('Ha seleccionado un nuevo horario ¿Está seguro que desea modificar el horario del aula?', function (e) {
    			if (e) {
    				submitClassroomForm();
    			}
    		});
    	} else {
    		submitClassroomForm();
    	}
   	};
   	
   	$scope.deleteClassroom = function(evaluationEventEvaluationCenter, evaluationEventClassroom) {
		alertify.confirm("¿Está seguro que desea eliminar la configuración del aula?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando configuración del aula...');
		    	
		    	$http.get(GLOBAL_EVENT_CENTER_URL + '/' + evaluationEventEvaluationCenter.id + '/classroom/delete?evaluationEventClassroomId=' + evaluationEventClassroom.id).success(function(data) {
		    		if(data.error == 0) {
		    			var index = evaluationEventEvaluationCenter.evaluationEventClassrooms.indexOf(evaluationEventClassroom);
		    			evaluationEventEvaluationCenter.evaluationEventClassrooms.splice(index, 1);
		    		}
			    	hideWaitingModal();
			    	flashService.flash(data.message, data.type);
			    });
		    }
		});
   	};
   	
   	$scope.showAddAllClassrooms = function(evaluationEventEvaluationCenter) {
   		if ($scope.schedules.length > 0) {
	   		$scope.evaluationEventEvaluationCenter = evaluationEventEvaluationCenter;
	   		$scope.unselectedEventClassrooms = null;
	   		getUnselectedEventClassrooms(evaluationEventEvaluationCenter);
	   		$('#addAllClassroomsModal').modal('toggle');
   		} else {
   			flashService.flash('Debe definir al menos un horario para poder configurar aulas', 'success');
		}
	};
	
	$scope.addAllClassrooms = function(evaluationEventEvaluationCenter) {
		var url = GLOBAL_EVENT_CENTER_URL + '/' + evaluationEventEvaluationCenter.id + '/classroom/addall';
		showWaitingModal('Configurando aulas...');
		
		$http.post(url, $scope.unselectedEventClassrooms, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
			if (responseMessage.type == 'success') {
				getEvaluationCenters();
				$('#addAllClassroomsModal').modal('toggle');
			}
			
			hideWaitingModal();
			alertify.alert(responseMessage.message);
	    });
	};
   	
   	function submitClassroomForm() {
   		var url = $('#classroomForm').attr('action');
   		var data = $('#classroomForm').serialize();
   		showWaitingModal('Configurando aula...');
	   
   		//Si no ha seleccionado ningún CAP, no enviamos los datos cap.id
   		if (data.indexOf("cap.id=&") != -1) {
   			data = data.replace('cap.id=&','');
   		}
	   
   		$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data, status, headers, config) {
   			var contentType = headers('content-type') || "";
   			if (contentType.indexOf('json') > -1) {
   				getEvaluationCenters();
   				$('#classroom-modal').modal('toggle');
   				if (data.hasOwnProperty('created')) {
   					alertify.alert("Aula configurada correctamente");
   				} else if (data.hasOwnProperty('updated')) {
   					alertify.alert("Configuración del aula modificada correctamente");
   				}
   			} else {
   				$("#classroom-modal .modal-content").html(data);
   				$compile($("#classroom-modal .modal-content"))($scope);
   			}
   			
   			hideWaitingModal();
   		});
   	}
   	
   	function getUnselectedEventClassrooms(evaluationEventEvaluationCenter) {
   		$scope.loadingUnselectedEventClassrooms = true;
		
		$http.get(GLOBAL_EVENT_CENTER_URL + '/' + $scope.evaluationEventId + '/' + evaluationEventEvaluationCenter.id + '/unselectedeventclassrooms/list').success(function(data) {
    		$scope.unselectedEventClassrooms = data;
    		$scope.loadingUnselectedEventClassrooms = false;
	    });
   	}
   	
   	
    //****************//
    // 	CENTERS       //
    //****************//
   	$scope.showAddCenter = function(evaluationEventEvaluationCenter) {
   		evaluationEventEvaluationCenterSelected = evaluationEventEvaluationCenter;
		$scope.selectedCenters = {};
		$scope.selectedCenters.selected = [];
		$scope.unselectedCenters = {};
		$scope.getCenters(evaluationEventEvaluationCenter);
		$scope.getUnselectedCenters(evaluationEventEvaluationCenter);
		$('#add-center-modal').modal('toggle');
	};
	
	$scope.getCenters = function(evaluationEventEvaluationCenter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/' + evaluationEventEvaluationCenter.id + '/center/list').
	    	success(function(data) {
	    		$scope.selectedCenters.selected = data;
		    });
	};
	
	$scope.getUnselectedCenters = function(evaluationEventEvaluationCenter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/' + evaluationEventEvaluationCenter.id + '/center/unselectedlist').
	    	success(function(data) {
	    		$scope.unselectedCenters = data;
		    });
	};
	
	$scope.addCenters = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationcenter/' + evaluationEventEvaluationCenterSelected.id + '/center/add',
		    data: $scope.selectedCenters.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $('#add-center-modal').modal('toggle');
		   flashService.flash("Centros educativos asignados correctamente", "success");	 
	   });
	};
}