package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ModeService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Progress;
import es.grammata.evaluation.evs.mvc.controller.util.ResultData;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;

@Controller
public class EvaluationEventStudentController extends BaseController {

	private static final String ROLE_STUDENT = "STUDENT";

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private UserService userService;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private EvaluationClient evaluationClient;

	@Autowired
	private ModeService modeService;
	
	@Autowired
	private TestService testService;

	@Autowired
	private AcademicPeriodService academicPeriodService;

	@Autowired
	private AcademicLevelService academicLevelService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Asignaci\u00f3n de usuarios");
		model.put("edit", true);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);

		model.put("academicPeriods", academicPeriodService.findActive());
		model.put("academicLevels", academicLevelService.findNotDeleted());
		model.put("modes", modeService.findNotDeleted());

		return "evaluation_event/student-evaluation-event-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody ResultData listStudents(@PathVariable Long id, @RequestParam int page, @RequestParam int pageSize,
			@RequestParam String q, @RequestParam Map<String, String> allRequestParams) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		//Set<User> students = evaluationEvent.getStudents();
		EvaluationEvent evaluationEvent = new EvaluationEvent();
		evaluationEvent.setId(id);

		ResultData result = new ResultData<User>();
		List<User> students = evaluationAssignmentService.findUsersByEvaluationEvent(evaluationEvent, page - 1, pageSize, q);
		result.setData(students);
		result.setTotal(evaluationAssignmentService.totalEvaluationEventStudents(id, q));

		return result;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteStudent(@PathVariable Long id, @RequestParam Long idStudent) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		//User student = userService.findById(idStudent);
		//evaluationEvent.getStudents().remove(student);
		//evaluationEventService.update(evaluationEvent);

		Message message = new Message();
		message.setMessage("El estudiante ha sido eliminado correctamente");
		message.setType(Message.TYPE_SUCCESS);
		message.setError(0);

		EvaluationEvent evaluationEvent = new EvaluationEvent();
		User student = new User();
		evaluationEvent.setId(id);
		student.setId(idStudent);

		EvaluationAssignment evaluationAssignmnet = new EvaluationAssignment();
		evaluationAssignmnet.setEvaluationEvent(evaluationEvent);
		evaluationAssignmnet.setUser(student);
		evaluationAssignmnet = evaluationAssignmentService.findByUnique(evaluationEvent, student);
		try {
			evaluationAssignmentService.delete(evaluationAssignmnet.getId());
		} catch (Exception ex) {
			message.setType(Message.TYPE_ERROR);
			message.setMessage("No se ha podido eliminar, compruebe que el usuario no tenga asignaturas asociadas.");
			message.setError(1);
		}


		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/allstudents", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<User> listAllStudents(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		List<User> students = userService.findByRole(ROLE_STUDENT);
		students.removeAll(evaluationAssignmentService.findUsersByEvaluationEvent(evaluationEvent));

		return students;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody void addStudents(@PathVariable Long id, @RequestBody List<User> students) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		//evaluationEvent.getStudents().addAll(students);
		//evaluationEventService.update(evaluationEvent);

		for(User user : students) {
			EvaluationAssignment evaluationAssignmnet = new EvaluationAssignment();
			evaluationAssignmnet.setEvaluationEvent(evaluationEvent);
			evaluationAssignmnet.setUser(user);
			evaluationAssignmentService.save(evaluationAssignmnet);
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/{studentId}/matters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventMatter> listMatters(@PathVariable Long id,  @PathVariable Long studentId,
			@RequestParam Map<String, String> allRequestParams) {

		List<EvaluationEventMatter> studentMatters = evaluationAssignmentMatterService.findEvaluationEventMattersByStudentId(id, studentId);

		return studentMatters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/{studentId}/matters/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteMatter(@PathVariable Long id, @PathVariable Long studentId, @RequestParam Long matterId) {
		EvaluationAssignmentMatter eam = evaluationAssignmentMatterService.find(id, studentId, matterId);
		if(eam != null) {
			deleteStudentMatterTests(eam);
			evaluationAssignmentMatterService.delete(eam.getId());
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/{studentId}/allmatters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventMatter> listAllMatters(@PathVariable Long id, @PathVariable Long studentId) {
		List<EvaluationEventMatter> matters = evaluationEventMatterService.findByEvaluationEventWithBank(id);
		List<EvaluationEventMatter> studentMatters = evaluationAssignmentMatterService.findEvaluationEventMattersByStudentId(id, studentId);
		matters.removeAll(studentMatters);

		return matters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/existsmatterwithbank", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody int existsMatterWithBank(@PathVariable Long id) {
		Long totalEvaluationEventMatterWithBank = evaluationEventMatterService.countByEvaluationEventWithBank(id);
		if(totalEvaluationEventMatterWithBank > 0) {
			return 1;
		}

		return 0;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/{studentId}/matters/add", method=RequestMethod.POST,
			consumes="application/json", produces="application/json")
	public @ResponseBody void addMatters(@PathVariable Long id,  @PathVariable Long studentId,
			@RequestBody List<EvaluationEventMatter> matters) {

		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		User user = new User();
		user.setId(studentId);

		for(EvaluationEventMatter evaluationEventMatter: matters) {
			EvaluationAssignmentMatter evaluationAssignmentMatter = new EvaluationAssignmentMatter();
			EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUnique(evaluationEvent, user);
			evaluationEventMatter = evaluationEventMatterService.findById(evaluationEventMatter.getId());
			
			Center center = null;
			List<EvaluationAssignmentMatter> evaluationAssignmentMatters = evaluationAssignmentMatterService.findByEvaluationAssignment(evaluationAssignment.getId());
			if (!evaluationAssignmentMatters.isEmpty()) {
				center = evaluationAssignmentMatters.get(0).getCenter();
			}
			
			evaluationAssignmentMatter.setEvaluationAssignment(evaluationAssignment);
			evaluationAssignmentMatter.setEvaluationEventMatter(evaluationEventMatter);
			evaluationAssignmentMatter.setCenter(center);
			evaluationAssignmentMatter.setChannel("EVS");

			evaluationAssignmentMatterService.save(evaluationAssignmentMatter);

			// crear asociacion con test del alumno
			HashMap<String, EvaluationType> evalTypesMap = this.loadEvaluationTypes(evaluationEvent.getEvaluationTypes());
			if(evaluationEventMatter.getBank() != null) {
				
				// MODIFICADO: usando test activos
				List<Test> activeTests = testService.findByBankAndActive(evaluationAssignmentMatter.getEvaluationEventMatter().getBank().getId());	
				for(Test test : activeTests) {
					if(evalTypesMap.containsKey(test.getEvaluationType().getCode())) {
						EvaluationEventMatterTest evaluationEventMatterTest = evaluationEventMatterTestService.findByUnique(test, evaluationEventMatter);
						if(evaluationEventMatterTest == null) {
							evaluationEventMatterTest = new EvaluationEventMatterTest();
							evaluationEventMatterTest.setTest(test);
							evaluationEventMatterTest.setEvaluationEventMatter(evaluationEventMatter);
							evaluationEventMatterTestService.save(evaluationEventMatterTest);
						}
						MatterTestStudent matterTestStudent = matterTestStudentService.findByUnique(evaluationEventMatterTest, evaluationAssignmentMatter);
						if(matterTestStudent == null) {
							matterTestStudent = new MatterTestStudent();
							matterTestStudent.setEvaluationAssignmentMatter(evaluationAssignmentMatter);
							matterTestStudent.setEvaluationEventMatterTest(evaluationEventMatterTest);
							matterTestStudentService.save(matterTestStudent);
						}
					}
				}
			}
		}

	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/student/{studentId}/matter/{matterId}/tests", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Test> loadMatterTests(@PathVariable Long id, @PathVariable Long studentId, @PathVariable Long matterId) {
		EvaluationAssignmentMatter evaluationAssignmentMatter = evaluationAssignmentMatterService.find(id, studentId, matterId);
		List<MatterTestStudent> matterTestStudents = matterTestStudentService.findByEvaluationAssignmentMatter(evaluationAssignmentMatter);
		List<Test> tests = new ArrayList<Test>();
		for(MatterTestStudent matterTestStudent : matterTestStudents) {
			Test test = matterTestStudent.getEvaluationEventMatterTest().getTest();
			test.getBank().setEvaluationEventType(null);
			tests.add(test);
		}

		return tests;
	}


	public HashMap<String, EvaluationType> loadEvaluationTypes(Set<EvaluationType> evaluationTypes) {
		HashMap<String, EvaluationType> evalTypesMap = new HashMap<String, EvaluationType>();
		for(EvaluationType evaluationType : evaluationTypes) {
			evalTypesMap.put(evaluationType.getCode(), evaluationType);
		}
		return evalTypesMap;
	}

	private void deleteStudentMatterTests(EvaluationAssignmentMatter evaluationAssignmentMatter) {
		// borrar test si los hubiese
		 matterTestStudentService.deleteByEvaluationAssignmentMatter(evaluationAssignmentMatter);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students/loadstudents", method=RequestMethod.GET)
	public @ResponseBody Message loadWSStudents(@PathVariable Long id, HttpServletRequest request) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);

		String modeLoad = request.getParameter("modeLoad");
		String academicPeriodLoad = request.getParameter("academicPeriodLoad");
		String academicLevelLoad = request.getParameter("academicLevelLoad");

		Message message = evaluationClient.loadStudents(evaluationEvent, null, academicPeriodLoad, modeLoad, academicLevelLoad, true);

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students/loadstudentspr", method=RequestMethod.GET)
	public @ResponseBody Progress studentsProgress(@PathVariable Long id, HttpServletRequest request) {
		Progress progress = new Progress();
		progress.setRunning((evaluationClient.STUDENTS_RUNNING?1:0));
		progress.setProgress(evaluationClient.STUDENTS_PROGRESS);
		progress.setMessage(evaluationClient.STUDENTS_MESSAGE);
		progress.setMessage_type(evaluationClient.STUDENTS_MESSAGE_TYPE);
		return progress;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/students/loadstudentsreset", method=RequestMethod.GET)
	public @ResponseBody void loadReset(@PathVariable Long id, HttpServletRequest request) {
		evaluationClient.STUDENTS_RUNNING = false;
		evaluationClient.STUDENTS_PROGRESS = 0;
	}

}