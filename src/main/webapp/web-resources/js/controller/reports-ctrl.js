app.controller('reportsCtrl', ['$scope', '$window', '$http', '$compile', '$timeout', 'flashService', reportsCtrl]);

function reportsCtrl($scope, $window, $http, $compile, $timeout, flashService) {

    $scope.init = function() {
		getEvaluationEvents();
		getDepartments();
    };

    
    getEvaluationEvents = function() {
		$http.get(GLOBAL_REPORTS_URL + '/evaluationevents').success(function(data) {
		    $scope.evaluationEvents = data;
		});
    };
    
    
    getDepartments = function() {
		$http.get(GLOBAL_REPORTS_URL + '/departments').success(function(data) {
		    $scope.departments = data;
		});
    };

  
    $scope.generateListingPdf = function(selectedReport) {
    	var evaluationEventId = $scope.evaluationEventId || 0;
    	var departmentId = $scope.departmentId || 0;
    	
    	var reportUrl = "";
    	if(selectedReport == 1) {
    		reportUrl = GLOBAL_REPORTS_URL + '/listado-validacion-bloques-'
			+ evaluationEventId + '-' + departmentId + '.pdf';
		} else {
			reportUrl = GLOBAL_REPORTS_URL + '/listado-validacion-tests-' + evaluationEventId + '-' + departmentId + '.pdf';
		}

		$window.open(reportUrl, '_blank');
    };
    
    $scope.generateListingExcel = function(selectedReport) {
    	
    	var evaluationEventId = $scope.evaluationEventId || 0;
    	var departmentId = $scope.departmentId || 0;
		var reportUrl = "";
		if(selectedReport == 1) {
			reportUrl = GLOBAL_REPORTS_URL + '/listado-validacion-bloques-'
			+ evaluationEventId + '-' + departmentId;
		} else {
			reportUrl = GLOBAL_REPORTS_URL + '/listado-validacion-tests-' + evaluationEventId + '-' + departmentId;
		}
		
	   $http({
		    method: 'GET',
		    url: reportUrl,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       var anchor = angular.element('<a/>');
	       var filename = reportUrl + '.csv';
	       anchor.attr({
	           href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
	           target: '_blank',
	           download: filename
	       });
	       angular.element(document.body).append(anchor);
	       anchor[0].click();
	       $timeout(function () {
		          anchor.remove();
		       }, 50);
	       
	       $('#report-modal').modal('toggle');

		   //flashService.flash("Generado correctamente", "success");	 
	   });
    };
   
 
}
