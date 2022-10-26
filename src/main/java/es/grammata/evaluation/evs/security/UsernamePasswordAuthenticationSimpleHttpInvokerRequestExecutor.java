package es.grammata.evaluation.evs.security;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.security.crypto.codec.Base64;

/**
 * A basic username-password authentication support to <code>SimpleHttpInvokerRequestExecutor</code>.
 *
 * @author ajmedialdea
 *
 */
public class UsernamePasswordAuthenticationSimpleHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    private static final Log LOGGER = LogFactory.getLog(UsernamePasswordAuthenticationSimpleHttpInvokerRequestExecutor.class);

    private String username;

    private String password;

    public void setUsername(String username) {
	this.username = username;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * Provided so subclasses can perform additional configuration if required (eg set additional request
     * headers for non-security related information etc).
     *
     * @param con the HTTP connection to prepare
     * @param contentLength the length of the content to send
     *
     * @throws IOException if thrown by HttpURLConnection methods
     */
    protected void doPrepareConnection(HttpURLConnection con, int contentLength)
        throws IOException {
	// Nothing
    }

    /**
     * Called every time a HTTP invocation is made.
     * <p>
     * Simply allows the parent to setup the connection, and then adds an
     * <code>Authorization</code> HTTP header property that will be used for
     * BASIC authentication.
     * </p>
     * <p>
     * The username and password of Siette system are required.
     * </p>
     *
     * @param con
     *            the HTTP connection to prepare
     * @param contentLength
     *            the length of the content to send
     *
     * @throws IOException
     *             if thrown by HttpURLConnection methods
     */
    protected void prepareConnection(HttpURLConnection con, int contentLength) throws IOException {
	super.prepareConnection(con, contentLength);

	if (username != null && password != null) {
	    String base64 = username + ":" + password;
	    con.setRequestProperty("Authorization", "Basic " + new String(Base64.encode(base64.getBytes())));

	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("HttpInvocation now presenting via BASIC authentication.");
	    }
	} else {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Unable to set BASIC authentication header as Siette system username and/or password aren't provided.");
	    }
	}

	doPrepareConnection(con, contentLength);
    }

}
