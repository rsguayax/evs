package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface NetService extends BaseService<Net> {
	public Net findByCode(String code);
	public boolean existsNet(String name, String code, Long evaluationCenterId);
	public boolean existsNetUpdate(String name, String code,  Long evaluationCenterId, Long currentId);
	public void updateNet(Net net, List<Server> servers);
	public void saveNet(Net net, List<Server> servers);
	public void deleteNet(Long id);
}
