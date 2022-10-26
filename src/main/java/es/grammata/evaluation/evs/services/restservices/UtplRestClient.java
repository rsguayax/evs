package es.grammata.evaluation.evs.services.restservices;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import sun.misc.BASE64Encoder;

public class UtplRestClient {
	
	@Value("${service.utpl.rest.user}")
	private String restUser;
	
	@Value("${service.utpl.rest.password}")
	private String restPassword;
	
	@Value("${service.utpl.rest.apim.apikey}")
	private String utplRestApiKey;
	
	@Value("${service.utpl.rest.authorizationtoken.uri}")
	private String authorizationTokenUri;
	
	@Value("${service.utpl.rest.authorizationtoken.user}")
	private String authorizationTokenUser;
	
	@Value("${service.utpl.rest.authorizationtoken.password}")
	private String authorizationTokenPassword;
	
	@Value("${service.restproerp.app_id}")
	private String restProErpAppId;
	
	@Value("${service.restproerp.api_key}")
	private String restProErpApiKey;
	
	private String apimAuthToken;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(UtplRestClient.class);
	
//	protected static final String API_KEY = "VPGosI63KZaIPZ9RvjSy4T/+eB4zfCVkMsVHKcSBTZc=";
	
//	protected static final String APP_ID = "ea3dfd2a2df844b8a9bfb61b4f140e96";	
	
	protected String generateAuthHeader(String appId, String method, String uri, String apiKey) throws Exception {
		
		uri = URLEncoder.encode(uri, "UTF-8").toLowerCase();
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		Long seconds = timestamp.getTime()/1000; 

		String hash = appId + method + uri + seconds;
		String crypt = this.hashSha256(hash, apiKey);

		String authParam = "amx " + appId + ":" + crypt + ":" + seconds;
		return authParam;
	}
	
	protected String generateRestProErpAuthHeader(String method, String uri) throws Exception {
		
		uri = URLEncoder.encode(uri, "UTF-8").toLowerCase();
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		Long seconds = timestamp.getTime()/1000; 

		String hash = restProErpAppId + method + uri + seconds;
		String crypt = this.hashSha256(hash, restProErpApiKey);

		String authParam = "amx " + restProErpAppId + ":" + crypt + ":" + seconds;
		return authParam;
	}

	protected String getApimAuthorizationToken() {
		String authorizationToken = null;
		ClientResponse response = null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(authorizationTokenUri);
			String authHeader = "Basic " + new String(Base64.encodeBase64((authorizationTokenUser + ":" + authorizationTokenPassword).getBytes()), "UTF-8");
	
			for(int i = 0; i < 5; i++) {
				response = webResource.header("Authorization", authHeader)
				    .post(ClientResponse.class);
				
				if(response.getStatus()==200) {
					JsonObject responseJson = new JsonParser().parse(response.getEntity(String.class)).getAsJsonObject();
					authorizationToken = responseJson.get("access_token").getAsString();
					break;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error al hacer la petición a " + authorizationTokenUri);
			log.error(ex.getMessage());
		} finally {
			if(response != null) {
				response.close();
			}
		}
		
		return authorizationToken;
	}

	protected static String hashSha256(String toHash, String seed) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(seed.getBytes("UTF-8")), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(keySpec);
		byte[] hashBytes = mac.doFinal(toHash.getBytes("UTF-8"));

		return new String(Base64.encodeBase64(hashBytes), "UTF-8");
	}
	
	
	protected String callService(String uri, String apiKey, String appId, String js) {
		
		String outputFinal = null;
		ClientResponse response = null;
		try {
			Client client = Client.create();

			WebResource webResource = client.resource(uri);
			
			String authHeader = null;
			
			// amx appId:firma:timestamp
			authHeader = this.generateAuthHeader(appId, "POST", uri, apiKey);
	
			for(int i = 0; i < 1000; i++) {
				response = webResource.header("Authorization", authHeader)
					.header("Content-Type", "application/json")			    
				    .post(ClientResponse.class, js);
				if(response.getStatus()==200) {
					break;
				}
			}
			
			outputFinal = response.getEntity(String.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error al hacer la petición a " + uri);
			log.error("Mensaje: " + js);
			log.error(ex.getMessage());
		} finally {
			if(response != null) {
				response.close();
			}
		}
		
		return outputFinal;
	}


	protected String callServiceV2(String uri) {
		String outputFinal = null;
		ClientResponse response = null;
		try {
			Client client = Client.create();

			WebResource webResource = client.resource(uri);
			BASE64Encoder encoder = new BASE64Encoder();
			String authHeader = "Basic " + encoder.encode((restUser + ":" + restPassword).getBytes());
			response = webResource.header("Authorization", authHeader)
					.header("Content-Type", "application/json")			    
				    .get(ClientResponse.class);
			outputFinal = response.getEntity(String.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error al hacer la petición a " + uri);
			log.error(ex.getMessage());
		} finally {
			if(response != null) {
				response.close();
			}
		}
		
		return outputFinal;
	}
	
	protected String callServiceApimNetcore(String uri,  MultivaluedMap<String, String> params) {
		String outputFinal = null;
		ClientResponse response = null;
		try {
			Client client = Client.create();

			WebResource webResource = client.resource(uri);
			response = webResource.queryParams(params)
					.header("apikey", utplRestApiKey)
					.header("Content-Type", "application/json")		    
				    .get(ClientResponse.class);
			outputFinal = response.getEntity(String.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error al hacer la petición a " + uri);
			log.error(ex.getMessage());
		} finally {
			if(response != null) {
				response.close();
			}
		}
		
		return outputFinal;
	}
	
	protected String callServiceApim(String uri,  String jsParams) {
		String outputFinal = null;
		ClientResponse response = null;
		try {
			Client client = Client.create();

			WebResource webResource = client.resource(uri);
			
			if (apimAuthToken == null) {
				apimAuthToken = getApimAuthorizationToken();
			}
	
			for(int i = 0; i < 5; i++) {
				response = webResource.header("Authorization", "Bearer " + apimAuthToken)
					.header("Content-Type", "application/json")			    
				    .post(ClientResponse.class, jsParams);
				if(response.getStatus()==200) {
					break;
				} else if (response.getStatus() == 401) {
					apimAuthToken = getApimAuthorizationToken();
				}
			}
			
			outputFinal = response.getEntity(String.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error al hacer la petición a " + uri);
			log.error("Mensaje: " + jsParams);
			log.error(ex.getMessage());
		} finally {
			if(response != null) {
				response.close();
			}
		}
		
		return outputFinal;
	}
}
