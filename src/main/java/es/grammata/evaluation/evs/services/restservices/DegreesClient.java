package es.grammata.evaluation.evs.services.restservices;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;

@Component
@Configurable
public class DegreesClient extends UtplRestClient {

	@Value("${service.restproerp.programasacademicos.uri}")
	private String programasAcademicosUri;
	
	@Autowired
	private DegreeService degreeService;
	
	public void loadDegrees() throws Exception {
		Client client = Client.create();
		WebResource webResource = client.resource(programasAcademicosUri + "?tamanio=10000&pagina=1");
		String authHeader = generateRestProErpAuthHeader("GET", programasAcademicosUri);
		ClientResponse response = webResource.header("Authorization", authHeader).header("Content-Type", "application/json").get(ClientResponse.class);
		
		if(response.getStatus() == 200) {
			JsonObject jsonResponse = new JsonParser().parse(response.getEntity(String.class)).getAsJsonObject();
			
			if (jsonResponse.has("Status") && jsonResponse.get("Status").getAsInt() == 0) {
				JsonArray degreesList = jsonResponse.get("Data").getAsJsonObject().get("Resultados").getAsJsonArray();
				User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();      
				
				for (int i=0; i<degreesList.size(); i++) {
					JsonObject degreeJson = degreesList.get(i).getAsJsonObject();
					String code = degreeJson.get("Codigo").getAsString();
					String name = degreeJson.get("Nombre").getAsString();
					String utplId = degreeJson.get("Id").getAsString();
					String academicLevel = degreeJson.get("NivelAcademico").getAsString();
					String mode = degreeJson.get("Modalidad").getAsString();
					Degree degree = degreeService.findByCode(code);
					
					if (degree == null) {
						degree = new Degree();
						degree.setUtplId(utplId);
						degree.setCode(code);
						degree.setName(name);
						degree.setMode(mode);
						degree.setAcademicLevel(academicLevel);
						degree.setCreated(new Date());
						degree.setModified(new Date());
						degree.setCreated_By(loggedUser.getId().intValue());
						degree.setModified_By(loggedUser.getId().intValue());
						degree.setActive(true);
						degreeService.save(degree);
					} else {
						degree.setName(name);
						degree.setMode(mode);
						degree.setAcademicLevel(academicLevel);
						degree.setModified(new Date());
						degree.setModified_By(loggedUser.getId().intValue());
						degreeService.update(degree);
					}
				}
			}
		}
	
		if(response != null) {
			response.close();
		}
	}
}
