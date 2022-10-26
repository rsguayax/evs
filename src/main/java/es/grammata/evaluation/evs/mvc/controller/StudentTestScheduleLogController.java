package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.GenericScheduleLog;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.GenericScheduleLogService;
import es.grammata.evaluation.evs.data.services.repository.StudentTestScheduleLogService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

@Controller
public class StudentTestScheduleLogController extends BaseController {
	
	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired
	private StudentTestScheduleLogService studentTestScheduleLogService;
	
	@Autowired
	private GenericScheduleLogService genericScheduleLogService;
	
	@RequestMapping("/evaluationevent/{evaluationEventId}/admin/testsscheduleslog")
	public String admin(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		model.put("evaluationEvent", evaluationEvent);
		model.put("isAutomaticAvaluationEvent", (evaluationEvent.getConfiguration().getAssignmentType().equals(EvaluationEventConfiguration.AUTOMATIC_TYPE)?"1":"0"));
		
		return "evaluation_event/admin/tests-schedules-log";
	}
	
	@RequestMapping("/evaluationevent/{evaluationEventId}/studentswithlog/list")
	public @ResponseBody List<GenericInfo> listStudentsWithLog(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		List<GenericInfo> studentsWithLog = studentTestScheduleLogService.getStudentsInfoByEvaluationEvent(evaluationEventId);
		
		return studentsWithLog;
	}
	
	@RequestMapping("/evaluationassignment/{evaluationAssignmentId}/testsscheduleslog")
	public @ResponseBody List<GenericInfo> studentTestsLog(Map<String, Object> model, @PathVariable Long evaluationAssignmentId) {
		List<GenericInfo> testsLog = studentTestScheduleLogService.getTestsLogInfoByEvaluationAssignment(evaluationAssignmentId);
		
		return testsLog;
	}
	
	@RequestMapping("/evaluationevent/{evaluationEventId}/genericlogs")
	public @ResponseBody List<GenericScheduleLog> genericScheduleLog(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		List<GenericScheduleLog> testsLog = genericScheduleLogService.getLogGenericByEvaluationEvent(evaluationEventId);
		
		return testsLog;
	}
}
