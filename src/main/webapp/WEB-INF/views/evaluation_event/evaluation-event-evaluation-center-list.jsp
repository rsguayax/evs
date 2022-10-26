<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Evento de evaluaci&oacute;n</title>

<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active');
		$('#tabEvaluationCenters').addClass('active');
	});
</script>

</head>
<body>
	<div ng-controller="evaluationEventEvaluationCenterCtrl" ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
	 		<h3>Centros de evaluaci&oacute;n del evento de evaluaci&oacute;n ${evaluationEvent.name}
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
					<div class="tab-content container" ng-cloak>
				        <div class="tab-pane active">
				        	<div class="right-buttons">
								<button class="btn btn-default" ng-click="showAddEvaluationCenter()"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir centro de evaluaci&oacute;n</button>
							</div>
						</div>
						<br/>
						<div class="panel-group">
				  			<div ng-repeat="evaluationEventEvaluationCenter in evaluationEventEvaluationCenters" class="panel panel-default" style="margin-bottom: 20px;">
		  						<div class="panel-heading">
		  							<span>{{evaluationEventEvaluationCenter.evaluationCenter.code}} - {{evaluationEventEvaluationCenter.evaluationCenter.name}}</span>
		  							<a class="pull-right" href='#' ng-click="deleteEvaluationCenter(evaluationEventEvaluationCenter)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Eliminar</a>
		  						</div>
		      					<div class="panel-body">
		      						<div class="panel-body-header">
		      							<div class="title"><span>Aulas</span></div>
		      							<div class="buttons">
		      								<button class="btn btn-default" ng-click="showAddCenter(evaluationEventEvaluationCenter)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Asociar centros educativos</button>
											<button class="btn btn-default" ng-click="showAddAllClassrooms(evaluationEventEvaluationCenter)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Configurar todas las aulas</button>
											<button class="btn btn-default" ng-click="addClassroom(evaluationEventEvaluationCenter)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Configurar nueva aula</button>
										</div>
		      						</div>
		      						<div class="panel-body-content">
		      							<table class="table table-striped" ng-show="evaluationEventEvaluationCenter.evaluationEventClassrooms.length > 0">
											<tr style="background-color: #ddd;">
												<th>Nombre</th>
												<th>Capacidad</th>
												<th>CAP</th>
												<th>Red</th>
												<th>Horario</th>
												<th></th>
											</tr>
											<tr ng-repeat="evaluationEventClassroom in evaluationEventEvaluationCenter.evaluationEventClassrooms" class="ng-cloak">
												<td width="15%">{{evaluationEventClassroom.classroom.name}}</td>
												<td width="15%">{{evaluationEventClassroom.seats}}</td>
												<td width="15%">
													<span ng-show="evaluationEventClassroom.cap">{{evaluationEventClassroom.cap.serialNumber}}</span>
													<span ng-hide="evaluationEventClassroom.cap">-</span>
												</td>
												<td width="15%">
													<span ng-show="evaluationEventClassroom.net">{{evaluationEventClassroom.net.code}}</span>
													<span ng-hide="evaluationEventClassroom.net">-</span>
												</td>
												<td width="30%">{{evaluationEventClassroom.schedule.name}}</td>
												<td width="15%" align="right">
													<a href='<c:url value="/eventclassroom/{{evaluationEventClassroom.id}}/schedule"/>' title="Horario"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a>
													<a href='#' title="Editar" ng-click="editClassroom(evaluationEventEvaluationCenter, evaluationEventClassroom)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
													<a href='#' title="Eliminar" ng-click="deleteClassroom(evaluationEventEvaluationCenter, evaluationEventClassroom)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
												</td>
											</tr>
										</table>
										<div ng-show="evaluationEventEvaluationCenter.evaluationEventClassrooms.length == 0">
								        	<h4>No hay aulas configuradas</h4>
								        </div>
		      						</div>
		      					</div>
						  	</div>
						  	<div ng-show="evaluationEventEvaluationCenters.length == 0">
					        	<h4>No hay centros de evaluaci&oacute;n definidos</h4>
					        </div>
					        <div class="loading-small" ng-show="loadingEvaluationEventEvaluationCenters">
					        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
					        </div>
						</div>
			    	</div>
				</div>
			</div>
		</div>
	
		<!-- MODAL ADD EVALUATION CENTER -->
		<div class="modal" id="add-evaluation-center-modal" tabindex="-1" role="dialog" aria-labelledby="add-evaluation-center-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
			      	<div class="modal-header">
			        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        	<h3 class="modal-title" id="add-evaluation-center-modalLabel">Asignaci&oacute;n de centros de evaluaci&oacute;n</h3>
			      	</div>
			      	<div class="modal-body">
			      		<ui-select multiple ng-model="selectedEvaluationCenters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
					    	<ui-select-match placeholder="Seleccione centros de evaluación...">{{$item.code}} &lt;{{$item.name}}&gt;</ui-select-match>
						    <ui-select-choices repeat="evaluationCenter in unselectedEvaluationCenters | propsFilter: {name: $select.search, code: $select.search}">
					      		<div ng-bind-html="evaluationCenter.name | highlight: $select.search"></div>
					      		<small>
					      			C&oacute;digo: <span ng-bind-html="''+evaluationCenter.code| highlight: $select.search"></span>
						        	Nombre: <span ng-bind-html="''+evaluationCenter.name| highlight: $select.search"></span>
						      	</small>
						    </ui-select-choices>
					  	</ui-select>
		      		</div>
			      	<div class="modal-footer">
			        	<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			        	<button type="button" class="btn btn-default" ng-click="addEvaluationCenters()">A&ntilde;adir</button>
			      	</div>
		    	</div>
	  		</div>
		</div>
	
		<!-- MODAL ADD CENTER -->
		<div class="modal" id="add-center-modal" tabindex="-1" role="dialog" aria-labelledby="add-center-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
			      	<div class="modal-header">
			        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        	<h3 class="modal-title" id="add-center-modalLabel">Asignaci&oacute;n de centros educativos</h3>
			      	</div>
			      	<div class="modal-body">
			      		<ui-select multiple ng-model="selectedCenters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
					    	<ui-select-match placeholder="Seleccione centros educativos...">{{$item.code}} &lt;{{$item.name}}&gt;</ui-select-match>
						    <ui-select-choices repeat="center in unselectedCenters | propsFilter: {name: $select.search, code: $select.search}">
					      		<div ng-bind-html="center.name | highlight: $select.search"></div>
					      		<small>
					      			C&oacute;digo: <span ng-bind-html="''+center.code| highlight: $select.search"></span>
						        	Nombre: <span ng-bind-html="''+center.name| highlight: $select.search"></span>
						      	</small>
						    </ui-select-choices>
					  	</ui-select>
		      		</div>
			      	<div class="modal-footer">
			        	<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			        	<button type="button" class="btn btn-default" ng-click="addCenters()">A&ntilde;adir</button>
			      	</div>
		    	</div>
	  		</div>
		</div>
	
		<!-- MODAL ADD EDIT CLASSROOM -->
		<div class="modal" id="classroom-modal" tabindex="-1" role="dialog" aria-labelledby="classroom-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
	
		    	</div>
	  		</div>
		</div>
	
		<!-- MODAL ADD ALL CLASSROOMS -->
		<div class="modal" id="addAllClassroomsModal" tabindex="-1" role="dialog" aria-labelledby="addAllClassroomsModalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
			      	<div class="modal-header">
			        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        	<h3 class="modal-title" id="addAllClassroomsModalLabel">Configurar todas las aulas</h3>
			      	</div>
			      	<div class="modal-body">
			      		<table class="table table-striped" ng-show="unselectedEventClassrooms.length > 0">
							<tr style="background-color: #ddd;">
								<th>Nombre</th>
								<th>Capacidad</th>
								<th>CAP</th>
								<th>Red</th>
								<th>Horario</th>
							</tr>
							<tr ng-repeat="eventClassroom in unselectedEventClassrooms" class="ng-cloak">
								<td width="20%">{{eventClassroom.classroom.name}}</td>
								<td width="15%">{{eventClassroom.seats}}</td>
								<td width="20%">{{eventClassroom.cap.serialNumber}}</td>
								<td width="20%" ng-show="eventClassroom.net">{{eventClassroom.net.code}}</td>
								<td width="20%" ng-hide="eventClassroom.net">----</td>
								<td width="45%">
									<select class="form-control" ng-model="eventClassroom.schedule" ng-options="schedule as schedule.name for schedule in schedules track by schedule.id"></select>
								</td>
							</tr>
						</table>
						<div ng-show="unselectedEventClassrooms.length == 0">
				        	<h4>Todas las aulas del centro de evaluaci&oacute;n "{{evaluationEventEvaluationCenter.evaluationCenter.name}}" est&aacute;n configuradas</h4>
				        </div>
				        <div class="loading-small" ng-show="loadingUnselectedEventClassrooms">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
		      		</div>
			      	<div class="modal-footer">
			        	<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			        	<button type="button" class="btn btn-default" ng-click="addAllClassrooms(evaluationEventEvaluationCenter)">Configurar todas</button>
			      	</div>
		    	</div>
	  		</div>
		</div>
	
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/evaluation-event-evaluation-center-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>