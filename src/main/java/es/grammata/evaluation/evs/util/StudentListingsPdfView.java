package es.grammata.evaluation.evs.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import es.grammata.evaluation.evs.mvc.controller.util.StudentListElementReport;


public class StudentListingsPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

    int type = (int) model.get("type");
    String evaluationEventName = (String) model.get("evaluationEventName"); // 1

	 List<StudentListElementReport> elements = (List<StudentListElementReport>) model.get("elements");
	 
	 boolean hasRows = false;
	 
	 for(StudentListElementReport element : elements) {
		 String evaluationCenterName = element.getCenterName();
		 String classroomName = element.getClassRoomName();
		 String strDate = element.getDateStr();
		 String strStartTime = element.getInitDateStr();
		 String strEndTime = element.getEndDateStr();
		 List<Object[]> rows = element.getUsers();
 
		 if(rows != null && rows.size() > 0) {
			 	hasRows = true;

				// Header
				Table htable = new Table(4);
				htable.setBorder(Table.NO_BORDER);
				htable.setPadding(0.6F);

				// Logo
				Image logo = Image.getInstance(getServletContext()
						.getRealPath(
								"/web-resources/img/v-logo.png"));
				logo.scalePercent(9.0f);

				Cell hcell = new Cell(logo);
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHeader(true);
				hcell.setColspan(1);
				hcell.setRowspan(3);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				htable.addCell(hcell);

				hcell = new Cell(
						new Chunk(
								"UNIVERSIDAD T\u00c9CNICA PARTICULAR DE LOJA",
								FontFactory.getFont(
										FontFactory.HELVETICA,
										12.0f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHeader(true);
				hcell.setColspan(3);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				htable.addCell(hcell);

				hcell = new Cell(new Chunk(
						"Modalidad de Estudios a Distancia",
						FontFactory.getFont(FontFactory.HELVETICA,
								10.0f)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHeader(true);
				hcell.setColspan(3);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				htable.addCell(hcell);

				htable.endHeaders();

				// Evaluation event

				hcell = new Cell(new Chunk(evaluationEventName,
						FontFactory.getFont(FontFactory.HELVETICA,
								9.0f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(3);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				htable.addCell(hcell);

				// Evaluation center and classroom
				hcell = new Cell();
				hcell.setBorder(Cell.NO_BORDER);
				htable.addCell(hcell); 
				
				
				
				hcell = new Cell(new Chunk(evaluationCenterName,
						FontFactory.getFont(FontFactory.HELVETICA,
								9.0f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(2);
				htable.addCell(hcell); 
				

				hcell = new Cell(new Chunk(
						"Aula: " + classroomName,
						FontFactory.getFont(FontFactory.HELVETICA,
								9.0f)));
				hcell.setBorder(Cell.NO_BORDER);
				htable.addCell(hcell);

				// Date and times
				
				hcell = new Cell();
				hcell.setBorder(Cell.NO_BORDER);
				htable.addCell(hcell); 
				

				hcell = new Cell(new Chunk("Fecha: " + strDate,
						FontFactory.getFont(FontFactory.HELVETICA,
								9.0f)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(2);
				htable.addCell(hcell);

				
				hcell = new Cell(new Chunk("Horario: "
						+ strStartTime + " - " + strEndTime,
						FontFactory.getFont(FontFactory.HELVETICA,
								9.0f)));
				hcell.setBorder(Cell.NO_BORDER);
				htable.addCell(hcell);

				// Student count

				hcell = new Cell(new Chunk("Hay "
						+ rows.size()
						+ (rows.size() == 1 ? " estudiante"
								: " estudiantes"),
						FontFactory.getFont(FontFactory.HELVETICA,
								8.0f)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(3);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				htable.addCell(hcell);

				doc.add(htable);

				// Listing

				Object[] theaders = new Object[] { "N\u00ba",
						"Identificaci\u00f3n",
						"Apellidos y nombre", "Titulaci\u00f3n" };

				Table table = new Table(theaders.length);
				table.setTableFitsPage(true);
				table.setWidths(new float[] { 0.35f, 0.75f, 2.5f,
						1.0f });
				table.setPadding(2.0f);
				table.setBorder(0);
				for (int i = 0; i < theaders.length; i++) {
					Object theader = theaders[i];
					Cell cell = new Cell(new Phrase(
							theader != null ? theader.toString()
									: "", FontFactory.getFont(
									FontFactory.HELVETICA, 7.3f,
									Font.BOLD)));
					cell.setBorderWidth(1.0f);
					cell.setHeader(true);
					if (i == 0) {
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					if (i == 1 || i == 3) {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					table.addCell(cell);
				}
				table.endHeaders();

				int cn = 0;
				for (Object[] row : rows) {
					cn++;
					for (int i = 0; i < row.length; i++) {								
						Object item = row[i];
						if(i==0) item = cn;
						
						Cell cell = new Cell(
								new Phrase(
										item != null ? item
												.toString() : "",
										FontFactory
												.getFont(
														FontFactory.HELVETICA,
														7.3f)));
						cell.setBorderWidth(1.0f);
						if (i == 0) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						if (i == 1 || i == 3) {
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						table.addCell(cell);
					}
				}
				
				doc.add(table);
				doc.newPage();
		 }					
     }
	 if (!hasRows) {
	    doc.add(new Paragraph("No hay datos."));
	    return;
	}
  }

}
