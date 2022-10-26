package es.grammata.evaluation.evs.scheduling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;
import es.grammata.evaluation.evs.util.AssignedSchedulesMailer;

/**
 * @author ajmedialdea
 *
 */
public class SchedulingApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static int counter = 0;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
	ApplicationContext applicationContext = event.getApplicationContext();

	TaskScheduler scheduler = (TaskScheduler) applicationContext.getBean("scheduler");

	TasksRegistry tasksRegistry = (TasksRegistry) applicationContext.getBean("tasksRegistry");

	EvaluationEventService evaluationEventService =
		(EvaluationEventService) applicationContext.getBean("evaluationEventServiceImpl");

	StudentTestsSchedules studentTestsSchedules =
		(StudentTestsSchedules) applicationContext.getBean("studentTestsSchedules");

	CenterService centerService =
		(CenterService) applicationContext.getBean("centerServiceImpl");

	EvaluationCenterService evaluationCenterService =
		(EvaluationCenterService) applicationContext.getBean("evaluationCenterServiceImpl");

	ClassroomTimeBlockService classroomTimeBlockService =
		(ClassroomTimeBlockService) applicationContext.getBean("classroomTimeBlockServiceImpl");

	EvaluationEventMatterService evaluationEventMatterService =
		(EvaluationEventMatterService) applicationContext.getBean("evaluationEventMatterServiceImpl");

	MatterTestStudentService matterTestStudentService =
		(MatterTestStudentService) applicationContext.getBean("matterTestStudentServiceImpl");

	EvaluationClient evaluationClient =
		(EvaluationClient) applicationContext.getBean("evaluationClient");

	EvaluationAssignmentService evaluationAssignmentService =
		(EvaluationAssignmentService) applicationContext.getBean("evaluationAssignmentServiceImpl");

	AssignedSchedulesMailer assignedSchedulesMailer =
		(AssignedSchedulesMailer) applicationContext.getBean("assignedSchedulesMailer");

	if (counter == 0) {
	    List<EvaluationEvent> evaluationEvents = evaluationEventService.findAll();
	    if (evaluationEvents != null) {
		for (EvaluationEvent evaluationEvent : evaluationEvents) {
		    Trigger t1 = new EvaluationEventConfigurationTrigger(evaluationEvent.getConfiguration(), EvaluationEventConfigurationTrigger.STUDENTS_AND_SCHEDULES_LOAD);
		    StudentsAndSchedulesLoadTask studentsAndSchedulesLoadTask = new StudentsAndSchedulesLoadTask(
			    evaluationEvent.getId(), studentTestsSchedules, centerService, evaluationCenterService,
			    classroomTimeBlockService, evaluationEventMatterService, matterTestStudentService, evaluationClient,
			    evaluationEventService, evaluationAssignmentService);
		    SchedulableTask st1 = new SchedulableTask(studentsAndSchedulesLoadTask, scheduler, t1);
		    tasksRegistry.register(evaluationEvent.getId(), st1);

		    Trigger t2 = new EvaluationEventConfigurationTrigger(evaluationEvent.getConfiguration(), EvaluationEventConfigurationTrigger.EVALUATION_SCHEDULES_MAILING);
		    EvaluationSchedulesMailingTask evaluationSchedulesMailingTask = new EvaluationSchedulesMailingTask(
			    evaluationEvent.getId(), assignedSchedulesMailer, evaluationAssignmentService);
		    SchedulableTask st2 = new SchedulableTask(evaluationSchedulesMailingTask, scheduler, t2);
		    tasksRegistry.register(evaluationEvent.getId(), st2);
		}
	    }
	}
	counter++;
    }

}
