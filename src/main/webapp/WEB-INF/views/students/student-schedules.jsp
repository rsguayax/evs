<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
	<title>${headText}</title>
	<script type="text/javascript" src="<c:url value="/web-resources/js/jquery.PrintArea.js" />" charset="utf-8"></script>
</head>
<body>
	<div class="row" ng-app="app" ng-controller="studentSchedulesCtrl" ng-init="init('${student.id}');" ng-cloak>
		<div class="col-md-12">
			<h3>${headText}</h3>
			<div style="margin-top: 20px;">
				<div ng-show="eventsEvaluations.length > 0">
					<div ng-repeat="eventEvaluations in eventsEvaluations" class="panel-group">
				  		<div class="panel panel-default">
					    	<div class="panel-heading">
					      		<h4 class="panel-title">
							        <a data-toggle="collapse" href="#event{{eventEvaluations.evaluationEventId}}">Evento de evaluación: {{eventEvaluations.evaluationEventName}}</a>
					      		</h4>
					    	</div>
					    	<div id="event{{eventEvaluations.evaluationEventId}}" class="panel-collapse collapse">
					      		<div class="panel-body">
					      			<div ng-show="eventEvaluations.studentEvaluations.length > 0">
					      				<div class="right-buttons" style="margin-bottom: 12px;">
							        		<button ng-show="eventEvaluations.lastScheduleModification != null" class="btn btn-primary" ng-click="showLastScheduleModification(eventEvaluations.lastScheduleModification)"><span class="glyphicon glyphicon-print"></span> Imprimir última modificación de horarios</button>
											<a class="btn btn-primary" href="<c:url value="/students/${student.id}/evaluationevent/{{eventEvaluations.evaluationEventId}}/editexamsschedules" />"><span class="glyphicon glyphicon-edit"></span> Modificar horarios</a>
										</div>
					
		 					      		<div ng-repeat="studentEvaluation in eventEvaluations.studentEvaluations" class="panel panel-primary" style="margin-bottom: 20px;">
											<div class="panel-heading">
												<span>{{studentEvaluation.classroomTimeBlock.startDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}} </span>
												<span>- {{studentEvaluation.classroomTimeBlock.startDate | date:'HH:mm' : 'UTC'}} </span>
												<span>a {{studentEvaluation.classroomTimeBlock.endDate | date:'HH:mm' : 'UTC'}}</span>
											</div>
											<div class="panel-body">
												<div class="bs-callout no-margin">
													<div><b>Centro de evaluación:</b> {{studentEvaluation.evaluationCenter.name}}</div>
												  	<div><b>Aula:</b> {{studentEvaluation.classroom.name}}</div>
													<div><b>Fecha:</b> {{studentEvaluation.classroomTimeBlock.startDate | date:"d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
													<div><b>Horario:</b> {{studentEvaluation.classroomTimeBlock.startDate | date:'HH:mm' : 'UTC'}} - {{studentEvaluation.classroomTimeBlock.endDate | date:'HH:mm' : 'UTC'}}</div>
												</div>
												<div>
													<h3>Exámenes</h3>
													<table class="table table-striped">
														<tr style="background-color: #e1e1e1"><th>Asignatura</th><th>Test</th><th>Evaluación</th></tr>
														<tr ng-repeat="matterTest in studentEvaluation.matterTestInfos" class="ng-cloak">
															<td width="40%">{{matterTest.matterName}}</td>
															<td width="30%">{{matterTest.testName}}</td>
															<td width="30%">{{matterTest.testEvaluationType}}</td>
														</tr>
													</table>
												</div>
									  		</div>
						  				</div>
					  				</div>
					  				<div ng-show="eventEvaluations.studentEvaluations.length == 0">
					  					<h4>No hay horarios del estudiante en el evento de evaluación</h4>
					  				</div>
						      	</div>
					    	</div>
					  	</div>
					</div>
				</div>
				<div ng-show="eventsEvaluations.length == 0">
		        	<h4>No hay ningún evento de evaluación con horarios del estudiante</h4>
		        </div>
		        <div class="loading-small" ng-show="eventsEvaluations == null">
		        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
		        </div>
			</div>
		</div>
		
		<!-- MODAL SCHEDULE MODIFICATION -->
		<div class="modal" id="scheduleModificationModal" tabindex="-1" role="dialog" aria-labelledby="scheduleModificationModalTitle">
		  	<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h3 class="modal-title" id="scheduleModificationModalTitle">Modificación de horarios</h3>
		      		</div>
		      		<div id="scheduleModificationPrint" class="modal-body">
		      			<div><b>{{lastScheduleModification.studentName}}</b></div>
						<div style="margin-top: 12px;">Modificación de horarios realizada el {{lastScheduleModification.date | date:"d 'de' MMMM 'de' yyyy" : 'UTC'}} a las {{lastScheduleModification.date | date:'HH:mm' : 'UTC'}} por {{lastScheduleModification.createdBy}}</div>
						<div ng-repeat="scheduleModificationInfo in lastScheduleModification.scheduleModificationInfos" class="panel panel-primary" style="margin-top: 20px;">
							<div class="panel-heading">Examen de la asignatura "{{scheduleModificationInfo.matterName}}"</div>
							<div class="panel-body">
								<table class="table table-borderless no-margin">
									<tr>
										<td>
											<div class="block-info block-info-border-left">
											  	<h4>Horario anterior</h4>
											  	<div>
											  		<div><b>Centro de evaluación:</b> {{scheduleModificationInfo.oldClassroomTimeBlock.evaluationCenterName}}</div>
												  	<div><b>Aula:</b> {{scheduleModificationInfo.oldClassroomTimeBlock.classroomName}}</div>
													<div><b>Fecha:</b> {{scheduleModificationInfo.oldClassroomTimeBlock.startDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
													<div><b>Horario:</b> {{scheduleModificationInfo.oldClassroomTimeBlock.startDate | date:'HH:mm' : 'UTC'}} - {{scheduleModificationInfo.oldClassroomTimeBlock.endDate | date:'HH:mm' : 'UTC'}}</div>
											  	</div>
											</div>
										</td>
										<td>
											<div class="block-info block-info-border-left">
											  	<h4>Nuevo horario</h4>
											  	<div>
											  		<div><b>Centro de evaluación:</b> {{scheduleModificationInfo.newClassroomTimeBlock.evaluationCenterName}}</div>
												  	<div><b>Aula:</b> {{scheduleModificationInfo.newClassroomTimeBlock.classroomName}}</div>
													<div><b>Fecha:</b> {{scheduleModificationInfo.newClassroomTimeBlock.startDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
													<div><b>Horario:</b> {{scheduleModificationInfo.newClassroomTimeBlock.startDate | date:'HH:mm' : 'UTC'}} - {{scheduleModificationInfo.newClassroomTimeBlock.endDate | date:'HH:mm' : 'UTC'}}</div>
											  	</div>
											</div>
										</td>
									</tr>
								</table>
					  		</div>
		  				</div>
		  				<div><b>Observaciones:</b></div>
						<div ng-bind-html="lastScheduleModification.message | newlines"></div>
		      		</div>
		      		<div class="modal-footer">
		      			<button type="button" class="btn btn-primary" ng-click="printScheduleModification()">Imprimir</button>
				        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		      		</div>
		    	</div>
		  	</div>
	  	</div>
	</div>
	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/student-schedules-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>