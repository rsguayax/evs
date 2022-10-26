app.controller('adminEventCtrl', ['$scope', '$window', '$http', '$compile', '$controller', '$filter', '$timeout', '$interval', 'flashService', adminEventCtrl] );

function adminEventCtrl($scope, $window, $http, $compile, $controller, $filter, $timeout, $interval, flashService) {

	$scope.init = function(evaluationEventId, assignmentType) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.assignmentType = assignmentType;
		$scope.running = false;
		$scope.runningManual = false;
		
		angular.extend(this, $controller('UploaderController', {$scope: $scope}));
		$scope.uploader.onProgressAll = function(progress) {
			if(progress == 100) {			
				$timeout(function() { 
					showWaitingModal('Archivo subido correctamente, procesando archivo...');  
				}, 1000);
			}
        };
		/*$scope.uploader.onCompleteAll = function() {
			$timeout(function() {
				$('#load-external-modal').modal('toggle');
				hideWaitingModal();
				alertify.alert('Archivo subido y procesado correctamente');
			}, 2000);
	    };*/
	    $scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {
			$timeout(function() {
				$('#load-external-modal').modal('toggle');
				getTestsSchedulesInfo();
				getAvailableSeatsInfo();
				hideWaitingModal();
				flashService.flash(response.message, response.type);	 
			}, 2000);
	    };
		
		getEventCenters();
		getScheduleModificationDates();
		getTestsSchedulesInfo();
		getAvailableSeatsInfo();
	};

	$scope.addScheduleModificationDate = function() {
		var startDate = moment($scope.scheduleModificationStartDate, 'DD-MM-YYYY HH:mm', true);
		var endDate = moment($scope.scheduleModificationEndDate, 'DD-MM-YYYY HH:mm', true);

		if (startDate.isValid() && endDate.isValid()) {
			if (startDate < endDate) {
				var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/addschedulemodificationdate'
				var data = $.param({startDate: startDate.format('DD-MM-YYYY HH:mm'), endDate: endDate.format('DD-MM-YYYY HH:mm')});
				showWaitingModal('Añadiendo rango de fechas...');

				$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(responseMessage) {
					if (responseMessage.type == 'success') {
						$scope.scheduleModificationStartDate = '';
						$scope.scheduleModificationEndDate = '';
						getScheduleModificationDates();
					}

					hideWaitingModal();
		    		alertify.alert(responseMessage.message);
			    });
			} else {
				alertify.alert('La fecha de fin debe ser mayor a la fecha de inicio');
			}
		} else {
			alertify.alert('Las fechas introducidas no son válidas');
		}
	};

	$scope.deleteScheduleModificationDate = function(scheduleModificationDate) {
		alertify.confirm("¿Está seguro que desea eliminar el rango de fechas?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando rango de fechas...');

		    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/schedulemodificationdate/' + scheduleModificationDate.id + '/delete').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
		    			getScheduleModificationDates();
					}
		    		getScheduleModificationDates();
		    		hideWaitingModal();
		    		alertify.alert(responseMessage.message);
			    });
		    }
		});
	}

	$scope.getEventClassrooms = function() {
		$http.get(GLOBAL_EVENT_CENTER_URL + '/' + $scope.eventCenterId + '/eventclassroom/list').success(function(data) {
    		$scope.eventClassrooms = data;
    		$scope.eventClassroomId = null;
	    });
	};

	$scope.adminEventClassroom = function() {
		$window.open(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/admin', '_blank');
	};

	$scope.assignSchedulesToTests = function() {
		if($scope.assignmentType == 'AUTOMATICO') {
			showProgressModal('Asignando horarios a los tests...');
			$scope.getAssignSchedulesProgress();
	
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/assignschedules').success(function(responseMessage) {
				getTestsSchedulesInfo();
				getAvailableSeatsInfo()
	    		hideProgressModal();
	    		if(responseMessage != null && responseMessage.message != null) {
	    			alertify.alert(responseMessage.message);
				}
		    });
		} else {
			// abrir modal de carga de fichero
			$('#load-external-modal').modal('toggle');
		}
	};
	
	$scope.assignSchedulesWithCheking = function() {
		if($scope.assignmentType == 'AUTOMATICO') {
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/assign/isrunning').
		    	success(function(data) {    		
		    		$scope.running = data;
		    		if($scope.running == false) {
		    			$scope.assignSchedulesToTests();
		    		} else  {
		    			flashService.flash("Ya existe un proceso de asignación en marcha, espere unos momentos", "warning");
		    		}
			    });
		} else {
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/assign/isrunningmanual').
	    	success(function(data) {    		
	    		$scope.runningManual = data;
	    		if($scope.runningManual == false) {
	    			$scope.assignSchedulesToTests();
	    		} else  {
	    			flashService.flash("Ya existe un proceso de asignación en marcha, espere unos momentos", "warning");
	    		}
		    });
		}
	};
	
	$scope.checkIntervalRunning = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/assign/isrunning').
	    	success(function(data) {    		
	    		$scope.running = data;
	    		if($scope.running == false) {
	    			$interval.cancel($scope.timer);
	    			hideWaitingModal();
	    		}
		    });
		  }, 10000), 3000);
	};
	
	$scope.getAssignSchedulesProgress = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/assign/progress').
	    	success(function(data) {  
	    		if (data == 0) {
	    			$scope.progress = 1;
	    		} else {
	    			$scope.progress = data;
	    		}
		    });
		  }, 10000), 1000);
	};
	
	$scope.getDeleteSchedulesProgress = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/deleteschedules/progress').
	    	success(function(data) {    
	    		if (data.progress == 0) {
	    			$scope.progress = 1;
	    		} else {
	    			$scope.progress = data.progress;
	    		}
		    });
		  }, 2000), 1000);
	};
	
	$scope.deleteSchedules = function() {
		alertify.confirm("¿Está seguro que desea eliminar todos los horarios del evento de evaluación?", function (e) {
		    if (e) {
		    	showProgressModal('Eliminando horarios...');
				$scope.getDeleteSchedulesProgress();
				
				$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/deletechedules').success(function(responseMessage) {
					getTestsSchedulesInfo();
					getAvailableSeatsInfo()
		    		hideProgressModal();
		    		if(responseMessage != null && responseMessage.message != null) {
		    			alertify.alert(responseMessage.message);
					}
			    });
		    }
		});
	};
	
	$('#load-external-modal').on('hidden.bs.modal', function () {
		$scope.uploader.clearQueue();
	})

	$scope.exportExternal = function() {
		showProgressModal('Exportando información a Siette...');
		$scope.getExportExternalProgress();

		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/exportexternal').success(function(responseMessage) {
			if (responseMessage.type == 'success') {
				getTestsSchedulesInfo();
			}

			hideProgressModal();
    		alertify.alert(responseMessage.message);
	    });
	};
	
	$scope.getExportExternalProgress = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/exportschedulessiette/progress').
	    	success(function(data) {    
	    		if (data.progress == 0) {
	    			$scope.progress = 1;
	    		} else {
	    			$scope.progress = data.progress;
	    		}
		    });
		  }, 10000), 1000);
	};
	
	$scope.downloadSchedulesFile = function() {
		showWaitingModal('Generando plantilla de asignación de horarios');
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/downloadschedules'
	   }).success(function(data) {
	       $('#results-report-modal').modal('toggle');
	       var anchor = angular.element('<a/>');
	       var today = $filter('date')(new Date(),'ddMMyy-HHmmss');
	       var filename = $scope.evaluationEventId + "_horarios_" + today + ".csv";
	       anchor.attr({
	           href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
	           target: '_blank',
	           download: filename
	       });
	       angular.element(document.body).append(anchor);
	       anchor[0].click();
	       $timeout(function () {
		          anchor.remove();
		       }, 50);

		   flashService.flash("Fichero generado correctamente", "success");	 
	   }).finally(function() {
		   hideWaitingModal();
	   });
	};
	
	$scope.logVisible = function() {
		if(!angular.isUndefined($scope.testsSchedulesInfo)) {
			if($scope.assignmentType == 'AUTOMATIC') {
				return $scope.testsSchedulesInfo.logs > 0 && $scope.testsSchedulesInfo.testsWithoutSchedule > 0;
			} else {
				return ($scope.testsSchedulesInfo.logs > 0 || $scope.testsSchedulesInfo.genericlogs > 0);
			}
		} else {
			return false;
		}
	}

	function getEventCenters() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/eventcenter/list').success(function(data) {
    		$scope.eventCenters = data;
	    });
	};

	function getScheduleModificationDates() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/schedulemodificationdate/list').success(function(data) {
    		$scope.scheduleModificationDates = data;
	    });
	};

	function getTestsSchedulesInfo() {
		$scope.loadingTestsSchedulesInfo = true;

		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/tests/schedulesinfo').success(function(data) {
    		$scope.testsSchedulesInfo = data;
    		$scope.loadingTestsSchedulesInfo = false;

    		$scope.testsSchedulesChart = {
				'type': 'PieChart',
				'cssStyle': 'height:auto; width:auto;"',
				'data': {"cols": [
                    {id: "t", label: "Tests", type: "string"},
				    {id: "s", label: "Slices", type: "number"}
				 ], "rows": [
				    {c: [{v: "Tests con horario asignado"}, {v: $scope.testsSchedulesInfo.testsWithSchedule}]},
				    {c: [{v: "Tests pendientes de la asignación de horario"}, {v: $scope.testsSchedulesInfo.testsWithoutSchedule}]}
				]},
	            'options': {
	                'title': '',
	                'slices': {'0': {color: '#739600'}, '1': {color: '#d52b1e'}}
	            }
			};
	    });
	}

	function getAvailableSeatsInfo() {
		$scope.loadingAvailableSeatsInfo = true;

		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/availableseatsinfo').success(function(data) {
    		$scope.availableSeatsInfo = data;
    		$scope.loadingAvailableSeatsInfo = false;

    		var rows = [];
    		for(var i=0; i<data.evaluationCenters.length; i++) {
    			var evaluationCenter = data.evaluationCenters[i];
    			rows.push({c: [{v: evaluationCenter.name}, {v: evaluationCenter.availableSeats}]});
    		}

    		$scope.availableSeatsChart = {
				'type': 'PieChart',
				'cssStyle': 'height:auto; width:auto;"',
				'data': {
					"cols": [
	                    {id: "t", label: "Center", type: "string"},
					    {id: "s", label: "Slices", type: "number"}
					 ],
					 "rows": rows
				},
	            'options': {
	                'title': 'Plazas disponibles por centro de evaluación',
	                'slices': {'0': {color: '#739600'}, '1': {color: '#d52b1e'}}
	            }
			};
	    });
	}
	
	function showProgressModal(progressModalTitle) {
		$scope.progress = 1;
		$scope.progressModalTitle = progressModalTitle;
		$('#progress-modal').modal('toggle');
	}
	
	function hideProgressModal() {
		$('#progress-modal').modal('hide');
		$interval.cancel($scope.timer);
	}
}