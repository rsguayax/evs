package es.grammata.evaluation.evs.services.restservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.data.services.repository.ModeService;

@Component
@Configurable
public class MattersClient extends UtplRestClient {
	
	@Value("${service.utpl.rest.evaluacioncomponentes.uri}")
	private String evaluationComponentsUri;
	
	@Autowired
	private AcademicPeriodService academicPeriodService;

	@Autowired
	private ModeService modeService;
	
	@Autowired
	private AcademicLevelService academicLevelService;
	
	@Autowired
	private MatterService matterService;
	
	public void loadAllmatters() throws Exception {
		List<AcademicPeriod> academicPeriods = academicPeriodService.findActive();
		List<Mode> modes = modeService.findNotDeleted();
		List<AcademicLevel> academicLevels = academicLevelService.findNotDeleted();
		for (AcademicPeriod academicPeriod : academicPeriods) {
			for (Mode mode : modes) {
				for (AcademicLevel academicLevel : academicLevels) {
					String js = "{\"evn_codigo\":\"\",\"mod_codigo\":\""+ mode.getCode() + "\",\"nac_codigo\":\"" + academicLevel.getCode()+ "\",\"pac_codigo\":\"" + academicPeriod.getCode() + "\"}";
					String response = callServiceApim(evaluationComponentsUri, js);
					
					if (response != null && response.length() > 0) {
						JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
						
						if (jsonResponse.has("Tipo") && jsonResponse.get("Tipo").getAsString().equals("Data")) {
							JsonArray matterList = jsonResponse.get("Contenido").getAsJsonArray();
							
							for (int i=0; i<matterList.size(); i++) {
								JsonObject matterJson = matterList.get(i).getAsJsonObject();
								String code = matterJson.get("Codigo").getAsString();
								String mattername = matterJson.get("Nombre").getAsString();
								Matter matter = matterService.findByCode(code);
								
								if (matter == null) {
									matter = new Matter();
									matter.setCode(code);
									matter.setAcademicLevel(academicLevel);
									matter.setName(mattername);
									matter.setType(Matter.TYPE_WS);
									matterService.save(matter);
								} else if (!matter.getName().equals(mattername)) {
									matter.setName(mattername);
									matterService.update(matter);
								}
							}
						}
					}
				}
			}
		}
	}

}
