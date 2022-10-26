app.controller('evaluationCenterCtrl', ['$scope', '$http', 'filterFilter', 'config', 'flashService', evaluationCenterCtrl] );

function evaluationCenterCtrl($scope, $http, filterFilter, config, flashService) {
	
   $scope.init = function(evaluationCenterId) {
		 $scope.evaluationCenterId = evaluationCenterId;
		 if (evaluationCenterId != null) {
			 $scope.getAllCenters();
			 $scope.getCenters();
		 }
		 $scope.selectedCenters = {};
		 $scope.centeraux = {};
		 $scope.currentPage = 1;
         $scope.pageSize = 10;  
         $scope.loading = true;
         $scope.getEvaluationCenters();
   };
   
	
	$scope.getEvaluationCenters = function() {
		$http.get(GLOBAL_EVALUATIONCENTER_URL + '/evaluationcenters').
	    	success(function(data) {
	    		$scope.evaluationCenters = data;
	    		$scope.filterEvaluationCenters = data;	 
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
   $scope.$watch('search.name', function (term) {
	   var obj = { name: term }

       $scope.filterEvaluationCenters = filterFilter($scope.evaluationCenters, obj);
       
       $scope.currentPage = 1;
  });

   $scope.getAddresses = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/address').
		    success(function(data) {
		        $scope.addresses = data;
		    });
   };
	   
   $scope.createAddress = function(address) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/address/new',
		    data: address,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data != undefined && typeof data == "object"){
		       $scope.addresses.push(data);
		       flashService.flash("Creado correctamente", "success");	       
		   } else {
		       flashService.flash("Se ha producido un error", "danger");	 
		   }
		   $('#address-modal').modal('toggle');
	       $scope.address = null;
	   });
   };
   
   $scope.deleteAddress = function(address) {   
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/address/delete?id=' + address.id).
	    success(function(data) {
	    	var index = $scope.addresses.indexOf(address);
	    	$scope.addresses.splice(index, 1);     
	    	flashService.flash("Eliminado correctamente", "success");	 
	    });
   };
   
   $scope.editAddress = function(address) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/address/edit',
		    data: address,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $scope.getAddresses();
	       $('#address-modal').modal('toggle');
	       $scope.address = null;
	   });
   };
   
   $scope.showEditAddress = function(address) {
	   $scope.address = angular.copy(address);	   
	   $('#address-modal input#address_name').val(address.name);
	   $('#address-modal input#address_number').val(address.number);
	   $('#address-modal input#address_city').val(address.city);   
	   $('#address-modal input#address_phone').val(address.phone);
	   $('#address-modal select#address_type option[value=' + address.type + ']').prop('selected', true);
	   $('#address-modal input#address_id').val(address.id);
	   $('#address-modal').modal('toggle');
   };
   
   $scope.showCreateAddress = function() {
	   $scope.address = null;
	   $('#address-modal').modal('toggle');
   };
   
   
   
   /************** CLASSROOMS ******************/
   $scope.getClassrooms = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/classroom').
		    success(function(data) {
		        $scope.classrooms = data;
		    });
   };
	   
   $scope.createClassroom = function(classroom) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/classroom/new',
		    data: classroom,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.error == 0) {		
			   $scope.classrooms.push(data.data);
		   }
		   flashService.flash(data.message, data.type);	
	       $('#classroom-modal').modal('toggle');
	       $scope.classroom = null;
	   });
   };
   
   $scope.deleteClassroom = function(classroom) {   
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/classroom/delete?id=' + classroom.id).
	    success(function(data) {
	    	 if (data.error == 0) {			   
	    		var index = $scope.classrooms.indexOf(classroom);
	 	    	$scope.classrooms.splice(index, 1);   
		   	 }
	    	 flashService.flash(data.message, data.type);		  
	    });
   };
   
   $scope.editClassroom = function(classroom) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/classroom/edit',
		    data: classroom,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $scope.getClassrooms();
	       $('#classroom-modal').modal('toggle');
	       $scope.classroom = null;
	       flashService.flash(data.message, data.type);	
	   });
   };
   
   $scope.showEditClassroom = function(classroom) {
	   $scope.classroom = angular.copy(classroom);
	   $scope.getNets();
	   if ($scope.classroom.cap != null) {
		   $scope.getCapsUnassigned($scope.classroom.cap.id);
	   } else {
		   $scope.getCapsUnassigned();
	   }
	   $('#classroom-modal').modal('toggle');
   };
   
   $scope.showCreateClassroom = function() {
	   $scope.classroom = null;
	   $scope.getCapsUnassigned();
	   $('#classroom-modal').modal('toggle');
   };
   
   
   
   /****************** CAPs **********************/
   $scope.getCaps = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/cap').
		    success(function(data) {
		        $scope.caps = data;
		    });
   };
   
   $scope.getCapsUnassigned = function(capId) {
	   var capIdQuery = ''; 
	   if(capId != null) {
		   capIdQuery = '?capId=' + capId; 
	   }
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/capunassigned' + capIdQuery).
		    success(function(data) {
		        $scope.capsunassigned = data;
		    });
   };
   
   $scope.createCap = function(cap) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/cap/new',
		    data: cap,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.hasOwnProperty('id')) {
			   $scope.caps.push(data);
		       flashService.flash("CAP creado correctamente", "success");
		       $('#cap-modal').modal('toggle');
		       $scope.cap = null;
			} else if (data.hasOwnProperty('error')) {
				alertify.alert(data['error']);
			}
	   });
   };
   
   $scope.editCap = function(cap) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/cap/edit',
		    data: cap,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.hasOwnProperty('id')) {
			   $scope.getCaps();
			   flashService.flash("CAP modificado correctamente", "success");	
		       $('#cap-modal').modal('toggle');
		       $scope.cap = null;
			} else if (data.hasOwnProperty('error')) {
				alertify.alert(data['error']);
			}
	   });
   };
   
   $scope.deleteCap = function(cap) {
	   alertify.confirm("¿Está seguro que desea eliminar el CAP?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/cap/delete?id=' + cap.id).
			    success(function(data) {
			    	var index = $scope.caps.indexOf(cap);
			    	$scope.caps.splice(index, 1);     
			    	flashService.flash("CAP eliminado correctamente", "success");	 
			    });
		    }
	   });
   };
   
   $scope.showCreateCap = function() {
	   $scope.getServers();
	   $scope.cap = null;
	   $('#cap-modal').modal('toggle');
   };
   
   $scope.showEditCap = function(cap) {
	   $scope.cap = angular.copy(cap);
	   $scope.getServers();
	   $('#cap-modal input#cap_serialNumber').val(cap.serialNumber);
	   $('#cap-modal input#cap_id').val(cap.id);
	   $('#cap-modal').modal('toggle');
   };
   

   $scope.getServers = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/servers').
		    success(function(data) {
		        $scope.servers = data;
		    });
   };
   
   
   /********* CENTERS *************************/
   $scope.getAllCenters = function() {
		$http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/allcenters').
	    	success(function(data) {
	    		$scope.allcenters = data;  
		    });
	};
   
   
	$scope.getCenters = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/centers').
		    success(function(data) {
		        $scope.centers = data;
		    });
	};
	  
	$scope.showAddCenters = function() {	
		$scope.selectedCenters = {};
		$scope.getCenters();
		$scope.getAllCenters();
		$('#add-centers-modal').modal('toggle');
	};
	
	$scope.addCenters = function() {
		$http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/centers/add',
		    data: $scope.selectedCenters.selected,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   $scope.getCenters();
	       $('#add-centers-modal').modal('toggle');
	       $scope.selectedCenters.selected = {};
		   flashService.flash("Asignados correctamente", "success");	 
	 });
	};
	

   $scope.deleteCenter = function(center) {   
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/centers/delete?idcenter=' + center.id).
	    success(function(data) {
	    	var index = $scope.centers.indexOf(center);
	    	$scope.centers.splice(index, 1);     
	    	flashService.flash("Eliminado correctamente", "success");	 
	    });
   };
   
   $scope.showEditCenter = function(center) {
	   $scope.center = angular.copy(center);	   
	   $('#edit-center-modal input#center_name').val(center.name);
	   $('#edit-center-modal input#center_type').val(center.type);
	   $('#edit-center-modal input#center_address').val(center.address);   
	   $('#edit-center-modal input#center_code').val(center.code);
	   $('#edit-center-modal input#center_id').val(center.id);
	   $('#edit-center-modal').modal('toggle');
   };
   
   $scope.showCreateCenter = function() {
	   $scope.center = null;
	   $('#edit-center-modal').modal('toggle');
   };
   
   
   $scope.createCenter = function(center) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/center/new',
		    data: center,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.error == 0) {			   
			   $scope.centers.push(data.data);
	   	   }
		   
	       $('#edit-center-modal').modal('toggle');
	       flashService.flash(data.message, data.type);
	       $scope.center = null;
	   });
   };
   
   
   $scope.editCenter = function(center) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/center/edit',
		    data: center,
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.error == 0) {	
			   $scope.getCenters();
		   }
		   
		   $('#edit-center-modal').modal('toggle');
		   flashService.flash(data.message, data.type);
	       $scope.center = null;
	   });
   };
   
   
   
   /************** NETS ******************/
   $scope.getNets = function() {
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net').
		    success(function(data) {
		        $scope.nets = data;
		    });
   };
	   
   $scope.createNet = function(net) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net/new',
		    //data: net,
		    data: {'servers': $scope.selectedServers.selected, 'net': net},
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
		   if (data.error == 0) {		
			   $scope.nets.push(data.data);
		   }
		   flashService.flash(data.message, data.type);	
	       $('#net-modal').modal('toggle');
	       $scope.net = null;
	   });
   };
   
   $scope.deleteNet = function(net) {   
	   $http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net/delete?id=' + net.id).
	    success(function(data) {
	    	 if (data.error == 0) {			   
	    		var index = $scope.nets.indexOf(net);
	 	    	$scope.nets.splice(index, 1);   
		   	 }
	    	 flashService.flash(data.message, data.type);		  
	    });
   };
   
   $scope.editNet = function(net) {   
	   $http({
		    method: 'POST',
		    url: GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net/edit',
		    //data: net,
		    data: {'servers': $scope.selectedServers.selected, 'net': net},
		    headers: {'Content-Type': 'application/json'}
	   }).success(function(data) {
	       $scope.getNets();
	       $('#net-modal').modal('toggle');
	       $scope.net = null;
	       flashService.flash(data.message, data.type);	
	   });
   };
   
   $scope.showEditNet = function(net) {
	   $scope.net = angular.copy(net);
	   $scope.selectedServers = {};
	   $scope.allservers = {};
	   
	   $scope.getSelectedServers(net.id);
	   $scope.getAllServers(net.id);
	   $('#net-modal').modal('toggle');
   };
   
   $scope.showCreateNet = function() {
	   $scope.selectedServers = {};
	   $scope.allservers = {};
	   //$scope.getServers();
	   
	   $scope.net = null;
	   
	   $scope.getAllServers(0);
	   $('#net-modal').modal('toggle');
   };
   
   $scope.getSelectedServers = function(netId) {
		$http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net/' + netId + '/selectedservers').success(function(data) {
		    $scope.selectedServers.selected = data;
		});
   };
   
   $scope.getAllServers = function(netId) {
		$http.get(GLOBAL_EVALUATIONCENTER_URL + '/edit/' + $scope.evaluationCenterId + '/net/' + netId + '/allservers').
	    	success(function(data) {
	    		$scope.allservers = data;  
	    		$scope.selectLabel = "Seleccione servidor (limitado 50 primeros resultados)...";
		    });
	};

   
   
   
    
}
