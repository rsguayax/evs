<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Evento de evaluaci&oacute;n</title>
	<style type="text/css">
		.popover-title {
			font-size: 1.3em;
			font-width: bold;
			text-align: center;
		}

		*:focus,
		*:hover {
		    outline: none;
		    text-decoration: none;
		}

		a:-webkit-any-link {
			text-decoration: none;
		}

		.progress {
		  height: 3em;
		}

		.progress i {
		  line-height: 3.5em;
		}

		.progress-bar {
		  -webkit-transition: width 1s ease-in-out;
		  transition: width 1s ease-in-out;
		}
	</style>
	<script type="text/javascript">
		$(function() {
			$('li.tab-list').removeClass('active');
			$('#tabMattersTests').addClass('active');
		});
	</script>
</head>
<body>
	<div ng-controller="evaluationEventMatterTestCtrl" ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
	 		<h3>Temáticas del evento de evaluaci&oacute;n ${evaluationEvent.name}
	 			<small>
			  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
			  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
			  		</a>
	  			</small>
	 		</h3>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div id="tabs">
					<c:import url="include/uptab.jsp" />
					<div class="tab-content container">
						<div class="tab-pane active" id="tabMatters">
							<div class="row" style="margin-top: 50px;" ng-show="loading">
								<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
							</div>
						    <div class="row" ng-show="!loading" ng-cloak>
						    	<nav class="navbar navbar-default">
				  					<div class="container-fluid">
										<form class="navbar-form navbar-left" role="search">
						        			<div class="form-inline has-feedback">
						          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" />
						          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
						        			</div>
						      			</form>
				      				</div>
				      			</nav>
						        <table class="table table-striped">
									<tr style="background-color: #bbb;">
										<th>Nombre</th>
										<th>Test</th>
										<th></th>
									</tr>
						        	<tr ng-repeat="matter in filterMatters | orderBy: 'matterName' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" ng-class="{'danger': !matter.hasBank}">
										<td width="50%">{{matter.matterName}}</td>
										<td width="40%">{{matter.test.test.name}}</td>
										<td width="10%" align="right">
											<a href='#' title='Seleccionar test' ng-click="showEditTest(matter)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
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
				</div>
			</div>
		</div>
		
		<!-- MODAL TESTS -->
		<div class="modal" id="tests-modal" tabindex="-1" role="dialog" aria-labelledby="add-matters-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="tests-modalLabel">Tests de la temática {{matterSelected.matter.name}}</h3>
		      </div>
		      <div class="modal-body">
		      	<div class="row" style="margin-top: 20px" ng-show="loadingTests"><p align="center"><img src="<c:url value="/web-resources/img/load.gif" />"></p></div>
		        <div class="row"  ng-show="!loadingTests && tests.length" ng-cloak>
		        	<div class="col-md-12">
						<table class="table table-striped">
							<tr style="background-color: #BBBBBB"><th></th><th>Nombre</th><th>Tipo de evaluación</th></tr>
				        	<tr ng-repeat="test in tests | orderBy:'name'" class="ng-scope" >
								<td><input class="test-checkbox" type="checkbox" ng-value="test.id" ng-checked="test.id == matterSelected.test.test.id" ng-click="selectTest(test)"></td>
								<td width="70%">{{test.name}}</td>
								<td width="30%">{{test.evaluationType.name}}</td>
							</tr>
				        </table>
			        </div>
			     </div>
			     <div class="row" style="margin-top: 20px" ng-show="!loadingTests && tests.length == 0" ng-cloak>
			     	<p align="center">-- Sin resultados --</p>
			     </div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-default" ng-click="modifyTest()" ng-show="testSelected.id != matterSelected.test.test.id">Seleccionar test</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		      </div>
		    </div>
		  </div>
	    </div>
	</div>
	
	<content tag="local_script">
    	<script src="<c:url value="/web-resources/js/controller/evaluation-event-matter-test-ctrl.js" />"></script>
	</content>
</body>
</html>