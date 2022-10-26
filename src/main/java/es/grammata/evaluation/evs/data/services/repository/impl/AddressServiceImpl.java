package es.grammata.evaluation.evs.data.services.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Address;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.AddressService;


@Repository
@Transactional(readOnly = true)
public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {
}
