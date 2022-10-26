app.controller('evaluationEventTypesCtrl', ['$scope', '$http', '$timeout', '$interval', 'filterFilter', 'config', 'flashService', evaluationEventTypesCtrl] );

function evaluationEventTypesCtrl($scope, $http, $timeout, $interval, filterFilter, config, flashService) {
	
	$scope.init = function() {
		$scope.currentPage = 1;
        $scope.pageSize = 10;  
        $scope.loading = true;
        $scope.getEvaluationEventTypes();
	};
	
	$scope.showAddEvaluationEventType = function() {
		$scope.evaluationEventTypeData = {};
		$('#evaluation-event-type-modal').modal('toggle');
	};
	
	$scope.showEditEvaluationEventType = function(evaluationEventType) {
		$scope.evaluationEventTypeData = angular.copy(evaluationEventType);
		$('#evaluation-event-type-modal').modal('toggle');
	};
	
	$scope.getEvaluationEventTypes = function() {
		$http.get(GLOBAL_EVALUATION_EVENT_TYPE_URL + '/evaluationeventtypes').
	    	success(function(data) {
	    		$scope.evaluationEventTypes = data;
	    		$scope.filterEvaluationEventTypes = data;	 
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
	$scope.addEvaluationEventType = function() {
		var url = GLOBAL_EVALUATION_EVENT_TYPE_URL + '/add';

		$http.post(url, $scope.evaluationEventTypeData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
			if (responseMessage.type == 'success') {
				$scope.getEvaluationEventTypes();
				$('#evaluation-event-type-modal').modal('hide');
			}
			
			alertify.alert(responseMessage.message);
	    });
	};
	
	$scope.editEvaluationEventType = function() {
		var url = GLOBAL_EVALUATION_EVENT_TYPE_URL + '/edit';

		$http.post(url, $scope.evaluationEventTypeData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
			if (responseMessage.type == 'success') {
				$scope.getEvaluationEventTypes();
				$('#evaluation-event-type-modal').modal('hide');
			}
			
			alertify.alert(responseMessage.message);
	    });
	};
	
	$scope.deleteEvaluationEventType = function(evaluationEventType) {
		alertify.confirm("&iquest;Est&aacute; seguro que desea eliminar el tipo de evento de evaluaci&oacute;n?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_EVALUATION_EVENT_TYPE_URL + '/' + evaluationEventType.id + '/delete').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
						$scope.getEvaluationEventTypes();
					}
					
					alertify.alert(responseMessage.message);
			    })
		    }
	   });
	};
	
	$scope.enableEvaluationEventType = function(evaluationEventType) {
		alertify.confirm("&iquest;Est&aacute; seguro que desea habilitar el tipo de vento de evaluaci&oacute;n?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_EVALUATION_EVENT_TYPE_URL + '/' + evaluationEventType.id + '/enable').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
						$scope.getEvaluationEventTypes();
					}
					
					alertify.alert(responseMessage.message);
			    })
		    }
	   });
	};
	
	$scope.$watch('search.name', function (term) {
	   var obj = { name: term }

       $scope.filterEvaluationEventTypes = filterFilter($scope.evaluationEventTypes, obj);
       
       $scope.currentPage = 1;
	});	
}