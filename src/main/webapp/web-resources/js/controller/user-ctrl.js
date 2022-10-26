app.controller('userCtrl', ['$scope', '$window', '$http', '$compile', '$filter', userCtrl] );

function userCtrl($scope, $window, $http, $compile, $filter) {
	
	$scope.init = function() {
		$scope.getUsers();
		$scope.loading = true;
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.roles = [{name: "Administrador", code: "ADMIN", id: 1}];
//		$scope.roles = [{name: "Administrador", code: "ADMIN", id: 1}, {name: "Personal UTPL", code: "STAFF", id: 4}];
	};
	
	$scope.showAddUser = function() {
		$scope.userData = {};
		$scope.selectedRol = {};
		$scope.password = "";
		$scope.passwordConfirmation = "";
		$('#user-modal').modal('toggle');
	};
	
	$scope.showEditUser = function(user) {
		$scope.selectedUser = user;
		$scope.userData = angular.copy(user);
		$scope.selectedRol = $scope.userData.roles[0];
		$('#user-modal').modal('toggle');
	};
	
	$scope.showEditPassword = function(user) {
		$scope.selectedUser = user;
		$scope.password = "";
		$scope.passwordConfirmation = "";
		$('#user-modal').modal('hide');
		$('#edit-password-modal').modal('toggle');
	};
	
	$scope.addUser = function() {
		if ($scope.password.length > 0) {
			if ($scope.passwordConfirmation.length > 0) {
				if ($scope.password.length >= 6) {
					if ($scope.password == $scope.passwordConfirmation) {
						var url = GLOBAL_USER_URL + '/add';
						$scope.userData.password = $scope.password;

						$http.post(url, $scope.userData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
							if (responseMessage.type == 'success') {
								$scope.getUsers();
								$('#user-modal').modal('hide');
							}
							
							alertify.alert(responseMessage.message);
					    });
					} else {
						alertify.alert("La contraseña no coincide con la confirmación");
					} 
				} else {
					alertify.alert("La contraseña debe tener al menos 6 caracteres");
				} 
			} else {
				alertify.alert("Introduzca la confirmación de la contraseña");
			}
		} else {
			alertify.alert("Introduzca una contraseña");
		}
	};
	
	$scope.editUser = function() {
		var url = GLOBAL_USER_URL + '/edit';

		$http.post(url, $scope.userData, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
			if (responseMessage.type == 'success') {
				$scope.getUsers();
				$('#user-modal').modal('hide');
			}
			
			alertify.alert(responseMessage.message);
	    });
	};
	
	$scope.editPassword = function() {
		if ($scope.password.length > 0) {
			if ($scope.passwordConfirmation.length > 0) {
				if ($scope.password.length >= 6) {
					if ($scope.password == $scope.passwordConfirmation) {
						var url = GLOBAL_USER_URL + '/edit-password';
						var data = {'userId': $scope.selectedUser.id, 'password': $scope.password};

						$http.post(url, data, {headers: {'Content-Type': 'application/json; charset=UTF-8'}}).success(function(responseMessage) {
							if (responseMessage.type == 'success') {
								$scope.getUsers();
								$('#edit-password-modal').modal('hide');
							}
							
							alertify.alert(responseMessage.message);
					    });
					} else {
						alertify.alert("La contraseña no coincide con la confirmación");
					} 
				} else {
					alertify.alert("La contraseña debe tener al menos 6 caracteres");
				} 
			} else {
				alertify.alert("Introduzca la confirmación de la contraseña");
			}
		} else {
			alertify.alert("Introduzca una contraseña");
		}
	};
	
	$scope.getUsers = function() {
		$http.get(GLOBAL_USER_URL + '/users').
	    	success(function(data) {
	    		$scope.users = data;
	    		$scope.filterUsers = data;	 
		    }).finally(function() {
		    	$scope.loading = false;
		    });
	};
	
	$scope.disableUser = function(user) {
		alertify.confirm("&iquest;Est&aacute; seguro que desea deshabilitar el usuario?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_USER_URL + '/' + user.id + '/disable').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
						$scope.getUsers();
					}
					
					alertify.alert(responseMessage.message);
			    })
		    }
	   });
	};
	
	$scope.enableUser = function(user) {
		alertify.confirm("&iquest;Est&aacute; seguro que desea habilitar el usuario?", function (e) {
		    if (e) {
		    	$http.get(GLOBAL_USER_URL + '/' + user.id + '/enable').success(function(responseMessage) {
		    		if (responseMessage.type == 'success') {
						$scope.getUsers();
					}
					
					alertify.alert(responseMessage.message);
			    })
		    }
	   });
	};
	
	$scope.selectRol = function(selectedRol) {
		var roles= [];
		if (selectedRol != null) {
			roles.push(selectedRol);
		}
		$scope.userData.roles = roles;
	};
	
	$scope.$watch('search.name', function (term) {
        $scope.filterUsers = $filter('filter')($scope.users, term);
        
        $scope.currentPage = 1;
    });
}