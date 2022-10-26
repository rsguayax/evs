package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Schedule;
import es.grammata.evaluation.evs.data.model.repository.TimeBlock;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EvaluationEventEvaluationCenterController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private EvaluationCenterService evaluationCenterService;

	@Autowired
	private EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;

	@Autowired
	private EvaluationEventClassroomService evaluationEventClassroomService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private AvailableStateService availableStateService;

	@Autowired
	private CenterService centerService;
	
	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private EvaluationEventEvaluationCenterCenterService evaluationEventEvaluationCenterCenterService;



	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("edit", true);
		model.put("evaluationEvent", evaluationEvent);
		return "evaluation_event/evaluation-event-evaluation-center-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@Transactional
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventEvaluationCenter> listEvaluationCenters(@PathVariable Long id) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		//Hibernate.initialize(evaluationEvent.getEvaluationEventEvaluationCenters());
		List<EvaluationEventEvaluationCenter> evaluationCenters = evaluationEventEvaluationCenterService.findByEvaluationEvent(id);
		for(EvaluationEventEvaluationCenter eeec : evaluationCenters) {
			Hibernate.initialize(eeec.getEvaluationEventClassrooms());
		}

		return evaluationCenters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@Transactional
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/unselectedlist", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationCenter> unselectedListEvaluationCenters(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Hibernate.initialize(evaluationEvent.getEvaluationEventEvaluationCenters());
		Set<EvaluationCenter> selectedEvaluationCenters = evaluationEvent.getEvaluationCenters();
		List<EvaluationCenter> unselectedEvaluationCenters = evaluationCenterService.findAll();
		unselectedEvaluationCenters.removeAll(selectedEvaluationCenters);
		return unselectedEvaluationCenters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addEvaluationCenters(@PathVariable Long id, @RequestBody List<EvaluationCenter> evaluationCenters) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		for (EvaluationCenter evaluationcenter : evaluationCenters) {
			EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = new EvaluationEventEvaluationCenter(evaluationEvent, evaluationcenter);
			evaluationEventEvaluationCenterService.save(evaluationEventEvaluationCenter);

			evaluationcenter = evaluationCenterService.findById(evaluationcenter.getId());
			Set<Center> centers = evaluationcenter.getRegistrationCenters();
			for(Center center : centers) {
				EvaluationEventEvaluationCenterCenter eeecc = new EvaluationEventEvaluationCenterCenter(center, evaluationEventEvaluationCenter);
				evaluationEventEvaluationCenterCenterService.save(eeecc);
			}
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteEvaluationCenter(@PathVariable Long id, @RequestParam Long evaluationEventEvaluationCenterId) {
		Message message = new Message();
		
		// al ser la relacion manyToone no podemos borrar en cascada, antes que hacerla bidireccional lo hacemos programaticamente 
		List<Center> centers = evaluationEventEvaluationCenterCenterService.findCentersByEvaluationEventEvaluationCenterId(evaluationEventEvaluationCenterId);
		try {		
			evaluationEventEvaluationCenterCenterService.updateByEventEvaluationCenterId(id, evaluationEventEvaluationCenterId, new ArrayList());
			
			evaluationEventEvaluationCenterService.delete(evaluationEventEvaluationCenterId);
			message.setMessage("Centro de evaluación eliminado correctamente");
			message.setType(Message.TYPE_SUCCESS);
			message.setError(0);
		} catch (Exception ex) {
			message.setMessage("No se ha podido eliminar el centro de evaluación, compruebe que no existan alumnos asociados a horarios en ese aula.");
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
			evaluationEventEvaluationCenterCenterService.updateByEventEvaluationCenterId(id, evaluationEventEvaluationCenterId, centers);
		}
		
		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/schedule/list", method=RequestMethod.GET)
	public @ResponseBody List<Schedule> listSchedules(@PathVariable Long evaluationEventId) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		List<Schedule> schedules = scheduleService.findByEvaluationEvent(evaluationEventId);

		return schedules;
	}

	//	CLASSROOMS
	@Transactional
	@RequestMapping(value="/ajax/eventcenter/{evaluationEventId}/{eventCenterId}/classroom/new", method={RequestMethod.POST, RequestMethod.GET})
	public String createClassroom(@PathVariable Long evaluationEventId, @PathVariable Long eventCenterId, HttpServletRequest request, HttpServletResponse response, Map<String, Object> model, @Valid EvaluationEventClassroom evaluationEventClassroom, BindingResult bindingResult) {
		EvaluationEventEvaluationCenter eventCenter = evaluationEventEvaluationCenterService.findById(eventCenterId);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);

		if (request.getMethod().equals("POST")) {
			if(!bindingResult.hasErrors()) {
				
				if(evaluationEventClassroom.getNet() != null) {
					if(evaluationEventClassroom.getNet().getId() == null) {
						evaluationEventClassroom.setNet(null);
					}
				}
				
				evaluationEventClassroomService.save(evaluationEventClassroom);
				
				Schedule schedule = scheduleService.findById(evaluationEventClassroom.getSchedule().getId());

				evaluationEventClassroom = evaluationEventClassroomService.findById(evaluationEventClassroom.getId());
				AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);
				for (TimeBlock timeBlock : schedule.getTimeBlocks()) {
					ClassroomTimeBlock classroomTimeBlock = new ClassroomTimeBlock(evaluationEventClassroom, timeBlock, availableState);
					classroomTimeBlockService.save(classroomTimeBlock);
				}

				try {
					response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
					response.getOutputStream().print("{\"created\":\"true\"}");
					response.flushBuffer();
				} catch (Exception e) {
					e.printStackTrace();
				}

	            return null;
			}
		} else {
			evaluationEventClassroom = new EvaluationEventClassroom();
			evaluationEventClassroom.setEvaluationEventEvaluationCenter(eventCenter);
		}

		Hibernate.initialize(eventCenter.getEvaluationEventClassrooms());
		Set<Classroom> selectedClassrooms = eventCenter.getClassrooms();
		List<Classroom> unselectedClassrooms = eventCenter.getEvaluationCenter().getAvailableClassrooms();
		unselectedClassrooms.removeAll(selectedClassrooms);

		Set<Cap> selectedCaps = eventCenter.getCaps();
		Set<Cap> unselectedCaps = eventCenter.getEvaluationCenter().getCaps();
		unselectedCaps.removeAll(selectedCaps);
		
		Set<Net> unselectedNets = eventCenter.getEvaluationCenter().getNets();

		model.put("evaluationEventClassroom", evaluationEventClassroom);
		model.put("unselectedClassrooms", unselectedClassrooms);
		
		List<Schedule> schedules = scheduleService.findByEvaluationEvent(evaluationEventId);
		
		model.put("schedules", schedules);
		model.put("unselectedCaps", unselectedCaps);
		model.put("unselectedNets", unselectedNets);

		return "evaluation_event/evaluation-event-classroom-form";
	}

	@Transactional
	@RequestMapping(value="/ajax/eventcenter/{evaluationEventId}/{eventCenterId}/classroom/edit/{eventClassroomId}", method={RequestMethod.POST, RequestMethod.GET})
	public String editClassroom(@PathVariable Long evaluationEventId, @PathVariable Long eventCenterId, @PathVariable Long eventClassroomId, HttpServletRequest request, HttpServletResponse response, Map<String, Object> model, @Valid EvaluationEventClassroom evaluationEventClassroom, BindingResult bindingResult) {
		EvaluationEventEvaluationCenter eventCenter = evaluationEventEvaluationCenterService.findById(eventCenterId);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);	
		
		if (request.getMethod().equals("POST")) {
			if(!bindingResult.hasErrors()) {
				Schedule oldSchedule = evaluationEventClassroomService.findById(evaluationEventClassroom.getId()).getSchedule();
				
				if(evaluationEventClassroom.getNet() != null) {
					if(evaluationEventClassroom.getNet().getId() == null) {
						evaluationEventClassroom.setNet(null);
					}
				}
				
				evaluationEventClassroomService.update(evaluationEventClassroom);

				if (!evaluationEventClassroom.getSchedule().equals(oldSchedule)) {
					evaluationEventClassroom = evaluationEventClassroomService.findById(evaluationEventClassroom.getId());
					AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);

					// Delete the old ClassroomTimeBlocks
					for (ClassroomTimeBlock classroomTimeBlock : evaluationEventClassroom.getClassroomTimeBlocks()) {
						classroomTimeBlockService.delete(classroomTimeBlock.getId());
					}

					// Create the new ClassroomTimeBlocks
					for (TimeBlock timeBlock : evaluationEventClassroom.getSchedule().getTimeBlocks()) {
						ClassroomTimeBlock classroomTimeBlock = new ClassroomTimeBlock(evaluationEventClassroom, timeBlock, availableState);
						classroomTimeBlockService.save(classroomTimeBlock);
					}
				}

				try {
					response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
					response.getOutputStream().print("{\"updated\":\"true\"}");
					response.flushBuffer();
				} catch (Exception e) {
					e.printStackTrace();
				}

	            return null;
			}
		} else {
			evaluationEventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		}

		Hibernate.initialize(eventCenter.getEvaluationEventClassrooms());
		Set<Classroom> selectedClassrooms = eventCenter.getClassrooms();
		selectedClassrooms.remove(evaluationEventClassroom.getClassroom());
		Set<Classroom> unselectedClassrooms = eventCenter.getEvaluationCenter().getClassrooms();
		unselectedClassrooms.removeAll(selectedClassrooms);

		Set<Cap> selectedCaps = eventCenter.getCaps();
		selectedCaps.remove(evaluationEventClassroom.getCap());
		Set<Cap> unselectedCaps = eventCenter.getEvaluationCenter().getCaps();
		unselectedCaps.removeAll(selectedCaps);
		
		Set<Net> unselectedNets = eventCenter.getEvaluationCenter().getNets();

		model.put("evaluationEventClassroom", evaluationEventClassroom);
		model.put("unselectedClassrooms", unselectedClassrooms);
		
		List<Schedule> schedules = scheduleService.findByEvaluationEvent(evaluationEventId);
		model.put("schedules", schedules);
		model.put("unselectedCaps", unselectedCaps);
		model.put("unselectedNets", unselectedNets);

		return "evaluation_event/evaluation-event-classroom-form";
	}

	@RequestMapping(value="/eventcenter/{eventCenterId}/classroom/addall", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message createAllClassrooms(@PathVariable Long eventCenterId, @RequestBody List<EvaluationEventClassroom> eventClassrooms) {
		Message responseMessage = new Message();

		try {
			EvaluationEventEvaluationCenter eventCenter = evaluationEventEvaluationCenterService.findById(eventCenterId);

			for (EvaluationEventClassroom eventClassroom : eventClassrooms) {
				eventClassroom.setEvaluationEventEvaluationCenter(eventCenter);
				evaluationEventClassroomService.save(eventClassroom);

				eventClassroom = evaluationEventClassroomService.findById(eventClassroom.getId());
				AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);
				for (TimeBlock timeBlock : eventClassroom.getSchedule().getTimeBlocks()) {
					ClassroomTimeBlock classroomTimeBlock = new ClassroomTimeBlock(eventClassroom, timeBlock, availableState);
					classroomTimeBlockService.save(classroomTimeBlock);
				}
			}

			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Aulas configuradas correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al configurar las aulas: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@RequestMapping(value="/eventcenter/{eventCenterId}/classroom/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteClassroom(@PathVariable Long eventCenterId, @RequestParam Long evaluationEventClassroomId) {
		Message message = new Message();
		try {
			evaluationEventClassroomService.delete(evaluationEventClassroomId);
			message.setMessage("Evento eliminado correctamente");
			message.setType(Message.TYPE_SUCCESS);
			message.setError(0);
		} catch(Exception ex) {
			Throwable t = ex.getCause();
		    while ((t != null) && !(t instanceof ConstraintViolationException)) {
		        t = t.getCause();
		    }
		    
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
		    if (t instanceof ConstraintViolationException) {
			    message.setMessage("No se ha podido eliminar, compruebe que no existan ereturn new arraylistntidades asociadas.");
		    } else {
		    	message.setMessage("Error al eliminar evento de evaluación");
		    }
		}
		
		return message;
	}

	@Transactional
	@RequestMapping(value="/eventcenter/{eventCenterId}/unselectedclassrooms/list", method=RequestMethod.GET)
	public @ResponseBody List<Classroom> listUnselectedClassrooms(@PathVariable Long eventCenterId) {
		EvaluationEventEvaluationCenter eventCenter = evaluationEventEvaluationCenterService.findById(eventCenterId);

		Hibernate.initialize(eventCenter.getEvaluationEventClassrooms());
		Set<Classroom> selectedClassrooms = eventCenter.getClassrooms();
		List<Classroom> unselectedClassrooms = eventCenter.getEvaluationCenter().getAvailableClassrooms();
		unselectedClassrooms.removeAll(selectedClassrooms);

		return unselectedClassrooms;
	}

	@RequestMapping(value="/eventcenter/{evaluationEventId}/{eventCenterId}/unselectedeventclassrooms/list", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody List<EvaluationEventClassroom> listUnselectedEventClassrooms(@PathVariable Long evaluationEventId, @PathVariable Long eventCenterId) {
		EvaluationEventEvaluationCenter eventCenter = evaluationEventEvaluationCenterService.findById(eventCenterId);
		List<Schedule> schedules = new ArrayList<Schedule>(scheduleService.findByEvaluationEvent(evaluationEventId));
		Hibernate.initialize(eventCenter.getEvaluationEventClassrooms());
		
		Set<Classroom> selectedClassrooms = eventCenter.getClassrooms();
		List<Classroom> unselectedClassrooms = eventCenter.getEvaluationCenter().getAvailableClassrooms();
		unselectedClassrooms.removeAll(selectedClassrooms);

		List<EvaluationEventClassroom> unselectedEventClassrooms = new ArrayList<EvaluationEventClassroom>();
		for (Classroom classroom : unselectedClassrooms) {
			EvaluationEventClassroom eventClassroom = new EvaluationEventClassroom();
			eventClassroom.setEvaluationEventEvaluationCenter(eventCenter);
			eventClassroom.setClassroom(classroom);
			eventClassroom.setCap(classroom.getCap());
			eventClassroom.setSeats(classroom.getSeats());
			eventClassroom.setNet(classroom.getNet());

			if (schedules.size() > 0) {
				eventClassroom.setSchedule(schedules.get(0));
			}

			unselectedEventClassrooms.add(eventClassroom);
		}

		return unselectedEventClassrooms;
	}


	// CENTERS
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/{eventCenterId}/center/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Center> listCenters(@PathVariable Long id, @PathVariable Long eventCenterId) {
		List<Center> eventCenters = evaluationEventEvaluationCenterCenterService.findCentersByEvaluationEventEvaluationCenterId(eventCenterId);
		return eventCenters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/{eventCenterId}/center/unselectedlist", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Center> unselectedListCenters(@PathVariable Long id, @PathVariable Long eventCenterId) {
		List<Center> selectedCenters = evaluationEventEvaluationCenterCenterService.findCentersByEvaluationEventEvaluationCenterId(eventCenterId);
		List<Center> unselectedCenters = centerService.findActive();
		unselectedCenters.removeAll(selectedCenters);
		return unselectedCenters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationcenter/{eventCenterId}/center/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addCenters(@PathVariable Long id, @PathVariable Long eventCenterId, @RequestBody List<Center> centers) {
		EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = new EvaluationEventEvaluationCenter();
		evaluationEventEvaluationCenter.setId(eventCenterId);
		EvaluationEvent evaluationEvent = new EvaluationEvent();
		evaluationEvent.setId(id);
		/*for(Center center : centers) {
			EvaluationEventEvaluationCenterCenter eeecc = new EvaluationEventEvaluationCenterCenter(center, evaluationEventEvaluationCenter);
			evaluationEventEvaluationCenterCenterService.save(eeecc);
		}*/
		evaluationEventEvaluationCenterCenterService.updateByEventEvaluationCenterId(evaluationEvent.getId(), evaluationEventEvaluationCenter.getId(), centers);
	}
	
	// OTHERS
	@RequestMapping(value="/eventcenter/{eventCenterId}/classroomtimeblock/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<ClassroomTimeBlockInfo> listClassroomTimeBlocks(@PathVariable Long eventCenterId) {
		List<ClassroomTimeBlockInfo> classroomTimeBlocks = classroomTimeBlockService.findInfoByEventCenter(eventCenterId);

		return classroomTimeBlocks;
	}
}
