package es.grammata.evaluation.evs.services.httpservices.client;

import java.io.IOException;

import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.codec.Base64;

public class BasicAuthenticationHttpInvokerRequestExecutor extends
		CommonsHttpInvokerRequestExecutor {

	@Override
	protected PostMethod createPostMethod(HttpInvokerClientConfiguration config)
			throws IOException {
		PostMethod postMethod = super.createPostMethod(config);

		String user = "admin";
		String pw = "admin";

		SecurityContextImpl sc = new SecurityContextImpl();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, pw);

		sc.setAuthentication(auth);

		SecurityContextHolder.setContext(sc);
		String base64 = auth.getName() + ":" + auth.getCredentials().toString();
		postMethod.setRequestHeader("Authorization", "Basic "
				+ new String(Base64.encode(base64.getBytes())));

		return postMethod;
	}
}