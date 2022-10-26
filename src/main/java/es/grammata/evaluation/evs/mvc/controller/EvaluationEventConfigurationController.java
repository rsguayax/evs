package es.grammata.evaluation.evs.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventType;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventConfigurationService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.scheduling.EvaluationEventConfigurationTrigger;
import es.grammata.evaluation.evs.scheduling.EvaluationSchedulesMailingTask;
import es.grammata.evaluation.evs.scheduling.SchedulableTask;
import es.grammata.evaluation.evs.scheduling.StudentsAndSchedulesLoadTask;
import es.grammata.evaluation.evs.scheduling.TasksRegistry;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;
import es.grammata.evaluation.evs.util.AssignedSchedulesMailer;

@Controller
public class EvaluationEventConfigurationController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private EvaluationEventConfigurationService evaluationEventConfigurationService;

	@Autowired
	TaskScheduler scheduler;

	@Autowired
	TasksRegistry tasksRegistry;

	@Autowired
	private StudentTestsSchedules studentTestsSchedules;

	@Autowired
	private CenterService centerService;

	@Autowired
	private EvaluationCenterService evaluationCenterService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private EvaluationClient evaluationClient;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private AssignedSchedulesMailer assignedSchedulesMailer;

	
	@Autowired
	private DegreeService degreeService;

	private Map<String, Degree> degreeCache;
	
	
	@Autowired
	private MatterService matterService;

	private Map<String, Matter> matterCache;
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{evaluationEventId}/config", method=RequestMethod.GET)
	public String editConfiguration(HttpServletRequest request, Map<String, Object> model, @PathVariable Long evaluationEventId, @RequestParam(required=false) Integer create) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);

		if(create != null && create == 1) {
			model.put("successMessage", "Evento de evaluación creado correctamente.");
			model.put("infoMessage", "Modifique los valores de configuración por defecto mostrados en este formulario.");
		}
		
		model.put("evaluationEvent", evaluationEvent);
		model.put("edit", true);
		model.put("degrees", loadDegrees());
		model.put("matters", loadMatters());

		return "evaluation_event/evaluation-event-configuration-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/config/get", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody EvaluationEventConfiguration getConfiguration(@PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		EvaluationEventConfiguration evaluationEventConfiguration =  evaluationEvent.getConfiguration();
		return evaluationEventConfiguration != null ? evaluationEventConfiguration : new EvaluationEventConfiguration(evaluationEvent);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/config/edit/{configurationId}", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editConfiguration(@PathVariable Long evaluationEventId, @PathVariable Long configurationId, @RequestBody EvaluationEventConfiguration configuration) {
		Message responseMessage = new Message();

		try {
			evaluationEventConfigurationService.update(configuration);

			Trigger t1 = new EvaluationEventConfigurationTrigger(configuration, EvaluationEventConfigurationTrigger.STUDENTS_AND_SCHEDULES_LOAD);
			StudentsAndSchedulesLoadTask studentsAndSchedulesLoadTask = new StudentsAndSchedulesLoadTask(
				evaluationEventId, studentTestsSchedules, centerService, evaluationCenterService,
				classroomTimeBlockService, evaluationEventMatterService, matterTestStudentService, evaluationClient,
				evaluationEventService, evaluationAssignmentService);
			SchedulableTask st1 = new SchedulableTask(studentsAndSchedulesLoadTask, scheduler, t1);
			tasksRegistry.register(evaluationEventId, st1);

			Trigger t2 = new EvaluationEventConfigurationTrigger(configuration, EvaluationEventConfigurationTrigger.EVALUATION_SCHEDULES_MAILING);
			EvaluationSchedulesMailingTask evaluationSchedulesMailingTask = new EvaluationSchedulesMailingTask(
				evaluationEventId, assignedSchedulesMailer, evaluationAssignmentService);
			SchedulableTask st2 = new SchedulableTask(evaluationSchedulesMailingTask, scheduler, t2);
			tasksRegistry.register(evaluationEventId, st2);

			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Configuraci\u00f3n guardada correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al guardar la configuraci\u00f3n: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/config/getregistry", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody String getTasksRegistry(@PathVariable Long evaluationEventId) {
	    return tasksRegistry.toJSONString();
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/config/checkstudents", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Integer checkStudents(@PathVariable Long evaluationEventId) {
		Long total = evaluationAssignmentService.totalEvaluationEventStudents(evaluationEventId);
		return (total==0L?0:1);
	}
	
	
	private List<Degree> loadDegrees() {
		List<Degree> degrees =this.degreeService.findAllAtive();
		this.degreeCache = new HashMap<String, Degree>();
		for (Degree degree : degrees) {
			this.degreeCache.put(degree.getIdAsString(), degree);
			
		}
		return degrees;
	}
	
	@InitBinder
	protected void initTypesBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "degrees", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Degree) {
					return element;
				}
				if (element instanceof String) {
					//EvaluationType type = EvaluationEventController.this.evaluationTypeCache.get(element);
					Degree degree = EvaluationEventConfigurationController.this.degreeCache.get(element); //EvaluationEventController.this.evaluationEventTypeCache.get(element);
					return degree;
				}
				return null;
			}
		});
	}
	
	
	/**
	 * Carga de lista de materias para el combobox de lalistade configuracion 
	 * @return lista de materias
	 */
	private List<Matter> loadMatters() {
		List<Matter> matters =this.matterService.findAll();
		this.matterCache = new HashMap<String, Matter>();
		for (Matter matter : matters) {
			this.matterCache.put(matter.getIdAsString(), matter);
			
		}
		return matters;
	}
	
	@InitBinder
	protected void initTypesBinder2(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "matters", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Matter) {
					return element;
				}
				if (element instanceof String) {
					//EvaluationType type = EvaluationEventController.this.evaluationTypeCache.get(element);
					Matter matter = EvaluationEventConfigurationController.this.matterCache.get(element); //EvaluationEventController.this.evaluationEventTypeCache.get(element);
					return matter;
				}
				return null;
			}
		});
	}


}
