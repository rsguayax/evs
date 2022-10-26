var app = angular.module('integratorApp', ['mgcrea.ngStrap', 'ngMessages', 'ngFlash', 'ngSanitize', 'ui.select', 
                                           'ui.calendar', 'ui.bootstrap', 'googlechart', 'angularFileUpload']);

app.constant('config', {
});

app.service('flashService', ['Flash', function(Flash, $animate) {
	 this.flash = function(message, type) {
		 var id = Flash.create(type, message, 5000);
	 }
}]);

app.filter('propsFilter', function() {
	  return function(items, props) {
	    var out = [];

	    if (angular.isArray(items)) {
	      var keys = Object.keys(props);
	        
	      items.forEach(function(item) {
	        var itemMatches = false;

	        for (var i = 0; i < keys.length; i++) {
	          var prop = keys[i];
	          var text = props[prop].toLowerCase();
	          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
	            itemMatches = true;
	            break;
	          }
	        }

	        if (itemMatches) {
	          out.push(item);
	        }
	      });
	    } else {
	      // Let the output be the input untouched
	      out = items;
	    }

	    return out;
	  };
});

app.filter('startPaginate', function () {
    return function (input, start) {
        if (!input || !input.length) { return; }

        start = +start;
        return input.slice(start);
    };
});


app.filter('nestedPropsFilter', function() {
	  return function(items, props) {
	    var out = [];

	    if (angular.isArray(items)) {
	        items.forEach(function(item) {
	        var itemMatches = false;

	        for (var i = 0; i < props.length; i++) {
	          var text =  props[i]['value'].toLowerCase();
	          if (item[props[i]['obj']][props[i]['prop']].toString().toLowerCase().indexOf(text) !== -1) {
	            itemMatches = true;
	            break;
	          }
	        }

	        if (itemMatches) {
	          out.push(item);
	        }
	      });
	    } else {
	      // Let the output be the input untouched
	      out = items;
	    }

	    return out;
	  };
});


app.service('mainService', ['$http', '$q', function($http, $q) {
	 var self = this;
	 self.evaluationTypes = [];
//	 self.testTypes = [];
	
	 self.loadEvaluationTypes = function() {
		var deferred = $q.defer(); 
		 
		if(self.evaluationTypes == null || self.evaluationTypes.length == 0) {
			$http.get(GLOBAL_AUX_URL + '/loadevaluationtypes').
		    	success(function(data) {
		    		deferred.resolve(data);
			    });
		} else {
			deferred.resolve(self.evaluationTypes);
		}
		
		return deferred.promise;
	 };
	 
//	 self.loadTestTypes = function() {
//			var deferred = $q.defer(); 
//			 
//			if(self.testTypes == null || self.testTypes.length == 0) {
//				$http.get(GLOBAL_AUX_URL + '/loadtesttypes').
//			    	success(function(data) {
//			    		deferred.resolve(data);
//				    });
//			} else {
//				deferred.resolve(self.testTypes);
//			}
//			
//			return deferred.promise;
//		 };
}]);


app.filter('isEmpty', [function() {
	return function(object) {
		return angular.equals({}, object);
	}
}])

app.filter('newlines', function () {
    return function(text) {
    	if(text)
        	return text.replace(/\n/g, '<br/>');
        return '';
    }
})

 app.run(function($rootScope) {
        $rootScope.confirmAction = function(message, link, id) {
    		alertify.confirm(message, function (e) {
    			if (e) {
    				window.location = link + id;
    			}
    		});
        };
    });



