<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Verificar evaluación</title>
<script type="text/javascript" src="<c:url value="/web-resources/js/jquery.PrintArea.js" />" charset="utf-8"></script>
</head>
<body>
	<div ng-controller="checkEvaluationCtrl" ng-init="init('${evaluationEvent.id}');" ng-cloak>
		<div class="page-header">
			<table class="no-margin" style="width: 100%;">
				<tr>
					<td><h3>Información de la evaluación "${evaluationEvent.name}"</h3></td>
					<td style="text-align: right;">
						<button class="btn btn-default navbar-btn" ng-click="printEvaluationInfo()""><span class="glyphicon glyphicon-print"></span> Imprimir</button>
					</td>
				</tr>
			</table>
		</div>
		<div id="evaluationInfoPrint" ng-show="!loadingEvaluationInfo">
			<h2 class="visible-print" style="display: none; margin-bottom: 20px; padding-bottom: 8px; border-bottom: 1px solid #ccc;">Información de la evaluación "${evaluationEvent.name}"</h2>
			<div ng-show="!evaluationInfo.checkOk">
				<div ng-show="evaluationInfo.studentsMattersWithoutTest.length > 0" style="margin-bottom: 36px;">
					<div class="alert alert-warning">
						<table>
							<tr>
								<td><span style="color: #eaab00; font-size: 32px;" class="glyphicon glyphicon-exclamation-sign"></span></td>
								<td style="padding-left: 8px; font-size: 18px;">Hay <b>{{evaluationInfo.studentsMattersWithoutTest.length}}</b> estudiantes pendientes de la asignación de tests</td>
							</tr>
						</table>
					</div>
					<div ng-repeat="studentMattersWithoutTest in evaluationInfo.studentsMattersWithoutTest" class="ng-cloak" style="margin-bottom: 14px; border-top: 1px solid #ccc; padding: 14px 0px 0px 0px;">
						<p class="studentName" style="font-size: 16px; font-weight: bold; margin: 0px;">{{studentMattersWithoutTest.studentIdentification}} - {{studentMattersWithoutTest.studentFullName}}</p>
						<table class="table table-striped table-vcentered matterTestInfo" style="margin-top: 14px;">
							<tr style="background-color: #bbb"><th>Código</th><th>Asignatura</th><th>Periodo académico</th><th>Nivel académico</th><th>Modalidad</th><th>Centro</th><th>Mensaje</th></tr>
							<tr ng-repeat="matterWithoutTest in studentMattersWithoutTest.mattersWithoutTest" class="ng-cloak">
								<td width="10%">{{matterWithoutTest.code}}</td>
								<td width="20%">{{matterWithoutTest.name}}</td>
								<td width="10%">{{matterWithoutTest.academicPeriod}}</td>
								<td width="10%">{{matterWithoutTest.academicLevel}}</td>
								<td width="10%">{{matterWithoutTest.mode}}</td>
								<td width="10%">{{matterWithoutTest.center}}</td>
								<td width="30%">{{matterWithoutTest.message}}</td>
							</tr>
						</table>
					</div>
				</div>
				<div ng-show="evaluationInfo.studentsTestsWithoutSchedule.length > 0">
					<div class="alert alert-warning">
						<table>
							<tr>
								<td><span style="color: #eaab00; font-size: 32px;" class="glyphicon glyphicon-exclamation-sign"></span></td>
								<td style="padding-left: 8px; font-size: 18px;">Hay <b>{{evaluationInfo.testsSchedulesInfo.testsWithoutSchedule}}</b> tests pendientes de la asignación de horario</td>
							</tr>
						</table>
					</div>
					<div style="margin-top: 10px;">
						<div ng-repeat="studentTestsWithoutSchedule in evaluationInfo.studentsTestsWithoutSchedule" class="ng-cloak" style="margin-bottom: 14px; border-top: 1px solid #ccc; padding: 14px 0px 0px 0px;">
							<p class="studentName" style="font-size: 16px; font-weight: bold; margin: 0px;">{{studentTestsWithoutSchedule.studentIdentification}} - {{studentTestsWithoutSchedule.studentFullName}}</p>
							<div ng-repeat="testWithoutSchedule in studentTestsWithoutSchedule.testsWithoutSchedule" class="matterTestInfo">
								<div ng-if="testWithoutSchedule.logs.length > 0" class="panel panel-primary" style="margin: 14px 0px 0px 0px;">
									<div class="panel-heading">
										<table>
											<tr>
												<td style="padding-right: 25px;"><b>Asignatura:</b> {{testWithoutSchedule.matterName}}</td>
												<td style="padding-right: 25px;"><b>Test:</b> {{testWithoutSchedule.testName}}</td>
												<td style="padding-right: 25px;"><b>Periodo académico:</b> {{testWithoutSchedule.matter.academicPeriod}}</td>
												<td style="padding-right: 25px;"><b>Nivel académico:</b> {{testWithoutSchedule.matter.academicLevel}}</td>
												<td style="padding-right: 25px;"><b>Modalidad:</b> {{testWithoutSchedule.matter.mode}}</td>
												<td style="padding-right: 25px;"><b>Centro:</b> {{testWithoutSchedule.matter.center}}</td>
											</tr>
										</table>
									</div>
									<div class="panel-body">
										<table class="no-margin" style="width: 100%; background-color:#f5f5f5; border: 1px solid #ddd;">
											<tr>
												<td style="width: 50%; font-weight: bold; padding: 12px;">Información de la jornada de evaluación</td>
												<td style="width: 50%; font-weight: bold; padding: 12px;">Mensaje</td>
											</tr>
										</table>
										<div>
											<div ng-repeat="testLog in testWithoutSchedule.logs" class="bs-callout row" style="padding: 12px; margin: 12px 0px;">
												<table class="no-margin" style="width: 100%">
													<tr>
														<td style="width: 50%;">
															<div><b>Centro de evaluación:</b> {{testLog.evaluationCenterName}}</div>
															<div><b>Aula:</b> {{testLog.classroomName}}</div>
															<div><b>Fecha:</b> {{testLog.timeBlockStartDate | date:"EEEE d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
															<div><b>Horario:</b> {{testLog.timeBlockStartDate | date:'HH:mm' : 'UTC'}} - {{testLog.timeBlockEndDate | date:'HH:mm' : 'UTC'}}</div>
															<div><b>Tipos de estudiantes:</b> {{testLog.timeBlockStudentTypes}}</div>
														</td>
														<td style="width: 50%; padding-left: 12px;">{{testLog.message}}</td>
													</tr>
												</table>
											</div>
										</div>
									</div>
								</div>
								<div ng-if="testWithoutSchedule.logs.length == 0">
									<div class="alert alert-danger" style="margin-top: 10px;">
										<table>
											<tr>
												<td><span style="color: #eaab00; font-size: 20px;" class="glyphicon glyphicon-exclamation-sign"></span></td>
												<td class="testAlertMessage" style="padding-left: 8px; font-size: 14px;">Vuelve a ejecutar el proceso de asignación de horarios para asignar un horario o obtener el log del test "{{testWithoutSchedule.testName}}" de la asignatura "{{testWithoutSchedule.matter.name}}"</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div ng-show="evaluationInfo.checkOk">
				<div class="alert alert-success">
					<table>
						<tr>
							<td><span style="color: #739600; font-size: 32px;" class="glyphicon glyphicon-ok-sign"></span></td>
							<td style="padding-left: 8px; font-size: 18px">Todos los estudiantes tienen tests asociados para todas las asignaturas y todos los tests tienen horario asignado</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="loading-big" ng-show="loadingEvaluationInfo">
        	<img src="<c:url value="/web-resources/img/load.gif" />" />
        </div>
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/check-evaluation-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>