package es.grammata.evaluation.evs.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegreeSubject;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeSubjectService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventDegreeSubjectModel2;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Messages;

@Controller
public class EvaluationEventDegreeSubjectController extends BaseController {


	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private DegreeService degreeService;
	
	
	@Autowired
	private MatterService matterService;
	
	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private EvaluationEventDegreeService evaluationEventDegreeService;
	
	@Autowired
	private EvaluationEventDegreeSubjectService evaluationEventDegreeSubjectService;
	
	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	private Map<Long, Matter> subjects = null;

	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/allsubjects", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Matter> listAllSubjectss(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
//		List<Matter> subjects = this.matterService.findAll();
		List<Matter> subjects = matterService.getMattersByEvaluationEventType(evaluationEvent.getEvaluationEventTypes().getId());

		return subjects;
	}


	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/isubject/{idD}/ievaluationtypes/selected", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EvaluationEventDegreeSubject> listEvaluationEventDegres(@PathVariable Long id,
			@PathVariable Long idD, @RequestParam Map<String, String> allRequestParams) {
		EvaluationEventDegree eed = evaluationEventDegreeService.findById(idD);
		List<EvaluationEventDegreeSubject> eeds = evaluationEventDegreeSubjectService
				//.findByEvaluationEventDegreeSubjects(id, eed.getDegree().getId());
				
				.findByEvaluationEventDegree(id, eed.getDegree().getId());
		return eeds;
	}
	
	
	private Matter getSubjects(Long id) {
		if(subjects == null) {
			List<Matter> typesList = matterService.findAll();
			subjects = new HashMap<Long, Matter>();
			for(Matter et : typesList) {
				subjects.put(et.getId(), et);
			}
		}
		return subjects.get(id);
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/x/x", method = RequestMethod.GET)
	public @ResponseBody Message loadWSMatters(@PathVariable Long id, HttpServletRequest request) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Messages messages = new Messages();
		String subLoad = request.getParameter("subLoad");

		// Message message = evaluationClient.loadComponents(evaluationEvent.getCode(),
		// academicPeriodLoad, modeLoad, academicLevelLoad, true);
		Message message = new Message();
		message.setMessage("Modificación de tipos finalizada");
		message.setType(Message.TYPE_SUCCESS);
		messages.getMessages().add(message);
		return message;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degrees/{degreeId}/subjects/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Message addSubjects(@PathVariable Long id, @PathVariable Long degreeId, @RequestBody EvaluationEventDegreeSubjectModel2 subjectEvent, HttpServletRequest request) {
		Message responseMessage = new Message();
		
		try {
			EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
			
			// Si hay inscipciones en la titulación, no se puede eliminar
			EvaluationEventDegree eed = evaluationEventDegreeService.findById(degreeId);
			List<Enrollment> degreeEnrollments = enrollmentService.findbyEvaluationEventAndDegree(id, eed.getDegree().getId());

			if (degreeEnrollments.size() == 0) {
				// Eliminamos las temáticas de la titulación
				Degree degree = eed.getDegree();
				List<EvaluationEventDegreeSubject> evaluationEventDegreeSubjects = evaluationEventDegreeSubjectService.findByEvaluationEventAndDegree(evaluationEvent.getId(), degree.getId());
				for (EvaluationEventDegreeSubject evaluationEventDegreeSubject : evaluationEventDegreeSubjects) {
					evaluationEventDegreeSubjectService.delete(evaluationEventDegreeSubject.getId());
				}
				
				// Creamos las temáticas de la titulación
				for (int i = 0; i < subjectEvent.getModel().size(); i++) {
					Matter subject = subjectEvent.getModel().get(i).getSubject();
					EvaluationEventDegreeSubject eeds = new EvaluationEventDegreeSubject();
					eeds.setEvaluationEvent(evaluationEvent);
					eeds.setDegree(degree);
					eeds.setSubject(subject);
					eeds.setWeight(subjectEvent.getModel().get(i).getWeight());
					evaluationEventDegreeSubjectService.save(eeds);
					
					// Creamos asociación entre Evento de Evaluación y Asignatura
					EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findByEvaluationEventAndMatter(evaluationEvent.getId(), eeds.getSubject().getId());
					if (evaluationEventMatter == null) {
						Matter matter = matterService.findById(eeds.getSubject().getId());
						evaluationEventMatter = new EvaluationEventMatter();
						evaluationEventMatter.setEvaluationEvent(evaluationEvent);
						evaluationEventMatter.setMatter(matter);
						evaluationEventMatter.setBank(matter.getBanks().iterator().next());
						evaluationEventMatterService.save(evaluationEventMatter);
					}
				}
				
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Temáticas asignadas correctamente a la titulación");
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("No se pueden editar las temáticas de la titulación porque hay inscripciones activas en la titulación");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al añadir las temáticas a la titulación: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degrees/delete3/{idD}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String dropEvaluationEventDegreeSubjectRelation(@PathVariable Long id,
			@PathVariable Long idD) {
		String retorno = "Se ha borrado el registro";
		EvaluationEventDegreeSubject eeds = this.evaluationEventDegreeSubjectService.findById(idD);
		try {
			this.evaluationEventDegreeSubjectService.delete(idD);
		} catch (Exception e) {
			// TODO: handle exception
			 retorno = "No existe el registro";
		}
		
		return retorno;
	}
}
