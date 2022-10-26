package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.Schedule;
import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.model.repository.TimeBlock;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleService;
import es.grammata.evaluation.evs.data.services.repository.StudentTypeService;
import es.grammata.evaluation.evs.data.services.repository.TimeBlockService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.validator.ScheduleValidator;

@Controller
public class ScheduleController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private TimeBlockService timeBlockService;

	@Autowired
	private StudentTypeService studentTypeService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private AvailableStateService availableStateService;
	
	@Autowired
	private ScheduleValidator scheduleValidator;
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

	    binder.registerCustomEditor(Set.class, "studentTypes", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof StudentType) {
					return element;
				} else if (element instanceof String) {
					Long id = Long.parseLong((String) element);
					StudentType studentType = studentTypeService.findById(id);
					return studentType;
				}
				return null;
			}
		});
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{evaluationEventId}/schedule", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		model.put("edit", true);
		model.put("evaluationEvent", evaluationEvent);
		return "evaluation_event/evaluation-event-schedule-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{evaluationEventId}/schedule/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Schedule> listSchedules(@PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		List<Schedule> schedules = scheduleService.findByEvaluationEvent(evaluationEventId);

		return schedules;
	}

	@RequestMapping(value="/ajax/evaluationevent/{evaluationEventId}/schedule/new", method={RequestMethod.POST, RequestMethod.GET})
	public String newSchedule(HttpServletRequest request, Map<String, Object> model, HttpServletResponse response, @PathVariable Long evaluationEventId, @Valid Schedule schedule, BindingResult bindingResult) {
		if (request.getMethod().equals("POST")) {
			scheduleValidator.validate(schedule, bindingResult);
			
			if(!bindingResult.hasErrors()) {
				scheduleService.save(schedule);
				try {
					response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
					response.getOutputStream().print("{\"created\":\"true\", \"editUrl\":\"/evs/evaluationevent/" + evaluationEventId + "/schedule/edit/" + schedule.getId() +"\"}");
					response.flushBuffer();
				} catch (Exception e) {
					e.printStackTrace();
				}

	            return null;
			}
		} else {
			schedule = new Schedule();
			EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
			schedule.setEvaluationEvent(evaluationEvent);
		}

		model.put("schedule", schedule);

		return "evaluation_event/schedule-add-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/schedule/edit/{scheduleId}", method={RequestMethod.POST, RequestMethod.GET})
	public String editSchedule(HttpServletRequest request, Map<String, Object> model, @PathVariable Long evaluationEventId, @PathVariable Long scheduleId, @Valid Schedule schedule, BindingResult bindingResult) {
		if (request.getMethod().equals("POST")) {
			scheduleValidator.validate(schedule, bindingResult);
			
			if (!bindingResult.hasErrors()) {
				scheduleService.update(schedule);
			}
		} else {
			schedule = scheduleService.findById(scheduleId);
		}

		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		model.put("evaluationEvent", evaluationEvent);
		model.put("schedule", schedule);
		model.put("studentTypes", studentTypeService.findAll());

		return "evaluation_event/schedule-edit-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/schedule/delete/{scheduleId}", method=RequestMethod.GET)
	public @ResponseBody void deleteSchedule(@PathVariable Long evaluationEventId, @PathVariable Long scheduleId) {
		scheduleService.delete(scheduleId);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/eventclassrooms/size", method=RequestMethod.GET, produces="application/json")
	public String sizeEventClassrooms(HttpServletResponse response, @PathVariable Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);
		Set<EvaluationEventClassroom> eventClassrooms = schedule.getEvaluationEventClassrooms();

		try {
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("{\"size\":\"" + eventClassrooms.size() + "\"}").getBytes("UTF-8"));
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/timeblock/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<TimeBlock> listTimeBlocks(@PathVariable Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);
		Set<TimeBlock> timeBlocks = schedule.getTimeBlocks();

		return timeBlocks;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/timeblock/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody TimeBlock newTimeBlock(HttpServletRequest request, HttpServletResponse response, @PathVariable Long scheduleId, @Valid TimeBlock timeBlock, BindingResult bindingResult) {
		Schedule schedule = scheduleService.findById(scheduleId);

		if(!bindingResult.hasErrors()) {
			timeBlockService.save(timeBlock);
			timeBlock = timeBlockService.findById(timeBlock.getId());

			if (request.getParameter("updateAll") != null) {
				AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);
				for (EvaluationEventClassroom eventClassroom : schedule.getEvaluationEventClassrooms()) {
					ClassroomTimeBlock classroomTimeBlock = new ClassroomTimeBlock(eventClassroom, timeBlock, availableState);
					classroomTimeBlockService.save(classroomTimeBlock);
				}
			}

			return timeBlock;
		} else {
			try {
				String errors = "";
				for (FieldError error : bindingResult.getFieldErrors()) {
					errors += "<b>" + error.getField() + ":</b> " + error.getDefaultMessage() + "<br/>";
				}

				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
				response.getOutputStream().write(("{\"errors\":\"" + errors + "\"}").getBytes("UTF-8"));
				response.flushBuffer();
			} catch (Exception e) {
				e.printStackTrace();
			}

            return null;
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/timeblock/edit/{timeBlockId}", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody TimeBlock editTimeBlock(HttpServletRequest request, HttpServletResponse response, @PathVariable Long scheduleId, @PathVariable Long timeBlockId, @Valid TimeBlock timeBlock, BindingResult bindingResult) {
		if(!bindingResult.hasErrors()) {
			timeBlockService.update(timeBlock);

			if (request.getParameter("updateAll") != null) {
				timeBlock = timeBlockService.findById(timeBlockId);
				for (ClassroomTimeBlock classroomTimeBlock : timeBlock.getClassroomTimeBlocks()) {
					classroomTimeBlock.setStartDate(timeBlock.getStartDate());
					classroomTimeBlock.setEndDate(timeBlock.getEndDate());
					classroomTimeBlock.setStudentTypes(new HashSet<StudentType>(timeBlock.getStudentTypes()));
					classroomTimeBlockService.update(classroomTimeBlock);
				}
			}

			return timeBlock;
		} else {
			try {
				String errors = "";
				for (FieldError error : bindingResult.getFieldErrors()) {
					errors += "<b>" + error.getField() + ":</b> " + error.getDefaultMessage() + "<br/>";
				}

				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
				response.getOutputStream().write(("{\"errors\":\"" + errors + "\"}").getBytes("UTF-8"));
				response.flushBuffer();
			} catch (Exception e) {
				e.printStackTrace();
			}

            return null;
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/timeblock/delete/{timeBlockId}", method=RequestMethod.GET)
	public @ResponseBody void deleteTimeBlock(@PathVariable Long scheduleId, @PathVariable Long timeBlockId, @RequestParam Boolean deleteAll) {
		TimeBlock timeBlock = timeBlockService.findById(timeBlockId);

		for (ClassroomTimeBlock classroomTimeBlock : timeBlock.getClassroomTimeBlocks()) {
			if (deleteAll) {
				classroomTimeBlockService.delete(classroomTimeBlock.getId());
			} else {
				classroomTimeBlock.setTimeBlock(null);
				classroomTimeBlockService.update(classroomTimeBlock);
			}
		}

		timeBlockService.delete(timeBlockId);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/schedule/{scheduleId}/timeblock/update", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<String> updateTimeBlocks(@PathVariable Long scheduleId, @RequestBody String json, @RequestParam Boolean updateAll) {
		Schedule schedule = scheduleService.findById(scheduleId);
		JSONObject response = new JSONObject();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			JSONArray timeBlocks = new JSONArray(json);

			for(int i=0; i<timeBlocks.length(); i++) {
				JSONObject timeBlockJson = timeBlocks.getJSONObject(i);
				TimeBlock timeBlock = timeBlockService.findById(timeBlockJson.getLong("id"));
				if (timeBlock.getSchedule().equals(schedule)) {
					timeBlock.setStartDate(dateFormat.parse(timeBlockJson.getString("startDate")));
					timeBlock.setEndDate(dateFormat.parse(timeBlockJson.getString("endDate")));
					timeBlockService.update(timeBlock);

					if (updateAll) {
						for (ClassroomTimeBlock classroomTimeBlock : timeBlock.getClassroomTimeBlocks()) {
							classroomTimeBlock.setStartDate(timeBlock.getStartDate());
							classroomTimeBlock.setEndDate(timeBlock.getEndDate());
							classroomTimeBlockService.update(classroomTimeBlock);
						}
					}
				} else {
					throw new Exception("Los bloques horarios actualizados no pertenecen a horario");
				}
			}

			response.put("updated", true);
		} catch (Exception e) {
			response.put("error", "Se ha producido un error:<br/>" + e.getMessage());
		}

		HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(response.toString(), httpHeaders, HttpStatus.OK);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter', 'student')")
	@RequestMapping(value="/timeblock/{timeBlockId}/classroomtimeblocks/size", method=RequestMethod.GET, produces="application/json")
	public String sizeClassroomTimeBlocks(HttpServletResponse response, @PathVariable Long timeBlockId) {
		TimeBlock timeBlock = timeBlockService.findById(timeBlockId);
		Set<ClassroomTimeBlock> classroomTimeBlocks = timeBlock.getClassroomTimeBlocks();

		try {
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("{\"size\":\"" + classroomTimeBlocks.size() + "\"}").getBytes("UTF-8"));
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
