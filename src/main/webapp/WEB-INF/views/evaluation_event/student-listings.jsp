<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<html>
<head>
	<title>Listados de estudiantes</title>
</head>
<body>
	<div class="page-header">
 		<h3>Listados de estudiantes del evento de evaluaci&oacute;n ${evaluationEvent.name}
 			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
  			</small>
 		</h3>
	</div>
	<div class="row" ng-controller="studentListingsCtrl" ng-init="init(${evaluationEvent.id});" ng-cloak>
		<div class="col-md-12">	
			<div class="panel panel-primary">
	            <div class="panel-heading">
	                <h4 class="panel-title">
	                    Generación listados
	                </h4>
	            </div>              
       	    	<div class="panel-body">			
					<div class="panel-group" id="accordion">
				        <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Listados generales <a href="#" ng-click="showInfo(1)"><span class="glyphicon glyphicon-info-sign pull-right" style="color:#333333" aria-hidden="true"></span></a> </a>
				                </h4>
				            </div>
				            <div id="collapseOne" class="panel-collapse collapse collapse">
				                <div class="panel-body">
									<form>
										<div class="form-group">
											<label for="eventCenterId">Centro de evaluaci&oacute;n</label>
											<select id="eventCenterId" name="eventCenterId" class="form-control ng-cloak" ng-model="eventCenterId" ng-change="getEventClassrooms(1)">
												<option></option>
												<option ng-repeat="eventCenter in eventCenters" value="{{eventCenter.id}}">{{eventCenter.evaluationCenter.name}}</option>
											</select>
										</div>
										<div class="form-group">
											<label for="eventClassroomId">Aula</label>
											<select id="eventClassroomId" name="eventClassroomId" class="form-control ng-cloak" ng-model="eventClassroomId" ng-change="getClassroomTimeBlocks(1)">
												<option></option>
												<option ng-repeat="eventClassroom in eventClassrooms" value="{{eventClassroom.id}}">{{eventClassroom.classroomName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label for="classroomTimeBlockId">Jornada</label>
											<select id="classroomTimeBlockId" name="classroomTimeBlockId" class="form-control ng-cloak" ng-model="classroomTimeBlockId">
												<option></option>
												<option ng-repeat="classroomTimeBlock in classroomTimeBlocks" value="{{classroomTimeBlock.id}}">
													{{classroomTimeBlock.startDate | date : "EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}} |
													{{classroomTimeBlock.startDate | date : 'HH:mm' : 'UTC'}} - {{classroomTimeBlock.endDate | date : 'HH:mm' : 'UTC'}}
												</option>
											</select>
										</div>
										<button type="button" class="btn btn-default" ng-click="generateListing()" ng-disabled="checkForm(1)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado</button>
										<button type="button" class="btn btn-default" ng-click="generateListingExcel()" ng-disabled="checkForm(2)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado contraseñas</button>
									
									</form>                
								</div>
				            </div>
				        </div>
				        <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">Listado por jornada <a href="#" ng-click="showInfo(2)"><span class="glyphicon glyphicon-info-sign pull-right" style="color:#333333" aria-hidden="true"></span></a></a>
				                </h4>
				            </div>
				            <div id="collapseTwo" class="panel-collapse collapse">
				                <div class="panel-body">
									<form>
										<div class="form-group">
											<label for="journeyId">Jornada</label>
											<select id="journeyId" name="journeyId" class="form-control ng-cloak" ng-model="journeyId">
												<option></option>
												<option ng-repeat="journey in journeys" value="{{journey.id}}">
													{{journey.startDate | date : "EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}} |
													{{journey.startDate | date : 'HH:mm' : 'UTC'}} - {{journey.endDate | date : 'HH:mm' : 'UTC'}}
												</option>
											</select>
										</div>
										<button type="button" class="btn btn-default" ng-click="generateJourneysListing()" ng-disabled="checkFormJourney()"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado</button>
									</form>                
								</div>
				            </div>
				        </div>
			        </div>
	        	</div>
	        </div>
		        
		        
		        
		        
		    <!-- Documentacion --> 
	       
       		<div class="panel panel-info">
	            <div class="panel-heading">
	                <h4 class="panel-title">
	                    Generación documentos <a href="#" ng-click="showFiles()" alt="Documentacion generada" title="Documentacion generada"><span class="glyphicon glyphicon-folder-open pull-right" style="color:#333333" aria-hidden="true"></span></a> 
	                </h4>
	            </div>              
        	    <div class="panel-body">
	        	     <div class="panel-group" id="accordion2">
				        <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">Centro, aula y jornada <a href="#" ng-click="showInfo(4)"><span class="glyphicon glyphicon-info-sign pull-right" style="color:#333333" aria-hidden="true"></span></a> </a>
				                </h4>
				            </div>
				            <div id="collapseThree" class="panel-collapse collapse collapse">
				                <div class="panel-body">
									<form>
										<div class="form-group">
											<label for="eventCenterIdDoc">Centro de evaluaci&oacute;n</label>
											<select id="eventCenterIdDoc" name="eventCenterIdDoc" class="form-control ng-cloak" ng-model="eventCenterIdDoc" ng-change="getEventClassrooms(2)">
												<option></option>
												<option ng-repeat="eventCenter in eventCentersDoc" value="{{eventCenter.id}}">{{eventCenter.evaluationCenter.name}}</option>
											</select>
										</div>
										<div class="form-group">
											<label for="eventClassroomIdDoc">Aula</label>
											<select id="eventClassroomIdDoc" name="eventClassroomIdDoc" class="form-control ng-cloak" ng-model="eventClassroomIdDoc" ng-change="getClassroomTimeBlocks(2)">
												<option></option>
												<option ng-repeat="eventClassroom in eventClassroomsDoc" value="{{eventClassroom.id}}">{{eventClassroom.classroomName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label for="classroomTimeBlockIdDoc">Jornada</label>
											<select id="classroomTimeBlockIdDoc" name="classroomTimeBlockIdDoc" class="form-control ng-cloak" ng-model="classroomTimeBlockIdDoc">
												<option></option>
												<option ng-repeat="classroomTimeBlock in classroomTimeBlocksDoc" value="{{classroomTimeBlock.id}}">
													{{classroomTimeBlock.startDate | date : "EEEE',' d 'de' MMMM 'de' yyyy" : 'UTC'}} |
													{{classroomTimeBlock.startDate | date : 'HH:mm' : 'UTC'}} - {{classroomTimeBlock.endDate | date : 'HH:mm' : 'UTC'}}
												</option>
											</select>
										</div>
										<button type="button" class="btn btn-default" ng-click="generateDocuments(1)" ng-disabled="checkFormDoc(1) || disableDocButton"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado</button>							
									</form>                
								</div>
				            </div>
				        </div>	
				                
				        <div class="panel panel-default">
				            <div class="panel-heading">
				                <h4 class="panel-title">
				                    <a data-toggle="collapse" data-parent="#accordion2" href="#collapseFour">Cédula <a href="#" ng-click="showInfo(5)"><span class="glyphicon glyphicon-info-sign pull-right" style="color:#333333" aria-hidden="true"></span></a> </a>
				                </h4>
				            </div>
				            <div id="collapseFour" class="panel-collapse collapse collapse">
				                <div class="panel-body">
									<form>
										<div class="form-group">
											<label for="identification">Cédula alumno</label>
											<input id="identification" name="identification" class="form-control ng-cloak" ng-model="identification">
										</div>								
										<button type="button" class="btn btn-default" ng-click="generateDocumentsOnline(2)" ng-disabled="!checkIdentification()"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado</button>							
									</form>                
								</div>
				            </div>
				        </div>		        
				    </div>
			    </div>			    
		    </div>
		</div>
		
		<!-- MODAL DOCUMENTACION -->
		<div class="modal" id="show-files-modal" tabindex="-1" role="dialog" aria-labelledby="show-files-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
	    		<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h3 class="modal-title" id="show-files-modalLabel">Descarga documentación</h3>
	      			</div>
	      			<div class="modal-body">
	      				<div class="row">
							<div class="col-md-12">
								<div class="well" ng-if="zipFile">
									Archivo ZIP de evento: <b>{{zipFile.fileName}}</b> generado el <b>{{zipFile.createdDate | date:"dd/MM/yyyy"}}</b>
									 a las <b>{{zipFile.createdDate | date:"HH:mm"}}</b> <a href="#" target="_self"><span ng-click="downloadFile(zipFile.id)" class="glyphicon glyphicon-download pull-right" style="color:#333333" aria-hidden="true"  alt="Descargar" title="Descargar"></span></a></b>
								</div>
								<div class="well" ng-if="!zipFile">
									No se ha generado archivo ZIP de evento.						
								</div>
							</div>
	        			</div>
	      				<div class="row">
							<div class="col-md-12">
								<h4 class="modal-title" id="show-files-modalLabel">Seleccione centro de evaluación</h4>
					 			<select id="eventCenterFileId" name="eventCenterFileId" class="form-control ng-cloak" ng-model="eventCenterFileId" ng-change="loadFiles()">
									<option></option>
									<option ng-repeat="eventCenter in eventCenters" value="{{eventCenter.id}}">{{eventCenter.evaluationCenter.name}}</option>
								</select>
							</div>
	        			</div>
	        			<div class="row">
	        				<div class="col-md-12"  ng-if="eventCenterFileId">
	        					<br/><br/>
								<table class="table" ng-if="files.length > 0">
									<thead class="thead-light">
										<tr>
											<th>Nombre fichero</th>
											<th>Aula</th>
											<th>F. test</th>
											<th>Jornada</th>
											<th>F. creación</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
										<tr ng-repeat="file in files">
											<th>{{file.fileName}}</th>
											<td ng-if="file.classroomCode">{{file.classroomCode}}</td><td ng-if="!file.classroomCode">---</td>
											<td ng-if="file.startDateTest">{{file.startDateTest | date:"dd/MM/yyyy"}}</td><td ng-if="!file.startDateTest">---</td>
											<td ng-if="file.startDateTest">{{file.startDateTest | date:"HH:mm"}} - {{file.endDateTest | date:"HH:mm"}}</td><td ng-if="!file.startDateTest">---</td>
											<td ng-if="file.createdDate">{{file.createdDate | date:"dd/MM/yyyy HH:mm"}}</td><td ng-if="!file.createdDate">---</td>
											<td><a href="#" target="_self"><span ng-click="downloadFile(file.id)" class="glyphicon glyphicon-download pull-right" style="color:#333333" aria-hidden="true" alt="Descargar" title="Descargar"></span></a></td>
										</tr>								
									</tbody>
								</table>
								<p ng-if="files.length == 0" class="text-center">-- No se han encontrado archivos asociados --</p>
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
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/student-listings-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>