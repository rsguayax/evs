package es.grammata.evaluation.evs.services.restservices.server;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.NestedServletException;


public class EvsRestAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(EvsRestAuthenticationFilter.class);
	
	private static final String SEC_TOKEN_HEADER = "X-Auth-Token";
	
	private String token = null;
	
	@Value("${service.rest.api_key}")
	private String apiKey;
	
	@Value("${service.rest.app_id}")
	private String appId;
	
	@Value("${service.rest.service_uri}")
	private String serviceUri;
	
	@Value("${service.rest.interval}")
	private Long interval;
	
	

	protected EvsRestAuthenticationFilter() {
		super("/");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		this.token = request.getHeader(SEC_TOKEN_HEADER);
		
		if(this.token!=null && !this.token.equals("")) {
			Authentication authResult;
			try {
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					return;
				}
			} catch (AuthenticationException failed) {
				unsuccessfulAuthentication(request, response, failed);
				return;
			}
	
			try {
				successfulAuthentication(request, response, chain, authResult);
			} catch (NestedServletException e) {
				if (e.getCause() instanceof AccessDeniedException) {
					unsuccessfulAuthentication(request, response,
							new LockedException("Forbidden"));
				}
			}
		} else {
			unsuccessfulAuthentication(request, response,
					new LockedException("Bad Token"));
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		AbstractAuthenticationToken userAuthenticationToken = authUserByToken(this.token);
		if (userAuthenticationToken == null)
			throw new AuthenticationServiceException(MessageFormat.format(
					"Error: {0}", "Token erróneo"));

		return userAuthenticationToken;
	}

	private AbstractAuthenticationToken authUserByToken(String tokenRaw) {
		AbstractAuthenticationToken authToken = null;
		try {
			String[] tokenParts = tokenRaw.split(":");
			if(tokenParts != null && tokenParts.length == 3 && tokenParts[0].equals(this.appId) && checkTimestampExpired(tokenParts[2])) {
				String seconds = tokenParts[2];
				String generateToken = generateAuthHeader(this.appId, RequestMethod.POST.name(), this.serviceUri, this.apiKey, seconds);
				if(generateToken.equals(tokenRaw)) {
					authToken = new UsernamePasswordAuthenticationToken("restuser",
							"", null);
				}
			} else {
				authToken = null;
			}
		} catch (Exception e) {
			logger.error("Error en proceso de desencriptado", e);
		}
		
		return authToken;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);

		getSuccessHandler().onAuthenticationSuccess(request, response,
				authResult);
	}
	
	
	protected String generateAuthHeader(String appId, String method, String uri, String apiKey, String seconds) throws Exception {
		
		uri = URLEncoder.encode(uri, "UTF-8").toLowerCase();
		String hash = appId + method + uri + seconds;
		String crypt = this.hashSha256(hash, apiKey);

		String authParam = appId + ":" + crypt + ":" + seconds;

		return authParam;
	}
	
	protected boolean checkTimestampExpired(String tokenSeconds) {
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		Long curSeconds = timestamp.getTime()/1000; 
		Long numSeconds = Long.parseLong(tokenSeconds);
		if((curSeconds + this.interval) < numSeconds && curSeconds >= numSeconds) {
			return false;
		}
			
		return true;
	}


	protected static String hashSha256(String toHash, String seed) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(seed.getBytes("UTF-8")), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(keySpec);
		byte[] hashBytes = mac.doFinal(toHash.getBytes("UTF-8"));

		return new String(Base64.encodeBase64(hashBytes), "UTF-8");
	}
	
	
	
	

}