package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterId;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventMatterInfo;


public interface EvaluationEventMatterService extends BaseService<EvaluationEventMatter> {
	public void delete(EvaluationEventMatter entity);
	public EvaluationEventMatter findById(EvaluationEventMatterId id);
	public void update(EvaluationEventMatter entity);
	public void save(EvaluationEventMatter entity);
	public void deleteAll(Long evaluationEventId, EvaluationEventMatter evaluationEventMatter);
	public EvaluationEventMatter findByUnique(Long evaluationEventId, Long matterId, Long academicPeriodId, Long modeId);
	public List<EvaluationEventMatter> findByParams(Long evaluationEventId, Long academicPeriodId, Long modeId);
	public List<Long> getEvaluationTypes(Long id);
	public List<EvaluationEventMatter> findByEvaluationEvent(Long evaluationEventId);
	public List<EvaluationEventMatterInfo> findInfoByEvaluationEvent(Long evaluationEventId);
	public List<EvaluationEventMatterInfo> findInfoByAdmissionEvaluationEvent(Long evaluationEventId);
	public EvaluationEventMatter findByEvaluationEventAndMatter(Long evaluationEventId, Long matterId);
	public List<EvaluationEventMatter> findByEvaluationEventWithBank(Long evaluationEventId);
	public Long countByEvaluationEventWithBank(Long evaluationEventId);
	public List<WeekDay> getDaysAllowed(Long evaluationEventMatterId);
	public boolean hasTests(Long evaluationEventMatterId);
	public List<EvaluationEventMatter> getEvalMattersByEvaluationEventAndBankDepartment(Long evaluationEventId, Long departmentId);
}
