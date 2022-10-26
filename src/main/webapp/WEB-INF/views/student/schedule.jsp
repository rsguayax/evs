<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Horario</title>
</head>
<body>
	<p>
		Estimado Estudiante: <strong>${userFullName}</strong>
	</p>

	<p>
		Los horarios de las evaluaciones presenciales correspondientes a <strong>${evaluationEventName}</strong> son los siguientes:
	</p>

	<p>
		<strong>Horarios</strong>
	</p>

	<c:choose>
    	<c:when test="${not empty rows}">
    		<div class="table-responsive">
				<table class="table table-striped table-condensed">
					<thead>
						<tr>
							<th>Asignatura</th>
							<th>Tipo Evaluaci&oacute;n</th>
							<th>Fecha</th>
							<th>Hora</th>
							<th>Lugar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rows}" var="row">
			    		<tr>
							<td><strong>${row[0]}</strong></td>
							<td>${row[1]}</td>
							<td>${row[2]}</td>
							<td>${row[3]}</td>
							<td>${row[4]}</td>
						</tr>
						</c:forEach>
					<tbody>
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<p>No hay.</p>
		</c:otherwise>
	</c:choose>

	<p>
		<strong>Nota:</strong> Asistir puntualmente y presentarse con su TABLET
		entregada por la universidad, con bater&iacute;a cargada al 100% y el cargador.
		Adem&aacute;s portar c&eacute;dula de identidad (original), traer: esferogr&aacute;fico, l&aacute;piz 2b y
		borrador blanco.
	</p>

	<p>Agradecemos su participaci&oacute;n en este proceso de evaluaci&oacute;n en l&iacute;nea.</p>

	<p>Cordialmente,</p>

	<p>
		<strong>VICERRECTORADO DE MODALIDAD A DISTANCIA</strong>
	</p>
</body>
</html>