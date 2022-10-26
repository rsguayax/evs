package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface AcademicPeriodService extends BaseService<AcademicPeriod> {
	public AcademicPeriod findByCode(String code);
	public AcademicPeriod findByExternalId(String externaId);
	public List<AcademicPeriod> findAll();
	public List<AcademicPeriod> findActive();
	public void updateAllAsNotActive();
}

