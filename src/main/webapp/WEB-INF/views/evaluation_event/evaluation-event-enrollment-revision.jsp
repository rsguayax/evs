<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<link
	href="https://unpkg.com/bootstrap-table@1.18.3/dist/bootstrap-table.min.css"
	rel="stylesheet">

<script
	src="https://unpkg.com/tableexport.jquery.plugin/tableExport.min.js"></script>
<script
	src="https://unpkg.com/bootstrap-table@1.18.3/dist/bootstrap-table.min.js"></script>
<script
	src="https://unpkg.com/bootstrap-table@1.18.3/dist/bootstrap-table-locale-all.min.js"></script>
<script
	src="https://unpkg.com/bootstrap-table@1.18.3/dist/extensions/export/bootstrap-table-export.min.js"></script>
<html>
<head>
<title>Evento de evaluaci&oacute;n</title>
<style type="text/css">
.popover-title {
	font-size: 1.3em;
	font-width: bold;
	text-align: center;
}

*:focus, *:hover {
	outline: none;
	text-decoration: none;
}

a:-webkit-any-link {
	text-decoration: none;
}

.progress {
	height: 3em;
}

.progress i {
	line-height: 3.5em;
}

.progress-bar {
	-webkit-transition: width 1s ease-in-out;
	transition: width 1s ease-in-out;
}

#degreeSelect .ui-select-choices {
	min-width: 800px;
}
</style>
<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active');
		//$('#tabEnrollmentRevision').addClass('active');
		$('#evaluation-event-degree-listt').select2();
	});
</script>
</head>
<body>
	<div ng-controller="evaluationEventEnrollmentRevisionCtrl"
		ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
			<h3>
				Revisi&oacute;n evento de evaluaci&oacute;n ${evaluationEvent.name}
				<small> <a href="<c:url value="/evaluationevent" />"
					class="pull-right"> <span
						class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
						Volver
				</a>
				</small>
			</h3>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div id="tabs">
					<div class="tab-content container">
						<div class="tab-pane active" id="tabEnrollmentRevision">

							<div class="row" style="margin-top: 50px;" ng-show="loading">
								<p align="center">
									<img src="<c:url value="/web-resources/img/load-small.gif" />">
								</p>
							</div>
							<div class="row" ng-show="!loading" ng-cloak>
								<nav class="navbar navbar-default">
									<div class="container-fluid">
										<form class="navbar-form navbar-left" role="search">
											<div class="form-inline has-feedback">
												<input type="text" class="form-control"
													placeholder="B&uacute;squeda" ng-model="filterFormData.search" ng-keyup="filterEnrollmentRevisions()" />
												<span
													class="glyphicon glyphicon-search form-control-feedback"
													aria-hidden="true"></span>
											</div>
										</form>
										<div class="nav navbar-nav navbar-right">
											<div>
<!-- 												<button type="button" class="btn btn-info" ng-click="generateRandomGrades()"> -->
<%-- 													<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Generar notas aleatorias --%>
<!-- 												</button> -->
<!-- 												<button type="button" class="btn btn-danger" ng-click="deleteEnrollmentsRevision()"> -->
<%-- 													<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Eliminar revisión --%>
<!-- 												</button> -->
												<button type="button" class="btn btn-warning" ng-click="newEnrollmentsRevision()">
													<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Nueva Revisión
												</button>
												<button type="button" class="btn btn-success navbar-btn"
													ng-click="printReport()">
													<span class="glyphicon glyphicon-print"
														aria-hidden="true"></span> Reporte
												</button>
													<button type="button" class="btn btn-danger navbar-btn"
													ng-click="closeEvent()">
													<span class="glyphicon glyphicon-folder-close"
														aria-hidden="true"></span> Cerrar Evento de Evaluación
												</button>									
											</div>
										</div>
									</div>
								</nav>

								<nav class="navbar navbar-default">
									<div class="container-fluid">
										<form class="navbar-form navbar-left" role="search">
											<div class="form-inline has-feedback">
												<ui-select id="degreeSelect" ng-model="filterFormData.degree"
													theme="bootstrap" ng-change="filterEnrollmentRevisions()"> <ui-select-match
													placeholder="Seleccione Titulación..">{{$select.selected.degree.name}}</ui-select-match>
												<ui-select-choices repeat="degree in degrees">
												<div ng-bind-html="degree.degree.name | highlight: $select.search"></div>
												<small>
										        	Nivel acad&eacute;mico: <span ng-bind-html="''+degree.degree.academicLevel | highlight: $select.search"></span>
										        	Modalidad: <span ng-bind-html="''+degree.degree.mode | highlight: $select.search"></span>
										        	C&oacute;digo: <span ng-bind-html="''+degree.degree.code | highlight: $select.search"></span>
										      	</small>
												</ui-select-choices> </ui-select>
											</div>
										</form>
											
										<div class="nav navbar-nav navbar-right">
											<button type="button" class="btn btn-info" ng-click="clearFilter()">
												<span class="glyphicon glyphicon-erase" aria-hidden="true"></span> Limpiar filtro
											</button>
											<button type="button" class="btn btn-success navbar-btn" ng-click="printReportByDegree()">
												<span class="glyphicon glyphicon-print" aria-hidden="true"></span> Imprimir filtro
											</button>										
										</div>
									</div>
								</nav>

								<table class="table table-striped">
									<tr style="background-color: #bbb;">
										<th>Nombre</th>
										<th>Identificación</th>
										<th>Titulación</th>
										<th class="text-center">Prioridad</th>
										<th class="text-center">Revisión</th>
										<th>Estado</th>
										<th>Nota</th>
										<th></th>
									</tr>
									<tr >
								        <td ng-show="loadingData" colspan="9">
								            Cargando informacion...          
								        </td>
								    </tr>
								    <tr >
								        <td ng-show="filteredEnrollmentRevisions.length==0" colspan="9">
								            No hay información          
								        </td>
								    </tr>
									<tr ng-repeat="enrollmentRevision in filteredEnrollmentRevisions | orderBy: 'enrollment.id' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
										<td width="30%">{{enrollmentRevision.userFullName}}</td>
										<td width="10%">{{enrollmentRevision.userIdentification}}</td>
										<td width="15%">{{enrollmentRevision.degreeName}}</td>
										<td width="10%" class="text-center">{{enrollmentRevision.priority}}</td>
										<td width="10%" class="text-center">{{enrollmentRevision.revision}}</td>
										<td width="10%">{{enrollmentRevision.statusString}}</td>
										<td width="10%">{{enrollmentRevision.grade}}</td>
										<td width="5%" align="right">
											<a href='#' title="Nueva Revisión" ng-click="viewModalEditEnrollmentRevision(enrollmentRevision)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
										</td>
									</tr>
								</table>

								<nav aria-label="Paginado">
									Mostrando <select data-ng-model="pageSize"
										data-ng-options="option for option in [10, 25, 50, 100]"></select>
									&iacute;tems por p&aacute;gina
									<uib-pagination ng-model="currentPage"
										items-per-page="pageSize" max-size="10"
										total-items="filteredEnrollmentRevisions.length" direction-links="true"
										boundary-links="true" first-text="&laquo"
										previous-text="&lsaquo;" next-text="&rsaquo;"
										last-text="&raquo;" class="pagination-sm pull-right"
										style="margin: 0;"></uib-pagination>
								</nav>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>

		<!-- Formulario de edición de  revision de inscripciones-->

		<div class="modal" id="add-enrollment-revision-modal" tabindex="-1"
			role="dialog" aria-labelledby="add-degrees-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-degrees-modalLabel">Revisión de inscripci&oacute;n</h3>
					</div>
					<div class="modal-body">
						<div class="row form-group">
							<div class="col-md-12">
								<label for="fullNameLoad">Nombre y Apellidos</label> <input
									type="text"
									class="form-control"
									ng-model="enrollmentObj.userFullName"
									readonly=»readonly»></input>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-md-12">
								<label for="identificationLoad">Identificación</label> <input
									type="text"
									class="form-control"
									ng-model="enrollmentObj.userIdentification"
									readonly=»readonly»></input>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-md-12">
								<label for="degreeLoad">Titulación</label> <input type="text"
									class="form-control"
									ng-model="enrollmentObj.degreeName"
									readonly=»readonly»></input>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-md-12">
								<label for="statusLoad">Estado</label>
								<ui-select ng-model="status.selected" theme="bootstrap">
								<ui-select-match placeholder="Seleccione estado">{{$select.selected.name}}</ui-select-match>
								<ui-select-choices
									repeat="status in status | propsFilter: {name: $select.search, id: $select.search}">
								<div ng-bind-html="status.name | highlight: $select.search"></div>
								</ui-select-choices> </ui-select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default"
							ng-click="editEnrollmentRevision(enrollmentObj, status.selected)">Guardar</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<content tag="local_script"> <script
		src="<c:url value="/web-resources/js/controller/evaluation-event-enrollment-revision-ctrl.js" />"></script>
	</content>

	<!-- MODAL WAIT -->
	<div class="modal fade bs-example-modal-sm" id="waiting-modal"
		tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">
						<span class="glyphicon glyphicon-time"></span> <span
							id="waiting-message"></span> Actualizando información
					</h4>
				</div>
				<div class="modal-body">
					<div class="progress">
						<div class="progress-bar progress-bar-striped active"
							style="width: 100%"></div>
					</div>
					<sub><strong>Nota:</strong> El proceso puede tardar varios
						minutos</sub>
				</div>
			</div>
		</div>
	</div>
</body>
</html>