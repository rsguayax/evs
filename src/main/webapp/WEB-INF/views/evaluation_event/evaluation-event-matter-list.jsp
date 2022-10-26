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
			$('#tabMatters').addClass('active');
		});
	</script>
</head>
<body>
	<div ng-controller="evaluationEventMattersCtrl" ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
	 		<h3>Asignaturas del evento de evaluaci&oacute;n ${evaluationEvent.name}
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
							<!--
							<div style="height: 30px;">
								<div class="left">
									<form class="form-inline has-feedback"">
				        				<input type="text" ng-model="search.name" class="form-control" placeholder="Búsqueda"/>
				        				<i class="glyphicon glyphicon-search form-control-feedback"></i>
				        			</form>
			        			</div>
								<div class="right">
									<a href="<c:url value="/evaluationevent"/>/edit/{{evaluationEventId}}/matters/bankreport" target="_blank" class="btn btn-default"><span class="glyphicon glyphicon-list-alt"></span> Asignatura/Banco</a>
									<button class="btn btn-danger" ng-click="removeMatters()"><span class="glyphicon glyphicon-trash"></span> Eliminar asignaturas</button>
									<button class="btn btn-success" ng-click="showAddMatter()"><span class="glyphicon glyphicon-plus"></span> Añadir asignatura</button>
									<button class="btn btn-default" ng-click="showLoadMatters()"><span class="glyphicon glyphicon-cloud-download"></span> Cargar asignaturas</button>
								</div>
							</div>
							-->
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
						      			<div class="nav navbar-nav navbar-right">
											<a class="btn btn-default" href="<c:url value="/evaluationevent"/>/edit/{{evaluationEventId}}/matters/listado-asignaturas-banco-{{evaluationEventId}}-{{currentDate | date:'ddMMyyyyHHmmss'}}.pdf" target="_blank">
												<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> Asignatura / Banco
											</a>
						        			<button type="button" class="btn btn-default navbar-btn" ng-click="showAddMatter()">
						        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir asignatura
						        			</button>
						        			<button type="button" class="btn btn-danger navbar-btn" ng-click="removeMatters()">
						        				<span class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span> Eliminar asignaturas
						        			</button>
							      			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="showLoadMatters()">
						        				<span class="glyphicon glyphicon-import" aria-hidden="true"></span> Cargar asignaturas
						        			</button>
					        			</div>
				      				</div>
				      			</nav>
						        <table class="table table-striped">
									<tr style="background-color: #bbb;">
										<th>Nombre</th>
										<th>C&oacute;digo</th>
										<th>P. acad&eacute;mico</th>
										<th>Nivel</th>
										<th>Modalidad</th>
										<th></th>
									</tr>
						        	<tr ng-repeat="matter in filterMatters | orderBy: 'matter.matterName' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" ng-class="{'danger': !matter.hasBank}">
										<!-- <td width="35%" ng-class="{'no-bank': !matter.hasBank}">{{matter.matter.name}}</td> -->
										<td width="35%">{{matter.matterName}}</td>
										<td width="15%">{{matter.matterCode}}</td>
										<td width="15%">{{matter.academicPeriodName}}</td>
										<td width="10%">{{matter.academicLevelName}}</td>
										<td width="15%">{{matter.modeName}}</td>
										<td width="20%" align="right">
											<a href='#' title='Banco de preguntas' ng-click="showSelectBank(matter)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
											<a href='#' title='Tipos de evaluación' ng-click="showLoadTypes(matter)"><span class="glyphicon glyphicon-tags" aria-hidden="true"></span></a>
											<a href='#' title='D&iacute;as evaluables' ng-click="showEvaluableDays(matter)"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a>
											<a href='#' title='Eliminar' ng-click="deleteMatter(matter)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
										</td>
									</tr>
						        </table>
						        <!--
						        <p align="center">
					            	<uib-pagination total-items="filterMatters.length" direction-links="true" boundary-links="true"
					            			previous-text="&lsaquo;" next-text="&rsaquo;"
					            			first-text="&lsaquo;&lsaquo;&lsaquo;" last-text="&rsaquo;&rsaquo;&rsaquo;"
					            			items-per-page="pageSize" ng-model="currentPage" max-size="10"
				            				class="pagination-sm"></uib-pagination>
				            	</p>
				            	-->
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

    	<!-- MODAL MATERIAS -->
		<div class="modal" id="add-matter-modal" tabindex="-1" role="dialog" aria-labelledby="add-matters-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-matters-modalLabel">Asignaci&oacute;n de asignaturas</h3>
					</div>
					<div class="modal-body">
						<div class="row" style="margin-bottom: 10px">
							<div class="col-md-12">
								<form class="form-inline">
									<label for="academicPeriodLoad">Per&iacute;odo</label>
									<select name="academicPeriodAdd" id="academicPeriodAdd"	ng-model="academicPeriodAdd" class="form-control" style="width: 40%;">
										<c:forEach items="${academicPeriods}" var="ap">
											<option value="<c:out value="${ap.code}"/>">
												<c:out value="${ap.name}" />
											</option>
										</c:forEach>
									</select>

									<label for="modeLoad">Modalidad</label>
									<select name="modeAdd" id="modeAdd" class="form-control" ng-model="modeAdd" style="width: 40%;">
										<c:forEach items="${modes}" var="m">
											<option value="<c:out value="${m.code}"/>">
												<c:out value="${m.name}" />
											</option>
										</c:forEach>
									</select>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<ui-select multiple ng-model="selectedMatters.selected"	theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
									<ui-select-match placeholder="{{selectLabel}}">{{$item.name}}&lt;{{$item.code}}&gt;</ui-select-match>
									<ui-select-choices repeat="matter in allmatters | propsFilter: {name: $select.search, code: $select.search} | limitTo: 50">
										<div ng-bind-html="name | highlight: $select.search"></div>
										<small>
											Nombre: <span ng-bind-html="''+matter.name| highlight: $select.search"></span>
											C&oacute;digo: <span ng-bind-html="''+matter.code| highlight: $select.search"></span>
										</small>
									</ui-select-choices>
								</ui-select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default" ng-click="addMatters()" ng-disabled="!showAddMatterBtn()">Actualizar</button>
					</div>
				</div>
			</div>
		</div>

		<!-- MODAL DIAS -->
		<div class="modal" id="add-days-modal" tabindex="-1" role="dialog" aria-labelledby="add-days-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-days-modalLabel">Asignaci&oacute;n de d&iacute;as evaluables</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								<ui-select multiple ng-model="matterDays.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false" 
								on-remove="removeDay($item, $model)">
									<ui-select-match placeholder="{{daysPlaceholder}}">{{$item.name}}&lt;{{$item.code}}&gt;</ui-select-match>
									<ui-select-choices repeat="day in allDays | propsFilter: {name: $select.search} | orderBy: 'id'">
										<div ng-bind-html="name | highlight: $select.search"></div>
										<small>
											Nombre: <span ng-bind-html="''+day.name| highlight: $select.search"></span>
										</small>
									</ui-select-choices>
								</ui-select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default" ng-click="addDays()">Actualizar</button>
					</div>
				</div>
			</div>
		</div>

		<!-- MODAL TIPOS EVALUACION -->
  		<div class="modal" id="evaluation-types-modal" tabindex="-1" role="dialog" aria-labelledby="evaluation-types-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
	    		<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h3 class="modal-title" id="evaluation-types-modalLabel">Tipos de evaluaci&oacute;n</h3>
	      			</div>
	      			<div class="modal-body">
	      				<div class="row">
							<div class="col-md-12">
					  			<ui-select multiple ng-model="selectedTypes.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
					    			<ui-select-match placeholder="Seleccione tipos de evaluaci&oacute;n...">{{$item.name}} &lt;{{$item.code}}&gt;</ui-select-match>
					    				<ui-select-choices repeat="type in allTypes | propsFilter: {name: $select.search, code: $select.search}">
					      					<div ng-bind-html="name | highlight: $select.search"></div>
					      						<small>
					        						Nombre: <span ng-bind-html="''+type.name| highlight: $select.search"></span>
					        						C&oacute;digo: <span ng-bind-html="''+type.code| highlight: $select.search"></span>
					      						</small>
					    				</ui-select-choices>
					  			</ui-select>
							</div>
	        			</div>
	      			</div>
	      			<div class="modal-footer">
	        			<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        			<button type="button" class="btn btn-default" ng-click="addTypes(matter)">Actualizar</button>
	      			</div>
	    		</div>
	  		</div>
    	</div>

		<!-- MODAL BANCO -->
  		<div class="modal" id="add-bank-modal" tabindex="-1" role="dialog" aria-labelledby="add-bank-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
	    		<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h3 class="modal-title" id="add-bank-modalLabel">Asignaci&oacute;n de banco de preguntas</h3>
	      			</div>
	      			<div class="modal-body">
	      				<div class="row">
							<div class="col-md-12">
					  			<ui-select ng-model="bankMatter.selected" theme="bootstrap" ng-disabled="disabled">
					    			<ui-select-match placeholder="Seleccione banco de preguntas (limitado 50 primeros resultados)...">{{$select.selected.name}}</ui-select-match>
					    				<ui-select-choices repeat="bank in banks | propsFilter: {name: $select.search, state: $select.search} | limitTo: 50">
					      					<div ng-bind-html="name | highlight: $select.search"></div>
					      						<small>
					        						Nombre: <span ng-bind-html="''+bank.name| highlight: $select.search"></span>
					        						Estado: <span ng-bind-html="''+bank.state| highlight: $select.search"></span>
					      						</small>
					    				</ui-select-choices>
					  			</ui-select>
							</div>
	        			</div>
	      			</div>
	      			<div class="modal-footer">
	        			<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        			<button type="button" class="btn btn-default" ng-click="setBank()">Seleccionar</button>
	      			</div>
	    		</div>
	  		</div>
		</div>

		<!-- MODAL CARGA -->
    	<div class="modal" id="load-matters-modal" tabindex="-1" role="dialog" aria-labelledby="load-matters-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
	    		<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h3 class="modal-title" id="load-matters-modalLabel">Carga masiva de asignaturas</h3>
	      			</div>
	      			<div class="modal-body">
	      				<div class="row">
	      					<div class="col-md-12">
	      						<form class="form-inline">
		      		 	 			<label for="academicPeriodLoad">Per&iacute;odo:</label>
				  		 			<select name="academicPeriodLoad"  id="academicPeriodLoad" class="form-control">
						    		<c:forEach items="${academicPeriods}" var="ap">
						        		<option value="<c:out value="${ap.code}"/>">
						            		<c:out value="${ap.name}"/>
						        		</option>
						    		</c:forEach>
						  			</select>

						  			<label for="academicLevelLoad">Nivel:</label>
						  			<select name="academicLevelLoad"  id="academicLevelLoad" class="form-control">
						    		<c:forEach items="${academicLevels}" var="al">
						        		<option value="<c:out value="${al.code}"/>">
						            		<c:out value="${al.name}"/>
						        		</option>
						    		</c:forEach>
						  			</select>

						  			<label for="modeLoad">Modalidad</label>
						  			<select name="modeLoad"  id="modeLoad" class="form-control">
						    		<c:forEach items="${modes}" var="m">
						        		<option value="<c:out value="${m.code}"/>">
						            		<c:out value="${m.name}"/>
						        		</option>
						    		</c:forEach>
						  			</select>
								</form>
							</div>
						</div>
						<div class="spacer-30"></div>
						<div class="row">
							 <div class="col-md-12">
				  				<uib-progressbar class="progress-striped active" value="mattersProgress" type="success">
				  					<i>{{mattersProgress}}%	</i>
				  				</uib-progressbar>
							</div>
	        			</div>
	      			</div>
	      			<div class="modal-footer">
	        			<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        			<button type="button" class="btn btn-default" ng-click="loadAllMatters()" ng-disabled="showLoadBtn==0">Cargar</button>
	      			</div>
	    		</div>
	  		</div>
		</div>
	</div>

	<content tag="local_script">
    	<script src="<c:url value="/web-resources/js/controller/evaluation-event-matters-ctrl.js" />"></script>
		<!--<script type="text/ng-template" id="popover-extraScore">
			<div class="form-group" style="text-align: center" id="extraScore-id">
				<h4>Puntuación adicional</h4>
		         <input id="extrascore-val" placeholder="Puntación adicional" class="form-control" maxlength="5" ng-model="extraScore"
						style="display: inline; width: auto">
		         <a href='#' ng-click="setExtraScore()"><span class="glyphicon glyphicon-pencil" aria-hidden="true" title="Modificar"></span></a>
		    </div>
		</script> -->
	</content>
</body>
</html>