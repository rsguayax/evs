package es.grammata.evaluation.evs.util;

import java.util.ArrayList;
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
import es.grammata.evaluation.evs.data.services.repository.UserService;

public class StudentResultsMailer {

	private static final Logger LOGGER = Logger
			.getLogger(StudentResultsMailer.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private UserService userService;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private EmailNotificationService emailNotificactionService;

	private String from;
	
	private String to; 

    private int test;
	    
    private String testTo;
	
	
    public void setTest(int test) {
		this.test = test;
	}
	
    public void setTestTo(String testTo) {
  		this.testTo = testTo;
  	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public void send(HashMap<String, HashMap<String, String>> tests) {
		MyMimeMessagePreparator preparator = new MyMimeMessagePreparator();
		preparator.setVelocityEngine(velocityEngine);
		preparator
				.setTemplateLocation("templates/student-results-notification.vm");
		preparator.setFrom(from);
		preparator.setTo(test == 1 ? testTo : to);
		
		Map.Entry<String, HashMap<String, String>> firstTest = tests.entrySet().iterator().next();
		String fullName = firstTest.getValue().get("fullName");
		String evaluationEventName = firstTest.getValue().get("evaluationEventName");
		
		preparator
		.setSubject("UTPL - Notificación de los resultados de las "
				+ "evaluaciones presenciales correspondientes a " + evaluationEventName);

		
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("fullName", StringUtil.replaceToHTML(fullName));
		model.put("elements", tests);
		model.put("evaluationEventName", evaluationEventName);
		preparator.setModel(model);
		
		try {
			mailSender.send(preparator);
		} catch (MailException e) {
			LOGGER.error("Se ha producido un error al enviar correo de notificación de resultados", e);
			throw e;
		}
	}
	

	public void send(String username, String callbackBaseUrl) {
		MyMimeMessagePreparator preparator = new MyMimeMessagePreparator();
		preparator.setVelocityEngine(velocityEngine);
		preparator
				.setTemplateLocation("templates/student-results-notification.vm");
		preparator.setFrom(from);
		User user = userService.findByUsername(username);
		if (user != null && user.getEmail() != null) {
			preparator.setTo(test == 1 ? testTo : user.getEmail());
			List<EvaluationAssignment> evaluationAssignments = evaluationAssignmentService
					.findByUsername(user.getUsername());
			if (evaluationAssignments != null) {
				for (EvaluationAssignment evaluationAssignment : evaluationAssignments) {
					EvaluationEvent evaluationEvent = evaluationAssignment
							.getEvaluationEvent();
					if (evaluationEvent != null) {
						preparator
								.setSubject("UTPL - Notificación de los horarios de las "
										+ "evaluaciones presenciales correspondientes a '"
										+ evaluationEvent.getName() + "'");

						Map<String, Object> model = new HashMap<String, Object>();
						model.put("userFullName", user.getFullName());
						model.put("evaluationEventName",
								evaluationEvent.getName());
						String token = UUID.randomUUID().toString();
						model.put("callbackUrl", callbackBaseUrl + token);
						preparator.setModel(model);

						try {
							mailSender.send(preparator);

							EmailNotification emailNotification = new EmailNotification();
							emailNotification.setUser(user);
							emailNotification
									.setEvaluationEvent(evaluationEvent);
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
