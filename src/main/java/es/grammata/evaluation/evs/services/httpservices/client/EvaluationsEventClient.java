package es.grammata.evaluation.evs.services.httpservices.client;  

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import siette.interfaces.EventosEvaluacionServicios;
  
@org.springframework.stereotype.Component
@Configurable
public class EvaluationsEventClient { 
	
	@Autowired
	public EventosEvaluacionServicios eventosEvaluacionServicios;
	
	
	public void createEvaluationEvent(String code, String name, Date initDate, Date endDate) {
		eventosEvaluacionServicios.crearEventoEvaluacion(code, name, initDate, endDate);
	}
	
	public void initEvaluationEvent(String code) {
		eventosEvaluacionServicios.inicioEventoEvaluacion(code);
	}
	
	public void endEvaluationEvent(String code) {
		eventosEvaluacionServicios.finEventoEvaluacion(code);
	}
	
	public void errorEvaluationEvent(String code, String message) {
		eventosEvaluacionServicios.errorEventoEvaluacion(code, message);
	}
	
}  