<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Bancos de preguntas</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="bankCtrl" ng-init="init();">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading && filterBanks" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" size="60"/>
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
		      			<div class="nav navbar-nav navbar-right">
		      				<button ng-if="showAll" type="button" class="btn btn-success navbar-btn" style="margin-right: 10px;" data-ng-click="showActiveBanks()">
		        				<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Mostrar activos
		        			</button>
		      				<button ng-if="!showAll" type="button" class="btn btn-info navbar-btn" style="margin-right: 10px;" data-ng-click="showAllBanks()">
		        				<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Mostrar todos
		        			</button>
			      			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" data-ng-click="loadExternalBanks()">
		        				<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Actualizar
		        			</button>
	        			</div>
      				</div>
      			</nav>
		        <table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Id</th>
						<th>Nombre</th>
						<th>N. Preguntas</th>
						<th>Estado</th>
						<th>Id externo</th>
						<th>Tipo evento evaluación</th>
						<th></th>
					</tr>
		        	<tr ng-repeat="bank in filterBanks  | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" ng-class="{'danger': bank.hasActiveTestWithoutEvaluationType}">
						<td width="5%">{{bank.id}}</td>
						<td width="30%">{{bank.name}}</td>
						<td width="10%">{{bank.questionNumber}}</td>
						<td width="20%">{{bank.state}}</td>
						<td width="5%">{{bank.externalId}}</td>
						<td width="10%">{{bank.evaluationEventType.name}}</td>
						<td width="10%" align="right">
							<a href='<s:url value="bank/edit/{{bank.id}}"/>' title="Ver banco"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
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
	            		total-items="filterBanks.length"
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
	    <script src="<c:url value="/web-resources/js/controller/bank-ctrl.js" />"></script>
	</content>
</body>
</html>