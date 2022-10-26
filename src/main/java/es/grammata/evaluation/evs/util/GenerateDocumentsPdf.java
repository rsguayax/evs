package es.grammata.evaluation.evs.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import es.grammata.evaluation.evs.mvc.controller.util.StudentExtendedDocReport;
import es.grammata.evaluation.evs.mvc.controller.util.StudentExtendedMatterDocReport;
import es.grammata.evaluation.evs.mvc.controller.util.StudentListElementReport;

public class GenerateDocumentsPdf {
	
	
	private ServletContext servletContext;
	
	
	

	public void buildPdfDocument(Map<String, Object> model, ServletContext servletContext, File file) throws Exception {
		
		Document doc = new Document();
		
		this.servletContext = servletContext;
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();
		this.buildPdfDocument(model, doc, writer, servletContext);
		doc.close();
	}
	
	
	
	protected void buildPdfDocument(Map<String, Object> model, Document doc,
			PdfWriter writer, ServletContext servletContext) throws Exception {
		
		this.servletContext = servletContext;
				
		int type = (int) model.get("type");
		String evaluationEventName = (String) model.get("evaluationEventName"); // 1
	
		List<StudentListElementReport> elements = (List<StudentListElementReport>) model
				.get("elements");
	
		boolean hasRows = false;
		
		// si es modo persistido habra que generar un documento por centro, almacenarlo y enviar correo avisando de que esta listo
	
		if(elements != null && elements.size() > 0) {
			for(StudentListElementReport element : elements) {
				List<StudentListElementReport> elementsAux = new ArrayList<StudentListElementReport>();
				elementsAux.add(element);
				if(type != 3) {
					hasRows = generateList(doc, elementsAux, evaluationEventName, 1) || hasRows;
					// hay que imprimirlo dos veces
					doc.newPage();
					hasRows = generateList(doc, elementsAux, evaluationEventName, 1) || hasRows;	
					doc.setPageSize(PageSize.A4.rotate());
					doc.newPage();
					hasRows = generateList(doc, elementsAux, evaluationEventName, 2) || hasRows;
					doc.setPageSize(PageSize.A4);
					doc.newPage();
				}
				hasRows = generateVoucher(doc, elementsAux, evaluationEventName, 1) || hasRows;
				
			}
		}
	
		if (!hasRows) {
			doc.add(new Paragraph("No hay datos."));
		}
	
	}
	
	private int getLastElementnoEmpty(List<StudentListElementReport> elements) {
		int last = 0;
		for (int i = 0; i < elements.size(); i++) {
			StudentListElementReport element = elements.get(i);
			if(element.getUsers() != null && element.getUsers().size() > 0) {
				last = i;
			}
		}
		return last;
	}
	
	private boolean generateList(Document doc,
			List<StudentListElementReport> elements,
			String evaluationEventName, int type) throws Exception {
		boolean hasRows = false;
		int last = getLastElementnoEmpty(elements);
		for (int cont = 0; cont < elements.size(); cont++) {
			StudentListElementReport element = elements.get(cont);
			String evaluationCenterName = element.getCenterName();
			String classroomName = element.getClassRoomName();
			String strDate = element.getDateStr();
			String strStartTime = element.getInitDateStr();
			String strEndTime = element.getEndDateStr();
			List<Object[]> rows = element.getUsers();
	
			if (rows != null && rows.size() > 0) {
				hasRows = true;
	
				generateHeader(type, evaluationEventName, evaluationCenterName,
						classroomName, strStartTime, strEndTime, strDate, doc,
						rows);
	
				// Listing
				Object[] theaders = null; 
				
				if(type == 2) {
					theaders = new Object[] { "N\u00ba",
							"Identificaci\u00f3n", "Apellidos y nombre",
							"Titulaci\u00f3n", "# pruebas", "Desarrolladas", "No desarr.", "Subtotal", "Firma estudiante" };
				} else if (type == 1) {
					theaders = new Object[] { "N\u00ba",
							"Identificaci\u00f3n", "Apellidos y nombre",
							"Titulaci\u00f3n" };
				}
	
				Table table = new Table(theaders.length);
				table.setTableFitsPage(true);			
				if(type == 2) {
					table.setWidths(new float[] { 0.15f, 0.5f, 1.5f, 1.0f, 0.35f, 0.45f, 0.45f, 0.45f, 0.45f });
				} else if(type == 1) {
					table.setWidths(new float[] { 0.35f, 0.75f, 2.5f, 1.0f });
				}
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
					if(type == 2) {
						Integer ntests = element.getExtendedUsersInfo().get(cn).getExtendedMatters().size();					
						List<Object> rList = Arrays.asList(row);
						ArrayList<Object> updatableList = new ArrayList<Object>();
						updatableList.addAll(rList); 
						updatableList.add(ntests);
						row = updatableList.toArray();
					}
					
					cn++;
					for (int i = 0; i < row.length; i++) {
						Object item = row[i];
						if (i == 0)
							item = cn;
	
						Cell cell = new Cell(new Phrase(
								item != null ? item.toString() : "",
								FontFactory
										.getFont(FontFactory.HELVETICA, 7.3f)));
						cell.setBorderWidth(1.0f);
						if (i == 0) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						if (i == 1 || i == 3) {
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						table.addCell(cell);
					}
					
					// rellenamos los que faltan
					if(type == 2) {
						int rowSize = theaders.length - row.length;
						for (int i = 0; i < rowSize; i++) {
							Cell cell = new Cell(new Phrase("",
									FontFactory
											.getFont(FontFactory.HELVETICA, 7.3f)));
							cell.setBorderWidth(1.0f);						
							table.addCell(cell);
						}
					}
				}
	
				doc.add(table);
				if(!(cont == last)) {
					doc.newPage();
				}
			}
		}
	
		return hasRows;
	
	}
	
	
	
	private Table getHeaderTitles(String evaluationEventName, String title) throws Exception {
		Table htable = new Table(4);
		htable.setBorder(Table.NO_BORDER);
		htable.setPadding(0.6F);

	
		// Logo
		Image logo = Image.getInstance(servletContext.getRealPath(
				"/web-resources/img/v-logo.png"));
		logo.scalePercent(9.0f);
	
		Cell hcell = new Cell(logo);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setHeader(true);
		hcell.setColspan(1);
		if(title == null)
			hcell.setRowspan(3);
		else 
			hcell.setRowspan(4);
		
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);
	
		hcell = new Cell(new Chunk(
				"UNIVERSIDAD T\u00c9CNICA PARTICULAR DE LOJA",
				FontFactory
						.getFont(FontFactory.HELVETICA, 11.0f, Font.BOLD)));
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setHeader(true);
		hcell.setColspan(3);
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);
	
		hcell = new Cell(new Chunk("MODALIDAD ABIERTA Y A DISTANCIA",
				FontFactory.getFont(FontFactory.HELVETICA, 9.5f, Font.BOLD)));
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setHeader(true);
		hcell.setColspan(3);
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);
		
	
		// title
		if(title != null) {
			hcell = new Cell(
					new Chunk(title, FontFactory.getFont(
							FontFactory.HELVETICA, 9.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);
		}	
	
		// Evaluation event
		hcell = new Cell(
				new Chunk(evaluationEventName, FontFactory.getFont(
						FontFactory.HELVETICA, 9.5f, Font.BOLD)));
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(3);
		hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		htable.addCell(hcell);
		
		
		return htable;
	}
	
	private void generateHeader(int type, String evaluationEventName,
			String evaluationCenterName, String classroomName,
			String strStartTime, String strEndTime, String strDate,
			Document doc, List<Object[]> rows) throws Exception {
	
		if (type == 1 || type == 2) {
			// Header
			Table htable = this.getHeaderTitles(evaluationEventName, null);
			
	
			// row	
			Cell hcell = new Cell(
					new Chunk("Nombre centro: " + evaluationCenterName, FontFactory.getFont(
							FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			htable.addCell(hcell);
			
			hcell = new Cell();
			hcell.setBorder(Cell.NO_BORDER);
			htable.addCell(hcell);
	
			hcell = new Cell();
			hcell.setBorder(Cell.NO_BORDER);
			htable.addCell(hcell);
	
			// row
	
			hcell = new Cell(new Chunk("Aula: " + classroomName,
					FontFactory.getFont(FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			htable.addCell(hcell);
			
			/*hcell = new Cell();
			hcell.setBorder(Cell.NO_BORDER);
			htable.addCell(hcell);*/
	
			hcell = new Cell(new Chunk("Horario: " + strStartTime + " - "
					+ strEndTime, FontFactory.getFont(FontFactory.HELVETICA,
					9.0f, Font.BOLD)));
			hcell.setColspan(2);
			hcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			hcell.setBorder(Cell.NO_BORDER);
			htable.addCell(hcell);
			
			
			// row
	
			hcell = new Cell(new Chunk("Profesor evaluador: ______________________",
					FontFactory.getFont(FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			htable.addCell(hcell);
			
			/*hcell = new Cell();
			hcell.setBorder(Cell.NO_BORDER);
			htable.addCell(hcell);*/
	
			hcell = new Cell(new Chunk("Fecha: " + strDate,
					FontFactory.getFont(FontFactory.HELVETICA, 9.0f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			hcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			htable.addCell(hcell);
	
	
			// Student count
	
			hcell = new Cell(new Chunk("Hay " + rows.size()
					+ (rows.size() == 1 ? " estudiante" : " estudiantes"),
					FontFactory.getFont(FontFactory.HELVETICA, 8.0f)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			htable.addCell(hcell);
	
			doc.add(htable);
		}
	}
	
	private boolean generateVoucher(Document doc,
			List<StudentListElementReport> elements,
			String evaluationEventName, int type) throws Exception {
		
		boolean hasRows = false;
		for (int cont = 0; cont < elements.size(); cont++) {
			StudentListElementReport element = elements.get(cont);
			List<StudentExtendedDocReport> extendedInfos = element.getExtendedUsersInfo();
			if(extendedInfos != null && extendedInfos.size() > 0) {
				for(StudentExtendedDocReport info : extendedInfos) {
					generateVoucherDoc(type, doc, info, element.getCityName());
					doc.newPage();	
					generateInstructionsDoc(type, doc, info, element.getClassRoomName());
					doc.newPage();	
				}
				if(!hasRows) {
					hasRows = true;
				}
			}
	
		}
		
		return hasRows;
	}
	
	private void generateVoucherDoc(int type, Document doc, StudentExtendedDocReport info, String city) throws Exception {
	
		// Header
		for(int i = 0; i < 2; i++) {
			Table htable = this.getHeaderTitles(info.getEvaluationEventName(), "COMPROBANTE DE PRESENTACIÓN");
			htable.setWidth(95);
			//htable.endHeaders();
			
			// row
			Cell hcell = new Cell(Chunk.NEWLINE);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
		
		
			// row
			hcell = new Cell(
					new Chunk("Apellidos y nombre: " + info.getFullName(), FontFactory.getFont(
							FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
			
		
			// row
			hcell = new Cell(new Chunk("Identificador: " + info.getIdentification(),
					FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
			
			// row
			hcell = new Cell(new Chunk("Titulación: " + info.getCareers(),
					FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
			
			
			// row
			hcell = new Cell(new Chunk("Centro universitario: " + info.getCenterName(),
					FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(3);
			htable.addCell(hcell);
			
			if(info.getExtendedMatters() != null) {
				hcell = new Cell(new Chunk("Número evaluaciones: " + info.getExtendedMatters().size(),
						FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(1);
				hcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				htable.addCell(hcell);
			} else {
				hcell = new Cell(new Chunk("",
						FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(2);
				hcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				htable.addCell(hcell);
			}
			
			
			// row
			hcell = new Cell(new Chunk("Fecha: " + info.getStrDate(),
					FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			htable.addCell(hcell);
			
			hcell = new Cell(new Chunk("Horario: " + info.getTime(),
					FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(2);
			hcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			htable.addCell(hcell);
			
			// row
			hcell = new Cell(Chunk.NEWLINE);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
			
			
			for(StudentExtendedMatterDocReport semdr: info.getExtendedMatters()) {
				// row
				hcell = new Cell(new Chunk(semdr.getMatterName().toUpperCase() + " - " + semdr.getTest().toUpperCase(),
							FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setColspan(4);
				htable.addCell(hcell);
			}
			
			// row
			hcell = new Cell(Chunk.NEWLINE);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
					
			
			
			// row
			hcell = new Cell(new Chunk("Su próximo horario de exámenes presenciales será enviado a su correo electrónico personal o puede consultarlo en la edu sección Calendarios.",
					FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			hcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
			htable.addCell(hcell);
			
			// row
			hcell = new Cell(new Chunk("Comportamiento ético: \"El Art. 67. del reglamento de Régimen Académico dice: Fraude o deshonestidad académica.- Es toda acción que, inobservando el principio de transparencia académica, viola los derechos de autor o incumple las normas éticas establecidas por la Institución de Educación Superior (IES) o por el profesor, para los procesos de evaluación y/o de presentación de resultados de aprendizaje, investigación o sistematización. Configuran conductas de fraude o deshonestidad académica entre otras, las siguientes:\n" +
					"a. Apropiación de ideas o de información de pares dentro de procesos de evaluación.\n" +
					"b. Uso de soportes de información para el desarrollo de procesos de evaluación que no han sido autorizados por el profesor.\n" +
					"c. Reproducción en lo substancial, a través de la copia litera la parafrasis o sintesis de creaciones intelectuales o artisticas, sin observar los derechos de autor.\n" +
					"d. Acuerdo para la suplantación de identidad o la realización de actividades en procesos de evaluación, incluyendo el trabajo de titulación.\n" +
					"e. Acceso no autorizado a reactivos y/o respuestas para evaluaciones.\n" +
					"Por ello, tanto en las evaluaciones como en todo trabajo académico, el estudiante debe observar una conducta ética e intachable cualquier intento de fraude académico: sustracción de exámenes, copia de evaluaciones a distancia y en presencia, etc. es objeto de suspensión del o los componentes, o de expulsión inmediata e irrevocable de la universidad.\"\n " +
					"Información del trámite de recalificación de evaluaciones:\n" +
					"Para información del trámite de recalificación ingresa a la siguiente dirección distancia utpl.edu.ec/tramites\n" +
					"Los cuadernillos de preguntas se sujetan a esta disposición:\n" +
					"\"Aviso de confidencialidad\n El presente documento (evaluación) ha sido elaborado por la uTPL con fines académicos; en tal virtud, tiene el carácter de confidencial y para uso exclusivo de la Institución y de la persona a la que se remite. Por lo expuesto: se le notifica que cualquier uso, reproducción, distribución, copia o cualquier otra acción del mismo. a través de cualquier medio, está estrictamente prohibido y, será sancionado de conformidad con las normas contenidas en la legislación interna y/o externa en nuestro país, aplicable en la materia\".\n\n" +
					"Con la firma de presente documento, el estudiante deja constancia de haberse presentado a rendir las evaluaciones presenciales en linea correspondientes al Evaluación Final."
					, FontFactory.getFont(FontFactory.HELVETICA, 6.7f)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			hcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
			htable.addCell(hcell);
			
			// row
			hcell = new Cell(Chunk.NEWLINE);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
							
			
			// row
			if(city == null || city.equals("")) {
				city = "_____________";
			}
			hcell = new Cell(new Chunk("Es dado en la ciudad de " + city + " en " + info.getStrDate(),
					FontFactory.getFont(FontFactory.HELVETICA, 6.7f, Font.BOLD)));
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
			
			// row
			hcell = new Cell(Chunk.NEWLINE);
			hcell.setBorder(Cell.NO_BORDER);
			hcell.setColspan(4);
			htable.addCell(hcell);
							
			
			// row
			if(i == 0) {
				hcell = new Cell(new Chunk("Firma del estudiante",
						FontFactory.getFont(FontFactory.HELVETICA, 6.7f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				hcell.setColspan(4);
				htable.addCell(hcell);
			} else {
				hcell = new Cell(new Chunk("Firma del evaluador",
						FontFactory.getFont(FontFactory.HELVETICA, 6.7f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				hcell.setColspan(2);
				htable.addCell(hcell);
				
				hcell = new Cell(new Chunk("Nombre del evaluador",
						FontFactory.getFont(FontFactory.HELVETICA, 6.7f, Font.BOLD)));
				hcell.setBorder(Cell.NO_BORDER);
				hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				hcell.setColspan(2);
				htable.addCell(hcell);
			}
			
			doc.add(htable);
			
			doc.newPage();	
		}
		
	}
	
	
	private boolean generateInstructions(Document doc,
			List<StudentListElementReport> elements,
			String evaluationEventName, int type) throws Exception {
		
		boolean hasRows = false;
		for (int cont = 0; cont < elements.size(); cont++) {
			StudentListElementReport element = elements.get(cont);
			List<StudentExtendedDocReport> extendedInfos = element.getExtendedUsersInfo();
			if(extendedInfos != null && extendedInfos.size() > 0) {
				for(StudentExtendedDocReport info : extendedInfos) {
					generateInstructionsDoc(type, doc, info, element.getClassRoomName());
					doc.newPage();
				}
				if(!hasRows) {
					hasRows = true;
				}
			}
	
		}
		
		return hasRows;
	}
	
	private void generateInstructionsDoc(int type, Document doc, StudentExtendedDocReport info, String classroomName) throws Exception {
	
		// Header
		Table htable = this.getHeaderTitles(info.getEvaluationEventName(), "EVALUACIÓN PRESENCIAL EN LÍNEA CONTROLADA");
		htable.setWidth(95);
		
		// row
		Cell hcell = new Cell(Chunk.NEWLINE);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		htable.addCell(hcell);
		
		
		// row	
		Chunk chunk = new Chunk("DATOS DEL ESTUDIANTE", FontFactory.getFont(
				FontFactory.HELVETICA, 8.5f, Font.BOLD));
		chunk.setUnderline(0.1f, -2f);
		hcell = new Cell(chunk);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		htable.addCell(hcell);
		
		// row
		Cell dcell = new Cell(Chunk.NEWLINE);
		dcell.setBorder(Cell.NO_BORDER);
		dcell.setColspan(4);
		htable.addCell(dcell);
		
		doc.add(htable);
		
		Table dtable = new Table(4);
		dtable.setBorder(Table.NO_BORDER);
		dtable.setPadding(2F);
	
		// row
		hcell = new Cell(
				new Chunk("Apellidos y nombre: " + info.getFullName(), FontFactory.getFont(
						FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);
		hcell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
	
		// row
		hcell = new Cell(new Chunk("Identificador: " + info.getIdentification(),
				FontFactory.getFont(FontFactory.HELVETICA, 7.5f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);	
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
		
		// row
		hcell = new Cell(new Chunk("Centro universitario: " + info.getCenterName(),
				FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);	
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
		// row
		hcell = new Cell(new Chunk("Aula: " + classroomName,
				FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);	
		hcell.setColspan(4);
		dtable.addCell(hcell);
			
		// row
		hcell = new Cell(new Chunk("Fecha: " + info.getStrDate(),
				FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);	
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
		hcell = new Cell(new Chunk("Horario: " + info.getTime(),
				FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorderWidth(1.0f);	
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
		// row
		hcell = new Cell(Chunk.NEWLINE);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		dtable.addCell(hcell);
			
		// row
		hcell = new Cell(Chunk.NEWLINE);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		dtable.addCell(hcell);
		
		doc.add(dtable);
		
		
		Table ttable = new Table(4);
		ttable.setBorder(Table.NO_BORDER);
	
		
		// row
		chunk = new Chunk("INSTRUCCIONES PARA EL ESTUDIANTE", FontFactory.getFont(
				FontFactory.HELVETICA, 8.5f, Font.BOLD));
		chunk.setUnderline(0.1f, -2f); //0.1 thick, 2 y-location
		hcell = new Cell(chunk);
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		ttable.addCell(hcell);
		
		
		// row
		hcell = new Cell(new Chunk("- Encienda la tableta.\n" +
				"- Cierre todas las aplicaciones de la tableta.\n" +
				"- Ubique el icono de WIFI/REDES.\n" +
				"- Escoja la red con el nombre: " + info.getNetName() + "\n" +
				"- Escriba la siguiente contraseña: " + info.getNetPassword() + "\n" +
				"- Haga clic en el Aplicativo: Evaluación en linea.\n" +
				"- Ingrese su usuario y contraseña:\n"+
				" 	USUARIO: " + info.getUserName() + "\n" +
				"	CONTRASEÑA: " + info.getPassword() + "\n" +
				"- Rinda sus evaluaciones seleccionando la materia con la que usted desee iniciar.\n" +
				"- Al finalizar cada evaluación haga clic en el icono \"Finalizar test\", para que se registre su evaluación.\n" +
				"- Para escoger una nueva materia, haga clic en \"Hacer un Test\".\n" +
				"- Finalizadas todas sus evaluaciones, notifique al docente evaluador, quien le entregará el comprobante de presentación.\n"
				, FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD)));
		hcell.setBorder(Cell.NO_BORDER);
		hcell.setColspan(4);
		hcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		ttable.addCell(hcell);
	
		doc.add(ttable);
	
	}
}
