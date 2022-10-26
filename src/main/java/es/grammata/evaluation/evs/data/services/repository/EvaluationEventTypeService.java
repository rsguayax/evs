package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventType;
import es.grammata.evaluation.evs.data.services.base.BaseService;

/**
 * 
 * @author andres. Interfaz, hereda los metodos CRUD de BaseService, aumentamos
 *         dos nuevos metodos para realizar un borrado logica y obtener una
 *         lista de los objetos con estado active=true
 * 
 *
 */
public interface EvaluationEventTypeService extends BaseService<EvaluationEventType> {
	/**
	 * Recupera una lista de tipos de evento de evaluacion, con estado active = true
	 * 
	 * @return lista de tipos de eventop de evaluaci�n
	 */
	public List<EvaluationEventType> findAllAtive();
	public EvaluationEventType findbyName(String name);
}
