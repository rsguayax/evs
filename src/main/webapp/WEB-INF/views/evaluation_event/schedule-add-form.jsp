<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(function() {
		$('#scheduleForm').bind('keydown', function(e) {
		    if (e.keyCode == 13) {
		        e.preventDefault();
		    }
		});
	});
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<h3 class="modal-title" id="add-schedule-modalLabel">Nuevo horario</h3>
</div>
<div class="modal-body">
	<sf:form id="scheduleForm" modelAttribute="schedule">
		<sf:hidden path="evaluationEvent.id" readonly="true" />
		<div class="form-group">
			<sf:label path="name">Nombre</sf:label>
			<sf:input path="name" id="name" class="form-control" /> <sf:errors path="name" cssClass="error" />
		</div>
	</sf:form>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	<button type="button" class="btn btn-default" ng-click="submitScheduleForm()">Guardar</button>
</div>
