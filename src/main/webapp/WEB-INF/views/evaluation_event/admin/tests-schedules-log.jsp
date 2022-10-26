<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Administraci&oacute;n</title>
</head>
<body>
	<div class="page-header">
  		<h3>Log del &uacute;ltimo proceso de asignaci&oacute;n de horarios a tests del evento de evaluaci&oacute;n ${evaluationEvent.name}</h3>
	</div>
	<div ng-controller="testsSchedulesLogCtrl" ng-init="init('${evaluationEvent.id}', '${isAutomaticAvaluationEvent}');" ng-cloak>
		<div class="panel-group" id="accordion">
			<div class="panel panel-default" ng-show="isAutomaticAvaluationEvent=='0'" >
				<div class="panel-heading"><a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" style="color: #000000;">
					<span class="glyphicon glyphicon-plus"></span> Log de asignación manual</a></div>
				<div id="collapseOne" class="panel-collapse collapse">
					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<div  ng-if="genericLogs.length > 0">
									<div class="loading-small" ng-show="loadingGenericLogs">
							        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
							        </div>
									<table class="table table-striped" ng-show="!loadingGenericLog">
										<tr style="background-color: #bbbbbb"><th>Mensaje</th></tr>
							        	<tr ng-repeat="log in genericLogs | orderBy: 'name' | startPaginate: (currentPageGeneric - 1) * pageSizeGeneric | limitTo: pageSizeGeneric" class="ng-cloak">
											<td width="15%">{{log.message}}</td>						
										</tr>
							        </table>
							        <nav aria-label="Paginado" ng-show="!loadingGenericLogs">
							        	Mostrando
										<select	data-ng-model="pageSizeGeneric" data-ng-options="option for option in [10, 25, 50, 100]"></select>
										&iacute;tems por p&aacute;gina
						            	<uib-pagination
						            		ng-model="currentPageGeneric"
						            		items-per-page="pageSizeGeneric"
						            		max-size="10"
						            		total-items="genericLogs.length"
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
						        <div ng-if="genericLogs.length == 0">
						        	<h4>No hay log gen&eacute;rico del &uacute;ltimo proceso de asignaci&oacute;n de horarios a tests</h4>
						        </div>
					        </div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-default"  ng-show="isAutomaticAvaluationEvent=='1'">
				<div class="panel-heading"><a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" style="color: #000000;"><span class="glyphicon glyphicon-plus"></span> Log por alumno</a></div>
				<div id="collapseTwo" class="panel-collapse collapse">
					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<div  ng-if="studentsWithLog.length > 0">
									<div class="loading-small" ng-show="loadingStudentsWithLog">
							        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
							        </div>
									<table class="table table-striped" ng-show="!loadingStudentsWithLog">
										<tr style="background-color: #bbbbbb"><th>Cédula</th><th>Estudiante</th><th></th></tr>
							        	<tr ng-repeat="student in studentsWithLog | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-cloak">
											<td width="15%">{{student.identification}}</td>
											<td width="80%">{{student.fullName}}</td>
											<td width="5%">
												<a title="Ver log" href ng-click="showStudentLog(student)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
											</td>
										</tr>
							        </table>
							        <nav aria-label="Paginado" ng-show="!loadingStudentsWithLog">
							        	Mostrando
										<select	data-ng-model="pageSize" data-ng-options="option for option in [10, 25, 50, 100]"></select>
										&iacute;tems por p&aacute;gina
						            	<uib-pagination
						            		ng-model="currentPage"
						            		items-per-page="pageSize"
						            		max-size="10"
						            		total-items="studentsWithLog.length"
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
						        <div ng-if="studentsWithLog.length == 0">
						        	<h4>No hay log del &uacute;ltimo proceso de asignaci&oacute;n de horarios a tests</h4>
						        </div>
					        </div>
						</div>
					</div>
				</div>
			</div>
		</div>

        <!-- Modal -->
		<div class="modal" id="logModal" tabindex="-1" role="dialog" aria-labelledby="logModalTitle">
		  	<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h3 class="modal-title" id="logModalTitle"></h3>
		      		</div>
		      		<div class="modal-body">
			      		<ol ng-show="logView == 1" class="breadcrumb">
					  		<li class="active">Tests</li>
						</ol>
						<ol ng-show="logView == 2" class="breadcrumb">
					  		<li><a href ng-click="showStudentTestsLog()">Tests</a></li>
					  		<li class="active" id="breadcrumbTest"></li>
						</ol>
			      		<table ng-show="logView == 1" class="table table-striped table-link no-margin">
			      			<tr style="background-color: #e1e1e1;"><th>Asignatura</th><th>Test</th></tr>
			      			<tr ng-repeat="studentTestlog in studentTestslog" ng-click="showStudentTestLog(studentTestlog)">
			      				<td>{{studentTestlog.matterName}}</td>
			      				<td>{{studentTestlog.testName}}</td>
			      			</tr>
			      		</table>
		      			<div ng-show="logView == 2" class="panel panel-primary no-margin">
							<div class="panel-heading"><span id="testName"></span></div>
							<div class="panel-body">
								<div class="row no-margin" style="background-color:#f5f5f5; border: 1px solid #ddd; padding: 12px 0px;">
									<div class="col-lg-6"><b>Información de la jornada de evaluación</b></div>
									<div class="col-lg-6"><b>Mensaje</b></div>
								</div>
								<div style="max-height: 400px; overflow-y: auto;">
									<div ng-repeat="studentTestlog in studentTestlogs" class="bs-callout row">
										<div class="col-lg-6">
											<div><b>Centro de evaluación:</b> {{studentTestlog.evaluationCenterName}}</div>
										  	<div><b>Aula:</b> {{studentTestlog.classroomName}}</div>
											<div><b>Fecha:</b> {{studentTestlog.timeBlockStartDate | date:"EEEE d 'de' MMMM 'de' yyyy" : 'UTC'}}</div>
											<div><b>Horario:</b> {{studentTestlog.timeBlockStartDate | date:'HH:mm' : 'UTC'}} - {{studentTestlog.timeBlockEndDate | date:'HH:mm' : 'UTC'}}</div>
											<div><b>Tipos de estudiantes:</b> {{studentTestlog.timeBlockStudentTypes}}</div>
										</div>
										<div class="col-lg-6">
											<span class="text-danger">{{studentTestlog.message}}</span>
										</div>
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
		<!-- Modal -->

	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/tests-schedules-log-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>