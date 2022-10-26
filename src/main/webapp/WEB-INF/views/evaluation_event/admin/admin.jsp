<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Administraci&oacute;n</title>

	<link rel="stylesheet" type="text/css" href="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.css" />"/>

	<script type="text/javascript" src="<c:url value="/web-resources/js/datetimepicker/jquery.datetimepicker.min.js" />" charset="utf-8"></script>

	<script type="text/javascript">
		$(function() {
			$.datetimepicker.setLocale('es');
			$('#scheduleModificationStartDate, #scheduleModificationEndDate').datetimepicker({
				format:'d-m-Y H:i',
				dayOfWeekStart: 1,
				step: 30,
				onChangeDateTime: function(currentTime, $input) {
					if ($.trim($input.val()).length > 0 && !moment($input.val(), 'DD-MM-YYYY HH:mm', true).isValid()) {
						$input.val('');
						alert('Introduzca una fecha válida');
					}
			  	}
			});
		});
	</script>
	
	 <style>
            .my-drop-zone { border: dotted 3px lightgray; height: 130px}
            .nv-file-over { border: dotted 3px red; } /* Default class applied to drop zones on over */
            .another-file-over-class { border: dotted 3px green; }
     </style>

</head>
<body>
	<div class="page-header">
  		<h3>Gesti&oacute;n del evento ${evaluationEvent.name}
  			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
	  		</small>
  		</h3>
	</div>
	<div class="row" ng-controller="adminEventCtrl" ng-init="init('${evaluationEvent.id}', '${assignmentType}');">
		<div class="col-md-12">
			<div class="panel-group">
				<div class="panel panel-default">
					<div class="panel-heading"><span>Fechas en las que se permite modificar los horarios de evaluaciones</span></div>
					<div class="panel-body">
						<table class="table table-borderless table-condensed">
							<tr>
								<td style="width: 50%;">
									<input id="scheduleModificationStartDate" class="form-control" type="text" name="studentsSearch" placeholder="Fecha de inicio" ng-model="scheduleModificationStartDate"/>
								</td>
								<td style="width: 50%;">
									<input id="scheduleModificationEndDate" class="form-control" type="text" name="studentsSearch" placeholder="Fecha de fin" ng-model="scheduleModificationEndDate"/>
								</td>
								<td>
									<button class="btn btn-default" ng-click="addScheduleModificationDate()"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Añadir rango de fechas</button>
								</td>
							</tr>
						</table>
						<table class="table table-striped" ng-show="scheduleModificationDates.length > 0">
							<tr style="background-color: #e1e1e1"><th>Fecha de inicio</th><th>Fecha de fin</th><th></th></tr>
				        	<tr ng-repeat="scheduleModificationDate in scheduleModificationDates" class="ng-cloak">
								<td width="45%">{{scheduleModificationDate.startDate | date:'dd-MM-yyyy HH:mm' : 'UTC'}}</td>
								<td width="45%">{{scheduleModificationDate.endDate | date:'dd-MM-yyyy HH:mm' : 'UTC'}}</td>
								<td width="10%">
									<a title="Eliminar" href ng-click="deleteScheduleModificationDate(scheduleModificationDate)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
				        </table>
				        <div ng-show="scheduleModificationDates.length == 0">
				        	<h4>No hay definido ningún rango de fechas</h4>
				        </div>
				        <div class="loading-small" ng-show="scheduleModificationDates == null">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
			  		</div>
		  		</div>

		  		<div class="panel panel-default" style="margin-top: 20px;">
					<div class="panel-heading"><span>Asignación de horarios</span></div>
					<div class="panel-body">
						<div>
							<div ng-show="!loadingTestsSchedulesInfo">
								<table style="width: 100%;">
									<tr>
										<td><span style="color: #eaab00; font-size: 32px;" class="glyphicon glyphicon-exclamation-sign"></span></td>
	 									<td style="padding-left: 8px; font-size: 18px;">Hay <b>{{testsSchedulesInfo.testsWithSchedule}}</b> tests con horario asignado y <b>{{testsSchedulesInfo.testsWithoutSchedule}}</b> pendientes de la asignación de horario</td>
										<td style="padding-left: 8px;" align="right">
											<table>
												<tr>
													<td style="padding-right: 4px;"><button class="btn btn-success" ng-click="exportExternal()"><span class="glyphicon glyphicon-export" aria-hidden="true"></span> Exportar Siette</button></td>
													<td style="padding-right: 4px;"  ng-if="testsSchedulesInfo.testsWithoutSchedule > 0"><a class="btn btn-default" href="<c:url value="/evaluationevent/{{evaluationEventId}}/admin/checkevaluation"/>" target="_blank"><span class="glyphicon glyphicon-check"></span> Verificar</a></td>
													<td ng-if="logVisible()" style="padding-right: 4px;"><a class="btn btn-default" href="<c:url value="/evaluationevent/{{evaluationEventId}}/admin/testsscheduleslog"/>" target="_blank"><span class="glyphicon glyphicon-list-alt"></span> Log</a></td>
													<td style="padding-right: 4px;"><button class="btn btn-default" ng-click="assignSchedulesWithCheking()"><span class="glyphicon glyphicon-time" aria-hidden="true"></span> Asignación</button></td>										
													<td style="padding-right: 4px;"><button class="btn btn-default" ng-click="downloadSchedulesFile()"><span class="glyphicon glyphicon-download" aria-hidden="true"></span> Desc. plantilla</button></td>	
													<c:if test="${debug}"><td style="padding-right: 4px;"><button class="btn btn-danger" ng-click="deleteSchedules()"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Eliminar horaios</button></td></c:if>									
												</tr>
											</table>
										</td>
									</tr>
								</table>
								<table ng-if="testsSchedulesInfo.testsWithoutSchedule == 0">
									<tr>
										<td><span style="color: #739600; font-size: 32px;" class="glyphicon glyphicon-ok-sign"></span></td>
										<td style="padding-left: 8px; font-size: 18px">Todos los tests tienen horario asignado</td>
									</tr>
								</table>
								<div google-chart chart="testsSchedulesChart" style="{{testsSchedulesChart.cssStyle}}" ></div>
							</div>
							<div class="loading-small" ng-show="loadingTestsSchedulesInfo">
					        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
					        </div>
				        </div>
				        <div style="margin-top: 10px;">
							<div ng-show="!loadingAvailableSeatsInfo">
								<table ng-if="availableSeatsInfo.totalAvailableSeats > 0">
									<tr>
										<td><span style="color: #739600; font-size: 32px;" class="glyphicon glyphicon-ok-sign"></span></td>
										<td style="padding-left: 8px; font-size: 18px">Hay <b>{{availableSeatsInfo.totalAvailableSeats}}</b> plazas disponibles para realizar exámenes</td>
									</tr>
								</table>
								<table ng-if="availableSeatsInfo.totalAvailableSeats == 0">
									<tr>
										<td><span style="color: #eaab00; font-size: 32px;" class="glyphicon glyphicon-exclamation-sign"></span></td>
										<td style="padding-left: 8px; font-size: 18px; width: 100%;"><b>No</b> hay plazas disponibles para realizar exámenes</td>
									</tr>
								</table>
								<div google-chart chart="availableSeatsChart" style="{{availableSeatsChart.cssStyle}}" ></div>
							</div>
							<div class="loading-small" ng-show="loadingAvailableSeatsInfo">
					        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
					        </div>
				        </div>
			  		</div>
		  		</div>

		  		<div class="panel panel-default" style="margin-top: 20px;">
					<div class="panel-heading"><span>Aulas</span></div>
					<div class="panel-body">
						<table class="table table-borderless table-condensed" ng-show="eventCenters != null">
							<tr><th>Centro de evaluacion:</th><th>Aula:</th><th></th></tr>
							<tr>
								<td>
									<select class="form-control ng-cloak" ng-model="eventCenterId" ng-change="getEventClassrooms()">
										<option ng-repeat="eventCenter in eventCenters" value="{{eventCenter.id}}">{{eventCenter.evaluationCenter.name}}</option>
									</select>
								</td>
								<td>
									<select class="form-control ng-cloak" ng-model="eventClassroomId">
										<option ng-repeat="eventClassroom in eventClassrooms" value="{{eventClassroom.id}}">{{eventClassroom.classroomName}}</option>
									</select>
								</td>
								<td>
									<button class="btn btn-default" ng-disabled="eventClassroomId == null" ng-click="adminEventClassroom()"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Gestionar aula</button>
								</td>
							</tr>
						</table>
						<div class="loading-small" ng-show="eventCenters == null">
				        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
				        </div>
			  		</div>
		  		</div>
			</div>
		</div>
		
		<!-- MODAL PROGRESS -->
		<div class="modal" id="progress-modal" tabindex="-1" role="dialog" aria-labelledby="progress-modalLabel" data-keyboard="false" data-backdrop="static">
		  <div class="modal-dialog" role="document"  style="width: 1024px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="progress-modalLabel">{{progressModalTitle}}</h3>
		      </div>
		      <div class="modal-body">
				<div class="row">
					<div class="col-md-12">
					  	<uib-progressbar class="progress-striped active" value="progress" type="success">
					  		<i>{{progress}}%	</i>
					  	</uib-progressbar>
					</div>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		
		<!-- MODAL CARGA EXTERNA -->
		<div class="modal" id="load-external-modal" tabindex="-1" role="dialog"
			aria-labelledby="load-external-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="load-external-modalLabel">Carga fichero asignaciones</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								  <div  class="container" nv-file-drop="" uploader="uploader" filters="fileFilter, queueLimit">

						            <div class="row">
						
						                <div class="col-md-12"  ng-show="!uploader.queue.length">
						
						                    <h3>Seleccción de fichero</h3>
						
						                    <div ng-show="uploader.isHTML5">
						                        <!-- 3. nv-file-over uploader="link" over-class="className" -->
						                        <div class="well my-drop-zone" nv-file-over="" uploader="uploader"  filters="fileFilter, queueLimit">
						                            Arrastre y suelte el fichero aquí
						                        </div>
						                    </div> 
						                    <input type="file" nv-file-select="" uploader="uploader" filters="fileFilter, queueLimit"/>
						                </div>
						
						               <div class="col-md-9" style="margin-bottom: 40px">
						
						                <div ng-repeat="item in uploader.queue">
						                    <div class="row">
						                        <div class="col-md-12">
						                            <h3>{{ item.file.name }} <span ng-show="uploader.isHTML5" style="font-size: 100%">({{ item.file.size/1024|number:2 }} KB)</span></h3>                          
						                        </div>
						                    </div>
						                    <div class="row">
						                    	<div class="col-md-8">
							                        <div class="progress" style="">
						                                <div class="progress-bar" role="progressbar" ng-style="{ 'width': item.progress + '%' }"></div>
							                        </div>
							                    </div>
						                        <div class="col-md-4">
						                            <button type="button" class="btn btn-success btn-xs" ng-click="uploader.uploadAll()" ng-disabled="!uploader.getNotUploadedItems().length">
						                            	<span class="glyphicon glyphicon-upload"></span> Cargar
							                        </button>
							                        <button type="button" class="btn btn-danger btn-xs" ng-click="uploader.clearQueue();" ng-disabled="!uploader.queue.length">
							                            <span class="glyphicon glyphicon-trash"></span> Borrar
							                        </button>
						                        </div>                      
						                    </div>
						                </div>
						            </div>						
						        </div>								
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					</div>
				</div>
			</div>
		</div>
		
		
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/admin-event-ctrl.js" />" charset="utf-8"></script>
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/uploader-ctrl.js" />" charset="utf-8"></script>	
	</content>
</body>
</html>