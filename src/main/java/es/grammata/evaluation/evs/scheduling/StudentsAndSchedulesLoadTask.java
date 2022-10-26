package es.grammata.evaluation.evs.scheduling;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.StudentTestScheduleLog;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;

/**
 * @author ajmedialdea
 *
 */
public class StudentsAndSchedulesLoadTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsAndSchedulesLoadTask.class);

    private Long evaluationEventId;

    private StudentTestsSchedules studentTestsSchedules;

    private CenterService centerService;

    private EvaluationCenterService evaluationCenterService;

    private ClassroomTimeBlockService classroomTimeBlockService;

    private EvaluationEventMatterService evaluationEventMatterService;

    private MatterTestStudentService matterTestStudentService;

    private EvaluationClient evaluationClient;

    private EvaluationEventService evaluationEventService;

    private EvaluationAssignmentService evaluationAssignmentService;

    private StudentsAndSchedulesLoadTask() {

    }

    public StudentsAndSchedulesLoadTask(Long evaluationEventId, StudentTestsSchedules studentTestsSchedules,
	    CenterService centerService, EvaluationCenterService evaluationCenterService,
	    ClassroomTimeBlockService classroomTimeBlockService,
	    EvaluationEventMatterService evaluationEventMatterService,
	    MatterTestStudentService matterTestStudentService, EvaluationClient evaluationClient,
	    EvaluationEventService evaluationEventService, EvaluationAssignmentService evaluationAssignmentService) {
		super();
		this.evaluationEventId = evaluationEventId;
		this.studentTestsSchedules = studentTestsSchedules;
		this.centerService = centerService;
		this.evaluationCenterService = evaluationCenterService;
		this.classroomTimeBlockService = classroomTimeBlockService;
		this.evaluationEventMatterService = evaluationEventMatterService;
		this.matterTestStudentService = matterTestStudentService;
		this.evaluationClient = evaluationClient;
		this.evaluationEventService = evaluationEventService;
		this.evaluationAssignmentService = evaluationAssignmentService;
    }

    @Override
    public void run() {
    	  	
    	EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
    	if(evaluationEvent.getConfiguration().getAssignmentType().equals(EvaluationEventConfiguration.AUTOMATIC_TYPE)) {
			if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("Comezando proceso de carga en segundo plano...");
			}
			List<EvaluationEventMatter> eems = evaluationEventMatterService.findByEvaluationEvent(evaluationEventId);
			
			if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("Comezando proceso de carga de estudiantes...");
			}
		
			// TODO que escriba en el log
			for (EvaluationEventMatter eem : eems) {
			    evaluationClient.loadStudents(evaluationEvent, eem.getMatter().getCode(), eem.getAcademicPeriod().getCode(),
				    eem.getMode().getCode(), eem.getMatter().getAcademicLevel().getCode(), true);
			}
		
			if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("Comezando proceso de asignación de horarios...");
			}
			assignSchedules(evaluationEventId);
		
			if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("Finalizado proceso de carga en segundo plano\n\n\n\n");
			}
		
			if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("La tareas '" + StudentsAndSchedulesLoadTask.class.getSimpleName()
				    + "' ha finalizado correctamente.");
			}
    	} else {
    		LOGGER.info("El evento es de tipo de asignación manual, no se ejecutará la tarea de carga\n\n\n\n");
    	}
    }

    @Transactional
    private Message assignSchedules(Long evaluationEventId) {
		Message responseMessage = new Message();
	
//		try {
//		    List<EvaluationAssignment> evaluationAssignments = evaluationAssignmentService.findByEvaluationEvent(evaluationEventId);
//		    Center center = null;
//		    List<ClassroomTimeBlock> timeBlocks = null;
//		    List<EvaluationCenter> evaluationCentersAllowed = null;
//	
//		    for (EvaluationAssignment evaluationAssignment : evaluationAssignments) {
//				studentTestsSchedules.setEvaluationAssignment(evaluationAssignment);
//				List<MatterTestStudentInfo> matterTestStudents = matterTestStudentService.findInfoWithoutStudentEvaluationByEvaluationAssignment(evaluationAssignment.getId());
//		
//				for (MatterTestStudentInfo matterTestStudent : matterTestStudents) {
//				    if (matterTestStudent.getCenterId() != null) {
//					if (center == null || center.getId() != matterTestStudent.getCenterId()) {
//					    center = centerService.findById(matterTestStudent.getCenterId());
//					    evaluationCentersAllowed = evaluationCenterService.findByEvaluationEventAndCenter(
//						    evaluationAssignment.getEvaluationEvent().getId(), center.getId());
//					    timeBlocks = classroomTimeBlockService.findByEvaluationEventAndCenter(
//						    evaluationAssignment.getEvaluationEvent().getId(), center.getId());
//					}
//		
//					if (LOGGER.isInfoEnabled()) {
//					    LOGGER.info("Estudiante: " + matterTestStudent.getStudentEvaluationId() + " - Asignatura: "
//						    + matterTestStudent.getMatterName() + " - Test: " + matterTestStudent.getTestName());
//					}
//		
//					if (!timeBlocks.isEmpty()) {
//					    List<WeekDay> matterDaysAllowed = evaluationEventMatterService
//						    .getDaysAllowed(matterTestStudent.getEvaluationEventMatterId());
//		
//					    boolean scheduleAssigned = studentTestsSchedules.assignScheduleToTest(matterTestStudent,
//						    matterDaysAllowed, evaluationCentersAllowed, timeBlocks);
//		
//					    if (!scheduleAssigned) {
//						if (LOGGER.isInfoEnabled()) {
//						    LOGGER.info("No asignado bloque de tiempo");
//						}
//						// No se ha asignado horario al test, obtenemos
//						// el log con los motivos de por que no se ha
//						// podido asignar a cada bloque horario
//						List<StudentTestScheduleLog> logs = studentTestsSchedules.getTestScheduleLog();
//						for (StudentTestScheduleLog studentLog : logs) {
//						    String logMessage = studentLog.getMessage();
//						    if (logMessage != null) {
//							logMessage.replace("<br />", "");
//							if (LOGGER.isInfoEnabled()) {
//							    LOGGER.info(studentLog.getMessage());
//							}
//						    }
//						}
//					    } else {
//						// LOGGER.info("Asignado bloque de tiempo");
//					    }
//		
//					    studentTestsSchedules.clearTestScheduleLog();
//					} else {
//					    if (LOGGER.isInfoEnabled()) {
//						LOGGER.info("El centro \"" + center.getName()
//						    + "\" no tiene asociado ningún centro de evalución con horarios definidos");
//					    }
//					    throw new Exception("El centro \"" + center.getName()
//						    + "\" no tiene asociado ningún centro de evalución con horarios definidos");
//					}
//				    } else {
//					if (LOGGER.isInfoEnabled()) {
//					    LOGGER.info("El estudiante \"" + evaluationAssignment.getUser().getFullName()
//						+ "\" no está asociado a ningún centro para la asignatura \""
//						+ matterTestStudent.getMatterName() + "\"");
//					}
//					throw new Exception("El estudiante \"" + evaluationAssignment.getUser().getFullName()
//						+ "\" no está asociado a ningún centro para la asignatura \""
//						+ matterTestStudent.getMatterName() + "\"");
//				    }
//				}
//		
//				// Guardamos las asignaciones de test a horarios para el
//				// EvaluationAssignment
//				studentTestsSchedules.saveTestsSchedules();
//		    }
//	
//		    if (LOGGER.isInfoEnabled()) {
//			LOGGER.info("Horarios asignados correctamente a los tests");
//		    }
//		} catch (Exception e) {
//		    LOGGER.error("Se ha producido un error al asignar los horarios a los tests: <br /><br />" + e.getMessage());
//		}
	
		return responseMessage;
    }

}
