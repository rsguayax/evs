package es.grammata.evaluation.evs.mvc.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.grammata.evaluation.evs.data.model.repository.Schedule;
import es.grammata.evaluation.evs.data.services.repository.ScheduleService;

@Component
public class ScheduleValidator implements Validator {
	
	@Autowired
	ScheduleService scheduleService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Schedule.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Schedule schedule = (Schedule) target;
		
		if (!schedule.getName().isEmpty()) {
			Schedule schTemp = scheduleService.findByName(schedule.getEvaluationEvent().getId(), schedule.getName());
			if (schTemp != null && !schTemp.getId().equals(schedule.getId())) {
				errors.rejectValue("name", null, "Ya existe un horario con ese nombre");
			}
		}
	}
}
