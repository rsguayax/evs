package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.ScheduleModificationDate;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationDateService;

@Repository
@Transactional(readOnly = true)
public class ScheduleModificationDateServiceImpl extends BaseServiceImpl<ScheduleModificationDate> implements ScheduleModificationDateService {

}
