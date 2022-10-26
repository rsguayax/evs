app.controller('evaluationEventStudentsCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', evaluationEventStudentsCtrl] );

function evaluationEventStudentsCtrl($scope, $http, $timeout, $interval, config, flashService) {

	$scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;

		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;

		$scope.mainSearch = '';
		//$scope.getStudents();
		$scope.studentsProgress = 0;
		$scope.running = 0;
		$scope.showLoadBtn = 0;
		$scope.existsMatterWithBankVar = 0;
		$scope.existsMatterWithBank();
	};

	$scope.showAddStudent = function() {
		$scope.selectLabel = "Cargando...";
		$scope.selectedStudents = {};
		$scope.getStudents();
		$scope.getAllStudents();
		$('#add-student-modal').modal('toggle');
	};

	$scope.getAllStudents = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/allstudents').
	    	success(function(data) {
	    		$scope.allstudents = data;
	    		$scope.allstudentsAsync = data;
	    		$scope.selectLabel = "Seleccione estudiante (limitado 50 primeros resultados)...";
		    });
	};

	$scope.getStudents = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students?pageSize=10&page=' + $scope.currentPage + '&q=' + $scope.mainSearch).
	    	success(function(data) {
	    		$scope.students = data.data;
	    		$scope.totalItems = data.total;
		    });
	};

	$scope.pageChanged = function() {
		$scope.currentPage = this.currentPage;
		$scope.getStudents();
	};


	$scope.deleteStudent = function(student) {
		alertify.confirm("Se eliminar&aacute; la asociaci&oacute;n con el estudiante, &iquest;est&aacute; seguro de continuar?", function(e)  {
			if (e) {
				$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students/delete?idStudent=' + student.id).
		    	success(function(data) {
		    		if(data.error == 0) {
			    		var index = $scope.students.indexOf(student);
				    	$scope.students.splice(index, 1);
		    		}
			    	flashService.flash(data.message, data.type);
			    });
			}
		});
	};

	$scope.addStudents = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students/add',
		    data: $scope.selectedStudents.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getStudents();
		   for(var i=0; i < $scope.selectedStudents.selected.length; i++) {
			   var index = $scope.allstudents.indexOf($scope.selectedStudents.selected[i]);
		       $scope.allstudents.splice(index, 1);
		   }
	       $('#add-student-modal').modal('toggle');
	       $scope.selectedStudents.selected = {};
		   flashService.flash("Asignados correctamente", "success");
	   });
	};

	/******************************************************************/
	$scope.showAddMatter = function(student) {
		$scope.selectLabel = "Cargando...";
		$scope.loadingMatters = true;
		$scope.matters = {};
		$scope.allmatters = undefined;
		$scope.selectedMatters = {};
		$scope.studentSelected = student;
		$scope.getMatters(student);
		$scope.getAllMatters(student);
		$('#add-matter-modal').modal('toggle');
	};

	$scope.getAllMatters = function(student) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/' + student.id + '/allmatters').
	    	success(function(data) {
	    		$scope.allmatters = data;
	    		$scope.selectLabel = "Seleccione asignatura (limitado 50 primeros resultados)...";
		    });
	};
	
	$scope.existsMatterWithBank = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/existsmatterwithbank').
	    	success(function(data) {
	    		if(data == 1) {
	    			$scope.getStudents();
	    			$scope.existsMatterWithBankVar = 1;
	    		} else {
	    			$scope.existsMatterWithBankVar = 0;
	    		}
	    		$scope.loading = false;
		    });
	};
	

	$scope.getMatters = function(student) {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/' + student.id + '/matters').
	    	success(function(data) {
	    		$scope.matters = data;
	    		$scope.loadingMatters = false;
		    });
	};

	$scope.addMatters = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/' + $scope.studentSelected.id + '/matters/add',
		    data: $scope.selectedMatters.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $('#add-matter-modal').modal('toggle');
	       $scope.selectedMatters.selected = {};
	       $scope.studentSelected = {};
		   flashService.flash("Asignados correctamente", "success");
	   });
	};

	$scope.deleteStudentMatter = function(matter) {
		alertify.confirm("Se eliminar&aacute; la asociaci&oacute;n con la asignatura, &iquest;est&aacute; seguro de continuar?", function(e)  {
			if (e) {
				$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/' + $scope.studentSelected.id + '/matters/delete?matterId=' + matter.id).
			    	success(function(data) {
			    		var index = $scope.matters.indexOf(matter);
			    		$scope.allmatters.push(matter);
				    	$scope.matters.splice(index, 1);
				    	flashService.flash("Eliminado correctamente", "success");
				    });
			}
		});
	};

	/******************************************************************/
	$scope.showMatterTests = function(matter) {
		$scope.loadingTests = true;
		$scope.tests = {};
		$scope.matterSelected = matter;
		$scope.getMatterTests();
		$('#tests-modal').modal('toggle');
	};


	$scope.getMatterTests = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/student/' + $scope.studentSelected.id + '/matter/' + $scope.matterSelected.id + '/tests').
	    	success(function(data) {
	    		$scope.tests = data;
	    		$scope.loadingTests = false;
		    });
	};

	/**********************************************************/

	$scope.showLoadStudents = function() {
		$scope.loadStudentsProgress();

		$('#load-students-modal').modal('toggle');
	};
 
	$scope.loadStudentsProgress = function() {
		$interval.cancel($scope.timer);
		$timeout($scope.timer = $interval(function(){
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students/loadstudentspr').
	    	success(function(data) {
	    		$scope.studentsProgress = data.progress;
	    		$scope.running = data.running;
	    		if($scope.running == 0 && $scope.studentsProgress == 100) {
	    			$scope.loadStudentsReset()
	    			$interval.cancel($scope.timer);
	    			flashService.flash(data.message, data.message_type);
	    			$('#load-students-modal').modal('hide');
	    			$scope.getStudents();
	    			$scope.studentsProgress = 0;
	    			$scope.showLoadBtn = 1;
	    		} else if($scope.studentsProgress == 0) {
	    			$interval.cancel($scope.timer);
	    			$scope.showLoadBtn = 1;
	    		}
		    });
		  }, 2000), 5000);
	};

	$scope.loadAllStudents = function() {
		var modeLoad = $("#modeLoad").val();
		var academicPeriodLoad = $("#academicPeriodLoad").val();
		var academicLevelLoad = $("#academicLevelLoad").val();
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students/loadstudents',
			params:  {"modeLoad": modeLoad, "academicPeriodLoad": academicPeriodLoad,  "academicLevelLoad": academicLevelLoad}
		}).success(function(data) {
			//$scope.showLoadBtn = 1;
		});
		$scope.studentsProgress = 1;
		$scope.running = 1;
		$scope.showLoadBtn = 0;
		$scope.loadStudentsProgress();
	};

	$scope.loadStudentsReset = function() {
		$http({
			method: 'GET',
			url: GLOBAL_EVALUATIONEVENT_URL + '/edit/' + $scope.evaluationEventId + '/students/loadstudentsreset',
		}).success(function(data) {
		});
		$timeout(function() {}, 2000);
	};

}