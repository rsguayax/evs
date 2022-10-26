package es.grammata.evaluation.evs.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class EvsAuthenticationDetails extends WebAuthenticationDetails {

    private final String role;

    public EvsAuthenticationDetails(HttpServletRequest request) {
        super(request);
        role = request.getParameter("role");
    }

    public String getRole() {
        return role;
    }
}