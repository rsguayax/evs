package es.grammata.evaluation.evs.mvc.controller.util;

import java.awt.Color;
import java.util.List;

public class Report {
	public static Color HIGHLIGHT_COLOR;
	public static Color HEADER_COLOR;
	
	private String title;
	private List<String> headers;
	private List<Cell> cells;
	private float[] columnWidths;
	
	static {
		HIGHLIGHT_COLOR = new Color(242, 222, 222);
		HEADER_COLOR = new Color(200, 200, 200);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	
	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	
	public float[] getColumnWidths() {
		return columnWidths;
	}

	public void setColumnWidths(float[] columnWidths) {
		this.columnWidths = columnWidths;
	}




	public class Cell {
		List<String> cols;
		Color color;
		
		public Cell() {}

		public List<String> getCols() {
			return cols;
		}

		public void setCols(List<String> cols) {
			this.cols = cols;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}
		
	}
	
}
