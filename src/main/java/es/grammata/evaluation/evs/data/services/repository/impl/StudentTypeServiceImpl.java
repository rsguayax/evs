package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.StudentTypeService;

@Repository
@Transactional(readOnly = true)
public class StudentTypeServiceImpl extends BaseServiceImpl<StudentType> implements StudentTypeService {
	public StudentType findByValue(String value) {
		TypedQuery<StudentType> query = this.em.createQuery("SELECT st FROM " + StudentType.class.getSimpleName() +
				" st WHERE st.value='" + value+ "'", StudentType.class);

		List<StudentType> stps = query.getResultList();
		StudentType stp = null;
		if(stps != null && stps.size() > 0) {
			stp = stps.get(0);
		}

		return stp;
	}

}
