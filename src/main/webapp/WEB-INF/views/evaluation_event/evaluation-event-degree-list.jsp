<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
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
</style>
<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active');
		$('#tabDegrees').addClass('active');
	});
</script>
</head>
<body>
	<div ng-controller="evaluationEventDegreesCtrl"
		ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
			<h3>
				Titulaciones del evento de evaluaci&oacute;n ${evaluationEvent.name}
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
					<c:import url="include/uptab.jsp" />
					<div class="tab-content container">
						<div class="tab-pane active" id="tabDegrees">

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
													placeholder="B&uacute;squeda" data-ng-model="search.name" />
												<span
													class="glyphicon glyphicon-search form-control-feedback"
													aria-hidden="true"></span>
											</div>
										</form>
										<div class="nav navbar-nav navbar-right">
											<button type="button" class="btn btn-default navbar-btn"
												ng-click="showAddDegree()">
												<span class="glyphicon glyphicon-plus-sign"
													aria-hidden="true"></span> A&ntilde;adir titulaci&oacute;n
											</button>

										</div>
									</div>
								</nav>
								<table class="table table-striped">
									<tr style="background-color: #bbb;">
										<th>Nombre</th>
										<th>Nivel Acad&eacute;mico</th>
										<th>Modalidad</th>
										<th>Nota de corte</th>
										<th>Plazas</th>
										<th></th>
									</tr>
									<tr ng-repeat="degree in filterDegrees | orderBy: 'degree.name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
										<td width="40%">{{degree.degree.name}}</td>
										<td width="15%">{{degree.degree.academicLevel}}</td>
										<td width="12%">{{degree.degree.mode}}</td>
										<td width="15%">{{degree.cut_off_grade}}</td>
										<td width="10%">{{degree.quota}}</td>
										<td width="8%" align="right">
											<a href='#' title='Temáticas' ng-click="showAddSubject(degree)"><span class="glyphicon glyphicon-tags" aria-hidden="true"></span></a>
											<a href='#' title='Editar' ng-click="showEditDegree(degree)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
											<a href='#' title='Eliminar' ng-click="deleteDegree(degree)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
										</td>
									</tr>
								</table>
								<nav aria-label="Paginado">
									Mostrando <select data-ng-model="pageSize"
										data-ng-options="option for option in [10, 25, 50, 100]"></select>
									&iacute;tems por p&aacute;gina
									<uib-pagination ng-model="currentPage"
										items-per-page="pageSize" max-size="10"
										total-items="filterDegrees.length" direction-links="true"
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
		<!-- MODAL TITULACION -->
		<div class="modal" id="degree-modal" tabindex="-1" role="dialog" aria-labelledby="degree-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="degree-modalLabel">
						<span ng-show="!degreeData.id">Añadir titulaci&oacute;n</span>
						<span ng-show="degreeData.id">Editar titulaci&oacute;n</span>
						</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12 form-group">
								<label>Titulación</label>
								<ui-select ng-model="degreeData.degree" theme="bootstrap" sortable="true" close-on-select="true" ng-disabled="degreeData.id">
								<ui-select-match>{{$select.selected.name}}</ui-select-match>
								<ui-select-choices repeat="degree in alldegrees | propsFilter: {name: $select.search, code: $select.search} ">
								<div ng-bind-html="degree.name | highlight: $select.search"></div>
						      	<small>
						        	Nivel acad&eacute;mico: <span ng-bind-html="''+degree.academicLevel | highlight: $select.search"></span>
						        	Modalidad: <span ng-bind-html="''+degree.mode | highlight: $select.search"></span>
						        	C&oacute;digo: <span ng-bind-html="''+degree.code | highlight: $select.search"></span>
						      	</small>
								</ui-select-choices> </ui-select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="cutOffGrade">Nota de corte</label>
								<input id="cutOffGrade" type="number" min="0" class="form-control" ng-model="degreeData.cut_off_grade" step="0.01" />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="quota">Cupo de plazas ofertadas</label> 
								<input id="quota" type="number" min="0"class="form-control" ng-model="degreeData.quota" step="1" />
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default" ng-click="addDegree()" ng-show="!degreeData.id">Añadir titulaci&oacute;n</button>
						<button type="button" class="btn btn-default" ng-click="editDegree()" ng-show="degreeData.id">Editar titulaci&oacute;n</button>
					</div>
				</div>
			</div>
		</div>
		<!-- MODAL PARA AÑADIR TEMATICAS A LA TITULACIÒN-->
		<div class="modal" id="add-subject-modal" tabindex="-1" role="dialog"
			aria-labelledby="add-subjects-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-subjects-modalLabel">Asignaci&oacute;n
							de tem&aacute;ticas</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12 form-group">
								<label>Temática</label>
								<ui-select ng-model="subject.selected" theme="bootstrap" sortable="true" close-on-select="true">
								<ui-select-match>{{$select.selected.name}}</ui-select-match>
								<ui-select-choices repeat="subject in allsubjects | propsFilter: {name: $select.search, id: $select.search} ">
								<div ng-bind-html="name | highlight: $select.search"></div>
									<span ng-bind-html="''+subject.name| highlight: $select.search"></span>
								</ui-select-choices> </ui-select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="weightLoad">Peso</label> <input
									type="number" min="0" max="1" name="weightAdd"
									class="form-control" ng-model="weightAdd"
									ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/" step="0.01" ng-min="0" />
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
							<button type="button" class="btn btn-default sabado1" abc="200"
							ng-click="addSubjectsArray(subject.selected)">Añadir</button>
							<button type="button" class="btn btn-default sabado2" abc="200"
							ng-click="adddArray()">Guardar</button>
					</div>
					
					<!-- Modelo para la tabla de tematicas asignadas a una titulacion -->
					<div class="modal-body">
						<table class="table table-striped">
							<tr style="background-color: #bbb;">
								<th>Temática</th>
								<th>Peso</th>
								<th></th>
							</tr>				
							<tr ng-repeat="subject in degreeSubjects | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
								<td width="45%">{{subject.subject.name}}</td>
								<td width="15%">{{subject.weight}}</td>
								<td width="20%" align="right"><a href='#' title='Eliminar'
									ng-click="deleteDegreeSubject(subject, $index)"><span
										class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<content tag="local_script"> <script
		src="<c:url value="/web-resources/js/controller/evaluation-event-degrees-ctrl.js" />"></script>
	</content>
</body>
</html>