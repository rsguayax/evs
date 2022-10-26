package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.ScheduleModificationDate;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.TestSync;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.GenericScheduleLogService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationDateService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.data.services.repository.StudentTestScheduleLogService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.TestSyncService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo2;
import es.grammata.evaluation.evs.mvc.controller.util.DayTest;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationAssignmentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationCenterTimeBloks;
import es.grammata.evaluation.evs.mvc.controller.util.ExtendedUser;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Progress;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.mvc.controller.util.TestGroup;
import es.grammata.evaluation.evs.services.httpservices.client.EvaluationsEventClient;
import es.grammata.evaluation.evs.services.httpservices.client.TestClient;
import es.grammata.evaluation.evs.util.DateUtil;
import siette.models.Usuario;

@Controller
public class AdminEventController extends BaseController {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(AdminEventController.class);
	
	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired
	private ScheduleModificationDateService scheduleModificationDateService;
	
	@Autowired
	private EvaluationEventClassroomService evaluationEventClassroomService;
	
	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;
	
	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentTestsSchedules studentTestsSchedules;
	
	@Autowired
	private StudentTestScheduleLogService studentTestScheduleLogService;
	
	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;
	
	@Autowired
	private TestSyncService testSyncService;
	
	@Autowired
	private TestClient testClient;
	
	@Autowired
	private EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;
	
	@Autowired
	private EvaluationsEventClient evaluationsEventClient;

	@Autowired
	private GenericScheduleLogService genericScheduleLogService;
	
	@Autowired
	private StudentEvaluationService studentEvaluationService;
	
	@Autowired
	private AvailableStateService availableStateService;
	
	@Autowired
	private TestService testService;
	
	@Value("${settings.debug}")
	private boolean settingsdDebug;
	
	public static boolean RUNNING = false;

	public static boolean DELETE_SCHEDULES_RUNNING = false;
	
	public static int DELETE_SCHEDULES_PROGRESS = 0;
	
	public static boolean EXPORT_SCHEDULES_SIETTE_RUNNING = false;
	
	public static int EXPORT_SCHEDULES_SIETTE_PROGRESS = 0;
	
	@RequestMapping("/evaluationevent/{evaluationEventId}/admin")
	public String admin(Map<String, Object> model, @PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		model.put("evaluationEvent", evaluationEvent);
		EvaluationEventConfiguration conf = evaluationEvent.getConfiguration();
		String type = EvaluationEventConfiguration.MANUAL_TYPE;
		if(conf != null) {
			type = conf.getAssignmentType();
		}
		model.put("evaluationEvent", evaluationEvent);
		model.put("assignmentType", type);
		model.put("debug", settingsdDebug);
		
		return "evaluation_event/admin/admin";
	}
	
	@Transactional
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/eventcenter/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventEvaluationCenter> listEventCenters(@PathVariable Long evaluationEventId) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		
		//Hibernate.initialize(evaluationEvent.getEvaluationEventEvaluationCenters());
		List<EvaluationEventEvaluationCenter> eventCenters = evaluationEventEvaluationCenterService.findByEvaluationEvent(evaluationEventId);
		for(EvaluationEventEvaluationCenter eeec : eventCenters) {
			Hibernate.initialize(eeec.getEvaluationEventClassrooms());
		}
		
		
		return eventCenters;
	}
	
	@RequestMapping(value="/eventcenter/{eventCenterId}/eventclassroom/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<GenericInfo> listEventClassrooms(@PathVariable Long eventCenterId) {
		List<GenericInfo> eventClassrooms = evaluationEventClassroomService.findInfoByEventCenter(eventCenterId);
		
		return eventClassrooms;
	}
	
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/schedulemodificationdate/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<ScheduleModificationDate> listScheduleModificationDates(@PathVariable Long evaluationEventId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		Set<ScheduleModificationDate> scheduleModificationDates = evaluationEvent.getScheduleModificationDates();
		
		return scheduleModificationDates;
	}
	
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/addschedulemodificationdate", method=RequestMethod.POST)
	public @ResponseBody Message addScheduleModificationDate(@PathVariable Long evaluationEventId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		Message responseMessage = new Message();
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
			
			ScheduleModificationDate scheduleModificationDate = new ScheduleModificationDate(evaluationEvent, dateFormat.parse(startDate), dateFormat.parse(endDate));
			scheduleModificationDateService.save(scheduleModificationDate);
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Rango de fechas creado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al añadir el rango de fechas: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}
	
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/schedulemodificationdate/{scheduleModificationDateId}/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteScheduleModificationDate(@PathVariable Long evaluationEventId, @PathVariable Long scheduleModificationDateId) {
		Message responseMessage = new Message();
		
		try {
			scheduleModificationDateService.delete(scheduleModificationDateId);
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Rango de fechas eliminado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al eliminar el rango de fechas: <br /><br />" + e.getMessage());
		}
		
		return responseMessage;
	}
	
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/schedulesinfo", method=RequestMethod.GET)
	public @ResponseBody GenericInfo testsSchedulesInfo(@PathVariable Long evaluationEventId) {
		GenericInfo testsSchedulesInfo = matterTestStudentService.getTestsSchedulesInfo(evaluationEventId);
		testsSchedulesInfo.put("logs", studentTestScheduleLogService.getCountByEvaluationEvent(evaluationEventId));
		testsSchedulesInfo.put("genericlogs", genericScheduleLogService.getCountByEvaluationEvent(evaluationEventId));
		return testsSchedulesInfo;
	}
	
	@Transactional
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/availableseatsinfo", method=RequestMethod.GET)
	public @ResponseBody GenericInfo availableSeatsInfo(@PathVariable Long evaluationEventId) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
		JSONArray evaluationCentersJson = new JSONArray();
		int totalAvailableSeats = 0;
		
		List<EvaluationEventEvaluationCenter> eventCenters = evaluationEventEvaluationCenterService.findByEvaluationEvent(evaluationEventId);
		for (EvaluationEventEvaluationCenter eventCenter : eventCenters) {
			int centerAvailableSeats = 0;
			
			Hibernate.initialize(eventCenter.getEvaluationEventClassrooms());
			for (EvaluationEventClassroom eventClassroom : eventCenter.getEvaluationEventClassrooms()) {
				for (ClassroomTimeBlock classroomTimeBlock : eventClassroom.getClassroomTimeBlocks()) {
					Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
					centerAvailableSeats += classroomTimeBlock.getAvailableSeats();
				}
			}
			
			if(centerAvailableSeats < 0) {
				centerAvailableSeats = 0;
			}
			
			totalAvailableSeats += centerAvailableSeats;
			
			JSONObject evaluationCenterJson = new JSONObject();
			evaluationCenterJson.put("name", eventCenter.getEvaluationCenter().getName());
			evaluationCenterJson.put("availableSeats", centerAvailableSeats);
			evaluationCentersJson.put(evaluationCenterJson);
		}
		
		JSONObject availableSeatsInfoJson = new JSONObject();
		availableSeatsInfoJson.put("totalAvailableSeats", totalAvailableSeats);
		availableSeatsInfoJson.put("evaluationCenters", evaluationCentersJson);
		GenericInfo availableSeatsInfo = new GenericInfo(availableSeatsInfoJson);
		
		return availableSeatsInfo;
	}
	
	@Transactional
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assignschedules", method=RequestMethod.GET)
	public @ResponseBody Message assignSchedulesToTests(@PathVariable Long evaluationEventId) {
		Message responseMessage = new Message();
		
		if(!RUNNING) {
			try {
				RUNNING = true;
				
				List<EvaluationAssignment> evaluationAssignments = evaluationAssignmentService.findWithTestsWithoutScheduleByEvaluationEvent(evaluationEventId);
				
				List<MatterTestStudentInfo> matterTestStudents = matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationEvent(evaluationEventId);
				HashMap<Long, List<MatterTestStudentInfo>> evaluationAssignmentsMatterTestStudents = new HashMap<Long, List<MatterTestStudentInfo>>();
				for (MatterTestStudentInfo mts : matterTestStudents) {
					if (!evaluationAssignmentsMatterTestStudents.containsKey(mts.getEvaluationAssignmentId())) {
						evaluationAssignmentsMatterTestStudents.put(mts.getEvaluationAssignmentId(), new ArrayList<MatterTestStudentInfo>());
					}
					
					evaluationAssignmentsMatterTestStudents.get(mts.getEvaluationAssignmentId()).add(mts);
				}
				
				List<EvaluationEventEvaluationCenter> evaluationEventEvaluationCenters = evaluationEventEvaluationCenterService.findByEvaluationEvent(evaluationEventId);
				HashMap<Long, EvaluationCenterTimeBloks> evaluationCentersTimeBloks = new HashMap<Long, EvaluationCenterTimeBloks>();
				for (EvaluationEventEvaluationCenter evaluationEventEvaluationCenter : evaluationEventEvaluationCenters) {
					List<ClassroomTimeBlock> timeBlocks = classroomTimeBlockService.findByEventCenter(evaluationEventEvaluationCenter.getId());
					evaluationCentersTimeBloks.put(evaluationEventEvaluationCenter.getEvaluationCenter().getId(), new EvaluationCenterTimeBloks(evaluationEventEvaluationCenter.getEvaluationCenter(), timeBlocks));
				}
				
				// Marcamos como leidos todos los logs de la asignación de horarios a test anterior 
				//studentTestsSchedules.markAllTestScheduleLogAsRead();
				studentTestsSchedules.deleteLogByEvaluationEvent(evaluationEventId);
				
				long startTime = System.currentTimeMillis();
				int count = 0;
				int size = evaluationAssignments.size();
				for (EvaluationAssignment evaluationAssignment : evaluationAssignments) {
					count++;
					
					List<MatterTestStudentInfo> evaluationAssignmentMatterTestStudents = evaluationAssignmentsMatterTestStudents.get(evaluationAssignment.getId());
					studentTestsSchedules.processAssignment(evaluationAssignment, evaluationCentersTimeBloks, evaluationAssignmentMatterTestStudents, count, size);	
					
					if((count % 100) == 0) {
						long endTimeTmp = System.currentTimeMillis();
						showDebugMessage("-----Tiempo total acumulado " + count + " de " + size + " en " + (endTimeTmp - startTime) + " milisegundos");
					}
				}
				
				long endTime = System.currentTimeMillis();
				showDebugMessage("TOTAL: " + count + " en " + (endTime - startTime) + " milisegundos");
	
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Horarios asignados correctamente a los tests");
			} catch (Exception e) {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha producido un error al asignar los horarios a los tests: <br /><br />" + e.getMessage());
			} finally {
				RUNNING = false;
			}
		} else {
			responseMessage.setType(Message.TYPE_WARNING);
			responseMessage.setMessage("Ya existe un proceso de asignación en curso, espere unos momentos");
		}
		
		return responseMessage;
	}
	
	@Transactional
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/deletechedules", method=RequestMethod.GET)
	public @ResponseBody Message deleteSchedules(@PathVariable Long evaluationEventId) {
		Message responseMessage = new Message();
		
		if(settingsdDebug && !DELETE_SCHEDULES_RUNNING) {
			try {
				DELETE_SCHEDULES_RUNNING = true;
				DELETE_SCHEDULES_PROGRESS = 0;
				int studentEvaluationsProcessed = 0;
				AvailableState availableState = availableStateService.findByState(AvailableState.AVAILABLE);
				
				List<Long> studentEvaluationsIds = studentEvaluationService.findIdsByEvaluationEvent(evaluationEventId);
				for (Long studentEvaluationId : studentEvaluationsIds) {
					matterTestStudentService.deleteAllByStudentEvaluation(studentEvaluationId);
					studentEvaluationService.deleteById(studentEvaluationId);
					
					studentEvaluationsProcessed += 1;
					DELETE_SCHEDULES_PROGRESS = (studentEvaluationsProcessed * 100) / studentEvaluationsIds.size();
				}
				
				List<ClassroomTimeBlock> classroomTimeBlocks = classroomTimeBlockService.findByEvaluationEvent(evaluationEventId);
				for (ClassroomTimeBlock classroomTimeBlock : classroomTimeBlocks) {
					classroomTimeBlock.setAvailableState(availableState);
					classroomTimeBlockService.update(classroomTimeBlock);
				}
	
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Horarios del evento de evaluación eliminados correctamente");
			} catch (Exception e) {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha producido un error al eliminar los horarios : <br /><br />" + e.getMessage());
			} finally {
				DELETE_SCHEDULES_RUNNING = false;
			}
		} else {
			responseMessage.setType(Message.TYPE_WARNING);
			responseMessage.setMessage("Ya existe un proceso de eliminación de horarios en curso, espere unos momentos");
		}
		
		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assign/isrunning", method=RequestMethod.GET)
	public @ResponseBody boolean isRunning(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		return RUNNING;
	}
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assignreset", method=RequestMethod.GET)
	public @ResponseBody void loadReset(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		RUNNING = false;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assign/progress", method=RequestMethod.GET)
	public @ResponseBody int getProgress(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		GenericInfo testsSchedulesInfo = matterTestStudentService.getTestsSchedulesInfo(evaluationEventId);
		long testsWithoutSchedule = (Long) testsSchedulesInfo.get("testsWithoutSchedule");
		long testsWithSchedule = (Long) testsSchedulesInfo.get("testsWithSchedule");
		long total = testsWithoutSchedule + testsWithSchedule;
		return (int) ((testsWithSchedule * 100.0) / total);
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/deleteschedules/progress", method=RequestMethod.GET)
	public  @ResponseBody Progress getDeleteSchedulesProgress(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		Progress progress = new Progress();
		progress.setRunning(DELETE_SCHEDULES_RUNNING?1:0);
		progress.setProgress(DELETE_SCHEDULES_PROGRESS);
		return progress;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/exportschedulessiette/progress", method=RequestMethod.GET)
	public  @ResponseBody Progress getExportSchedulesSietteProgress(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		Progress progress = new Progress();
		progress.setRunning(EXPORT_SCHEDULES_SIETTE_RUNNING?1:0);
		progress.setProgress(EXPORT_SCHEDULES_SIETTE_PROGRESS);
		return progress;
	}
	
	/*@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processAssignment(Long eaId, Center center,
			List<ClassroomTimeBlock> timeBlocks,
			List<EvaluationCenter> evaluationCentersAllowed,
			int count, int totalAssignments) throws Exception {
		
		long startTime = System.currentTimeMillis();

		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findById(eaId);
		studentTestsSchedules.setEvaluationAssignment(evaluationAssignment);
		List<MatterTestStudentInfo> matterTestStudents = matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationAssignment(evaluationAssignment.getId());
		
		for (MatterTestStudentInfo matterTestStudent : matterTestStudents) {
			if (matterTestStudent.getCenterId() != null) {
				if (center == null || center.getId() != matterTestStudent.getCenterId()) {
					center = centerService.findById(matterTestStudent.getCenterId());
					evaluationCentersAllowed = evaluationCenterService.findByEvaluationEventAndCenter(evaluationAssignment.getEvaluationEvent().getId(), center.getId());
					timeBlocks = classroomTimeBlockService.findByEvaluationEventAndCenter(evaluationAssignment.getEvaluationEvent().getId(), center.getId());
				}
				
				if (!timeBlocks.isEmpty()) {
					List<WeekDay> matterDaysAllowed = evaluationEventMatterService.getDaysAllowed(matterTestStudent.getEvaluationEventMatterId());
					
					boolean scheduleAssigned = studentTestsSchedules.assignScheduleToTest(matterTestStudent, matterDaysAllowed, evaluationCentersAllowed, timeBlocks);

					if (!scheduleAssigned) {
						studentTestsSchedules.saveTestScheduleLog();
					}
					
					studentTestsSchedules.clearTestScheduleLog();
				} else {
					throw new Exception("El centro \"" + center.getName() + "\" no tiene asociado ningún centro de evalución con horarios definidos");
				}
			} else {
				throw new Exception("El estudiante \"" + evaluationAssignment.getUser().getFullName() + "\" no está asociado a ningún centro para la asignatura \"" + matterTestStudent.getMatterName() + "\"");
			}
		}
		
		studentTestsSchedules.saveTestsSchedules();
		
		long endTime = System.currentTimeMillis();

		showDebugMessage("Procesado de matricula " + count + " de " + totalAssignments
				+ " en " + (endTime - startTime) + " milisegundos");
	}*/
	
	
	@RequestMapping("/evaluationevent/{id}/tests/exportexternal")
	public synchronized @ResponseBody Message synchronize(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Message responseMessage = exportShedulesSietteWithoutCaps(evaluationEvent);
//		Message responseMessage = exportShedulesSietteWithCaps(evaluationEvent);
		return responseMessage;
	}
	
	private Message exportShedulesSietteWithoutCaps(EvaluationEvent evaluationEvent) { 
		Message responseMessage = new Message();
		DayTest procesedDayTest = null;
		
		if(!EXPORT_SCHEDULES_SIETTE_RUNNING) {
			EXPORT_SCHEDULES_SIETTE_RUNNING = true;
			EXPORT_SCHEDULES_SIETTE_PROGRESS = 0;
			int daysTestsProcessed = 0;
			
			String evaluationEventCode = evaluationEvent.getCode();
			
			try {
				long startTime = System.currentTimeMillis();
				evaluationsEventClient.createEvaluationEvent(evaluationEventCode, evaluationEvent.getName(), evaluationEvent.getStartDate(), evaluationEvent.getEndDate());
				
				TestSync testSync = new TestSync();
				testSync.setState(TestSync.STATE_START);
				testSync.setEvaluationEvent(evaluationEvent);
				testSyncService.save(testSync);
				
				evaluationsEventClient.initEvaluationEvent(evaluationEventCode);
				
				// Para cada test del evento de evaluación creamos un grupo de usuarios por día
				HashMap<DayTest, List<MatterTestStudentInfo>> dayTestStudentsMap = new HashMap<DayTest, List<MatterTestStudentInfo>>();
				List<MatterTestStudentInfo> matterTestStudents = matterTestStudentService.findInfoWithStudentEvaluationByEvaluationEvent(evaluationEvent.getId());
				for (MatterTestStudentInfo mts : matterTestStudents) {
					DayTest dayTest = new DayTest(mts);
					
					if (!dayTestStudentsMap.containsKey(dayTest)) {
						dayTestStudentsMap.put(dayTest, new ArrayList<MatterTestStudentInfo>());
					}
					
					dayTestStudentsMap.get(dayTest).add(mts);
				}
				
				for (Entry<DayTest, List<MatterTestStudentInfo>> entry : dayTestStudentsMap.entrySet()) {
					DayTest dayTest = entry.getKey();
					procesedDayTest = dayTest;
					Date startDate = DateUtil.setTime(dayTest.getDay(), "07:00:00");
					Date endDate = DateUtil.setTime(dayTest.getDay(), "23:00:00");
					List<Usuario> users = new ArrayList<Usuario>();
					MatterTestStudentInfo firstMatterTestStudent = entry.getValue().get(0);
					Test test = testService.findById(firstMatterTestStudent.getTestId());
					int testId = test.getExternalId();
					String matterCode = firstMatterTestStudent.getMatterCode();
					int bankId = test.getBank().getExternalId();
					String evaluationType = test.getEvaluationType().getCode();
					int idProfesor = 1;
					
					for (MatterTestStudentInfo mts : entry.getValue()) {
						User user = userService.findById(mts.getUserId());
						
						if (user.getUsername() == null || user.getUsername().isEmpty()) {
							throw new Exception("El usuario con ID " + user.getId() + " no tiene nombre de usuario");
						}
						
						Usuario usuario = new Usuario(user.getFullName(), user.getIdentification(), user.getUsername(), mts.getExternalPassword(), user.getEmail());
						users.add(usuario);
					}
					
					long startTime2 = System.currentTimeMillis();
					
					int groupId = testClient.callSetGroupWithoutCap(evaluationEvent, testId, users, startDate, endDate, idProfesor, bankId, matterCode, evaluationType);
				    if(groupId == -1) {
				    	throw new Exception("Error en el test con ID: " + dayTest.getTestId() + " el día: " + dayTest.getDay());
				    }
				    
				    long endTimeTmp2 = System.currentTimeMillis();
					showDebugMessage("Tiempo sincronizar grupo con Siette: " + (endTimeTmp2 - startTime2) + " milisegundos");
					
					daysTestsProcessed += 1;
				    EXPORT_SCHEDULES_SIETTE_PROGRESS = (daysTestsProcessed * 100) / dayTestStudentsMap.size();
				    
				    showDebugMessage("Progreso: " + EXPORT_SCHEDULES_SIETTE_PROGRESS + "%");
				}

				testSync = new TestSync();
				testSync.setState(TestSync.STATE_END);
				testSync.setEvaluationEvent(evaluationEvent);
				testSyncService.save(testSync);
				
				evaluationsEventClient.endEvaluationEvent(evaluationEventCode);
				
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Información de alumnos, horarios y tests exportados correctamente");
				
				long endTimeTmp = System.currentTimeMillis();
				showDebugMessage((endTimeTmp - startTime) + " milisegundos");
			} catch (Exception ex) {
				String exMessage = procesedDayTest!= null ? "Se ha producido un error procesando el test con ID: " + procesedDayTest.getTestId() + " del dia: " + procesedDayTest.getDay() : "";
				String respMessage ="Error al exportar información de alumnos, horarios y tests";
						
				if (ex.getMessage() != null) {
					exMessage += "\nError: " + ex.getMessage();
					respMessage += "<br/>Error: " + ex.getMessage();
				}
				
				TestSync testSync = new TestSync();
				testSync.setState(TestSync.STATE_ERROR);
				testSync.setEvaluationEvent(evaluationEvent);
				testSync.setDescription(exMessage);		
				testSyncService.save(testSync);
				
				evaluationsEventClient.errorEvaluationEvent(evaluationEventCode, "Test con ID:" + procesedDayTest.getTestId() + " Dia: " + procesedDayTest.getDay());
				
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage(respMessage);
			} finally {
				EXPORT_SCHEDULES_SIETTE_RUNNING = false;
			}
		} else {
			responseMessage.setType(Message.TYPE_WARNING);
			responseMessage.setMessage("Ya existe un proceso de exportación de horarios a Siette en curso, espere unos momentos");
		}
		
		return responseMessage;
	}
	
	private Message exportShedulesSietteWithCaps(EvaluationEvent evaluationEvent) { 
		Message responseMessage = new Message();
		EvaluationEventMatterTest procesedEvaluationEventMatterTest = null;
		
		if(!EXPORT_SCHEDULES_SIETTE_RUNNING) {
			EXPORT_SCHEDULES_SIETTE_RUNNING = true;
			EXPORT_SCHEDULES_SIETTE_PROGRESS = 0;
			int testsProcessed = 0;
			
			String evaluationEventCode = evaluationEvent.getCode();
			
			try {
				long startTime = System.currentTimeMillis();
				evaluationsEventClient.createEvaluationEvent(evaluationEventCode, evaluationEvent.getName(), evaluationEvent.getStartDate(), evaluationEvent.getEndDate());
				
				TestSync testSync = new TestSync();
				testSync.setState(TestSync.STATE_START);
				testSync.setEvaluationEvent(evaluationEvent);
				testSyncService.save(testSync);
				
				evaluationsEventClient.initEvaluationEvent(evaluationEventCode);
				
				List<EvaluationEventMatterTest> eemts = null;
				if (evaluationEvent.isAdmissionOrComplexiveType()) {
					eemts = evaluationEventMatterTestService.findByEvaluationEvent(evaluationEvent.getId(), false);
				} else {
					eemts = evaluationEventMatterTestService.findByEvaluationEvent(evaluationEvent.getId(), true);
				}
				
				for(EvaluationEventMatterTest eemt : eemts) {
					procesedEvaluationEventMatterTest = eemt;
					HashMap<ClassroomTimeBlockInfo2, List<ExtendedUser>> schedules = new HashMap<ClassroomTimeBlockInfo2, List<ExtendedUser>>();
					
					int bankExternalId = eemt.getEvaluationEventMatter().getBank().getExternalId();
					int testExternalId = eemt.getTest().getExternalId();
					String matterCode = eemt.getEvaluationEventMatter().getMatter().getCode();
					
					long startTime2 = System.currentTimeMillis();
					List<MatterTestStudentInfo> mtss = matterTestStudentService.findInfoByEvaluationEventMatterTest(eemt.getId());
					
					// agrupamos por horario
					for(MatterTestStudentInfo mts : mtss) {
						EvaluationAssignmentInfo evaluationAssignment = evaluationAssignmentService.findInfoById(mts.getEvaluationAssignmentId());
						User user = userService.findById(evaluationAssignment.getUserId())  ;
						ExtendedUser eu = new ExtendedUser();
						eu.setUser(user);
						eu.setExternalPassword(evaluationAssignment.getExternalPassword());
						
						if (mts.getClassroomTimeBlockId() != null) {
							ClassroomTimeBlockInfo2 ctb = classroomTimeBlockService.findInfoById(mts.getClassroomTimeBlockId());
							if (ctb != null) {
								if(schedules.containsKey(ctb)) {
									schedules.get(ctb).add(eu);
								} else {
									 List<ExtendedUser> users = new ArrayList<ExtendedUser>();
									 users.add(eu);
									 schedules.put(ctb, users);
								}
							}
						}
					}
						
					// para cada test enviamos a siette por horario
					List<TestGroup> testGroups = new ArrayList<TestGroup>();
					for (Entry<ClassroomTimeBlockInfo2, List<ExtendedUser>> entry : schedules.entrySet()) {
				    	ClassroomTimeBlockInfo2 ctb = (ClassroomTimeBlockInfo2)entry.getKey();
				        List<ExtendedUser> users = (List<ExtendedUser>)entry.getValue();

				        String capSsid = ctb.getCapSsid() != null ? ctb.getCapSsid() : "NoCapSsid";
				    	
						TestGroup testGroup = new TestGroup();
						testGroup.setBankExternlId(bankExternalId);
						testGroup.setMatterCode(matterCode);
						
						// TODO
						/****** Hasta nueva orden usamos el intervalo de fechas que comprende a todo el evento de evaluacion ***/
						//testGroup.setEndDate(ctb.getEndDate());
						//testGroup.setInitDate(ctb.getStartDate());				
						//testGroup.setEndDate(evaluationEvent.getEndDate());
						//testGroup.setInitDate(evaluationEvent.getStartDate());
						
						/*** NUEVA ORDEN (por Diego): la jornada de 08:00 a 20:00 **/					
						DateTime startDt = new DateTime(ctb.getStartDate());
						DateTime endDt = new DateTime(ctb.getEndDate());
						
						startDt = startDt.hourOfDay().setCopy(8);
						startDt = startDt.minuteOfHour().setCopy(0);
						startDt = startDt.secondOfMinute().setCopy(0);
						
						endDt = endDt.hourOfDay().setCopy(20);
						endDt = endDt.minuteOfHour().setCopy(0);
						endDt = endDt.secondOfMinute().setCopy(0);
						
						testGroup.setEndDate(endDt.toDate());
						testGroup.setInitDate(startDt.toDate());
								
						testGroup.setTestExternalId(testExternalId);	
						testGroup.setUsers(users);
						testGroup.setCapSsid(capSsid);
						testGroup.setEvaluationEventCode(evaluationEventCode);
						if (eemt.getTest().getEvaluationType() != null) {
							testGroup.setEvaluationType(eemt.getTest().getEvaluationType().getCode());
						}
						
						String evalCenter = ctb.getEvaluationCenterCode();
						String classroom = ctb.getClassroomName();
						
						testGroup.setEvalCenter(evalCenter);
						testGroup.setClassroom(classroom);
						testGroup.setBlockId(ctb.getId().toString());
						testGroups.add(testGroup);   	
				    }
					
					long endTimeTmp2 = System.currentTimeMillis();
					showDebugMessage("Tiempo Agrupar por horario " + mtss.size() + " MatterTestStudents: " + (endTimeTmp2 - startTime2) + " milisegundos");
					
					long startTime3 = System.currentTimeMillis();
					showDebugMessage("Enviando a Siette el ID: " + eemt.getId());
				    boolean res = testClient.createGroupsWithCap(testGroups, evaluationEvent);
				    if(!res) {
				    	throw new Exception("Error en test de asignatura: " + eemt.getId() + " id de test de Siette: " + testExternalId);
				    }
				    long endTimeTmp3 = System.currentTimeMillis();
					showDebugMessage("Tiempo sincronizar " + testGroups.size() + " horarios con Siette: " + (endTimeTmp3 - startTime3) + " milisegundos");
				    
				    testsProcessed += 1;
				    EXPORT_SCHEDULES_SIETTE_PROGRESS = (testsProcessed * 100) / eemts.size();
				    
				    showDebugMessage("Progreso: " + EXPORT_SCHEDULES_SIETTE_PROGRESS + "%");
				}
				
				testSync = new TestSync();
				testSync.setState(TestSync.STATE_END);
				testSync.setEvaluationEvent(evaluationEvent);
				testSyncService.save(testSync);
				
				evaluationsEventClient.endEvaluationEvent(evaluationEventCode);
				
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Información de alumnos, horarios y tests exportados correctamente");
				
				long endTimeTmp = System.currentTimeMillis();
				showDebugMessage((endTimeTmp - startTime) + " milisegundos");
			} catch (Exception ex) {
				String exMessage = procesedEvaluationEventMatterTest!= null ? "Se ha producido un error procesando el EvaluationEventMatterTest con ID: " + procesedEvaluationEventMatterTest.getId() : "";
				String respMessage ="Error al exportar información de alumnos, horarios y tests";
						
				if (ex.getMessage() != null) {
					exMessage += "\nError: " + ex.getMessage();
					respMessage += "<br/>Error: " + ex.getMessage();
				}
				
				TestSync testSync = new TestSync();
				testSync.setState(TestSync.STATE_ERROR);
				testSync.setEvaluationEvent(evaluationEvent);
				testSync.setDescription(exMessage);		
				testSyncService.save(testSync);
				
				evaluationsEventClient.errorEvaluationEvent(evaluationEventCode, "EvaluationEventMatterTest con ID:" + procesedEvaluationEventMatterTest.getId());
				
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage(respMessage);
			} finally {
				EXPORT_SCHEDULES_SIETTE_RUNNING = false;
			}
		} else {
			responseMessage.setType(Message.TYPE_WARNING);
			responseMessage.setMessage("Ya existe un proceso de exportación de horarios a Siette en curso, espere unos momentos");
		}
		
		return responseMessage;
	}
	
//	@RequestMapping("/evaluationevent/{id}/tests/exportexternal")
//	public synchronized @ResponseBody Message synchronize(@PathVariable Long id) {
//		Message message = new Message();
//		
//		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
//		
//		String evaluationEventCode = evaluationEvent.getCode();
//		
//		try {
//			evaluationsEventClient.createEvaluationEvent(evaluationEventCode, evaluationEvent.getName(), evaluationEvent.getStartDate(), evaluationEvent.getEndDate());
//			
//			TestSync testSync = new TestSync();
//			testSync.setState(TestSync.STATE_START);
//			testSync.setEvaluationEvent(evaluationEvent);
//			testSyncService.save(testSync);
//			
//			evaluationsEventClient.initEvaluationEvent(evaluationEventCode);
//			
//			List<EvaluationEventMatterTest> eemts = null;
//			if (evaluationEvent.isAdmissionOrComplexiveType()) {
//				eemts = evaluationEventMatterTestService.findByEvaluationEvent(evaluationEvent.getId(), false);
//			} else {
//				eemts = evaluationEventMatterTestService.findByEvaluationEvent(evaluationEvent.getId(), true);
//			}
//			
//			for(EvaluationEventMatterTest eemt : eemts) {
//				HashMap<ClassroomTimeBlock, List<ExtendedUser>> schedules = new HashMap<ClassroomTimeBlock, List<ExtendedUser>>();
//				
//				int bankExternalId = eemt.getEvaluationEventMatter().getBank().getExternalId();
//				int testExternalId = eemt.getTest().getExternalId();
//				String matterCode = eemt.getEvaluationEventMatter().getMatter().getCode();
//				
//				List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTest(eemt);
//				
//				// agrupamos por horario
//				for(MatterTestStudent mts : mtss) {
//					User user = mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser();
//					ExtendedUser eu = new ExtendedUser();
//					eu.setUser(user);
//					eu.setExternalPassword(mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getExternalPassword());
//					
//					if(mts.getStudentEvaluation() != null && mts.getStudentEvaluation().getClassroomTimeBlock() != null) {
//						ClassroomTimeBlock ctb = mts.getStudentEvaluation().getClassroomTimeBlock();
//						if(schedules.containsKey(ctb)) {
//							schedules.get(ctb).add(eu);
//						} else {
//							 List<ExtendedUser> users = new ArrayList<ExtendedUser>();
//							 users.add(eu);
//							 schedules.put(ctb, users);
//						}
//					}
//				}
//				
//				// para cada test enviamos a siette por horario
//				List<TestGroup> testGroups = new ArrayList<TestGroup>();
//				Iterator it = schedules.entrySet().iterator();
//			    while (it.hasNext()) {
//			    	Map.Entry pair = (Map.Entry)it.next();
//			        ClassroomTimeBlock ctb = (ClassroomTimeBlock)pair.getKey();
//			        List<ExtendedUser> users = (List<ExtendedUser>)pair.getValue();
//			        
//			        String capSsid = ctb.getEvaluationEventClassroom().getCap().getSsid();
//			    	
//					TestGroup testGroup = new TestGroup();
//					testGroup.setBankExternlId(bankExternalId);
//					testGroup.setMatterCode(matterCode);
//					
//					// TODO
//					/****** Hasta nueva orden usamos el intervalo de fechas que comprende a todo el evento de evaluacion ***/
//					//testGroup.setEndDate(ctb.getEndDate());
//					//testGroup.setInitDate(ctb.getStartDate());				
//					//testGroup.setEndDate(evaluationEvent.getEndDate());
//					//testGroup.setInitDate(evaluationEvent.getStartDate());
//					
//					/*** NUEVA ORDEN (por Diego): la jornada de 08:00 a 20:00 **/					
//					DateTime startDt = new DateTime(ctb.getStartDate());
//					DateTime endDt = new DateTime(ctb.getEndDate());
//					
//					startDt = startDt.hourOfDay().setCopy(8);
//					startDt = startDt.minuteOfHour().setCopy(0);
//					startDt = startDt.secondOfMinute().setCopy(0);
//					
//					endDt = endDt.hourOfDay().setCopy(20);
//					endDt = endDt.minuteOfHour().setCopy(0);
//					endDt = endDt.secondOfMinute().setCopy(0);
//					
//					testGroup.setEndDate(endDt.toDate());
//					testGroup.setInitDate(startDt.toDate());
//							
//					testGroup.setTestExternalId(testExternalId);	
//					testGroup.setUsers(users);
//					testGroup.setCapSsid(capSsid);
//					testGroup.setEvaluationEventCode(evaluationEventCode);
//					if (eemt.getTest().getEvaluationType() != null) {
//						testGroup.setEvaluationType(eemt.getTest().getEvaluationType().getCode());
//					}
//					
//					String evalCenter = ctb.getEvaluationCenter().getCode();
//					String classroom = ctb.getEvaluationEventClassroom().getClassroom().getName();
//					
//					testGroup.setEvalCenter(evalCenter);
//					testGroup.setClassroom(classroom);
//					
//					testGroup.setBlockId(ctb.getId().toString());
//					
//					testGroups.add(testGroup);   	
//			    }
//			    
//			    boolean res = testClient.createGroups(testGroups, evaluationEvent);
//			    if(!res) {
//			    	throw new Exception("Error en test de asignatura: " + eemt.getId() + " id de test de Siette: " + testExternalId);
//			    }
//			}
//			
//			testSync = new TestSync();
//			testSync.setState(TestSync.STATE_END);
//			testSync.setEvaluationEvent(evaluationEvent);
//			testSyncService.save(testSync);
//			
//			evaluationsEventClient.endEvaluationEvent(evaluationEventCode);
//			
//			message.setType(Message.TYPE_SUCCESS);
//			message.setMessage("Información de alumnos, horarios y tests exportados correctamente");
//			
//		} catch (Exception ex) {
//			TestSync testSync = new TestSync();
//			testSync.setState(TestSync.STATE_ERROR);
//			testSync.setEvaluationEvent(evaluationEvent);
//			testSync.setDescription((ex.getCause()==null)?"":ex.getCause().toString().substring(1, 254));		
//			testSyncService.save(testSync);
//			
//			evaluationsEventClient.errorEvaluationEvent(evaluationEventCode, (ex.getMessage()==null)?"":ex.getMessage());
//			
//			message.setType(Message.TYPE_ERROR);
//			message.setMessage("Error al exportar información de alumnos, horarios y tests");
//		}
//		
//		return message;
//	}
	
	private void showDebugMessage(String message) {
		if (settingsdDebug) {
			System.out.println(message);
		}
	}
}
