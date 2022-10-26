app.controller('editExamsSchedulesCtrl', ['$scope', '$window', '$http', '$compile', editExamsSchedulesCtrl] );

function editExamsSchedulesCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function(studentId, evaluationEventId) {
		$scope.studentId = studentId;
		$scope.evaluationEventId = evaluationEventId;
		getStudentEvaluations();
		getLastScheduleModification();
	};
	
	$scope.showEditExamSchedule = function(matterTest) {
		$scope.editExamScheduleMatterTest = matterTest;
		$scope.examEventCenterId = matterTest.eventCenterId;
		
		getEvaluationCenters(matterTest);
		getClassroomTimeBlocks($scope.examEventCenterId);
		restoreEditExamScheduleWizard();
		$('#editExamScheduleModal').modal('toggle');
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
			$scope.editExamScheduleMatterTest.eventCenterId = $scope.examClassroomTimeBlock.evaluationEventEvaluationCenterId;
			$scope.editExamScheduleMatterTest.evaluationCenterName = $scope.examClassroomTimeBlock.evaluationCenterName;
			$scope.editExamScheduleMatterTest.classroomName = $scope.examClassroomTimeBlock.classroomName;
			$scope.editExamScheduleMatterTest.timeBlockId = $scope.examClassroomTimeBlock.id;
			$scope.editExamScheduleMatterTest.timeBlockStartDate = $scope.examClassroomTimeBlock.startDate;
			$scope.editExamScheduleMatterTest.timeBlockEndDate = $scope.examClassroomTimeBlock.endDate;
			$scope.editExamScheduleMatterTest.modified = true;
			$('#editExamScheduleModal').modal('toggle');
		} else {
			$scope.showEditExamScheduleStep(errorStep);
		}	
	};
	
	$scope.editExamsSchedules = function() {
		var modifiedMatterTests = $scope.modifiedMatterTests();
		
		if (modifiedMatterTests.length > 0) {
			if ($.trim($scope.schedulesModificationMessage).length > 0) {
				alertify.confirm('¿Está seguro que desea modificar los horarios de los exámenes?', function (e) {
				    if (e) {
				    	var url = GLOBAL_STUDENTS_URL + '/' + $scope.studentId + '/evaluationevent/' + $scope.evaluationEventId + '/editexamsschedules/'
						var data = {modifiedMatterTests: modifiedMatterTests, schedulesModificationMessage: $scope.schedulesModificationMessage};
						showWaitingModal('Modificando horarios...');
						
						$http.post(url, data, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
							hideWaitingModal();
							
							if (responseMessage.type == 'success') {
								getStudentEvaluations();
								getLastScheduleModification();
								$scope.schedulesModificationMessage = "";
								
								alertify.confirm(responseMessage.message + ' ¿Desea imprimir la modificación de horarios realizada?', function (e) {
								    if (e) {
								    	$scope.showLastScheduleModification();
								    }
							    });
							} else {
								alertify.alert(responseMessage.message);
							}
					    });
				    }
				});
			} else {
				alertify.alert('Indique el motivo de la modificación de horarios');
			}
		} else {
			alertify.alert('No ha modificado ningún horario');
		}
	};
	
	$scope.cancelExamsSchedulesModification = function() {
		alertify.confirm('¿Está seguro que desea cancelar todas las modificaciones de horarios realizadas?', function (e) {
		    if (e) {
		    	getStudentEvaluations();
		    	$scope.schedulesModificationMessage = "";
		    }
		});
	};
	
	$scope.selectExamEvaluationCenter = function() {
		$scope.examClassroomTimeBlock = null;
		getClassroomTimeBlocks($scope.examEventCenterId);
	};
	
	$scope.modifiedMatterTests = function() {
		var modifiedMatterTests = [];
	    
		for (var i in $scope.matterTests) {
			var matterTest = $scope.matterTests[i];
			if (matterTest.modified) {
				modifiedMatterTests.push(matterTest);
			}
	    }
		
		return modifiedMatterTests;
	}
	
	$scope.showLastScheduleModification = function() {
		$('#scheduleModificationModal').modal('toggle');
	};
	
	$scope.printScheduleModification = function() {	
		$("#scheduleModificationPrint").printArea();
	};
	
	function getStudentEvaluations() {
		$http.get(GLOBAL_STUDENTS_URL + '/' + $scope.studentId + '/evaluationevent/' + $scope.evaluationEventId + '/studentevaluation/list').success(function(data) {
    		$scope.studentEvaluations = data;
    		$scope.matterTests = [];
    		
    		for (var i in $scope.studentEvaluations) {
    			var studentEvaluation = $scope.studentEvaluations[i];
    			for (var j in studentEvaluation.matterTestInfos) {
    				var matterTest = studentEvaluation.matterTestInfos[j];
    				matterTest.evaluationCenterName = studentEvaluation.evaluationCenter.name;
    				matterTest.eventCenterId = studentEvaluation.evaluationEventEvaluationCenterId;
    				matterTest.classroomName = studentEvaluation.classroom.name;
    				matterTest.timeBlockId = studentEvaluation.classroomTimeBlock.id;
    				matterTest.timeBlockStartDate = studentEvaluation.classroomTimeBlock.startDate;
    				matterTest.timeBlockEndDate = studentEvaluation.classroomTimeBlock.endDate;
    				$scope.matterTests.push(matterTest);
    			}
    	    }
	    });
	}
	
	function getEvaluationCenters(matterTest) {
		var url = GLOBAL_STUDENTS_URL + '/' + $scope.studentId + '/evaluationevent/' + $scope.evaluationEventId + '/evaluationcenter/list';
		var data = $.param({centerId: matterTest.centerId});
		
		$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
    		$scope.eventCenters = data;
	    });
	}
	
	function getClassroomTimeBlocks(eventCenterId) {
		$http.get(GLOBAL_EVENT_CENTER_URL + '/' + eventCenterId + '/classroomtimeblock/list').success(function(data) {
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
	
	function getLastScheduleModification() {
		$http.get(GLOBAL_STUDENTS_URL + '/' + $scope.studentId + '/evaluationevent/' + $scope.evaluationEventId + '/lastschedulemodification').success(function(data) {
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