app.controller('evaluationEventMattersCtrl', ['$scope', '$http', '$timeout', '$interval', '$filter', 'config', 'flashService', 'filterFilter',  evaluationEventMattersCtrl]);

function evaluationEventMattersCtrl($scope, $http, $timeout, $interval, $filter, config, flashService, filterFilter) {
	

	$scope.init = function(evaluationEventId) {
		$scope.availableDays = [{"id": "1", "code": "LUN", "name": "Lunes"}, {"id": "2", "code": "MAR", "name": "Martes"}, 
		                        {"id": "3", "code": "MIE", "name": "Mi\u00e9rcoles"}, 
		                  {"id": "4", "code": "JUE", "name": "Jueves"}, {"id": "5", "code": "VIE", "name": "Viernes"}, 
		                  {"id": "6", "code": "SAB", "name": "S\u00e1bado"}, 
		                  {"id": "7", "code": "DOM", "name": "Domingo"}];
		$scope.evaluationEventId = evaluationEventId;
		$scope.mattersFilter = {};
		//$scope.allDays = $scope.availableDays;
		$scope.getMatters();
		$scope.extraScore = 0;
		$scope.mattersProgress = 0;
    	$scope.bankSelected = undefined;
    	$scope.getAllTypes();
        $scope.currentPage = 1;
        $scope.pageSize = 10;  
        $scope.loading = true;
        $scope.running = 0;
        $scope.showLoadBtn = 0;
        $scope.academicPeriodAdd = "";
        $scope.modeAdd = "";
        $scope.currentDate = new Date();
	};
	
	$scope.showEvaluableDays = function(matter) {
		$scope.matterDays = {};
		$scope.daysSelected = {};
		//$scope.selectMatterFields(matter);
		//$scope.allDays = {};
		$scope.matterSelected = matter;
		$scope.getMatterDays(matter);
		
		$('#add-days-modal').modal('toggle');
	};
	
	$scope.removeDay = function(item, model) {
		$scope.allDays.push(item);
	}
	
	$scope.getMatterDays = function(matter) {
		$scope.daysPlaceholder = "Cargando...";
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/days?matterId='+matter.id).
			success(function(data) {
				$scope.matterDays.selected = $filter('orderBy')(data, 'id', false);
				var tmpDays = angular.copy($scope.availableDays);
				angular.forEach(data, function(value, key) {
					delete tmpDays[value.id - 1];
				});
				$scope.allDays = tmpDays;
				$scope.daysPlaceholder = "Seleccione d\u00cdas...";
			});
	};
	
	$scope.showAddMatter = function() {	
		$scope.selectLabel = "Cargando...";
		$scope.selectedMatters = {};
		$scope.allmatters = {};
 		$scope.getMatters();
		$scope.getAllMatters();
		$('#add-matter-modal').modal('toggle');
	};
	
	$scope.showExtraScore = function(matter) {	
		$scope.selectMatterFields(matter);
		$scope.extraScoreMatterId = matter.id;
	};

	
	$scope.getAllMatters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/allmatters').
	    	success(function(data) {
	    		$scope.allmatters = data;  
	    		$scope.selectLabel = "Seleccione asignatura (limitado 50 primeros resultados)...";
		    });
	};
	
	$scope.getMatters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters').
	    	success(function(data) {
	    		$scope.matters = data;	 
	    		$scope.filterMatters  = $scope.matters;  
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
	$scope.deleteMatter = function(matter) {
		alertify.confirm("Se eliminar&aacute; la asociaci&oacute;n con la asignatura, &iquest;est&aacute; seguro de continuar?", function(e)  {
			if (e) {
				$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/delete?idMatter=' + matter.id).
		    	success(function(data) {
		    		var index = $scope.matters.indexOf(matter);
			    	$scope.matters.splice(index, 1);  
			    	var index = $scope.filterMatters.indexOf(matter);
			    	$scope.filterMatters.splice(index, 1);  
			    	flashService.flash(data.message, data.type); 
			    });
			}
		});
	};
	
	$scope.addMatters = function() {
		var d= {'matters': $scope.selectedMatters.selected, 'academicPeriodAdd': $scope.academicPeriodAdd, 'modeAdd': $scope.modeAdd};
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/add',
		    data: {'matters': $scope.selectedMatters.selected, 'academicPeriodAdd': $scope.academicPeriodAdd, 'modeAdd': $scope.modeAdd},
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getMatters();
	       $('#add-matter-modal').modal('toggle');
	       $scope.selectedMatters.selected = {};
		   flashService.flash("Asignados correctamente", "success");	 
	 });
	};
	
	$scope.addDays = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matter/' + $scope.matterSelected.id + '/days/add',
		    data: $scope.matterDays.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if(data.data != null && data.data != undefined) {
			   var tmpDays = $scope.availableDays;
			   angular.forEach($scope.matters, function(value, key) {
					if(value.id == $scope.matterSelected.id) {
						value = data.data;
					}
				});
			   $scope.allDays = tmpDays;
		   }
	       $('#add-days-modal').modal('toggle');
		   flashService.flash(data.message, data.type);	 
	 });
	};
	
		
	$scope.setExtraScore = function() {
	   $scope.extraScore = $("#extrascore-val").val();
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/' + $scope.extraScoreMatterId + '/extrascore',
		    data: 'extraScore=' + $scope.extraScore,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	   }).success(function(data) {
		   $scope.getMatters();
		   $('#extraScore-id').each(function(){ 
			   $(this).scope().$hide();
		   });
		   flashService.flash("Modificado correctamente", "success");	 
	   });
	};
	
	$scope.showLoadMatters = function() {
		$scope.loadMattersProgress();

		$('#load-matters-modal').modal('toggle');
	};
	
	$scope.loadMattersProgress = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/loadmatterspr').
	    	success(function(data) {
	    		$scope.mattersProgress = data.progress;
	    		$scope.running = data.running;
	    		if($scope.running == 0 && $scope.mattersProgress == 100) {
	    			$scope.loadMattersReset()
	    			$interval.cancel($scope.timer);
	    			flashService.flash(data.message, data.message_type);
	    			$('#load-matters-modal').modal('hide');
	    			$scope.loading = true;
	    			$scope.getMatters();
	    			$scope.mattersProgress = 0;
	    			$scope.showLoadBtn = 1;
	    		} else if($scope.mattersProgress == 0) {
	    			$interval.cancel($scope.timer);
	    			$scope.showLoadBtn = 1;
	    		}
		    });
		  }, 2000), 3000);
	};
	
	$scope.loadAllMatters = function() {
		var modeLoad = $("#modeLoad").val();
		var academicPeriodLoad = $("#academicPeriodLoad").val();
		var academicLevelLoad = $("#academicLevelLoad").val();
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/loadmatters',
			params:  {"modeLoad": modeLoad, "academicPeriodLoad": academicPeriodLoad,  "academicLevelLoad": academicLevelLoad}
		}).success(function(data) {
			//$('#load-matters-modal').modal('hide');
			//flashService.flash(data.message, data.type);
			//$interval.cancel($scope.timer);
			//$scope.mattersProgress = 0;
			//$scope.showLoadBtn = 1;
		});
		$scope.mattersProgress = 1;
		$scope.running = 1;
		$scope.showLoadBtn = 0;
		$scope.loadMattersProgress();
	};
	
	$scope.loadMattersReset = function() {
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/loadmattersreset',		
		}).success(function(data) {
		});
		$timeout(function() {}, 2000); 
	};
	
	
	$scope.removeMatters = function() {
		alertify.confirm("Se eliminar&aacute;n todas las asociaciones de asignaturas con el evento, &iquest;est&aacute; seguro de continuar?", function(e)  {
			if (e) {
				showWaitingModal("Eliminando asignaturas...");	
				$http({
						method: 'POST',
						url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/deletematters',
					}).success(function(data) {				 
						 flashService.flash(data.message, data.type);
						 $scope.getMatters();
						 hideWaitingModal();
					});	
			}
		});
	};
	
      $scope.$watch('search.name', function (term) {
          $scope.filterMatters = $filter('filter')($scope.matters, term);
          
          $scope.currentPage = 1;
      });
      
	
		
     /**********************************************************************************/
		
	$scope.showSelectBank = function(matter) {
		$scope.bankMatter = {};
		$scope.selectMatterFields(matter);
		$scope.getSelectedBank(matter);
		$scope.matterSelected = matter;
		$scope.getBanks(matter);
		
		$('#add-bank-modal').modal('toggle');
	};
		
		
	$scope.getBanks = function(matter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matter/' + matter.id + '/banks').
	    	success(function(data) {
	    		$scope.banks = data;
	    	});
	};
	
	$scope.setBank = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matter/' + $scope.matterSelected.id + '/banks/selected',
		    data: $scope.bankMatter.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.matterSelected.bank = $scope.bankMatter.selected;
		   if(data.error == 0) {
			   $scope.matterSelected.hasBank = true;
		   }
	       $('#add-bank-modal').modal('toggle');   
	       flashService.flash(data.message, data.type);	 
	 });
	};
	
	$scope.getSelectedBank = function(matter) {
		//$scope.bankMatter.selected = matter.bank;
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matter/' + matter.id + '/banks/loadselected',
		    data:  $scope.bankMatter.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $scope.bankMatter.selected = data; 
	   });
	};
	
	
	$scope.selectMatterFields = function(matter) {
		$scope.extraScore = matter.extraScore;
   	 	$scope.bankSelected = matter.bank;	   
	};
	
	
	/******************************************************************************/
	$scope.addTypes = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matters/' + $scope.selectedMatter.id + '/evaluationtypes/add',
		    data: $scope.selectedTypes.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $('#evaluation-types-modal').modal('toggle');
	       $scope.selectedTypes.selected = {};
	       angular.forEach(data.messages, function(value, key) {
	    	   flashService.flash(value.message, value.type);	 
	       });	  
	 });
	};
	
	
	$scope.showLoadTypes = function(matter) {
		$scope.selectedTypes = {};
		$scope.getSelectedTypes(matter);
		$scope.selectedMatter = matter;
		$('#evaluation-types-modal').modal('toggle');
	};
	
	$scope.getAllTypes = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/evaluationtypes').
	    	success(function(data) {
	    		$scope.allTypes = data;  
		    });
	};
	
	$scope.getSelectedTypes = function(matter) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/matter/' + matter.id + '/evaluationtypes/selected').
	    	success(function(data) {
	    		 $scope.selectedTypes.selected = data;
		    });
	};
	
	$scope.showAddMatterBtn = function() {
	    if($scope.academicPeriodAdd === undefined || $scope.modeAdd === undefined || 
	    		$scope.academicPeriodAdd === null || $scope.modeAdd === null ||
	    			$scope.academicPeriodAdd === '' || $scope.modeAdd === '') {
	    				return false;
	    			} else {
	    				return true;
	    			}
	    			
	};
	
	
		
		
}