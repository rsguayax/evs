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

import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.services.repository.ModeService;

@Component
@Configurable
public class ModalidadClient extends UtplRestClient {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(ModalidadClient.class);
	
	@Autowired
	private ModeService modeService;
	
	@Value("${service.utpl.rest.modalidades.uri}")
	private String modalidadesUri;

	public void loadAndCreateModes() throws Exception {
		try {
			String response = callServiceV2(modalidadesUri);
			createModes(response);
		} catch (Exception ex) {
			log.error("Error al cargar modalidades");
			ex.printStackTrace();
			throw new Exception(ex);
		}
	}
	
	@Transactional
	private void createModes(String modesXml) throws SAXException, IOException, ParserConfigurationException, ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(modesXml));
		Document document = builder.parse(is);
		String status = document.getElementsByTagName("Status").item(0).getTextContent();
		NodeList dataList = document.getElementsByTagName("Data");
		
		if (status.equals("1") && dataList.getLength() > 0) {
			modeService.deleteAll();
			
			for (int i = 0; i < dataList.getLength(); i++) {
				Element data = (Element) dataList.item(i);
				String code = data.getElementsByTagName("mod_codigo").item(0).getTextContent();
				String name = data.getElementsByTagName("mod_nombre").item(0).getTextContent();
				String description = data.getElementsByTagName("mod_descripcion").item(0).getTextContent();
				String createDateString = data.getElementsByTagName("mod_fecha_creacion").item(0).getTextContent();
				Date createDate = null;
				
				if (createDateString != null && createDateString.length() > 0) {
					createDate = format.parse(createDateString);
				}
				
				Mode mode = modeService.findByCode(code);
				
				if(mode == null) {
					mode = new Mode();
					mode.setCode(code);
				}
				
				mode.setName(name);
				mode.setDescription(description);
				mode.setCreateDate(createDate);
				mode.setDeleted(false);
				if (mode.getId() == null) {
					modeService.save(mode);
				} else {
					modeService.update(mode);
				}
			}
		}
	}
}
