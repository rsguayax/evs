package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Log;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.LogService;

@Repository
@Transactional(readOnly = true)
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

}
