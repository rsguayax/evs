app.controller('emailNotificationsCtrl', ['$scope', '$http', '$timeout', '$interval', 'filterFilter', 'config', 'flashService', emailNotificationsCtrl]);

function emailNotificationsCtrl($scope, $http, $timeout, $interval, filterFilter, config, flashService) {

    $scope.init = function(evaluationEventId, userId) {
	$scope.evaluationEventId = evaluationEventId;
	$scope.userId = userId;

	$scope.loading = true;
	$scope.getEmailNotifications();

	$scope.currentPage = 1;
	$scope.pageSize = 10;
    };

    $scope.getEmailNotifications = function() {
	$http.get(GLOBAL_EMAIL_NOTIFICATION_URL + '/email-notifications',
		{params: {evaluationEventId: $scope.evaluationEventId, userId: $scope.userId}}).success(function(data) {
	    $scope.emailNotifications = data;
	    $scope.filterEmailNotifications = data;
	}).finally(function() {
	    $scope.loading = false;
	});
    };

    $scope.$watch('search.name', function(term) {
	$scope.filterEmailNotifications = filterFilter($scope.emailNotifications, {user: {fullName: term}});
	$scope.currentPage = 1;
    });

}