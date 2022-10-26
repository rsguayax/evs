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
			$('#tabSchedules').addClass('active');
		});
	</script>
</head>
<body>
	<div ng-controller="scheduleCtrl" ng-init="init(${evaluationEvent.id});">
		<div class="page-header">
	 		<h3>Horarios del evento de evaluaci&oacute;n ${evaluationEvent.name}
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
					<div class="tab-content container">
				        <div class="tab-pane active" ng-cloak>
							<!--
				        	<div class="right-buttons">
								<button class="btn btn-default" ng-click="showAddSchedule()"><span class="glyphicon glyphicon-plus"></span> Añadir horario</button>
							</div>
							-->

							<nav class="navbar navbar-default">
			  					<div class="container-fluid">
									<!--
									<form class="navbar-form navbar-left" role="search" ng-submit="getStudents()">
					        			<div class="form-inline has-feedback">
					          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search" />
					          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
					        			</div>
					      			</form>
					      			-->
					      			<div class="nav navbar-nav navbar-right">
					      				<button type="button" class="btn btn-default navbar-btn" style="margin-right: 10px;" ng-click="showAddSchedule()">
					        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
					        			</button>
				        			</div>
			      				</div>
			      			</nav>

					        <table class="table table-striped" ng-show="schedules.length > 0">
					        	<tr style="background-color: #bbb;">
					        		<th>Nombre</th>
					        		<th></th>
					        	</tr>
					        	<tr ng-repeat="schedule in schedules" class="ng-cloak">
									<td width="80%">{{schedule.name}}</td>
									<td width="20%" align="right">
										<a href="<c:url value="/evaluationevent/{{evaluationEventId}}/schedule/edit/{{schedule.id}}"/>" title="Editar"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
										<a href="#" title="Eliminar" ng-click="deleteSchedule(schedule)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
									</td>
								</tr>
					        </table>
					        <div ng-show="schedules.length == 0">
					        	<h4>No hay horarios definidos para el evento de evaluaci&oacute;n</h4>
					        </div>
					        <div class="loading-small" ng-show="loadingSchedules">
					        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
					        </div>
				        </div>
			    	</div>
				</div>

			</div>
		</div>

		<!-- MODAL ADD SCHEDULE -->
		<div class="modal" id="add-schedule-modal" tabindex="-1" role="dialog" aria-labelledby="add-schedule-modalLabel">
			<div class="modal-dialog" role="document" style="width: 1024px;">
		    	<div class="modal-content"></div>
		  	</div>
		</div>
	</div>

	<content tag="local_script">
		<script type="text/javascript" src="<c:url value="/web-resources/js/controller/schedule-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>