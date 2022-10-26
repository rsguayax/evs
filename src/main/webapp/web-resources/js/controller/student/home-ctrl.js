app.controller('homeCtrl', ['$scope', '$window', '$http', '$compile', homeCtrl] );

function homeCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function() {
		getEvaluationAssignments();
	};
	
	function getEvaluationAssignments() {
		$http.get(GLOBAL_STUDENT_BASE_URL + '/evaluationassignment/list').success(function(data) {
    		$scope.evaluationAssignments = data;
	    });
	};
}