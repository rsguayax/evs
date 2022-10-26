<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Asignaturas</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="mattersCtrl"  ng-init="init();">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading &&  filterMatters" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" size="60"/>
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
		      			<div class="nav navbar-nav navbar-right">
			      			<a href="matter/new" class="btn btn-default navbar-btn" style="margin-right: 10px;">
		        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
		        			</a>
		        			<button type="button" class="btn btn-info navbar-btn" style="margin-right: 10px;" ng-click="assignBanks()">
		        				<span class="glyphicon glyphicon-list" aria-hidden="true"></span> Asignar bancos de preguntas
		        			</button>
		        			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="loadMatters()">
		        				<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Actualizar
		        			</button>
	        			</div>
      				</div>
      			</nav>
				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Nombre</th>
						<th>Código</th>
						<th></th>
					</tr>
					<tr ng-repeat="matter in filterMatters | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" ng-class="{'danger': !matter.hasBanks}">
						<td width="60%">{{matter.name}}</td>
						<td width="20%">{{matter.code}}</td>
						<td align="right">
							<a href='<s:url value="matter/edit/{{matter.id}}"/>'><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							<a href='javascript:void(0)' ng-click="confirmAction('¿Está seguro de eliminar?', '<s:url value="matter/delete/"/>', matter.id)">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
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
	            		total-items="filterMatters.length"
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
	    <script src="<c:url value="/web-resources/js/controller/matters-ctrl.js" />"></script>
	</content>
</body>
</html>