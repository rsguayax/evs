package es.grammata.evaluation.evs.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.services.lrs.XapiWrap;



@Controller
public class HomeController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(HomeController.class);
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Autowired
	private MatterTestStudentService mtsService;
	
	@Autowired
	private AcademicPeriodService academicPeriodService;
	
	
	@Autowired
	private XapiWrap xapiWrap;

	@RequestMapping("/")
	public String redirectToHome(Map<String, Object> model) {	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<String>();
 
        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
 
        if (roles.contains("STUDENT")) {
        	return "redirect:" + "/student";
        } else {
        	return "redirect:" + "/home";
        }
	}

	@RequestMapping("/home")
	public String showHomePage(Map<String, Object> model) {
		model.put("headText", "Sistema integrador");
		log.info("Insertado parámetro en modelo");

		
		return "home";
	}

	@RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout, HttpSession session) {
	    ModelAndView model = new ModelAndView();
	    if (error != null) {
		String errorMsg = "none";
		AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (ex != null) {
		    errorMsg = ex.getMessage();
		}
		log.error(errorMsg);

		model.addObject("error", "El nombre de usuario o la contrase\u00f1a o el rol indicados no son correctos.");
	    }

	    if (logout != null) {
		//model.addObject("msg", "Correcto");
	    }
	    model.setViewName("login");

	    return model;
	}

	@RequestMapping(value = "/access_denied", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView loginAccessDenied() {

		ModelAndView mav = new ModelAndView("errors/errorext");
		mav.addObject("headText",  "Permiso denegado");
		mav.addObject("errorMessage",  "No tiene permisos para acceder a la página solicitada");
		return mav;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/deleteperiods", method = {RequestMethod.GET, RequestMethod.POST})
	public void deletePeriods(HttpServletRequest request) {
		List<AcademicPeriod> periods = academicPeriodService.findAll();
		for(AcademicPeriod period : periods) {
			try {
				academicPeriodService.delete(period.getId());			
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/testxapi", method = {RequestMethod.GET, RequestMethod.POST})
	public String testtxapi(HttpServletRequest request) {
		
		/*****
		 * EJEMPLO USO XAPI
		 */
		
		/*EvaluationAssignmentMatter eam = new EvaluationAssignmentMatter();
		eam.setId(1629L);
		List<MatterTestStudent> mtss = mtsService.findByEvaluationAssignmentMatter(eam);
		
		for(MatterTestStudent mts : mtss) {
			Map<String, String> props = new HashMap<String, String>();
			User user = mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser();
			props.put("fullname", user.getFullName());
			props.put("useremail", user.getEmail());
			props.put("username", user.getUsername());
			
			EvaluationEventMatterTest eemt = mts.getEvaluationEventMatterTest();
			props.put("mattercode",eemt.getEvaluationEventMatter().getMatter().getCode());
			props.put("testtype", eemt.getTest().getEvaluationType().getCode());
			props.put("evaleventcode", eemt.getEvaluationEventMatter().getEvaluationEvent().getCode());
			props.put("mattername", eemt.getEvaluationEventMatter().getMatter().getName());
			
			Date testDate = (mts.getSession().getTestDate() != null?mts.getSession().getTestDate():null);
			props.put("testDate", Long.toString(testDate.getTime()));
			
			String score = (mts.getSession().getRate()!=null)?mts.getSession().getRate().toString():null;
			props.put("score", score);
			props.put("sessionid", mts.getSession().getId().toString());
			
			props.put("mtsid", mts.getId().toString());
			
			props.put("eventname", "\\core\\event\\assessment_scored");
			props.put("action", "scored");
			props.put("target", "assessment");
			props.put("objecttable", null);
			props.put("objectid", null);
			props.put("userauth", "manual");

			
			xapiWrap.scoreTest(props);
			
		}*/

		return "home";
	}
	
	/*
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/updpasswdaux", method = RequestMethod.GET)
	public void updatePasswords() {
		List<EvaluationAssignment> eass = evaluationAssignmentService.findAll();
		for(EvaluationAssignment ea : eass) {
			String pass = ea.getExternalPassword();
			String aux = pass;
			try {
				aux = AESCrypto.encrypt(pass);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ea.setExternalPassword(aux);
			evaluationAssignmentService.save(ea);
		}
	
	}
	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/comebackpasswd", method = RequestMethod.GET)
	public void updatePasswordss() {
		List<EvaluationAssignment> eass = evaluationAssignmentService.findAll();
		BASE64Decoder b = new BASE64Decoder();
		for(EvaluationAssignment ea : eass) {
			String pass = ea.getExternalPassword();
			String aux = pass;
			if(pass.length() > 25) {
				try {
					aux = AESCrypto.decrypt(b.decodeBuffer(aux).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				try {
					aux = AESCrypto.decrypt(aux);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ea.setExternalPassword(aux);
			evaluationAssignmentService.save(ea);
		}
	
	}*/
	
	
}
