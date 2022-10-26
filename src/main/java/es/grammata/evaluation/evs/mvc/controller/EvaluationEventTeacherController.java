package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventTeacher;
import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTeacherService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class EvaluationEventTeacherController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EvaluationEventTeacherService evaluationEventTeacherService;
	
	@RequestMapping(value="/evaluationevent/edit/{id}/teacher", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("edit", true);
		model.put("evaluationEvent", evaluationEvent);
		return "evaluation_event/evaluation-event-teacher-list";
	}
	
	@RequestMapping(value="/evaluationevent/edit/{id}/teacher/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<EvaluationEventTeacher> listTeachers(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<EvaluationEventTeacher> evaluationEventTeachers = evaluationEvent.getEvaluationEventTeachers();
		
		return evaluationEventTeachers;
	}
	
	@RequestMapping(value="/evaluationevent/edit/{id}/teacher/unselectedlist", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<User> unselectedListTeachers(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<User> selectedTeachers = evaluationEvent.getTeachers();
		List<User> unselectedTeachers = userService.findByRole(Role.TEACHER);
		unselectedTeachers.removeAll(selectedTeachers);
		return unselectedTeachers;
	}
	
	@RequestMapping(value="/evaluationevent/edit/{id}/teacher/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addTeachers(@PathVariable Long id, @RequestBody List<User> teachers) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		for (User teacher : teachers) {
			EvaluationEventTeacher evaluationEventTeacher = new EvaluationEventTeacher(evaluationEvent, teacher);
			evaluationEventTeacherService.save(evaluationEventTeacher);
		}
	}

	@RequestMapping(value="/evaluationevent/edit/{id}/teacher/delete/{evaluationEventTeacherId}", method=RequestMethod.GET)
	public @ResponseBody void deleteTeacher(@PathVariable Long id, @PathVariable Long evaluationEventTeacherId) {
		evaluationEventTeacherService.delete(evaluationEventTeacherId);
	}
}
