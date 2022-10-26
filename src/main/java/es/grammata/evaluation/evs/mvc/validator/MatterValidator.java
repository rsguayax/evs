package es.grammata.evaluation.evs.mvc.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.Matter;

@Component
public class MatterValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Matter.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Matter matter = (Matter) target;
		CorrectionRule correctionRule = matter.getCorrectionRule();
		
		if (correctionRule != null && (correctionRule.getMinGrade() != null || correctionRule.getMaxGrade() != null || !Strings.isNullOrEmpty(correctionRule.getType()))) {
			if (correctionRule.getMinGrade() == null) {
				errors.rejectValue("correctionRule.minGrade", null, "Campo obligatorio");
			} else if (correctionRule.getMinGrade() < 0) {
				errors.rejectValue("correctionRule.minGrade", null, "El valor debe ser superior o igual a 0");
			}
			
			if (correctionRule.getMaxGrade() == null) {
				errors.rejectValue("correctionRule.maxGrade", null, "Campo obligatorio");
			} else if (correctionRule.getMinGrade() != null && correctionRule.getMaxGrade() < correctionRule.getMinGrade()) {
				errors.rejectValue("correctionRule.maxGrade", null, "El valor debe ser superior o igual al grado mínimo");
			}
			
			if (Strings.isNullOrEmpty(correctionRule.getType())) {
				errors.rejectValue("correctionRule.type", null, "Campo obligatorio");
			}
		}
	}
}
