app.controller('UploaderController', ['$scope', 'FileUploader', UploaderController]);

    
function UploaderController($scope, FileUploader) {
	
        var uploader = $scope.uploader = new FileUploader({
        	 url: GLOBAL_EVALUATIONEVENT_URL + '/' + $scope.evaluationEventId + '/uploadschedules'
        });
        
        FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
            return true; // true|false
        };
        
        // FILTERS
      
        // a sync filter
        uploader.filters.push({
            name: 'syncFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return uploader.queue.length !== 1; // only one file in the queue
                //return this.queue.length < 10;
            }
        });
      
        // an async filter
        uploader.filters.push({
            name: 'asyncFilter',
            fn: function(item /*{File|FileLikeObject}*/, options, deferred) {
                setTimeout(deferred.resolve, 1e3);
            }
        });
        
        uploader.filters.push({
            name: 'fileFilter',
            fn: function(item, options) {
                var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                var allow = '|csv|txt|vnd.ms-excel|plain|'.indexOf(type) !== -1;
                return allow;
            }
        });
        
        uploader.filters.push({
        	name: 'queueLimit',
            fn: function(item, options) {
            	var allow = uploader.queue.length !== 1; // only one file in the queue
            	return allow;
            }
        });

        // CALLBACKS
        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function(fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function(addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function(item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function(fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function(progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
            console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
        };
        uploader.onCompleteAll = function() {
            console.info('onCompleteAll');
        };

        console.info('uploader', uploader);
        
    };
