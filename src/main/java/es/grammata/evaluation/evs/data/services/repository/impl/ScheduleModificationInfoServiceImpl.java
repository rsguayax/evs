package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.ScheduleModificationInfo;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationInfoService;

@Repository
@Transactional(readOnly = true)
public class ScheduleModificationInfoServiceImpl extends BaseServiceImpl<ScheduleModificationInfo> implements ScheduleModificationInfoService {

}
