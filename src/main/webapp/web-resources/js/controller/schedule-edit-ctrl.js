app.controller('scheduleEditCtrl', ['$scope', '$timeout', '$http', '$compile', 'flashService', 'uiCalendarConfig', scheduleEditCtrl]);

function scheduleEditCtrl($scope, $timeout, $http, $compile, flashService, uiCalendarConfig) {
	var firstRender = true;
	
	$scope.init = function(scheduleId, eventStartDate, eventEndDate) {
		$scope.scheduleId = scheduleId;
		$scope.eventStartDate = moment(eventStartDate);
		$scope.eventEndDate = moment(eventEndDate);
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
			            text: 'Añadir jornada evaluación',
			            click: function() {
			            	showAddTimeBlockModal('', '');
			            }
			        },
			        saveChanges: {
			            text: 'Guardar cambios',
			            click: function() {
			            	$http.get(GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/eventclassrooms/size').success(function(data) {
			        			if (data.size > 0) {
			        				alertify.confirm("El horario está asociado a varias aulas ¿Desea actualizar los cambios en todas las aulas asociadas al horario?", function (e) {
			        				    if (e) {
			        				    	updateTimeBlocks(true);
			        				    } else {
			        				    	updateTimeBlocks(false);
			        				    }
			        				});
			        			} else {
			        				updateTimeBlocks(false);
			        			}
			        	    });
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
		var startDate = moment($('#startDate').val(), 'DD-MM-YYYY HH:mm', true);
		var endDate = moment($('#endDate').val(), 'DD-MM-YYYY HH:mm', true);
		
		if (startDate.isValid() && endDate.isValid()) {
			if (startDate < endDate) {
				if (isEventDate(startDate)) {
					if (startDate.format('DD-MM-YYYY') == endDate.format('DD-MM-YYYY')) {
						if ($('#timeBlockForm').data('addForm') == 'true') {
							$http.get(GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/eventclassrooms/size').success(function(data) {
								if (data.size > 0) {
									alertify.confirm("El horario está asociado a varias aulas ¿Desea añadir la jornada de evaluación a todas las aulas asociadas al horario?", function (e) {
									    if (e) {
									    	$('#updateAll').val('true');
									    	submitTimeBlockForm();
									    } else {
									    	submitTimeBlockForm();
									    }
									});
								} else {
									submitTimeBlockForm();
								}
						    });
						} else {
							$http.get(GLOBAL_TIME_BLOCK_URL + '/' + $('#timeBlockId').val() + '/classroomtimeblocks/size').success(function(data) {
								if (data.size > 0) {
									alertify.confirm("La jornada de evaluación está asociado a varias aulas ¿Desea actualizar la jornada de evaluación en todas las aulas?", function (e) {
									    if (e) {
									    	$('#updateAll').val('true');
									    	submitTimeBlockForm();
									    } else {
									    	submitTimeBlockForm();
									    }
									});
								} else {
									submitTimeBlockForm();
								}
						    });
						}
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
		    	$http.get(GLOBAL_TIME_BLOCK_URL + '/' + timeBlockId + '/classroomtimeblocks/size').success(function(data) {
					if (data.size > 0) {
						alertify.confirm("La jornada de evaluación está asociada a varias aulas ¿Desea eliminar la jornada de evaluación en todas las aulas?", function (e) {
						    if (e) {
						    	deleteTimeBlock(timeBlockId, true)
						    } else {
						    	deleteTimeBlock(timeBlockId, false)
						    }
						});
					} else {
						deleteTimeBlock(timeBlockId, false)
					}
			    });
		    }
		});
   	};
   	
   	function getTimeBlocks() {
		$http.get(GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/timeblock/list').
	    	success(function(data) {
	    		$scope.timeBlocks.splice(0, $scope.timeBlocks.length);
	    		$.each(data, function (index, timeBlock) {
	    			$scope.timeBlocks.push(timeBlockToCalendarTimeBlock(timeBlock));
	    		})
		    });
	}
   	
   	function submitTimeBlockForm() {
   		var url = $('#timeBlockForm').attr('action');
		var formData = $('#timeBlockForm').serialize();
		
		// Si el campo updateAll es nulo, lo eliminamos de los datos
   		if (formData.indexOf('updateAll=&') != -1) {
   			formData = formData.replace('updateAll=&', '');
   		}
		
		$http.post(url, formData, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
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
   	}
   	
   	function deleteTimeBlock(timeBlockId, deleteAll) {
   		$http.get(GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/timeblock/delete/' + timeBlockId + '?deleteAll=' + deleteAll).success(function(data) {
    		var timeBlock = getTimeBlock(timeBlockId);
			var index = $scope.timeBlocks.indexOf(timeBlock);
	    	$scope.timeBlocks.splice(index, 1);     
	    	alertify.alert('Jornada de evaluación eliminada correctamente');
	    	hideTimeBlockModal();
	    });
   	}
	
	function updateTimeBlocks(updateAll) {
		var url = GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/timeblock/update?updateAll=' + updateAll;
		var updatedTimeBlocks = [];
		
		$.each($scope.updatedTimeBlocks, function (index, calendarTimeBlock) {
			updatedTimeBlocks.push(calendarTimeBlockToTimeBlock(calendarTimeBlock));
		})
		
		$http.post(url, updatedTimeBlocks, {headers: {'Content-Type': 'application/json'}}).success(function(data) {
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
		$('#timeBlockFormTitle').text('Nueva jornada de evaluación');
		$('#timeBlockForm').data('addForm', 'true');
		$('#timeBlockForm').attr('action', GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/timeblock/new');
		$('#timeBlockId').val('');
		$('#updateAll').val('');
		$('#startDate').val(startDate);
		$('#endDate').val(endDate);
		$('#studentTypes option').prop('selected', false);
		$('#deleteTimeBlockButton').remove();
		$('#time-block-form-modal').modal('toggle');
	}
	
	function showEditTimeBlockModal(timeBlock) {
		$('#timeBlockFormTitle').text('Editar jornada de evaluación');
		$('#timeBlockForm').data('addForm', 'false');
		$('#timeBlockForm').attr('action', GLOBAL_SCHEDULE_URL + '/' + $scope.scheduleId + '/timeblock/edit/' + timeBlock.id);
		$('#timeBlockId').val(timeBlock.id);
		$('#updateAll').val('');
		$('#startDate').val(timeBlock.start.format('DD-MM-YYYY HH:mm'));
		$('#endDate').val(timeBlock.end.format('DD-MM-YYYY HH:mm'));
		$('#studentTypes option').prop('selected', false);
		for(var i=0; i<timeBlock.studentTypes.length; i++) {
		    $('#studentType' +  timeBlock.studentTypes[i].id).prop('selected', true);
		}
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
		var studentTypes = timeBlock.studentTypes != null ? timeBlock.studentTypes : [];
		var title = studentTypes.length > 0 ? 'Tipos de estudiantes:' : '';
		
		for (var i=0; i<studentTypes.length; i++) {
			title += '<br /> &nbsp;-' + studentTypes[i].value;
		}
		
		calendarTimeBlock = {
			'id': timeBlock.id,
			'title': title, 
			'start': moment(timeBlock.startDate).utc(), 
			'end': moment(timeBlock.endDate).utc(),
			'studentTypes': studentTypes,
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
}
