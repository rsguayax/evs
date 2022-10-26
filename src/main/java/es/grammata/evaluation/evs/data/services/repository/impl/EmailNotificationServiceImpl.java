package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EmailNotification;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EmailNotificationService;

@Repository
@Transactional(readOnly = true)
public class EmailNotificationServiceImpl extends
	BaseServiceImpl<EmailNotification> implements EmailNotificationService {

    @Override
    public EmailNotification findByToken(String token) {
	TypedQuery<EmailNotification> query = this.em.createQuery(
		"select e from " + EmailNotification.class.getSimpleName()
			+ " e where e.token='" + token + "'",
		EmailNotification.class);

	List<EmailNotification> emailNotifications = query.getResultList();
	EmailNotification emailNotification = null;
	if (emailNotifications != null && emailNotifications.size() > 0) {
	    emailNotification = emailNotifications.get(0);
	}
	return emailNotification;
    }

    @Override
    public List<EmailNotification> findByEvaluationEventAndUser(Long evaluationEventId, Long userId) {
	String strQuery = "select en from " + EmailNotification.class.getSimpleName()
		+ " en where en.evaluationEvent.id=" + evaluationEventId + " and en.user.id=" + userId;
	if ((evaluationEventId == null || evaluationEventId < 0L) && (userId == null || userId < 0L)) {
	    strQuery = "select en from " + EmailNotification.class.getSimpleName() + " en";
	} else if ((evaluationEventId == null || evaluationEventId < 0L) && userId != null && userId >= 0L) {
	    strQuery = "select en from " + EmailNotification.class.getSimpleName()
		    + " en where en.user.id=" + userId;
	} else if ((userId == null || userId < 0L) && evaluationEventId != null && evaluationEventId >= 0L) {
	    strQuery = "select en from " + EmailNotification.class.getSimpleName()
		    + " en where en.evaluationEvent.id=" + evaluationEventId;
	}
	TypedQuery<EmailNotification> query = this.em.createQuery(strQuery, EmailNotification.class);
	List<EmailNotification> emailNotifications = query.getResultList();
	return emailNotifications;
    }

}
