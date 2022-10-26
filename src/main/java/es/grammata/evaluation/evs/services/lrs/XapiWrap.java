package es.grammata.evaluation.evs.services.lrs;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


@org.springframework.stereotype.Component
@Configurable
public class XapiWrap {
	
	private static final String SCORE_VERB = "http://adlnet.gov/expapi/verbs/scored";
	private static final String TEST_ACTIVITY = "http://lrs.learninglocker.net/define/extensions/evs_test";
	private static final String ASSESSMENT_ACTIVITY = "http://adlnet.gov/expapi/activities/assessment";
	private static final String TEST_SCORE = "http://lrs.learninglocker.net/define/extensions/evs_score";
	
	@Value("${lrs.url}")
	private String LRS_URL;
	
	@Value("${lrs.username}")
	private String USERNAME;
	
	@Value("${lrs.password}")
	private String PASSWORD;
	

	public void scoreTest(Map<String, String> props) {
		try {			
			StatementClient client = new StatementClient(LRS_URL, USERNAME,
					PASSWORD);

			Agent agent = new Agent();
			agent.setMbox("mailto:" + props.get("useremail"));
			agent.setName(props.get("fullname"));

			Verb verb = new Verb(SCORE_VERB);
			

			Context context = new Context();
			context.setPlatform("EVS");
			context.setLanguage("es");
			HashMap<String, JsonElement> contextExtensions = new HashMap<String, JsonElement>();
			JsonObject contJsonObject = new JsonObject();
			contJsonObject.addProperty("mattercode", props.get("matterCode"));
			contJsonObject.addProperty("testtype", props.get("testtype"));
			contJsonObject.addProperty("mtsid", props.get("mtsid"));
			contJsonObject.addProperty("evaleventcode", props.get("evaleventcode"));
			contJsonObject.addProperty("mattername", props.get("mattername"));
			
			contextExtensions.put("http://lrs.learninglocker.net/define/extensions/grammata_data", contJsonObject);
			
			contJsonObject = new JsonObject();
			contJsonObject.addProperty("eventname", "\\core\\event\\assessment_scored");
			contJsonObject.addProperty("action", "scored");
			contJsonObject.addProperty("target", "assessment");
			contJsonObject.addProperty("objecttable", "");
			contJsonObject.addProperty("userauth", "manual");		
			
			contextExtensions.put("http://lrs.learninglocker.net/define/extensions/logstore_standard_log", contJsonObject);
			
			context.setExtensions(contextExtensions);
			

			Activity activity = new Activity(
					TEST_ACTIVITY + "?mtsid=" + props.get("mtsid"));
					
			ActivityDefinition ad = new ActivityDefinition();
			
			ad.setType(ASSESSMENT_ACTIVITY);
			
			HashMap<String, JsonElement> extensions = new HashMap<String, JsonElement>();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("score", props.get("score"));
			jsonObject.addProperty("testdate", props.get("testdate"));
			jsonObject.addProperty("sessionid", props.get("sessionid"));
			
			extensions.put(TEST_SCORE, jsonObject);
			
			ad.setExtensions(extensions);
			
			activity.setDefinition(ad);
		

			Statement st = new Statement();
			st.setActor(agent);
			st.setVerb(verb);
			st.setObject(activity);
			st.setContext(context);

			//String publishedId = client.postStatement(st);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void example() {

		try {			
			StatementClient client = new StatementClient(LRS_URL, USERNAME,
					PASSWORD);
			
			StatementResult results = client.getStatements();
			

			Agent agent = new Agent();
			agent.setMbox("mailto:mcastillo@grammata.es");
			agent.setName("Mati Castillo Tarifa");

			Verb verb = new Verb("http://adlnet.gov/expapi/verbs/initialized");

			Activity activity = new Activity(
					"http://adlnet.gov/expapi/activities/lesson");

			Statement st = new Statement();
			st.setActor(agent);
			st.setVerb(verb);
			st.setObject(activity);

			String publishedId = client.postStatement(st);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
