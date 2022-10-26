package es.grammata.evaluation.evs.util;

import java.io.StringWriter;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author ajmedialdea
 *
 */
public class MyMimeMessagePreparator implements MimeMessagePreparator {

    private String from;

    private String to;

    private String subject;

    private VelocityEngine velocityEngine;

    private String templateLocation;

    private Map<String, Object> model;

    public void setFrom(String from) {
    	this.from = from;
    }

    public void setTo(String to) {
    	this.to = to;
    }

    public void setSubject(String subject) {
    	this.subject = subject;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }

    public void setTemplateLocation(String templateLocation) {
    	this.templateLocation = templateLocation;
    }

    public void setModel(Map<String, Object> model) {
    	this.model = model;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {
		mimeMessage.addHeader("Disposition-Notification-To", from);
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(generateText(), true);
    }

    private String generateText() {
		StringWriter writer = new StringWriter();
		VelocityEngineUtils.mergeTemplate(velocityEngine,
			templateLocation,
			"UTF-8",
			model,
			writer);
		return writer.toString();
    }

}
