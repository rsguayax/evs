package es.grammata.evaluation.evs.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

public class GenerateDocumentsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc,
			PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		GenerateDocumentsPdf generateDocumentsPdf = new GenerateDocumentsPdf();
		generateDocumentsPdf.buildPdfDocument(model, doc, writer, getServletContext());
	}
	
}
