package es.grammata.evaluation.evs.services.restservices;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

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

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;

@Component
@Configurable
public class NivelAcademicoClient extends UtplRestClient {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(NivelAcademicoClient.class);
	
	@Autowired
	private AcademicLevelService academicLevelService;

	@Value("${service.utpl.rest.niveles.uri}")
	private String nivelesUri;
	
	public void loadAndCreateLevels() throws Exception {
		try {
			String response = callServiceV2(nivelesUri);
			createLevels(response);
		} catch (Exception ex) {
			log.error("Error al cargar niveles academicos");
			ex.printStackTrace();
			throw new Exception(ex);
		}
	}
	
	@Transactional
	private void createLevels(String levelsXml) throws SAXException, IOException, ParserConfigurationException, ParseException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(levelsXml));
		Document document = builder.parse(is);
		String status = document.getElementsByTagName("Status").item(0).getTextContent();
		NodeList dataList = document.getElementsByTagName("Data");
		
		if (status.equals("1") && dataList.getLength() > 0) {
			academicLevelService.deleteAll();
			
			for (int i = 0; i < dataList.getLength(); i++) {
				Element data = (Element) dataList.item(i);
				String code = data.getElementsByTagName("nac_codigo").item(0).getTextContent();
				String name = data.getElementsByTagName("nac_nombre").item(0).getTextContent();
				String description = data.getElementsByTagName("nac_descripcion").item(0).getTextContent();
				
				AcademicLevel academicLevel = academicLevelService.findByCode(code);
				
				if(academicLevel == null) {
					academicLevel = new AcademicLevel();
					academicLevel.setCode(code);
				}
				
				academicLevel.setName(name);
				academicLevel.setDescription(description);
				academicLevel.setDeleted(false);
				if (academicLevel.getId() == null) {
					academicLevelService.save(academicLevel);
				} else {
					academicLevelService.update(academicLevel);
				}
			}
		}
	}
}
