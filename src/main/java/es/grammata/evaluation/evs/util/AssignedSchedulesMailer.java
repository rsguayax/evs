package es.grammata.evaluation.evs.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import es.grammata.evaluation.evs.data.model.repository.EmailNotification;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EmailNotificationService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;

/**
 * @author ajmedialdea
 *
 */
public class AssignedSchedulesMailer {

    private static final Logger LOGGER = Logger.getLogger(AssignedSchedulesMailer.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private EvaluationAssignmentService evaluationAssignmentService;

    @Autowired
    private EmailNotificationService emailNotificactionService;

    private String from;

    private int test;
    
    private String testTo;

    private String callbackBaseUrl;

    public void setFrom(String from) {
    	this.from = from;
    }

    public void setTest(int test) {
    	this.test = test;
    } 
    
    public void setTestTo(String testTo) {
    	this.testTo = testTo;
    }

    public void setCallbackBaseUrl(String callbackBaseUrl) {
    	this.callbackBaseUrl = callbackBaseUrl;
    }

    public void send(List<User> users) {
		MyMimeMessagePreparator preparator = new MyMimeMessagePreparator();
		preparator.setVelocityEngine(velocityEngine);
		preparator.setTemplateLocation("templates/assigned-schedules-notification.vm");
		preparator.setFrom(from);
		for (User user : users) {
		    if (user.getEmail() != null) {
				preparator.setTo(test == 1 ? testTo : user.getEmail());
				List<EvaluationAssignment> evaluationAssignments = evaluationAssignmentService.findByUsername(user.getUsername());
				if (evaluationAssignments != null) {
				    for (EvaluationAssignment evaluationAssignment : evaluationAssignments) {
						EvaluationEvent evaluationEvent = evaluationAssignment.getEvaluationEvent();
						if (evaluationEvent != null) {
						    preparator.setSubject("UTPL - Notificaci\u00f3n de los horarios de las "
							    + "evaluaciones presenciales correspondientes a '"
							    + evaluationEvent.getName() + "'");
			
						    Map<String, Object> model = new HashMap<String, Object>();
						    model.put("userFullName", user.getFullName());
						    model.put("evaluationEventName", evaluationEvent.getName());
						    String token = UUID.randomUUID().toString();
						    model.put("callbackUrl", callbackBaseUrl + token);
						    preparator.setModel(model);
			
						    try {
								mailSender.send(preparator);
				
								EmailNotification emailNotification = new EmailNotification();
								emailNotification.setUser(user);
								emailNotification.setEvaluationEvent(evaluationEvent);
								emailNotification.setToken(token);
								emailNotification.setSent(new Date());
								emailNotificactionService.save(emailNotification);		
						    } catch (MailException e) {
						    	LOGGER.error("", e);
						    }
						}
				    }
				}
		    }
		}
    }

}
