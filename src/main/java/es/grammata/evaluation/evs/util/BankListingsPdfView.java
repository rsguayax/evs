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
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import es.grammata.evaluation.evs.mvc.controller.util.BankListElementReport;
import es.grammata.evaluation.evs.mvc.controller.util.BankListReport;

public class BankListingsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc,
			PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BankListReport bankListReport = (BankListReport) model
				.get("bankListReport");

		String evaluationEventName = bankListReport.getEvaluationEventName();
		String departmentName = bankListReport.getDepartmentName();

		List<BankListElementReport> elements = bankListReport.getElements();

		boolean hasRows = false;
	
		if (elements != null && elements.size() > 0) {
			hasRows = true;

			// Header
			Table htable = new Table(4);
			htable.setBorder(Table.NO_BORDER);
			htable.setPadding(0.6F);

			// Logo
			Image logo = Image.getInstance(getServletContext().getRealPath(
					"/web-resources/img/v-logo.png"));
			logo.scalePercent(9.0f);

			Cell hcell = new Cell(logo);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setHeader(true);
			hcell.setColspan(1);
			hcell.setRowspan(6);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);

			hcell = new Cell(new Chunk(
					"UNIVERSIDAD T\u00c9CNICA PARTICULAR DE LOJA",
					FontFactory
							.getFont(FontFactory.HELVETICA, 12.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setHeader(true);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);

			hcell = new Cell(new Chunk("Modalidad de Estudios a Distancia",
					FontFactory.getFont(FontFactory.HELVETICA, 10.0f)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setHeader(true);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);

			
			hcell = new Cell(
					new Chunk(bankListReport.getTitle(), FontFactory.getFont(
							FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);
			
			
			hcell = new Cell(
					new Chunk(evaluationEventName, FontFactory.getFont(
							FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);


			hcell = new Cell(new Chunk(departmentName, FontFactory.getFont(
					FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);
			
			hcell = new Cell(new Chunk(Chunk.NEWLINE));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);
			
			
			htable.endHeaders();
	
			
			doc.add(htable);

			// Listing
			Object[] theaders = null;

			Table table = null;
			if(bankListReport.getType() == 1) {
				theaders = new Object[] { "Banco", "Responsable", "Departamento", "Asignatura", "P. activas",
						"P. ensayo", "P. inactivas", "P. ingresadas", "P. act. no valid.",
						"P. objetivas", "P. baja", "P. activas validadas", "Estado"};
	
				table = new Table(theaders.length);
				table.setTableFitsPage(true);
				table.setWidths(new float[] { 1f, 1f, 1f, 1f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 
						 0.35f, 0.3f, 0.4f, 0.4f });
				table.setPadding(2.0f);
				table.setBorder(0);
				for (int i = 0; i < theaders.length; i++) {
					Object theader = theaders[i];
					Cell cell = new Cell(new Phrase(
							theader != null ? theader.toString() : "",
							FontFactory.getFont(FontFactory.HELVETICA, 7.3f,
									Font.BOLD)));
					cell.setBorderWidth(1.0f);
					cell.setHeader(true);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					table.addCell(cell);
				}
				table.endHeaders();
	
				for (BankListElementReport bl : elements) {
					String bankName = bl.getBankName();
					String manager = bl.getManager();
					String matterName = bl.getMatterName();
					Integer activeQuestions = bl.getActiveQuestions();
					Integer ensayQuestions = bl.getEnsayQuestions(); // ENSAYO?
					Integer inactiveQuestions = bl.getInactiveQuestions();
					Integer insertQuestions = bl.getInsertQuestions(); // INGRESADAS?
					Integer noValActiveQuestions = bl.getNoValActiveQuestions();
					Integer objQuestions = bl.getObjQuestions();
					Integer unsubscribeQuestions = bl.getUnsubscribeQuestions(); // DADAS
																					// DE
																					// BAJA?
					Integer valActiveQuestions = bl.getValActiveQuestions();
					String state = bl.getState();
					String departmentNameTmp = bl.getDepartmentName();
	
					Object[] rows = new Object[] { bankName, manager, departmentNameTmp, matterName,
							activeQuestions, ensayQuestions, inactiveQuestions,
							insertQuestions, noValActiveQuestions, objQuestions,
							unsubscribeQuestions, valActiveQuestions, state };
	
					for (int i = 0; i < rows.length; i++) {
						Object item = rows[i];
	
						Cell cell = new Cell(new Phrase(
								item != null ? item.toString() : "",
								FontFactory.getFont(FontFactory.HELVETICA, 7.3f)));
						cell.setBorderWidth(1.0f);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						/*if (i == 0) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						if (i == 1 || i == 3) {
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
						table.addCell(cell);
					}
				}
			} else {
				theaders = new Object[] { "Banco", "Responsable", "Departamento", "Asignatura", "Cód. asignatura", "Test",
						"Eval.", "P. test", "Punt.", "P. act.",
						"Estado"};
	
				table = new Table(theaders.length);
				table.setTableFitsPage(true);
				table.setWidths(new float[] { 1f, 1f, 1f, 1f, 0.8f, 0.6f, 0.4f, 0.3f, 0.3f, 0.3f, 
						 0.35f});
				table.setPadding(2.0f);
				table.setBorder(0);
				for (int i = 0; i < theaders.length; i++) {
					Object theader = theaders[i];
					Cell cell = new Cell(new Phrase(
							theader != null ? theader.toString() : "",
							FontFactory.getFont(FontFactory.HELVETICA, 7.3f,
									Font.BOLD)));
					cell.setBorderWidth(1.0f);
					cell.setHeader(true);
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					table.addCell(cell);
				}
				table.endHeaders();
	
				for (BankListElementReport bl : elements) {
					String bankName = bl.getBankName();
					String testDepartmentNameTmp = bl.getDepartmentName();
					String matterName = bl.getMatterName();
					String matterCode = bl.getMatterCode();
					String manager = bl.getManager();
					String testName = bl.getTestName();
					String eType = bl.getEvaluationType();
					Integer testQuestions = bl.getTestQuestions();
					Float maxRate = bl.getMaxRate();
					Integer testActQuestions = bl.getTestActiveQuestions();
					String testState = bl.getTestState();
	
					Object[] rows = new Object[] { bankName, manager, testDepartmentNameTmp, matterName,
							matterCode, testName, eType,
							testQuestions, maxRate, testActQuestions, testState };
	
					for (int i = 0; i < rows.length; i++) {
						Object item = rows[i];
	
						Cell cell = new Cell(new Phrase(
								item != null ? item.toString() : "",
								FontFactory.getFont(FontFactory.HELVETICA, 7.3f)));
						cell.setBorderWidth(1.0f);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						/*if (i == 0) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						if (i == 1 || i == 3) {
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
						table.addCell(cell);
					}
				}
			}

			doc.add(table);
			doc.newPage();
		}

		if (!hasRows) {
			doc.add(new Paragraph("No hay datos."));
			return;
		}
	}
	
	@Override
	protected Document newDocument() {
	  return new Document(PageSize.A4.rotate());
	}

}
