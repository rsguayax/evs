app.controller('alertCtrl', ['$scope', 'flashService', '$animate', alertCtrl]);

function alertCtrl($scope, flashService, $animate) {
	
	$scope.init = function(successMessage, warningMessage, errorMessage, infoMessage) {
		if(!angular.isUndefined(successMessage) && successMessage) {
			flashService.flash(successMessage, 'success');
		}
		if(!angular.isUndefined(warningMessage) && warningMessage) {
			flashService.flash(warningMessage, 'warning');
		}
		if(!angular.isUndefined(errorMessage) && errorMessage) {
			flashService.flash(errorMessage, 'danger');
		}
		if(!angular.isUndefined(infoMessage) && infoMessage) {
			flashService.flash(infoMessage, 'info');
		}
	};
}
