package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.DocFile;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.DocFileService;

@Repository
@Transactional(readOnly = true)
public class DocFileServiceImpl extends BaseServiceImpl<DocFile> implements
		DocFileService {

	public List<DocFile> findByCenterCode(String evaluationEventCode, String evaluationCenterCode) {
		TypedQuery<DocFile> query = this.em.createQuery("select df from "
				+ DocFile.class.getSimpleName() + " df where df.evaluationCenterCode=:evaluationCenterCode " +
						" AND df.evaluationEventCode=:evaluationEventCode " +
						" order by fileName ASC", 
				DocFile.class);

		query.setParameter("evaluationCenterCode", evaluationCenterCode);
		query.setParameter("evaluationEventCode", evaluationEventCode);

		List<DocFile> docFiles = query.getResultList();

		return docFiles;
	}
	
	
	public List<DocFile> findByFileName(String evaluationEventCode, String evaluationCenterCode, String fileName) {
		
		TypedQuery<DocFile> query = this.em.createQuery("select df from "
				+ DocFile.class.getSimpleName() + " df where df.evaluationCenterCode=:evaluationCenterCode " +
						" AND df.evaluationEventCode=:evaluationEventCode AND df.fileName = :fileName",
				DocFile.class);

		query.setParameter("evaluationCenterCode", evaluationCenterCode);
		query.setParameter("evaluationEventCode", evaluationEventCode);
		query.setParameter("fileName", fileName);

		List<DocFile> docFiles = query.getResultList();

		return docFiles;
	}
	
	
	public List<DocFile> findByParams(String evaluationEventCode, String evaluationCenterCode, String classroomCode, Date startDateTest, 
			Date endDateTest) {
		
		String querySql = "select df from "
				+ DocFile.class.getSimpleName() + " df where " +
				" df.evaluationEventCode=:evaluationEventCode";
		
		if(evaluationCenterCode != null) {
			 querySql += " AND df.evaluationCenterCode='" + evaluationCenterCode + "'";
		}
		if(classroomCode != null) {
			 querySql += " AND df.classroomCode='" + classroomCode + "'";
		} else {
			 querySql += " AND df.classroomCode IS NULL";
		}
	    if(startDateTest != null && endDateTest != null) {
		     querySql += " AND df.startDateTest='" + startDateTest + "' AND df.endDateTest='" + endDateTest + "'";
	    } else {
	    	 querySql += " AND df.startDateTest=NULL AND df.endDateTest=NULL";
	    }
		
		TypedQuery<DocFile> query = this.em.createQuery(querySql,
				DocFile.class);

		query.setParameter("evaluationEventCode", evaluationEventCode);


		List<DocFile> docFiles = query.getResultList();

		return docFiles;
	}
	
	
	public DocFile findEventFile(String evaluationEventCode) {
		String querySql = "select df from "
				+ DocFile.class.getSimpleName() + " df where " +
				" df.evaluationEventCode=:evaluationEventCode";
		
		querySql += " AND df.evaluationCenterCode IS NULL";
		
		TypedQuery<DocFile> query = this.em.createQuery(querySql,
				DocFile.class);

		query.setParameter("evaluationEventCode", evaluationEventCode);

		List<DocFile> docFiles = query.getResultList();
		DocFile docFile = null;
		if(docFiles != null && docFiles.size() > 0) {
			docFile = docFiles.get(0);
		}

		return docFile;
	}
}
