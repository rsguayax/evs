package es.grammata.evaluation.evs.services.restservices;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;

@Component
@Configurable
public class PeriodosAcademicosClient extends UtplRestClient {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(PeriodosAcademicosClient.class);
	
	@Autowired
	private AcademicPeriodService academicPeriodService;

	@Value("${service.utpl.rest.periodos.uri}")
	private String periodosUri;
	
	public void loadAndCreatePeriods() throws Exception {
		try {
			String response = callServiceV2(periodosUri);
			createPeriods(response);
		} catch (Exception ex) {
			log.error("Error al cargar periodos academicos");
			ex.printStackTrace();
			throw new Exception(ex);
		}
	}
	
	@Transactional
	private void createPeriods(String periodsXml) throws SAXException, IOException, ParserConfigurationException, ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(periodsXml));
		Document document = builder.parse(is);
		String status = document.getElementsByTagName("Status").item(0).getTextContent();
		NodeList dataList = document.getElementsByTagName("Data");
		
		if (status.equals("1") && dataList.getLength() > 0) {
			academicPeriodService.updateAllAsNotActive();
			
			for (int i = 0; i < dataList.getLength(); i++) {
				Element data = (Element) dataList.item(i);
				String externalId = data.getElementsByTagName("pac_guid").item(0).getTextContent();
				String code = data.getElementsByTagName("PAC_CODIGO").item(0).getTextContent();
				String name = data.getElementsByTagName("PAC_NOMBRE").item(0).getTextContent();
				String description = data.getElementsByTagName("PAC_DESCRIPCION").item(0).getTextContent();
				Date startDate = format.parse(data.getElementsByTagName("PAC_FECHA_INICIO").item(0).getTextContent());
				Date endDate = format.parse(data.getElementsByTagName("PAC_FECHA_FIN").item(0).getTextContent());
				
				AcademicPeriod academicPeriod = academicPeriodService.findByCode(code);
				
				if(academicPeriod == null) {
					academicPeriod = new AcademicPeriod();
					academicPeriod.setCode(code);
				}
				
				academicPeriod.setExternalId(externalId);
				academicPeriod.setName(name);
				academicPeriod.setDescription(description);
				academicPeriod.setStartDate(startDate);
				academicPeriod.setEndDate(endDate);
				academicPeriod.setActive(true);
				if (academicPeriod.getId() == null) {
					academicPeriodService.save(academicPeriod);
				} else {
					academicPeriodService.update(academicPeriod);
				}
			}
		}
	}
}
