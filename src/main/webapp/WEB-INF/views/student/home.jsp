<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Eventos de evaluación</title>
</head>
<body>
	<div ng-controller="homeCtrl" ng-init="init();">
		<h3>Eventos de evaluación</h3>
		<div ng-cloak>
			<div class="list-clickable" ng-show="evaluationAssignments.length > 0">
				<a ng-repeat="evaluationAssignment in evaluationAssignments" class="ng-cloak" href="<c:url value="/student/evaluationevent/{{evaluationAssignment.evaluationEventId}}" />">{{evaluationAssignment.evaluationEventName}}</a>
			</div>
	        <div ng-show="evaluationAssignments.length == 0">
	        	<h4>No estas asignado a ningún evento de evaluación</h4>
	        </div>
	        <div class="loading-small" ng-show="evaluationAssignments == null">
	        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
	        </div>
        </div>
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/student/home-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>