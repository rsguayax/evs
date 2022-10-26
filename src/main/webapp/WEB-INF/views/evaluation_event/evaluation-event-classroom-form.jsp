<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<script type="text/javascript">
	var classroomsSeats = {<c:forEach var="classroom" items="${unselectedClassrooms}">'${classroom.id}': ${classroom.seats},</c:forEach>};
	var classroomsCap = {<c:forEach var="classroom" items="${unselectedClassrooms}">'${classroom.id}': [${classroom.cap.id}, '${classroom.cap.serialNumber}'],</c:forEach>};
	var classroomsNet = {<c:forEach var="classroom" items="${unselectedClassrooms}">'${classroom.id}': [${classroom.net.id}, '${classroom.net.code}'],</c:forEach>};
	var editForm = ${evaluationEventClassroom.id == null ? false : true};
	
	function updateSeatsHelpText() {
		var classroomId = $('#classroom option:selected').val();
		
		if (classroomsSeats.hasOwnProperty(classroomId)) {
			$('#seatsHelp').html('La capacidad del aula es de <b>' + classroomsSeats[classroomId] + '</b> plazas');
			if($('#seats').val() == null || $('#seats').val() == '') {
				$('#seats').val(classroomsSeats[classroomId]);
			}
		}
	}
	
	function updateDefaultCap() {
		var classroomId = $('#classroom option:selected').val();
		
		if (classroomsCap.hasOwnProperty(classroomId)) {
			if (!editForm) {
				$("#cap").val(classroomsCap[classroomId][0]);
			}
			
			$('#capHelp').html('El cap asociado por defecto al aula es el <b>' + classroomsCap[classroomId][1] + '</b>');
		}
	}

	function updateDefaultNet() {
		var classroomId = $('#classroom option:selected').val();
		
		if (classroomsNet.hasOwnProperty(classroomId)) {
			if (!editForm) {
				$("#net").val(classroomsNet[classroomId][0]);
			}
			
			$('#capHelp').html('La red asociada por defecto al aula es <b>' + classroomsCap[classroomId][1] + '</b>');
		}
	}
	
	$(function() {
		$('#classroomForm').bind('keydown', function(e) {
		    if (e.keyCode == 13) {
		        e.preventDefault();
		    }
		});
		
		$('#classroom').change(function() {
			updateSeatsHelpText();
			updateDefaultCap();
			updateDefaultNet();
		});
		
		updateSeatsHelpText();
		updateDefaultCap();
		updateDefaultNet();
	});
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<h3 class="modal-title" id="classroom-modalLabel">${evaluationEventClassroom.id == null ? "Configurar nueva aula" : "Configurar aula"}</h3>
</div>
<sf:form id="classroomForm" modelAttribute="evaluationEventClassroom">
	<div class="modal-body">
		<fieldset>
			<sf:hidden path="id" readonly="true" />
			<sf:hidden path="evaluationEventEvaluationCenter.id" readonly="true" />
			<sf:label path="classroom">Aula:</sf:label>
			<sf:select path="classroom.id" id="classroom" class="form-control">
            	<sf:options items="${unselectedClassrooms}" itemValue="id" itemLabel="name" />
            </sf:select>
			<sf:errors path="classroom" cssClass="error" />
			<br />
			<sf:label path="schedule">Horario:</sf:label>
			<sf:select path="schedule.id" id="schedule" class="form-control">
            	<sf:options items="${schedules}" itemValue="id" itemLabel="name" />
            </sf:select>
			<sf:errors path="schedule" cssClass="error" />
			<br />
			<sf:label path="cap">CAP:</sf:label>
			<sf:select path="cap.id" id="cap" class="form-control" >
				<sf:option value="">------</sf:option>
				<sf:options items="${unselectedCaps}" itemValue="id" itemLabel="serialNumber" />
			</sf:select>
			<small id="capHelp" class="text-muted"></small>
			<sf:errors path="cap" cssClass="error" />
			<br /><br />
			<sf:label path="net">Red:</sf:label>
			<sf:select path="net.id" id="net" class="form-control" >
				<sf:option value="">------</sf:option>
				<sf:options items="${unselectedNets}" itemValue="id" itemLabel="code" />
			</sf:select>
			<small id="netHelp" class="text-muted"></small>
			<sf:errors path="net" cssClass="error" />
			<br /><br />
			<sf:label path="seats">Capacidad:</sf:label>
			<sf:input path="seats" id="seats" class="form-control" type="number" />
			<small id="seatsHelp" class="text-muted"></small>
		 	<sf:errors path="seats" cssClass="error" />
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		<button type="button" class="btn btn-default" ng-click="submitClassroomForm()">Guardar</button>
	</div>
</sf:form>