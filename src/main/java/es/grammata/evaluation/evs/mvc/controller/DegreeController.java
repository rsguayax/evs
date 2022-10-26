package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.data.services.repository.WeekDayService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.services.restservices.DegreesClient;



@Controller
public class DegreeController extends BaseController{

	@Autowired
	private DegreeService degreeService;

	@Autowired
	private WeekDayService weekDayService;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	public DegreesClient degreesClient;

	private List<AcademicLevel> academicLevels;

	private List<WeekDay> daysAllowed;

	private Map<String, WeekDay> weekDaysCache;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/degree/new", method = {RequestMethod.POST, RequestMethod.GET})
	public String createDegree(HttpServletRequest request, Map<String, Object> model, @Valid Degree degree, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
		if (request.getMethod().equals("POST")) {
			if(!bindingResult.hasErrors()) {
				degree.setCreated(new Date());
				degree.setModified(new Date());
				degree.setActive(true);
				User u= this.userLogin();
				degree.setCreated_By(Integer.parseInt(""+u.getId()));
				degree.setModified_By(Integer.parseInt(""+u.getId()));
				degreeService.save(degree);
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
				return "redirect:/degree/edit/" + degree.getId();
			}		
		} else {
			degree = new Degree();
		}

		model.put("degree", degree);
		model.put("headText", "Nueva Titulacion");
		addAuxToModel(model);
		return "evaluation_event_academic/degree-form";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/degree/edit/{id}", method = {RequestMethod.POST, RequestMethod.GET})
	public String updateDegree(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id, @Valid Degree degree, BindingResult bindingResult, RedirectAttributes redirectAttrs, @ModelAttribute("successMessage") String successMessage) {
		if (request.getMethod().equals("POST")) {
			Degree aux= degreeService.findById(id);
			if (!bindingResult.hasErrors()) {
				degree.setCreated(aux.getCreated());
				degree.setModified(new Date());
				degree.setActive(true);
				User u= this.userLogin();
				degree.setCreated_By(aux.getCreated_By());
				degree.setModified_By(Integer.parseInt(""+u.getId()));
				
				
				degreeService.update(degree);
				redirectAttrs.addFlashAttribute("successMessage", "Modificado correctamente");
				return "redirect:/degree";
			}
		} else {
			degree = degreeService.findById(id);
		}

		model.put("headText", "Configuraci\u00f3n de la titulacion " + degree.getName());
		model.put("degrees", degree);
		addAuxToModel(model);

		return "evaluation_event_academic/degree-form";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/degree/delete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
	public String deleteDegreeLogic(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id, @Valid Degree degree, BindingResult bindingResult, RedirectAttributes redirectAttrs, @ModelAttribute("successMessage") String successMessage) {
		Degree d=degreeService.findById(id);
		d.setActive(false);
		degreeService.update(d);
		if (request.getMethod().equals("POST")) {
			Degree aux= degreeService.findById(id);
			if (!bindingResult.hasErrors()) {
				degree.setActive(false);
				degree.setCreated(aux.getCreated());
				degree.setModified(new Date());
				User u= this.userLogin();
				degree.setCreated_By(aux.getCreated_By());
				degree.setModified_By(Integer.parseInt(""+u.getId()));	
				degreeService.update(degree);// .update(degree);
				return "redirect:/degree";
			}
		} else {
			degree = degreeService.findById(id);
		}

		model.put("headText", "Configuraci\u00f3n de la titulacion " + degree.getName());
		model.put("degrees", degree);
		addAuxToModel(model);
		return "redirect:/degree";
	}
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping("/degree")
	public String showDegreeList(Map<String, Object> model, @ModelAttribute("successMessage") String successMessage) {
		model.put("headText", "Titulaciones");
		List<Degree> degrees = degreeService.findAllAtive();
		model.put("degrees", degrees);
		return "evaluation_event_academic/degree-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/degree/degrees", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Degree> listDegrees() {
		List<Degree> degrees = degreeService.findAllAtive();
		return degrees;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/degree/loaddegrees", method=RequestMethod.GET)
	public @ResponseBody Message loadDegrees() {
		Message responseMessage = new Message();

		try {
			degreesClient.loadDegrees();
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Titulaciones actualizadas correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al actualizar las titulaciones: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@InitBinder
	protected void initDaysBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "daysAllowed", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof WeekDay) {
					return element;
				}
				if (element instanceof String) {
					WeekDay weekDay = DegreeController.this.weekDaysCache.get(element);
					return weekDay;
				}
				return null;
			}
		});
	}
	

	private void addAuxToModel(Map<String, Object> model) {
		model.put("academicLevels", academicLevels);
		model.put("daysAllowed", daysAllowed);
	}
	
	private List<WeekDay> loadDays() {
		List<WeekDay> weekDays = this.weekDayService.findAll();
		this.weekDaysCache = new HashMap<String, WeekDay>();
		for (WeekDay weekDay : weekDays) {
			this.weekDaysCache.put(weekDay.getIdAsString(), weekDay);
		}
		return weekDays;
	}
	
	public User userLogin() {
		User retorno = new User();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();      
		if (principal instanceof UserDetails) {      
			retorno= userService.findByUsername(((UserDetails) principal).getUsername());      
		}
		return retorno;
	}
}