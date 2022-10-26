<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false"%>
<html>
<head>
<title>Horario '${schedule.name}'</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.css" />"/>
	<script type="text/javascript" src="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.min.js" />" charset="utf-8"></script>

	<script type="text/javascript">
		$(function() {
			$.datetimepicker.setLocale('es');
			$('#startDate, #endDate').datetimepicker({
				format:'d-m-Y H:i',
				dayOfWeekStart: 1,
				step: 30,
				minDate: '<fmt:formatDate value="${evaluationEvent.startDate}" pattern="yyyy/MM/dd" />',
				maxDate: '<fmt:formatDate value="${evaluationEvent.endDate}" pattern="yyyy/MM/dd" />',
				onChangeDateTime: function(currentTime, $input) {
					if ($.trim($input.val()).length > 0 && !moment($input.val(), 'DD-MM-YYYY HH:mm', true).isValid()) {
						$input.val('');
						alert('Introduzca una fecha v\u00e1lida');
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
	<div ng-controller="scheduleEditCtrl" ng-init="init(${schedule.id}, '<fmt:formatDate value="${evaluationEvent.startDate}" pattern="yyyy-MM-dd" />', '<fmt:formatDate value="${evaluationEvent.endDate}" pattern="yyyy-MM-dd" />')">

		<div class="page-header">
	 		<h3>Horario '${schedule.name}'
	 			<small>
			  		<a href="<s:url value="/evaluationevent/edit/${evaluationEvent.id}/schedule"/>" class="pull-right">
			  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
			  		</a>
	  			</small>
	 		</h3>
		</div>
		<div class="row">
			<div class="col-md-12">
				<sf:form modelAttribute="schedule">
					<sf:hidden path="id" readonly="true" />
					<sf:hidden path="evaluationEvent.id" readonly="true" />
					<div class="form-group">
						<sf:label for="name" path="name">Nombre</sf:label>
						<sf:input id="name" path="name" class="form-control" /> <sf:errors path="name" cssClass="error" />
					</div>
					<button type="submit" class="btn btn-default">Guardar</button>
				</sf:form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="ui-calendar">
					<div ui-calendar="calendarConfig.calendar" ng-model="timeBlockSource" calendar="timeBlocksCalendar"></div>
				</div>
			</div>
		</div>

		<!-- MODAL TIME BLOCK FORM -->
		<div class="modal" id="time-block-form-modal" tabindex="-1" role="dialog" aria-labelledby="timeBlockFormTitle">
		  	<div class="modal-dialog" role="document" style="width: 1024px;">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h3 class="modal-title" id="timeBlockFormTitle">Nueva jornada de evaluación</h3>
		      		</div>
		      		<div class="modal-body">
		  			 	<form id="timeBlockForm" method="post">
	  			 			<input id="timeBlockId" name="id" value="" type="hidden" readonly="true" />
		  			 			<input id="scheduleId" name="schedule.id" value="${schedule.id}" type="hidden" readonly="true" />
		  			 			<input id="updateAll" name="updateAll" value="" type="hidden" readonly="true" />

		  			 			<div class="form-group">
	  			 				<label for="startDate">Inicio</label>
	  			 				<input id="startDate" name="startDate" class="form-control" type="datetime" />
	  			 			</div>

	  			 			<div class="form-group">
	  			 				<label for="endDate">Fin</label>
	  			 				<input id="endDate" name="endDate" class="form-control" type="datetime" />
	  			 			</div>

	  			 			<div class="form-group">
		  			 			<label for="studentTypes">Tipos de estudiantes</label>
		  			 			<select id="studentTypes" name="studentTypes" class="form-control" multiple="multiple">
	  			 				<c:forEach items="${studentTypes}" var="studentType">
						        	<option id="studentType${studentType.id}" value="${studentType.id}">${studentType.value}</option>
								</c:forEach>
		  			 			</select>
	  			 			</div>
		  			 	</form>
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
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/schedule-edit-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>
