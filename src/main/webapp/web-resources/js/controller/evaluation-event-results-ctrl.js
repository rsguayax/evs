app.controller('evaluationEventResultsCtrl', ['$scope', '$http', '$filter', '$timeout', 'config', 'flashService', evaluationEventResultsCtrl] );

function evaluationEventResultsCtrl($scope, $http, $filter, $timeout, config, flashService) {
	
	$scope.init = function(evaluationEventId, evaluationEventName) {
		
		$scope.loading = true;
		$scope.evaluationEventId = evaluationEventId;
		$scope.evaluationEventName = evaluationEventName;

        $scope.selection = [];
		$scope.getTests();
		$scope.getEvaluationTypes();
		$scope.allEemtSelected = false;
		
		$scope.extraScore = 0;
		// 1 : results, 2: evaluations
		$scope.selectedReport = 1;
		
        $scope.currentPage = 1;
        $scope.pageSize = 10;  
		
		$scope.matters = {};
		$scope.testsFilter = {};
		$scope.searchSelectedMatters = {};
		$scope.searchSelectedMatters.selected = null;
		$scope.allmatters = undefined;	
 		$scope.getMatters();
	};

	$scope.getTests = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/tests').
	    	success(function(data) {
	    		$scope.allTests = data;
	    		$scope.tests = angular.copy($scope.allTests);
	    		$scope.filterTests = angular.copy($scope.allTests);
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
	$scope.getEvaluationTypes = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/evaluationtypes').
	    	success(function(data) {
	    		$scope.evaluationTypes = data;
		    });
	};
	
	// TODO mostrar solo materias que tengan algun resultado
	$scope.showResultsReport = function() {
		$scope.selectedMatters = {};
		$scope.selectedEvaluationTypes = {};
 		$scope.reportTests = angular.copy($scope.allTests);
 		$scope.filterReportTests();
 		$scope.currentPageReportTests = 1;
        $scope.pageSizeReportTests = 5;  
 		$('#results-report-modal').modal('toggle');
	};
	
	
	$scope.showEvaluationReport = function() {
		$scope.selectedMatters = {};
		$scope.allMattersSelection = false;
 		$('#evaluation-report-modal').modal('toggle');
	};
	
	$scope.filterReportTests = function() {
		var reportTests = [];
		var selectedMatters = $scope.selectedMatters.selected;
		var selectedEvaluationTypes = $scope.selectedEvaluationTypes.selected;
		
		for (var i = 0; i < $scope.allTests.length; i++) {
			var test = $scope.allTests[i];
			
			if (test.state && test.state != 'ESPERA') {
				if (selectedMatters && selectedMatters.length > 0) {
					var validMatter = false;
					for (var j = 0; j < selectedMatters.length; j++) {
						if (test.evaluationEventMatter.id == selectedMatters[j].id) {
							validMatter = true;
							break;
						}
					}
					
					if (!validMatter) continue;
				}
				
				if (selectedEvaluationTypes && selectedEvaluationTypes.length > 0) {
					var validEvaluationType = false;
					for (var j = 0; j < selectedEvaluationTypes.length; j++) {
						if (test.test.evaluationType.id == selectedEvaluationTypes[j].id) {
							validEvaluationType = true;
							break;
						}
					}
					
					if (!validEvaluationType) continue;
				}
				
				reportTests.push(test);
			}
		}  
		
		$scope.reportTests = reportTests;
	};
	
	$scope.generateResultsReport = function() {
		if($scope.reportTests && $scope.reportTests.length > 0) {
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/resultsreport',
			    data: $scope.reportTests,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
		       var anchor = angular.element('<a/>');
		       var today = $filter('date')(new Date(),'ddMMyy-HHmmss');
		       var filename = $scope.evaluationEventName + "_" + today + ".csv";
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
		       
		       $('#results-report-modal').modal('toggle');
			   flashService.flash("Generado correctamente", "success");	 
		   });
		} else {
			alertify.alert("No hay tests seleccionados");
		}
	};
	
	$scope.generateEvaluationReport = function() {
		if($scope.allMattersSelection || ($scope.selectedMatters.selected != null && $scope.selectedMatters.selected.length > 0)) {
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/evaluationreport',
			    data: {"all": $scope.allMattersSelection, "matters": $scope.selectedMatters.selected},
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
		       var anchor = angular.element('<a/>');
		       var today = $filter('date')(new Date(),'ddMMyy-HHmmss');
		       var filename = $scope.evaluationEventName + "_" + today + ".csv";
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
		       
		       $('#evaluation-report-modal').modal('toggle');
			   flashService.flash("Generado correctamente", "success");	 
		   });
		} else {
			alertify.alert("Seleccione alguna asignatura o todo el evento");
		}
	};
	
	$scope.getMatters = function() {
		//$scope.loading = true;
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/matters').
	    	success(function(data) {
	    		$scope.allmatters = data;	 
		    }).finally(function() {
		    	//$scope.loading = false;
		    });
	};
		
	
	$scope.enablePublish = function() {
		if( $scope.checkSelection()) {
			$scope.loading = true;
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/enablepublish',
			    data: $scope.selection,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
			    $scope.tests = data.data;
			    $scope.filterTests = angular.copy($scope.tests);  
			    flashService.flash(data.message, data.type);
		   }).finally(function() {
			   $scope.resetTable();
		   });
		}
	};
		
	
	$scope.publish = function() {
		if( $scope.checkSelection()) {
			$scope.loading = true;
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/publish',
			    data: $scope.selection,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
			    $scope.tests = data.data;
			    $scope.filterTests = angular.copy($scope.tests);  
			    angular.forEach(data.messages, function (value, key) {
			    	 flashService.flash(value.message, value.type);
		        });  
		   }).finally(function() {
			   $scope.resetTable();
		   });
	   }
	};
	
	$scope.notify = function() {
		var allow = true;
		if( $scope.checkSelection()) {
			$scope.loading = true;
			angular.forEach($scope.selection, function(value, key) {
				if(allow) {
					if(value.state != GLOBAL_STATE_PUBLISHED) {
						flashService.flash("Solo pueden notificarse tests publicados", "warning");
						allow = false;
						$scope.loading = false;
					}
				}
			});
			if(allow) {
				$http({
				    method: 'POST',
				    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/notify',
				    data: $scope.selection,
				    headers: {'Content-Type': 'application/json'}
			   }).success(function(data) {
				    $scope.tests = data.data;
				    $scope.filterTests = angular.copy($scope.tests);  
				    flashService.flash(data.message, data.type);
			   }).finally(function() {
				   $scope.resetTable();
			   });
			}
		}
	};
	
	
	
	$scope.qualify = function() {
		if( $scope.checkSelection()) {
			$scope.loading = true;
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/qualify',
			    data: $scope.selection,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
			    $scope.tests = data.data;
			    $scope.filterTests = angular.copy($scope.tests);  
			    flashService.flash(data.message, data.type);
		   }).finally(function() {
			   $scope.resetTable();
		   });
	    }
	};
	
	
	$scope.showExtraScore = function(test) {
		$scope.extraScore = test.extraScore;
		$scope.extraScoreMatterTest = test;
	};

	
	$scope.setExtraScore = function() {
	   $scope.loading = true;
	   $scope.extraScore = $("#extrascore-val").val();
	   if(+$scope.extraScore == 0 || !angular.isNumber(+$scope.extraScore)) {
		   $scope.extraScore = 0;
	   } else  if(+$scope.extraScore > 100) {
		   flashService.flash("El valor no puede ser superior a 100", "danger");
		   $scope.loading = false;
		   return;
	   }
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/tests/' + $scope.extraScoreMatterTest.id + '/extrascore',
		    data: 'extraScore=' + $scope.extraScore,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	   }).success(function(data) {
		   $scope.extraScoreMatterTest.extraScore = +$scope.extraScore;
		   $('#extraScore-id').each(function(){ 
			   $(this).scope().$hide();
		   });
		   flashService.flash(data.message, data.type);	
		   $scope.loading = false;
	   });
	};
	
	
	$scope.extraScoreApplied = function() {
		if( $scope.checkSelection()) {
			$scope.loading = true;
			$http({
			    method: 'POST',
			    url: GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/extrascoreapplied',
			    data: $scope.selection,
			    headers: {'Content-Type': 'application/json'}
		   }).success(function(data) {
			    if (typeof data === 'string' || data instanceof String) {		
			    	flashService.flash("Se ha producido un error", "error");
		   		} else {
		   			$scope.tests = data;
		   		    $scope.filterTests = angular.copy($scope.tests);  
				    flashService.flash("Proceso aplicado correctamente", "success");
		   		}
		   }).finally(function() {
			   $scope.resetTable();
		   });
		}
	};
	
	$scope.resetTable = function() {
		$scope.searchSelectedMatters.selected = null;
    	$scope.loading = false;
    	$scope.clearSelection();
    	$scope.allEemtSelected = false;
	};
	
	
	
	$scope.selectAllEemt = function selectAllEemt() {
		$scope.allEemtSelected = !$scope.allEemtSelected;
		if($scope.allEemtSelected) { 
			angular.forEach($scope.filterTests, function(eemt, key) {
				var item = angular.copy(eemt);
				delete item.checked;
				$scope.selection.push(item);
				eemt.checked = 1;
			});
		} else {
			$scope.selection = [];
			angular.forEach($scope.filterTests, function(value, key) {value.checked = 0;});
		}

	};
	
    $scope.toggleSelection = function toggleSelection(eemt) {
		var idx = $scope.selection.findIndex(function(item){ return item.id == eemt.id})
		if (idx > -1) {
			$scope.selection.splice(idx, 1);
			eemt.checked = 0;
			$scope.allEemtSelected = false;
		} else {
			var item = angular.copy(eemt);
			delete item.checked;
			$scope.selection.push(item);
			eemt.checked = 1;
		}
    };
    
    
    $scope.clearSelection = function() {
    	$scope.selection = [];
    	$scope.allEemtSelected = false;
    };
    
    $scope.checkSelection = function() {
    	if($scope.selection == null || $scope.selection.length == 0) {
    		flashService.flash("Seleccione al menos un test", "warning");
    		return false;
    	}
    	
    	return true;
    };
    
   $scope.changeMatter = function(item, model) {
	   $scope.loading = true;
	   $scope.clearSelection();
	   var query = "";
	   if(item) {
		   query = '?eemid=' + item.id;
	   }
	   $http.get(GLOBAL_EVALUATIONEVENT_URL + '/results/' + $scope.evaluationEventId + '/tests' + query).
   		success(function(data) {
    		$scope.tests = data;
    		$scope.filterTests = angular.copy($scope.tests);
	    }).finally(function() {
	    	$scope.loading = false;
	    });
   };   
}