<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Evento de evaluaci&oacute;n</title>

<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active')
		$('#tabStudents').addClass('active')
	});
</script>

</head>
<body>
	<div ng-controller="evaluationEventStudentsCtrl" ng-init="init(${evaluationEvent.id});">
	<div class="page-header">
 		<h3>Estudiantes del evento de evaluaci&oacute;n ${evaluationEvent.name}
 			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
  			</small>
 		</h3>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div id="tabs">
				<c:import url="include/uptab.jsp" />
				<div class="tab-content container" ng-cloak>
					<div class="tab-pane active" id="tabStudents">
						<div class="row" style="margin-top: 50px;" ng-show="loading">
							<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
						</div>
						<div ng-if="existsMatterWithBankVar==1 && !loading">
							<nav class="navbar navbar-default">
			  					<div class="container-fluid">
									<form class="navbar-form navbar-left" role="search" ng-submit="getStudents()">
					        			<div class="form-inline has-feedback">
					          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="$parent.mainSearch" size="60"/>
					          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
					        			</div>
					        			<small id="searchHelp" class="text-muted">Introduzca la cadena de b&uacute;queda y pulse "Intro"</small>
					      			</form>
					      			<div class="nav navbar-nav navbar-right">
						      			<button type="button" class="btn btn-danger navbar-btn" style="margin-right: 10px;" ng-click="showLoadStudents()">
					        				<span class="glyphicon glyphicon-import" aria-hidden="true"></span> Cargar estudiantes
					        			</button>
				        			</div>
			      				</div>
			      			</nav>
					        <table class="table table-striped">
								<tr style="background-color: #bbb;">
									<th>Apellidos</th>
									<th>Nombre</th>
									<th>Nombre de usuario</th>
									<th>C&eacute;dula</th>
									<th></th>
								</tr>
					        	<tr ng-repeat="student in students" class="ng-scope">
									<td width="20%">{{student.lastName}}</td>
									<td width="20%">{{student.firstName}}</td>
									<td width="20%">{{student.username}}</td>
									<td width="20%">{{student.identification}}</td>
									<td width="20%" align="right">
										<a href="#" title="Asignaturas" ng-click="showAddMatter(student)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
										<a href="#" title="Eliminar" ng-click="deleteStudent(student)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
									</td>
								</tr>
					        </table>
					        <nav aria-label="Paginado">
					        	<!--
					        	Mostrando
								<select	data-ng-model="pageSize" data-ng-options="option for option in [10, 25, 50, 100]"></select>
								&iacute;tems por p&aacute;gina
								-->
				            	<uib-pagination
				            		ng-model="currentPage"
				            		items-per-page="pageSize"
				            		max-size="10"
				            		total-items="totalItems"
				            		ng-change="pageChanged()"
				            		direction-links="true"
				            		boundary-links="true"
				            		first-text="&laquo"
				            		previous-text="&lsaquo;"
				            		next-text="&rsaquo;"
				            		last-text="&raquo;"
			            			class="pagination-sm pull-right"
			            			style="margin: 0;"></uib-pagination>
			            	</nav>
					    </div>
					    <div ng-if="existsMatterWithBankVar==0 && !loading">
					    	<nav class="navbar navbar-default">
			  					<div class="container-fluid">
									<p style="margin: 10px 0 0; text-align: center">No existe ninguna asignatura con un banco asociado, realice antes esta asignación.</p>
			      				</div>
			      			</nav>
					    </div>
					</div>
				</div>
			</div>
		</div>
	</div>

    <!-- MODAL ESTUDIANTES -->
	<div class="modal" id="add-student-modal" tabindex="-1" role="dialog" aria-labelledby="add-students-modalLabel">
	  <div class="modal-dialog" role="document"  style="width: 1024px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="add-students-modalLabel">Asignaci&oacute;n de estudiantes</h3>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
					  <ui-select multiple ng-model="selectedStudents.selected" theme="bootstrap" ng-disabled="disabled" sortable="true"
					  				close-on-select="false">
					    <ui-select-match placeholder="{{selectLabel}}">{{$item.username}} &lt;{{$item.fullName}}&gt;</ui-select-match>
					    <ui-select-choices repeat="student in allstudents | propsFilter: {username: $select.search, fullName: $select.search} | limitTo: 50">
					      <div ng-bind-html="student.username | highlight: $select.search"></div>
					      <small>
					        Nombre: <span ng-bind-html="''+student.fullName| highlight: $select.search"></span>
					        Username: <span ng-bind-html="''+student.username| highlight: $select.search"></span>
					      </small>
					    </ui-select-choices>
					  </ui-select>
				</div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button type="button" class="btn btn-default" ng-click="addStudents()">A&ntilde;adir</button>
	      </div>
	    </div>
	  </div>
	</div>


	<!-- MODAL MATERIAS -->
	<div class="modal" id="add-matter-modal" tabindex="-1" role="dialog" aria-labelledby="add-matters-modalLabel">
	  <div class="modal-dialog" role="document"  style="width: 1024px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="add-matters-modalLabel">Asignaci&oacute;n de asignaturas a {{studentSelected.firstName}} {{studentSelected.lastName}}</h3>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
					  <ui-select multiple ng-model="selectedMatters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true"
					  				close-on-select="false">
					    <ui-select-match placeholder="{{selectLabel}}">{{$item.matter.name}}; {{$item.academicPeriod.name}}; {{$item.mode.name}}</ui-select-match>
					    <ui-select-choices repeat="matter in allmatters | nestedPropsFilter: [{'obj': 'matter', 'prop': 'name', 'value': $select.search}, {'obj': 'academicPeriod', 'prop': 'name', 'value': $select.search}] | limitTo: 50">
					      <div ng-bind-html="matter.name | highlight: $select.search"></div>
					      <small>
					        Nombre: <span ng-bind-html="''+matter.matter.name| highlight: $select.search">; </span>
					        P. académico: <span ng-bind-html="''+matter.academicPeriod.name| highlight: $select.search">; </span>
							Modalidad: <span ng-bind-html="''+matter.mode.name| highlight: $select.search"></span>
					      </small>
					    </ui-select-choices>
					  </ui-select>
				</div>
	        </div>
	        <div class="row" style="margin-top: 20px" ng-show="loadingMatters"><p align="center"><img src="<c:url value="/web-resources/img/load.gif" />"></p></div>
	        <div class="spacer-40"></div>
	      	<div class="row" ng-show="!loadingMatters && matters.length" ng-cloak>
	        	<div class="col-md-12">
					<table class="table table-striped">
						<tr style="background-color: #BBBBBB"><th>Nombre</th><th>Per&iacute;odo acad&eacute;mico</th><th>Modalidad</th><th>Nivel acad&eacute;mico</th><th></th></tr>
			        	<tr ng-repeat="matter in matters | orderBy:'name'" class="ng-scope" >
							<td width="20%">{{matter.matter.name}}</td>
							<td width="20%">{{matter.academicPeriod.name}}</td>
							<td width="20%">{{matter.mode.name}}</td>
							<td width="20%">{{matter.matter.academicLevel.name}}</td>
							<td width="20%" align="right">
								<a href='#' ng-click="showMatterTests(matter)" title="Tests"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
								<a href='#' ng-click="deleteStudentMatter(matter)" title="Eliminar"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
							</td>
						</tr>
			        </table>
		        </div>
		     </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button type="button" class="btn btn-default" ng-click="addMatters()">A&ntilde;adir</button>
	      </div>
	    </div>
	  </div>
    </div>


    <!-- MODAL TESTS -->
	<div class="modal" id="tests-modal" tabindex="-1" role="dialog" aria-labelledby="add-matters-modalLabel">
	  <div class="modal-dialog" role="document"  style="width: 1024px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="tests-modalLabel">Tests de {{matterSelected.matter.name}} - {{matterSelected.academicPeriod.name}} - {{matterSelected.mode.name}} </h3>
	      </div>
	      <div class="modal-body">
	      	<div class="row" style="margin-top: 20px" ng-show="loadingTests"><p align="center"><img src="<c:url value="/web-resources/img/load.gif" />"></p></div>
	        <div class="row"  ng-show="!loadingTests && tests.length" ng-cloak>
	        	<div class="col-md-12">
					<table class="table table-striped">
						<tr style="background-color: #BBBBBB"><th>Nombre</th><th>Banco</th><th>T. evaluación</th></tr>
			        	<tr ng-repeat="test in tests | orderBy:'name'" class="ng-scope" >
							<td width="40%">{{test.name}}</td>
							<td width="30%">{{test.bankName}}</td>
							<td width="30%">{{test.evaluationType.name}}</td>
						</tr>
			        </table>
		        </div>
		     </div>
		     <div class="row" style="margin-top: 20px" ng-show="!loadingTests && tests.length == 0" ng-cloak>
		     	<p align="center">-- Sin resultados --</p>
		     </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	      </div>
	    </div>
	  </div>
    </div>


    <!-- MODAL CARGA -->
    <div class="modal" id="load-students-modal" tabindex="-1" role="dialog" aria-labelledby="load-students-modalLabel">
	  <div class="modal-dialog" role="document"  style="width: 1024px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="load-students-modalLabel">Carga masiva de estudiantes</h3>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
	      		<div class="col-md-12">
	      			<form class="form-inline">
		      		 	 <label for="academicPeriodLoad">Per&iacute;odo:</label>
				  		 <select name="academicPeriodLoad"  id="academicPeriodLoad" class="form-control">
						    <c:forEach items="${academicPeriods}" var="ap">
						        <option value="<c:out value="${ap.code}"/>">
						            <c:out value="${ap.name}"/>
						        </option>
						    </c:forEach>
						  </select>
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for="academicLevelLoad">Nivel:</label>
						  <select name="academicLevelLoad"  id="academicLevelLoad" class="form-control">
						    <c:forEach items="${academicLevels}" var="al">
						        <option value="<c:out value="${al.code}"/>">
						            <c:out value="${al.name}"/>
						        </option>
						    </c:forEach>
						  </select>
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for="modeLoad">Modalidad</label>
						  <select name="modeLoad"  id="modeLoad" class="form-control">
						    <c:forEach items="${modes}" var="m">
						        <option value="<c:out value="${m.code}"/>">
						            <c:out value="${m.name}"/>
						        </option>
						    </c:forEach>
						  </select>
					</form>
				</div>
			</div>
			<div class="spacer-30"></div>
			<div class="row">
				<div class="col-md-12">
				  	<uib-progressbar class="progress-striped active" value="studentsProgress" type="success">
				  		<i>{{studentsProgress}}%	</i>
				  	</uib-progressbar>
				</div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button type="button" class="btn btn-default" ng-click="loadAllStudents()" ng-disabled="showLoadBtn==0">Cargar</button>
	      </div>
	    </div>
	  </div>
	</div>
	</div>

	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/evaluation-event-students-ctrl.js" />"></script>
	</content>
</body>
</html>