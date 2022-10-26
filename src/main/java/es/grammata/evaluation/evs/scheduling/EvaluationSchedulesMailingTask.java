package es.grammata.evaluation.evs.scheduling;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.util.AssignedSchedulesMailer;

/**
 * @author ajmedialdea
 *
 */
public class EvaluationSchedulesMailingTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationSchedulesMailingTask.class);

    private Long evaluationEventId;

    public AssignedSchedulesMailer assignedSchedulesMailer;

    public EvaluationAssignmentService evaluationAssignmentService;
    

    private EvaluationSchedulesMailingTask() {

    }

    public EvaluationSchedulesMailingTask(Long evaluationEventId, AssignedSchedulesMailer assignedSchedulesMailer,
	    EvaluationAssignmentService evaluationAssignmentService) {
		super();
		this.evaluationEventId = evaluationEventId;
		this.assignedSchedulesMailer = assignedSchedulesMailer;
		this.evaluationAssignmentService = evaluationAssignmentService;
    }

    @Override
    public void run() {
		List<User> users = evaluationAssignmentService.findUnnotifiedUsersByEvaluationEvent(evaluationEventId);
		assignedSchedulesMailer.send(users);
	
		if (LOGGER.isInfoEnabled()) {
		    LOGGER.info("The task '" + EvaluationSchedulesMailingTask.class.getSimpleName() + "' has been successfully updated.");
		}
    }

}
