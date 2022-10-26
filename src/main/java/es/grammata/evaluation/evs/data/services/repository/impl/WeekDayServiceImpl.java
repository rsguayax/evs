package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.WeekDayService;

@Repository
@Transactional(readOnly = true)
public class WeekDayServiceImpl extends BaseServiceImpl<WeekDay> implements WeekDayService {

	@Override
	public WeekDay findByCode(String code) {
		TypedQuery<WeekDay> query = this.em.createQuery(
				"SELECT wd FROM " + WeekDay.class.getSimpleName() + " wd WHERE wd.code=:code", WeekDay.class);
		
		query.setParameter("code", code);
		List<WeekDay> weekDays = query.getResultList();
		
		if (!weekDays.isEmpty()) {
			return weekDays.get(0);
		} else {
			return null;
		}
	}
}
