package es.grammata.evaluation.evs.services.restservices;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@org.springframework.stereotype.Component
@Configurable
public class LoginClient extends UtplRestClient {

	private static final Logger LOGGER = Logger.getLogger(LoginClient.class);

	//private static final String SERVICE_URI = "https://srv-si-001.utpl.edu.ec/REST_PRO/api/seguridad/login";
	
	@Value("${security.remote.service.uri}")
	private String securityRemoteServiceUri;
	
	@Value("${security.remote.service.api.key}")
	private String securityRemoteServiceApiKey;
	
	@Value("${security.remote.service.app.id}")
	private String securityRemoteServiceAppId;

	public Map<String, String> login(String username, String password, String role) throws Exception {
	    Map<String, String> data = null;

	    JSONObject joUser = new JSONObject();	    
	    joUser.put("usuario", username);
	    joUser.put("contrasenia", password);
	    joUser.put("ou", role);

	    String strResponse = callService(securityRemoteServiceUri, securityRemoteServiceApiKey, securityRemoteServiceAppId, joUser.toString());
	    if (strResponse != null && !strResponse.isEmpty()) {
			try {
			    JSONObject joResponse = new JSONObject(strResponse);
			    if (joResponse.has("Status") 
				    && joResponse.has("Message") 
				    && joResponse.has("Data")) { // Valid response
					int status = joResponse.getInt("Status");
					String message = joResponse.getString("Message");
					if (status == 1) { // Success		    
					    JSONObject joData = joResponse.getJSONObject("Data");
					    String dn = joData.has("DN") ? joData.getString("DN") : null;
					    boolean isValidOu = checkOu(dn, role.substring(3));
					    if (isValidOu) { // Valid OU
							data = new HashMap<String, String>();
							data.put("externalId", joData.getString("Identificacion"));
							data.put("firstname", joData.getString("Nombres"));
							data.put("lastname", joData.getString("Apellidos"));
							data.put("email", joData.getString("CorreoUTPL"));
					    } else { // Invalid OU
					    	LOGGER.error("Error de autenticacion: este usuario no tiene el rol solicitado.");
					    }
					} else { // Failure
					    LOGGER.error("Error de autenticacion: status=" + status + ", message=" + message);
					}
			    } else { // Invalid response
			    	LOGGER.error("Respuesta no valida: no se encuentra alguna de las claves necesarias ('Status' o 'Message' o 'Data').");
			    }
			} catch (JSONException e) {
			    	LOGGER.error("Respuesta no valida:", e);
			}
	    }	    

	    return data;
	}
	
	private boolean checkOu(String dn, String ou) {	
	    if (dn == null || ou == null) {
	    	return false;
	    }
	    
	    try {
			LdapName ln = new LdapName(dn);
			for (Rdn rdn : ln.getRdns()) {
			    if ("OU".equalsIgnoreCase(rdn.getType()) 
				    && ou.equalsIgnoreCase(rdn.getValue().toString())) {
			    	return true;		
			    }
			}
	    } catch (InvalidNameException e) {    
	    }
	    return false;
	}

	private String callService() {

		String appId = "ea3dfd2a2df844b8a9bfb61b4f140e96";
		String apiKey = "VPGosI63KZaIPZ9RvjSy4T/+eB4zfCVkMsVHKcSBTZc=";
		String uri = "https://srv-si-001.utpl.edu.ec/REST_PRO/api/seguridad/login";

		Client client = Client.create();

		WebResource webResource = client.resource(uri);

		String js = "{\"usuario\":\"jgnagua1\",\"contrasenia\":\"3aP3rjum\",\"ou\":\"OU_ESTUDIANTES\"}";

		try {
			// amx appId:firma:timestamp
			String authHeader = this.generateHauthHeader(appId, "POST", uri, apiKey);

			//Get response from RESTful Server get(ClientResponse.class);
			ClientResponse response = null;
			response = webResource.header("Content-Type", "application/json")
			    .header("Authorization", authHeader)
			    .post(ClientResponse.class, js);

			String jsonStr = response.getEntity(String.class);
			if(jsonStr != null && !jsonStr.equals("")) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = mapper.readValue(jsonStr, Map.class);
				if(map.containsKey("Tipo") && map.get("Tipo").equals("Data")) {
					LinkedHashMap<String, String> contenido = (LinkedHashMap)map.get("Contenido");
					String identificacion = contenido.get("Identificacion");
					String nombres = contenido.get("Nombres");
					String apellidos = contenido.get("Apellidos");
					String email = contenido.get("Email");
				} else if(map.containsKey("Tipo") && map.get("Tipo").equals("Error")) {
					LinkedHashMap<String, String> contenido = (LinkedHashMap)map.get("Contenido");
					String codigo = String.valueOf(contenido.get("Codigo"));
					String descripcion = contenido.get("Descripcion");
					LOGGER.error("Error validación: " + codigo + " - " + descripcion);
				} else {
					LOGGER.error("ERROR: " + response.getStatus() + " - " + response.getStatusInfo());
				}
			} else {
				LOGGER.error("ERROR: " + response.getStatus() + " - " + response.getStatusInfo());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}


	private String generateHauthHeader(String appId, String method, String uri, String apiKey) throws Exception {

		uri = URLEncoder.encode(uri, "UTF-8").toLowerCase();
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		Long seconds = timestamp.getTime()/1000;

		String hash = appId + method + uri + seconds;
		String crypt = this.hashSha256(hash, apiKey);

		String authParam = "amx " + appId + ":" + crypt + ":" + seconds;

		return authParam;
	}


	public static String hashSha256(String toHash, String seed) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(seed.getBytes("UTF-8")), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(keySpec);
		byte[] hashBytes = mac.doFinal(toHash.getBytes("UTF-8"));

		return new String(Base64.encodeBase64(hashBytes), "UTF-8");
	}


	public static void main(String[] args) {
		LoginClient loginClient = new LoginClient();
		loginClient.callService();
	}


}
