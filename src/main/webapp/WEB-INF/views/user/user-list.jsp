<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>
<html>
<head>
	<title>Usuarios</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-app="app" ng-controller="userCtrl" ng-init="init();" ng-cloak>
		<div class="col-md-12">
			<nav class="navbar navbar-default">
  				<div class="container-fluid">
					<form class="navbar-form navbar-left" role="search">
		        		<div class="form-inline has-feedback">
		          			<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" />
		          			<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        		</div>
		      		</form>
		      		<div class="nav navbar-nav navbar-right">
			      		<a href='#' ng-click="showAddUser()" class="btn btn-default navbar-btn" style="margin-right: 10px;">
		        			<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
		        		</a>
	        		</div>
      			</div>
      		</nav>

 			<div ng-show="filterUsers.length > 0 && !loading">
  				<table class="table table-striped">
		        	<tr style="background-color: #bbb;">
		        		<th>Nombre</th>
		        		<th>Apellidos</th>
		        		<th>Nombre de usuario</th>
		        		<th>Email</th>
		        		<th>Rol</th>
		        		<th>Activo</th>
		        		<th></th>
		        	</tr>
		        	<tr ng-repeat="user in filterUsers | orderBy: 'id' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-cloak">
						<td width="23%">{{user.firstName}}</td>
						<td width="25%">{{user.lastName}}</td>
						<td width="12%">{{user.username}}</td>
						<td width="15%">{{user.email || '---'}}</td>
						<td width="10%"><span ng-repeat="rol in user.roles">{{rol.name}}</span></td>
						<td width="10%">
							<span ng-if="user.enabled">Si</span>
							<span ng-if="!user.enabled">No</span>
						</td>
						<td width="5%" class="text-right">
							<a href='#' ng-click="showEditUser(user)" ng-if="user.enabled" title="Editar"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							<a href='#' ng-click="disableUser(user)" ng-if="user.enabled" title="Deshabilitar"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
							<a href='#' ng-click="enableUser(user)" ng-if="!user.enabled" title="Habilitar"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></a>
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
	            		total-items="filterUsers.length"
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
	        <div ng-show="filterUsers.length == 0 && !loading">
	        	<h4>No se han encontrado usuarios</h4>
	        </div>
	        <div class="loading-small" ng-show="loading">
	        	<img src="<c:url value="/web-resources/img/load-small.gif" />" />
	        </div>
		</div>
		
		<!-- MODAL USER -->
		<div class="modal" id="user-modal" tabindex="-1" role="dialog" aria-labelledby="user-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 720px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="user-modalLabel">Usuario</h3>
		      </div>
		      <div class="modal-body">
	      		<form>
	      			<div class="form-group">
						<label path="firstName">Nombre</label>
						<input id="firstName" class="form-control" type="text" ng-model="userData.firstName" />
					</div>
					<div class="form-group">
						<label path="lastName">Apellidos</label>
						<input id="lastName" class="form-control" type="text" ng-model="userData.lastName" />
					</div>
					<div class="form-group">
						<label path="email">Email</label>
						<input id="email" class="form-control" type="text" ng-model="userData.email" />
					</div>
					<div class="form-group">
						<label path="rol">Rol</label>
						<select class="form-control" ng-model="selectedRol" ng-change="selectRol(selectedRol)"  ng-options="rol as rol.name for rol in roles track by rol.id"></select>
					</div>
					<div ng-show="!userData.id" class="form-group">
						<label path="username">Nombre de usuario</label>
						<input id="username" class="form-control" type="text" ng-model="userData.username" />
					</div>
					<div ng-show="!userData.id" class="form-group">
						<label path="password">Contraseña</label>
						<input id="password" class="form-control" type="password" ng-model="password" />
					</div>
					<div ng-show="!userData.id" class="form-group">
						<label path="passwordConfirmation">Confirmar contraseña</label>
						<input id="passwordConfirmation" class="form-control" type="password" ng-model="passwordConfirmation" />
					</div>
	      		</form>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-info" ng-click="showEditPassword(selectedUser)" ng-show="userData.id"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span> Modificar contrase&ntilde;a</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		        <button type="button" class="btn btn-default" ng-click="addUser()" ng-show="!userData.id">A&ntilde;adir</button>
		        <button type="button" class="btn btn-default" ng-click="editUser()" ng-show="userData.id">Editar</button>
		      </div>
		    </div>
		  </div>
	    </div>
	    
	    <!-- MODAL EDIT PASSWORD -->
		<div class="modal" id="edit-password-modal" tabindex="-1" role="dialog" aria-labelledby="edit-password-modalLabel">
		  <div class="modal-dialog" role="document"  style="width: 600px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" id="edit-password-modalLabel">Modificar contraseña de "{{selectedUser.fullName}}"</h3>
		      </div>
		      <div class="modal-body">
	      		<form>
					<div class="form-group">
						<label path="editPassword">Contraseña</label>
						<input id="editPassword" class="form-control" type="password" ng-model="password" />
					</div>
					<div class="form-group">
						<label path="editPasswordConfirmation">Confirmar contraseña</label>
						<input id="editPasswordConfirmation" class="form-control" type="password" ng-model="passwordConfirmation" />
					</div>
	      		</form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
   		      	<button type="button" class="btn btn-info" ng-click="editPassword()"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span> Modificar contrase&ntilde;a</button>
		      </div>
		    </div>
		  </div>
	    </div>
	</div>

	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/user-ctrl.js" />" charset="utf-8"></script>
	</content>
</body>
</html>