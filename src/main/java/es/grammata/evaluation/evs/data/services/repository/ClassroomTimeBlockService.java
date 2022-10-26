package es.grammata.evaluation.evs.data.services.repository;

import java.util.Date;
import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo2;

public interface ClassroomTimeBlockService extends BaseService<ClassroomTimeBlock> {
	public List<ClassroomTimeBlock> findByEventCenter(Long eventCenterId);
	public List<ClassroomTimeBlockInfo> findInfoByEventCenter(Long eventCenterId);
	public List<ClassroomTimeBlock> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId);
	public List<ClassroomTimeBlock> findByTimeBlock(Long timeBlockId, Long evaluatonEventId);
	public List<ClassroomTimeBlock> findByEvaluationEvent(Long evaluationEventId);
	public List<ClassroomTimeBlock> findByDates(Date startDate, Date endDate, Long evaluationEventId);
	public ClassroomTimeBlock findByClassroomTimeBlock(Long evaluationEventId, String centerCode, String classroomCode, Date startDate, Date EndDate);
	public ClassroomTimeBlockInfo2 findInfoById(Long id);
}
