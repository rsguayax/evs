package es.grammata.evaluation.evs.data.services.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.data.services.base.BaseService;


@Repository
@Transactional(readOnly = true)
public class BaseServiceImpl<T extends BaseModelEntity<?>> implements BaseService<T> {

	@PersistenceContext
	protected EntityManager em;
	
	private Class<T> parameterClass = null;


	public T findById(Serializable id) {
		return em.find(getParameterClass(), id);
	}
	
	
	public List<T> findAll() {
		List<T> list = em.createQuery("select c from " + getParameterClass().getSimpleName() + " c order by id asc ", 
				getParameterClass()).getResultList();
		return list;
		
	}

	public List<T> findAll(int page, int pageSize) {

		TypedQuery<T> query = em.createQuery("select c from " + getParameterClass().getSimpleName() + " c", getParameterClass());

		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}
	
	public List<T> findAllOrder(String orderBy) {
		List<T> list = em.createQuery("select c from " + getParameterClass().getSimpleName() + " c order by " + orderBy, 
				getParameterClass()).getResultList();
		return list;
		
	}


	@Transactional
	public void save(T entity) {
		em.persist(entity);
		em.flush();
	}
	
	@Transactional
	public void update(T entity) {
		if (entity.getId() != null) {
			em.merge(entity);
			em.flush();
		} 
	}
	
	@Transactional
	public void delete(final Object id) {
		em.remove(em.getReference(getParameterClass(), id));
		em.flush();
	}
	
	
	@SuppressWarnings("unchecked")
	private Class<T> getParameterClass() {
		if (parameterClass == null) {
			parameterClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}
		
		return parameterClass;
	}
	
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
