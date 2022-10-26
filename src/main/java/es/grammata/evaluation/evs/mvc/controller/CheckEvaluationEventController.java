package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.StudentTestScheduleLogService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;

@Controller
public class CheckEvaluationEventController extends BaseController {
	
	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@Autowired
	private StudentTestScheduleLogService studentTestScheduleLogService;
	
	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;
	
	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;
	
	@RequestMapping("/evaluationevent/{evaluationEventId}/admin/checkevaluation")
	public String checkEvaluation(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		model.put("evaluationEvent", evaluationEvent);
		
		return "evaluation_event/admin/check-evaluation";
	}
	
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/check", method=RequestMethod.GET)
	public @ResponseBody GenericInfo checkEvaluation(@PathVariable Long evaluationEventId) {
		GenericInfo evaluationInfo = new GenericInfo();
		GenericInfo testsSchedulesInfo = matterTestStudentService.getTestsSchedulesInfo(evaluationEventId);
		List<GenericInfo> studentsMattersWithoutTest = new ArrayList<GenericInfo>();
		List<GenericInfo> studentsTestsWithoutSchedule = new ArrayList<GenericInfo>();
		
		for (EvaluationAssignmentMatter evaluationAssignmentMatter : evaluationAssignmentMatterService.findWithoutTest(evaluationEventId)) {
			GenericInfo studentMattersWithoutTest = null;
			
			for (GenericInfo studentMatters : studentsMattersWithoutTest) {
				if ((long) studentMatters.get("evaluationAssignmentId") == evaluationAssignmentMatter.getEvaluationAssignment().getId()) {
					studentMattersWithoutTest = studentMatters;
				}
			}
			
			if (studentMattersWithoutTest == null) {
				studentMattersWithoutTest = new GenericInfo();
				studentMattersWithoutTest.put("evaluationAssignmentId",  evaluationAssignmentMatter.getEvaluationAssignment().getId());
				studentMattersWithoutTest.put("studentIdentification",  evaluationAssignmentMatter.getEvaluationAssignment().getUser().getIdentification());
				studentMattersWithoutTest.put("studentFullName",  evaluationAssignmentMatter.getEvaluationAssignment().getUser().getFullName());
				studentMattersWithoutTest.put("studentFirstName",  evaluationAssignmentMatter.getEvaluationAssignment().getUser().getFirstName());
				studentMattersWithoutTest.put("studentLastName",  evaluationAssignmentMatter.getEvaluationAssignment().getUser().getLastName());
				studentMattersWithoutTest.put("mattersWithoutTest",  new ArrayList<GenericInfo>());
				studentsMattersWithoutTest.add(studentMattersWithoutTest);
			}
			
			String message = null;
			if (!evaluationAssignmentMatter.getEvaluationEventMatter().getHasBank()) {
				message = "La asignatura no tiene asignado un banco de preguntas";
			} else if (!evaluationEventMatterService.hasTests(evaluationAssignmentMatter.getEvaluationEventMatter().getId())) {
				message = "La asignatura no tiene asignado ningún test";
			} else {
				message = "El estudiante no tiene asignado el test";
			}
			
			GenericInfo matterWithoutTest = new GenericInfo();
			matterWithoutTest.put("id", evaluationAssignmentMatter.getEvaluationEventMatter().getMatter().getId());
			matterWithoutTest.put("name", evaluationAssignmentMatter.getEvaluationEventMatter().getMatter().getName());
			matterWithoutTest.put("code", evaluationAssignmentMatter.getEvaluationEventMatter().getMatter().getCode());
			matterWithoutTest.put("mode", evaluationAssignmentMatter.getEvaluationEventMatter().getMode().getName());
			matterWithoutTest.put("academicPeriod", evaluationAssignmentMatter.getEvaluationEventMatter().getAcademicPeriod().getName());
			matterWithoutTest.put("academicLevel", evaluationAssignmentMatter.getEvaluationEventMatter().getMatter().getAcademicLevel().getName());
			matterWithoutTest.put("center", evaluationAssignmentMatter.getCenter().getName());
			matterWithoutTest.put("message", message);
			
			((List<GenericInfo>) studentMattersWithoutTest.get("mattersWithoutTest")).add(matterWithoutTest);
		}
		
		for (EvaluationAssignment evaluationAssignment : evaluationAssignmentService.findWithTestsWithoutScheduleByEvaluationEvent(evaluationEventId)) {
			List<Long> matterTestStudentIdsWithoutSchedule = new ArrayList<Long>();
			for (MatterTestStudentInfo matterTestStudentInfo : matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationAssignment(evaluationAssignment.getId())) {
				matterTestStudentIdsWithoutSchedule.add(matterTestStudentInfo.getId());
			}
			
			GenericInfo studentTestsWithoutSchedule = new GenericInfo();
			studentTestsWithoutSchedule.put("evaluationAssignmentId",  evaluationAssignment.getId());
			studentTestsWithoutSchedule.put("studentIdentification",  evaluationAssignment.getUser().getIdentification());
			studentTestsWithoutSchedule.put("studentFullName",  evaluationAssignment.getUser().getFullName());
			studentTestsWithoutSchedule.put("studentFirstName",  evaluationAssignment.getUser().getFirstName());
			studentTestsWithoutSchedule.put("studentLastName",  evaluationAssignment.getUser().getLastName());
			studentTestsWithoutSchedule.put("testsWithoutSchedule",  studentTestScheduleLogService.getTestsLogInfoByEvaluationAssignmentAndMatterTestStudentIds(evaluationAssignment.getId(), matterTestStudentIdsWithoutSchedule));
			studentsTestsWithoutSchedule.add(studentTestsWithoutSchedule);
		}
		
		if ((long) testsSchedulesInfo.get("testsWithoutSchedule") > 0 || !studentsMattersWithoutTest.isEmpty()) {
			evaluationInfo.put("checkOk", false);
			evaluationInfo.put("testsSchedulesInfo", testsSchedulesInfo);
			evaluationInfo.put("studentsMattersWithoutTest", studentsMattersWithoutTest);
			evaluationInfo.put("studentsTestsWithoutSchedule", studentsTestsWithoutSchedule);
		} else {
			evaluationInfo.put("checkOk", true);
		}
		
		return evaluationInfo;
	}
}
