<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%@ page session="false"%>
<html>
<head>
<title>Titulaciones</title>
</head>
<body>
	<div class="page-header">
		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="degreesCtrl" ng-init="init();">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center">
					<img src="<c:url value="/web-resources/img/load-small.gif" />">
				</p>
			</div>
			<div class="row" ng-show="!loading &&  filterDegrees" ng-cloak>
				<nav class="navbar navbar-default">
					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
							<div class="form-inline has-feedback">
								<input type="text" class="form-control"
									placeholder="B&uacute;squeda" data-ng-model="search.name"
									size="60" /> <span
									class="glyphicon glyphicon-search form-control-feedback"
									aria-hidden="true"></span>
							</div>
						</form>
						<div class="nav navbar-nav navbar-right">
<!-- 							<a href="degree/new" class="btn btn-default navbar-btn" -->
<%-- 								style="margin-right: 10px;"> <span --%>
<%-- 								class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> --%>
<!-- 								A&ntilde;adir -->
<!-- 							</a> -->
							<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="loadDegrees()">
		        				<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Actualizar
		        			</button>
						</div>
					</div>
				</nav>


				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Nombre</th>
						<th>C&oacute;digo</th>
						<th>Nivel Acad&eacute;mico</th>
						<th>Modalidad</th>
						<th></th>
					</tr>
					<tr
						ng-repeat="degree in filterDegrees | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize"
						class="ng-scope">
						<td width="56%">{{degree.name}}</td>
						<td width="15%">{{degree.code}}</td>
						<td width="12%">{{degree.academicLevel}}</td>
						<td width="12%">{{degree.mode}}</td>
						<td width="5%" align="right">
<%-- 						<a href='<s:url value="degree/edit/{{degree.id}}"/>'><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a> --%>
 						<a href='#' title='Eliminar' ng-click="delete(degree.id)"><span class="glyphicon glyphicon-trash" aria-hidden="true" title="Eliminar titulaci&oacute;n"></span></a></td>
					</tr>
				</table>
				<nav aria-label="Paginado">
					Mostrando <select data-ng-model="pageSize"
						data-ng-options="option for option in [10, 25, 50, 100]"></select>
					&iacute;tems por p&aacute;gina
					<uib-pagination ng-model="currentPage" items-per-page="pageSize"
						max-size="10" total-items="filterDegrees.length"
						direction-links="true" boundary-links="true" first-text="&laquo"
						previous-text="&lsaquo;" next-text="&rsaquo;" last-text="&raquo;"
						class="pagination-sm pull-right" style="margin: 0;"></uib-pagination>
				</nav>
			</div>
		</div>
	</div>
	<content tag="local_script"> <script
		src="<c:url value="/web-resources/js/controller/degrees-ctrl.js" />"></script>
	</content>
</body>
</html>