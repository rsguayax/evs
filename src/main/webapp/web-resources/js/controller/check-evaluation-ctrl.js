app.controller('checkEvaluationCtrl', ['$scope', '$window', '$http', '$compile', checkEvaluationCtrl] );

function checkEvaluationCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		checkEvaluation();
	};
	
	$scope.printEvaluationInfo = function() {	
		$("#evaluationInfoPrint").printArea();
	};
	
	function checkEvaluation() {
		$scope.loadingEvaluationInfo = true;
		
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/check').success(function(data) {
			$scope.evaluationInfo = data;
			$scope.loadingEvaluationInfo = false;
	    });
	}
}