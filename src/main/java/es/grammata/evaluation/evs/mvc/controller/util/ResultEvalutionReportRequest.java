package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;

public class ResultEvalutionReportRequest {
	
	private boolean all;
	private List<EvaluationEventMatter> matters;
	
	public boolean getAll() {
		return all;
	}
	public void setAll(boolean all) {
		this.all = all;
	}
	public List<EvaluationEventMatter> getMatters() {
		return matters;
	}
	public void setMatters(List<EvaluationEventMatter> matters) {
		this.matters = matters;
	}
}
