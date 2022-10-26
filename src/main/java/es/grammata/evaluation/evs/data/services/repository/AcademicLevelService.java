package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface AcademicLevelService extends BaseService<AcademicLevel> {
	public AcademicLevel findByCode(String code);
	public List<AcademicLevel> findAll();
	public List<AcademicLevel> findNotDeleted();
	public void deleteAll();
}
