package es.grammata.evaluation.evs.data.services.base;

import java.io.Serializable;
import java.util.List;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


public interface BaseService<T extends BaseModelEntity<?>> {

	public T findById(Serializable id);
	
	public List<T> findAll();

	public List<T> findAll(int page, int pageSize);

	public void save(T entity);
	
	public void update(T entity);
	
	public void delete(final Object id);
	
	public List<T> findAllOrder(String orderBy);

}
