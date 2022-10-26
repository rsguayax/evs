package es.grammata.evaluation.evs.util;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import es.grammata.evaluation.evs.mvc.controller.util.Report;
import es.grammata.evaluation.evs.mvc.controller.util.Report.Cell;

public class PdfRevenueReportView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map model, Document doc,
			PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		Report report = (Report)model.get("report");
		
		// Document header and footer
		Phrase phDocHeader = new Phrase(new Chunk("UTPL - " + report.getTitle(),
			FontFactory.getFont(FontFactory.HELVETICA, 10.0f)));

		HeaderFooter docHeader = new HeaderFooter(phDocHeader, false);
		docHeader.setAlignment(Element.ALIGN_CENTER);
		doc.setHeader(docHeader);

		HeaderFooter docFooter = new HeaderFooter(new Phrase(new Chunk("P\u00e1gina ",
				FontFactory.getFont(FontFactory.HELVETICA, 10.0f))), null);
		docFooter.setAlignment(Element.ALIGN_RIGHT);
		doc.setFooter(docFooter);

		// Header
		Table htable = new Table(5);
		htable.setBorder(Table.NO_BORDER);
		htable.setPadding(3);

		// Logo
		Image logo = Image.getInstance(getServletContext().getRealPath("/web-resources/img/v-logo.png"));
		logo.scalePercent(10.0f);

		com.lowagie.text.Cell hcell = new com.lowagie.text.Cell(logo);
		hcell.setBorder(com.lowagie.text.Cell.NO_BORDER);
		hcell.setHeader(true);
		hcell.setBackgroundColor(new Color(0, 66, 113));
		hcell.setBorderWidthBottom(2);
		hcell.setBorderColorBottom(new Color(234, 171, 0));
		hcell.setBorderWidthBottom(2);
		hcell.setColspan(1);
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);

		hcell = new com.lowagie.text.Cell(new Chunk("UNIVERSIDAD T\u00c9CNICA PARTICULAR DE LOJA",
			FontFactory.getFont(FontFactory.HELVETICA, 12.0f, Font.BOLD, new Color(255, 255, 255))));
		hcell.setBorder(com.lowagie.text.Cell.NO_BORDER);
		hcell.setBackgroundColor(new Color(0, 66, 113));
		hcell.setBorderWidthBottom(2);
		hcell.setBorderColorBottom(new Color(234, 171, 0));
		hcell.setHeader(true);
		hcell.setColspan(4);
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);


		htable.endHeaders();
		

		hcell = new com.lowagie.text.Cell(new Chunk(report.getTitle(), FontFactory.getFont(FontFactory.HELVETICA, 13.0f, Font.BOLD)));
		hcell.setBorder(com.lowagie.text.Cell.NO_BORDER);
		hcell.setColspan(5);
		hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		htable.addCell(hcell);
			
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		hcell = new com.lowagie.text.Cell(new Chunk(sdf.format(date), FontFactory.getFont(FontFactory.HELVETICA, 10.0f, Font.BOLD)));
		hcell.setBorder(com.lowagie.text.Cell.NO_BORDER);
		hcell.setColspan(5);
		hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		htable.addCell(hcell);

		doc.add(htable);
		
		
		Object[] theaders = report.getHeaders().toArray();

		Table table = new Table(theaders.length);
		table.setTableFitsPage(true);
		table.setPadding(3.0f);
		table.setBorder(0);
		Color backgroundColor = Report.HEADER_COLOR;
		for (int i = 0; i < theaders.length; i++) {
		    Object theader = theaders[i];
		    com.lowagie.text.Cell cell = new com.lowagie.text.Cell(new Phrase(theader != null ? theader.toString() : "",
			    FontFactory.getFont(FontFactory.HELVETICA, 8.0f, Font.BOLD)));
		    cell.setBorderWidth(1.0f);
		    cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(backgroundColor);
		    table.addCell(cell);
		}
		
		table.endHeaders();

		for (Cell cellAux : report.getCells()) {
			for(String col : cellAux.getCols()) {
				com.lowagie.text.Cell cell = new com.lowagie.text.Cell(new Phrase(col != null ? col.toString() : "",
						FontFactory.getFont(FontFactory.HELVETICA, 8.0f)));
					cell.setBorderWidth(1.0f);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					if(cellAux.getColor() != null) {
						cell.setBackgroundColor(cellAux.getColor());						
					}
					
					table.addCell(cell);
			}
		}
		
		if(report.getColumnWidths() != null) {
			table.setWidths(report.getColumnWidths());
		}

		doc.add(table);

	}
	
}