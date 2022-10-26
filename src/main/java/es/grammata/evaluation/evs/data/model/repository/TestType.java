package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "TEST_TYPE")
public class TestType extends BaseModelEntity<Long> {

	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	private String formula;

	@NotNull(message="El campo no puede estar vacío")
	private Float maxRate; 
	
	@NotNull(message="El campo no puede estar vacío")
	private Integer maxNumQuestion; 
	
	
	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	public Float getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(Float maxRate) {
		this.maxRate = maxRate;
	}
	
	public Integer getMaxNumQuestion() {
		return maxNumQuestion;
	}

	public void setMaxNumQuestion(Integer maxNumQuestion) {
		this.maxNumQuestion = maxNumQuestion;
	}

	@JsonIgnore
	public String getExpression(int A, int E, int B) {
		String expression = null;
		
		
		if(this.getFormula() != null) {
			expression = this.getFormula().replace("A", String.valueOf(A)).replace("E", String.valueOf(E)).
					replace("B", String.valueOf(B)).replace("NP", String.valueOf(A+B+E)).
					replace("NM", String.valueOf(this.getMaxRate()));		
		}
		
		return expression;
	}
	
	@JsonIgnore
	public Float getExpressionResult(String expression) {
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    Float result = null;
		try {
			result = ((Double)engine.eval(expression)).floatValue();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		if(result.isNaN()) {
			result = 0F;
		}
		
		return result;
	}
}
