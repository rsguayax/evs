app.controller('classroomTimeBlockCtrl', ['$scope', '$timeout', '$http', '$compile', 'flashService', 'uiCalendarConfig', classroomTimeBlockCtrl]);

function classroomTimeBlockCtrl($scope, $timeout, $http, $compile, flashService, uiCalendarConfig) {
	var firstRender = true;
	
	$scope.init = function(eventClassroomId, eventStartDate, eventEndDate, availableStateId) {
		$scope.eventClassroomId = eventClassroomId;
		$scope.eventStartDate = moment(eventStartDate);
		$scope.eventEndDate = moment(eventEndDate);
		$scope.availableStateId = availableStateId;
		$scope.timeBlocks = [];
		$scope.updatedTimeBlocks = [];
		$scope.timeBlockSource = [$scope.timeBlocks];
		getTimeBlocks();
		
		$scope.calendarConfig = {
		    calendar: {
		    	lang: 'es',
		    	defaultDate: $scope.eventStartDate,
		    	defaultView: 'agendaWeek',
		    	contentHeight: 550,
		    	customButtons: {
			        addTimeBlock: {
			            text: 'Añadir jornada de evaluación',
			            click: function() {
			            	showAddTimeBlockModal('', '');
			            }
			        },
			        saveChanges: {
			            text: 'Guardar cambios',
			            click: function() {
			            	updateTimeBlocks();
			            }
			        }
				},
		    	header: {
		    		left: 'addTimeBlock saveChanges',
		    		center: 'title',
		    		right: 'month agendaWeek today prev,next'
		    	},
		    	dayClick: function(date, jsEvent, view) {
		    		if (isEventDate(date)) {
		    			var startDate = date.format('DD-MM-YYYY HH:mm');
		    			var endDate = date.add(2, 'hours').format('DD-MM-YYYY HH:mm');
		    			showAddTimeBlockModal(startDate, endDate);
		    		}
		        },
		        eventClick: function(calEvent, jsEvent, view) {
		        	showEditTimeBlockModal(calEvent);
		        },
		        viewRender: function (view, element) {
		        	if (firstRender) {
		        		$('.fc-saveChanges-button').attr('id', 'saveChangesButton')
		        		disableSaveChangesButton();
		        		firstRender = false;
		        	}
		        	$(".fc-day").each(function(index, value) {
		        		var date = moment($(this).data('date'));
		        		
		        		if (!isEventDate(date)) {
		        			$(this).addClass('disabled-day');
		        		}
		        	});
	        	},
	        	eventRender: function(event, element) {
	        	    element.find('.fc-title').html(event.title);
	        	},
	        	eventResize: function(event, delta, revertFunc) {
	        		addToUpdatedTimeBlocks(event);
	            },
	            eventDrop: function(event, delta, revertFunc) {
	            	if (isEventDate(event.start)) {
	            		if (event.start.format('DD-MM-YYYY') == event.end.format('DD-MM-YYYY')) {
	            			addToUpdatedTimeBlocks(event);
	            		} else {
	            			revertFunc();
	            			alertify.alert('La jornada de evaluación no puede abarcar varios días');
	            		}
	            	} else {
	            		revertFunc();
	            		alertify.alert('La jornada de evaluación no puede estar fuera del periodo de evaluación');
	            	}
	            }
		    }
	    }; 
	};
	
	$scope.submitTimeBlockForm = function() {
		var startDate = moment($scope.timeBlock.startDate, 'DD-MM-YYYY HH:mm', true);
		var endDate = moment($scope.timeBlock.endDate, 'DD-MM-YYYY HH:mm', true);
		
		if (startDate.isValid() && endDate.isValid()) {
			if (startDate < endDate) {
				if (isEventDate(startDate)) {
					if (startDate.format('DD-MM-YYYY') == endDate.format('DD-MM-YYYY')) {
						var url = $('#timeBlockForm').attr('action');
						var formData = $('#timeBlockForm').serialize();
						
						// Si el timeBlock.id es nulo, lo eliminamos de los datos
				   		if (formData.indexOf("timeBlock.id=&") != -1) {
				   			formData = formData.replace('timeBlock.id=&', '');
				   		}
				   		
				   		showWaitingModal('Guardando jornada de evaluación...');
	
						$http.post(url, formData, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
							hideWaitingModal();
							
							if (data.hasOwnProperty('id')) {
								var timeBlock = getTimeBlock(data.id);
								
								if (timeBlock != null) {
									var index = $scope.timeBlocks.indexOf(timeBlock);
									$scope.timeBlocks[index] = timeBlockToCalendarTimeBlock(data);
									alertify.alert('Jornada de evaluación modificada correctamente');
								} else {
									$scope.timeBlocks.push(timeBlockToCalendarTimeBlock(data));
									alertify.alert('Jornada de evaluación creada correctamente');
								}
								
								hideTimeBlockModal();
							} else if (data.hasOwnProperty('errors')) {
								alertify.alert(data['errors']);
							}
					    });
					} else {
						alertify.alert('La jornada de evaluación no puede abarcar varios días');
					}
				} else {
		    		alertify.alert('La jornada de evaluación no puede estar fuera del periodo de evaluación');
		    	}
			} else {
				alertify.alert('La fecha de fin debe ser mayor a la fecha de inicio');
			}
		} else {
			alertify.alert('Las fechas introducidas no son válidas');
		}
   	};
   	
   	$scope.deleteTimeBlock = function(timeBlockId) {
   		alertify.confirm("¿Está seguro que desea eliminar la jornada de evaluación?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando jornada de evaluación...');
		    	
		    	$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/delete/' + timeBlockId).success(function(data) {
		    		var timeBlock = getTimeBlock(timeBlockId);
					var index = $scope.timeBlocks.indexOf(timeBlock);
			    	$scope.timeBlocks.splice(index, 1);
			    	hideWaitingModal();
			    	alertify.alert('Jornada de evaluación eliminado correctamente');
			    	hideTimeBlockModal();
			    });
		    }
		});
   	};
   	
   	$scope.addTimeBlockTeachers = function(timeBlock) {
		if ($scope.selectedTeachers.selected.length > 0) {
			showWaitingModal('Asignando docentes...');
			
			$http({
			    method: 'POST',
			    url: GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/' + timeBlock.id + '/addteachers',
			    data: $scope.selectedTeachers.selected,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
			   $scope.selectedTeachers.selected = [];
			   getTimeBlockTeachers(timeBlock);
			   getUnselectedTimeBlockTeachers(timeBlock);
			   hideWaitingModal();
			   alertify.alert('Docentes asignados correctamente');	 
		   });
		} else {
			alertify.alert('No ha seleccionado ningún docente');
		}
	};
	
	$scope.deleteTimeBlockTeacher = function(timeBlock, timeBlockTeacher) {
		alertify.confirm("¿Está seguro que desea eliminar el docente de la jornada de evaluación?", function (e) {
		    if (e) {
		    	showWaitingModal('Eliminando docente...');
		    	
		    	$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/' + timeBlock.id + '/teacher/' + timeBlockTeacher.id + '/delete').success(function(data) {
		    		getTimeBlockTeachers(timeBlock);
		    		getUnselectedTimeBlockTeachers(timeBlock);
		    		hideWaitingModal();
		    		alertify.alert('Docente eliminado de la jornada de evaluación correctamente');	 
			    });
		    }
		});
	};
   	
   	function getTimeBlocks() {
   		$scope.loadingTimeBlocks = true;
   		
		$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/list').success(function(data) {
    		$scope.timeBlocks.splice(0, $scope.timeBlocks.length);
    		$.each(data, function (index, timeBlock) {
    			$scope.timeBlocks.push(timeBlockToCalendarTimeBlock(timeBlock));
    		})
    		$scope.loadingTimeBlocks = false;
	    });
	}
	
	function updateTimeBlocks() {
		var url = GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/update';
		var updatedTimeBlocks = [];
		
		showWaitingModal('Guardando cambios...');
		
		$.each($scope.updatedTimeBlocks, function (index, calendarTimeBlock) {
			updatedTimeBlocks.push(calendarTimeBlockToTimeBlock(calendarTimeBlock));
		})
		
		$http.post(url, updatedTimeBlocks, {headers: {'Content-Type': 'application/json'}}).success(function(data) {
			hideWaitingModal();
			
			if (data.hasOwnProperty('updated')) {
				disableSaveChangesButton();
				alertify.alert('Bloques horarios actualizados correctamente');
			} else if (data.hasOwnProperty('error')) {
				alertify.alert(data['error']);
			}
	    });
	}
	
	function addToUpdatedTimeBlocks(calendarTimeBlock) {
		if ($scope.updatedTimeBlocks.indexOf(calendarTimeBlock) == -1) {
			$scope.updatedTimeBlocks.push(calendarTimeBlock);
		}
		
    	enableSaveChangesButton();
	}
	
	function disableSaveChangesButton() {
		$('#saveChangesButton').removeClass().addClass('btn btn-default');
		$('#saveChangesButton').prop('disabled', true);
	}
	
	function enableSaveChangesButton() {
		$('#saveChangesButton').removeClass('btn-default').addClass('btn-primary');
		$('#saveChangesButton').prop('disabled', false);
	}
	
	function isEventDate(date) {
		date = moment(date.format('YYYY-MM-DD'));
		return (date >= $scope.eventStartDate && date <= $scope.eventEndDate);
	}
	
	function showAddTimeBlockModal(startDate, endDate) {
		$scope.timeBlockTeachers = [];
		$scope.timeBlock = {
			'availableStateId': $scope.availableStateId,
			'startDate': startDate,
			'endDate': endDate,
		}
		$('#timeBlockFormTitle').text('Nueva jornada de evaluación');
		$('#timeBlockForm').attr('action', GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/new');
		$('#deleteTimeBlockButton').remove();
		$('#teachers').hide();
		$('#time-block-form-modal').modal('toggle');
	}
	
	function showEditTimeBlockModal(timeBlock) {
		$scope.timeBlock = timeBlock;
		$('#timeBlockFormTitle').text('Editar jornada de evaluación');
		$('#timeBlockForm').attr('action', GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/edit/' + timeBlock.id);
		
		$scope.selectedTeachers = {};
		$scope.selectedTeachers.selected = [];
		$scope.timeBlockTeachers = null;
		getTimeBlockTeachers(timeBlock);
		getUnselectedTimeBlockTeachers(timeBlock);
		$('#teachers').show();
		
		$('#deleteTimeBlockButton').remove();
		$('#time-block-form-modal .modal-footer').append('<button id="deleteTimeBlockButton" type="button" class="btn btn-danger" ng-click="deleteTimeBlock(' + timeBlock.id + ')">Eliminar</button>');
		$compile($('#deleteTimeBlockButton'))($scope);
		
		$('#time-block-form-modal').modal('toggle');
	}
	
	function hideTimeBlockModal() {
		$('#time-block-form-modal').modal('toggle');
	}
	
	function getTimeBlock(timeBlockId) {
		for (var i=0; i<$scope.timeBlocks.length; i++) {
			var timeBlock = $scope.timeBlocks[i];
			if (timeBlock.id == timeBlockId) {
				return timeBlock;
			}
		}
		
		return null;
	}
	
	function timeBlockToCalendarTimeBlock(timeBlock) {
		var start = moment(timeBlock.startDate).utc();
		var end = moment(timeBlock.endDate).utc();
		var studentTypes = timeBlock.studentTypes != null ? timeBlock.studentTypes : [];
		var studenTypesIds = [];
		var title = studentTypes.length > 0 ? 'Tipos de estudiantes:' : '';
		
		for (var i=0; i<studentTypes.length; i++) {
			studenTypesIds.push(studentTypes[i].id.toString());
			title += '<br /> &nbsp;-' + studentTypes[i].value;
		}
		
		calendarTimeBlock = {
			'id': timeBlock.id,
			'timeBlockId': timeBlock.timeBlock != null ? timeBlock.timeBlock.id : '',
			'availableStateId': timeBlock.availableState.id,
			'title': title, 
			'start': start, 
			'end': end,
			'startDate': start.format('DD-MM-YYYY HH:mm'), 
			'endDate':  end.format('DD-MM-YYYY HH:mm'),
			'studentTypes': studenTypesIds,
			'editable': true,
			'stick': true,
			'backgroundColor': '#068bd8',
			'borderColor': '#0166a1',
			'textColor': '#fff'
		};
		return calendarTimeBlock;
	}
	
	function calendarTimeBlockToTimeBlock(calendarTimeBlock) {
		timeBlock = {
			'id': calendarTimeBlock.id, 
			'startDate': calendarTimeBlock.start.format('DD-MM-YYYY HH:mm'), 
			'endDate': calendarTimeBlock.end.format('DD-MM-YYYY HH:mm')
		};
		return timeBlock;
	}
	
	function getTimeBlockTeachers(timeBlock) {
		$scope.loadingTimeBlockTeachers = true;
		
		$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/' + timeBlock.id + '/teachers').success(function(data) {
			$scope.timeBlockTeachers = data;
			$scope.loadingTimeBlockTeachers = false;
			
			var evaluationEventTeachersIds = [];
			for (var i=0; i<data.length; i++) {
				evaluationEventTeachersIds.push(data[i].id.toString());
			}
			timeBlock.evaluationEventTeachers = evaluationEventTeachersIds;
	    });
	}
   	
   	function getUnselectedTimeBlockTeachers(timeBlock) {
   		$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + $scope.eventClassroomId + '/timeblock/' + timeBlock.id + '/unselectedteachers').success(function(data) {
    		$scope.unselectedTeachers = data;
	    });
	}
}
