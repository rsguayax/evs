package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventTeacher;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTeacherService;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventTeacherServiceImpl extends BaseServiceImpl<EvaluationEventTeacher> implements EvaluationEventTeacherService {

    @Override
    public List<EvaluationEventTeacher> findByUsername(String username) {
	TypedQuery<EvaluationEventTeacher> query = this.em.createQuery("select eet from "
		+ EvaluationEventTeacher.class.getSimpleName() +
		" eet where eet.teacher.username = :username", EvaluationEventTeacher.class);

	query.setParameter("username", username);

	List<EvaluationEventTeacher> evaluationEventTeachers = query.getResultList();

	return evaluationEventTeachers;
    }

}
