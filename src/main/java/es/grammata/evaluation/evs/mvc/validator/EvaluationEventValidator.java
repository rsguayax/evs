package es.grammata.evaluation.evs.mvc.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;

@Component
public class EvaluationEventValidator implements Validator {
	
	@Autowired
	EvaluationEventService evaluationEventService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EvaluationEvent.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EvaluationEvent evaluationEvent = (EvaluationEvent) target;
		
		if (!evaluationEvent.getCode().isEmpty()) {
			EvaluationEvent evTemp = evaluationEventService.findByCode(evaluationEvent.getCode());
			if (evTemp != null && !evTemp.getId().equals(evaluationEvent.getId())) {
				errors.rejectValue("code", null, "Ya existe un evento de evaluación con ese código");
			}
		}
		
		if (evaluationEvent.getStartDate() != null && evaluationEvent.getEndDate() != null) { 
			if (evaluationEvent.getStartDate().after(evaluationEvent.getEndDate()) || evaluationEvent.getStartDate().equals(evaluationEvent.getEndDate())) {
				errors.rejectValue("startDate", null, "La fecha de inicio debe ser menor que la fecha de fin");
			}
		}
	}
}
