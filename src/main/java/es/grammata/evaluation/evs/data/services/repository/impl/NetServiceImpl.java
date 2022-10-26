package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.NetService;
import es.grammata.evaluation.evs.data.services.repository.ServerService;


@Repository
@Transactional(readOnly = true)
public class NetServiceImpl extends BaseServiceImpl<Net> implements NetService {
	
	@Autowired
	private ServerService serverService;
	
	
	public Net findByCode(String code) {
		TypedQuery<Net> query = this.em.createQuery("select r from " + Net.class.getSimpleName() + 
				" r where r.code='" + code + "'", Net.class);
		
		List<Net> nets = query.getResultList();
		Net net = null;
		if(nets != null && nets.size() > 0) {
			net = nets.get(0);
		}
		
		return net;
	}
	
	public boolean existsNet(String name, String code, Long evaluationCenterId) {
		String query = "select count(*) from net " +
				"where evaluationcenter_id = " + evaluationCenterId 
				+ " and name = '" + name  + "' or code = '" + code  + "'"; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
	
	public boolean existsNetUpdate(String name, String code, Long evaluationCenterId, Long currentId) {
		String query = "select count(*) from net " +
				"where evaluationcenter_id = " + evaluationCenterId + 
				" and (name = '" + name  + "' or code = '" + code  + "') and id <> " + currentId; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
	
	@Transactional
	public void updateNet(Net net, List<Server> servers) {
		/*List<Server> netServers = serverService.findByNet(net.getId());
		if(servers == null) {
			servers = new ArrayList<Server>();
		}
		if(netServers != null) {
			for(Server netServer : netServers) {
				boolean exists = false;
				for(Server server : servers) {
					if(server.getId().equals(netServer.getId())) {
						exists = true;
					}
				}
				if(!exists) {
					netServer.setNet(null);
					serverService.update(netServer);
				}
			}
		}
		
		for(Server server : servers) {
			server.setNet(net);
			serverService.update(server);
		}
		
		this.update(net);*/
		
		Set<Server> serversSet = new HashSet<Server>(servers);
		net.setServers(serversSet);
		this.update(net);
	}
	
	@Transactional
	public void saveNet(Net net, List<Server> servers) {
		this.save(net);
		
		/*if(servers != null) {
			for(Server server : servers) {
				server.setNet(net);
				serverService.update(server);
			}
		}*/
		
		if(servers != null) {
			Set<Server> serversSet = new HashSet<Server>(servers);
			net.setServers(serversSet);
			this.update(net);
		}
	}
	
	@Transactional
	public void deleteNet(Long id) {
		/*List<Server> netServers = serverService.findByNet(id);
		
		if(netServers != null) {
			for(Server server : netServers) {
				server.setNet(null);
				serverService.update(server);
			}
		}
		
		this.delete(id);*/
		
		this.delete(id);
	}
	
	
}
