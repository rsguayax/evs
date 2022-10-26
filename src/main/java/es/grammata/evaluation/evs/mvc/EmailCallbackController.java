package es.grammata.evaluation.evs.mvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grammata.evaluation.evs.data.model.repository.EmailNotification;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EmailNotificationService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class EmailCallbackController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger(EmailCallbackController.class);

    private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("EEEE, d MMMM yyyy");

    private static final SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm");

    @Autowired
    private EmailNotificationService emailNotificactionService;

    @Autowired
    private EvaluationAssignmentService evaluationAssignmentService;

    @Autowired
    private MatterTestStudentService matterTestStudentService;

    @RequestMapping("/email-callback")
    public String callback(@RequestParam("token") String token, Map<String, Object> model) {
		if (LOGGER.isInfoEnabled()) {
		    LOGGER.info("Test callback with token=" + token);
		}
	
		EmailNotification emailNotification = emailNotificactionService.findByToken(token);
		if (emailNotification != null) {
		    fillModel(emailNotification.getUser(), emailNotification.getEvaluationEvent(), model);
	
		    // Set read timestamp
		    if (emailNotification.getRead() == null) {
				emailNotification.setRead(new Date());
				emailNotificactionService.update(emailNotification);
		    }
		}
	
		return "student/schedule";
    }

    private void fillModel(User user, EvaluationEvent evaluationEvent, Map<String, Object> model) {
		model.put("userFullName", user.getFullName());
		model.put("evaluationEventName", evaluationEvent.getName());
	
		List<String[]> rows = new ArrayList<String[]>();
	
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUnique(evaluationEvent, user);
		if (evaluationAssignment != null) {
		    List<Object[]> aObjs = matterTestStudentService.findEvaluationSchedule(evaluationAssignment.getId());
		    if (aObjs != null) {
				String[] row;
				for (Object[] aObj : aObjs) {
				    row = new String[5];
		
				    row[0] = (String) aObj[0];
		
				    StringBuilder sbEvaluationTypes = new StringBuilder();
				    int i = 0;
				    for (EvaluationType evaluationType : evaluationEvent.getEvaluationTypes()) {
						if (i > 0) {
						    sbEvaluationTypes.append(",");
						}
						sbEvaluationTypes.append(evaluationType.getName());
						i++;
				    }
				    
				    row[1] = sbEvaluationTypes.toString();
		
				    Date startDate = (Date) aObj[1];
				    String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
				    String strTime = startDate != null ? TIME_SDF.format(startDate) : "";
				    row[2] = strDate;
				    row[3] = strTime;
		
				    StringBuilder sbLocation = new StringBuilder();
		
				    sbLocation.append("Aula ");
				    sbLocation.append(aObj[2]);
				    sbLocation.append(", ");
				    sbLocation.append(aObj[3]);
				    sbLocation.append(" - ");
				    sbLocation.append("Centro ");
				    sbLocation.append(aObj[4]);
				    sbLocation.append(" | ");
		
				    sbLocation.append(aObj[5] != null ? aObj[5] : "");
				    sbLocation.append(" ");
				    sbLocation.append(aObj[6] != null ? aObj[6] : "");
				    sbLocation.append(", ");
				    sbLocation.append(aObj[7] != null ? aObj[7] : "");
				    sbLocation.append(" | ");
				    sbLocation.append(aObj[8] != null ? ((String) aObj[8]).toUpperCase() : "");
		
				    row[4] = sbLocation.toString();
		
				    rows.add(row);
				}
		    }
		}
		model.put("rows", rows);
    }

}
