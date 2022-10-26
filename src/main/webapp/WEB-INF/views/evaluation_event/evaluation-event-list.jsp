<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
	<title>Eventos de evaluaci&oacute;n</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	
	<div class="row" ng-app="app" ng-controller="evaluationEventCtrl" ng-init="init();">
		<div class="col-md-12">
			<!--
			<h3>${headText}</h3>
			<div class="row" ng-show="!loading && filterEvaluationEvents">
				<div class="left">
					<form class="form-inline has-feedback">
	       				<input type="text" ng-model="search.name" class="form-control" placeholder="Búsqueda"/>
	       				<i class="glyphicon glyphicon-search form-control-feedback"></i>
	       			</form>
	       			<br>
	    		</div>
    		</div>
    		-->

			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading && filterEvaluationEvents" ng-cloak>
				<!--  -->
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" />
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
		      			<div class="nav navbar-nav navbar-right">
			      			<a href="evaluationevent/new" class="btn btn-default navbar-btn" style="margin-right: 10px;">
		        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
		        			</a>
	        			</div>
      				</div>
      			</nav>
				<!--  -->
				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>C&oacute;digo</th>
						<th>Nombre</th>
						<th>Fecha de inicio</th>
						<th>Fecha de fin</th>
						<th>Observaci&oacute;n</th>
						<th></th>
					</tr>
					<tr ng-repeat="evaluationEvent in filterEvaluationEvents | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" init-popover>
						<td width="15%">{{evaluationEvent.code}}</td>
						<td width="15%">{{evaluationEvent.name}}</td>
						<td width="15%">{{evaluationEvent.startdate | date:'yyyy-MM-dd HH:mm:ss'}}</td>
						<td width="15%">{{evaluationEvent.enddate | date:'yyyy-MM-dd HH:mm:ss'}}</td>
						<td width="25%">{{evaluationEvent.comment}}</td>
						<td width="15%" align="right">
							<a title="M&aacute;s detalles"
								role="button"
								data-placement="bottom"
								data-html="true"
								data-trigger="hover"
								data-content='<div>N&ordm; estudiantes: {{evaluationEvent.studentCount}} <br/> N&ordm; notificaciones: {{evaluationEvent.notificationCount}} <br/> N&ordm; notificaciones le&iacute;das: {{evaluationEvent.notificationReadCount}}</div>'
								rel="popover"><span class="glyphicon glyphicon-option-horizontal" aria-hidden="true"></span></a>
							<a title="Notificaciones" href='<s:url value="email-notification-list?evaluationEventId={{evaluationEvent.id}}"/>'><span class="glyphicon glyphicon-bell" aria-hidden="true"></span></a>
							<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'results_manager')">
							<a title="Publicaci&oacute;n de resultados" href='<s:url value="evaluationevent/results/{{evaluationEvent.id}}/publish"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span></a>
							</sec:authorize>
							<a title="Gesti&oacute;n de estudiantes y aulas" href="<s:url value="/evaluationevent/{{evaluationEvent.id}}/admin"/>"><span class="glyphicon glyphicon-education" aria-hidden="true"></span></a>
							<a title="Documentaci&oacute;n y listados" href="<s:url value="/evaluationevent/{{evaluationEvent.id}}/student-listings"/>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span></a>
							<a title="Editar" href='<s:url value="evaluationevent/edit/{{evaluationEvent.id}}"/>'><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							<a ng-if="evaluationEvent.isAdmissionOrComplexiveType" title="Revisión de Inscripciones" href='<s:url value="evaluationevent/edit/{{evaluationEvent.id}}/enrollmentrevision"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
							<!--  <a title="Eliminar" href='<s:url value="evaluationevent/delete/{{evaluationEvent.id}}"/>' class="message-link"
								data-message="&iquest;Est&aacute; seguro de eliminar?"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a> -->
							<a title="Eliminar" href='#' ng-click="deleteEvaluationEvent(evaluationEvent)" class="message-link"
								data-message="&iquest;Est&aacute; seguro de eliminar?"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								
						</td>
					</tr>
				</table>
		        <nav aria-label="Paginado">
		        	Mostrando
					<select	data-ng-model="pageSize" data-ng-options="option for option in [10, 25, 50, 100]"></select>
					&iacute;tems por p&aacute;gina
	            	<uib-pagination
	            		ng-model="currentPage"
	            		items-per-page="pageSize"
	            		max-size="10"
	            		total-items="filterEvaluationEvents.length"
	            		direction-links="true"
	            		boundary-links="true"
	            		first-text="&laquo"
	            		previous-text="&lsaquo;"
	            		next-text="&rsaquo;"
	            		last-text="&raquo;"
            			class="pagination-sm pull-right"
            			style="margin: 0;"></uib-pagination>
            	</nav>
			</div>

		</div>
	</div>
	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/evaluation-event-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>