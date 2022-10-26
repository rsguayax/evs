package es.grammata.evaluation.evs.data.services.base.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class QueryExecutor {

	public static List<String[]> executeNativeQueryNoCastCheck(String statement, EntityManager em) {
		Query query = em.createNativeQuery(statement);
		return query.getResultList();
	}

}
