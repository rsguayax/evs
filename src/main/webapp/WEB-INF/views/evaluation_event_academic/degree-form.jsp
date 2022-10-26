<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Titulacion</title>
</head>
<body>
	<div ng-controller="degreeCtrl" ng-init="init(${degree.id});">
		<div class="page-header">
			<h3>${headText}
				<small>
					<a href="<c:url value="/degree" />" class="pull-right">
						<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
					</a>
				</small>
			</h3>
			
		</div>
		<div class="row" ng-cloak>
			<div class="col-md-12">
				<sf:form modelAttribute="degree">
					<sf:hidden path="id" readonly="true" />
					<div class="form-group">
						<sf:label path="name">Nombre</sf:label>
						<sf:input path="name" id="name" class="form-control" ></sf:input>
						 <sf:errors path="name" cssClass="error" />
					</div>
							</div>
						</div>
					</div>
					<button type="submit" class="btn btn-default">Guardar</button>
				</sf:form>
			</div>
		</div>
	</div>
	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/degree-ctrl.js" />"></script>
	</content>
</body>
</html>