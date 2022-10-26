<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<c:if test="${edit}">
	<ul class="nav nav-tabs" style="margin-top: 10px;">	
	 		<li class="active tab-list" id="tabEvaluationEvent"><a href="<c:url value="/evaluationevent/edit/${evaluationEvent.id}"/>">Edici&oacute;n</a></li>
	 		<li class="tab-list" id="tabConfig"><a href="<c:url value="/evaluationevent/edit/${evaluationEvent.id}/config"/>">Configuraci&oacute;n</a></li>
	 		<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')">
		  		<li class="tab-list" id="tabSchedules"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/schedule"/>'>Horarios</a></li>
		  	</sec:authorize>
		  	<li class="tab-list" id="tabEvaluationCenters"><a href="<c:url value="/evaluationevent/edit/${evaluationEvent.id}/evaluationcenter"/>">Centros de evaluaci&oacute;n</a></li>
		  	
		  	<c:if test="${!evaluationEvent.admissionOrComplexiveType}">
		  	<li class="tab-list" id="tabMatters"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/matter"/>'>Asignaturas</a></li>
		  	<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'evaluation_center_supporter')">
		  		<li class="tab-list" id="tabStudents"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/student"/>'>Estudiantes</a></li>
		  	</sec:authorize>
		  	</c:if>
		  	
		  	<c:if test="${evaluationEvent.admissionOrComplexiveType}">
		  	<li class="tab-list" id="tabDegrees"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/degree"/>'>Titulaciones</a></li>
	 		<li class="tab-list" id="tabMattersTests"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/mattertest"/>'>Temáticas</a></li>
		  	<li class="tab-list" id="tabEnrollments"><a href='<c:url value="/evaluationevent/edit/${evaluationEvent.id}/enrollment"/>'>Inscripciones</a></li>
		  	</c:if> 
	</ul>
</c:if>