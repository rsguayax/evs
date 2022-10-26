package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;

public class ExtendedNet {	
	private Net net ;
	private List<Server> servers;
	
	public Net getNet() {
		return net;
	}
	public void setNet(Net net) {
		this.net = net;
	}
	public List<Server> getServers() {
		return servers;
	}
	public void setServers(List<Server> servers) {
		this.servers = servers;
	}
	
	
}
