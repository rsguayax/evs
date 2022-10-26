app.controller('testsSchedulesLogCtrl', ['$scope', '$window', '$http', '$compile', testsSchedulesLogCtrl] );

function testsSchedulesLogCtrl($scope, $window, $http, $compile) {

	$scope.init = function(evaluationEventId, isAutomaticAvaluationEvent) {
		$scope.isAutomaticAvaluationEvent = isAutomaticAvaluationEvent;
	    $scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.currentPageGeneric = 1;
		$scope.pageSizeGeneric = 10;
		$scope.evaluationEventId = evaluationEventId;
		getStudentsWithLog();
		getGenericLogs();
	};

	$scope.showStudentLog = function(student) {
		showWaitingModal('Cargando el log');

		$http.get(GLOBAL_BASE_URL + '/evaluationassignment/' + student.evaluationAssignmentId + '/testsscheduleslog').success(function(data) {
			$scope.studentTestslog = data;
			$scope.logView = 1;
			$('#logModalTitle').text('Log del estudiante "' + student.fullName + '"')

			hideWaitingModal();
			$('#logModal').modal('toggle');
	    });
	}

	$scope.showStudentTestsLog = function() {
		$scope.logView = 1;
	}

	$scope.showStudentTestLog = function(studentTestlog) {
		$scope.studentTestlogs = studentTestlog.logs;
		$scope.logView = 2;
		$('#breadcrumbTest').text(studentTestlog.testName);
		$('#testName').text(studentTestlog.testName);
	}

	function getStudentsWithLog() {
		$scope.loadingStudentsWithLog = true;

		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/studentswithlog/list').success(function(data) {
    		$scope.studentsWithLog = data;
    		$scope.loadingStudentsWithLog = false;
	    });
	};
	
	function getGenericLogs() {
		$scope.loadingGenericLogs = true;

		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/genericlogs').success(function(data) {
    		$scope.genericLogs = data;
    		$scope.loadingGenericLogs = false;
	    });
	};


}