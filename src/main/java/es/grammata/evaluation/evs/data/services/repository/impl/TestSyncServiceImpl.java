package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.TestSync;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.TestSyncService;


@Repository
@Transactional(readOnly = true)
public class TestSyncServiceImpl extends BaseServiceImpl<TestSync> implements TestSyncService {
}
