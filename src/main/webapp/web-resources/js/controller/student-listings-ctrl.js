app.controller('studentListingsCtrl', ['$scope', '$window', '$http', '$compile', '$timeout', '$q', 'flashService', studentListingsCtrl]);

function studentListingsCtrl($scope, $window, $http, $compile, $timeout, $q, flashService) {

    $scope.init = function(evaluationEventId) {
		$scope.evaluationEventId = evaluationEventId;
		$scope.disableDocButton = false;
		getEventCenters();
		getJourneys();
    };

    getEventCenters = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/eventcenter/list').success(function(data) {
		    $scope.eventCenters = data;
		    $scope.eventClassroomId = null;
		    $scope.eventClassrooms = {};
		    $scope.classroomTimeBlockId = null;
		    $scope.classroomTimeBlocks = {};
		    $scope.timeBlockId = null;
		    
		    $scope.eventCentersDoc = data;
		    $scope.eventClassroomIdDoc = null;
		    $scope.eventClassroomsDoc = {};
		    $scope.classroomTimeBlockIdDoc = null;
		    $scope.classroomTimeBlocksDoc = {};
		    $scope.timeBlockIdDoc = null;
		});
    };

    $scope.getEventClassrooms = function(type) {
    	var eventCenterId = $scope.eventCenterId;
    	if(type == 2) {
    		eventCenterId = $scope.eventCenterIdDoc;
    	}
    	if(eventCenterId) {
			$http.get(GLOBAL_EVENT_CENTER_URL + '/' + eventCenterId + '/eventclassroom/list').success(function(data) {
				if(type == 1) {
					$scope.eventClassrooms = data;
					$scope.eventClassroomId = null;
				} else {
					$scope.eventClassroomsDoc = data;
					$scope.eventClassroomIdDoc = null;
				}
			});
    	} else {
    		if(type == 1) {
    		  $scope.eventClassroomId = null;
    		  $scope.eventClassrooms = {};
    		  $scope.classroomTimeBlockId = null;
  		      $scope.classroomTimeBlocks = {};
    		} else {
    		  $scope.eventClassroomIdDoc = null;
      		  $scope.eventClassroomsDoc = {};
      		  $scope.classroomTimeBlockIdDoc = null;
    		  $scope.classroomTimeBlocksDoc = {};
    		}
    	}
    };

    $scope.getClassroomTimeBlocks = function(type) {
    	var eventClassroomId = $scope.eventClassroomId;
    	if(type == 2) {
    		eventClassroomId = $scope.eventClassroomIdDoc;
    	}
    	if(eventClassroomId) {
			$http.get(GLOBAL_EVENT_CLASSROOM_URL + '/' + eventClassroomId + '/admin/timeblocks').success(function(data) {
				if(type == 1) {
				    $scope.classroomTimeBlocks = data;
				    $scope.classroomTimeBlockId = null;
				} else {
					 $scope.classroomTimeBlocksDoc = data;
					 $scope.classroomTimeBlockIdDoc = null;
	    		}
			});
    	} else {
    		 if(type == 1) {    	
	    		 $scope.classroomTimeBlockId = null;
	    		 $scope.classroomTimeBlocks = {};
    		 } else {
    			 $scope.classroomTimeBlockIdDoc = null;
	    		 $scope.classroomTimeBlocksDoc = {};
    		 }
    	}
    };
    
    getJourneys = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/claasroomjourneys').success(function(data) {
		    $scope.journeys = data;
		});
    };
    
    
    $scope.getTimeBlocks = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/journeys').success(function(data) {
		    $scope.timeBlocks = data;
		    $scope.timeBlockId = null;
		});
    };

    $scope.generateListing = function() {
    	var eventCenterId = $scope.eventCenterId || 0;
    	var eventClassroomId = $scope.eventClassroomId || 0;
    	var classroomTimeBlockId = $scope.classroomTimeBlockId || 0;
	
		var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/listado-estudiantes-extendido-'
			+ eventCenterId + '-' + eventClassroomId + '-' + classroomTimeBlockId + '.pdf';
	
		$window.open(url, '_blank');
    };
    
    $scope.generateJourneysListing = function() {
    	var journeyId = $scope.journeyId || 0;
    	
		var url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/listado-estudiantes-extendido-'
			+ 0 + '-' + 0 + '-' + journeyId + '.pdf';
	
		$window.open(url, '_blank');
    };
    
    // genera los documentos y los visualiza en linea
    $scope.generateDocumentsOnline = function(type) {
    	var eventCenterId = $scope.eventCenterIdDoc || 0;
    	var eventClassroomId = $scope.eventClassroomIdDoc || 0;
    	var classroomTimeBlockId = $scope.classroomTimeBlockIdDoc || 0;
    	var identification = $scope.identification || 0;
    	if($scope.identification != 0) {
    		eventCenterId = 0;
        	eventClassroomId = 0;
        	classroomTimeBlockId = 0;
    	}
    	var url = "";
    	if(type == 1) {
			url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/generate-onlinedocs-'
				+ eventCenterId + '-' + eventClassroomId + '-' + classroomTimeBlockId + '-0';
	    } else {
	    	if(identification) {		
				url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/generate-onlinedocs-'
					+ eventCenterId + '-' + eventClassroomId + '-' + classroomTimeBlockId + '-' + identification;
	    	}
    	}
  
        $window.open(url, '_blank'); 

    };
    
    
    $scope.generateDocuments = function(type) {
    	$http.get(GLOBAL_EVALUATIONEVENT_URL + '/checkdocprocessactive').success(function(data) {
    		$scope.docProcessActive = data;
	    	if($scope.docProcessActive == 1) {
	    		alertify.alert("Ya existe un proceso de generación de documentación en marcha.")
	    		return;
	    	}
	    	
	    	var eventCenterId = $scope.eventCenterIdDoc || 0;
	    	var eventClassroomId = $scope.eventClassroomIdDoc  || 0;
	    	var classroomTimeBlockId = $scope.classroomTimeBlockIdDoc  || 0;
	    	var identification = $scope.identification || 0;
	    	var url = "";
	    	if(type == 1) {
				url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId  + '/generate-docs-'
					+ eventCenterId  + '-' + eventClassroomId  + '-' + classroomTimeBlockId  + '-0';
		    } else {
		    	if(identification) {		
					url = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId  + '/generate-docs-'
						+ eventCenterId  + '-' + eventClassroomId  + '-' + classroomTimeBlockId  + '-' + identification;
		    	}
	    	}
	    	
	    	$scope.disableDocButton = true;
	    	flashService.flash("Documento generándose, este proceso puede necesitar un tiempo variable para ser completado, recibir&aacute; un email cuando est&eacute; disponible", "info");	 
	    	$http({
	   		    method: 'GET',
	   		    url: url,
	   		    headers: {'Content-Type': 'application/json'}
	   	   }).success(function(data) {
	   		   $scope.disableDocButton = false;
	   		   flashService.flash("Documento generado, compruebe el área de descargas", "success");	 
	   	   });
    	});

    };
    
    $scope.checkForm = function(type) {
    	if(type == 1) {
	    	if((!$scope.eventCenterId
					&& !$scope.eventClassroomId
					&& !$scope.classroomTimeBlockId) || ($scope.eventCenterId > 0 && !$scope.eventClassroomId
							&& !$scope.classroomTimeBlockId) || ($scope.eventCenterId > 0
									&& $scope.eventClassroomId > 0
									&& $scope.classroomTimeBlockId > 0)) {
	    		return false;
	    	} else{
	    		return true;
	    	}
    	} else {
    		if(!$scope.eventCenterId == 0 && !$scope.eventClassroomId == 0 && !$scope.classroomTimeBlockId == 0) {
	    		return false;
	    	} else{
	    		return true;
	    	}
    	}
    };
    
    
    $scope.checkFormDoc = function(type) {
    	if(type == 1) {
	    	if((!$scope.eventCenterIdDoc 
					&& !$scope.eventClassroomIdDoc
					&& !$scope.classroomTimeBlockIdDoc ) || ($scope.eventCenterIdDoc  > 0 && !$scope.eventClassroomIdDoc 
							&& !$scope.classroomTimeBlockIdDoc ) || ($scope.eventCenterIdDoc  > 0
									&& $scope.eventClassroomIdDoc  > 0
									&& $scope.classroomTimeBlockIdDoc  > 0)) {
	    		return false;
	    	} else{
	    		return true;
	    	}
    	} else {
    		if(!$scope.eventCenterIdDoc  == 0 && !$scope.eventClassroomIdDoc  == 0 && !$scope.classroomTimeBlockIdDoc  == 0) {
	    		return false;
	    	} else{
	    		return true;
	    	}
    	}
    };
    
    
    $scope.checkIdentification = function() {
    	if(!$scope.identification) {
    		return false;
    	} else {
    		return true;
    	}
    };
    
    $scope.checkFormJourney = function() {
    	if($scope.journeyId) {
    		return false;
    	} else {
    		return true;
    	}
    };
    
    $scope.showInfo = function(opt) {
    	var message = "";
    	if(opt == 1) {
    		message = "Esta sección le permite generar un informe por evento " +
    				"(sin seleccionar ning&uacute;n filtro), por centro de evaluaci&oacute;n (seleccionando &uacute;nicamente centro)," +
    				" y por centro, aula y jornada (seleccionando todos los filtros) o un informe de contraseñas por alumno y grupo de evaluaci&oacute;n";
    	} else if (opt == 2) {
    		message = "Este listado le permite generar un informe por jornada gen&eacute;rica";
    	} else if (opt == 3) {
    		message = "Este listado le permite generar la documentaci&oacute;n de evaluaci&oacute;n";
    	} else if (opt == 4) {
    		message = "Esta sección le permite generar la documentaci&oacute;n de evaluaci&oacute;n por evento " +
				"(sin seleccionar ning&uacute;n filtro), por centro de evaluaci&oacute;n (seleccionando &uacute;nicamente centro)," +
				" y por centro, aula y jornada (seleccionando todos los filtros). Para generar un fichero ZIP con " +
				"la documentaci&oacute;n de todos los centros pulse en el bot&oacute;n sin seleccionar ning&uacute;n filtro.";
    	} else if (opt == 5) {
    		message = "Esta sección le permite generar la documentación de evaluaci&oacute;n por c&eacute;dula de alumno";
    	}
    	alertify
    	  .alert(message, function(){
    	    alertify.message('Aceptar');
    	  });
    };
    
    $scope.generateListingExcel = function() {
    	
    	var eventCenterId = $scope.eventCenterId || 0;
    	var eventClassroomId = $scope.eventClassroomId || 0;
    	var classroomTimeBlockId = $scope.classroomTimeBlockId || 0;
    	
    	if(eventCenterId == 0 || eventClassroomId == 0 || classroomTimeBlockId == 0) {
    		alertify.alert("Para el informe de contraseñas son obligatorios todos los filtros");
    		return;
    	}
    	
	
	   var reportUrl = GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/listado-passwords-'
			+ eventCenterId + '-' + eventClassroomId + '-' + classroomTimeBlockId;
		
	   $http({
		    method: 'GET',
		    url: reportUrl,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       var anchor = angular.element('<a/>');
	       var filename = 'listado-passwords-'
			+ eventCenterId + '-' + eventClassroomId + '-' + classroomTimeBlockId + '.csv';
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
    
    $scope.showFiles = function() {
    	$scope.loadZipFile();
		$('#show-files-modal').modal('toggle');
		$scope.eventCenterFileId = null;
		$scope.files = {};
	};
	
	$scope.loadZipFile = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/doc/zipfile').success(function(data) {
		    $scope.zipFile = data;
		});
    };
	
	
	$scope.loadFiles = function() {
    	if($scope.eventCenterFileId) {
			$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/eventcenter/' + $scope.eventCenterFileId + '/files').success(function(data) {
			    $scope.files = data;
			});
    	}
    };

    $scope.downloadFile = function(fileId) {
		var anchor = angular.element('<a/>');
		anchor.attr({
			href: GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId
				+ '/files/' + fileId + '/download-doc',
	        target: '_self',
        });
		angular.element(document.body).append(anchor);
		anchor[0].click();
		$timeout(function() {
			anchor.remove();
		}, 50);
	};
	
	$scope.checkDocProcessActive = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/checkdocprocessactive').success(function(data) {
    		$scope.docProcessActive = data;
	    });
	};


}
