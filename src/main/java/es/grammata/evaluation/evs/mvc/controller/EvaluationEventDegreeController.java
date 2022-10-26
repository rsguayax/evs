package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
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
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeSubjectService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EvaluationEventDegreeController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private DegreeService degreeService;

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private EvaluationEventDegreeService evaluationEventDegreeService;

	@Autowired
	private EvaluationEventDegreeSubjectService evaluationEventDegreeSubjectService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degree", method = RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Asignaci\u00f3n de titulaciones");
		model.put("edit", true);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);

		model.put("degrees", degreeService.findAll());

		return "evaluation_event/evaluation-event-degree-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degrees", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EvaluationEventDegree> listDegrees(@PathVariable Long id,
			@RequestParam Map<String, String> allRequestParams) {
		List<EvaluationEventDegree> degrees = evaluationEventDegreeService.findByEvaluationEvent(id);
		return degrees;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/alldegrees", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Degree> listAllDegrees(@PathVariable Long id) {
		List<Degree> degrees = degreeService.findAllAtive();
		return degrees;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/alleedegrees", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EvaluationEventDegree> listAlleeDegrees(@PathVariable Long id) {
		List<EvaluationEventDegree> degrees = evaluationEventDegreeService.findAll();
		return degrees;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/degrees/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message addDegree(@PathVariable Long id, @RequestBody EvaluationEventDegree evaluationEventDegree) {
		Message responseMessage = new Message();

		try {
			if (evaluationEventDegree.getDegree() != null) {
				if (evaluationEventDegree.getQuota() != null && evaluationEventDegree.getQuota() > 0) {
					if (evaluationEventDegree.getCut_off_grade() != null && evaluationEventDegree.getCut_off_grade() > 0) {
						if (evaluationEventDegree.getCut_off_grade() <= 1) {
							EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
							evaluationEventDegree.setEvaluationEvent(evaluationEvent);
							evaluationEventDegreeService.save(evaluationEventDegree);
							
							responseMessage.setType(Message.TYPE_SUCCESS);
							responseMessage.setMessage("Titulación añadida correctamente al evento de evaluación");
						} else {
							responseMessage.setType(Message.TYPE_ERROR);
							responseMessage.setMessage("Introduzca una nota de corte válida (Valor entre 0 y 1)");
						}
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("Introduzca una nota de corte");
					}
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca el cupo de plazas ofertadas");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Selecione una titulación");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);

			if (e.getCause() instanceof ConstraintViolationException) {
				responseMessage.setMessage("Ya existe la titulación en el evento de evaluación");
			} else {
				responseMessage.setMessage("Se ha producido un error al añadir la titulación al evento de evaluación: <br /><br />" + e.getMessage());
			}
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/degrees/edit", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editDegree(@PathVariable Long id, @RequestBody EvaluationEventDegree evaluationEventDegree) {
		Message responseMessage = new Message();

		try {
			if (evaluationEventDegree.getQuota() != null && evaluationEventDegree.getQuota() > 0) {
				if (evaluationEventDegree.getCut_off_grade() != null && evaluationEventDegree.getCut_off_grade() > 0) {
					if (evaluationEventDegree.getCut_off_grade() <= 1) {
						EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
						evaluationEventDegree.setEvaluationEvent(evaluationEvent);
						evaluationEventDegreeService.update(evaluationEventDegree);
						
						responseMessage.setType(Message.TYPE_SUCCESS);
						responseMessage.setMessage("Titulación modificado correctamente");
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("Introduzca una nota de corte válida (Valor entre 0 y 1)");
					}
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca una nota de corte");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Introduzca el cupo de plazas ofertadas");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al editar la titulación del evento de evaluación: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degrees/delete/{idD}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Message dropEvaluationEventDegreeSubjectRelation(@PathVariable Long id,
			@PathVariable Long idD) {
		Message responseMessage = new Message();

		try {
			// Si hay inscipciones en la titulación, no se puede eliminar
			EvaluationEventDegree eed1 = evaluationEventDegreeService.findById(idD);
			List<Enrollment> degreeEnrollments = enrollmentService.findbyEvaluationEventAndDegree(id,
					eed1.getDegree().getId());

			if (degreeEnrollments.size() == 0) {
				List<EvaluationEventDegreeSubject> eeds = this.evaluationEventDegreeSubjectService
						.findByEvaluationEventDegree(id, eed1.getDegree().getId());

				for (int i = 0; i < eeds.size(); i++) {
					this.evaluationEventDegreeSubjectService.delete(eeds.get(i).getId());
				}

				this.evaluationEventDegreeService.delete(idD);
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Titulación eliminada correctamente del evento de evaluación");
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage(
						"No se puede eliminar la titulación del evento de evaluación porque hay inscripciones activas en la titulación");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage(
					"Se ha producido un error al eliminar la titulación del evento de evaluación: <br /><br />"
							+ e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/degrees/{degreeId}/degreeenrollmentscount", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody int getDegreeEnrollmentsCount(@PathVariable Long id, @PathVariable Long degreeId) {
		EvaluationEventDegree eed1 = evaluationEventDegreeService.findById(degreeId);
		List<Enrollment> degreeEnrollments = enrollmentService.findbyEvaluationEventAndDegree(id,
				eed1.getDegree().getId());
		return degreeEnrollments.size();
	}
}