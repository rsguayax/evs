app.controller('evaluationEventEnrollmentRevisionCtrl', ['$scope', '$http', '$timeout', '$interval', '$filter', 'config', 'flashService', 'filterFilter', evaluationEventEnrollmentRevisionCtrl]);

function evaluationEventEnrollmentRevisionCtrl($scope, $http, $timeout, $interval, $filter, config, flashService, filterFilter) {

	$scope.init = function(evaluationEvent, state) {
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
		$scope.filterFormData = {};
		$scope.evaluationEventId = evaluationEvent;
		$scope.getEnrollmentRevisions();
		$scope.getDegrees();
		$scope.stateEE = "";
		$scope.stateEvent();
		$scope.loadingData = true;
		
		$scope.status = [
			{ id: 5, name: 'ADMITIDO MANUALMENTE' },
			{ id: 8, name: 'DENEGADO' }
		];
	};
	
	$scope.generateRandomGrades = function() {
		$('#waiting-modal').modal('toggle');
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/enrollmentsrevision/generaterandomgrades').
		success(function(responseMessage) {
			$('#waiting-modal').modal('hide');
			if (responseMessage.type == 'success') {
				$scope.getEnrollmentRevisions();
			}
			
			alertify.alert(responseMessage.message);
		});
	}
	
	$scope.deleteEnrollmentsRevision = function() {
		$('#waiting-modal').modal('toggle');
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/enrollmentsrevision/delete').
		success(function(responseMessage) {
			$('#waiting-modal').modal('hide');
			if (responseMessage.type == 'success') {
				$scope.getEnrollmentRevisions();
			}
			
			alertify.alert(responseMessage.message);
		});
	}

	$scope.newEnrollmentsRevision = function() {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede editar las inscripciones');
		} else {
			$('#waiting-modal').modal('toggle');
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/enrollmentsrevision/new').
			success(function(responseMessage) {
				$('#waiting-modal').modal('hide');
				if (responseMessage.type == 'success') {
					$scope.getEnrollmentRevisions();
				}
				
				alertify.alert(responseMessage.message);
			});
		}
	}

	$scope.getEnrollmentRevisions = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollmentrevisionlist1').
			success(function(data) {
				$scope.loadingData = false;
				$scope.enrollmentRevisions = data;
				$scope.filteredEnrollmentRevisions = $scope.enrollmentRevisions;
			}).finally(function() {
				$scope.loading = false;
			});
	};

	$scope.viewModalEditEnrollmentRevision = function(enrollment) {
		$scope.enrollmentObj = enrollment;
		$scope.status.selected = null;
		$('#add-enrollment-revision-modal').modal('toggle');
	};

	$scope.editEnrollmentRevision = function(enrollment, status) {
		if ($scope.stateEE == "cerrado") {
			alertify.alert('El evento esta cerrado, no se puede editar las inscripciones');
		} else {
			$('#waiting-modal').modal('toggle');
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/enrollmentsrevision/' + enrollment.enrollmentId + '/updatestatus/' + status.id).
				success(function(responseMessage) {
					$('#waiting-modal').modal('hide');
					if (responseMessage.type == 'success') {
						$('#add-enrollment-revision-modal').modal('hide');
						$scope.getEnrollmentRevisions();
					}
					
					alertify.alert(responseMessage.message);
				});
		};
	};

	$scope.getDegrees = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/degrees').
			success(function(data) {
				$scope.degrees = data;
			}).finally(function() {
		});
	};

	$scope.printReport = function() {
		$('#waiting-modal').modal('toggle');
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollmentrevisionlist/print').
			success(function(data) {
				$('#waiting-modal').modal('hide');
				var contentTypeHeader = 'application/vnd.ms-excel';
				var blob = new Blob([data], { type: contentTypeHeader });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'EvaluationEventReports.xls';
				link.click();
			});
	};

	$scope.printReportByDegree = function() {
		$('#waiting-modal').modal('toggle');
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/enrollmentrevisionlist/degree/' + $scope.filterFormData.degree.degree.id + '/print2').
			success(function(data) {
				$('#waiting-modal').modal('hide');
				var contentTypeHeader = 'application/vnd.ms-excel';
				var blob = new Blob([data], { type: contentTypeHeader });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				link.download = 'EvaluationEventReports.xls';
				link.click();
			});
	};

	$scope.closeEvent = function() {
		$scope.stateEvent();
		if ($scope.stateEE == "cerrado") {
			alertify.alert("El evento ya se encuentra cerrado");
		} else {
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/close1').
				success(function(data) {
					alertify.alert("El evento se ha cerrado correctamente.");

				});
		}

	};

	$scope.stateEvent = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/state1').
			success(function(data) {
				$scope.stateEE = data.message;
			});
	};
	
	$scope.clearFilter = function() {
		$scope.filterFormData = {};
		$scope.filterEnrollmentRevisions();
	};
	
	$scope.filterEnrollmentRevisions = function() {
		$scope.filteredEnrollmentRevisions = $filter('filter')($scope.enrollmentRevisions, $scope.filterFormData.search);
		if ($scope.filterFormData.degree != null) {
			$scope.filteredEnrollmentRevisions = $filter('filter')($scope.filteredEnrollmentRevisions, {degreeId: $scope.filterFormData.degree.degree.id});
		}
		$scope.currentPage = 1;
    };
}