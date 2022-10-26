app.controller('evaluationEventCtrl', ['$scope', '$http', 'filterFilter', 'config', 'flashService', evaluationEventCtrl]);

function evaluationEventCtrl($scope, $http, filterFilter, config, flashService) {

	$scope.init = function() {
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.loading = true;
		$scope.getEvaluationEvents();

		$("[data-toggle='popover']").popover();
	};

	$scope.getEvaluationEvents = function() {
		$http.get(GLOBAL_EVALUATIONEVENT_URL + '/evaluationevents').
			success(function(data) {
				var rData = $scope.toEvaluationEvents(data);
				$scope.evaluationEvents = rData;
				$scope.filterEvaluationEvents = rData;
			}).finally(function() {
				$scope.loading = false;
			});
	};

	$scope.deleteEvaluationEvent = function(evaluationEvent) {
		alertify.confirm("Se eliminar&aacute; el evento de evaluaci&oacute;n, &iquest;est&aacute; seguro de continuar?", function(e) {
			if (e) {
				$http.get(GLOBAL_EVALUATIONEVENT_URL + '/' + evaluationEvent.id + '/delete').
					success(function(data) {
						flashService.flash(data.message, data.type);
						$scope.getEvaluationEvents();
					});
			}
		});

	};

	$scope.$watch('search.name', function(term) {
		var obj = { name: term };

		$scope.filterEvaluationEvents = filterFilter($scope.evaluationEvents, obj);

		$scope.currentPage = 1;
	});

	$scope.toEvaluationEvents = function(arr) {

		if (!arr) {
			return [];
		}
		return arr.map(function(obj) {
			var rObj = {};
			rObj.id = obj[0];
			rObj.code = obj[1];
			rObj.name = obj[2];
			rObj.startdate = obj[3];
			rObj.enddate = obj[4];
			rObj.comment = obj[5];
			rObj.studentCount = obj[6];
			rObj.isAdmissionOrComplexiveType = (obj[7] == 1 || obj[7] == 2 || obj[7] == 3) ? true : false;
			rObj.notificationCount = obj[8];
			rObj.notificationReadCount = obj[9];
			return rObj;
		});
	};
}

app.directive('initPopover', function() {
	return function(scope, element, attrs) {
		element.find('a[rel="popover"]').popover();
	};
});
