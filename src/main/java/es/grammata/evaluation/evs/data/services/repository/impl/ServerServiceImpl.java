package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ServerService;

@Repository
@Transactional(readOnly = true)
public class ServerServiceImpl extends BaseServiceImpl<Server> implements
		ServerService {
	  
	/*public List<Server> findByNet(Long netId) {
  		TypedQuery<Server> query = this.em.createQuery("select s from " + Server.class.getSimpleName() +
  			" s, " + Net.class.getSimpleName() + " n where " +
  					"  n.id=s.net.id AND n.id=:netId", Server.class);
  	
  		query.setParameter("netId", netId);
  		
  		List<Server> servers = query.getResultList();
  		
  		return servers;
    }*/
	
	/*public List<Server> findUnassigned() {
		String query = "select s from " + Server.class.getSimpleName() + 
				" s  where s.net is null";
		
		query += " order by name asc";
	
		TypedQuery<Server> typedQuery = this.em.createQuery(query, Server.class);
		
		List<Server> servers = typedQuery.getResultList();

		return servers;
	}*/
}
