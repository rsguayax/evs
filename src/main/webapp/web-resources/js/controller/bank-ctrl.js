app.controller('bankCtrl', ['$scope', '$http', 'filterFilter', '$timeout', '$interval', '$filter', 'config', 'flashService', 'mainService', bankCtrl]);

function bankCtrl($scope, $http, filterFilter, $timeout, $interval, $filter, config, flashService, mainService) {

    $scope.init = function(bankId) {
		if (!bankId) {
		    $scope.currentPage = 1;
		    $scope.pageSize = 10;
		    $scope.loading = true;
		    $scope.showAll = false;
		    $scope.getBanks();
		} else {
		    $scope.bankId = bankId;
		    $scope.currentPeriodId = null;
		    $scope.getTests();
		}
		mainService.loadEvaluationTypes().then(function(result) {
			$scope.evaluationTypes = result;
		});
    };

    $scope.getBanks = function() {
		$http.get(GLOBAL_BANK_URL + '/banks?showAll=' + $scope.showAll).success(function(data) {
		    $scope.banks = data;
		    $scope.filterBanks = data;
		    $scope.checkCountActiveTestsWithoutEvaluationType();
		}).finally(function() {
		    $scope.loading = false;
		});
    };
    
    $scope.checkCountActiveTestsWithoutEvaluationType = function() {
		$http.get(GLOBAL_BANK_URL + '/countactivetestswithoutevaluationtype').success(function(data) {
    		if (data > 0) {
    			alertify.alert('Hay ' + data + ' tests activos que no tienen un tipo de evaluaci&oacute;n asignado');
    		} 
	    });
	};
	
	$scope.showAllBanks = function() {
		$scope.showAll = true;
		$scope.loading = true;
		$scope.getBanks();
    };
    
    $scope.showActiveBanks = function() {
		$scope.showAll = false;
		$scope.loading = true;
		$scope.getBanks();
    };

    $scope.$watch('search.name', function(term) {
		var obj = {name: term};
	
		$scope.filterBanks = filterFilter($scope.banks, obj);
	
		$scope.currentPage = 1;
    });

    $scope.loadExternalBanks = function() {    	
    	alertify.confirm("\u00bfSeguro que quiere actualizar los bancos de preguntas?", function(e) {
        	if(e) {
	    		showWaitingModal("Actualizando bancos...");
	        	$http.get(GLOBAL_BANK_URL + '/loadexternalbanks').
	        	success(function(data) {
	        		if(typeof $scope.search !== "undefined") {
	        			$scope.search.name = '';
	        		}
	        	    $scope.banks = data;
	        	    $scope.filterBanks = data;
	        	    hideWaitingModal();
	        	    flashService.flash("Actualici&oacute;n completada", "success");
	        	});
	    	}
		});
    };

    $scope.getTests = function() {
		$http.get(GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/test/tests').
		success(function(data) {
		    $scope.tests = data;
		});
    };

    $scope.getPeriods = function() {
		$http.get(GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/period/periods').
		success(function(data) {
		    $scope.periods = data;
		});
    };

    $scope.getTeachersPeriods = function() {
		$http.get(GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/teacherperiod/periods').
		success(function(data) {
		    $scope.teacherPeriods = data;
		});
    };

    $scope.showTest = function(test) {
		$scope.test = test;
		$('#test-modal').modal('toggle');
    };

    $scope.editTest = function(test) {
		$http({
		    method: 'POST',
		    url: GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/test/' + test.id + '/edit',
		    data: test,
		    headers: {'Content-Type': 'application/json'}
		}).success(function(data) {
		    flashService.flash(data.message, data.type);
		    $('#test-modal').modal('toggle');
		    $scope.test = null;
		    $scope.getTests();
		});
    }

    $scope.showAddTeacherPeriod = function() {
		$scope.teachers = {};
		//$scope.teachers.selected = [];
		$('#add-teacher-period-modal').modal('toggle');
		$scope.getTeachers();
    };
    
    $scope.showAddPeriod = function(period) {
    	$scope.currentPeriodId = null;
    	if(period != undefined && period != null) {
    		$scope.currentPeriodId = period.id;
    		$('#initDate').val($filter('date')(period.initDate,'dd-MM-yyyy HH:mm'));
    		$('#endDate').val($filter('date')(period.endDate,'dd-MM-yyyy HH:mm'));
    	}
		$('#add-period-modal').modal('toggle');
    };

    $scope.getTeachers = function() {
		$http.get(GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/teachers').
		success(function(data) {
		    $scope.teachers = data;
		});
    };

    $scope.addTeacherPeriod = function(period) {
		period.teacher = $scope.teachers.selected;
		period.id = 0;
		$http({
		    method: 'POST',
		    url: GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/teacherperiod/add',
		    data: period,
		    headers: {'Content-Type': 'application/json'}
		}).success(function(data) {
		    flashService.flash(data.message, data.type);
		    $('#add-teacher-period-modal').modal('toggle');
		    $scope.period = null;
		    $scope.getTeachersPeriods();
		});
    };
    
    
    $scope.addPeriod = function(period) {
		period.id = 0;
		$http({
		    method: 'POST',
		    url: GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/period/add',
		    data: period,
		    headers: {'Content-Type': 'application/json'}
		}).success(function(data) {
		    flashService.flash("Creado correctamente", "success");
		    $('#add-period-modal').modal('toggle');
		    $scope.period = null;
		    $scope.getPeriods();
		});
    };
    
    $scope.modifyPeriod = function(period) {
    	period.id = $scope.currentPeriodId;
    	period.initDate = $('#initDate').val();
    	period.endDate = $('#endDate').val();
		$http({
		    method: 'POST',
		    url: GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/period/modify',
		    data: period,
		    headers: {'Content-Type': 'application/json'}
		}).success(function(data) {
			 flashService.flash("Modificado correctamente", "success");
		    $('#add-period-modal').modal('toggle');
		    $scope.period = null;
		    $scope.getPeriods();
		});
    };
    
    $scope.deletePeriod = function(period) {
		$http({
		    method: 'POST',
		    url: GLOBAL_BANK_URL + '/edit/' + $scope.bankId + '/period/delete',
		    data: period.id,
		    headers: {'Content-Type': 'application/json'}
		}).success(function(data) {
			 flashService.flash("Eliminado correctamente", "success");
		    $scope.period = null;
		    $scope.getPeriods();
		});
    };
    
    $scope.correctionRuleRequired = function() {
		if ($scope.test && $scope.test.correctionRule) {
			return $scope.test.correctionRule.minGrade || $scope.test.correctionRule.maxGrade || $scope.test.correctionRule.type;
		}
		
		return false;
	};
}