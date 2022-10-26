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
 		<h3>Informes
 			<small>
		  		<a href="<c:url value="/evaluationevent" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
  			</small>
 		</h3>
	</div>
	<div class="row" ng-controller="reportsCtrl" ng-init="init()" ng-cloak>
		<div class="col-md-12">				
			 <div class="panel-group" id="accordion">
		        <div class="panel panel-default">
		            <div class="panel-heading">
		                <h4 class="panel-title">
		               	<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Validación de Bloques de Bancos </a>
		                </h4>
		            </div>
		            <div id="collapseOne" class="panel-collapse collapse collapse">
		                <div class="panel-body">
							<form>
								<div class="form-group">
									<label for="evaluationEventId1">Evento de evaluacion</label>
									<select id="evaluationEventId1" name="evaluationEventId1" class="form-control ng-cloak" ng-model="evaluationEventId">
										<option></option>
										<option ng-repeat="evaluationEvent in evaluationEvents" value="{{evaluationEvent.id}}">{{evaluationEvent.name}}</option>
									</select>
								</div>
								<div class="form-group">
									<label for="departmentId1">Departamento</label>
									<select id="departmentId1" name="departmentId1" class="form-control ng-cloak" ng-model="departmentId">
										<option></option>
										<option ng-repeat="department in departments" value="{{department.id}}">{{department.name}}</option>
									</select>
								</div>							
								<button type="button" class="btn btn-default" ng-click="generateListingPdf(1)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado PDF</button>
								<button type="button" class="btn btn-default" ng-click="generateListingExcel(1)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado Excel</button>
							</form>                
						</div>
		            </div>
		        </div>
		        <div class="panel panel-default">
		            <div class="panel-heading">
		                <h4 class="panel-title">
		                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">Validación de Test </a>
		                </h4>
		            </div>
		            <div id="collapseTwo" class="panel-collapse collapse">
		                <div class="panel-body">
							<form>
								<div class="form-group">
									<label for="evaluationEventId2">Evento de evaluacion</label>
									<select id="evaluationEventId2" name="evaluationEventId2" class="form-control ng-cloak" ng-model="evaluationEventId">
										<option></option>
										<option ng-repeat="evaluationEvent in evaluationEvents" value="{{evaluationEvent.id}}">{{evaluationEvent.name}}</option>
									</select>
								</div>
								<div class="form-group">
									<label for="departmentId2">Departamento</label>
									<select id="departmentId2" name="departmentId2" class="form-control ng-cloak" ng-model="departmentId">
										<option></option>
										<option ng-repeat="department in departments" value="{{department.id}}">{{department.name}}</option>
									</select>
								</div>							
								<button type="button" class="btn btn-default" ng-click="generateListingPdf(2)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado PDF</button>
								<button type="button" class="btn btn-default" ng-click="generateListingExcel(2)"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Generar listado Excel</button>
							</form>                
						</div>
		            </div>
	            </div>
	        </div>
	    </div>
	</div>



	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/reports-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>