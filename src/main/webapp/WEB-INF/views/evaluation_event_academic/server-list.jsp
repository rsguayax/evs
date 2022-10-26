<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
<title>Servidores</title>
</head>
<body>
	<div class="page-header">
  		<h3>${headText}</h3>
	</div>
	<div class="row" ng-controller="serverCtrl" ng-init="init();">
		<div class="col-md-12">
			<div class="row" style="margin-top: 50px;" ng-show="loading">
				<p align="center"><img src="<c:url value="/web-resources/img/load-small.gif" />"></p>
			</div>
		    <div class="row" ng-show="!loading && filterServers" ng-cloak>
				<nav class="navbar navbar-default">
  					<div class="container-fluid">
						<form class="navbar-form navbar-left" role="search">
		        			<div class="form-inline has-feedback">
		          				<input type="text" class="form-control" placeholder="B&uacute;squeda" data-ng-model="search.name" size="60"/>
		          				<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
		        			</div>
		      			</form>	
		      			<div class="nav navbar-nav navbar-right">
			      			<a href="server/new" class="btn btn-default navbar-btn" style="margin-right: 10px;">
		        				<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> A&ntilde;adir
		        			</a>
	        			</div>	      			
      				</div>
      			</nav>
		        <table class="table table-striped">
					<tr style="background-color: #bbb;">
						<th>Id</th>
						<th>Nombre</th>
						<th>Código</th>
						<th>URL</th>
						<th></th>
					</tr>
		        	<tr ng-repeat="server in filterServers  | orderBy: 'name' | startPaginate: (currentPage - 1) * pageSize | limitTo: pageSize" class="ng-scope" >
						<td width="5%">{{server.id}}</td>
						<td width="40%">{{server.name}}</td>
						<td width="10%">{{server.code}}</td>
						<td width="20%">{{server.url}}</td>
						<td width="10%" align="right">
							<a href='<s:url value="server/edit/{{server.id}}"/>' title="Editar servidor"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							<a href='<s:url value="server/delete/{{server.id}}"/>' title="Eliminar servidor"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
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
	            		total-items="filterServers.length"
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
	<content tag="local_script">
	    <script src="<c:url value="/web-resources/js/controller/server-ctrl.js" />"></script>
	</content>
</body>
</html>