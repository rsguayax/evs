app.controller('studentsSearchCtrl', ['$scope', '$window', '$http', '$compile', studentsSearchCtrl] );

function studentsSearchCtrl($scope, $window, $http, $compile) {
	
	$scope.init = function() {
		$scope.currentPage = 1;
		$scope.pageSize = 10;
	};
	
	$scope.searchStudents = function() {
		var searchText = $.trim($scope.studentsSearchText);
		
		if (searchText.length > 0) {
			$scope.loadingStudentsSearchResults = true;
			var url = GLOBAL_STUDENTS_URL + '/search'
			var data = $.param({searchText: searchText});
			
			$http.post(url, data, {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}}).success(function(data) {
				$scope.studentsSearchResults = data;
				$scope.loadingStudentsSearchResults = false;
				$scope.currentPage = 1;
		    });
		} else {
			alertify.alert('Introduzca algún término de búsqueda');
		}
	};
}