<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Tipos de eventos de evaluación</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="evaluationEventTypesCtrl"  ng-init="init();">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading &&  filterEvaluationEventTypes" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" size="60"/>
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>
		      			<div class="nav navbar-nav navbar-right">
<!-- 		      				<button type="button" class="btn btn-default navbar-btn" ng-click="showAddEvaluationEventType()"> -->
<%-- 		        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir tipo de evento de evaluación --%>
<!-- 		        			</button> -->
	        			</div>
      				</div>
      			</nav>
				<table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Id</th>
						<th>Nombre</th>
						<th>Descripción</th>
						<th>Activo</th>
						<th></th>
					</tr>
					<tr ng-repeat="evaluationEventType in filterEvaluationEventTypes | orderBy: 'id' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
						<td width="10%">{{evaluationEventType.id}}</td>
						<td width="30%">{{evaluationEventType.name}}</td>
						<td width="35%">{{evaluationEventType.description}}</td>
						<td width="15%">
							<span ng-if="evaluationEventType.active">Si</span>
							<span ng-if="!evaluationEventType.active">No</span>
						</td>
						<td align="right">
							<a href='#' ng-click="showEditEvaluationEventType(evaluationEventType)" ng-if="evaluationEventType.active" title="Editar"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							<a href='#' ng-click="deleteEvaluationEventType(evaluationEventType)" ng-if="evaluationEventType.active" title="Deshabilitar"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
							<a href='#' ng-click="enableEvaluationEventType(evaluationEventType)" ng-if="!evaluationEventType.active" title="Habilitar"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></a>
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
	            		total-items="filterEvaluationEventTypes.length"
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
		
		<!-- MODAL TIPO EVENTO EVALUACION -->
		<div class="modal" id="evaluation-event-type-modal" tabindex="-1" role="dialog" aria-labelledby="evaluation-event-type-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="evaluation-event-type-modalLabel">Tipo de evento de evaluación</h3>
		      </div>
		      <div class="modal-body">
	      		<form>
	      			<div class="form-group">
						<label path="name">Nombre</label>
						<input id="name" class="form-control" type="text" ng-model="evaluationEventTypeData.name" />
					</div>
					<div class="form-group">
						<label path="description">Descripción</label>
						<input id="description" class="form-control" type="text" ng-model="evaluationEventTypeData.description" />
					</div>
	      		</form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		        <button type="button" class="btn btn-default" ng-click="addEvaluationEventType()" ng-show="!evaluationEventTypeData.id">A&ntilde;adir</button>
		        <button type="button" class="btn btn-default" ng-click="editEvaluationEventType()" ng-show="evaluationEventTypeData.id">Editar</button>
		      </div>
		    </div>
		  </div>
	    </div>
	</div>
	<content tag="local_script">
	    <script src="<c:url value="/web-resources/js/controller/evaluation-event-types-ctrl.js" />"></script>
	</content>
</body>
</html>