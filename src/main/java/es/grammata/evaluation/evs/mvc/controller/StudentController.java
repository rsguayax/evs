package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import es.grammata.evaluation.evs.data.model.repository.Role;
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
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.exception.ResourceNotFoundException;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventEvaluationCenterInfo;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventInfo;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules.TestScheduleModification;

@Controller
public class StudentController {
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired 
	private StudentEvaluationService studentEvaluationService;
	
	@Autowired
	private ScheduleModificationService scheduleModificationService;
	
	@Autowired
	private EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;
	
	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;
	
	@Autowired 
	private ScheduleModificationInfoService scheduleModificationInfoService;
	
	@Autowired
	private StudentTestsSchedules studentTestsSchedules;
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping("/students")
	public String studentsSearch(Map<String, Object> model) {
		model.put("headText", "Estudiantes");

		return "students/students-search";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/search", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody List<User> searchStudents(@RequestParam("searchText") String searchText) {
		List<User> students = userService.findByRoleAndSearchText(Role.STUDENT, searchText);
		
		return students;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/schedules")
	public String studentSchedules(Map<String, Object> model, @PathVariable Long userId) {
		User student = userService.findById(userId);
		model.put("headText", "Horarios del estudiante \"" +  student.getIdentification() + " - " + student.getFullName() + "\"");
		model.put("student", student);

		return "students/student-schedules";
	}
	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/eventsevaluations/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<GenericInfo> listEventsEvaluations(@PathVariable Long userId) {
		User user = userService.findById(userId);
		List<EvaluationAssignment> evaluationAssignments = evaluationAssignmentService.findByUsername(user.getUsername());
		List<GenericInfo> eventsEvaluations = new ArrayList<GenericInfo>();
		
		for (EvaluationAssignment evaluationAssignment : evaluationAssignments) {
			GenericInfo eventEvaluations = new GenericInfo();
			eventEvaluations.put("evaluationEventId", evaluationAssignment.getEvaluationEvent().getId());
			eventEvaluations.put("evaluationEventName", evaluationAssignment.getEvaluationEvent().getName());
			eventEvaluations.put("lastScheduleModification", scheduleModificationService.getLastInfoByEvaluationAssignment(evaluationAssignment.getId()));
			
			List<StudentEvaluation> studentEvaluations = studentEvaluationService.findByEvaluationAssignment(evaluationAssignment.getId());
			
			// Initialize MatterTests and ClassroomTimeBlock - StudentEvaluations
			for (StudentEvaluation studentEvaluation : studentEvaluations) {
				Hibernate.initialize(studentEvaluation.getMatterTests());
				Hibernate.initialize(studentEvaluation.getClassroomTimeBlock().getStudentEvaluations());
			}
			
			eventEvaluations.put("studentEvaluations", studentEvaluations);
			eventsEvaluations.add(eventEvaluations);
		}
		
		return eventsEvaluations;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/evaluationevent/{evaluationEventId}/editexamsschedules", method=RequestMethod.GET)
	public String editExamsSchedules(Map<String, Object> model, @PathVariable Long userId, @PathVariable Long evaluationEventId) {
		User user = userService.findById(userId);
		EvaluationEvent evaluationEvent = evaluationEventService.findByIdAndUsername(evaluationEventId, user.getUsername());
		
		if (evaluationEvent != null) {
			model.put("user", user);
			model.put("evaluationEvent", evaluationEvent);
			return "students/edit-exams-schedules";
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/evaluationevent/{evaluationEventId}/studentevaluation/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<StudentEvaluation> listStudentEvaluations(@PathVariable Long userId, @PathVariable Long evaluationEventId) {
		User user = userService.findById(userId);
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
		List<StudentEvaluation> studentEvaluations = new ArrayList<StudentEvaluation>();
		
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
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/evaluationevent/{evaluationEventId}/evaluationcenter/list", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody List<EvaluationEventEvaluationCenterInfo> listEventCenters(@PathVariable Long userId, @PathVariable Long evaluationEventId, @RequestParam(value="centerId", required=false) Long centerId) {
		User user = userService.findById(userId);
		EvaluationEventInfo evaluationEvent = evaluationEventService.findInfoByIdAndUsername(evaluationEventId, user.getUsername());
		List<EvaluationEventEvaluationCenterInfo> eventCenters =  new ArrayList<EvaluationEventEvaluationCenterInfo>();
		
		if (evaluationEvent != null) {
			if (centerId != null) {
				eventCenters.addAll(evaluationEventEvaluationCenterService.findInfoByEvaluationEventAndCenter(evaluationEvent.getId(), centerId));
			} else {
				eventCenters.addAll(evaluationEventEvaluationCenterService.findInfoByEvaluationEvent(evaluationEvent.getId()));
			}
		}

		return eventCenters;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/evaluationevent/{evaluationEventId}/lastschedulemodification", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody GenericInfo lastScheduleModification(@PathVariable Long userId, @PathVariable Long evaluationEventId) {
		User user = userService.findById(userId);
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
		GenericInfo lastScheduleModificationInfo = scheduleModificationService.getLastInfoByEvaluationAssignment(evaluationAssignment.getId());
		
		return lastScheduleModificationInfo;
	}
	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/students/{userId}/evaluationevent/{evaluationEventId}/editexamsschedules", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editExamsSchedules(@PathVariable Long userId, @PathVariable Long evaluationEventId, @RequestBody String json) {
		Message responseMessage = new Message();
		
		try {
			User user = userService.findById(userId);
			EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUsernameAndEvaluationEvent(user.getUsername(), evaluationEventId);
			
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

				if (!oldClassroomTimeBlock.equals(newClassroomTimeBlock)) {
					TestScheduleModification testScheduleModification = studentTestsSchedules.new TestScheduleModification(matterTestStudentInfo, oldClassroomTimeBlock, newClassroomTimeBlock);
					testsSchedulesModifications.add(testScheduleModification);
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Ha seleccionado el mismo horario");
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
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al modificar los horarios de los exámenes: <br /><br />" + e.getMessage());
			return responseMessage;
		}
	}
}
