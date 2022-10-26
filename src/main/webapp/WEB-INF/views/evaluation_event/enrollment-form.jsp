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
		$('#tabEnrollments').addClass('active');
	});
	
	$("tr td:nth-child(3)").click(function(){
	     if (!$(this).hasClass("not-active")){
	         alert("El item sera eliminado.");
	         // tu codigo para eliminar aqui
	    }
	});
	
	
	$(document).ready(function() {
		  $("#aaa1").prop("disabled", true);
		  $('#b').css({
		    'cursor': 'none',
		    'user-select': 'none'
		  });
		  $('#b').click(function(){return false})

		})
</script>
</head>
<body>
	<div ng-controller="enrollmentCtrl"
		ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
			<h3>
				Inscripciones del evento de evaluaci&oacute;n
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
				<div id="tabs2">
					<c:import url="include/uptab.jsp" />
					<div class="tab-content container">
						<div class="tab-pane active" id="tabEnrollments">

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
											<button ng-show="allMattersWithTest" type="button" class="btn btn-default navbar-btn"
												ng-click="showAddEnrollment()">
												<span class="glyphicon glyphicon-plus-sign"
													aria-hidden="true"></span> A&ntilde;adir Inscripci&oacute;n
											</button>

										</div>
									</div>
								</nav>
								<table class="table table-striped">
									<tr style="background-color: #bbb;">
										<th>Nombre</th>
										<th>Apellidos</th>
										<th>Identificación</th>
										<th>Email</th>
										<th>Titulación</th>
										<th>Prioridad</th>
										<th>Centro de evaluación</th>
										<th></th>
									</tr>
									<tr ng-repeat="enrollment in filterEnrollments | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope">
										<td width="15%">{{enrollment.userFirstName}}</td>
										<td width="15%">{{enrollment.userLastName}}</td>
										<td width="10%">{{enrollment.userIdentification}}</td>
										<td width="14%">{{enrollment.userEmail}}</td>
										<td width="15%">{{enrollment.degreeName}}</td>
										<td width="8%">{{enrollment.priority}}</td>
										<td width="15%">{{enrollment.evaluationCenterName}}</td>
										<td width="8%" align="right" class="not-active">
											<a href="#" title="Temáticas y Tests" ng-click="showMatters(enrollment)"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a>
<%-- 											<a href='#' title='Editar' ng-click="editEnrollment(enrollment)"  ><span class="glyphicon glyphicon-pencil" aria-hidden="true" ></span></a> --%>
											<a href='#' title='Eliminar' ng-click="deleteEnrollment(enrollment)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
										</td>
									</tr>
								</table>
								<nav aria-label="Paginado">
						        	Mostrando
									<select	data-ng-model="pageSize" data-ng-options="option for option in [10, 25, 50, 100]"></select>
									&iacute;tems por p&aacute;gina
					            	<uib-pagination
					            		ng-model="currentPage"
					            		items-per-page="pageSize"
					            		max-size="10"
					            		total-items="filterEnrollments.length"
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
						</div>
					</div>

				</div>
			</div>
		</div>
		
		<!-- MODAL MATERIAS -->
		<div class="modal" id="matters-modal" tabindex="-1" role="dialog" aria-labelledby="add-matters-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="add-matters-modalLabel">Tem&aacute;ticas y Tests de la titulaci&oacute;n {{enrollmentSelected.degree.name}}</h3>
		      </div>
		      <div class="modal-body">
		      	<div class="row" style="margin-top: 50px;" ng-show="loadingMattersAndTests">
					<p align="center">
						<img src="<c:url value="/web-resources/img/load-small.gif" />">
					</p>
				</div>
		      	<div class="row" ng-show="!loadingMattersAndTests && matters.length" ng-cloak>
		        	<div class="col-md-12">
						<table class="table table-striped">
							<tr style="background-color: #BBBBBB"><th>Tem&aacute;tica</th><th>Test</th></tr>
				        	<tr ng-repeat="matter in matters | orderBy:'name'" class="ng-scope" >
								<td width="50%">{{matter.evaluationEventMatter.matter.name}}</td>
								<td width="50%">{{matter.testName}}</td>
							</tr>
				        </table>
			        </div>
			     </div>
			     <div class="row" ng-show="!loadingMattersAndTests && matters.length == 0" ng-cloak>
		        	<div class="col-md-12">
		        		<p>No se han encontrado tem&aacute;ticas de la titulaci&oacute;n {{enrollmentSelected.degree.name}}</p>
		        	</div>
	        	</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		      </div>
		    </div>
		  </div>
	    </div>

		<!-- MODAL PARA CREACION DE INSCRIPCIONES Y USUARIO -->
		<div class="modal" id="add-enrollment-modal" tabindex="-1"
			role="dialog" aria-labelledby="add-degrees-modalLabel">
			<div class="modal-dialog" role="document" style="width: 720px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-degrees-modalLabel">Crear Inscripci&oacute;n</h3>
					</div>
					<div class="modal-body">
					
					
					<div class="row">
							<div class="col-md-12 form-group">
								<label for="identificationLoad">Identificación</label>
								<table style="width: 100%;">
									<tr>
										<td style="width: 100%;"><input type="text" name="identificationAdd" class="form-control" ng-model="identificationAdd" ></input></td>
										<td style="padding-left: 8px;"><button type="button" class="btn btn-default" ng-click="viewDataUser(identificationAdd)">Buscar</button></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="firstnameLoad">Nombre</label> <input type="text"
									name="firstnameAdd" class="form-control"
									ng-model="firstnameAdd"  />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="lastnameLoad">Apellidos</label> <input
									type="text" name="lastnameAdd" class="form-control"
									ng-model="lastnameAdd" />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="emailLoad">Email</label> <input type="text"
									name="emailAdd" class="form-control" ng-model="emailAdd" />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label>Centro de evaluaci&oacute;n</label>
								<ui-select ng-model="selectedEvaluationCenter.selected" theme="bootstrap" sortable="true" close-on-select="true">
								<ui-select-match>{{$select.selected.name}}</ui-select-match>
								<ui-select-choices repeat="evaluationCenter in evaluationCenters">
								<div ng-bind-html="name | highlight: $select.search"></div>
								<small> Nombre: <span ng-bind-html="''+evaluationCenter.name| highlight: $select.search"></span></small>
								</ui-select-choices></ui-select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label>Titulaciones</label>
								<ui-select multiple limit="2" ng-model="selectedDegrees.selected" 
									theme="bootstrap" ng-disabled="disabled" sortable="true"
									close-on-select="true"> <ui-select-match
									placeholder="Seleccione las titulaciones por orden de prioridad (Titulaci&oacute;n 1, Titulaci&oacute;n 2 )">{{$item.degree.name}}</ui-select-match>
								<ui-select-choices
									repeat="degree in alldegrees  ">
								<div ng-bind-html="name | highlight: $select.search"></div>
								<small> Nombre: <span
									ng-bind-html="''+degree.degree.name| highlight: $select.search"></span>
									C&oacute;digo: <span
									ng-bind-html="''+degree.degree.id| highlight: $select.search"></span>
								</small> </ui-select-choices> </ui-select>
							</div>
						</div>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
						<button type="button" class="btn btn-default"
							ng-click="addEnrollment()">Crear Inscripción</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- MODAL PARA EDICION DE INSCRIPCIONES -->
		<div class="modal" id="add-enrollment-edit-modal" tabindex="-1"
			role="dialog" aria-labelledby="add-enrollment-edit-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-enrollment-edit-modalLabel">Editar Inscripci&oacute;n</h3>
					</div>
					<div class="modal-body">
					<div class="row">
							<div class="col-md-12">
								<label for="identificationLoad">Identificación</label> <input
									type="text" name="identificationAdd" class="form-control"
									ng-model="identificationAdd" ></input>
									
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<label for="firstnameLoad">Nombre</label> <input type="text"
									name="firstnameAdd" class="form-control"
									ng-model="firstnameAdd"  />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<label for="lastnameLoad1">Apellidos</label> <input
									type="text" name="lastnameAdd" class="form-control"
									ng-model="lastnameAdd" />
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<label for="emailLoad">Email</label> <input type="text"
									name="emailAdd" class="form-control" ng-model="emailAdd" />
							</div>
						</div>
						<div>
							<br>
						</div>
						<div class="row">
							<div class="col-md-12">

								<ui-select limit="1" multiple ng-model="selectedDegrees.selected" 
									theme="bootstrap"  sortable="true"
									close-on-select="true"> <ui-select-match
									placeholder="{{selectLabel}}">{{$item.degree.name}}&lt;{{$item.degree.code}}&gt;</ui-select-match>
								<ui-select-choices
									repeat="degree in alldegrees  ">
								<div ng-bind-html="name | highlight: $select.search"></div>
								<small> Nombre: <span
									ng-bind-html="''+degree.degree.name| highlight: $select.search"></span>
									C&oacute;digo: <span
									ng-bind-html="''+degree.degree.id| highlight: $select.search"></span>
								</small> </ui-select-choices> </ui-select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
						<button type="button" class="btn btn-default"
							ng-click="saveEditEnrollment(selectedDegrees.selected)">Editar Inscripción</button>
					</div>
				</div>
			</div>
		</div>


	</div>

	<content tag="local_script"> <script
		src="<c:url value="/web-resources/js/controller/enrollment-ctrl.js" />"></script>

	</content>
</body>
</html>