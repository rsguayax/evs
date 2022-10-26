app.controller('evaluationEventConfigurationCtrl', ['$scope', '$http', 'filterFilter', '$timeout', '$interval', 'config', evaluationEventConfigurationCtrl] );

function evaluationEventConfigurationCtrl($scope, $http, filterFilter, $timeout, $interval, config) {

	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;		
		getConfiguration();
	};

	$scope.editConfiguration = function() {
		var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/config/edit/' + $scope.configuration.id
		showWaitingModal('Guardando configuración...');

		$http.post(url, $scope.configuration, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
			hideWaitingModal();
			$('.alert').css('display', 'none');
			alertify.alert(responseMessage.message);
	    });
	};

	$scope.consultTasksRegistry = function() {
	    var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/config/getregistry';
	    $http.get(url, $scope.configuration, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data) {
			var data1 = filterFilter(data, {id: "studentsandschedulesloadtask_" + $scope.evaluationEventId});
			var data2 = filterFilter(data, {id: "evaluationschedulesmailingtask_" + $scope.evaluationEventId});
			var now = new Date();
			$scope.studentsAndSchedulesLoadNextExecutionDateTime = data1[0].delay > 0 ? now.getTime() + data1[0].delay : 0;
			$scope.evaluationSchedulesMailingNextExecutionDateTime = data2[0].delay > 0 ? now.getTime() + data2[0].delay : 0;
	    });
	};
	
	$scope.checkStudents = function() {
		var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/config/checkstudents';
		showWaitingModal('Guardando configuración...');

		$http.get(url, $scope.configuration, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data) {
			$scope.assignmentTypeDisabled = data;
	    });
	};
	
	$scope.correctionRuleRequired = function() {
		if ($scope.configuration && $scope.configuration.correctionRule) {
			return $scope.configuration.correctionRule.minGrade || $scope.configuration.correctionRule.maxGrade || $scope.configuration.correctionRule.type;
		}
		
		return false;
	};

	function getConfiguration() {

	    // Datepickers

	    $scope.dateOptions = {
		    initDate: new Date()
	    };

	    $scope.dateOptions1 = angular.copy($scope.dateOptions);
	    $scope.dateOptions2 = angular.copy($scope.dateOptions);
	    $scope.dateOptions3 = angular.copy($scope.dateOptions);
	    $scope.dateOptions4 = angular.copy($scope.dateOptions);

	    $scope.format = 'dd-MM-yyyy';
	    $scope.altInputFormats = ['d!-M!-yyyy'];

	    // Timepickers

	    $scope.hstep = 1;
	    $scope.mstep = 30;
	    $scope.ismeridian = false;


	    $http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/config/get').success(function(data) {
    		$scope.configuration = data;
    		$scope.configuration.evaluationEvent = {'id': $scope.evaluationEventId};

    		// Init
    		$scope.configuration.dailyStudentsAndSchedulesLoadStartDate = new Date($scope.configuration.dailyStudentsAndSchedulesLoadStartDate);
    		$scope.configuration.dailyStudentsAndSchedulesLoadEndDate = new Date($scope.configuration.dailyStudentsAndSchedulesLoadEndDate);
    		$scope.configuration.dailyEvaluationSchedulesMailingStartDate = new Date($scope.configuration.dailyEvaluationSchedulesMailingStartDate);
    		$scope.configuration.dailyEvaluationSchedulesMailingEndDate = new Date($scope.configuration.dailyEvaluationSchedulesMailingEndDate);
    		$scope.changed1();
    		$scope.changed2();
    		$scope.changed3();
    		$scope.changed4();
	    });
	    
	    $scope.assignmentTypeDisabled = 1;
		$http.get( GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/config/checkstudents', $scope.configuration, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(data) {
			$scope.assignmentTypeDisabled = data;
	    });
	};

	// Datepickers

	$scope.open1 = function() {
	    $scope.popup1.opened = true;
	};

	$scope.popup1 = {
		opened: false
	};

	$scope.changed1 = function () {
	    $scope.dateOptions2.minDate = $scope.configuration.dailyStudentsAndSchedulesLoadStartDate;
	};

	$scope.open2 = function() {
	    $scope.popup2.opened = true;
	};

	$scope.popup2 = {
		opened: false
	};

	$scope.changed2 = function () {
	    $scope.dateOptions1.maxDate = $scope.configuration.dailyStudentsAndSchedulesLoadEndDate;
	};

	$scope.open3 = function() {
	    $scope.popup3.opened = true;
	};

	$scope.popup3 = {
		opened: false
	};

	$scope.changed3 = function () {
	    $scope.dateOptions4.minDate = $scope.configuration.dailyEvaluationSchedulesMailingStartDate;
	};

	$scope.open4 = function() {
	    $scope.popup4.opened = true;
	};

	$scope.popup4 = {
		opened: false
	};

	$scope.changed4 = function () {
	    $scope.dateOptions3.maxDate = $scope.configuration.dailyEvaluationSchedulesMailingEndDate;
	};

}