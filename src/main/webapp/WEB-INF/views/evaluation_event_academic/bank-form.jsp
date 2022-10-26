<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Banco de preguntas</title>
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
	<div ng-controller="bankCtrl" ng-init="init(${bank.id});">
		<div class="page-header">
			<h3>${headText}
				<small> <a href="<c:url value="/bank" />" class="pull-right"> <span
						class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
				</a>
				</small>
			</h3>
		</div>
		<div class="row">
			<div class="col-md-12">
				<sf:form modelAttribute="bank">
					<sf:hidden path="id" readonly="true" />
					<div class="form-group">
						<sf:label path="name">Nombre</sf:label>
						<sf:input path="name" id="name" class="form-control" readonly="true" />
						<sf:errors path="name" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="questionNumber">N&ordm; preguntas</sf:label>
						<sf:input path="questionNumber" id="questionNumber" class="form-control"
							readonly="true" />
						<sf:errors path="questionNumber" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="state">Estado</sf:label>
						<sf:input path="state" class="form-control" readonly="true" />
						<sf:errors path="state" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="createDate">Fecha de creaci&oacute;n</sf:label>
						<sf:input path="createDate" id="createDate" class="form-control"
							readonly="true" />
						<sf:errors path="createDate" cssClass="error" />
					</div>
					<div class="form-group">
						<sf:label path="externalId">Id externo</sf:label>
						<sf:input path="externalId" id="externalId" class="form-control"
							readonly="true" />
						<sf:errors path="externalId" cssClass="error" />
					</div>
				</sf:form>
			</div>
		</div>

		<div class="container" style="margin-top: 20px;">
			<div class="panel-group" id="testAccordion">
				<div class="panel panel-default" ng-show="bankId">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#testAccordion" href="#collapseBank" ng-click="getTests()">
								<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Tests
							</a>
						</h4>
					</div>
					<div id="collapseBank" class="panel-collapse collapse">
						<div class="panel-body">
							<div class="row">
								<div class="col-md-12" class="ng-cloak">
									<p align="center" ng-hide="tests.length">-- Sin resultados --</p>
									<table class="table table-striped" ng-show="tests.length">
										<tr style="background-color: #bbb;">
											<th>Id</th>
											<th>Nombre</th>
											<th>T. evaluaci&oacute;n</th>
											<th>N&ordm; Max. Pr.</th>
											<th>N&ordm; Min. Pr.</th>
											<th>Id externo</th>
											<th>Activo</th>
											<th></th>
										</tr>
										<tr ng-repeat="test in tests" class="ng-scope">
											<td width="10%">{{test.id}}</td>
											<td width="30%">{{test.name}}</td>
											<td width="20%">{{test.evaluationType.name}}</td>
											<td width="10%">{{test.maxQuestionNum}}</td>
											<td width="10%">{{test.minQuestionNum}}</td>
											<td width="10%">{{test.externalId}}</td>
											<td width="5%" ng-if="test.active==0">No</td>
											<td width="5%" ng-if="test.active==1">S&iacute;</td>
											<td width="10%" align="right"><a href='#'
												ng-click="showTest(test)" title="Ver Test"><span
													class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a> <!--<a href='#' ng-click="showTest(test)" title="Ver Test"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>-->
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="container" style="margin-top: 20px;">
			<div class="panel-group" id="periodsAccordion">
				<div class="panel panel-default" ng-show="bankId">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#periodsAccordion" href="#collapsePeriods"
								ng-click="getPeriods()"> <span class="glyphicon glyphicon-plus"></span>
								Periodos de modificación del Banco
							</a>
						</h4>
					</div>
					<div id="collapsePeriods" class="panel-collapse collapse">
						<div class="panel-body">
							<div class="row">
								<div class="col-md-12" class="ng-cloak">
									<p align="center" ng-hide="periods.length">-- Sin resultados --</p>
									<table class="table table-striped"  ng-show="periods.length">
										<tr style="background-color: #BBBBBB">
											<th>Fecha de Inicio</th>
											<th>Fecha de Fin</th>
											<th></th>
										</tr>
										<tr ng-repeat="period in periods" class="ng-scope">
											<td width="45%">{{period.initDate| date:'dd-MM-yyyy HH:mm' :
												'UTC'}}</td>
											<td width="45%">{{period.endDate| date:'dd-MM-yyyy HH:mm' :
												'UTC'}}</td>
											<td width="10%" align="right"><a href='#'
												ng-click="showAddPeriod(period)" title="Editar Per&iacute;odo"><span
													class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>&nbsp;
													<a href='#'
													ng-click="deletePeriod(period)" title="Borrar Per&iacute;odo"><span
													class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
										</tr>
									</table>
									<button type="button" class="btn btn-default"
										ng-click="showAddPeriod()">Añadir</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--  <div class="container" style="margin-top: 20px;">
			<div class="panel-group" id="teacherPeriodsAccordion">
				<div class="panel panel-default" ng-show="bankId">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#teacherPeriodsAccordion" href="#collapseTeacherPeriods"
								ng-click="getTeachersPeriods()"> <span
								class="glyphicon glyphicon-plus"></span> Periodos de modificación del
								Banco para profesores
							</a>
						</h4>
					</div>
					<div id="collapseTeacherPeriods" class="panel-collapse collapse">
						<div class="panel-body">
							<div class="row">
								<div class="col-md-12" class="ng-cloak">
									<p align="center" ng-hide="teacherPeriods.length">-- Sin resultados
										--</p>
									<table class="table table-striped"  ng-hide="teacherPeriods.length">
										<tr style="background-color: #BBBBBB">
											<th>Profesor</th>
											<th>Fecha de Inicio</th>
											<th>Fecha de Fin</th>
											<th></th>
										</tr>
										<tr ng-repeat="period in teacherPeriods" class="ng-scope">
											<td width="40%">{{period.teacher.firstName}}
												{{period.teacher.lastName}}</td>
											<td width="25%">{{period.initDate| date:'dd-MM-yyyy HH:mm' :
												'UTC'}}</td>
											<td width="25%">{{period.endDate| date:'dd-MM-yyyy HH:mm' :
												'UTC'}}</td>
											<td width="10%" align="right"><a href='#'
												ng-click="showTest(period)" title="Editar Periodo"><span
													class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
										</tr>
									</table>
									<button type="button" class="btn btn-default"
										ng-click="showAddTeacherPeriod()">Añadir</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>-->

		<!-- MODAL TESTS -->
		<div class="modal" id="test-modal" tabindex="-1" role="dialog"
			aria-labelledby="test-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="test-modalLabel">Test</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								<form name="testForm" novalidate>
									<fieldset>
										<label>Nombre</label> <input type="text" name="test_name"
											id="test_name" ng-model="test.name" class="form-control" readonly />
										<label>Nº máximo preguntas</label> <input type="text" name="test_maxq"
											id="test_maxq" ng-model="test.maxQuestionNum" class="form-control"
											readonly /> <label>Nº mínimo preguntas</label> <input type="text"
											name="test_minq" id="test_minq" ng-model="test.maxQuestionNum"
											class="form-control" readonly /> <label>Fecha creación</label> <input
											type="text" name="test_create_date" id="test_create_date"
											ng-model="test.createDate" class="form-control" readonly /> <label>Id
											externo</label> <input type="text" name="test_external_id"
											id="test_external_id" ng-model="test.externalId" class="form-control"
											readonly /> <label>Tiempo (s)</label> <input type="text"
											name="test_time" id="test_time" ng-model="test.time"
											class="form-control" readonly /> <label>Activo</label> <input
											ng-if="test.active==1" type="text" name="test_active_text_true"
											id="test_active_text_true" class="form-control" readonly value="Sí" />
										<input ng-if="test.active==0" type="text"
											name="test_active_text_false" id="test_active_text_false"
											class="form-control" readonly value="No" /> <input type="hidden"
											name="test_active" id="test_active" ng-model="test.active"
											class="form-control" readonly /> 
										<label>Casillero</label>
										<input type="text" name="test_locker" id="test_locker" ng-model="test.locker" class="form-control" />
										<label>Tipo de evaluación</label> 
										<select name="evaluation_type" id="evaluation_type" ng-model="test.evaluationType" ng-options="type.name for type in evaluationTypes track by type.id" class="form-control"></select>
										<div class="help-block" ng-messages="testForm.evaluation_type.$error"
											ng-if="testForm.evaluation_type.$touched">
											<div ng-messages-include="error-messages"></div>
										</div>
										<%-- <label>Tipo de test</label> <select name="test_type" id="test_type"
											ng-model="test.testType"
											ng-options="type.name for type in testTypes track by type.id"
											class="form-control">
										</select>
										<div class="help-block" ng-messages="testForm.test_type.$error"
											ng-if="testForm.test_type.$touched">
											<div ng-messages-include="error-messages"></div>
										</div> --%>
										<div class="panel-group" style="margin-top: 10px;">
											<div class="panel panel-default">
												<div class="panel-heading"><span>Criterio de corrección</span></div>
												<div class="panel-body">
								  			 		<div class="form-group">
														<label>Grado m&iacute;nimo</label>
				  										<input type="number" name="correctionRuleMinGrade" class="form-control" ng-model="test.correctionRule.minGrade" ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/" step="0.01" ng-min="0" ng-required="correctionRuleRequired()"/>
				  										<div ng-messages="testForm.correctionRuleMinGrade.$error" ng-if="testForm.correctionRuleMinGrade.$touched">
													        <div ng-message="min" class="error">El valor debe ser superior o igual a 0</div>
													        <div ng-messages-include="error-messages"></div>
												      	</div>
								  			 		</div>
								  			 		<div class="form-group">
														<label>Grado m&aacute;ximo</label>
								  			 			<input type="number" name="correctionRuleMaxGrade" class="form-control" ng-model="test.correctionRule.maxGrade" ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/" step="0.01" ng-min="test.correctionRule.minGrade" ng-required="correctionRuleRequired()"/>
								  			 			<div ng-messages="testForm.correctionRuleMaxGrade.$error" ng-if="testForm.correctionRuleMaxGrade.$touched">
													        <div ng-message="min" class="error">El valor debe ser superior o igual al grado m&iacute;nimo</div>
													        <div ng-messages-include="error-messages"></div>
												      	</div>
								  			 		</div>
								  			 		<div class="form-group">
								  			 			<label>Tipo</label>
								  			 			<select name="correctionRuleType" class="form-control" ng-model="test.correctionRule.type" ng-options="o.v as o.n for o in [{n: 'Correcci&oacute;n SIETTE', v: 'CANONICAL_RULE'}, {n: 'Correcci&oacute;n UTPL', v: 'UTPL_RULE'}]" ng-required="correctionRuleRequired()"></select>
							  			 				<div ng-messages="testForm.correctionRuleType.$error" ng-if="testForm.correctionRuleType.$touched"><div ng-messages-include="error-messages"></div></div>
							  			 			</div>
												</div>
											</div>
										</div>
										<input type="hidden" name="test_id" id="test_id" ng-model="test.id"
											class="form-control" />
									</fieldset>
								</form>
							</div>
						</div>
					</div>
					<div class="modal-footer">						
						<button ng-disabled="testForm.$invalid" type="button" class="btn btn-default"
							ng-click="editTest(test)">Modificar</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					</div>
				</div>
			</div>
		</div>

		<!-- MODAL PERIODOS PROFESOR -->
		<!-- <div class="modal" id="add-teacher-period-modal" tabindex="-1" role="dialog"
			aria-labelledby="add-teacher-period-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-teacher-period-modalLabel">Asignaci&oacute;n
							de bancos de preguntas</h3>
					</div>
					<div class="modal-body">
						<form name="testForm">
							<fieldset>
								<div class="row">
									<div class="col-md-12">
										<ui-select ng-model="teachers.selected" theme="bootstrap"
											ng-disabled="disabled" sortable="true" close-on-select="true">
										<ui-select-match placeholder="Seleccione docente...">{{$select.selected.fullName}}
										({{$select.selected.username}})</ui-select-match> <ui-select-choices
											repeat="teacher in teachers | propsFilter: {username: $select.search, fullName: $select.search}">
										<div ng-bind-html="teacher.username | highlight: $select.search"></div>
										<small> Nombre: <span
											ng-bind-html="''+teacher.fullName| highlight: $select.search"></span>
											Username: <span
											ng-bind-html="''+teacher.username| highlight: $select.search"></span>
										</small> </ui-select-choices> </ui-select>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<label path="initDate">Fecha de inicio: </label> <input
											ng-model="period.initDate" id="initDate" name="initDate"
											class="form-control" type="datetime" />
									</div>
									<div class="col-md-6">
										<label path="endDate">Fecha de fin: </label> <input
											ng-model="period.endDate" id="endDate" name="endDate"
											class="form-control" type="datetime" />
									</div>
								</div>
							</fieldset>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default"
							ng-click="addTeacherPeriod(period)">A&ntilde;adir</button>
					</div>
				</div>
			</div>-->
		
		
		
		<!-- MODAL PERIODOS -->
		<div class="modal" id="add-period-modal" tabindex="-1" role="dialog"
			aria-labelledby="add-period-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="add-period-modalLabel">Asignaci&oacute;n
							per&iacute;odos de bancos de preguntas</h3>
					</div>
					<div class="modal-body">
						<form name="periodForm">
							<fieldset>
								<div class="row">
									<div class="col-md-6">
										<label path="initDate">Fecha de inicio: </label> <input
											ng-model="period.initDate" id="initDate" name="initDate"
											class="form-control" type="datetime" />
									</div>
									<div class="col-md-6">
										<label path="endDate">Fecha de fin: </label> <input
											ng-model="period.endDate" id="endDate" name="endDate"
											class="form-control" type="datetime" />
									</div>
								</div>
							</fieldset>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<button type="button" class="btn btn-default"
							ng-click="addPeriod(period)" ng-show="currentPeriodId == null">A&ntilde;adir</button>
						<button type="button" class="btn btn-default"
							ng-click="modifyPeriod(period)" ng-show="currentPeriodId != null">Modificar</button>
					</div>
				</div>
			</div>
		</div>
	</div>


	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/bank-ctrl.js" />"></script>
	</content>
</body>
</html>