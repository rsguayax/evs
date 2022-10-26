app.controller('evaluationEventMatterTestCtrl', ['$scope', '$http', '$timeout', '$interval', '$filter', 'config', 'flashService', 'filterFilter',  evaluationEventMatterTestCtrl]);

function evaluationEventMatterTestCtrl($scope, $http, $timeout, $interval, $filter, config, flashService, filterFilter) {
	
	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.mattersFilter = {};
		$scope.getMatters();
        $scope.currentPage = 1;
        $scope.pageSize = 10;  
        $scope.loading = true;
        $scope.checkAllMattersWithTest();
	};
	
	$scope.showEditTest = function(matter) {
		$scope.loadingTests = true;
		$scope.tests = [];
		$scope.testSelected = matter.test.test;
		$scope.matterSelected = matter;
		$scope.getMatterTests(matter);
		$('#tests-modal').modal('toggle');
	};
	
	$scope.getMatters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters').
	    	success(function(data) {
	    		$scope.matters = data;
	    		$scope.filterMatters  = $scope.matters;  
		    }).finally(function() {
		    	for(var i=0; i < $scope.matters.length; i++) {
		    		$scope.getTest($scope.matters[i]);
		    	}
	    		$scope.loading = false;
		    });
	};
	
	$scope.getTest = function(matter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/mattertest/' + matter.id + '/test').
	    	success(function(data) {
	    		matter.test = data;
		    });
	};
	
	$scope.getMatterTests = function(matter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/mattertest/' + matter.id + '/tests').
    		success(function(data) {
	    		$scope.tests = data;
	    		$scope.loadingTests = false;
    		});
	};
	
	$scope.selectTest = function(test) {
		$scope.testSelected = test;
		
		$('.test-checkbox').each(function () {
			$(this).prop('checked', $(this).val() == test.id);
		});
	};
	
	$scope.modifyTest = function() {
		alertify.confirm("Se modificar&aacute;n todos los test de las inscripciones que deben examinarse de la tem&aacute;tica " + $scope.matterSelected.matterName + " &iquest;Est&aacute; seguro que desea modificar el test?", function (e) {
		    if (e) {
		    	var url = GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/mattertest/' + $scope.matterSelected.id + '/selecttest'
				var data = {selectedTestId: $scope.testSelected.id};
				showWaitingModal('Seleccionando test...');
				
				$http.post(url, data, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
					hideWaitingModal();
					
					if (responseMessage.type == 'success') {
						$scope.getMatters();
						$('#tests-modal').modal('hide');
					}
					
					alertify.alert(responseMessage.message);
			    });
		    }
	   });
	};
	
	$scope.checkAllMattersWithTest = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/mattertest/checkallmatterswithtest').
		success(function(data) {
    		if (!data) {
    			alertify.alert("Hay tem&aacute;ticas que no tienen un test asignado");
    		}
		});
	};
	
	$scope.$watch('search.name', function (term) {
        $scope.filterMatters = $filter('filter')($scope.matters, term);
        
        $scope.currentPage = 1;
    });
}