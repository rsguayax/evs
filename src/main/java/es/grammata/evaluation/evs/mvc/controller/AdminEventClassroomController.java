package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventTeacher;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventConfigurationService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentSearchResult;

@Controller
public class AdminEventClassroomController extends BaseController {

	@Autowired
	private EvaluationEventClassroomService evaluationEventClassroomService;
	
	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private StudentEvaluationService studentEvaluationService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private AvailableStateService availableStateService;
	
	@Autowired
	private EvaluationEventConfigurationService evaluationEventConfigurationService;
	
	

	@RequestMapping("/eventclassroom/{eventClassroomId}/admin")
	public String admin(Map<String, Object> model, @PathVariable Long eventClassroomId) {
		EvaluationEventClassroom eventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		model.put("evaluationEvent", evaluationEventService.findById(eventClassroom.getEvaluationEvent().getId()));
		model.put("eventClassroom", eventClassroom);

		return "evaluation_event/admin/classroom";
	}

	@Transactional
	@RequestMapping(value="/eventclassroom/{eventClassroomId}/admin/timeblocks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<ClassroomTimeBlock> listTimeBlocks(@PathVariable Long eventClassroomId) {
		EvaluationEventClassroom eventClassroom = evaluationEventClassroomService.findById(eventClassroomId);
		Set<ClassroomTimeBlock> timeBlocks = eventClassroom.getClassroomTimeBlocks();

		// Initialize StudentEvaluations
		for (ClassroomTimeBlock timeBlock : timeBlocks) {
			Hibernate.initialize(timeBlock.getStudentEvaluations());
		}

		return timeBlocks;
	}

	@Transactional
	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/get", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody ClassroomTimeBlock getTimeBlock(@PathVariable Long classroomTimeBlockId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());

		return classroomTimeBlock;
	}

	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/teachers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<EvaluationEventTeacher> listTeachers(@PathVariable Long classroomTimeBlockId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		Set<EvaluationEventTeacher> teachers = classroomTimeBlock.getEvaluationEventTeachers();

		return teachers;
	}

	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/unselectedteachers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<EvaluationEventTeacher> listUnselectedTeachers(@PathVariable Long classroomTimeBlockId) {
		return new HashSet();
	}

	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/addteachers", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addTeachers(@PathVariable Long classroomTimeBlockId, @RequestBody List<EvaluationEventTeacher> eventTeachers) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		classroomTimeBlock.getEvaluationEventTeachers().addAll(eventTeachers);
		classroomTimeBlockService.update(classroomTimeBlock);
	}

	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/teacher/{eventTeacherId}/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteTeacher(@PathVariable Long classroomTimeBlockId, @PathVariable Long eventTeacherId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
		classroomTimeBlock.removeEvaluationEventTeacher(eventTeacherId);
		classroomTimeBlockService.update(classroomTimeBlock);
	}

	@Transactional
	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/students", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<StudentEvaluation> listStudents(@PathVariable Long classroomTimeBlockId) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);

		Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
		Set<StudentEvaluation> students = classroomTimeBlock.getStudentEvaluations();

		// Initialize MatterTests
		for (StudentEvaluation student : students) {
			Hibernate.initialize(student.getMatterTests());
		}

		return students;
	}

	@Transactional
	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/searchstudents", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody List<StudentSearchResult> searchStudents(@PathVariable Long classroomTimeBlockId, @RequestParam("searchFor") String searchFor) {
		ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);

		Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
		Set<StudentEvaluation> timeBlockStudents = classroomTimeBlock.getStudentEvaluations();

		List<Long> timeBlockEvaluationAssignmentIds = new ArrayList<Long>();
		for (StudentEvaluation timeBlockStudent : timeBlockStudents) {
			timeBlockEvaluationAssignmentIds.add(timeBlockStudent.getEvaluationAssignment().getId());
		}

		List<StudentSearchResult> studentsSearchResults = evaluationAssignmentService.findStudents(classroomTimeBlock.getEvaluationEventClassroom().getEvaluationEvent(), searchFor);
		for (StudentSearchResult studentSearchResult : studentsSearchResults) {
			studentSearchResult.setIncludedInTimeBlock(timeBlockEvaluationAssignmentIds.contains(studentSearchResult.getEvaluationAssignmentId()));
		}

		return studentsSearchResults;
	}

	@Transactional
	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/addstudent", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message addStudent(@PathVariable Long classroomTimeBlockId, @RequestBody String json) {
		Message responseMessage = new Message();

		try {
			ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
			Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
			
			Long evaluationEventId = classroomTimeBlock.getEvaluationEventClassroom().getEvaluationEvent().getId();
			
			EvaluationEventConfiguration evConfiguration = evaluationEventConfigurationService.findByEvaluationEvent(evaluationEventId);

			// si la asignacion es manual no se tienen en cuenta el numero de asientos
			if ((classroomTimeBlock.getAvailableState().getValue().equals(AvailableState.AVAILABLE) && 
					classroomTimeBlock.getAvailableSeats() > 0) || (evConfiguration.getAssignmentType().equals(EvaluationEventConfiguration.MANUAL_TYPE))) {
				JSONObject studentData = new JSONObject(json);

				StudentSearchResult student = new StudentSearchResult(studentData.getJSONObject("student"));
				EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findById(student.getEvaluationAssignmentId());

				StudentEvaluation studentEvaluation = new StudentEvaluation();
				studentEvaluation.setClassroomTimeBlock(classroomTimeBlock);
				studentEvaluation.setEvaluationAssignment(evaluationAssignment);
				studentEvaluationService.save(studentEvaluation);

				// Check if we need to update the availableState of the ClassroomTimeBlock
				classroomTimeBlock.getStudentEvaluations().add(studentEvaluation);
				if (classroomTimeBlock.getAvailableSeats() <= 0) {
					classroomTimeBlock.setAvailableState(availableStateService.findByState(AvailableState.FULL));
					classroomTimeBlockService.update(classroomTimeBlock);
				}

				JSONArray testsJson = studentData.getJSONArray("tests");
				List<MatterTestStudentInfo> tests = new ArrayList<MatterTestStudentInfo>();
				for(int i=0; i<testsJson.length(); i++) {
					tests.add(new MatterTestStudentInfo(testsJson.getJSONObject(i)));
				}
				matterTestStudentService.updateStudentEvaluation(studentEvaluation, tests);

				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Estudiante a\u00f1adido correctamente a la jornada de evaluaci\u00f3n");
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("No hay plazas libres en la jornada de evaluaci\u00f3n");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al a\u00f1adir el estudiante a la jornada de evaluaci\u00f3n: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@Transactional
	@RequestMapping(value="/classroomtimeblock/{classroomTimeBlockId}/student/{studentEvaluationId}/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteStudent(@PathVariable Long classroomTimeBlockId, @PathVariable Long studentEvaluationId) {
		Message responseMessage = new Message();

		try {
			StudentEvaluation studentEvaluation = studentEvaluationService.findById(studentEvaluationId);

			if (studentEvaluation.getClassroomTimeBlock().getId().equals(classroomTimeBlockId)) {
				Hibernate.initialize(studentEvaluation.getMatterTests());
				for (MatterTestStudent matterTestStudent : studentEvaluation.getMatterTests()) {
					matterTestStudentService.deleteStudentEvaluation(matterTestStudent.getId());
				}

				// Check if we need to update the availableState of the ClassroomTimeBlock
				ClassroomTimeBlock classroomTimeBlock = studentEvaluation.getClassroomTimeBlock();
				Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
				classroomTimeBlock.getStudentEvaluations().remove(studentEvaluation);
				if (classroomTimeBlock.getAvailableState().getValue().equals(AvailableState.FULL) && classroomTimeBlock.getAvailableSeats() > 0) {
					classroomTimeBlock.setAvailableState(availableStateService.findByState(AvailableState.AVAILABLE));
					classroomTimeBlockService.update(classroomTimeBlock);
				}

				studentEvaluationService.delete(studentEvaluation.getId());

				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Estudiante eliminado correctamente de la jornada de evaluaci\u00f3n");
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("El estudiante no est\u00e1 asignado a la jornada de evaluaci\u00f3n");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al eliminar el estudiante de la jornada de evaluaci\u00f3n: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@Transactional
	@RequestMapping(value="/studentevaluation/{studentEvaluationId}/tests", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<MatterTestStudentInfo> listTests(@PathVariable Long studentEvaluationId) {
		StudentEvaluation studentEvaluation = studentEvaluationService.findById(studentEvaluationId);
		Hibernate.initialize(studentEvaluation.getMatterTests());

		return studentEvaluation.getMatterTestInfos();
	}

	@RequestMapping(value="/studentevaluation/{studentEvaluationId}/unassignedtests", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<MatterTestStudentInfo> listUnassignedTests(@PathVariable Long studentEvaluationId) {
		StudentEvaluation studentEvaluation = studentEvaluationService.findById(studentEvaluationId);
		List<MatterTestStudentInfo> unassignedTests = matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationAssignment(studentEvaluation.getEvaluationAssignment().getId());

		return unassignedTests;
	}

	@RequestMapping(value="/studentevaluation/{studentEvaluationId}/addtests", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addTests(@PathVariable Long studentEvaluationId, @RequestBody List<MatterTestStudentInfo> tests) {
		StudentEvaluation studentEvaluation = studentEvaluationService.findById(studentEvaluationId);
		matterTestStudentService.updateStudentEvaluation(studentEvaluation, tests);
	}

	@Transactional
	@RequestMapping(value="/studentevaluation/{studentEvaluationId}/test/{matterTestStudentId}/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteTest(@PathVariable Long studentEvaluationId, @PathVariable Long matterTestStudentId) {
		Message responseMessage = new Message();
		responseMessage.setType(Message.TYPE_SUCCESS);

		try {
			matterTestStudentService.deleteStudentEvaluation(matterTestStudentId);
			StudentEvaluation studentEvaluation = studentEvaluationService.findById(studentEvaluationId);
			String message = "Test eliminado correctamente de la jornada de evaluaci\u00f3n";

			Hibernate.initialize(studentEvaluation.getMatterTests());
			if (studentEvaluation.getMatterTests().isEmpty()) {
				// Check if we need to update the availableState of the ClassroomTimeBlock
				ClassroomTimeBlock classroomTimeBlock = studentEvaluation.getClassroomTimeBlock();
				Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
				classroomTimeBlock.getStudentEvaluations().remove(studentEvaluation);
				if (classroomTimeBlock.getAvailableState().getValue().equals(AvailableState.FULL) && classroomTimeBlock.getAvailableSeats() > 0) {
					classroomTimeBlock.setAvailableState(availableStateService.findByState(AvailableState.AVAILABLE));
					classroomTimeBlockService.update(classroomTimeBlock);
				}

				studentEvaluationService.delete(studentEvaluation.getId());

				message += "<br /><br />Se ha eliminado el estudiante de la jornada de evaluaci\u00f3n porque se han eliminado todos sus tests asociados al bloque horario";
				responseMessage.setType(Message.TYPE_WARNING);
			}

			responseMessage.setMessage(message);
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al eliminar el test de la jornada de evaluaci\u00f3n: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@RequestMapping(value="/evaluationassignment/{evaluationAssignmentId}/tests", method=RequestMethod.GET)
	public @ResponseBody List<MatterTestStudentInfo> listStudentTests(@PathVariable Long evaluationAssignmentId) {
		List<MatterTestStudentInfo> studentTests = matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationAssignment(evaluationAssignmentId);

		return studentTests;
	}
}
