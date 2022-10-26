package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.Department;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface DepartmentService extends BaseService<Department>  {
	public Department findByName(String name);
}
