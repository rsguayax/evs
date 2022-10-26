package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ClassroomService;


@Repository
@Transactional(readOnly = true)
public class ClassroomServiceImpl extends BaseServiceImpl<Classroom> implements ClassroomService {
	public boolean existsClassroom(String name, Long evaluationCenterId) {
		String query = "select count(*) from classroom where evaluationcenter_id = " + evaluationCenterId + " and name = '" + name  + "'"; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
	
	
	public boolean existsClassroomUpdate(String name, Long evaluationCenterId, Long currentId) {
		String query = "select count(*) from classroom where evaluationcenter_id = " + evaluationCenterId + " and name = '" + name  + "' and id <> " + currentId; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
	
	// comprueba que el aula tiene asignada una red y si esa red tiene el servidor dado
	public boolean existsNetAndServerClassroom(String name, Long evaluationCenterId) {
		String query = "select count(*) from classroom where evaluationcenter_id = " + evaluationCenterId + " and name = '" + name  + "'"; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}

	@Override
	public List<Classroom> findByEvaluationCenter(Long evaluationCenterId) {
		TypedQuery<Classroom> query = this.em.createQuery("SELECT c FROM " + Classroom.class.getSimpleName() +
				" c WHERE c.evaluationCenter.id=:evaluationCenterId ORDER BY c.id", Classroom.class);

		query.setParameter("evaluationCenterId", evaluationCenterId);
		List<Classroom> classrooms = query.getResultList();

		return classrooms;
	}
}
