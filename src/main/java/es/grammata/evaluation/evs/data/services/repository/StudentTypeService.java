package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface StudentTypeService extends BaseService<StudentType> {
	public StudentType findByValue(String value);
}
