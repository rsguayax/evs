<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>${headText}</title>
    <link href="<c:url value="/web-resources/css/fonts.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap-theme.min.css" />" rel="stylesheet">
    <link href="<c:url value="/web-resources/css/custom.css" />" rel="stylesheet" />

    <script src="<c:url value="/web-resources/js/jquery-1.11.3.min.js" />"></script>
	<style>
		.col-centered {
		    float: none;
		    margin: 0 auto;
		}
	</style>
</head>
<body onload='document.loginForm.username.focus();'>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-8 col-centered">
				<div class="jumbotron" style="margin-top: 50px;">
					<h2 align="left">${headText}</h2>
					<p align="left">${errorMessage}</p>
					<button class="btn btn-danger" onclick="window.history.back()">Volver</button>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/web-resources/bootstrap-custom/js/bootstrap.min.js"/>"></script>
</body>
</html>