app.controller('evaluationEventDegreesCtrl', ['$scope', '$http', '$timeout', '$interval', '$filter', 'config', 'flashService', 'filterFilter', evaluationEventDegreesCtrl]);

function evaluationEventDegreesCtrl($scope, $http, $timeout, $interval, $filter, config, flashService, filterFilter) {

	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.degreesFilter = {};
		$scope.getDegrees();
		$scope.getAllDegrees();
		$scope.getSubjects();
		$scope.degreesProgress = 0;
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
		$scope.loading = true;
		$scope.running = 0;
		$scope.showLoadBtn = 0;
		$scope.getAllSubjects();
		$scope.weightAdd = "";
		$scope.degreeSubjects = [];
		$scope.stateEE = "";
		$scope.stateEvent();
		$scope.subjectObj = {};
	};
	/**
	CARGA DETODAS LAS TITLACIONES PERTENCIENTES A UN EVENTO DE EVALUACIÓN EN EL MODAL
	 */
	
	$scope.showAddDegree = function() {
		$scope.degreeData = {};
		$('#degree-modal').modal('toggle');
	};
	
	$scope.showEditDegree = function(degree) {
		$scope.degreeData = angular.copy(degree);
		$('#degree-modal').modal('toggle');
	};

	/**OBTENCIÓN DE TODAS LAS TITULACIONES EXISTENTES
	 */
	$scope.getAllDegrees = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/alldegrees').
			success(function(data) {
				$scope.alldegrees = data;
				$scope.selectLabel = "Seleccione Titulaci\u00F3n...";
			});
	};
	/**
	OBTENCIÓN DE TODAS LAS TITULACIONES EXISTENTES PERTENECIENTES A UN EVENTO DE EVLAUACION 
	 
	*/
	$scope.getDegrees = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees').
			success(function(data) {
				$scope.degrees = data;
				$scope.filterDegrees = $scope.degrees;
			}).finally(function() {
				$scope.loading = false;
			});
	};

	$scope.getDegreeEnrollmentsCount = function(degree) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/' + degree.id + '/degreeenrollmentscount').
			success(function(data) {
				$scope.degreeEnrollmentsCount = data;
			});
	};
	
	$scope.addDegree = function() {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede agregar titulaciones');
		} else {
			var url = GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/add';
			
			$http.post(url, $scope.degreeData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
				if (responseMessage.type == 'success') {
					$scope.getDegrees();
					$('#degree-modal').modal('hide');
				}
				
				alertify.alert(responseMessage.message);
		    });
		}
	};
	
	$scope.editDegree = function() {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede editar titulaciones');
		} else {
			var url = GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/edit';
	
			$http.post(url, $scope.degreeData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
				if (responseMessage.type == 'success') {
					$scope.getDegrees();
					$('#degree-modal').modal('hide');
				}
				
				alertify.alert(responseMessage.message);
		    });
		}
	};

	$scope.showLoadDegrees = function() {
		$scope.loadDegreesProgress();
		$('#load-degrees-modal').modal('toggle');
	};

	$scope.loadAllDegrees = function() {
		var cut_off_gradeLoad = $("#cut_off_gradeLoad").val();
		var quotaLoad = $("#quotaLoad").val();
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/loaddegrees',
			params: { "cut_off_gradeLoad": cut_off_gradeLoad, "quotaLoad": quotaLoad }
		}).success(function(data) {
		});
		$scope.degreesProgress = 1;
		$scope.running = 1;
		$scope.showLoadBtn = 0;
		$scope.loadDegreesProgress();
	};

	$scope.loadDegreesReset = function() {
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/loaddegreesreset',
		}).success(function(data) {
		});
		$timeout(function() { }, 2000);
	};

	$scope.$watch('search.name', function(term) {
		var obj = [{ 'obj': 'degree', 'prop': 'name', 'value': term }];
		$scope.filterDegrees = $filter('nestedPropsFilter')($scope.degrees, obj);
		$scope.currentPage = 1;
	});

	$scope.getSubjects = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/allsubjects').
			success(function(data) {
				$scope.subjects = data;
				$scope.filterSubjects = $scope.subjects;
			}).finally(function() {
				$scope.loading = false;
			});
	};


	$scope.showAddSubject = function(degree) {
		$scope.subjectPlaceholder = "Cargando...";
		$scope.selectedSubjects = {};
		$scope.allsubjects = {};
		$scope.getSubjects();
		$scope.getAllSubjects();
		$scope.getEEDSubjects(degree.id);
		$scope.selectedSubject = degree;
		$scope.getDegreeEnrollmentsCount(degree);
		$scope.weightAdd = '';
		$scope.subject = {};
		$('#add-subject-modal').modal('toggle');
	};

	$scope.getAllSubjects = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/allsubjects').
			success(function(data) {
				$scope.allsubjects = data;
				$scope.subjectPlaceholder = "Seleccione una tem\u00E1tica ...";
			});
	};

	$scope.getEEDSubjects = function(id) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/isubject/' + id + '/ievaluationtypes/selected').
			success(function(data) {
				$scope.degreeSubjects = [];
				for (var i = 0; i < data.length; i++) {
					var degreeSubject = data[i];
					$scope.degreeSubjects.push({ "weight": degreeSubject.weight, "subject": degreeSubject.subject });
				}
			});
	};


	$scope.addSubjectsArray = function(subject) {
		$scope.subjectObj = subject;
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado');
		} else {
			if ($scope.degreeEnrollmentsCount == 0) {
				if ((getWeightCount() + $scope.weightAdd) <= 1) {
					if (subject == null) {
						alertify.alert('Seleccione una tem&aacute;tica');
						return;
					} else if ($scope.weightAdd == '') {
						alertify.alert('Introduzca el peso de la tem&aacute;tica');
						return;
					}

					for (var i = 0; i < $scope.degreeSubjects.length; i++) {
						if ($scope.degreeSubjects[i].subject.name == $scope.subjectObj.name) {
							alertify.alert('No se puede repetir la tem&aacute;tica');
							return;
						}
					}
					
					$scope.degreeSubjects.push({ "weight": $scope.weightAdd, subject });
				} else {
					alertify.alert('La sumatoria de los pesos es superior a 1');
				}
			} else {
				alertify.alert('No se pueden editar las tem&aacute;ticas de la titulaci&oacute;n porque hay inscripciones activas en la titulaci&oacute;n');
			}
		}
	};

	$scope.adddArray = function() {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado');
		} else {
			if ($scope.degreeEnrollmentsCount == 0) {
				if (getWeightCount() == 1) {
					$http({
						method: 'POST',
						url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/' + $scope.selectedSubject.id + '/subjects/add',
						data: { 'model': $scope.degreeSubjects },
						headers: { 'Content-Subject': 'application/json' }
					}).success(function(responseMessage) {
						if (responseMessage.type == 'success') {
							$scope.getSubjects();
							$('#add-subject-modal').modal('hide');
						}

						alertify.alert(responseMessage.message);
					});
				} else {
					alertify.alert('No se puede guardar debido a que la sumatoria de pesos de las tem&aacute;ticas no es igual a 1');
				}
			} else {
				alertify.alert('No se pueden editar las tem&aacute;ticas de la titulaci&oacute;n porque hay inscripciones activas en la titulaci&oacute;n');
			}
		}
	};

	$scope.deleteDegreeSubject = function(subject, index) {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado');
		} else {
			if ($scope.degreeEnrollmentsCount == 0) {
				$scope.degreeSubjects.splice(index, 1);
			} else {
				alertify.alert('No se pueden editar las tem&aacute;ticas de la titulaci&oacute;n porque hay inscripciones activas en la titulaci&oacute;n');
			}
		}
	};

	$scope.deleteDegree = function(degree) {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado');
		} else {
			alertify.confirm("&iquest;Est&aacute; seguro que desea eliminar la titulaci&oacute;n del evento de evaluaci&oacute;n?", function (e) {
			    if (e) {
			    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees/delete/' + degree.id).success(function(responseMessage) {
			    		if (responseMessage.type == 'success') {
			    			$scope.getDegrees();
						}

						alertify.alert(responseMessage.message);
				    })
			    }
		   });
		}
	};

	$scope.stateEvent = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/state1').
			success(function(data) {
				$scope.stateEE = data.message;

			});
	};

	$scope.limpiar = function() {
		location.reload();
	};

	function getWeightCount() {
		var weightCount = 0;
		for (var i = 0; i < $scope.degreeSubjects.length; i++) {
			weightCount += $scope.degreeSubjects[i].weight;
		}
		return weightCount;
	}
}