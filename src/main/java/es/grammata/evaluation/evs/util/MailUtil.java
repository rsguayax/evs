package es.grammata.evaluation.evs.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;


public class MailUtil {

	private JavaMailSender mailSender;
	
	public MailUtil(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendSimpleMail(EvaluationEvent evaluationEvent,
			String email, String subject, String text, String from) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(subject);
		message.setText(text);
		message.setTo(email);
		message.setFrom(from);
		mailSender.send(message);
	}

}
