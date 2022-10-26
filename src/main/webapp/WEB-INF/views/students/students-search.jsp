<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
	<title>Estudiantes</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-app="app" ng-controller="studentsSearchCtrl" ng-init="init();" ng-cloak>
		<div class="col-md-12">
			<!--
			<table style="width: 50%;">
  				<tr>
	  				<td style="width: 100%">
				  		<input class="form-control" type="text" name="studentsSearch" placeholder="Buscar estudiante... (Cédula, nombre o apellidos)" ng-model="studentsSearchText"/>
			  		</td>
		  			<td style="padding-left: 8px;">
		  				<button type="button" class="btn btn-default" ng-click="searchStudents()"><span class="glyphicon glyphicon-search"></span> Buscar</button>
	  				</td>
  				</tr>
  			</table>
  			-->

  			<!--  -->
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<form class="navbar-form navbar-left" role="search" ng-submit="searchStudents()">
	        			<div class="form-inline has-feedback">
	          				<input type="text" class="form-control"
	          					placeholder="Busque estudiantes por c&eacute;dula, username, nombre o apellidos..."
	          					name="studentsSearch"
	          					data-ng-model="studentsSearchText"
	          					size="60" />
	          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
	        			</div>
	        			<small id="searchHelp" class="text-muted">Introduzca la cadena de b&uacute;queda y pulse "Intro"</small>
	      			</form>
    			</div>
    		</nav>
			<!--  -->

 			<div ng-show="studentsSearchResults.length > 0 && !loadingStudentsSearchResults">
  				<table class="table table-striped">
		        	<tr style="background-color: #bbb;">
		        		<th>Nombre</th>
		        		<th>Apellidos</th>
		        		<th>Username</th>
		        		<th>C&eacute;dula</th>
		        		<th></th>
		        	</tr>
		        	<tr ng-repeat="studentSearchResult in studentsSearchResults | orderBy: 'lastName' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-cloak">
						<td width="25%">{{studentSearchResult.firstName}}</td>
						<td width="35%">{{studentSearchResult.lastName}}</td>
						<td width="20%">{{studentSearchResult.username}}</td>
						<td width="15%">{{studentSearchResult.identification || '---'}}</td>
						<td width="5%" class="text-right">
							<a title="Horarios de ex&aacute;menes" href="<c:url value="/students/{{studentSearchResult.id}}/schedules" />" target="_blank">
								<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
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
	            		total-items="studentsSearchResults.length"
	            		direction-links="true"
	            		boundary-links="true"
	            		first-text="&laquo"
	            		previous-text="&lsaquo;"
	            		next-text="&rsaquo;"
	            		last-text="&raquo;"
            			class="pagination-sm pull-right"
            			style="margin: 0;"></uib-pagination>
            	</nav>
            	<!--
		        <div class="text-center">
		        	<uib-pagination ng-show="studentsSearchResults.length > pageSize" class="pagination-sm" total-items="studentsSearchResults.length" direction-links="true" boundary-links="true"
		        		previous-text="&lsaquo;" next-text="&rsaquo;"
            			first-text="&lsaquo;&lsaquo;" last-text="&rsaquo;&rsaquo;"
            			items-per-page="pageSize" ng-model="currentPage" max-size="10"></uib-pagination>
		        </div>
		        -->
	        </div>
	        <div ng-show="studentsSearchResults.length == 0 && !loadingStudentsSearchResults">
	        	<h4>No se han encontrado estudiantes</h4>
	        </div>
	        <div class="loading-small" ng-show="loadingStudentsSearchResults">
	        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
	        </div>

		</div>
	</div>

	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/students-search-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>