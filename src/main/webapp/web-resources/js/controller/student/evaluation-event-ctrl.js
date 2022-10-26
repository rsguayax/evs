app.controller('evaluationEventCtrl', ['$scope', '$window', '$http', '$compile', evaluationEventCtrl] );

function evaluationEventCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function(evaluationEventId, isScheduleModificationDate) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.isScheduleModificationDate = isScheduleModificationDate;
		getStudentEvaluations();
		getScheduleModificationAttempts();
		getLastScheduleModification();
	};
	
	$scope.showEditExamsSchedules = function() {
		if ($scope.scheduleModificationAttempts.remainingAttempts > 0) {
			window.location.href = GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/editexamsschedules';
		} else {
			alertify.alert('Ha agotado el número de intentos permitidos de modificación de horarios');
		}
	};
	
	$scope.showEditExamSchedule = function(matterTest, eventCenterId) {
		if ($scope.scheduleModificationAttempts.remainingAttempts > 0) {
			$scope.editExamScheduleMatterTest = matterTest;
			$scope.examEventCenterId = eventCenterId;
			
			getEvaluationCenters(matterTest);
			getClassroomTimeBlocks(eventCenterId);
			restoreEditExamScheduleWizard();
			$('#editExamScheduleModal').modal('toggle');
		} else {
			alertify.alert('Ha agotado el número de intentos permitidos de modificación de horarios');
		}
	};
	
	$scope.showEditExamScheduleStep = function(step) {
		if ($scope.editExamScheduleEnabledSteps.indexOf(step) != -1) {
			$scope.editExamScheduleActiveStep = step;
			
			$('#editExamScheduleWizard .steps-indicator .step').removeClass('active');
			$('#editExamScheduleWizard .steps-indicator .step').eq(step-1).removeClass('disabled');
			$('#editExamScheduleWizard .steps-indicator .step').eq(step-1).addClass('active');
			
			$('#editExamScheduleWizard .steps .step').css('display', 'none');
			$('#editExamScheduleWizard .steps .step').eq(step-1).css('display', 'block');
		}
	};
	
	$scope.nextEditExamScheduleStep = function() {
		var errorStep = checkEditExamScheduleForm();
		
		if (!errorStep) {
			$scope.showEditExamScheduleStep($scope.editExamScheduleActiveStep + 1);
		} else {
			$scope.showEditExamScheduleStep(errorStep);
		}	
	}
	
	$scope.previousEditExamScheduleStep = function() {
		$scope.showEditExamScheduleStep($scope.editExamScheduleActiveStep - 1);
	};
	
	$scope.editExamSchedule = function() {
		var errorStep = checkEditExamScheduleForm();
		
		if (!errorStep) {
			if ($scope.scheduleModificationAttempts.remainingAttempts > 0) {
				alertify.confirm('Quedan ' + $scope.scheduleModificationAttempts.remainingAttempts + ' intentos de modificación de horarios. ¿Está seguro que desea continuar?', function (e) {
				    if (e) {
				    	var url = GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/editexamschedule'
						var data = {matterTest: $scope.editExamScheduleMatterTest, classroomTimeBlock: $scope.examClassroomTimeBlock, scheduleModificationMessage: $scope.scheduleModificationMessage};
						showWaitingModal('Modificando horario...');
						
						$http.post(url, data, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
							hideWaitingModal();
							
							if (responseMessage.type == 'success') {
								getStudentEvaluations();
								getScheduleModificationAttempts();
								getLastScheduleModification();
								$('#editExamScheduleModal').modal('toggle');
								
								alertify.confirm(responseMessage.message + ' ¿Desea imprimir la modificación de horario realizada?', function (e) {
								    if (e) {
								    	$scope.showLastScheduleModification();
								    }
							    });
							}  else {
								alertify.alert(responseMessage.message);
							}
					    });
				    }
				});
			} else {
				alertify.alert('Ha agotado el número de intentos permitidos de modificación de horarios');
			}
		} else {
			$scope.showEditExamScheduleStep(errorStep);
		}	
	};
	
	$scope.selectExamEvaluationCenter = function() {
		$scope.examClassroomTimeBlock = null;
		getClassroomTimeBlocks($scope.examEventCenterId);
	};
	
	$scope.showLastScheduleModification = function() {
		$('#scheduleModificationModal').modal('toggle');
	};
	
	$scope.printScheduleModification = function() {	
		$("#scheduleModificationPrint").printArea();
	};
	
	function getStudentEvaluations() {
		$http.get(GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/studentevaluation/list').success(function(data) {
    		$scope.studentEvaluations = data;
	    });
	}
	
	function getEvaluationCenters(matterTest) {
		var url = GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/evaluationcenter/list';
		var data = $.param({centerId: matterTest.centerId});
		
		$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
    		$scope.eventCenters = data;
	    });
	}
	
	function getClassroomTimeBlocks(eventCenterId) {
		$http.get(GLOBAL_STUDENT_BASE_URL + '/eventcenter/' + eventCenterId + '/classroomtimeblock/list').success(function(data) {
			$scope.classroomTimeBlocks = {};
			
			for (var i=0; i<data.length; i++) {
				var classroomTimeBlock = data[i];
				
				if (classroomTimeBlock.availableState == 'AVAILABLE' || isStudentClassroomTimeBlock(classroomTimeBlock.id)) {
					if (!$scope.classroomTimeBlocks.hasOwnProperty(classroomTimeBlock.classroomName)) {
						$scope.classroomTimeBlocks[classroomTimeBlock.classroomName] = [];
					}
					
					$scope.classroomTimeBlocks[classroomTimeBlock.classroomName].push(classroomTimeBlock);
				}
			}
	    });
	}
	
	function getScheduleModificationAttempts() {
		$http.get(GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/schedulemodificationattempts').success(function(data) {
			$scope.scheduleModificationAttempts = data;
	    });
	}
	
	function getLastScheduleModification() {
		$http.get(GLOBAL_STUDENT_BASE_URL + '/evaluationevent/' + $scope.evaluationEventId + '/lastschedulemodification').success(function(data) {
			$scope.lastScheduleModification = data != "" ? data : null;
	    });
	}
	
	function isStudentClassroomTimeBlock(classroomTimeBlockId) {
		for (var i=0; i<$scope.studentEvaluations.length; i++) {
			if ($scope.studentEvaluations[i].classroomTimeBlock.id == classroomTimeBlockId) {
				return true;
			}
		}
		
		return false;
	}
	
	function restoreEditExamScheduleWizard() {
		$('#editExamScheduleWizard .steps-indicator .step').removeClass('active');
		$('#editExamScheduleWizard .steps-indicator .step').addClass('disabled');
		$scope.editExamScheduleEnabledSteps = [1];
		$scope.editExamScheduleActiveStep = 1;
		$scope.showEditExamScheduleStep(1);
		$scope.examClassroomTimeBlock = null;
		$scope.scheduleModificationMessage = '';
	}
	
	function checkEditExamScheduleForm() {
		var enabledSteps = [1];
		var errorStep = 0;

		for (var i=0; i<$scope.editExamScheduleEnabledSteps.length;  i++) {
			var step = $scope.editExamScheduleEnabledSteps[i];
			var errorMessage = null;
			
			if (step == 1) {
				if ($scope.examEventCenterId != null) {
					enabledSteps.push(2);
				} else {
					errorMessage = 'Debe seleccionar un centro'
				}
			} else if (step == 2) {
				if ($scope.examClassroomTimeBlock != null) {
					enabledSteps.push(3);
				} else {
					errorMessage = 'Debe seleccionar un horario';
				}
			} else if (step == 3) {
				if ($.trim($scope.scheduleModificationMessage).length > 0) {
					enabledSteps.push(4);
				} else {
					errorMessage = 'Indique el motivo del cambio de horario';
				}
			}
			
			if (errorMessage != null && step <= $scope.editExamScheduleActiveStep) {
				alertify.alert(errorMessage);
				errorStep = step;
				break;
			}
		}
		
		$('#editExamScheduleWizard .steps-indicator .step').each(function(index, element) {
			if (enabledSteps.indexOf(index+1) == -1) {
				$(element).addClass('disabled');
			}
		});
		
		$scope.editExamScheduleEnabledSteps = enabledSteps;
		return errorStep;
	}
}