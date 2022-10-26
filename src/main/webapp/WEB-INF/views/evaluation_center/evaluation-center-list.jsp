<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
<title>Centros de evaluaci&oacute;n</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="evaluationCenterCtrl" ng-init="init();">
		<div class="col-md-12">

    		<sec:authorize access="@evsSecurityHandler.hasProfile('gestor_centros')">
			   <!--   Es gestor de centros -->
			</sec:authorize>

			<sec:authorize access="@evsSecurityHandler.hasMProfile('gestor_centros', 'gestor_cuentas')">
			     <!--  Es gestor de centros o de cuentas -->
			</sec:authorize>

    		<sec:authorize  access="@evsSecurityHandler.hasPermission('evaluation_center', 'read')">
    			 <!--  Tiene permiso de lectura en centros de evaluacion -->
    		</sec:authorize>

			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading && filterEvaluationCenters" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" />
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
		      			<div class="nav navbar-nav navbar-right">
			      			<a href="evaluationcenter/new" class="btn btn-default navbar-btn" style="margin-right: 10px;">
		        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
		        			</a>
	        			</div>
      				</div>
      			</nav>
				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Nombre</th>
						<th>C&oacute;digo</th>
						<th></th>
					</tr>
					<tr ng-repeat="evaluationCenter in filterEvaluationCenters | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" >
						<td width="60%">{{evaluationCenter.name}}</td>
						<td width="20%">{{evaluationCenter.code}}</td>
						<td align="right">
							<a href='<s:url value="evaluationcenter/edit/{{evaluationCenter.id}}"/>'><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
								<a href='javascript:void(0)' ng-click="confirmAction('¿Esta seguro de eliminar?', '<s:url value="evaluationcenter/delete/"/>', evaluationCenter.id)">
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
	            		total-items="filterEvaluationCenters.length"
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
		<script src="<c:url value="/web-resources/js/controller/evaluation-center-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>