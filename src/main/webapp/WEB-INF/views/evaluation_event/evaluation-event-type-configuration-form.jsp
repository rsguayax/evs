<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Evento de evaluaci&oacute;n</title>

<script type="text/javascript">
	$(function() {
		$('li.tab-list').removeClass('active');
		$('#tabConfig').addClass('active');
	});
</script>

</head>
<body>
	<div class="page-header">
 		<h3>l Configuraci&oacute;n del evento de evaluaci&oacute;n ${evaluationEvent.name}
 			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
  			</small>
 		</h3>
	</div>
	<div class="row" ng-controller="evaluationEventConfigurationCtrl" ng-init="init(${evaluationEvent.id});">
		<div class="col-md-12">
			<div id="tabs">
				<c:import url="include/uptab.jsp" />
				<div class="tab-content container">
			        <div class="tab-pane active" ng-cloak>
	  			 		<form name="configurationForm" ng-show="configuration != null" novalidate>
	  			 			<div class="form-group">
		  			 			<label>N&uacute;mero m&aacute;ximo de ex&aacute;menes por jornada de evaluaci&oacute;n</label>
		  			 			<input type="number" name="maxTimeBlockTests" class="form-control" ng-model="configuration.maxTimeBlockTests" required/>
					        	<div ng-messages="configurationForm.maxTimeBlockTests.$error" ng-if="configurationForm.maxTimeBlockTests.$touched"><div ng-messages-include="error-messages"></div></div>
		  			 		</div>
		  			 		<div class="form-group">
		  			 			<label>N&uacute;mero m&aacute;ximo de ex&aacute;menes por d&iacute;a</label>
		  			 			<input type="number" name="maxDailyTests" class="form-control" ng-model="configuration.maxDailyTests" required/>
		  			 			<div ng-messages="configurationForm.maxDailyTests.$error" ng-if="configurationForm.maxDailyTests.$touched"><div ng-messages-include="error-messages"></div></div>
		  			 		</div>
		  			 		<div class="form-group">
		  			 			<label>Permitir realizar ex&aacute;menes en varios bloques horarios del mismo d&iacute;a</label>
		  			 			<select name="testsInSeveralTimeBlocksSameDay" class="form-control" ng-model="configuration.testsInSeveralTimeBlocksSameDay" ng-options="o.v as o.n for o in [{n: 'Si', v: true}, {n: 'No', v: false}]" required></select>
		  			 			<div ng-messages="configurationForm.testsInSeveralTimeBlocksSameDay.$error" ng-if="configurationForm.testsInSeveralTimeBlocksSameDay.$touched"><div ng-messages-include="error-messages"></div></div>
	  			 			</div>
	  			 			<div class="form-group">
		  			 			<label>Tipo de asignación de horarios</label>
		  			 			<select name="assignmentType" class="form-control" ng-model="configuration.assignmentType" ng-options="o.v as o.n for o in [{n: 'Manual', v: 'MANUAL'}, {n: 'Automático', v: 'AUTOMATICO'}]" required ng-disabled="assignmentTypeDisabled"></select>
		  			 			<div ng-messages="configurationForm.assignmentType.$error" ng-if="configurationForm.assignmentType.$touched"><div ng-messages-include="error-messages"></div></div>
	  			 			</div>

							<div class="form-group">
								<label>Periodo de ejecuci&oacute;n de la carga diaria de estudiantes y asignaci&oacute;n de horarios</label>
								<div class="row">
	  								<div class="col-lg-6">
	  									<div class="input-group">
			  			 					<input type="text" class="form-control"
			  			 						uib-datepicker-popup="{{format}}"
			  			 						ng-model="configuration.dailyStudentsAndSchedulesLoadStartDate"
			  			 						is-open="popup1.opened"
			  			 						datepicker-options="dateOptions1"
			  			 						ng-required="true"
			  			 						ng-change="changed1()"
			  			 						close-text="Cerrar"
			  			 						current-text="Hoy"
			  			 						clear-text="Limpiar"
			  			 						alt-input-formats="altInputFormats" placeholder="Fecha de inicio" />
											<span class="input-group-btn">
		           								<button type="button" class="btn btn-default" ng-click="open1()">
		           									<i class="glyphicon glyphicon-calendar" aria-hidden="true"></i>
		           								</button>
		           							</span>
										</div>
	  								</div>
	  								<div class="col-lg-6">
	  									<div class="input-group">
			  			 					<input type="text" class="form-control"
			  			 						uib-datepicker-popup="{{format}}"
			  			 						ng-model="configuration.dailyStudentsAndSchedulesLoadEndDate"
			  			 						is-open="popup2.opened"
			  			 						datepicker-options="dateOptions2"
			  			 						ng-required="true"
			  			 						ng-change="changed2()"
			  			 						close-text="Cerrar"
			  			 						current-text="Hoy"
			  			 						clear-text="Limpiar"
			  			 						alt-input-formats="altInputFormats" placeholder="Fecha de fin" />
											<span class="input-group-btn">
		           								<button type="button" class="btn btn-default" ng-click="open2()">
		           									<i class="glyphicon glyphicon-calendar" aria-hidden="true"></i>
		           								</button>
		           							</span>
										</div>
	  								</div>
  								</div>
							</div>

							<div class="form-group">
								<label>Hora de ejecuci&oacute;n diaria de la carga de estudiantes y asignaci&oacute;n de horarios.
									<em>Nota: la hora est&aacute; en modo
										<span ng-switch on="ismeridian">
	      									<span ng-switch-when="true">12H</span>
	      									<span ng-switch-default>24H</span>
	  									</span>
  									</em>
								</label>
								<div uib-timepicker ng-model="configuration.dailyStudentsAndSchedulesLoadTime"
											hour-step="hstep"
											minute-step="mstep"
											show-meridian="ismeridian"></div>
							</div>

							<div class="alert alert-info" role="alert"
								ng-show="studentsAndSchedulesLoadNextExecutionDateTime">Pr&oacute;xima ejecuci&oacute;n de esta tarea: {{studentsAndSchedulesLoadNextExecutionDateTime | date: 'dd/MM/yyyy HH:mm'}}</div>

							<div class="form-group">
								<label>Periodo de ejecuci&oacute;n del env&iacute;o diario de las notificaciones de horarios a los estudiantes</label>
								<div class="row">
	  								<div class="col-lg-6">
	  									<div class="input-group">
			  			 					<input type="text" class="form-control"
			  			 						uib-datepicker-popup="{{format}}"
			  			 						ng-model="configuration.dailyEvaluationSchedulesMailingStartDate"
			  			 						is-open="popup3.opened"
			  			 						datepicker-options="dateOptions3"
			  			 						ng-required="true"
			  			 						ng-change="changed3()"
			  			 						close-text="Cerrar"
			  			 						current-text="Hoy"
			  			 						clear-text="Limpiar"
			  			 						alt-input-formats="altInputFormats" placeholder="Fecha de inicio" />
											<span class="input-group-btn">
		           								<button type="button" class="btn btn-default" ng-click="open3()">
		           									<i class="glyphicon glyphicon-calendar" aria-hidden="true"></i>
		           								</button>
		           							</span>
										</div>
	  								</div>
	  								<div class="col-lg-6">
	  									<div class="input-group">
			  			 					<input type="text" class="form-control"
			  			 						uib-datepicker-popup="{{format}}"
			  			 						ng-model="configuration.dailyEvaluationSchedulesMailingEndDate"
			  			 						is-open="popup4.opened"
			  			 						datepicker-options="dateOptions4"
			  			 						ng-required="true"
			  			 						ng-change="changed4()"
			  			 						close-text="Cerrar"
			  			 						current-text="Hoy"
			  			 						clear-text="Limpiar"
			  			 						alt-input-formats="altInputFormats" placeholder="Fecha de fin" />
											<span class="input-group-btn">
		           								<button type="button" class="btn btn-default" ng-click="open4()">
		           									<i class="glyphicon glyphicon-calendar" aria-hidden="true"></i>
		           								</button>
		           							</span>
										</div>
	  								</div>
  								</div>
							</div>

							<div class="form-group">
								<label>Hora de ejecuci&oacute;n diaria del env&iacute;o de las notificaciones de horarios a los estudiantes.
									<em>Nota: la hora est&aacute; en modo
										<span ng-switch on="ismeridian">
	      									<span ng-switch-when="true">12H</span>
	      									<span ng-switch-default>24H</span>
	  									</span>
  									</em>
								</label>
								<div uib-timepicker ng-model="configuration.dailyEvaluationSchedulesMailingTime"
											hour-step="hstep"
											minute-step="mstep"
											show-meridian="ismeridian"></div>
							</div>
							
							<div class="panel-group">
								<div class="panel panel-default">
									<div class="panel-heading"><span>Criterio de corrección de tests</span></div>
									<div class="panel-body">
					  			 		<div class="form-group">
											<label>Grado m&iacute;nimo</label>
	  										<input type="number" name="correctionRuleMinGrade" class="form-control" ng-model="configuration.correctionRule.minGrade" ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/" step="0.01" ng-min="0" ng-required="correctionRuleRequired()"/>
	  										<div ng-messages="configurationForm.correctionRuleMinGrade.$error" ng-if="configurationForm.correctionRuleMinGrade.$touched">
										        <div ng-message="min" class="error">El valor debe ser superior o igual a 0</div>
										        <div ng-messages-include="error-messages"></div>
									      	</div>
					  			 		</div>
					  			 		<div class="form-group">
											<label>Grado m&aacute;ximo</label>
					  			 			<input type="number" name="correctionRuleMaxGrade" class="form-control" ng-model="configuration.correctionRule.maxGrade" ng-pattern="/^[0-9]+(\.[0-9]{1,2})?$/" step="0.01" ng-min="configuration.correctionRule.minGrade" ng-required="correctionRuleRequired()"/>
					  			 			<div ng-messages="configurationForm.correctionRuleMaxGrade.$error" ng-if="configurationForm.correctionRuleMaxGrade.$touched">
										        <div ng-message="min" class="error">El valor debe ser superior o igual al grado m&iacute;nimo</div>
										        <div ng-messages-include="error-messages"></div>
									      	</div>
					  			 		</div>
					  			 		<div class="form-group">
					  			 			<label>Tipo</label>
					  			 			<select name="correctionRuleType" class="form-control" ng-model="configuration.correctionRule.type" ng-options="o.v as o.n for o in [{n: 'Correcci&oacute;n SIETTE', v: 'CANONICAL_RULE'}, {n: 'Correcci&oacute;n UTPL', v: 'UTPL_RULE'}]" ng-required="correctionRuleRequired()"></select>
				  			 				<div ng-messages="configurationForm.correctionRuleType.$error" ng-if="configurationForm.correctionRuleType.$touched"><div ng-messages-include="error-messages"></div></div>
				  			 			</div>
									</div>
								</div>
							</div>

							<div class="alert alert-info" role="alert"
								ng-show="evaluationSchedulesMailingNextExecutionDateTime">Pr&oacute;xima ejecuci&oacute;n de esta tarea: {{evaluationSchedulesMailingNextExecutionDateTime | date: 'dd/MM/yyyy HH:mm'}}</div>

							<button type="button" class="btn btn-info" ng-click="consultTasksRegistry()">Consultar las pr&oacute;ximas ejecuciones</button>

							<button type="button" class="btn btn-default" ng-disabled="configurationForm.$invalid" ng-click="editConfiguration()"><span class="glyphicon glyphicon-saved" aria-hidden="true"></span> Guardar</button>
	  			 		</form>
	  			 		<div class="loading-small" ng-show="configuration == null">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
			        </div>
		    	</div>
			</div>
		</div>
	</div>
	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/evaluation-event-configuration-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>