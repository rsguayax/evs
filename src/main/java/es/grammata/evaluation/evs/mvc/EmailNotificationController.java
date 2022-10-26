package es.grammata.evaluation.evs.mvc;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EmailNotification;
import es.grammata.evaluation.evs.data.services.repository.EmailNotificationService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class EmailNotificationController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationController.class);

    @Autowired
    private EmailNotificationService emailNotificactionService;

    @RequestMapping("/email-notification-list")
    public String view(@RequestParam(value = "evaluationEventId", required = false, defaultValue = "-1") Long evaluationEventId,
	    @RequestParam(value = "userId", required = false, defaultValue = "-1") Long userId,
	    Map<String, Object> model) {
		model.put("headText", "Notificaciones por e-mail de los horarios de evaluaci\u00f3n");
		model.put("evaluationEventId", evaluationEventId);
		model.put("userId", userId);
		return "student/email-notification-list";
    }

    @RequestMapping(value = "/email-notification/email-notifications",
	    method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<EmailNotification> loadEmailNotifications(@RequestParam(value = "evaluationEventId", required = false, defaultValue = "-1") Long evaluationEventId,
	    @RequestParam(value = "userId", required = false, defaultValue = "-1") Long userId) {
    	return emailNotificactionService.findByEvaluationEventAndUser(evaluationEventId, userId);
    }

}
