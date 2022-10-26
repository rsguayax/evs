package es.grammata.evaluation.evs.mvc.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.custom.Qualification;
import es.grammata.evaluation.evs.data.model.custom.StatusEnrolled;
import es.grammata.evaluation.evs.data.model.repository.AdmissionGrade;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.AdmissionGradeService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentRevisionService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.DegreeEnrollmentRevision;
import es.grammata.evaluation.evs.mvc.controller.util.DegreeEnrollmentsRevision;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentRevisionInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.util.EnrollmentUtils;

@Controller
public class EnrollmentRevisionController extends BaseController {

	@Autowired
	private EnrollmentRevisionService enrollmentRevisionService;

	@Autowired
	private AdmissionGradeService admissionGradeService;

	@Autowired
	private EnrollmentService enrollmentService;
	
	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private RulesEnrollmentController rulesEnrollmentController;
	
	@Autowired
	private EvaluationEventDegreeService evaluationEventDegreeService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevision/{idER}/status/{idS}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String dropEvaluationEventDegreeSubjectRelation(@PathVariable Long id, @PathVariable Long idER,
			@PathVariable Long idS) {
		String retorno = "";
		EnrollmentRevision er = this.enrollmentRevisionService.findById(idER);
		er.setStatus(Integer.parseInt("" + idS));
		this.enrollmentRevisionService.update(er);
		return retorno;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevisionlist1", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EnrollmentRevisionInfo> listEnrollmentRevisions(@PathVariable Long id) {
		List<EnrollmentRevisionInfo> enrollmentRevisions = enrollmentRevisionService.findInfoByEvaluationEventOrderByUser(id);
		return enrollmentRevisions;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/loadGradeAll/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> loadGradeAllByEvaluationEvent(@PathVariable Long id, HttpServletRequest request) {
		// Datos recibicidos desde pagina o vista
						
		List<AdmissionGrade> listaTotal = admissionGradeService.findByEvaluationEventId(id);
		
		Map<String,AdmissionGrade> listMapClean = new HashMap<String,AdmissionGrade>();
		
		for (AdmissionGrade ad : listaTotal) {
			listMapClean.put(ad.getEvaluationEvent().getId()+"_"+ad.getUser().getId(), ad);
		}
		for(Entry<String, AdmissionGrade> p : listMapClean.entrySet()) {			
			List<Enrollment> listEnrolled = enrollmentService
					.findbyEvaluationEventAndUser(p.getValue().getEvaluationEvent().getId(), p.getValue().getUser().getId());			
			for (Enrollment e : listEnrolled) {
				List<Qualification> qualif = getQualificationByUserAndDegree(e.getDegree().getId(),
						e.getUser().getId());
				updateGradeEnrolled(e, getGradeFinalDegree(qualif));				
			}
		}
		
		List<Enrollment> forRulesExecute = enrollmentService.findByEvaluationEvent(id);
		Map<String,Enrollment> listMapCleanEnroll = new LinkedHashMap<String,Enrollment>();
		for (Enrollment ad : forRulesExecute) {			
			listMapCleanEnroll.put(ad.getEvaluationEvent().getId()+"_"+ad.getUser().getId(), ad);
		}
		
		for(Entry<String, Enrollment> p : listMapCleanEnroll.entrySet()) {			
			List<Enrollment> enrrolmentsPriority = enrollmentService.findbyEvaluationEventAndUser(p.getValue().getEvaluationEvent().getId(),p.getValue().getUser().getId());
			for(Enrollment e: enrrolmentsPriority) {
				try {
					rulesEnrollmentController.executeProcessRuleMultiple(e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block				
				}
			}			
		}
        return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/loadGradeAll", method = RequestMethod.GET)
	public @ResponseBody void loadGradeAll() {
		// Datos recibicidos desde pagina o vista
		List<AdmissionGrade> listaTotal = admissionGradeService.findAll();
		for (AdmissionGrade ad : listaTotal) {
			List<Enrollment> listEnrolled = enrollmentService
					.findbyEvaluationEventAndUser(ad.getEvaluationEvent().getId(), ad.getUser().getId());
			for (Enrollment e : listEnrolled) {
				List<Qualification> qualif = getQualificationByUserAndDegree(e.getDegree().getId(),
						e.getUser().getId());
				updateGradeEnrolled(e, getGradeFinalDegree(qualif));
			}
		}
	}

	/**
	 * Actualiza califacacion de inscripcion
	 * 
	 * @param enrollment -> inscripcion de estudiante
	 * @param grade      -> calificacion
	 */
	public void updateGradeEnrolled(Enrollment enrollment, double grade) {
		try {
			enrollment.setGrade(grade);
			enrollmentService.update(enrollment);
		} catch (Exception ex) {
			enrollment.setGrade(0.0);
			enrollmentService.update(enrollment);
		}
	}

	/**
	 * Obtiene las calificaciones y las almacena en objeto accesible
	 * 
	 * @param degreeId -> Identificador de titulacion
	 * @param userId   -> Identificador de usuario
	 * @return Lista de calificaciones segun titulacion de usuario
	 */
	public List<Qualification> getQualificationByUserAndDegree(Long degreeId, Long userId) {
		List<Object> dataQualifications = admissionGradeService.findAllQualificationByDegreeAndUser(degreeId, userId);
		List<Qualification> qualificationFinalSubject = new ArrayList<Qualification>();
		for (Object q : dataQualifications) {
			Object[] manager = (Object[]) q;
			Long subjectId = (Long) manager[0];
			double grade = Double.parseDouble((String) manager[1]);
			double weight = Double.parseDouble((String) manager[2]);
			double gradeFinal = Double.parseDouble((String) manager[3]);
			qualificationFinalSubject.add(new Qualification(subjectId, grade, weight, gradeFinal));
		}
		return qualificationFinalSubject;
	}

	/**
	 * Metodo para realizar la suma total de la calificacion
	 * 
	 * @param qualifications -> Lista de calificaciones de titulacion
	 * @return calificaciones calculada
	 */
	public double getGradeFinalDegree(List<Qualification> qualifications) {
		double total = 0;
		for (Qualification q : qualifications)
			total += q.getGradeTotal();
		return total;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevisionlist/print", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String printReport(@PathVariable Long id, @RequestParam Map<String, String> allRequestParams,
			HttpServletRequest request, HttpServletResponse response) {

		List<EnrollmentRevision> ers = enrollmentRevisionService.findByEvaluationEvent(id);

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "filename=EvaluationEventReports.xls");
			String estado = "";

			PrintWriter excelWrite = response.getWriter();

			excelWrite.println("Nombre" + "\t" + "Apellidos" + "\t" + "Identificacion" + "\t" + "Titulacion" + "\t"
					+ "# Revision" + "\t" + "Estado" + "\t" + "Nota");
			for (int i = 0; i < ers.size(); i++) {


				if (ers.get(i).getStatus() == 1) {
					estado = "APTO";
				}
				if (ers.get(i).getStatus() == 2) {
					estado = "NO CONSUMIDO";
				}
				if (ers.get(i).getStatus() == 3) {
					estado = "CURSO CERO";
				}
				if (ers.get(i).getStatus() == 4) {
					estado = "NO ADMITIDO";
				}
				if (ers.get(i).getStatus() == 5) {
					estado = "ADMITIDO MANUALMENTE";
				}
				if (ers.get(i).getStatus() == 6) {
					estado = "EN PROCESO";
				}
				if (ers.get(i).getStatus() == 7) {
					
					estado = "NO APLICALBE";
				}
				if (ers.get(i).getStatus() == 8) {
					estado = "DENEGADO";
				}
				if (ers.get(i).getStatus() == 9) {
					estado = "DESCONOCIDO";
				}
				
				double roundDbl = Math.round(ers.get(i).getEnrollment().getGrade()*100.0)/100.0;

				excelWrite.println(ers.get(i).getEnrollment().getUser().getFirstName() + "\t"
						+ ers.get(i).getEnrollment().getUser().getLastName() + "\t"
						+ ers.get(i).getEnrollment().getUser().getIdentification() + "\t"
						+ ers.get(i).getEnrollment().getDegree().getName() + "\t" + ers.get(i).getRevision() + "\t"
						+ estado + "\t" + roundDbl);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevisionlist/degree/{idD}/print2", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String printReportBydegree(@PathVariable Long id, @PathVariable Long idD,
			@RequestParam Map<String, String> allRequestParams, HttpServletRequest request,
			HttpServletResponse response) {
		List<EnrollmentRevision> ers = enrollmentRevisionService.findbyEvaluationEventAndDegree(id, idD);

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "filename=EvaluationEventReports.xls");
			String estado = "";

			PrintWriter excelWrite = response.getWriter();

			excelWrite.println("Nombre" + "\t" + "Apellidos" + "\t" + "Identificacion" + "\t" + "Titulacion" + "\t"
					+ "# Revision" + "\t" + "Estado" + "\t" + "Nota");
			for (int i = 0; i < ers.size(); i++) {

				if (ers.get(i).getStatus() == 1) {
					estado = "APTO";
				}
				if (ers.get(i).getStatus() == 2) {
					estado = "NO CONSUMIDO";
				}
				if (ers.get(i).getStatus() == 3) {
					estado = "CURSO CERO";
				}
				if (ers.get(i).getStatus() == 4) {
					estado = "NO ADMITIDO";
				}
				if (ers.get(i).getStatus() == 5) {
					estado = "ADMITIDO MANUALMENTE";
				}
				if (ers.get(i).getStatus() == 6) {
					estado = "EN PROCESO";
				}
				if (ers.get(i).getStatus() == 7) {
					estado = "NO APLICALBE";
				}
				if (ers.get(i).getStatus() == 8) {
					estado = "DENEGADO";
				}
				if (ers.get(i).getStatus() == 9) {
					estado = "DESCONOCIDO";
				}
				double roundDbl = Math.round(ers.get(i).getEnrollment().getGrade()*100.0)/100.0;

				excelWrite.println(ers.get(i).getEnrollment().getUser().getFirstName() + "\t"
						+ ers.get(i).getEnrollment().getUser().getLastName() + "\t"
						+ ers.get(i).getEnrollment().getUser().getIdentification() + "\t"
						+ ers.get(i).getEnrollment().getDegree().getName() + "\t" + ers.get(i).getRevision() + "\t"
						+ estado + "\t" + roundDbl);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/enrollmentsrevision/new", method = RequestMethod.GET)
	public @ResponseBody Message newEnrollmentsRevision(@PathVariable Long id) {
		Message responseMessage = new Message();

		try {
			EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
			
			// Si no se han calculado las notas de admisión, se calculan en base a las notas obtenidas de siette teniendo en cuenta los pesos de las temáticas en las titulaciones
			if (evaluationEvent.getState().equals(EvaluationEvent.STATE_QUALIFIED)) {
				List<Enrollment> enrollments = enrollmentService.findByEvaluationEvent(id);
				for (Enrollment enrollment : enrollments) {
					List<Qualification> qualif = getQualificationByUserAndDegree(enrollment.getDegree().getId(), enrollment.getUser().getId());
					Double finalGrade = getGradeFinalDegree(qualif);
					
					enrollment.setGrade(finalGrade);
					enrollmentService.update(enrollment);
				}
				
				evaluationEvent.setState(EvaluationEvent.STATE_ADMISSION_QUALIFIED);
				evaluationEventService.update(evaluationEvent);
			}
			
			generateNewEnrollmentsRevision(id);
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Revisión de las inscripciones realizada correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al generar una nueva revisión de las inscripciones: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/enrollmentsrevision/{enrollmentId}/updatestatus/{status}", method = RequestMethod.GET)
	public @ResponseBody Message updateStatusEnrollmentsRevision(@PathVariable Long id, @PathVariable Long enrollmentId, @PathVariable int status) {
		Message responseMessage = new Message();

		try {
			Enrollment enrollment = enrollmentService.findById(enrollmentId);
			EnrollmentRevision enrollmentRevision = enrollmentRevisionService.findByEnrollmentId(enrollment.getId());
			
			if (enrollmentRevision.getStatus() != status) {
				enrollmentRevision.setStatus(status);
				enrollmentRevision.setRevision(enrollmentRevision.getRevision() + 1);
				enrollmentRevisionService.update(enrollmentRevision);
				
				// Si se le asigna el estado ADMITIDO MANUALMENTE y el usuario tiene otra inscripcion, actualizamos el estado de la otra inscripción a NO APLICABLE
				// Si se le asigna el estado DENEGADO y el usuario tiene otra inscripcion en estado distinto a DENEGADO o ADMITIDO MANUALMENTE, actualizamos el estado de la otra inscripción a EN PROCESO
				List<Enrollment> userEnrollments = enrollmentService.findbyEvaluationEventAndUser(id, enrollment.getUser().getId());
				for (Enrollment userEnrollment : userEnrollments) {
					if (!enrollment.getId().equals(userEnrollment.getId())) {
						enrollmentRevision = enrollmentRevisionService.findByEnrollmentId(userEnrollment.getId());
						if (status == StatusEnrolled.MANUALLY_ADMITTED.status()) {
							enrollmentRevision.setStatus(StatusEnrolled.NOT_APPLICABLE.status());
							enrollmentRevision.setRevision(enrollmentRevision.getRevision() + 1);
							enrollmentRevisionService.update(enrollmentRevision);
						} else if (status == StatusEnrolled.DENIED.status() && enrollmentRevision.getStatus() != StatusEnrolled.DENIED.status() && enrollmentRevision.getStatus() != StatusEnrolled.MANUALLY_ADMITTED.status()) {
							enrollmentRevision.setStatus(StatusEnrolled.IN_PROCESS.status());
							enrollmentRevisionService.update(enrollmentRevision);
						}
					}
				}

				return newEnrollmentsRevision(id);
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("La inscripción ya tiene el estado " + EnrollmentUtils.getStatusString(status));
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al actualizar el estado de la inscripción: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/enrollmentsrevision/generaterandomgrades", method = RequestMethod.GET)
	public @ResponseBody Message enrollmentsRevisionGenerateRandomGrades(@PathVariable Long id) {
		Message responseMessage = new Message();

		try {
			Random random = new Random();
			List<Enrollment> enrollments = enrollmentService.findByEvaluationEvent(id);
			for (Enrollment enrollment : enrollments) {
				enrollment.setGrade(random.nextDouble());
				enrollmentService.update(enrollment);
			}
			
			EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
			evaluationEvent.setState(EvaluationEvent.STATE_ADMISSION_QUALIFIED);
			evaluationEventService.update(evaluationEvent);
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Notas aleatorias generadas correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al generar las notas aleatorias: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/enrollmentsrevision/delete", method = RequestMethod.GET)
	public @ResponseBody Message deleteEnrollmentsRevision(@PathVariable Long id) {
		Message responseMessage = new Message();

		try {
			List<EnrollmentRevision> enrollmentRevisions = enrollmentRevisionService.findByEvaluationEvent(id);
			for (EnrollmentRevision enrollmentRevision : enrollmentRevisions) {
				enrollmentRevisionService.delete(enrollmentRevision.getId());
			}
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Revisión de inscripciones eliminada correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al eliminar la revisión de inscripciones: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}

	private void generateNewEnrollmentsRevision(Long evaluationEventId) {
		// Inicializamos los objetos que que almacenarán las titulaciones con todas las inscripciones ordenadas por nota
		Map<Long, DegreeEnrollmentsRevision> degreeEnrollmentsRevisionMap = new HashMap<Long, DegreeEnrollmentsRevision>();
		List<EvaluationEventDegree> evaluationEventDegrees = evaluationEventDegreeService.findByEvaluationEvent(evaluationEventId);
		for (EvaluationEventDegree evaluationEventDegree : evaluationEventDegrees) {
			degreeEnrollmentsRevisionMap.put(evaluationEventDegree.getDegree().getId(), new DegreeEnrollmentsRevision(evaluationEventDegree));
		}
		
		// Recorremos el listado de Titulaciones - Inscripciones y vamos añadiendo las inscripciones ordenadas por nota
		for (DegreeEnrollmentsRevision degreeEnrollmentsRevision : degreeEnrollmentsRevisionMap.values()) {
			// Si ya existen revisiones de las inscripciones, las usamos para inicializar las inscripciones, en caso contrario usamos las inscripciones
			List<EnrollmentRevision> degreeEnrollmentRevisions = enrollmentRevisionService.findbyEvaluationEventAndDegreeOrderByGrade(evaluationEventId, degreeEnrollmentsRevision.getDegree().getId());
			if (degreeEnrollmentRevisions.size() > 0) {
				degreeEnrollmentsRevision.setEnrollmentRevisions(degreeEnrollmentRevisions);
			} else {
				List<Enrollment> degreeEnrollments = enrollmentService.findbyEvaluationEventAndDegreeOrderByGrade(evaluationEventId, degreeEnrollmentsRevision.getDegree().getId());
				degreeEnrollmentsRevision.setEnrollments(degreeEnrollments);
			}
		}
		
		// Actualizamos los estados de las inscripciones con 2 prioridades
		updateEnrollmentsWithTwoPriorities(evaluationEventId, degreeEnrollmentsRevisionMap);
		
		// Creamos o actualizamos las revisiones de las inscripciones
		for (DegreeEnrollmentsRevision degreeEnrollmentsRevision : degreeEnrollmentsRevisionMap.values()) {
			for (DegreeEnrollmentRevision degreeEnrollmentRevision : degreeEnrollmentsRevision.getEnrollments()) {
				EnrollmentRevision enrollmentRevision = enrollmentRevisionService.findByEnrollmentId(degreeEnrollmentRevision.getEnrollmentId());
				
				if (enrollmentRevision == null) {
					User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Enrollment enrollment = enrollmentService.findById(degreeEnrollmentRevision.getEnrollmentId());
					enrollmentRevision = new EnrollmentRevision();
					enrollmentRevision.setCreated(new Date());
					enrollmentRevision.setCreated_by(loggedUser.getId().intValue());
					enrollmentRevision.setEnrollment(enrollment);
					enrollmentRevision.setRevision(1);
					enrollmentRevision.setStatus(degreeEnrollmentRevision.getStatus());
					enrollmentRevisionService.save(enrollmentRevision);
				} else if (enrollmentRevision.getStatus() != degreeEnrollmentRevision.getStatus()) {
					enrollmentRevision.setStatus(degreeEnrollmentRevision.getStatus());
					enrollmentRevision.setRevision(enrollmentRevision.getRevision() + 1);
					enrollmentRevisionService.update(enrollmentRevision);
				}
			}
		}
	}
	
	private void updateEnrollmentsWithTwoPriorities(Long evaluationEventId, Map<Long, DegreeEnrollmentsRevision> degreeEnrollmentsRevisionMap) {
		// Recorremos todas las inscripciones de prioridad 2
		List<Enrollment> enrollmentsPriority2 = enrollmentService.findbyEvaluationEventWithPriority(evaluationEventId, 2);
		for (Enrollment enrollmentPriority2 : enrollmentsPriority2) {
			Enrollment enrollmentPriority1 = enrollmentService.findbyEvaluationEventAndUserWithPriority1(evaluationEventId, enrollmentPriority2.getUser().getId());
			DegreeEnrollmentRevision degreeEnrollmentRevisionPriority1 = degreeEnrollmentsRevisionMap.get(enrollmentPriority1.getDegree().getId()).getDegreeEnrollmentRevision(enrollmentPriority1);
			DegreeEnrollmentRevision degreeEnrollmentRevisionPriority2 = degreeEnrollmentsRevisionMap.get(enrollmentPriority2.getDegree().getId()).getDegreeEnrollmentRevision(enrollmentPriority2);
			
			// Actualizamos el estado de la inscripción de prioridad 2 en función del estado de la inscripción de prioridad 1
			DegreeEnrollmentsRevision degreeEnrollmentsRevision2 = degreeEnrollmentsRevisionMap.get(enrollmentPriority2.getDegree().getId());
			boolean updated = degreeEnrollmentsRevision2.updateEnrollmentPriority2Status(degreeEnrollmentRevisionPriority1, degreeEnrollmentRevisionPriority2);
			
			// Si se ha actualizado alguna inscripción, volvemos a revisar todas. Ya que la actualización del estado de una inscripción puede modificar el estado de otras inscripciones
			if (updated) {
				updateEnrollmentsWithTwoPriorities(evaluationEventId, degreeEnrollmentsRevisionMap);
				return;
			}
			
			// Actualizamos el estado de la inscripción de prioridad 1 en función del estado de la inscripción de prioridad 2
			DegreeEnrollmentsRevision degreeEnrollmentsRevision1 = degreeEnrollmentsRevisionMap.get(enrollmentPriority1.getDegree().getId());
			updated = degreeEnrollmentsRevision1.updateEnrollmentPriority1Status(degreeEnrollmentRevisionPriority1, degreeEnrollmentRevisionPriority2);
			
			// Si se ha actualizado alguna inscripción, volvemos a revisar todas. Ya que la actualización del estado de una inscripción puede modificar el estado de otras inscripciones
			if (updated) {
				updateEnrollmentsWithTwoPriorities(evaluationEventId, degreeEnrollmentsRevisionMap);
				return;
			}
		}
	}
}
