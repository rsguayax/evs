app.controller('adminEventClassroomCtrl', ['$scope','$http', '$compile', '$filter', 'flashService', 'uiCalendarConfig', adminEventClassroomCtrl]);

function adminEventClassroomCtrl($scope, $http, $compile, $filter, flashService, uiCalendarConfig) {
	
	$scope.init = function(eventClassroomId, eventStartDate, eventEndDate) {
		$scope.eventClassroomId = eventClassroomId;
		$scope.eventStartDate = moment(eventStartDate);
		$scope.eventEndDate = moment(eventEndDate);
		$scope.timeBlocks = [];
		$scope.timeBlockSource = [$scope.timeBlocks];
		getTimeBlocks();
		restoreAddStudentWizard();
		
		$scope.calendarConfig = {
		    calendar: {
		    	lang: 'es',
		    	defaultDate: $scope.eventStartDate,
		    	defaultView: 'agendaWeek',
		    	contentHeight: 440,
		    	header: {
		    		left: 'month agendaWeek',
		    		center: 'title',
		    		right: 'today prev,next'
		    	},
		        eventClick: function(calEvent, jsEvent, view) {
		        	$('.fc-event.selected').removeClass('selected');
		        	$(this).addClass('selected');
		        	showTimeBlockInfo(calEvent);
		        },
		        viewRender: function (view, element) {
		        	$(".fc-day").each(function(index, value) {
		        		var date = moment($(this).data('date'));
		        		
		        		if (!isEventDate(date)) {
		        			$(this).addClass('disabled-day');
		        		}
		        	});
	        	},
	        	eventRender: function(event, element) {
	        	    element.find('.fc-title').html(event.title);
	        	    
	        	    if ($scope.timeBlock != null && event.id == $scope.timeBlock.id) {
	        	    	element.addClass('selected');
	        	    }
	        	}
		    }
	    }; 
	};
	
	$scope.addTimeBlockTeachers = function(timeBlock) {
		if ($scope.selectedTimeBlockTeachers.selected.length > 0) {
			showWaitingModal('Añadiendo docente a la jornada de evaluación...');
			
			$http({
			    method: 'POST',
			    url: GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/addteachers/',
			    data: $scope.selectedTimeBlockTeachers.selected,
			    headers: {'Content-Type': 'application/json; charset=UTF-8'}
		   }).success(function(data) {
			   $scope.selectedTimeBlockTeachers.selected = [];
			   getTimeBlockTeachers(timeBlock);
			   getUnselectedTimeBlockTeachers(timeBlock);
			   hideWaitingModal();
			   alertify.alert('Docentes asignados correctamente a la jornada de evaluación');	 
		   });
		} else {
			alertify.alert('No ha seleccionado ningún docente');
		}
	};
	
	$scope.deleteTimeBlockTeacher = function(timeBlock, timeBlockTeacher) {
		alertify.confirm("¿Está seguro que desea eliminar el docente de la jornada de evaluación?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando docente de la jornada de evaluación...');
		    	
		    	$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/teacher/' + timeBlockTeacher.id + '/delete').success(function(data) {
		    		getTimeBlockTeachers(timeBlock);
		    		getUnselectedTimeBlockTeachers(timeBlock);
		    		hideWaitingModal();
		    		alertify.alert('Docente eliminado correctamente de la jornada de evaluación');	 
			    });
		    }
		});
	};
	
	$scope.deleteTimeBlockStudent = function(timeBlock, timeBlockStudent) {
		alertify.confirm("¿Está seguro que desea eliminar el estudiante de al jornada de evaluación?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando estudiante de la jornada de evaluación...');
		    	
		    	$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/student/' + timeBlockStudent.id + '/delete').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
						getTimeBlockStudents(timeBlock);
						refreshTimeBlock(timeBlock);
					}
		    		
		    		hideWaitingModal();
		    		alertify.alert(responseMessage.message);
			    });
		    }
		});
	};
	
	$scope.showAddStudent = function(timeBlock) {
		restoreAddStudentWizard();
		$('#addStudentModal').modal('toggle');
	};
	
	$scope.showStudentTests = function(timeBlockStudent) {
		$scope.timeBlockStudent = timeBlockStudent;
		$scope.studentTests = null;
		$scope.selectedStudentTests = {};
		$scope.selectedStudentTests.selected = [];
		$('#studentTestsModal').modal('toggle');
		getStudentTests(timeBlockStudent);
		getUnassignedStudentTest(timeBlockStudent);
	};
	
	$scope.addStudentTests = function(timeBlockStudent) {
		if ($scope.selectedStudentTests.selected.length > 0) {
			showWaitingModal('Añadiendo tests a la jornada de evaluación...');
			
			$http({
				method: 'POST',
			    url: GLOBAL_BASE_URL + '/studentevaluation/' + timeBlockStudent.id + '/addtests',
			    data: $scope.selectedStudentTests.selected,
			    headers: {'Content-Type': 'application/json; charset=UTF-8'}
		   }).success(function(data) {
			   $scope.selectedStudentTests.selected = [];
			   getStudentTests(timeBlockStudent);
			   getUnassignedStudentTest(timeBlockStudent);
			   hideWaitingModal();
			   alertify.alert('Test asignados correctamente');	 
		   });
		} else {
			alertify.alert('No ha seleccionado ningún test');
		}
	};
	
	$scope.deleteStudentTest = function(timeBlockStudent, studentTest) {
		alertify.confirm("¿Está seguro que desea eliminar de la jornada de evaluación el test \"" + studentTest.testName + "\" del estudiante \"" + timeBlockStudent.evaluationAssignment.user.fullName + "\"?", function (e) {
		    if (e) {
		    	if (studentTest.studentEvaluationId == timeBlockStudent.id) {
		    		showWaitingModal('Eliminando test de la jornada de evaluación...');
		    		
			    	$http.get(GLOBAL_BASE_URL + '/studentevaluation/' + timeBlockStudent.id + '/test/' + studentTest.id + '/delete').success(function(responseMessage) {
			    		if (responseMessage.type == 'warning') {
			    			getTimeBlockStudents($scope.timeBlock);
							refreshTimeBlock($scope.timeBlock);
							$('#studentTestsModal').modal('toggle');
			    		} else {
			    			getStudentTests(timeBlockStudent);
				    		getUnassignedStudentTest(timeBlockStudent);
			    		}
			    		
			    		hideWaitingModal();
			    		alertify.alert(responseMessage.message);
				    });
		    	} else {
		    		alertify.alert('El test no pertenece al estudiante \"' + timeBlockStudent.evaluationAssignment.user.fullName + '\"');
				}
		    }
		});
	};
	
	$scope.showAddStudentStep = function(step) {
		if ($scope.addStudentEnabledSteps.indexOf(step) != -1) {
			$scope.addStudentActiveStep = step;
			
			$('#addStudentWizard .steps-indicator .step').removeClass('active');
			$('#addStudentWizard .steps-indicator .step').eq(step-1).removeClass('disabled');
			$('#addStudentWizard .steps-indicator .step').eq(step-1).addClass('active');
			
			$('#addStudentWizard .steps .step').css('display', 'none');
			$('#addStudentWizard .steps .step').eq(step-1).css('display', 'block');
		}
	};
	
	$scope.nextAddStudenStep = function() {
		var errorStep = checkAddStudentForm();
		
		if (!errorStep) {
			$scope.showAddStudentStep($scope.addStudentActiveStep + 1);
		} else {
			$scope.showAddStudentStep(errorStep);
		}	
	}
	
	$scope.previousAddStudenStep = function() {
		$scope.showAddStudentStep($scope.addStudentActiveStep - 1);
	}
	
	$scope.addStudent = function(timeBlock) {
		var errorStep = checkAddStudentForm();
		
		if (!errorStep) {
			var url = GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/addstudent'
			var data = {student: $scope.newStudent, tests: $scope.selectedNewStudentTests};
			showWaitingModal('Añadiendo estudiante...');
			
			$http.post(url, data, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
				if (responseMessage.type == 'success') {
					getTimeBlockStudents(timeBlock);
					refreshTimeBlock(timeBlock);
					$('#addStudentModal').modal('toggle');
				}
				
				hideWaitingModal();
				alertify.alert(responseMessage.message);
		    });
		} else {
			$scope.showAddStudentStep(errorStep);
		}	
	}
	
	$scope.searchStudents = function(timeBlock, searchFor) {
		$scope.loadingStudentsSearchResults = true;
		var url = GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/searchstudents'
		var data = $.param({searchFor: searchFor});
		
		$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
			$scope.newStudent = null;
			$scope.newStudentTests = null;
			$scope.studentsSearchResults = data;
			$scope.loadingStudentsSearchResults = false;
	    });
	}
	
	$scope.selectNewStudent = function(newStudent) {
		$scope.newStudent = newStudent;
		$scope.selectedNewStudentTests = [];
		getNewStudentTests(newStudent);
	}
	
	$scope.updateNewStudentTests = function() {
		$scope.selectedNewStudentTests = $filter('filter')($scope.newStudentTests, {checked: true});
	}
	
   	function getTimeBlocks() {
   		$scope.loadingTimeBlocks = true;
   		
		$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/admin/timeblocks').success(function(data) {
    		$scope.timeBlocks.splice(0, $scope.timeBlocks.length);
    		$.each(data, function (index, timeBlock) {
    			$scope.timeBlocks.push(timeBlockToCalendarTimeBlock(timeBlock));
    		})
    		$scope.loadingTimeBlocks = false;
	    });
	}
   	
   	function refreshTimeBlock(timeBlock) {
   		$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/get').success(function(data) {
   			var refreshedTimeBlock = timeBlockToCalendarTimeBlock(data);
   			
   			for (var i=0; i<$scope.timeBlocks.length; i++) {
   				if ($scope.timeBlocks[i].id == refreshedTimeBlock.id) {
   					$scope.timeBlocks[i] = refreshedTimeBlock;
   					break;
   				}
   			}
	    	
   			if ($scope.timeBlock.id == refreshedTimeBlock.id) {
   				$scope.timeBlock = refreshedTimeBlock;
   			}
	    });
   	}
   	
   	function getTimeBlockTeachers(timeBlock) {
   		$scope.loadingTimeBlockTeachers = true;
   		
   		$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/teachers').success(function(data) {
    		$scope.timeBlockTeachers = data;
    		$scope.loadingTimeBlockTeachers = false;
	    });
   	}
   	
   	function getUnselectedTimeBlockTeachers(timeBlock) {
   		$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/unselectedteachers').success(function(data) {
    		$scope.unselectedTimeBlockTeachers = data;
	    });
	}
   	
   	function getTimeBlockStudents(timeBlock) {
   		$scope.loadingTimeBlockStudents = true;
   		
   		$http.get(GLOBAL_BASE_URL + '/classroomtimeblock/' + timeBlock.id + '/students').success(function(data) {
    		$scope.timeBlockStudents = data;
    		$scope.loadingTimeBlockStudents = false;
	    });
   	}
   	
   	function getStudentTests(timeBlockStudent) {
   		$scope.loadingStudentTests = true;
   		
   		$http.get(GLOBAL_BASE_URL + '/studentevaluation/' + timeBlockStudent.id + '/tests').success(function(data) {
			$scope.studentTests = data;
			$scope.loadingStudentTests = false;
	    });
	}
   	
   	function getUnassignedStudentTest(timeBlockStudent) {
   		$http.get(GLOBAL_BASE_URL + '/studentevaluation/' + timeBlockStudent.id + '/unassignedtests').success(function(data) {
    		$scope.unassignedStudentTests = data;
	    });
	}
   	
   	function getNewStudentTests(newStudent) {
   		$http.get(GLOBAL_BASE_URL + '/evaluationassignment/' + newStudent.evaluationAssignmentId + '/tests/').success(function(data) {
			$scope.newStudentTests = data;
	    });
   	}
   	
   	function showTimeBlockInfo(timeBlock) {
   		$scope.timeBlock = timeBlock;
   		$scope.selectedTimeBlockTeachers = {};
		$scope.selectedTimeBlockTeachers.selected = [];
		$scope.timeBlockTeachers = null;
		$scope.timeBlockStudents = null;
   		getTimeBlockStudents(timeBlock);
   	}
   	
	function isEventDate(date) {
		date = moment(date.format('YYYY-MM-DD'));
		return (date >= $scope.eventStartDate && date <= $scope.eventEndDate);
	}
	
	function timeBlockToCalendarTimeBlock(timeBlock) {
		var percentageAvailableSeats = Math.round(timeBlock.availableSeats * 100 / timeBlock.seats);
		var percentageOccupiedSeats = Math.round(timeBlock.occupiedSeats * 100 / timeBlock.seats);
		var title = 'Plazas disponibles: ' + timeBlock.availableSeats + '/' + timeBlock.seats + ' (' + percentageAvailableSeats + '%) <br/><br/>';
		title += 'Plazas ocupadas: ' + timeBlock.occupiedSeats + '/' + timeBlock.seats + ' (' + percentageOccupiedSeats + '%)';
		
		calendarTimeBlock = {
			'id': timeBlock.id,
			'title': title, 
			'start': moment(timeBlock.startDate).utc(), 
			'end': moment(timeBlock.endDate).utc(),
			'seats': timeBlock.seats,
			'availableSeats': timeBlock.availableSeats,
			'occupiedSeats': timeBlock.occupiedSeats,
			'percentageAvailableSeats': percentageAvailableSeats,
			'percentageOccupiedSeats': percentageOccupiedSeats,
			'editable': false,
			'stick': true,
			'backgroundColor': '#068bd8',
			'borderColor': '#0166a1',
			'textColor': '#fff'
		};
		return calendarTimeBlock;
	}
	
	function restoreAddStudentWizard() {
		$('#addStudentWizard .steps-indicator .step').removeClass('active');
		$('#addStudentWizard .steps-indicator .step').addClass('disabled');
		$scope.studentsSearch = '';
		$scope.addStudentEnabledSteps = [1];
		$scope.addStudentActiveStep = 1;
		$scope.showAddStudentStep(1);
		$scope.studentsSearchResults = null;
		$scope.newStudent = null;
		$scope.newStudentTests = null;
		$scope.selectedNewStudentTests = [];
	}
	
	function checkAddStudentForm() {
		var enabledSteps = [1];
		var errorStep = 0;

		for (var i=0; i<$scope.addStudentEnabledSteps.length;  i++) {
			var step = $scope.addStudentEnabledSteps[i];
			var errorMessage = null;
			
			if (step == 1) {
				if ($scope.newStudent != null) {
					enabledSteps.push(2);
				} else {
					errorMessage = 'Debe seleccionar un estudiante'
				}
			} else if (step == 2) {
				if ($scope.selectedNewStudentTests.length > 0) {
					enabledSteps.push(3);
				} else {
					errorMessage = 'Debe seleccionar al menos un test';
				}
			} 
			
			if (errorMessage != null && step <= $scope.addStudentActiveStep) {
				alertify.alert(errorMessage);
				errorStep = step;
				break;
			}
		}
		
		$('#addStudentWizard .steps-indicator .step').each(function(index, element) {
			if (enabledSteps.indexOf(index+1) == -1) {
				$(element).addClass('disabled');
			}
		});
		
		$scope.addStudentEnabledSteps = enabledSteps;
		return errorStep;
	}
}
