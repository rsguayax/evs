package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Department;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.DepartmentService;

@Repository
@Transactional(readOnly = true)
public class DepartmentServiceImpl extends BaseServiceImpl<Department> implements DepartmentService {
	public Department findByName(String name) {
		TypedQuery<Department> query = this.em.createQuery("select r from " + Department.class.getSimpleName() + 
				" r where r.name='" + name + "'", Department.class);
		
		List<Department> departments = query.getResultList();
		Department department = null;
		if(departments != null && departments.size() > 0) {
			department = departments.get(0);
		}
		
		
		return department;
	}
}
