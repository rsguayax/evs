package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentRevisionService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeSubjectService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EvaluationEventEnrollmentRevisionController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(EvaluationEventEnrollmentRevisionController.class);

	private Long aux;
	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private DegreeService degreeService;

	@Autowired
	private EvaluationEventDegreeService evaluationEventDegreeService;

	@Autowired
	private EvaluationEventDegreeSubjectService evaluationEventDegreeSubjectService;

	@Autowired
	private EnrollmentRevisionService enrollmentRevisionService;

	private Map<String, EvaluationEventDegree> evaluationEventDegreeCache;

	/***
	 * Metodo para llamar a la ventana de revision de inscripciones
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return Vista con la lista de revisiones del evento de evaluacion
	 * 
	 */
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevision", method = RequestMethod.GET)
	public String showListEnrollmentRevision(HttpServletRequest request, Map<String, Object> model,
			@PathVariable Long id) {
		aux = id;
		model.put("headText", "Revisi\u00f3n de inscripciones");
		model.put("edit", true);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);

		//model.put("enrollment-revisions", this.enrollmentRevisionService.findAll());

		return "evaluation_event/evaluation-event-enrollment-revision";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevision/edit/{idEE}", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String updateEnrollmentRevision(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id,
			@PathVariable Long idEE, @Valid EnrollmentRevision enrollmentRevision, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, @ModelAttribute("successMessage") String successMessage) {
		aux = id;
		if (request.getMethod().equals("POST")) {
			
		}

		return null;
	}

	/**
	 * Metodo que lista todas las revisiones de las inscipciones
	 * 
	 * @param id
	 * @param allRequestParams
	 * @return
	 */
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevisionlist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EnrollmentRevision> listDegrees(@PathVariable Long id,
			@RequestParam Map<String, String> allRequestParams) {

		aux = id;
		List<EnrollmentRevision> degrees = enrollmentRevisionService.findAll();

		return degrees;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollmentrevisions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Degree> listAllDegrees(@PathVariable Long id) {
		aux = id;
		List<Degree> degrees = degreeService.findAll();
		return degrees;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/allenrollmentrevisions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EvaluationEventDegree> listAlleeDegrees(@PathVariable Long id) {
		aux = id;
		List<EvaluationEventDegree> degrees = evaluationEventDegreeService.findAll();
		return degrees;
	}

	@InitBinder
	protected void initTypesBinder2(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "evaluationEventDegree", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof EvaluationEventDegree) {
					return element;
				}
				if (element instanceof String) {
					EvaluationEventDegree type = EvaluationEventEnrollmentRevisionController.this.evaluationEventDegreeCache
							.get(element);
					return type;
				}
				return null;
			}

		});
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/close1", method = RequestMethod.GET)
	public @ResponseBody Message closeEvent(@PathVariable Long id, HttpServletRequest request) {
		Message message = new Message();
		EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
		if (evaluationEvent.getState().equals("CLOSED")) {
			message.setMessage("El evento ya ha sido cerrado");
			message.setType(Message.TYPE_ERROR);
		} else {
			evaluationEvent.setState("CLOSED");
			this.evaluationEventService.update(evaluationEvent);
			message.setMessage("Se ha cerrado el evento");
			message.setType(Message.TYPE_SUCCESS);

		}
		return message;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/state1", method = RequestMethod.GET)
	public @ResponseBody Message stateEvent1(@PathVariable Long id, HttpServletRequest request) {
		Message message = new Message();
		EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
		if (evaluationEvent.getState().equals("CLOSED")) {
			message.setMessage("cerrado");
			message.setType(Message.TYPE_ERROR);
		} else {
			message.setMessage("cerrar");
			message.setType(Message.TYPE_SUCCESS);
		}

		return message;
	}

}
