<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Evento de evaluación</title>

<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active');
		$('#tabTeachers').addClass('active');
	});
</script>

</head>
<body>
	<div ng-controller="evaluationEventTeacherCtrl" ng-init="init('${evaluationEvent.id}');">
		<div class="right-buttons">
			<a href="<c:url value="/evaluationevent"/>" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span> Volver</a>
		</div>
		<div id="tabs">
			<c:import url="include/uptab.jsp" />
			<div class="tab-content container">
				<div class="tab-pane active">
		        	<div class="right-buttons">
						<button class="btn btn-default" ng-click="showAddTeacher()"><span class="glyphicon glyphicon-plus"></span> Añadir docente</button>
					</div>
				</div>
				<br/>
		        <table class="table table-striped">
					<tr style="background-color: #BBBBBB"><th>Nombre</th><th>Apellidos</th><th>Username</th><th></th></tr>
		        	<tr ng-repeat="evaluationEventTeacher in evaluationEventTeachers" class="ng-cloak">
						<td width="25%">{{evaluationEventTeacher.teacher.firstName}}</td>
						<td width="35%">{{evaluationEventTeacher.teacher.lastName}}</td>
						<td width="20%">{{evaluationEventTeacher.teacher.username}}</td>
						<td width="20%" align="right">
							<a href='#' ng-click="deleteTeacher(evaluationEventTeacher)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
						</td>
					</tr>
		        </table>
			</div>
		</div>
		
		<!-- MODAL ADD TEACHER -->
		<div class="modal" id="add-teacher-modal" tabindex="-1" role="dialog" aria-labelledby="add-teacher-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
		    	<div class="modal-content">
			      	<div class="modal-header">
			        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        	<h3 class="modal-title" id="add-teacher-modalLabel">Asignación de docentes</h3>
			      	</div>
			      	<div class="modal-body">
			      		<ui-select multiple ng-model="selectedTeachers.selected" theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
					    	<ui-select-match placeholder="Seleccione docentes...">{{$item.username}} &lt;{{$item.fullName}}&gt;</ui-select-match>
						    <ui-select-choices repeat="teacher in unselectedTeachers | propsFilter: {username: $select.search, fullName: $select.search}">
					      		<div ng-bind-html="teacher.username | highlight: $select.search"></div>
					      		<small>
						        	Nombre: <span ng-bind-html="''+teacher.fullName| highlight: $select.search"></span>
						        	Username: <span ng-bind-html="''+teacher.username| highlight: $select.search"></span>
						      </small>
						    </ui-select-choices>
					  	</ui-select>
		      		</div>
			      	<div class="modal-footer">
			        	<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			        	<button type="button" class="btn btn-default" ng-click="addTeachers()">Añadir</button>
			      	</div>
		    	</div>
	  		</div>
		</div>
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/evaluation-event-teacher-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>