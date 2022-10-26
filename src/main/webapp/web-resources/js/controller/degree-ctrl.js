
app.controller('degreeCtrl', ['$scope', '$http', '$timeout', '$interval', 'config', 'flashService', degreeCtrl]);

function degreeCtrl($scope, $http, $timeout, $interval, config, flashService) {
	$scope.init = function(degreeId) {
		$scope.degreeId = degreeId;
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
	};
}
