<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Servidores</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.css" />" />
	<script type="text/javascript" src="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.min.js" />" charset="utf-8"></script>
	<script type="text/javascript">
		$(function() {
			$.datetimepicker.setLocale('es');
			$('#initDate, #endDate').datetimepicker({
				format:'d-m-Y H:i',
				dayOfWeekStart: 1,
				step: 30,
				onChangeDateTime: function(currentTime, $input) {
					if ($.trim($input.val()).length > 0 && !moment($input.val(), 'DD-MM-YYYY HH:mm', true).isValid()) {
						$input.val('');
						alert('Introduzca una fecha v\u00e1lida');
					}
			  	}
			});

		});
	</script>
</head>
<body>
	<div ng-controller="serverCtrl" ng-init="init(${server.id});">
		<div class="page-header">
			<h3>${headText}
				<small> <a href="<c:url value="/server" />" class="pull-right"> <span
						class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
				</a>
				</small>
			</h3>
		</div>
		<div class="row">
			<div class="col-md-12">
				<sf:form modelAttribute="server">
					<sf:hidden path="id" readonly="true" />
					<div class="form-group">
						<sf:label path="name">Nombre</sf:label>
						<sf:input path="name" id="name" class="form-control"/>
						<sf:errors path="name" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="code">C&oacute;digo</sf:label>
						<sf:input path="code" id="code" class="form-control" />
						<sf:errors path="code" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="url">URL</sf:label>
						<sf:input path="url" class="form-control"/>
						<sf:errors path="url" cssClass="error" />
					</div>
					<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-saved" aria-hidden="true"></span> Guardar</button>					
				</sf:form>
			</div>
		</div>
	</div>


	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/server-ctrl.js" />"></script>
	</content>
</body>
</html>