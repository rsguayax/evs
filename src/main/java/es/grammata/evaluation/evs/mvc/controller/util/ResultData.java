package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

public class ResultData<T> {
	
	private long total;
	private int error = 0;
	private List<T> data;
	
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	
}
