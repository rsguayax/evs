package es.grammata.evaluation.evs.mvc.controller.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.ScheduleModification;
import es.grammata.evaluation.evs.data.model.repository.ScheduleModificationInfo;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationInfoService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.exception.ResourceNotFoundException;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules.TestScheduleModification;

@Controller
public class StudentEditExamsSchedules {

	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired 
	private StudentEvaluationService studentEvaluationService;
	
	@Autowired 
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;
	
	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private ScheduleModificationService scheduleModificationService;
	
	@Autowired 
	private ScheduleModificationInfoService scheduleModificationInfoService;
	
	@Autowired
	private StudentTestsSchedules studentTestsSchedules;
							
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/editexamsschedules", method=RequestMethod.GET)
	public String editExamsSchedules(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EvaluationEvent evaluationEvent = evaluationEventService.findByIdAndUsername(evaluationEventId, user.getUsername());
		
		if (evaluationEvent != null) {
			model.put("evaluationEvent", evaluationEvent);
			return "student/edit-exams-schedules";
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@Transactional
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/editexamsschedules", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editExamsSchedules(@PathVariable Long evaluationEventId, @RequestBody String json) {
		Message responseMessage = new Message();
		System.out.println("llega al método...");
		System.out.println(json);
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
			
			if (evaluationAssignment.getEvaluationEvent().isScheduleModificationDate()) {
				JSONObject examsSchedulesData = new JSONObject(json);
				JSONArray modifiedMatterTests = examsSchedulesData.getJSONArray("modifiedMatterTests");
				ScheduleModification scheduleModification = new ScheduleModification(evaluationAssignment, examsSchedulesData.getString("schedulesModificationMessage"));
				List<TestScheduleModification> testsSchedulesModifications = new ArrayList<TestScheduleModification>();
				List<ScheduleModificationInfo> scheduleModificationInfos = new ArrayList<ScheduleModificationInfo>();
				
				studentTestsSchedules.setEvaluationAssignment(evaluationAssignment);
				
				for (int i=0; i<modifiedMatterTests.length(); i++) {
					JSONObject modifiedMatterTest = modifiedMatterTests.getJSONObject(i);
					MatterTestStudentInfo matterTestStudentInfo = new MatterTestStudentInfo(modifiedMatterTest);
					MatterTestStudent matterTestStudent = matterTestStudentService.findById(matterTestStudentInfo.getId());
					StudentEvaluation oldStudentEvaluation = studentEvaluationService.findById(modifiedMatterTest.getLong("studentEvaluationId"));
					ClassroomTimeBlock oldClassroomTimeBlock = oldStudentEvaluation.getClassroomTimeBlock();
					ClassroomTimeBlock newClassroomTimeBlock = classroomTimeBlockService.findById(modifiedMatterTest.getLong("timeBlockId"));
					
					if (oldStudentEvaluation.getEvaluationAssignment().equals(evaluationAssignment)) {
						if (!oldClassroomTimeBlock.equals(newClassroomTimeBlock)) {
							TestScheduleModification testScheduleModification = studentTestsSchedules.new TestScheduleModification(matterTestStudentInfo, oldClassroomTimeBlock, newClassroomTimeBlock);
							testsSchedulesModifications.add(testScheduleModification);
						} else {
							responseMessage.setType(Message.TYPE_ERROR);
							responseMessage.setMessage("Ha seleccionado el mismo horario");
							return responseMessage;
						}
					} else{
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("No tiene permisos para modificar los horarios de los exámenes");
						return responseMessage;
					}
					
					scheduleModificationInfos.add(new ScheduleModificationInfo(scheduleModification, matterTestStudent, oldClassroomTimeBlock, newClassroomTimeBlock));
				}
				
				boolean modified = studentTestsSchedules.modifyTestsSchedules(testsSchedulesModifications);
				
				if (modified) {
					studentTestsSchedules.saveTestsSchedules(null);
					
					// Save the schedule modification
					scheduleModificationService.save(scheduleModification);
					for (ScheduleModificationInfo scheduleModificationInfo : scheduleModificationInfos) {
						scheduleModificationInfoService.save(scheduleModificationInfo);
					}
					
					responseMessage.setType(Message.TYPE_SUCCESS);
					responseMessage.setMessage("Horarios de los exámenes modificados correctamente");
					return responseMessage;
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Se ha producido un error al modificar los horarios de los exámenes: <br /><br />" + studentTestsSchedules.getTestScheduleLog().get(0).getMessage());
					return responseMessage;
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha terminado el plazo para modificar los horarios de los exámenes");
				return responseMessage;
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al modificar los horarios de los exámenes: <br /><br />" + e.getMessage());
			return responseMessage;
		}
	}
}
