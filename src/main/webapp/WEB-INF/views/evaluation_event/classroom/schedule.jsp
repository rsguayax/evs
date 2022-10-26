<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false"%>
<html>
<head>
<title>Horario</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.css" />"/>
<script type="text/javascript" src="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.min.js" />" charset="utf-8"></script>

<script type="text/javascript">
	$(function() {
		$.datetimepicker.setLocale('es');
		$('#startDate, #endDate').datetimepicker({
			format:'d-m-Y H:i',
			dayOfWeekStart: 1,
			step: 30,
			minDate: '<fmt:formatDate value="${eventClassroom.evaluationEvent.startDate}" pattern="yyyy/MM/dd" />',
			maxDate: '<fmt:formatDate value="${eventClassroom.evaluationEvent.endDate}" pattern="yyyy/MM/dd" />',
			onChangeDateTime: function(currentTime, $input) {
				if ($.trim($input.val()).length > 0 && !moment($input.val(), 'DD-MM-YYYY HH:mm', true).isValid()) {
					$input.val('');
					alert('Introduzca una fecha válida');
				}
		  	}
		});
		
		$('#timeBlockForm').bind('keydown', function(e) {
		    if (e.keyCode == 13) {
		        e.preventDefault();
		    }
		});
	});
</script>

</head>
<body>
	<div ng-controller="classroomTimeBlockCtrl" ng-init="init('${eventClassroom.id}', '<fmt:formatDate value="${eventClassroom.evaluationEvent.startDate}" pattern="yyyy-MM-dd" />', '<fmt:formatDate value="${eventClassroom.evaluationEvent.endDate}" pattern="yyyy-MM-dd" />', '${availableState.id}')">
		<div class="right-buttons">
			<a href="<s:url value="/evaluationevent/edit/${eventClassroom.evaluationEvent.id}/evaluationcenter"/>" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span> Volver</a>
		</div>
		<div>	
			<h3>Horario del aula "${eventClassroom.classroom.name}" en el centro "${eventClassroom.evaluationCenter.code} - ${eventClassroom.evaluationCenter.name}"</h3>
			<div class="ui-calendar">
				<div ui-calendar="calendarConfig.calendar" ng-model="timeBlockSource" calendar="timeBlocksCalendar"></div>
				<div class="loading" ng-show="loadingTimeBlocks">
		        	<img src="<c:url value="/web-resources/img/load.gif" />" />
		        </div>
			</div>
		</div>
		
		<!-- MODAL TIME BLOCK FORM -->
		<div class="modal" id="time-block-form-modal" tabindex="-1" role="dialog" aria-labelledby="timeBlockFormTitle">
		  	<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h3 class="modal-title" id="timeBlockFormTitle">Nueva jornada de evaluación</h3>
		      		</div>
		      		<div class="modal-body">
		  			 	<form id="timeBlockForm" method="post">
		  			 		<fieldset>
		  			 			<input name="id" type="hidden" readonly="true" ng-value="timeBlock.id" />
		  			 			<input name="timeBlock.id" type="hidden" readonly="true" ng-value="timeBlock.timeBlockId" />
		  			 			<input name="availableState.id" type="hidden" readonly="true" ng-value="timeBlock.availableStateId" />
		  			 			<input name="seats" value="${eventClassroom.seats}" type="hidden" readonly="true" />
 		  			 			<input name="evaluationEventClassroom.id" value="${eventClassroom.id}" type="hidden" readonly="true" />
		  			 			<label for="startDate">Inicio:</label>
		  			 			<input id="startDate" name="startDate" class="form-control" type="datetime" ng-model="timeBlock.startDate" />
		  			 			<br />
		  			 			<label for="endDate">Fin:</label>
		  			 			<input id="endDate" name="endDate" class="form-control" type="datetime" ng-model="timeBlock.endDate" />
		  			 			<br />
		  			 			<label for="studentTypes">Tipos de estudiantes:</label>
		  			 			<select id="studentTypes" name="studentTypes" class="form-control" multiple="multiple" ng-model="timeBlock.studentTypes">
	  			 				<c:forEach items="${studentTypes}" var="studentType">     
						        	<option value="${studentType.id}">${studentType.value}</option>
								</c:forEach>
		  			 			</select>
		  			 			<select name="evaluationEventTeachers" class="form-control" multiple="multiple" ng-model="timeBlock.evaluationEventTeachers" ng-show="false">
	  			 					<option ng-repeat="timeBlockTeacher in timeBlockTeachers" value="{{timeBlockTeacher.id}}">{{timeBlockTeacher.teacher.fullName}}</option>
		  			 			</select>
		  			 		</fieldset>
		  			 	</form>
 		  			 	<div id="teachers" class="panel panel-primary" style="margin-top: 26px;">
					  		<div class="panel-heading">Docentes</div>
						  	<div class="panel-body">
					  			<table style="width: 100%;">
					  				<tr>
						  				<td style="width: 100%">
									  		<ui-select multiple ng-model="selectedTeachers.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
										    	<ui-select-match placeholder="Seleccione docentes...">{{$item.teacher.username}} &lt;{{$item.teacher.fullName}}&gt;</ui-select-match>
											    <ui-select-choices repeat="teacher in unselectedTeachers | filter: {teacher: {fullName: $select.search}}">
										      		<div ng-bind-html="teacher.teacher.fullName | highlight: $select.search"></div>
										      		<small>
											        	Nombre: <span ng-bind-html="''+teacher.teacher.fullName| highlight: $select.search"></span>
											        	Username: <span ng-bind-html="''+teacher.teacher.username"></span>
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
										<td width="25%">{{timeBlockTeacher.teacher.firstName}}</td>
										<td width="35%">{{timeBlockTeacher.teacher.lastName}}</td>
										<td width="20%">{{timeBlockTeacher.teacher.username}}</td>
										<td width="20%" align="right">
											<a href='#' ng-click="deleteTimeBlockTeacher(timeBlock, timeBlockTeacher)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
										</td>
									</tr>
						        </table>
						        <div ng-show="timeBlockTeachers.length == 0">
						        	<h4>No hay docentes asignados a la jornada de evaluación</h4>
						        </div>
						        <div class="loading-small" ng-show="loadingTimeBlockTeachers">
						        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
						        </div>
						  	</div>
						</div>
		      		</div>
		      		<div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
				        <button type="button" class="btn btn-default" ng-click="submitTimeBlockForm()">Guardar</button>
		      		</div>
		    	</div>
		  	</div>
		</div>
	</div>
	
	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/classroom-time-block-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>
