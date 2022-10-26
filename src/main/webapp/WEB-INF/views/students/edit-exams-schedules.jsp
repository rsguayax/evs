<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Modificar horarios de exámenes</title>
<script type="text/javascript" src="<c:url value="/web-resources/js/jquery.PrintArea.js" />" charset="utf-8"></script>
</head>
<body>
	<div ng-controller="editExamsSchedulesCtrl" ng-init="init('${user.id}', '${evaluationEvent.id}');" ng-cloak>
		<table class="table table-borderless">
			<tr>
				<td class="no-padding"><h3 class="no-margin">Modificar horarios de exámenes del estudiante "${user.fullName}"</h3></td>
				<td class="no-padding text-right"><a href="<c:url value="/students/${user.id}/schedules"/>" class="btn btn-primary"><span class="glyphicon glyphicon-chevron-left"></span> Volver</a></td>
			</tr>
		</table>
		<div id="editExamsSchedulesWizard">
			<table class="table table-borderless" style="margin: 0px 0px 12px 0px;">
				<tr>
					<td class="no-padding" style="vertical-align: bottom;"><h4 class="no-margin">Evento de evaluación: ${evaluationEvent.name}</h4></td>
					<td class="no-padding text-right"><button ng-show="lastScheduleModification != null" class="btn btn-primary" ng-click="showLastScheduleModification()"><span class="glyphicon glyphicon-print"></span> Imprimir última modificación de horarios</button></td>
				</tr>
			</table>
			<table class="table table-striped">
				<tr style="background-color: #bbbbbb;"><th></th><th>Asignatura</th><th>Test</th><th>Evaluación</th><th>Centro</th><th>Aula</th><th>Fecha</th><th>Horario</th><th></th></tr>
				<tr ng-repeat="matterTest in matterTests" class="ng-cloak">
					<td><span ng-show="matterTest.modified" class="glyphicon glyphicon-pencil" title="Horario modificado"></span></td>
					<td>{{matterTest.matterName}}</td>
					<td>{{matterTest.testName}}</td>
					<td>{{matterTest.testEvaluationType}}</td>
					<td>{{matterTest.evaluationCenterName}}</td>
					<td>{{matterTest.classroomName}}</td>
					<td>{{matterTest.timeBlockStartDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}}</td>
					<td>{{matterTest.timeBlockStartDate | date:'HH:mm' : 'UTC'}} - {{matterTest.timeBlockEndDate | date:'HH:mm' : 'UTC'}}</td>
					<td><a title="Modificar horario" href ng-click="showEditExamSchedule(matterTest)"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a></td>
				</tr>
			</table>
			<div ng-show="modifiedMatterTests().length > 0">
				<div class="form-group">
			  		<label for="schedulesModificationMessage">Indique el motivo de la modificación de horarios:</label>
			  		<textarea class="form-control" rows="5" id="schedulesModificationMessage" ng-model="schedulesModificationMessage"></textarea>
				</div>
				<div class="right-buttons" style="margin-top: 12px;">
					<button class="btn btn-primary" ng-click="editExamsSchedules()"><span class="glyphicon glyphicon-calendar"></span> Modificar horarios</button>
					<button class="btn btn-danger" ng-click="cancelExamsSchedulesModification()"><span class="glyphicon glyphicon-remove"></span> Cancelar cambios</button>
				</div>
			</div>
		</div>
		<div class="loading-small" ng-show="studentEvaluations == null">
        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
        </div>
	
		<!-- MODAL EDIT MATTER TEST SCHEDULE -->
		<div class="modal" id="editExamScheduleModal" tabindex="-1" role="dialog" aria-labelledby="editExamScheduleModalTitle">
		  	<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h3 class="modal-title" id="editExamScheduleModalTitle">Modificar horario de "{{editExamScheduleMatterTest.testName}}" de asignatura "{{editExamScheduleMatterTest.matterName}}"</h3>
		      		</div>
		      		<div class="modal-body">
		      			<div id="editExamScheduleWizard" class="wizard">
							<ul class="steps-indicator">
						  		<li class="step active"><a href ng-click="showEditExamScheduleStep(1)"><span class="step-number">1</span> Seleccionar centro</a></li>
						  		<li class="step disabled"><a href ng-click="showEditExamScheduleStep(2)"><span class="step-number">2</span> Seleccionar horario</a></li>
						  		<li class="step disabled"><a href ng-click="showEditExamScheduleStep(3)"><span class="step-number">3</span> Finalizar</a></li>
							</ul>
							<div class="steps">
								<div class="step">
									<select class="form-control ng-cloak" ng-model="examEventCenterId" ng-change="selectExamEvaluationCenter()" ng-show="eventCenters != null" ng-options="eventCenter.id as eventCenter.evaluationCenterName for eventCenter in eventCenters"></select>
									<div class="loading-small" ng-show="eventCenters == null">
							        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
							        </div>
									<div class="right-buttons" style="margin-top: 12px;">
										<button class="btn btn-primary" ng-click="nextEditExamScheduleStep()">Siguiente <span class="glyphicon glyphicon-chevron-right"></span></button>
									</div>
								</div>
								<div class="step">
									<div ng-repeat="(classroomName, timeBlocks) in classroomTimeBlocks" class="panel panel-primary" style="margin-bottom: 20px;"  ng-show="!(classroomTimeBlocks | isEmpty)">
										<div class="panel-heading">Aula "{{classroomName}}"</div>
										<div class="panel-body">
											<table class="table table-striped">
												<tr style="background-color: #e1e1e1"><th></th><th>Fecha</th><th>Horario</th><th>Plazas disponibles</th><th>Plazas ocupadas</th></tr>
												<tr ng-repeat="timeBlock in timeBlocks" class="ng-cloak">
													<td width="5%">
														<input name="examClassroomTimeBlock" type="radio" ng-value="{{timeBlock}}" ng-model="$parent.$parent.examClassroomTimeBlock">
													</td>
													<td width="35%">{{timeBlock.startDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}}</td>
													<td width="20%">{{timeBlock.startDate | date:'HH:mm' : 'UTC'}} - {{timeBlock.endDate | date:'HH:mm' : 'UTC'}}</td>
													<td width="20%">{{timeBlock.availableSeats}}/{{timeBlock.seats}} ({{timeBlock.percentageAvailableSeats }}%)</td>
													<td width="20%">{{timeBlock.occupiedSeats}}/{{timeBlock.seats}} ({{timeBlock.percentageOccupiedSeats }}%)</td>
												</tr>
											</table>
								  		</div>
					  				</div>
									<div ng-show="classroomTimeBlocks | isEmpty">
							        	<h4>No hay horarios con plazas libres en este centro</h4>
							        </div>
							        <div class="right-buttons" style="margin-top: 12px;">
										<button class="btn btn-primary" ng-click="previousEditExamScheduleStep()"><span class="glyphicon glyphicon-chevron-left"></span> Anterior</button>
										<button class="btn btn-primary" ng-click="nextEditExamScheduleStep()">Siguiente <span class="glyphicon glyphicon-chevron-right"></span></button>
									</div>
								</div>
								<div class="step">
									<div class="bs-callout">
									  	<h4>Examen de la asignatura "{{editExamScheduleMatterTest.matterName}}"</h4>
									  	<div style="margin-top: 12px;">
									  		<div><b>Centro de evaluación:</b> {{examClassroomTimeBlock.evaluationCenterName}}</div>
										  	<div><b>Aula:</b> {{examClassroomTimeBlock.classroomName}}</div>
											<div><b>Fecha:</b> {{examClassroomTimeBlock.startDate | date:"EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
											<div><b>Horario:</b> {{examClassroomTimeBlock.startDate | date:'HH:mm' : 'UTC'}} - {{examClassroomTimeBlock.endDate | date:'HH:mm' : 'UTC'}}</div>
									  	</div>
									</div>
									<div class="right-buttons" style="margin-top: 12px;">
										<button class="btn btn-primary" ng-click="previousEditExamScheduleStep()"><span class="glyphicon glyphicon-chevron-left"></span> Anterior</button>
										<button class="btn btn-primary" ng-click="editExamSchedule()"><span class="glyphicon glyphicon-edit"></span> Modificar horario</button>
									</div>
								</div>
							</div>
						</div>
		      		</div>
		      		<div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		      		</div>
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
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/edit-exams-schedules-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>