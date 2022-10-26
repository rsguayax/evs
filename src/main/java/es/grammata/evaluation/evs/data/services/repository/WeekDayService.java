package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface WeekDayService extends BaseService<WeekDay> {
	public WeekDay findByCode(String code);
}
