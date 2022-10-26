<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>
<html>
<head>
	<title>Asignatura</title>
</head>
<body>
	<div ng-controller="matterCtrl" ng-init="init(${matter.id});">
		<div class="page-header">
			<h3>${headText}
				<small>
					<a href="<c:url value="/matter" />" class="pull-right">
						<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> Volver
					</a>
				</small>
			</h3>
		</div>
		<div class="row" ng-cloak>
			<div class="col-md-12">
				<sf:form modelAttribute="matter">
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
						<sf:label path="academicLevel">Nivel acad&eacute;mico</sf:label>
						<sf:select path="academicLevel.id" id="academicLevel" class="form-control">
			            	<sf:options items="${academicLevels}" itemValue="id" itemLabel="name" />
			            </sf:select>
					</div>
					<div class="form-group">
						<sf:label path="daysAllowed">D&iacute;as permitidos</sf:label>
						<sf:select path="daysAllowed"  multiple="true" items="${daysAllowed}" itemLabel="name" itemValue="idAsString" class="form-control" id="days-allowed-list">
						</sf:select> <sf:errors path="daysAllowed" cssClass="error" />
					</div>
					<div class="panel-group" ng-if="matterId">
						<div class="panel panel-default">
							<div class="panel-heading"><span>Criterio de corrección de tests</span></div>
							<div class="panel-body">
								<sf:hidden path="correctionRule.id" readonly="true" />
			  			 		<div class="form-group">
									<sf:label path="correctionRule.minGrade">Grado m&iacute;nimo</sf:label>
									<sf:input class="form-control" type="number" id="correctionRule.minGrade" path="correctionRule.minGrade" step="0.01"/>
									<sf:errors path="correctionRule.minGrade" cssClass="error" />
						      	</div>
			  			 		<div class="form-group">
			  			 			<sf:label path="correctionRule.maxGrade">Grado m&aacute;ximo</sf:label>
									<sf:input class="form-control" type="number" id="correctionRule.maxGrade" path="correctionRule.maxGrade" step="0.01"/>
									<sf:errors path="correctionRule.maxGrade" cssClass="error" />
						      	</div>
						      	<div class="form-group">
			  			 			<sf:label path="correctionRule.type">Tipo</sf:label>
			  			 			<sf:select path="correctionRule.type" id="correctionRule.type" class="form-control">
			  			 				<sf:option value="">&nbsp;</sf:option>
			  			 				<sf:option value="CANONICAL_RULE">Correcci&oacute;n SIETTE</sf:option>
			  			 				<sf:option value="UTPL_RULE">Correcci&oacute;n UTPL</sf:option>
			  			 			</sf:select>
			  			 			<sf:errors path="correctionRule.type" cssClass="error" />
		  			 			</div>
							</div>
						</div>
					</div>
					<button type="submit" class="btn btn-default">Guardar</button>
				</sf:form>
			</div>
		</div>

		<div class="container" style="margin-top: 20px;">
			<div class="panel-group" id="bankAccordion">
				<div class="panel panel-default" ng-show="matterId">
			    	<div class="panel-heading">
			      		<h4 class="panel-title">
			        		<a class="accordion-toggle" data-toggle="collapse" data-parent="#bankAccordion" href="#collapseBank"  ng-click="getBanks()">
			          			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Bancos de preguntas
			        		</a>
			      		</h4>
			    	</div>
			    	<div id="collapseBank" class="panel-collapse collapse">
			      		<div class="panel-body">
			        		<div class="row">
								<div class="col-md-12" class="ng-cloak">
									<p align="center" ng-hide="banks.length">-- Sin resultados --</p>
									<table class="table table-striped" ng-show="banks.length">
										<tr style="background-color: #bbb;">
											<th>Id</th>
											<th>Nombre</th>
											<th>N. Preg.</th>
											<th>Estado</th>
											<th></th>
										</tr>
										<tr ng-repeat="bank in banks" class="ng-scope">
											<td width="5%">{{bank.id}}</td>
											<td width="60%">{{bank.name}}</td>
											<td width="10%">{{bank.questionNumber}}</td>
											<td width="15%">{{bank.state}}</td>
											<td width="10%" align="right">
												<a href='#' ng-click="deleteBank(bank)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
											</td>
										</tr>
									</table>

									<button type="button" class="btn btn-default" ng-click="showAddBank()">A&ntilde;adir</button>
								</div>
							</div>
			      		</div>
			    	</div>
			  </div>
			</div>
		</div>

		<!-- MODAL BANCOS -->
		<div class="modal" id="add-banks-modal" tabindex="-1" role="dialog" aria-labelledby="add-banks-modalLabel">
	  		<div class="modal-dialog" role="document"  style="width: 1024px;">
	    		<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h3 class="modal-title" id="add-banks-modalLabel">Asignaci&oacute;n de bancos de preguntas</h3>
	      			</div>
	      			<div class="modal-body">
	      				<div class="row">
							<div class="col-md-12">
					 			<ui-select multiple ng-model="selectedBanks.selected" theme="bootstrap" ng-disabled="disabled" sortable="true"
					  				close-on-select="false">
					    			<ui-select-match placeholder="Seleccione banco (limitado 50 primeros resultados)...">{{$item.name}}</ui-select-match>
					    				<ui-select-choices repeat="bank in allbanks  | propsFilter: {name: $select.search, state: $select.search}">
					      					<div ng-bind-html="bank.name | highlight: $select.search"></div>
					      						<small>
					        					Nombre: <span ng-bind-html="''+bank.name| highlight: $select.search"></span>
					        					Estado: <span ng-bind-html="''+bank.state| highlight: $select.search"></span>
					      						</small>
					    			</ui-select-choices>
					  			</ui-select>
							</div>
	        			</div>
	      			</div>
	      			<div class="modal-footer">
	        			<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	        			<button type="button" class="btn btn-default" ng-click="addBanks()">A&ntilde;adir</button>
	      			</div>
	    		</div>
	  		</div>
		</div>

	</div>

	<content tag="local_script">
		<script src="<c:url value="/web-resources/js/controller/matter-ctrl.js" />"></script>
	</content>
</body>
</html>