package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventTeacher;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface EvaluationEventTeacherService extends BaseService<EvaluationEventTeacher> {
    List<EvaluationEventTeacher> findByUsername(String username);
}
