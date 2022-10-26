<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" ng-app="integratorApp">
<head>
	<meta charset="UTF-8">
    <title></title>
    <link href="<c:url value="/web-resources/img/favicon.ico" />" rel="shortcut icon" type="image/x-icon" />
    <link href="<c:url value="/web-resources/css/fonts.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/bootstrap-custom/css/bootstrap-theme.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/web-resources/css/custom.css" />" rel="stylesheet" />

    <link rel="stylesheet" href="<c:url value="/web-resources/css/print.css" />"" type="text/css" media="print" />
    <link rel="stylesheet" href="<c:url value="/web-resources/js/alertify.js-0.3.11/themes/alertify.core.css" />"/>
	<link rel="stylesheet" href="<c:url value="/web-resources/js/alertify.js-0.3.11/themes/alertify.bootstrap.css" />"/>
	<link rel="stylesheet" href="<c:url value="/web-resources/js/angular-flash/angular-flash.css" />"/>
	<link rel="stylesheet" href="<c:url value="/web-resources/js/select2/css/select2.min.css" />"/>

    <script src="<c:url value="/web-resources/js/jquery-1.11.3.min.js" />"></script>

    <script>
  		$(function() {
  			var url = window.location.href;
	  		$('ul.nav a').filter(function() {
	  			var path = $(this).data("path");
				var regex = new RegExp("\\d+\/" + path);
		  		if (url.indexOf($(this).data("path")) > -1 && url.search(regex) == -1) {
	  		    	return true;
		  		} else {
					return false;
		  		}
	  		}).parent().addClass('active');


  			alertify.set({ labels: {
  			    ok     : "Aceptar",
  			    cancel : "Cancelar"
  			} });


  			$("#logout-link").click(function(e) {
  				e.preventDefault();
  				$("#logout-form").submit();
  			});

  			$('.message-link').on('click', function(ev) {
  				ev.preventDefault();
  				var message = $(this).data("message");
  				var link = $(this);
  				alertify.confirm(message, function (e) {
  				    if (e) {
  				       window.location = link.attr('href');
  				    }
  				});

  			});

  			$('.collapse').on('shown.bs.collapse', function(){
  				$(this).parent().find(".glyphicon-plus").removeClass("glyphicon-plus").addClass("glyphicon-minus");
  			}).on('hidden.bs.collapse', function(){
  				$(this).parent().find(".glyphicon-minus").removeClass("glyphicon-minus").addClass("glyphicon-plus");
  			});

  			$(".navbar-collapse.collapse").on("show.bs.collapse", function() {
			  	$(".navbar a.navbar-brand img").css("height", "40px");
			}).on("hide.bs.collapse", function() {
			  	$(".navbar a.navbar-brand img").css("height", "");
			});
  		});

  		function showWaitingModal(message) {
  			$('#waiting-message').text(message);
  			$('#waiting-modal').modal('show');
  		}

		function hideWaitingModal() {
			$('#waiting-modal').modal('hide');
  		}
  	</script>

  	<script type="text/ng-template" id="error-messages" >
  		<div ng-message="required" class="error">Campo obligatorio</div>
  		<div ng-message="minlength" class="error">Campo demasiado corto</div>
		<div ng-message="maxlength" class="error">Campo demasiado largo</div>
		<div ng-message="number" class="error">Debe ser un n&uacute;mero</div>
	</script>


  	<style>
	    [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
	        display: none !important;
	    }
	</style>

  	<decorator:head/>
</head>
<body role="document">
      <nav class="navbar navbar-inverse">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand" href="<c:url value="/" />">
            	<img alt="UTPL - EVS" src="<c:url value="/web-resources/img/logo.png" />" />
            </a>
          </div>

          <sec:authorize access="isAuthenticated()">
          <div class="navbar-collapse collapse" ng-controller="auxCtrl">
            <ul class="nav navbar-nav">
            	<%-- <li><a href="<c:url value="/evaluationevent"/>" data-path="evaluationevent">Eventos de evaluación</a></li>
           		<li><a href="<c:url value="/evaluationcenter"/>" data-path="evaluationcenter">Centros de evaluaci&oacute;n</a></li> --%>
            </ul>
	        <ul class="nav navbar-nav navbar-right">
	        	<c:set var="logUsername">
					<sec:authentication property="principal.username" />
				</c:set>
	            <li><a href="#"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> ${logUsername}</a></li>
	            <li>
               		<c:url var="logoutUrl" value="/logout"/>
					<form action="${logoutUrl}" method="post" id="logout-form">
					  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					</form>
					<a href="#" id="logout-link">Salir <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a>
                </li>
	        </ul>
          </div><!--/.nav-collapse -->
          </sec:authorize>

        </div>
      </nav>

    <div class="container-fluid"  class="ng-cloak">
	 	<div class="jumbotron">
			<c:if test="${not empty successMessage}"><div class="alert alert-success ng-cloak"><a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>${successMessage}</div></c:if>
			<c:if test="${not empty infoMessage}"><div class="alert alert-info ng-cloak"><a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>${infoMessage}</div></c:if>
			<c:if test="${not empty warningMessage}"><div class="alert alert-warning ng-cloak" ><a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>${warningMessage}</div></c:if>
			<c:if test="${not empty dangerMessage}"><div class="alert alert-danger ng-cloak"><a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>${dangerMessage}</div></c:if>
		 	<flash-message duration="5000" show-close="true" on-dismiss="myCallback(flash)" ></flash-message>

			<decorator:body />
		</div>
    </div><!-- /.container -->

    <!-- MODAL WAIT -->
    <div class="modal fade bs-example-modal-sm" id="waiting-modal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
	    <div class="modal-dialog modal-sm">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h4 class="modal-title">
	                    <span class="glyphicon glyphicon-time"></span>
	                    <span id="waiting-message"></span>
	                 </h4>
	            </div>
	            <div class="modal-body">
	                <div class="progress">
	                    <div class="progress-bar progress-bar-striped active" style="width: 100%"></div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>


    <script src="<c:url value="/web-resources/js/angular.min.js" />"></script>
    <script src="<c:url value="/web-resources/js/angular-locale_es-es.min.js" />" charset="utf-8"></script>

	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.4.0/angular-messages.js"></script>

    <script src="<c:url value="/web-resources/js/init.js" />"></script>
    <script src="<c:url value="/web-resources/js/app.js" />"></script>

	<script src="<c:url value="/web-resources/bootstrap-custom/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/web-resources/js/alertify.js-0.3.11/lib/alertify.min.js" />"></script>
	<script src="<c:url value="/web-resources/js/angular-flash/angular-flash.js" />"></script>

 	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.0-beta.17/angular-sanitize.js"></script>

  	<!-- ui-calendar files -->
  	<link rel="stylesheet" href="<c:url value="/web-resources/js/ui-calendar/fullcalendar.min.css" />"/>
	<script type="text/javascript" src="<c:url value="/web-resources/js/ui-calendar/moment.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/web-resources/js/ui-calendar/calendar.js" />"></script>
	<script type="text/javascript" src="<c:url value="/web-resources/js/ui-calendar/fullcalendar.min.js" />" charset="utf-8"></script>
	<script type="text/javascript" src="<c:url value="/web-resources/js/ui-calendar/lang-all.js" />" charset="utf-8"></script>
	<script type="text/javascript" src="<c:url value="/web-resources/js/ui-calendar/gcal.js" />"></script>

 	<!-- ui-select files -->
 	<script src="<c:url value="/web-resources/js/ui-select/select.min.js"/>"></script>
 	<link rel="stylesheet" href="<c:url value="/web-resources/js/ui-select/select.min.css"/>">

 	<script src="<c:url value="/web-resources/js/angular-strap/angular-strap.min.js" />"></script>
 	<script src="<c:url value="/web-resources/js/angular-strap/angular-strap.tpl.min.js" />"></script>

 	<script src="<c:url value="/web-resources/js/ui-bootstrap-tpls-1.3.2.min.js" />"></script>

 	<script src="<c:url value="/web-resources/js/select2/js/select2.min.js" />"></script>

 	<script src="<c:url value="/web-resources/js/controller/aux-ctrl.js" />"></script>

 	<script type="text/javascript" src="<c:url value="/web-resources/js/ng-google-chart.min.js" />" charset="utf-8"></script>

	<script src="<c:url value="/web-resources/js/angular-file-upload.min.js" />"></script>
	

	<decorator:getProperty property="page.local_script"></decorator:getProperty>
</body>
</html>