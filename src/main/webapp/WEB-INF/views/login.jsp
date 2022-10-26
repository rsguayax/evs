<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title></title>
    <link href="<c:url value="/web-resources/img/favicon.ico" />" rel="shortcut icon" type="image/x-icon" />
    <link href="<c:url value="/web-resources/css/fonts.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap-theme.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/css/custom.css" />" rel="stylesheet" />

	<style type="text/css">
		.error {
			padding: 15px;
			margin-bottom: 20px;
			border: 1px solid transparent;
			border-radius: 4px;
			color: #a94442;
			background-color: #f2dede;
			border-color: #ebccd1;
		}

		.msg {
			padding: 15px;
			margin-bottom: 20px;
			border: 1px solid transparent;
			border-radius: 4px;
			color: #31708f;
			background-color: #d9edf7;
			border-color: #bce8f1;
		}

		.left-side-col {
			background-color: #003F72;
			height: 100%;
			border-right: 3px solid #EAAB00;
		}

		.left-side-col img {
			padding: 5px 0;
		}

		.left-side-col .jumbotron {
			margin-top: 30px;
			background-color: #EAAB00;
			color: #003F72;
		}
	</style>
</head>
<body onload='document.loginForm.username.focus();'>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-8 left-side-col">
				<a href="<c:url value="/" />">
            		<img alt="UTPL - EVS" src="<c:url value="/web-resources/img/logo.png" />" />
            	</a>
            	<div class="jumbotron">
  					<h1>Sistema de Gesti&oacute;n de Evaluaci&oacute;n Online</h1>
  				</div>
			</div>
			<div class="col-lg-4">
				<div class="jumbotron" style="margin-top: 65px;">
					<p>Acceso</p>
					<form name="loginForm"  action="<c:url value='j_spring_security_check' />" method="post">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

						<c:if test="${not empty error}">
							<div class="alert alert-danger" role="alert">${error}</div>
						</c:if>
						<c:if test="${not empty msg}">
							<div class="alert alert-success" role="alert">${msg}</div>
						</c:if>

						<div class="form-group">
							<label for="username">Usuario</label>
							<input id="username" name="username" type="text" class="form-control" placeholder="Introduzca su nombre de usuario" />
						</div>

						<div class="form-group">
							<label for="password">Contrase&ntilde;a</label>
							<input id="password" name="password" type="password" class="form-control" placeholder="Introduzca su contrase&ntilde;a" />
						</div>

						<label class="radio-inline">
  							<input type="radio" name="role" id="roleStudent" value="STUDENT" checked="checked" /> Alumno
						</label>
						<label class="radio-inline">
  							<input type="radio" name="role" id="roleStaff" value="STAFF" /> Personal UTPL
						</label>

						<div class="form-group" style="margin-top: 10px;">
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span> Acceder</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/web-resources/js/jquery-1.11.3.min.js" />"></script>
	<script src="<c:url value="/web-resources/bootstrap-custom/js/bootstrap.min.js"/>"></script>
</body>
</html>