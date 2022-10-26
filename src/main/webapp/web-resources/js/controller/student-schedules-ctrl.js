app.controller('studentSchedulesCtrl', ['$scope', '$window', '$http', '$compile', studentSchedulesCtrl] );

function studentSchedulesCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function(studentId) {
		$scope.studentId = studentId;
		getEventsEvaluations();
	};
	
	$scope.showLastScheduleModification = function(lastScheduleModification) {
		$scope.lastScheduleModification = lastScheduleModification;
		$('#scheduleModificationModal').modal('toggle');
	};
	
	$scope.printScheduleModification = function() {	
		$("#scheduleModificationPrint").printArea();
	};
	
	function getEventsEvaluations() {
		$http.get(GLOBAL_STUDENTS_URL + '/' + $scope.studentId + '/eventsevaluations/list').success(function(data) {
    		$scope.eventsEvaluations = data;
	    });
	}
}