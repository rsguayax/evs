
app.controller('enrollmentCtrl', ['$scope', '$http', '$timeout', '$interval', '$filter', 'config', 'flashService', 'filterFilter', enrollmentCtrl]);

function enrollmentCtrl($scope, $http, $timeout, $interval, $filter, config, flashService, filterFilter) {
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.enrollmentFilter = {};
		$scope.getEnrollments();
		$scope.getAllDegrees();
		$scope.getEvaluationCenters();
		$scope.evaluationCenters = [];
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
		$scope.loadingMattersAndTests = false;
		$scope.running = 0;
		$scope.firstnameAdd = "";
		$scope.lastnameAdd = "";
		$scope.identificationAdd = "";
		$scope.emailAdd = "";
		$scope.enrollmentID = 0;
		$scope.datae = [];
		$scope.allMattersWithTest = false;
		$scope.checkAllMattersWithTest();
		$scope.stateEE = "";
		$scope.stateEvent();
	};

	$scope.showAddEnrollment = function() {
		$scope.selectedLabelEnrollment = "Cargando Inscripciones";
		$scope.identificationAdd = "";
		$scope.firstnameAdd = "";
		$scope.lastnameAdd = "";
		$scope.emailAdd = "";
		$scope.enrollmentSelected = {};
		$scope.selectedDegrees = {};
		$scope.selectedEvaluationCenter = {};
		$('#add-enrollment-modal').modal('toggle');
	};

	$scope.showMatters = function(enrollment) {
		$scope.matters = [];
		$scope.enrollmentSelected = enrollment;
		$scope.getMatters(enrollment);
		$('#matters-modal').modal('toggle');
	};

	$scope.getMatters = function(enrollment) {
		$scope.loadingMattersAndTests = true;
		
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/' + enrollment.id + '/matters').
			success(function(data) {
				$scope.matters = data;
				for (var i = 0; i < $scope.matters.length; i++) {
					var matter = $scope.matters[i];
					$scope.getTestName(matter);
				}
				$scope.loadingMattersAndTests = false;
			});
	};

	$scope.getTestName = function(matter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/' + $scope.enrollmentSelected.id + '/matter/' + matter.id + '/testname').
			success(function(data) {
				matter.testName = data;
			});
	};

	$scope.getEnrollments = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollments').
			success(function(data) {
				$scope.enrollments = data;
				$scope.filterEnrollments = $scope.enrollments;
			}).finally(function() {
				$scope.loading = false;
			});
	};

	$scope.getAllDegrees = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees').
			success(function(data) {
				$scope.alldegrees = data;
			});
	};
	
	$scope.getEvaluationCenters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/evaluationcenters').
			success(function(data) {
				$scope.evaluationCenters = data;
			});
	};

	$scope.$watch('search.name', function(term) {
        $scope.filterEnrollments = $filter('filter')($scope.enrollments, term);
        $scope.currentPage = 1;
	});

	$scope.addEnrollment = function() {
		var d = $scope.selectedDegrees.selected
		var evaluationCenterId = $scope.selectedEvaluationCenter.selected != null ? $scope.selectedEvaluationCenter.selected.id : null;
		var data1 = {
			'degrees': d,
			'firstname': $scope.firstnameAdd, 'lastname': $scope.lastnameAdd,
			'identification': $scope.identificationAdd, 'email': $scope.emailAdd,
			'evaluationCenterId': evaluationCenterId
		};
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede crear inscripciones');
		} else {
			$http({
				method: 'POST',
				url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/add',
				data: data1,
				headers: { 'Content-Subject': 'application/json' }
			}).success(function(responseMessage) {
				if (responseMessage.type == 'success') {
					$scope.getEnrollments();
					$('#add-enrollment-modal').modal('hide');
				}
				
				alertify.alert(responseMessage.message);
			});
		};
	};

	$scope.showLoadDegrees = function() {
		$scope.loadDegreesProgress();
		$('#add-enrollment-modal').modal('toggle');
	};

	$scope.deleteEnrollment = function(enrollment) {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede eliminar la inscripci\u00F3n');
		} else {
			var message = enrollment.priority == 1 ? 'Se eliminar&aacute;n todas las inscripciones de ' + enrollment.userFullName + ' ' : '';
			alertify.confirm(message + "&iquest;Est&aacute; seguro que desea eliminar la inscripci&oacute;n?", function(e) {
				if (e) {
					$('#waiting-message').html("Eliminado inscripci&oacute;n");
					$('#waiting-modal').modal('toggle');
					$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/delete/' + enrollment.id).
						success(function(responseMessage) {
							$('#waiting-modal').modal('hide');
							if (responseMessage.type == 'success') {
								$scope.getEnrollments();
							}
							
							alertify.alert(responseMessage.message);
						});
				}
			});
		}
	};

	$scope.editEnrollment = function(enrollment) {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede editar la inscripci\u00F3n');
		} else {
			if (enrollment.priority != 1) {
				alertify.confirm("&iquest;Est&aacute; seguro que desea editar la inscripci&oacute;n?", function(e) {
					if (e) {
						$scope.identificationAdd = enrollment.userIdentification;
						$scope.firstnameAdd = enrollment.userFirstName;
						$scope.lastnameAdd = enrollment.userLastName;
						$scope.emailAdd = enrollment.userEmail;
						$scope.enrollmentID = enrollment.id;
						$scope.viewModalEditEnrollmentRevision(enrollment);
					}
				});
			} else {
				alertify.alert("No se puede editar una inscripci\u00F3n de priordad 1");
			}
		}
	};


	$scope.viewDataUser = function(identificationAdd) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/user/identification/' + $scope.identificationAdd).
			success(function(data) {
				$scope.viewUserD = data;
				$scope.firstnameAdd = data.firstName;
				$scope.lastnameAdd = data.lastName;
				$scope.emailAdd = data.email;
			});
	};

	$scope.viewModalEditEnrollmentRevision = function(enrollment) {
		$scope.selectedLabelEnrollment = "Cargando Inscripciones";
		$scope.selectedEnrollment = {};
		$scope.selectedDegrees = {};
		$scope.selectedEvaluationCenter = {};
		$('#add-enrollment-edit-modal').modal('toggle');
	};

	$scope.saveEditEnrollment = function(matter) {
		$scope.datae = matter;
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede editar la inscripcion');
		} else {
			//id de tematica ---> matter[0].degree.id || id de la inscripcion--->  $scope.enrollmentID
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollment/edit1/' + $scope.enrollmentID + '/degree/' + matter[0].degree.id).
				success(function(data) {
					//$scope.allenrollments = data;
				});
			location.reload();
		};
	};

	$scope.checkAllMattersWithTest = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/mattertest/checkallmatterswithtest').
			success(function(data) {
				$scope.allMattersWithTest = data;
				if (!data) {
					alertify.alert("Hay tem&aacute;ticas que no tienen un test asignado. Por favor, asigna un test a cada tem&aacute;tica antes de realizar nuevas inscripciones");
				}
			});
	};
	/**
	Método para limpiar variables
	 */
	$scope.cleanVariables = function() {
		$scope.firstnameAdd = "";
		$scope.lastnameAdd = "";
		$scope.identificationAdd = "";
		$scope.emailAdd = "";

	};

	/**
	VERIFICAR ESTADO DEL EVENTO
	 */
	$scope.stateEvent = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/state1').
			success(function(data) {
				$scope.stateEE = data.message;
			});
	};
}
