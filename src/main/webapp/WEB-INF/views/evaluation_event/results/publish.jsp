<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
	<title>Publicación de resultados</title>
	<style type="text/css">
		.popover-title {
			font-size: 1.3em;
			font-width: bold;
			text-align: center;
		}

		*:focus, *:hover {
		    outline: none;
		    text-decoration: none;
		}

		a:-webkit-any-link {
			text-decoration: none;
		}

		#search-dropdown .ui-select-container.ui-select-bootstrap.dropdown.ng-valid.open  {
    		 width: 600px !important;
		}
	</style>
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
	<div class="row" ng-controller="evaluationEventResultsCtrl" ng-init="init(${evaluationEvent.id}, '${evaluationEvent.name}');">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div ng-show="!loading && tests.length">
		    	<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback" ng-cloak id="search-dropdown">
		          				<ui-select ng-show="!searchAllMattersSelection" ng-model="searchSelectedMatters.selected" theme="bootstrap" 
		          				ng-disabled="disabled"  on-select="changeMatter($item, $model)" >
								    <ui-select-match placeholder="Seleccione asignatura...." allow-clear>{{$select.selected.matter.name}}; {{$select.selected.academicPeriod.name}}; {{$item.mode.name}}</ui-select-match>
								    <ui-select-choices repeat="matter in allmatters | nestedPropsFilter: [{'obj': 'matter', 'prop': 'name', 'value': $select.search}, {'obj': 'academicPeriod', 'prop': 'name', 'value': $select.search}] | limitTo: 50">
								      <div ng-bind-html="matter.name | highlight: $select.search"></div>
								      <small>
								        Nombre: <span ng-bind-html="''+matter.matter.name| highlight: $select.search">; </span>
								        P. académico: <span ng-bind-html="''+matter.academicPeriod.name| highlight: $select.search">; </span>
										Modalidad: <span ng-bind-html="''+matter.mode.name| highlight: $select.search"></span>
								      </small>
								    </ui-select-choices>
								  </ui-select>
								 
		        			</div>
		      			</form>   		
		      			<div class="nav navbar-nav navbar-right">
		      				<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'results_manager')">
		      				<button class="btn btn-default" ng-click="qualify()">
		      					<span class="glyphicon glyphicon-check" aria-hidden="true"></span> Calificar
		      				</button>
		      				</sec:authorize>
			        		<button class="btn btn-default" ng-click="extraScoreApplied()">
			        			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Aplicar nota adic.
			        		</button>
			        		<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'results_manager')">
			        		<button class="btn btn-success" ng-click="enablePublish()">
			        			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Habilitar
			        		</button>
			        		<button class="btn btn-danger" ng-click="publish()">
			        			<span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span> Publicar
			        		</button>
			        		</sec:authorize>
			        		<button class="btn btn-info" ng-click="notify()">
			        			<span class="glyphicon glyphicon-bell" aria-hidden="true"></span> Notificar
			        		</button>
							<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'results_manager')">
			      			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="showResultsReport()">
		        				<span class="glyphicon glyphicon-download" aria-hidden="true"></span> Informe resultados
		        			</button>
		        			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="showEvaluationReport()">
		        				<span class="glyphicon glyphicon-download" aria-hidden="true"></span> Informe evaluaci&oacute;n
		        			</button>
		        			</sec:authorize>
	        			</div>
      				</div>
      			</nav>

				<table class="table table-striped" ng-cloak>
					<tr style="background-color: #bbb;">
						<th><input type="checkbox" name="selectedEemts[]" ng-checked="allEemtSelected" ng-click="selectAllEemt()" /></th>
						<th>C&oacute;digo</th>
						<th>Nombre</th>
						<th>Test</th>
						<th>N. Extra</th>
						<th>H. Publicaci&oacute;n</th>
						<th>Estado</th>
						<th></th>
					</tr>
			        <tr ng-repeat="eemt in filterTests | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
			        	<td><input type="checkbox" name="selectedEemts[]" ng-checked="eemt.checked" ng-click="toggleSelection(eemt)" value="{{eemt.id}}" /></td>
			        	<td width="15%">{{eemt.evaluationEventMatter.matter.code}}</td>
						<td width="40%">{{eemt.evaluationEventMatter.matter.name}}</td>
						<td width="15%">{{eemt.test.evaluationType.code}}</td>
						<td width="10%"><span ng-show="eemt.extraScoreApplied==1">Sí</span><span ng-show="eemt.extraScoreApplied==0">No</span></td>
						<td width="10%"><span ng-show="eemt.enablePublish==1">Sí</span><span ng-show="eemt.enablePublish==0">No</span></td>
						<td width="10%">{{eemt.state}}</td>
						<td>
							<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'results_manager')">
							<a href='#'
								bs-popover
								id="extra-score" type="button" data-placement="top"
								data-trigger="click" data-auto-close="true" ng-click="showExtraScore(eemt)"
								data-content-template="popover-extraScore">
								<span class="glyphicon glyphicon-plus-sign" aria-hidden="true" style="color: #c12e2a;" ng-if="eemt.extraScore==null || eemt.extraScore==0"></span>
								<span class="glyphicon glyphicon-plus-sign" aria-hidden="true" ng-if="eemt.extraScore!=null && eemt.extraScore!=0"></span>
							</a>
							</sec:authorize>
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
	            		total-items="filterTests.length"
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

		<!-- MODAL RESULTS REPORT -->
		<div class="modal" id="results-report-modal" tabindex="-1" role="dialog" aria-labelledby="report-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="report-modalLabel">Informe de resultados</h3>
		      </div>
		      <div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						 <ui-select multiple ng-model="selectedMatters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false" on-select="filterReportTests()" on-remove="filterReportTests()">
						    <ui-select-match placeholder="Seleccione asignatura....">{{$item.matter.name}}; {{$item.academicPeriod.name}}; {{$item.mode.name}}</ui-select-match>
						    <ui-select-choices repeat="matter in allmatters | nestedPropsFilter: [{'obj': 'matter', 'prop': 'name', 'value': $select.search}, {'obj': 'academicPeriod', 'prop': 'name', 'value': $select.search}] | limitTo: 50">
						      <div ng-bind-html="matter.name | highlight: $select.search"></div>
						      <small>
						        Nombre: <span ng-bind-html="''+matter.matter.name| highlight: $select.search">; </span>
						        P. académico: <span ng-bind-html="''+matter.academicPeriod.name| highlight: $select.search">; </span>
								Modalidad: <span ng-bind-html="''+matter.mode.name| highlight: $select.search"></span>
						      </small>
						    </ui-select-choices>
						  </ui-select>
						  <ui-select style="margin-top: 8px;" multiple ng-model="selectedEvaluationTypes.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false" on-select="filterReportTests()" on-remove="filterReportTests()">
						    <ui-select-match placeholder="Seleccione tipo de evaluación....">{{$item.name}}</ui-select-match>
						    <ui-select-choices repeat="evaluationType in evaluationTypes | filter: $select.search | limitTo: 50">
						      <div ng-bind-html="evaluationType.name | highlight: $select.search"></div>
						    </ui-select-choices>
						  </ui-select>
						  
						  <table class="table table-striped" ng-cloak style="margin-top: 12px;">
							<tr style="background-color: #bbb;">
								<th>C&oacute;digo</th>
								<th>Asignatura</th>
								<th>Test</th>
								<th>Estado</th>
							</tr>
					        <tr ng-repeat="eemt in reportTests | startPaginate: (currentPageReportTests - 1) * pageSizeReportTests | limitTo: pageSizeReportTests" class="ng-scope">
					        	<td width="25%">{{eemt.evaluationEventMatter.matter.code}}</td>
								<td width="45%">{{eemt.evaluationEventMatter.matter.name}}</td>
								<td width="15%">{{eemt.test.evaluationType.code}}</td>
								<td width="15%">{{eemt.state}}</td>
							</tr>
						</table>
						<nav aria-label="Paginado">
			            	<uib-pagination
			            		ng-model="currentPageReportTests"
			            		items-per-page="pageSizeReportTests"
			            		max-size="10"
			            		total-items="reportTests.length"
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
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		        <button type="button" class="btn btn-default" ng-click="generateResultsReport()">Generar</button>
		      </div>
		    </div>
		  </div>
	    </div>
	    
	    <!-- MODAL EVALUATION REPORT -->
		<div class="modal" id="evaluation-report-modal" tabindex="-1" role="dialog" aria-labelledby="report-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="report-modalLabel">Informe de evaluación</h3>
		      </div>
		      <div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						 <p>Evento completo: <input type="checkbox" name="allMattersSelection" ng-model="allMattersSelection" checked/></p>
						 <ui-select ng-show="!allMattersSelection" multiple ng-model="selectedMatters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true"
						  				close-on-select="false">
						    <ui-select-match placeholder="Seleccione asignatura....">{{$item.matter.name}}; {{$item.academicPeriod.name}}; {{$item.mode.name}}</ui-select-match>
						    <ui-select-choices repeat="matter in allmatters | nestedPropsFilter: [{'obj': 'matter', 'prop': 'name', 'value': $select.search}, {'obj': 'academicPeriod', 'prop': 'name', 'value': $select.search}] | limitTo: 50">
						      <div ng-bind-html="matter.name | highlight: $select.search"></div>
						      <small>
						        Nombre: <span ng-bind-html="''+matter.matter.name| highlight: $select.search">; </span>
						        P. académico: <span ng-bind-html="''+matter.academicPeriod.name| highlight: $select.search">; </span>
								Modalidad: <span ng-bind-html="''+matter.mode.name| highlight: $select.search"></span>
						      </small>
						    </ui-select-choices>
						  </ui-select>
					</div>
		        </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		        <button type="button" class="btn btn-default" ng-click="generateEvaluationReport()">Generar</button>
		      </div>
		    </div>
		  </div>
	    </div>

	</div>

	<content tag="local_script">
	    <script src="<c:url value="/web-resources/js/controller/evaluation-event-results-ctrl.js" />"></script>

	    <script type="text/ng-template" id="popover-extraScore">
		<div class="form-group" style="text-align: center" id="extraScore-id">
			<h4>Puntuación adicional</h4>
	         <input id="extrascore-val" placeholder="Puntación adicional" class="form-control" maxlength="5" ng-model="extraScore"
					style="display: inline; width: auto" type="number">
	         <a href='#' class="btn btn-default" ng-click="setExtraScore()" style="margin-top: 5px">Modificar<!--<span class="glyphicon glyphicon-pencil" aria-hidden="true" title="Modificar">--></span></a>
	    </div>
		</script>
	</content>
</body>
</html>