package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Session;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.SessionService;


@Repository
@Transactional(readOnly = true)
public class SessionServiceImpl extends BaseServiceImpl<Session> implements SessionService {
}
