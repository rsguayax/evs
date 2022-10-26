<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Evento de evaluaci&oacute;n</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.css" />" />
<script type="text/javascript"
	src="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.min.js" />"
	charset="utf-8"></script>

<script type="text/javascript">
	$(function() {
		$.datetimepicker.setLocale('es');
		$('#startDate, #endDate').datetimepicker(
				{
					format : 'd-m-Y H:i',
					dayOfWeekStart : 1,
					step : 30,
					onChangeDateTime : function(currentTime, $input) {
						if ($.trim($input.val()).length > 0
								&& !moment($input.val(), 'DD-MM-YYYY HH:mm',
										true).isValid()) {
							$input.val('');
							alert('Introduzca una fecha válida');
						}
					}
				});

		$('li.tab-list').removeClass('active');
		$('#tabEvaluationEvent').addClass('active');
		$('#evaluation-types-list').select2();
		$('#evaluation-event-types-list').select2();

	});
</script>

</head>
<body>
	<div class="page-header">
		<h3>
			Edici&oacute;n del evento de evaluaci&oacute;n
			${evaluationEvent.name} <small> <a
				href="<c:url value="/evaluationevent" />" class="pull-right"> <span
					class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
					Volver
			</a>
			</small>
		</h3>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div id="tabs">
				<c:import url="include/uptab.jsp" />
				<div class="tab-content container">
					<div class="tab-pane active">
						<sf:form modelAttribute="evaluationEvent">
							<div class="form-group">
								<sf:label path="evaluationEventTypes">Tipo de evento de evaluaci&oacute;n</sf:label>
								<sf:select path="evaluationEventTypes.id"
									items="${evaluationEventTypes}" itemLabel="name"
									itemValue="idAsString" class="form-control"
									id="evaluation-event-types-list">
								</sf:select>
								<sf:errors path="evaluationEventTypes" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="code">C&oacute;digo</sf:label>
								<sf:input path="code" id="code" class="form-control" /> <sf:errors path="code" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="name">Nombre</sf:label>
								<sf:input path="name" id="name" class="form-control" />
								<sf:errors path="name" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="startDate">Fecha de inicio</sf:label>
								<sf:input path="startDate" id="startDate" class="form-control"
									type="datetime" />
								<sf:errors path="startDate" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="endDate">Fecha de fin</sf:label>
								<sf:input path="endDate" id="endDate" class="form-control"
									type="datetime" />
								<sf:errors path="endDate" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="testDuration">Duraci&oacute;n del examen (minutos)</sf:label>
								<sf:input path="testDuration" id="testDuration"
									class="form-control" type="number" />
								<sf:errors path="testDuration" cssClass="error" />
							</div>
							<div class="form-group">
								<sf:label path="comment">Observaci&oacute;n</sf:label>
								<sf:textarea path="comment" id="comment" class="form-control" />
								<sf:errors path="comment" cssClass="error" />
								<sf:hidden path="id" readonly="true" />
							</div>
							<div class="form-group">
								<sf:label path="evaluationTypes">Etiquetas del Evento</sf:label>
								<sf:select path="evaluationTypes" multiple="true" 
									items="${evaluationTypes}" itemLabel="name"
									itemValue="idAsString" class="form-control"
									id="evaluation-types-list">
								</sf:select>
								<sf:errors path="evaluationTypes" cssClass="error" />
							</div>

							<button type="submit" class="btn btn-default">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>
								Guardar
							</button>
						</sf:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>