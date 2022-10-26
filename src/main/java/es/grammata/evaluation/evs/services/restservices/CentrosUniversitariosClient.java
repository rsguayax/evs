package es.grammata.evaluation.evs.services.restservices;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.services.repository.CenterService;

@Component
@Configurable
public class CentrosUniversitariosClient extends UtplRestClient {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CentrosUniversitariosClient.class);
	
	@Autowired
	private CenterService centerService;
	
	@Value("${service.utpl.rest.centros.uri}")
	private String centrosUri;
	
	public void loadAndCreateCenters() throws Exception {
		try {
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			params.add("SkipCount", "1"); 
			params.add("MaxResultCount", "1000");
			String response = callServiceApimNetcore(centrosUri, params);
			createCenters(response);
		} catch (Exception ex) {
			log.error("Error al cargar los centros universitarios");
			ex.printStackTrace();
			throw new Exception(ex);
		}
	}

	@Transactional
	private void createCenters(String responseJsonString) {
		JsonObject responseJson = new JsonParser().parse(responseJsonString).getAsJsonObject();
		
		if (responseJson.get("success").getAsBoolean()) {
			JsonArray centersJson = responseJson.get("result").getAsJsonObject().get("items").getAsJsonArray();		
			
			if (centersJson.size() > 0) {
				centerService.updateAllAsNotActive();
				
				for (int i = 0; i < centersJson.size(); i++) {
					JsonObject centerJson = centersJson.get(i).getAsJsonObject();
					Center center = centerService.findByUniqueCode(centerJson.get("codigoUnico").getAsString());
					
					if(center == null) {
						center = new Center();
						center.setUniqueCode(centerJson.get("codigoUnico").getAsString());
					}
										
					center.setCode(centerJson.get("codigo").getAsString());
					center.setExternalId(centerJson.get("guid").getAsString());
					center.setName(centerJson.get("nombre").getAsString());
					center.setAddress(centerJson.get("direccion").getAsString());
					center.setCity(centerJson.get("ciudad").getAsString());
					center.setType(!centerJson.get("descripcion").isJsonNull() ? centerJson.get("descripcion").getAsString() : null);
					center.setActive(true);
					if (center.getId() == null) {
						centerService.save(center);
					} else {
						centerService.update(center);
					}
				}
			}
		}
	}
}
