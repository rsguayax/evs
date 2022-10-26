package es.grammata.evaluation.evs.mvc.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationAssignmentInfo;

@Controller
public class StudentHomeController {
	
	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@RequestMapping("/student")
	public String home() {
		return "student/home";
	}
	
	@RequestMapping(value="/student/evaluationassignment/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationAssignmentInfo> listEvaluationAssignments() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<EvaluationAssignmentInfo> evaluationAssignments = evaluationAssignmentService.findInfoByUsername(user.getUsername());
		
		return evaluationAssignments;
	}
}
