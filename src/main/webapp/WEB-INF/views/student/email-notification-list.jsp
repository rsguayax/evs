<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Notificaciones por e-mail de los horarios de evaluaci&oacute;n</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}
  			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
	  		</small>
  		</h3>
	</div>
	<div class="row" ng-controller="emailNotificationsCtrl" ng-init="init(${evaluationEventId}, ${userId});">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading && filterEmailNotifications" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" size="60"/>
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
      				</div>
      			</nav>
				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Evento de evaluaci&oacute;n</th>
						<th>Estudiante</th>
						<th>Enviado</th>
						<th>Le&iacute;do</th>
						<th></th>
					</tr>
					<tr ng-repeat="emailNotification in filterEmailNotifications | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
						<td>{{emailNotification.evaluationEvent.name}} ({{emailNotification.evaluationEvent.code}})</td>
						<td>{{emailNotification.user.fullName}}</td>
						<td>{{emailNotification.sent | date:'yyyy-MM-dd HH:mm:ss'}}</td>
						<td>{{emailNotification.read | date:'yyyy-MM-dd HH:mm:ss'}}</td>
						<td align="right">
							<a title="Ver detalle" href='<s:url value="email-callback?token={{emailNotification.token}}"/>' target="_blank">
								<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
							</a>
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
	            		total-items="filterEmailNotifications.length"
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
	    <script src="<c:url value="/web-resources/js/controller/student/email-notifications-ctrl.js" />"></script>
	</content>
</body>
</html>