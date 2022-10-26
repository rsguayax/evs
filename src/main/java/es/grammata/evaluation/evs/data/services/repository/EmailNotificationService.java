package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EmailNotification;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface EmailNotificationService extends
	BaseService<EmailNotification> {

    EmailNotification findByToken(String token);

    List<EmailNotification> findByEvaluationEventAndUser(Long evaluationEventId, Long userId);
}
