package es.grammata.evaluation.evs.data.services.repository;

import java.util.Date;
import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.DocFile;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface DocFileService extends BaseService<DocFile> {
	public List<DocFile> findByCenterCode(String evaluationEventCode, String evaluationCenterCode);
	public List<DocFile> findByFileName(String evaluationEventCode, String evaluationCenterCode, String fileName);
	public List<DocFile> findByParams(String evaluationEventCode, String evaluationCenterCode, String classroomCode, Date startDateTest, 
			Date endDateTest);
	public DocFile findEventFile(String evaluationEventCode);
}
