package es.grammata.evaluation.evs.mvc.controller.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationInfoService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.exception.ResourceNotFoundException;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventEvaluationCenterInfo;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventInfo;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules.TestScheduleModification;

@Controller
public class StudentEvaluationEventController {
	
	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired 
	private StudentEvaluationService studentEvaluationService;
	
	@Autowired 
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@Autowired
	private EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;
	
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
	
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}", method=RequestMethod.GET)
	public String evaluatioEvent(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EvaluationEvent evaluationEvent = evaluationEventService.findByIdAndUsername(evaluationEventId, user.getUsername());
		
		if (evaluationEvent != null) {
			model.put("evaluationEvent", evaluationEvent);
			return "student/evaluation-event";
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@Transactional
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/studentevaluation/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<StudentEvaluation> listStudentEvaluations(@PathVariable Long evaluationEventId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<StudentEvaluation> studentEvaluations = new ArrayList<StudentEvaluation>();
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
		
		if (evaluationAssignment != null) {
			studentEvaluations.addAll(studentEvaluationService.findByEvaluationAssignment(evaluationAssignment.getId()));
		}
		
		// Initialize MatterTests and ClassroomTimeBlock - StudentEvaluations
		for (StudentEvaluation studentEvaluation : studentEvaluations) {
			Hibernate.initialize(studentEvaluation.getMatterTests());
			Hibernate.initialize(studentEvaluation.getClassroomTimeBlock().getStudentEvaluations());
		}
		
		return studentEvaluations;
	}
	
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/evaluationcenter/list", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody List<EvaluationEventEvaluationCenterInfo> listEventCenters(@PathVariable Long evaluationEventId, @RequestParam(value="centerId", required=false) Long centerId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<EvaluationEventEvaluationCenterInfo> eventCenters =  new ArrayList<EvaluationEventEvaluationCenterInfo>();
		EvaluationEventInfo evaluationEvent = evaluationEventService.findInfoByIdAndUsername(evaluationEventId, user.getUsername());
		
		if (evaluationEvent != null) {
			if (centerId != null) {
				eventCenters.addAll(evaluationEventEvaluationCenterService.findInfoByEvaluationEventAndCenter(evaluationEvent.getId(), centerId));
			} else {
				eventCenters.addAll(evaluationEventEvaluationCenterService.findInfoByEvaluationEvent(evaluationEvent.getId()));
			}
		}

		return eventCenters;
	}
	
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/schedulemodificationattempts", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody GenericInfo scheduleModificationAttempts(@PathVariable Long evaluationEventId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
		GenericInfo scheduleModificationAttempts = new GenericInfo();
		
		if (evaluationAssignment != null) {
			List<ScheduleModification> schedulesModifications = scheduleModificationService.findByEvaluationAssignmentAndCreatedBy(evaluationAssignment.getId(), user.getUsername());
			scheduleModificationAttempts.put("attempts", schedulesModifications.size());
			scheduleModificationAttempts.put("remainingAttempts", 3 - schedulesModifications.size());
		} else {
			scheduleModificationAttempts.put("attempts", 0);
			scheduleModificationAttempts.put("remainingAttempts", 0);
		}
		
		return scheduleModificationAttempts;
	}
	
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/lastschedulemodification", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody GenericInfo lastScheduleModification(@PathVariable Long evaluationEventId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
		GenericInfo lastScheduleModificationInfo = scheduleModificationService.getLastInfoByEvaluationAssignmentAndCreatedBy(evaluationAssignment.getId(), user.getUsername());
		
		return lastScheduleModificationInfo;
	}
	
	@RequestMapping(value="/student/eventcenter/{eventCenterId}/classroomtimeblock/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<ClassroomTimeBlockInfo> listClassroomTimeBlocks(@PathVariable Long eventCenterId) {
		List<ClassroomTimeBlockInfo> classroomTimeBlocks = classroomTimeBlockService.findInfoByEventCenter(eventCenterId);

		return classroomTimeBlocks;
	}
	
	@Transactional
	@RequestMapping(value="/student/evaluationevent/{evaluationEventId}/editexamschedule", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editExamSchedule(@PathVariable Long evaluationEventId, @RequestBody String json) {
		Message responseMessage = new Message();
		
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
			JSONObject examScheduleData = new JSONObject(json);
			
			if (evaluationAssignment.getEvaluationEvent().isScheduleModificationDate()) {
				MatterTestStudentInfo matterTestStudentInfo = new MatterTestStudentInfo(examScheduleData.getJSONObject("matterTest"));
				StudentEvaluation oldStudentEvaluation = studentEvaluationService.findById(matterTestStudentInfo.getStudentEvaluationId());
				ClassroomTimeBlockInfo newClassroomTimeBlockInfo = new ClassroomTimeBlockInfo(examScheduleData.getJSONObject("classroomTimeBlock"));
				ClassroomTimeBlock oldClassroomTimeBlock = oldStudentEvaluation.getClassroomTimeBlock();
				ClassroomTimeBlock newClassroomTimeBlock = classroomTimeBlockService.findById(newClassroomTimeBlockInfo.getId());

				if (oldStudentEvaluation.getEvaluationAssignment().equals(evaluationAssignment)) {
					if (!oldClassroomTimeBlock.equals(newClassroomTimeBlock)) {
						studentTestsSchedules.setEvaluationAssignment(evaluationAssignment);
						List<TestScheduleModification> testsSchedulesModifications = new ArrayList<TestScheduleModification>();
						TestScheduleModification testScheduleModification = studentTestsSchedules.new TestScheduleModification(matterTestStudentInfo, oldClassroomTimeBlock, newClassroomTimeBlock);
						testsSchedulesModifications.add(testScheduleModification);
						
						boolean modified = studentTestsSchedules.modifyTestsSchedules(testsSchedulesModifications);
						
						if (modified) {
							studentTestsSchedules.saveTestsSchedules(null);
							
							// Save the schedule modification
							MatterTestStudent matterTestStudent = matterTestStudentService.findById(matterTestStudentInfo.getId());
							ScheduleModification scheduleModification = new ScheduleModification(evaluationAssignment, examScheduleData.getString("scheduleModificationMessage"));
							scheduleModificationService.save(scheduleModification);
							ScheduleModificationInfo scheduleModificationInfo = new ScheduleModificationInfo(scheduleModification, matterTestStudent, oldClassroomTimeBlock, newClassroomTimeBlock);
							scheduleModificationInfoService.save(scheduleModificationInfo);
							
							responseMessage.setType(Message.TYPE_SUCCESS);
							responseMessage.setMessage("Horario del examen modificado correctamente");
							return responseMessage;
						} else {
							responseMessage.setType(Message.TYPE_ERROR);
							responseMessage.setMessage("Se ha producido un error al modificar los horarios de los exámenes: <br /><br />" + studentTestsSchedules.getTestScheduleLog().get(0).getMessage());
							return responseMessage;
						}
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("Ha seleccionado el mismo horario");
					}
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("No tiene permisos para modificar el horario del examen");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha terminado el plazo para modificar el horario del examen");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al modificar el horario del examen: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}
}
