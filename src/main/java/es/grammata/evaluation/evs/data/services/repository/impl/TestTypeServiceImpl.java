package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.TestType;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.TestTypeService;

@Repository
@Transactional(readOnly = true)
public class TestTypeServiceImpl extends BaseServiceImpl<TestType> implements TestTypeService {
}
