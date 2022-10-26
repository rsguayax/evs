package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.TimeBlock;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.TimeBlockService;

@Repository
@Transactional(readOnly = true)
public class TimeBlockServiceImpl extends BaseServiceImpl<TimeBlock> implements TimeBlockService {
	public List<TimeBlock> findRelatedByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<TimeBlock> query = this.em.createQuery("select tm from " + TimeBlock.class.getSimpleName() +
				" tm where EXISTS (SELECT cr FROM " + ClassroomTimeBlock.class.getSimpleName() + " cr where " +
				" cr.timeBlock = tm " +
				"and cr.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id = " + evaluationEventId + ")"
				, TimeBlock.class);

		List<TimeBlock> matterTestStudents = query.getResultList();


		return matterTestStudents;
	}
}
