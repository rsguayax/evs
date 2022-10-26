<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false"%>
<html>
<head>
<title>Administración</title>
</head>
<body>
	<div ng-controller="adminEventClassroomCtrl" ng-init="init('${eventClassroom.id}', '<fmt:formatDate value="${evaluationEvent.startDate}" pattern="yyyy-MM-dd" />', '<fmt:formatDate value="${evaluationEvent.endDate}" pattern="yyyy-MM-dd" />');">
		<h3 class="no-margin">Gestión del aula "${eventClassroom.classroom.name}" en el centro "${eventClassroom.evaluationCenter.code} - ${eventClassroom.evaluationCenter.name}"</h3>
		<div class="loading" ng-cloak>
			<img src="<c:url value="/web-resources/img/load.gif" />" />
		</div>
		<div ng-cloak>
			<div class="ui-calendar">
				<div ui-calendar="calendarConfig.calendar" ng-model="timeBlockSource" calendar="timeBlocksCalendar"></div>
				<div class="loading" ng-show="loadingTimeBlocks">
		        	<img src="<c:url value="/web-resources/img/load.gif" />" />
		        </div>
			</div>
			<div class="panel-group" ng-show="timeBlock != null">
	  			<!-- <div class="panel panel-primary" style="margin-top: 20px;">
					<div class="panel-heading"><span>Docentes</span></div>
					<div class="panel-body">
						<table style="width: 100%;">
			  				<tr>
				  				<td style="width: 100%">
							  		<ui-select multiple ng-model="selectedTimeBlockTeachers.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
								    	<ui-select-match placeholder="Seleccione docentes...">{{$item.teacher.username}} &lt;{{$item.teacher.fullName}}&gt;</ui-select-match>
									    <ui-select-choices repeat="timeBlockTeacher in unselectedTimeBlockTeachers | filter: {teacher: {fullName: $select.search}}">
								      		<div ng-bind-html="timeBlockTeacher.teacher.fullName | highlight: $select.search"></div>
								      		<small>
									        	Nombre: <span ng-bind-html="''+timeBlockTeacher.teacher.fullName| highlight: $select.search"></span>
									        	Username: <span ng-bind-html="''+timeBlockTeacher.teacher.username"></span>
									      	</small>
									    </ui-select-choices>
								  	</ui-select>
						  		</td>
					  			<td style="padding-left: 8px;">
					  				<button type="button" class="btn btn-default" ng-click="addTimeBlockTeachers(timeBlock)"><span class="glyphicon glyphicon-plus"></span> Añadir docentes</button>
				  				</td>
			  				</tr>
			  			</table>
						<br/>
						<table class="table table-striped" ng-show="timeBlockTeachers.length > 0">
							<tr style="background-color: #e1e1e1"><th>Nombre</th><th>Apellidos</th><th>Username</th><th></th></tr>
				        	<tr ng-repeat="timeBlockTeacher in timeBlockTeachers" class="ng-cloak">
								<td width="30%">{{timeBlockTeacher.teacher.firstName}}</td>
								<td width="40%">{{timeBlockTeacher.teacher.lastName}}</td>
								<td width="20%">{{timeBlockTeacher.teacher.username}}</td>
								<td width="10%">
									<a title="Eliminar de la jornada de evaluación" href ng-click="deleteTimeBlockTeacher(timeBlock, timeBlockTeacher)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
				        </table>
				        <div ng-show="timeBlockTeachers.length == 0">
				        	<h4>No hay profesores asignados a la jornada de evaluación</h4>
				        </div>
				        <div class="loading-small" ng-show="loadingTimeBlockTeachers">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
			  		</div>
		  		</div> -->
		  		<div class="panel panel-primary" style="margin-top: 20px;">
					<div class="panel-heading"><span>Estudiantes</span></div>
					<div class="panel-body">
						<table style="width: 100%; margin-bottom: 12px;">
			  				<tr>
			  					<td style="width: 100%;">
							  		<span><b>Capacidad del aula:</b> {{timeBlock.seats}}</span>
							  		<span style="margin-left: 30px;"><b>Plazas disponibles:</b> {{timeBlock.availableSeats}}/{{timeBlock.seats}} ({{timeBlock.percentageAvailableSeats }}%)</span>
							  		<span style="margin-left: 30px;"><b>Plazas ocupadas:</b> {{timeBlock.occupiedSeats}}/{{timeBlock.seats}} ({{timeBlock.percentageOccupiedSeats }}%)</span>
						  		</td>
					  			<td style="padding-left: 8px;">
									<button ng-if="timeBlock.availableSeats > 0" class="btn btn-default" ng-click="showAddStudent(timeBlock)"><span class="glyphicon glyphicon-plus"></span> Añadir estudiante</button>
				  				</td>
			  				</tr>
			  			</table>
						<table class="table table-striped" ng-show="timeBlockStudents.length > 0">
							<tr style="background-color: #e1e1e1"><th>Nombre</th><th>Apellidos</th><th>Username</th><th>C&eacute;dula</th><th></th></tr>
				        	<tr ng-repeat="timeBlockStudent in timeBlockStudents" class="ng-cloak">
								<td width="30%">{{timeBlockStudent.evaluationAssignment.user.firstName}}</td>
								<td width="30%">{{timeBlockStudent.evaluationAssignment.user.lastName}}</td>
								<td width="20%">{{timeBlockStudent.evaluationAssignment.user.username}}</td>
								<td width="15%">{{timeBlockStudent.evaluationAssignment.user.identification}}</td>
								<td width="10%">
									<a title="Tests" href ng-click="showStudentTests(timeBlockStudent)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
									<a title="Eliminar de la jornada de evaluación" href ng-click="deleteTimeBlockStudent(timeBlock, timeBlockStudent)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
				        </table>
				        <div ng-show="timeBlockStudents.length == 0">
				        	<h4>No hay estudiantes asignados a la jornada de evaluación</h4>
				        </div>
				        <div class="loading-small" ng-show="loadingTimeBlockStudents">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
			  		</div>
		  		</div>
			</div>

			<!-- MODAL STUDENT TESTS -->
			<div class="modal" id="studentTestsModal" tabindex="-1" role="dialog" aria-labelledby="studentTestsModalTitle">
			  	<div class="modal-dialog" role="document"  style="width: 1024px;">
			    	<div class="modal-content">
			      		<div class="modal-header">
			        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        		<h3 class="modal-title" id="studentTestsModalTitle">Tests</h3>
			      		</div>
			      		<div class="modal-body">
			      			<table style="width: 100%; margin-bottom: 12px;">
				  				<tr>
					  				<td style="width: 100%">
								  		<ui-select multiple ng-model="selectedStudentTests.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
									    	<ui-select-match placeholder="Seleccione los tests...">{{$item.matterName}} &lt;{{$item.testName}}&gt;</ui-select-match>
										    <ui-select-choices repeat="studentTest in unassignedStudentTests | propsFilter: {matterName: $select.search, testName: $select.search}">
									      		<div ng-bind-html="studentTest.testName | highlight: $select.search"></div>
									      		<small>
										        	Asignatura: <span ng-bind-html="''+studentTest.matterName| highlight: $select.search"></span>
										        	Test: <span ng-bind-html="''+studentTest.testName| highlight: $select.search"></span>
										      	</small>
										    </ui-select-choices>
									  	</ui-select>
							  		</td>
						  			<td style="padding-left: 8px;">
						  				<button type="button" class="btn btn-default" ng-click="addStudentTests(timeBlockStudent)"><span class="glyphicon glyphicon-plus"></span> Añadir tests</button>
					  				</td>
				  				</tr>
				  			</table>
			  			 	<table class="table table-striped" ng-show="studentTests.length > 0">
								<tr style="background-color: #e1e1e1"><th>Nombre</th><th>Asignatura</th><th></th></tr>
					        	<tr ng-repeat="studentTest in studentTests" class="ng-cloak">
									<td width="40%">{{studentTest.testName}}</td>
									<td width="50%">{{studentTest.matterName}}</td>
									<td width="10%">
										<a title="Eliminar" href ng-click="deleteStudentTest(timeBlockStudent, studentTest)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
									</td>
								</tr>
					        </table>
					        <div ng-show="studentTests.length == 0">
					        	<h4>No hay tests asignados al estudiante</h4>
					        </div>
					        <div class="loading-small" ng-show="loadingStudentTests">
					        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
					        </div>
			      		</div>
			      		<div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			      		</div>
			    	</div>
			  	</div>
			</div>

			<!-- MODAL ADD STUDENT -->
			<div class="modal" id="addStudentModal" tabindex="-1" role="dialog" aria-labelledby="addStudentModalTitle">
			  	<div class="modal-dialog" role="document"  style="width: 1024px;">
			    	<div class="modal-content">
			      		<div class="modal-header">
			        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        		<h3 class="modal-title" id="addStudentModalTitle">Añadir estudiante a la jornada de evaluación</h3>
			      		</div>
			      		<div class="modal-body">
			      			<div id="addStudentWizard" class="wizard">
								<ul class="steps-indicator">
							  		<li class="step active"><a href ng-click="showAddStudentStep(1)"><span class="step-number">1</span> Seleccionar estudiante</a></li>
							  		<li class="step disabled"><a href ng-click="showAddStudentStep(2)"><span class="step-number">2</span> Seleccionar tests</a></li>
							  		<li class="step disabled"><a href ng-click="showAddStudentStep(3)"><span class="step-number">3</span> Finalizar</a></li>
								</ul>
								<div class="steps">
									<div class="step">
										<table style="width: 100%; margin-bottom: 12px;">
							  				<tr>
								  				<td style="width: 100%">
								  					<div class="form has-feedback">
								  						<form role="search" ng-submit="searchStudents(timeBlock, studentsSearch)">
								  							<input type="text" class="form-control" placeholder="Buscar estudiante..."  size="60" ng-model="studentsSearch"  name="studentsSearch"/>
				          									<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
											  			</form>
											  		</div>
											  		<small id="searchHelp" class="text-muted">Introduzca la cadena de b&uacute;queda y pulse "Intro"</small>
										  		</td>
							  				</tr>
							  			</table>
							  			<table class="table table-striped table-vcentered" ng-show="studentsSearchResults.length > 0 && !loadingStudentsSearchResults">
								        	<tr style="background-color: #e1e1e1"><th></th><th>Nombre</th><th>Apellidos</th><th>Username</th><th class="text-center">Incluido</th></tr>
								        	<tr ng-repeat="studentSearchResult in studentsSearchResults" class="ng-cloak">
												<td width="5%">
													<input ng-if="!studentSearchResult.includedInTimeBlock" name="newStudent" type="radio" ng-click="selectNewStudent(studentSearchResult)">
												</td>
												<td width="25%">{{studentSearchResult.firstName}}</td>
												<td width="35%">{{studentSearchResult.lastName}}</td>
												<td width="20%">{{studentSearchResult.username}}</td>
												<td width="15%" class="text-center">
													<span ng-if="studentSearchResult.includedInTimeBlock" class="glyphicon glyphicon-ok-sign" style="font-size:26px; color: #739600;"></span>
													<span ng-if="!studentSearchResult.includedInTimeBlock" class="glyphicon glyphicon-remove-sign" style="font-size:26px; color: #d52b1e;"></span>
												</td>
											</tr>
								        </table>
								        <div ng-show="studentsSearchResults.length == 0 && !loadingStudentsSearchResults">
								        	<h4>No se han encontrado estudiantes</h4>
								        </div>
								        <div class="loading-small" ng-show="loadingStudentsSearchResults">
								        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
								        </div>
										<div class="right-buttons" style="margin-top: 12px;">
											<button class="btn btn-default" ng-click="nextAddStudenStep()">Siguiente <span class="glyphicon glyphicon-chevron-right"></span></button>
										</div>
									</div>
									<div class="step">
										<table class="table table-striped table-vcentered" ng-show="newStudentTests.length > 0" style="margin-top: 12px;">
								        	<tr style="background-color: #e1e1e1"><th></th><th>Nombre</th><th>Asignatura</th></tr>
								        	<tr ng-repeat="studentTest in newStudentTests" class="ng-cloak">
												<td width="5%">
													<input type="checkbox" name="newStudentTests" ng-model="studentTest.checked" ng-change="updateNewStudentTests()">
												</td>
												<td width="40%">{{studentTest.testName}}</td>
												<td width="50%">{{studentTest.matterName}}</td>
											</tr>
								        </table>
								        <div ng-show="newStudentTests.length == 0">
								        	<h4>El estudiante no tiene ningún test que no esté asignado a un horario</h4>
								        </div>
										<div class="right-buttons" style="margin-top: 12px;">
											<button class="btn btn-default" ng-click="previousAddStudenStep()"><span class="glyphicon glyphicon-chevron-left"></span> Anterior</button>
											<button class="btn btn-default" ng-click="nextAddStudenStep()">Siguiente <span class="glyphicon glyphicon-chevron-right"></span></button>
										</div>
									</div>
									<div class="step">
										<div class="bs-callout">
										  	<h4>{{newStudent.fullName}}</h4>
										  	<div class="panel panel-primary" style="margin-top: 12px;">
												<div class="panel-heading"><span>Tests</span></div>
												<div class="panel-body">
													<table class="table table-striped table-vcentered">
											        	<tr style="background-color: #e1e1e1"><th>Nombre</th><th>Asignatura</th></tr>
											        	<tr ng-repeat="studentTest in selectedNewStudentTests" class="ng-cloak">
															<td width="40%">{{studentTest.testName}}</td>
															<td width="50%">{{studentTest.matterName}}</td>
														</tr>
											        </table>
										  		</div>
									  		</div>
										</div>
										<div class="right-buttons" style="margin-top: 12px;">
											<button class="btn btn-default" ng-click="previousAddStudenStep()"><span class="glyphicon glyphicon-chevron-left"></span> Anterior</button>
											<button class="btn btn-default" ng-click="addStudent(timeBlock)"><span class="glyphicon glyphicon-plus"></span> Añadir estudiante</button>
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
		</div>
	</div>
	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/admin-event-classroom-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>