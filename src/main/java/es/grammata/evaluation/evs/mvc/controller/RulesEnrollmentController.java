package es.grammata.evaluation.evs.mvc.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.grammata.evaluation.evs.data.model.custom.JsonConfigRules;
import es.grammata.evaluation.evs.data.model.custom.Qualification;
import es.grammata.evaluation.evs.data.model.custom.StatusEnrolled;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.model.repository.GlobalConfig;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentRevisionService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.GlobalConfigService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class RulesEnrollmentController extends BaseController {	
	
	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private EvaluationEventDegreeService evaluationEventDegreeService;
	
	@Autowired
	private EnrollmentRevisionService enrollmentRevisionService;	
	
	@Autowired
	private GlobalConfigService globalConfigService;
	
	@Autowired
	private EnrollmentRevisionController enrollmentRevisionController;
	
	/**
	 * Llaves usadas en getTotalPlacesAndRevisionMin
	 */
	private String keyExistPlaceZeroC ="existPlaceZeroC";
	private String keyEnrolledMinGrade ="enrolledMinGrade";
	
		
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")	
	@RequestMapping(value = "/evaluationevent/rules/enrollment/{enrolledId}/status/{status}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> loadEnrolledByChange(HttpServletResponse response,@PathVariable Long enrolledId, @PathVariable int status) throws Exception{
		Map<String, String> map = new HashMap<String,String>();
		try {				
			//Buscamos todas las inscripciones activas
			//Enrollment enrollment = enrollmentService.findbyEvaluationEventAndUserAndDegree(evaluationEventId, userId,degreeId);
			Enrollment enrollment = enrollmentService.findById(enrolledId);
			
			if(getStatusSuitableByPriorityAfter(enrollment)) {
				throw new Exception(String.format("El estado ya fue asignado anteriormente ", null));
			};
			
			//Obtengo calificaciones finales			
			List<Qualification> qualif = enrollmentRevisionController.getQualificationByUserAndDegree(enrollment.getDegree().getId(),enrollment.getUser().getId());			
			enrollmentRevisionController.updateGradeEnrolled(enrollment, enrollmentRevisionController.getGradeFinalDegree(qualif));
			
			executeProcessRuleUnique(enrollment,status);			
			map.put("result", "Se ha ejecutado correctamente");			
			return new ResponseEntity<Object>(map, new HttpHeaders(), HttpStatus.OK);
		}catch(Exception e) {
			map.put("error", e.getMessage()==null?"Se ha producido un error desconocido":e.getMessage());
			return new ResponseEntity<Object>(map, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}		
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/rules", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Map<String, String> loadEnrolledAll(HttpServletResponse response) throws IOException {		
		try {
						
			List<Enrollment> enrollments = enrollmentService.findAllAtive();				
			for (Enrollment e : enrollments) {				
				executeProcessRuleMultiple(e);				
			}			
			Map<String, String> map = new HashMap<String,String>();
			map.put("response", "Se ha ejecutado correctamente");
			return map;
		}catch(Exception e) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
			return null;
		}
	}
	
	/**
	 * Metodo que ejecuta y carga inscripciones activas y asigna cupos segun reglas
	 * @param e
	 * @throws Exception 
	 */
	public void executeProcessRuleMultiple(Enrollment e) throws Exception {
			
		boolean ruleAprobed = false;
		String statusOccupied = StatusEnrolled.SUITABLE.value+","+StatusEnrolled.ZERO_COURSE.value+","+StatusEnrolled.MANUALLY_ADMITTED.value;
		if(enrolledRevisionStatusRestricted(e)) {
			saveEnrolledRevision(e);
		}else {
			List<JsonConfigRules> rules = dataConfig();
			//Buscamos las configuraciones dadas para el evento de evaluacion con su titulacion		
			EvaluationEventDegree evaluationEventDegree =  evaluationEventDegreeService.findByUnique(e.getEvaluationEvent().getId(), e.getDegree().getId());			
			if(evaluationEventDegree!=null && e.getGrade()!=null) {
				boolean placeAvailebles = getPlacesAvailableRule(evaluationEventDegree,statusOccupied);
				boolean note = getNoteRule(evaluationEventDegree,e.getGrade());
				boolean priority = getPriorityRule(e);			
				
				boolean isExistPlaceGeneral = getStatusSuitableByPriorityAfter(e);
				if(isExistPlaceGeneral) {
					placeAvailebles =true;
				}
				
				for (JsonConfigRules jRules : rules) {
					boolean placeAvailibleConfig = jRules.placeAvailiblesRule;
					boolean noteCuteConfig = jRules.noteRule;
					boolean prioridadConfig = jRules.priority==1?true:false;										
					
					if(placeAvailebles==placeAvailibleConfig && note==noteCuteConfig && prioridadConfig==priority) {
						ruleAprobed = true;
						if(!priority) {
							List<Enrollment> enrolls = enrollmentService.findbyEvaluationEventAndUser(e.getEvaluationEvent().getId(), e.getUser().getId());
							boolean isZeroCourse = getStatusSuitableByPriority(enrolls,jRules.priority,StatusEnrolled.ZERO_COURSE.value);
							if(isZeroCourse) {								
								if(note) {									
									if(placeAvailebles) {
										updateEnrolledRevisionPrioritySetStatus(e,1,StatusEnrolled.UNSUITABLE.value);
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.SUITABLE.toString()));
										break;
									}else {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.UNSUITABLE.toString()));
										break;
									}						
								}else {
									if(placeAvailebles) {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.NOT_CONSUMED.toString()));
										break;
									}else {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.UNSUITABLE.toString()));
										break;
									}									
								}							
							}
							
							boolean isSuitable = getStatusSuitableByPriority(enrolls,jRules.priority);							
							if(isSuitable) {								
								assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.NOT_CONSUMED.toString()));							
								break;
							}else {															
								if(jRules.asignedPlace) {								
									assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(jRules.status.toUpperCase()));								
								}else {
									assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(jRules.status.toUpperCase()));
								}
								break;
							}
						}else {													
							List<Enrollment> enrolls = enrollmentService.findbyEvaluationEventAndUser(e.getEvaluationEvent().getId(), e.getUser().getId());
							boolean isZeroCourse = getStatusSuitableByPriority(enrolls,jRules.priority,StatusEnrolled.ZERO_COURSE.value);
							if(isZeroCourse) {
								if(note) {
									if(placeAvailebles) {
										updateEnrolledRevisionPrioritySetStatus(e,2,StatusEnrolled.UNSUITABLE.value);
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.SUITABLE.toString()));										
										break;
									}else {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.UNSUITABLE.toString()));
										break;
									}						
								}else {
									if(placeAvailebles) {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.NOT_CONSUMED.toString()));
										break;
									}else {
										assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.UNSUITABLE.toString()));
										break;
									}									
								}							
							}
							
							boolean isSuitable = getStatusSuitableByPriority(enrolls,jRules.priority);
							if(isSuitable) {																
								assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.NOT_CONSUMED.toString()));
								break;
							}else {			
								if(jRules.asignedPlace) {
									assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(jRules.status.toUpperCase()));									
								}else {									
									assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(jRules.status.toUpperCase()));									
								}
								break;
							}		
						}					
					}else {
						//System.err.println("Place discponibles->" + placeAvailebles+ " nota-> "+note + " pioridad-" +priority);
					}
				}
				if(!ruleAprobed) {
					assignAvailablePlace(evaluationEventDegree,e,statusEnrolled(StatusEnrolled.UNKNOW.toString()));
				}
			}else {				
				System.err.println("No se encontro configuracion o calificacion final es nula");
			}
		}
	}
	
	/**
	 * Metodo que ejecuta y carga inscripciones activas (Aplicado para cambios manuales)
	 * @param e 
	 * @param status -> Estado recibido desde vista
	 * @throws Exception 
	 */
	public void executeProcessRuleUnique(Enrollment e,int status) throws Exception {		
		//Buscamos las configuraciones dadas para el evento de evaluacion con su titulacion		
		EvaluationEventDegree evaluationEventDegree =  evaluationEventDegreeService.findByUnique(e.getEvaluationEvent().getId(), e.getDegree().getId());
		String statusOccupiedGeneral = StatusEnrolled.SUITABLE.value+","+StatusEnrolled.MANUALLY_ADMITTED.value;
		String statusOccupiedZeroC = StatusEnrolled.ZERO_COURSE.value+"";
		String statusOccupiedTotal = StatusEnrolled.SUITABLE.value+","+StatusEnrolled.MANUALLY_ADMITTED.value+","+StatusEnrolled.ZERO_COURSE.value;
		if(evaluationEventDegree!=null && e.getGrade()!=null) {
			boolean placeAvailebles = getPlacesAvailableRule(evaluationEventDegree,statusOccupiedGeneral);
			boolean placeAvaileblesTotal = getPlacesAvailableRule(evaluationEventDegree,statusOccupiedTotal);
			boolean priority = getPriorityRule(e);			
						
			if(placeAvailebles) {
				if(!priority) {
					//boolean isPlaceExist = getStatusSuitableByPriorityAfter(e);							
					if(status!=StatusEnrolled.MANUALLY_ADMITTED.value) {							
						assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.DENIED.value);
						saveEnrolledRevisionPrioritySetStatus(e,1,StatusEnrolled.UNSUITABLE.value);						
					}else {
						if(placeAvaileblesTotal) {
							assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.MANUALLY_ADMITTED.value);
							saveEnrolledRevisionPrioritySetStatus(e,1,StatusEnrolled.NOT_APPLICABLE.value);							
						}else {
							//Cuantos cupos hay de curso cero, y si hay quitarle el cupo al de menor nota
							Map<String,Object> data = getTotalPlacesAndRevisionMin(evaluationEventDegree,statusOccupiedZeroC);
							boolean placeAvaileblesZeroC = (boolean)data.get(keyExistPlaceZeroC);
							if(placeAvaileblesZeroC) {							
								assignAvailablePlace(evaluationEventDegree,(Enrollment)data.get(keyEnrolledMinGrade),StatusEnrolled.UNSUITABLE.value);							
								assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.MANUALLY_ADMITTED.value);
								saveEnrolledRevisionPrioritySetStatus(e,1,StatusEnrolled.NOT_APPLICABLE.value);
							}else {
								throw new Exception("No hay lugares disponibles");
							}
						}						
					}
				}else {						
					//boolean isPlaceExist = getStatusSuitableByPriorityAfter(e);
					if(status!=StatusEnrolled.MANUALLY_ADMITTED.value) {						
						assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.DENIED.value);
						saveEnrolledRevisionPrioritySetStatus(e,2,StatusEnrolled.UNSUITABLE.value);						
					}else {
						if(placeAvaileblesTotal) {
							assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.MANUALLY_ADMITTED.value);
							saveEnrolledRevisionPrioritySetStatus(e,2,StatusEnrolled.NOT_APPLICABLE.value);							
						}else {
							Map<String,Object> data = getTotalPlacesAndRevisionMin(evaluationEventDegree,statusOccupiedZeroC);
							boolean placeAvaileblesZeroC = (boolean)data.get(keyExistPlaceZeroC);
							if(placeAvaileblesZeroC) {
								assignAvailablePlace(evaluationEventDegree,(Enrollment)data.get(keyEnrolledMinGrade),StatusEnrolled.UNSUITABLE.value);
								assignAvailablePlace(evaluationEventDegree,e,StatusEnrolled.MANUALLY_ADMITTED.value);
								saveEnrolledRevisionPrioritySetStatus(e,2,StatusEnrolled.NOT_APPLICABLE.value);
							}else {
								throw new Exception("No hay lugares disponibles");
							}							
						}					
					}
				}					
			}else {
				throw new Exception("No hay lugares disponibles");
			}		
		}else {			
			throw new Exception("No se encontro configuracion o calificacion final es nula");
		}
	}
	
	/**
	 * Metodo que obtiene datos cuando hay cursos ceros ocupando puestos y la inscripcion con menor nota
	 * @param evaluationEventDegree
	 * @return
	 */
	public Map<String,Object> getTotalPlacesAndRevisionMin(EvaluationEventDegree evaluationEventDegree,String statusOccupied) {
		boolean existPlacesZeroC = false;		
		List<EnrollmentRevision> result = enrollmentRevisionService.findbyEvaluationEventAndDegreeStatusAvailibles(
				evaluationEventDegree.getEvaluationEvent().getId(),
				evaluationEventDegree.getDegree().getId(),statusOccupied);
		int  totalInscripciones = result.size();			
		if(totalInscripciones>0) {
			existPlacesZeroC = true;
		}		
	
		List<Enrollment> resultEnrolled = new ArrayList<>();
		for(EnrollmentRevision e: result) {
			resultEnrolled.add(e.getEnrollment());
		}
		Enrollment enrMin = getEnrolledMinGrade(resultEnrolled);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put(keyExistPlaceZeroC, existPlacesZeroC);
		data.put(keyEnrolledMinGrade, enrMin);
		return data;
	}
	
	/**
	 * Metodo que busca la inscripcion con menor calificacion
	 * @param enrollments
	 * @return
	 */
	public Enrollment getEnrolledMinGrade(List<Enrollment> enrollments) {
        Enrollment enrMin = null;      
        double mayor, menor;
        if(enrollments==null || enrollments.isEmpty()) {
        	return enrMin;
        }
        mayor = menor = enrollments.get(0).getGrade();        
        
        for (Enrollment e: enrollments) {
        	enrMin = e;
            if(e.getGrade()<menor) {
                menor = e.getGrade();
                enrMin = e;
            }
        }
        return enrMin;        
	} 
	
	/**
	 * Metodo que se aplica para validar regla de lugares disponibles en titulacion
	 * @param evaluationEventDegree
	 * @return
	 */
	public boolean getPlacesAvailableRule(EvaluationEventDegree evaluationEventDegree,String statusOccupied) {		
		int  totalInscripciones = evaluationEventDegree.getQuota()-
					enrollmentRevisionService.findbyEvaluationEventAndDegreeStatusAvailibles(
							evaluationEventDegree.getEvaluationEvent().getId(),
							evaluationEventDegree.getDegree().getId(),statusOccupied).size();			
		if(totalInscripciones>0) {
			return true;
		}		
		return false;
	}
	
	/**
	 * Metodo que aplica para validar regla de nota minima
	 * @param evaluationEventDegree
	 * @param nota
	 * @return
	 */
	public boolean getNoteRule(EvaluationEventDegree evaluationEventDegree,double nota) {		
		if(evaluationEventDegree!=null && evaluationEventDegree.getCut_off_grade()<=nota) {
			return true;
		}
		return false;		
	}
	
	/**
	 * Metodo que aplica para validar regla de prioridad (prioridad_1 =true,prioridad_2 =false) 
	 * @param enrollment
	 * @return
	 */
	public boolean getPriorityRule(Enrollment enrollment) {
		if(enrollment!=null && enrollment.getPriority()==1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Metodo que ayuda a definar si en un prioridad distinta hay SUITABLE o NODISPON
	 * @param enrollments
	 * @param priority
	 * @return
	 */
	public boolean getStatusSuitableByPriority(List<Enrollment> enrollments,int priority) {
		for (Enrollment e : enrollments) {
			if(e.getPriority()!=priority) {
				EnrollmentRevision enrollRev = enrollmentRevisionService.findByEnrollmentId(e.getId());
				if(enrollRev==null)return false;		
				if(enrollRev.getStatus()==StatusEnrolled.SUITABLE.value
						|| enrollRev.getStatus()==StatusEnrolled.MANUALLY_ADMITTED.value
						|| enrollRev.getStatus()==StatusEnrolled.ZERO_COURSE.value) {
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	/**
	 * Metodo que ayuda a definar si en un prioridad distinta hay SUITABLE o NODISPON
	 * @param enrollments
	 * @param priority
	 * @return
	 */
	public boolean getStatusSuitableByPriority(List<Enrollment> enrollments,int priority,int status) {
		for (Enrollment e : enrollments) {
			if(e.getPriority()!=priority) {
				EnrollmentRevision enrollRev = enrollmentRevisionService.findByEnrollmentId(e.getId());
				if(enrollRev==null)return false;		
				if(enrollRev.getStatus()==status) {
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	/**
	 * Metodo que ayuda a buscar si anteriormente tenia un cupo asignado dentro de la misma prioridad
	 * @param enrollments
	 * @param priority
	 * @return si devuelve true-> tiene cupo asignado, si devuelve false-> nunca le asignaron cupo
	 */
	public boolean getStatusSuitableByPriorityAfter(Enrollment enrollments) {		
		EnrollmentRevision enrollRev = enrollmentRevisionService.findByEnrollmentId(enrollments.getId());		
		if(enrollRev==null)return false;		
		if(enrollRev.getStatus()==StatusEnrolled.SUITABLE.value
				|| enrollRev.getStatus()==StatusEnrolled.ZERO_COURSE.value
				|| enrollRev.getStatus()==StatusEnrolled.MANUALLY_ADMITTED.value) {
			return true;
		}
		return false;
	}
	
	/**
	 * Carga configuraciones de reglas
	 * @return
	 * @throws Exception 
	 */
	public List<JsonConfigRules> dataConfig() throws Exception{
		GlobalConfig gc = globalConfigService.findByNameConfig("rules_enrolled");
		if(gc==null) throw new Exception("No se ha encontrado configuraciones!!");
		String jsonConfig = gc.getValue();
		final Gson gson = new Gson();
		final Type typeListConfig = new TypeToken<List<JsonConfigRules>>(){}.getType();
	    final List<JsonConfigRules> listData = gson.fromJson(jsonConfig, typeListConfig);	    
		return listData;
	}
	
	/**
	 * Metodo para asignar cupos disponibles y agregar revision de inscripcion
	 * @param evaluationEventDegree
	 * @throws Exception
	 */	
	public void assignAvailablePlace(EvaluationEventDegree evaluationEventDegree,Enrollment enrollment,int status) throws Exception {
		/**if(evaluationEventDegree.getQuota()<=0) {
			throw new Exception("No hay cupos disponibles " + evaluationEventDegree.getQuota());
		}**/		
		
		EnrollmentRevision enrollFinal =  enrollmentRevisionService.findByEnrollmentId(enrollment.getId());		
		
		EnrollmentRevision newEnrolledRev = new EnrollmentRevision();
					
		newEnrolledRev.setRevision(1);
		
		if(enrollFinal!=null) {
			newEnrolledRev.setRevision(enrollFinal.getRevision()+1);
		}
		newEnrolledRev.setCreated(new Date());
		newEnrolledRev.setCreated_by(1);
		newEnrolledRev.setEnrollment(enrollment);
		newEnrolledRev.setStatus(status);
		enrollmentRevisionService.save(newEnrolledRev);
	}
	
	public void saveEnrolledRevision(Enrollment enrollment) {
		EnrollmentRevision enrollFinal =  enrollmentRevisionService.findByEnrollmentId(enrollment.getId());
		EnrollmentRevision newEnrolledRev = new EnrollmentRevision();
				
		newEnrolledRev.setRevision(enrollFinal.getRevision()+1);
		newEnrolledRev.setCreated(new Date());
		newEnrolledRev.setCreated_by(1);
		newEnrolledRev.setEnrollment(enrollment);
		newEnrolledRev.setStatus(enrollFinal.getStatus());
		enrollmentRevisionService.save(newEnrolledRev);		
	}
	
	/**
	 * Guardar revision de inscripcion segun la prioridad con nuevo estado
	 * @param enrollment
	 * @param prioridad
	 * @param status
	 */
	public void saveEnrolledRevisionPrioritySetStatus(Enrollment enrollment,int prioridad,int status) {
		
		EnrollmentRevision enrollFinal =  enrollmentRevisionService.findbyEvaluationEventAndUserAndPriority(
				enrollment.getEvaluationEvent().getId(), 
				enrollment.getUser().getId(),
				prioridad);		
		
		EnrollmentRevision newEnrolledRev = new EnrollmentRevision();		
		
		newEnrolledRev.setRevision(1);
		if(enrollFinal!=null) {
			newEnrolledRev.setRevision(enrollFinal.getRevision()+1);
		}		
		newEnrolledRev.setCreated(new Date());
		newEnrolledRev.setCreated_by(1);
		newEnrolledRev.setEnrollment(enrollFinal.getEnrollment());
		newEnrolledRev.setStatus(status);
		enrollmentRevisionService.save(newEnrolledRev);
	}
	
	/**
	 * Actualiza revision de inscripcion segun la prioridad con nuevo estado
	 * @param enrollment
	 * @param prioridad
	 * @param status
	 */
	public void updateEnrolledRevisionPrioritySetStatus(Enrollment enrollment,int prioridad,int status) {
		
		EnrollmentRevision enrollFinal =  enrollmentRevisionService.findbyEvaluationEventAndUserAndPriority(
				enrollment.getEvaluationEvent().getId(), 
				enrollment.getUser().getId(),
				prioridad);		
		enrollFinal.setStatus(status);
		
		enrollmentRevisionService.update(enrollFinal);
	}
	
	/**
	 * Metodo para obtener valor del status configurado
	 * @param status
	 * @return
	 */
	public int statusEnrolled(String status) {
		for (StatusEnrolled day : StatusEnrolled.values()) {			
		    if((day.toString()).equalsIgnoreCase(status)) {		    	
		    	return day.value; 
		    }
		}
		return 0;
	}
	
	public boolean enrolledRevisionStatusRestricted(Enrollment e) {
		List<Integer> statusRestricted = Arrays.asList(StatusEnrolled.DENIED.value,StatusEnrolled.IN_PROCESS.value,StatusEnrolled.NOT_APPLICABLE.value);
		EnrollmentRevision enrollFinal =  enrollmentRevisionService.findByEnrollmentId(e.getId());
		if(enrollFinal==null)return false;
		if(statusRestricted.contains(enrollFinal.getStatus())){
			return true;
		}
		return false;
	}

}
