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

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventTeacher;
import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTeacherService;
import es.grammata.evaluation.evs.data.services.repository.StudentTypeService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class ClassroomTimeBlockController extends BaseController {
	
	@Autowired
	private EvaluationEventClassroomService evaluationEventClassroomService;
	
	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;
	
	@Autowired 
	private StudentTypeService studentTypeService;
	
	@Autowired
	private EvaluationEventTeacherService evaluationEventTeacherService;
	
	@Autowired
	private AvailableStateService availableStateService;
	
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
	    
	    binder.registerCustomEditor(Set.class, "evaluationEventTeachers", new CustomCollectionEditor(Set.class) {
	    	protected Object convertElement(Object element) {
	    		if (element instanceof EvaluationEventTeacher) {
	    			return element;
	    		} else if (element instanceof String) {
	    			Long id = Long.parseLong((String) element);
	    			EvaluationEventTeacher evaluationEventTeacher = evaluationEventTeacherService.findById(id);
	    			return evaluationEventTeacher;
	    		}
	    		return null;
	    	}
	    });
	}
	
	
	@Transactional
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/schedule", method=RequestMethod.GET)
	public String schedule(HttpServletRequest request, Map<String, Object> model, @PathVariable Long eventClassroomId) {
		EvaluationEventClassroom eventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);
		Hibernate.initialize(eventClassroom.getEvaluationEvent());
		
		model.put("headText", "Horario del aula \"" + eventClassroom.getClassroom().getName() + "\"");
		model.put("eventClassroom", eventClassroom);
		model.put("availableState", availableState);
		model.put("studentTypes", studentTypeService.findAll());
		return "evaluation_event/classroom/schedule";
	}
	
	@Transactional
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<ClassroomTimeBlock> listClassroomTimeBlocks(@PathVariable Long eventClassroomId) {
		EvaluationEventClassroom eventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		Set<ClassroomTimeBlock> timeBlocks = eventClassroom.getClassroomTimeBlocks();
		
		// Initialize StudentEvaluations
		for (ClassroomTimeBlock timeBlock : timeBlocks) {
			Hibernate.initialize(timeBlock.getStudentEvaluations());
		}
		
		return timeBlocks;
	}
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody ClassroomTimeBlock newClassroomTimeBlock(HttpServletResponse response, @PathVariable Long eventClassroomId, @Valid ClassroomTimeBlock classroomTimeBlock, BindingResult bindingResult) {
		if(!bindingResult.hasErrors()) {
			classroomTimeBlockService.save(classroomTimeBlock);
			return classroomTimeBlock;
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
	
	@Transactional
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/edit/{classroomTimeBlockId}", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody ClassroomTimeBlock editClassroomTimeBlock(HttpServletResponse response, @PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId, @Valid ClassroomTimeBlock classroomTimeBlock, BindingResult bindingResult) {
		if(!bindingResult.hasErrors()) {
			classroomTimeBlockService.update(classroomTimeBlock);
			classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlock.getId());
			Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
			return classroomTimeBlock;
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
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/delete/{classroomTimeBlockId}", method=RequestMethod.GET)
	public @ResponseBody void deleteClassroomTimeBlock(@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) {
		classroomTimeBlockService.delete(classroomTimeBlockId);
	}
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/update", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<String> updateClassroomTimeBlocks(@PathVariable Long eventClassroomId, @RequestBody String json) {
		EvaluationEventClassroom eventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		JSONObject response = new JSONObject();
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			JSONArray classroomTimeBlocks = new JSONArray(json);
			
			for(int i=0; i<classroomTimeBlocks.length(); i++) {
				JSONObject classroomTimeBlockJson = classroomTimeBlocks.getJSONObject(i);
				ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockJson.getLong("id"));
				if (classroomTimeBlock.getEvaluationEventClassroom().equals(eventClassroom)) {
					classroomTimeBlock.setStartDate(dateFormat.parse(classroomTimeBlockJson.getString("startDate")));
					classroomTimeBlock.setEndDate(dateFormat.parse(classroomTimeBlockJson.getString("endDate")));
					classroomTimeBlockService.update(classroomTimeBlock);
				} else {
					throw new Exception("Los bloques horarios actualizados no pertenecen a este aula"); 
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
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/{classroomTimeBlockId}/teachers", method=RequestMethod.GET, produces="application/json")
	@ResponseBody Set<EvaluationEventTeacher> listClassroomTimeBlockTeachers(@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		Set<EvaluationEventTeacher> classroomTimeBlockTeachers = classroomTimeBlock.getEvaluationEventTeachers();
		return classroomTimeBlockTeachers;
	}
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/{classroomTimeBlockId}/unselectedteachers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<EvaluationEventTeacher> listUnselectedTeachers(@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) {
		return new HashSet();
	}
	
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/{classroomTimeBlockId}/addteachers", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addClassroomTimeBlockTeachers(@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId, @RequestBody List<EvaluationEventTeacher> eventTeachers) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		classroomTimeBlock.getEvaluationEventTeachers().addAll(eventTeachers);
		classroomTimeBlockService.update(classroomTimeBlock);
	}

	@RequestMapping(value="/eventclassroom/{eventClassroomId}/timeblock/{classroomTimeBlockId}/teacher/{eventTeacherId}/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteClassroomTimeBlockTeacher(@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId, @PathVariable Long eventTeacherId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		classroomTimeBlock.removeEvaluationEventTeacher(eventTeacherId);
		classroomTimeBlockService.update(classroomTimeBlock);
	}
}
