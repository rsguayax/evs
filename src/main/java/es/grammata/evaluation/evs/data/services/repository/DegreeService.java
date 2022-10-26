package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.services.base.BaseService;

/**
 * 
 * @author andres. Interfaz, hereda los metodos CRUD de BaseService, aumentamos
 *         dos nuevos metodos para realizar un borrado logica y obtener una
 *         lista de los objetos con estado active=true
 * 
 *
 */
public interface DegreeService extends BaseService<Degree> {

	/**
	 * Metodo que permite eliminar una materia cambiando su estado de active a
	 * false.
	 * 
	 * @param entity objeto titulacion
	 */
	public void deletLogic(Degree degree);;

	/**
	 * Recupera una lista de titulaciones, con estado active = true
	 * 
	 * @return
	 */
	public List<Degree> findAllAtive();

	public Degree findByCode(String code);
}
