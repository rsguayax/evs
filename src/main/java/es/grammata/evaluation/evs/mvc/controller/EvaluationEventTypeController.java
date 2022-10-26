package es.grammata.evaluation.evs.mvc.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.siette.api.EvsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventType;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTypeService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EvaluationEventTypeController extends BaseController {
	
	@Autowired
	private EvaluationEventTypeService evaluationEventTypeService;
	
	@Autowired
	private EvsService evsService;
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationeventtype", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model) {
		model.put("headText", "Tipos de eventos de evaluación");

		return "evaluation_event_academic/evaluation-event-type-list";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationeventtype/evaluationeventtypes", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventType> loadEvaluationEventTypes() {

		List<EvaluationEventType> evaluationEventTypes = evaluationEventTypeService.findAll();

		return evaluationEventTypes;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationeventtype/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message addEvaluationEventType(@RequestBody EvaluationEventType evaluationEventType) {
		Message responseMessage = new Message();

		try {
			if (evaluationEventType.getName() != null && evaluationEventType.getName().trim().length() > 0) {
				if (evaluationEventType.getDescription() != null && evaluationEventType.getDescription().trim().length() > 0) {
					evaluationEventType.setActive(true);
					evaluationEventType.setCreated(new Date());
					evaluationEventType.setModified(new Date());
					evaluationEventTypeService.save(evaluationEventType);
					
					responseMessage.setType(Message.TYPE_SUCCESS);
					responseMessage.setMessage("Tipo de evento de evaluación creado correctamente");
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca una descripción");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Introduzca un nombre");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al guardar el tipo de evento de configuración: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationeventtype/edit", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editEvaluationEventType(@RequestBody EvaluationEventType evaluationEventType) {
		Message responseMessage = new Message();

		try {
			if (evaluationEventType.getName() != null && evaluationEventType.getName().trim().length() > 0) {
				if (evaluationEventType.getDescription() != null && evaluationEventType.getDescription().trim().length() > 0) {
					evaluationEventType.setModified(new Date());
					evaluationEventTypeService.update(evaluationEventType);
					
					evsService.changeEvaluationEventTypeName(evaluationEventType.getId(), evaluationEventType.getName());
					evsService.changeEvaluationEventTypeDescription(evaluationEventType.getId(), evaluationEventType.getDescription());
					
					responseMessage.setType(Message.TYPE_SUCCESS);
					responseMessage.setMessage("Tipo de evento de evaluación modificado correctamente");
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca una descripción");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Introduzca un nombre");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al guardar el tipo de evento de configuración: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationeventtype/{evaluationEventTypeId}/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteEvaluationEventType(@PathVariable Long evaluationEventTypeId) {
		Message responseMessage = new Message();

		try {
			EvaluationEventType evaluationEventType = evaluationEventTypeService.findById(evaluationEventTypeId);
			evaluationEventType.setActive(false);
			evaluationEventType.setModified(new Date());
			evaluationEventTypeService.update(evaluationEventType);
			
			evsService.disableEvaluationEventType(evaluationEventType.getId());
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Tipo de evento de evaluación deshabilitado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al deshabilitar el tipo de evento de configuración: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationeventtype/{evaluationEventTypeId}/enable", method=RequestMethod.GET)
	public @ResponseBody Message enableEvaluationEventType(@PathVariable Long evaluationEventTypeId) {
		Message responseMessage = new Message();

		try {
			EvaluationEventType evaluationEventType = evaluationEventTypeService.findById(evaluationEventTypeId);
			evaluationEventType.setActive(true);
			evaluationEventType.setModified(new Date());
			evaluationEventTypeService.update(evaluationEventType);
			
			evsService.enableEvaluationEventType(evaluationEventType.getId());
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Tipo de evento de evaluación habilitado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al habilitar el tipo de evento de configuración: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
}
