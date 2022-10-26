<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Centro de evaluaci&oacute;n</title>
	<style>
		.ui-select-search {
			width: 100% !important;
		}
	</style>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}
  			<small>
		  		<a href="<c:url value="/evaluationcenter" />" class="pull-right">
		  			<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
		  		</a>
	  		</small>
  		</h3>
	</div>

	<div ng-controller="evaluationCenterCtrl" ng-init="init('${evaluationCenter.id}');">

	<div class="row">
		<div class="col-md-12">
			<sf:form modelAttribute="evaluationCenter">
				<sf:hidden path="id" readonly="true" />
				<div class="form-group">
					<sf:label path="name">Nombre</sf:label>
					<sf:input path="name" id="name" class="form-control" /> <sf:errors path="name" cssClass="error" />
				</div>
				<div class="form-group">
					<sf:label path="code">C&oacute;digo</sf:label>
					<sf:input path="code" id="code" class="form-control" /> <sf:errors path="code" cssClass="error" />
				</div>
				<div class="form-group">
					<sf:label path="name">Descripci&oacute;n</sf:label>
					<sf:textarea path="description" id="description" class="form-control" /> <sf:errors path="description" cssClass="error" />
				</div>
				<div class="form-group">
					<sf:label path="name">Transporte</sf:label>
					<sf:textarea path="transport" id="transport" class="form-control"  maxlength="100"/> <sf:errors path="transport" cssClass="error" />
				</div>
				<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-saved" aria-hidden="true"></span> Guardar</button>
			</sf:form>
		</div>
	</div>
	
	<div class="container" style="margin-top: 20px;">
		<div class="panel-group" id="netAccordion">
		  <div class="panel panel-default" ng-show="evaluationCenterId">
		    <div class="panel-heading">
		      <h4 class="panel-title">
		        <a class="accordion-toggle" data-toggle="collapse" data-parent="#netAccordion" href="#collapseNet"  ng-click="getNets()">
		          <span class="glyphicon glyphicon-plus"></span>
		          Redes
		        </a>
		      </h4>
		    </div>
		    <div id="collapseNet" class="panel-collapse collapse">
		      <div class="panel-body">
		        <div class="row">
					<div class="col-md-12" class="ng-cloak">
						<p align="center" ng-hide="nets.length">-- Sin resultados --</p>
						<table class="table table-striped" ng-show="nets.length">
							<tr style="background-color: #BBBBBB"><th>Id</th><th>Nombre</th><th>Código</th><th>Contraseña</th><th></th></tr>
							<tr ng-repeat="net in nets" class="ng-scope">
								<td width="10%">{{net.id}}</td>
								<td width="25%">{{net.name}}</td>
								<td width="15%">{{net.code}}</td>
								<td width="20%">{{net.password}}</td>								
								<td width="10%" align="right">
									<a href='#' ng-click="showEditNet(net)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
									<a href='#' ng-click="deleteNet(net)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
						</table>

						<button type="button" class="btn btn-default" ng-click="showCreateNet()">Nueva</button>
					</div>
				</div>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
	
	<div class="container" style="margin-top: 20px;">
		<div class="panel-group" id="classroomAccordion">
		  <div class="panel panel-default" ng-show="evaluationCenterId">
		    <div class="panel-heading">
		      <h4 class="panel-title">
		        <a class="accordion-toggle" data-toggle="collapse" data-parent="#classroomAccordion" href="#collapseClassroom"  ng-click="getClassrooms()">
		          <span class="glyphicon glyphicon-plus"></span>
		          Aulas
		        </a>
		      </h4>
		    </div>
		    <div id="collapseClassroom" class="panel-collapse collapse">
		      <div class="panel-body">
		        <div class="row">
					<div class="col-md-12" class="ng-cloak">
						<p align="center" ng-hide="classrooms.length">-- Sin resultados --</p>
						<table class="table table-striped" ng-show="classrooms.length">
							<tr style="background-color: #BBBBBB"><th>Id</th><th>Nombre</th><th>Número asientos</th><th>Ubicación</th><th>Cap</th><th>Red</th><th style="text-align: center;">Disponible</th><th></th></tr>
							<tr ng-repeat="classroom in classrooms" class="ng-scope">
								<td width="5%">{{classroom.id}}</td>
								<td width="20%">{{classroom.name}}</td>
								<td width="10%">{{classroom.seats}}</td>
								<td width="20%">{{classroom.location}}</td>
								<td width="15%"><span ng-if="classroom.cap != null">{{classroom.cap.serialNumber}}</span><span ng-if="classroom.cap == null">----------</span></td>
								<td width="15%"><span ng-if="classroom.net != null">{{classroom.net.code}}</span><span ng-if="classroom.net == null">----------</span></td>								
								<td width="5%" style="text-align: center;">
									<span ng-if="classroom.available" class="glyphicon glyphicon-ok-sign" style="font-size:26px; color: #739600;"></span>
									<span ng-if="!classroom.available" class="glyphicon glyphicon-remove-sign" style="font-size:26px; color: #d52b1e;"></span>
								</td>
								<td width="10%" align="right">
									<a href='#' ng-click="showEditClassroom(classroom)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
									<a href='#' ng-click="deleteClassroom(classroom)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
						</table>

						<button type="button" class="btn btn-default" ng-click="showCreateClassroom()">Nueva</button>
					</div>
				</div>
		      </div>
		    </div>
		  </div>
		</div>
	</div>

    <div class="container" style="margin-top: 20px;">
		<div class="panel-group" id="accordion">
		  <div class="panel panel-default" ng-show="evaluationCenterId">
		    <div class="panel-heading">
		      <h4 class="panel-title">
		        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne1"  ng-click="getAddresses()">
		          <span class="glyphicon glyphicon-plus"></span>
		          Direcciones
		        </a>
		      </h4>
		    </div>
		    <div id="collapseOne1" class="panel-collapse collapse">
		      <div class="panel-body">
		        <div class="row">
					<div class="col-md-12" class="ng-cloak">
						<p align="center" ng-hide="addresses.length">-- Sin resultados --</p>
						<table class="table table-striped" ng-show="addresses.length">
							<tr style="background-color: #BBBBBB"><th>Id</th><th>Tipo</th><th>Nombre</th><th>N&uacute;mero</th><th>Teléfono</th><th>Ciudad</th><th></th></tr>
							<tr ng-repeat="address in addresses" class="ng-scope">
								<td width="10%">{{address.id}}</td>
								<td width="20%" style="text-transform: capitalize;">{{address.type}}</td>
								<td width="35%">{{address.name}}</td>
								<td width="5%">{{address.number}}</td>
								<td width="10%">{{address.phone}}</td>
								<td width="10%">{{address.city}}</td>
								<td align="right">
									<a href='#' ng-click="showEditAddress(address)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
									<a href='#' ng-click="deleteAddress(address)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
								</td>
							</tr>
						</table>
						<button type="button" class="btn btn-default" ng-click="showCreateAddress()">Nueva</button>
					</div>
				</div>
		      </div>
		    </div>
		  </div>
		</div>
	</div>

	<sec:authorize access="@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')">
	<div class="container" style="margin-top: 20px;">
		<div class="panel-group" id="centerAccordion">
		  <div class="panel panel-default" ng-show="evaluationCenterId">
		    <div class="panel-heading">
		      <h4 class="panel-title">
		        <a class="accordion-toggle" data-toggle="collapse" data-parent="#centerAccordion" href="#collapseCenter" ng-click="getCenters()">
		          <span class="glyphicon glyphicon-plus"></span>
		          Centros educativos
		        </a>
		      </h4>
		    </div>
		    <div id="collapseCenter" class="panel-collapse collapse">
      			<div class="panel-body">
			        <div class="row">
						<div class="col-md-12" class="ng-cloak">
							<p align="center" ng-hide="centers.length">-- Sin resultados --</p>
							<table class="table table-striped" ng-show="centers.length">
								<tr style="background-color: #BBBBBB"><th>Id</th><th>Nombre</th><th>Dirección</th><th></th></tr>
								<tr ng-repeat="center in centers" class="ng-scope">
									<td width="10%">{{center.id}}</td>
									<td width="30%">{{center.name}}</td>
									<td width="50%">{{center.address}}</td>
									<td width="10%" align="right">
										<a href='#' title="Editar" ng-click="showEditCenter(center)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
										<a href='#' title="Eliminar" ng-click="deleteCenter(center)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
									</td>
								</tr>
							</table>

							<button type="button" class="btn btn-default" ng-click="showAddCenters()">Añadir</button>

							<!-- Crea uno y lo añade -->
							<button type="button" class="btn btn-default" ng-click="showCreateCenter()">Nuevo</button>
						</div>
					</div>
		      	</div>
		    </div>
		  </div>
		</div>
	</div>
	</sec:authorize>



	<div class="container" style="margin-top: 20px;">
		<div class="panel-group" id="capAccordion">
		  <div class="panel panel-default" ng-show="evaluationCenterId">
		    <div class="panel-heading">
		      <h4 class="panel-title">
		        <a class="accordion-toggle" data-toggle="collapse" data-parent="#capAccordion" href="#collapseCap" ng-click="getCaps()">
		          <span class="glyphicon glyphicon-plus"></span>
		          CAPs
		        </a>
		      </h4>
		    </div>
		    <div id="collapseCap" class="panel-collapse collapse">
      			<div class="panel-body">
			        <div class="row">
						<div class="col-md-12" class="ng-cloak">
							<p align="center" ng-hide="caps.length">-- Sin resultados --</p>
							<table class="table table-striped" ng-show="caps.length">
								<tr style="background-color: #bbb;">
									<th>Id</th>
									<th>Nombre</th>
									<th>N&uacute;mero de serie</th>
									<th>Nombre de red (SSID)</th>
									<th>Clave de red</th>
									<th>Servidor</th>
									<th></th>
								</tr>
								<tr ng-repeat="cap in caps" class="ng-scope">
									<td width="10%">{{cap.id}}</td>
									<td width="20%">{{cap.name}}</td>
									<td width="15%">{{cap.serialNumber}}</td>
									<td width="15%">{{cap.ssid}}</td>
									<td width="15%">{{cap.key}}</td>
									<td width="15%"><span ng-if="cap.server != null">{{cap.server.code}}</span><span ng-if="cap.server == null">----------</span></td>
									
									<td width="10%" align="right">
										<a href='#' title="Editar" ng-click="showEditCap(cap)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
										<a href='#' title="Eliminar" ng-click="deleteCap(cap)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
									</td>
								</tr>
							</table>

							<button type="button" class="btn btn-default" ng-click="showCreateCap()">Nuevo</button>
						</div>
					</div>
		      	</div>
		    </div>
		  </div>
		</div>
	</div>

	<!-- MODAL DIRECCIONES -->
	<div class="modal fade" id="address-modal" tabindex="-1" role="dialog" aria-labelledby="address-modalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="address-modalLabel">Dirección</h4>
		      </div>
		      <div class="modal-body">
		      	<div class="row">
					<div class="col-md-12">
						<form name="addressForm">
				     		<fieldset>
								<label>Nombre</label>
					        	<input type="text" name="address_name" id="address_name" ng-model="address.name" class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
					        	<div class="help-block" ng-messages="addressForm.address_name.$error" ng-if="addressForm.address_name.$touched"><div ng-messages-include="error-messages"></div></div>

					        	<label>Tel&eacute;fono</label>
					        	<input type="text" name="address_phone" id="address_phone" ng-model="address.phone"  class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
					        	<div class="help-block" ng-messages="addressForm.address_phone.$error"  ng-if="addressForm.address_phone.$touched"><div ng-messages-include="error-messages"></div></div>

					        	<label>N&uacute;mero</label>
					        	<input type="text" name="address_number" id="address_number" ng-model="address.number"  class="form-control"  ng-minlength="1" ng-maxlength="25" required/>
					        	<div class="help-block" ng-messages="addressForm.address_number.$error"  ng-if="addressForm.address_number.$touched"><div ng-messages-include="error-messages"></div></div>

					        	<label>Ciudad</label>
					        	<input type="text" name="address_city" id="address_city" ng-model="address.city" class="form-control" ng-minlength="1" ng-maxlength="250" required/>
					        	<div class="help-block" ng-messages="addressForm.address_city.$error"  ng-if="addressForm.address_city.$touched"><div ng-messages-include="error-messages"></div></div>

					        	<label>Tipo v&iacute;a</label>
					        	<select name="address_type" id="address_type" ng-model="address.type" class="form-control" ng-minlength="1" ng-maxlength="250" required/>
					        		<option value="calle">Calle</option>
					        		<option value="avenida">Avenida</option>
					        		<option value="carretera">Carretera</option>
					        	</select>
					        	<div class="help-block" ng-messages="addressForm.address_type.$error"  ng-if="addressForm.address_name.$touched"><div ng-messages-include="error-messages"></div></div>

					        	<input type="hidden" name="address_id" id="address_id" ng-model="address.id"  class="form-control"/>
				        	</fieldset>
			        	</form>
			        </div>
		        </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		        <button ng-if="!address.id" type="button" class="btn btn-default" ng-click="createAddress(address)" type="submit" ng-show="addressForm.$valid">Crear</button>
		        <button ng-if="address.id" type="button" class="btn btn-default" ng-click="editAddress(address)" ng-show="addressForm.$valid">Modificar</button>
		      </div>
		    </div>
		  </div>
	</div>


	<!-- MODAL AULAS -->
	<div class="modal fade" id="classroom-modal" tabindex="-1" role="dialog" aria-labelledby="classroom-modalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="classroom-modalLabel">Aula</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
					<form name="classroomForm">
			     		<fieldset>
							<label>Nombre</label>
				        	<input type="text" name="classroom_name" id="classroom_name" ng-model="classroom.name" class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="classroomForm.classroom_name.$error" ng-if="classroomForm.classroom_name.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Ubicaci&oacute;n</label>
				        	<input type="text" name="classroom_location" id="classroom_location" ng-model="classroom.location"  class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="classroomForm.classroom_location.$error"  ng-if="classroomForm.classroom_location.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>N&uacute;mero asientos</label>
				        	<input type="number" name="classroom_seats" id="classroom_seats" ng-model="classroom.seats"  class="form-control"  ng-minlength="1" ng-maxlength="25" required/>
				        	<div class="help-block" ng-messages="classroomForm.classroom_number.$error"  ng-if="classroomForm.classroom_seats.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Cap</label>
				        	<select class="form-control" name="classroom_cap" id="classroom_cap" ng-model="classroom.cap" ng-options="cap as cap.name for cap in capsunassigned track by cap.id">
				        		<option value="">Seleccione un cap</option>
				        	</select>
					        	<div class="help-block" ng-messages="classroomForm.classroom_cap.$error"  ng-if="classroomForm.classroom_cap.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Red</label>
				        	<select class="form-control" name="classroom_net" id="classroom_net" ng-model="classroom.net" ng-options="net as net.name for net in nets track by net.id">
				        		<option value="">Seleccione una red</option>
				        	</select>
					        	<div class="help-block" ng-messages="classroomForm.classroom_net.$error"  ng-if="classroomForm.classroom_net.$touched"><div ng-messages-include="error-messages"></div></div>
				        	
				        	
				        	
				        	
				        	<label>Disponible</label>
				        	<select class="form-control" name="classroom_available" id="classroom_available" ng-model="classroom.available" ng-options="o.v as o.n for o in [{n: 'Si', v: true}, {n: 'No', v: false}]" required></select>
				        	<div class="help-block" ng-messages="classroomForm.classroom_available.$error"  ng-if="classroomForm.cclassroom_available.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<input type="hidden" name="classroom_id" id="classroom_id" ng-model="classroom.id"  class="form-control"/>
			        	</fieldset>
		        	</form>
		        </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button ng-if="!classroom.id" type="button" class="btn btn-default" ng-click="createClassroom(classroom)" type="submit" ng-show="classroomForm.$valid">Crear</button>
	        <button ng-if="classroom.id" type="button" class="btn btn-default" ng-click="editClassroom(classroom)" ng-show="classroomForm.$valid">Modificar</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- MODAL CAPs -->
	<div class="modal fade" id="cap-modal" tabindex="-1" role="dialog" aria-labelledby="cap-modalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
		  		<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="cap-modalLabel">CAP</h4>
			  	</div>
			  	<div class="modal-body">
					<div class="row">
						<div class="col-md-12">
							<form name="capForm">
								<input type="hidden" id="classroom_id" name="cap_id" ng-model="cap.id"  class="form-control"/>

								<fieldset>
									<label>Nombre</label>
						        	<input type="text" id="cap_name" name="cap_name" ng-model="cap.name" class="form-control"  ng-minlength="1" ng-maxlength="40" required />
						        	<div class="help-block" ng-messages="capForm.cap_name.$error" ng-if="capForm.cap_name.$touched"><div ng-messages-include="error-messages"></div></div>
								</fieldset>

								<fieldset>
									<label>N&uacute;mero de serie</label>
						        	<input type="text" id="cap_serialNumber" name="cap_serialNumber" ng-model="cap.serialNumber" class="form-control"  ng-minlength="1" ng-maxlength="250" required />
						        	<div class="help-block" ng-messages="capForm.cap_serialNumber.$error" ng-if="capForm.cap_serialNumber.$touched"><div ng-messages-include="error-messages"></div></div>
								</fieldset>

								<fieldset>
									<label>Nombre de red (SSID)</label>
						        	<input type="text" id="cap_ssid" name="cap_ssid" ng-model="cap.ssid" class="form-control"  ng-minlength="1" ng-maxlength="40" required />
						        	<div class="help-block" ng-messages="capForm.cap_ssid.$error" ng-if="capForm.cap_ssid.$touched"><div ng-messages-include="error-messages"></div></div>
								</fieldset>

								<fieldset>
									<label>Clave de red</label>
						        	<input type="text" id="cap_key" name="cap_key" ng-model="cap.key" class="form-control"  ng-minlength="1" ng-maxlength="40" required />
						        	<div class="help-block" ng-messages="capForm.cap_key.$error" ng-if="capForm.cap_key.$touched"><div ng-messages-include="error-messages"></div></div>
								</fieldset>
								
								<label>Servidor</label>
						        	<select class="form-control" name="cap_server" id="cap_server" ng-model="cap.server" ng-options="server as server.name for server in servers track by server.id" required>
						        		<option value="">Seleccione un servidor</option>
						        	</select>
					        	<div class="help-block" ng-messages="classroomForm.classroom_server.$error"  ng-if="classroomForm.classroom_server.$touched"><div ng-messages-include="error-messages"></div></div>
								

							</form>
						</div>
					</div>
			  	</div>
			  	<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<button ng-if="!cap.id" type="button" class="btn btn-default" ng-click="createCap(cap)" type="submit" ng-show="capForm.$valid">Crear</button>
					<button ng-if="cap.id" type="button" class="btn btn-default" ng-click="editCap(cap)" ng-show="capForm.$valid">Modificar</button>
			  	</div>
			</div>
		</div>
	</div>


	 <!-- MODAL CENTROS EDUCATIVOS -->
	<div class="modal" id="add-centers-modal" tabindex="-1" role="dialog" aria-labelledby="add-centers-modalLabel">
	  <div class="modal-dialog" role="document"  style="width: 1024px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="add-centers-modalLabel">Asignaci&oacute;n de centros educativos</h3>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
 						<ui-select multiple ng-model="selectedCenters.selected" theme="bootstrap" ng-disabled="disabled" sortable="true"
					  				close-on-select="false">
					    <ui-select-match placeholder="Seleccione centro (limitado 50 primeros resultados)...">{{$item.name}} &lt;{{$item.code}}&gt;</ui-select-match>
					    <ui-select-choices repeat="center in allcenters | propsFilter: {name: $select.search, code: $select.search} | limitTo: 50">
					      <div ng-bind-html="name | highlight: $select.search"></div>
					      <small>
					        Nombre: <span ng-bind-html="''+center.name| highlight: $select.search"></span>
					        Código: <span ng-bind-html="''+center.code| highlight: $select.search"></span>
					      </small>
					    </ui-select-choices>
					  </ui-select>
				</div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button type="button" class="btn btn-default" ng-click="addCenters()">A&ntilde;adir</button>
	      </div>
	    </div>
	  </div>
    </div>


    <!-- MODAL CREACION CENTRO -->
	<div class="modal fade" id="edit-center-modal" tabindex="-1" role="dialog" aria-labelledby="edit-center-modalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="edit-center-modalLabel">Centro educativo</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
					<form name="editcenterForm">
			     		<fieldset>
							<label>Nombre</label>
				        	<input type="text" name="center_name" id="center_name" ng-model="center.name" class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="editcenterForm.center_name.$error" ng-if="editcenterForm.center_name.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>C&oacute;digo</label>
				        	<input type="text" name="center_code" id="center_code" ng-model="center.code"  class="form-control"  ng-minlength="1" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="editcenterForm.center_code.$error"  ng-if="editcenterForm.center_code.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Tipo</label>
				        	<input type="text" name="center_type" id="center_type" ng-model="center.type"  class="form-control"  ng-minlength="1" ng-maxlength="25" required/>
				        	<div class="help-block" ng-messages="editcenterForm.center_type.$error"  ng-if="editcenterForm.center_type.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Direcci&oacute;n</label>
				        	<input type="text" name="center_address" id="center_address" ng-model="center.address" class="form-control" ng-minlength="1" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="editcenterForm.center_address.$error"  ng-if="editcenterForm.center_address.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<input type="hidden" name="center_id" id="center_id" ng-model="center.id"  class="form-control"/>
			        	</fieldset>
		        	</form>
		        </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button ng-if="!center.id" type="button" class="btn btn-default" ng-click="createCenter(center)" type="submit" ng-show="editcenterForm.$valid">Crear</button>
	        <button ng-if="center.id" type="button" class="btn btn-default" ng-click="editCenter(center)" ng-show="editcenterForm.$valid">Modificar</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	<!-- MODAL REDES -->
	<div class="modal fade" id="net-modal" tabindex="-1" role="dialog" aria-labelledby="net-modalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="net-modalLabel">Aula</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
				<div class="col-md-12">
					<form name="netForm">
			     		<fieldset>
							<label>Nombre</label>
				        	<input type="text" name="net_name" id="net_name" ng-model="net.name" class="form-control"  ng-minlength="2" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="netForm.net_name.$error" ng-if="netForm.net_name.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>C&oacute;digo</label>
				        	<input type="text" name="net_code" id="net_location" ng-model="net.code"  class="form-control"  ng-minlength="2" ng-maxlength="250" required/>
				        	<div class="help-block" ng-messages="netForm.net_code.$error"  ng-if="netForm.net_code.$touched"><div ng-messages-include="error-messages"></div></div>

				        	<label>Password</label>
				        	<input type="text" name="net_password" id="net_password" ng-model="net.password"  class="form-control"  ng-minlength="2" ng-maxlength="25" required/>
				        	<div class="help-block" ng-messages="netForm.net_password.$error"  ng-if="netForm.net_password.$touched"><div ng-messages-include="error-messages"></div></div>
 
							<label>Servidores</label>
				  			<ui-select multiple ng-model="selectedServers.selected"	theme="bootstrap" ng-disabled="disabled" sortable="true" close-on-select="false">
								<ui-select-match placeholder="{{selectLabel}}">{{$item.name}}&lt;{{$item.code}}&gt;</ui-select-match>
								<ui-select-choices repeat="server in allservers | propsFilter: {name: $select.search, code: $select.search} | limitTo: 50">
									<div ng-bind-html="name | highlight: $select.search"></div>
									<small>
										Nombre: <span ng-bind-html="''+server.name| highlight: $select.search"></span>
										C&oacute;digo: <span ng-bind-html="''+server.code| highlight: $select.search"></span>
									</small>
								</ui-select-choices>
							</ui-select>
 					       
				        	<input type="hidden" name="net_id" id="net_id" ng-model="net.id"  class="form-control"/>
			        	</fieldset>
		        	</form>
		        </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        <button ng-if="!net.id" type="button" class="btn btn-default" ng-click="createNet(net)" type="submit" ng-show="netForm.$valid">Crear</button>
	        <button ng-if="net.id" type="button" class="btn btn-default" ng-click="editNet(net)" ng-show="netForm.$valid">Modificar</button>
	      </div>
	    </div>
	  </div>
	</div>

	</div>

	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/evaluation-center-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>