package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
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

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.services.repository.ServerService;
import es.grammata.evaluation.evs.mvc.base.BaseController;


@Controller
public class ServerController extends BaseController {

	@Autowired
	private ServerService serverService;


	@PostConstruct
	public void init() {
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'servers_manager')")
	@RequestMapping("/server")
	public String showList(Map<String, Object> model, @ModelAttribute("successMessage") String successMessage) {
		model.put("headText", "Servidores");
		return "evaluation_event_academic/server-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'servers_manager')")
	@RequestMapping(value="/server/servers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Server> listServers() {
		List<Server> servers = serverService.findAll();
		return servers;
	}


	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'servers_manager')")
	@RequestMapping(value="/server/edit/*", method=RequestMethod.POST)
	public String edit(@Valid Server server, BindingResult bindingResult, Map<String, Object> model, RedirectAttributes redirectAttrs,
			@ModelAttribute("successMessage") String successMessage,  HttpServletRequest request,
	        HttpServletResponse response) {

		if(bindingResult.hasErrors()) {
			return "evaluation_event_academic/server-form";
		} else {
			Server serverTmp = serverService.findById(server.getId());
			//Net net = serverTmp.getNet();
			//server.setNet(net);
			serverService.update(server);
			redirectAttrs.addFlashAttribute("successMessage", "Modificado correctamente");
		}

		return "redirect:/server";
	}
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/server/new", method=RequestMethod.GET)
	public String showCreatePage(Map<String, Object> model) {

		model.put("server", new Server());
		model.put("headText", "Nuevo servidor");

		return "evaluation_event_academic/server-form";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/server/edit/{id}", method=RequestMethod.GET)
	public String showEdit(@PathVariable Long id, Map<String, Object> model) {

		Server server = serverService.findById(id);
		model.put("headText", "Edici\u00f3n del servidor " + server.getName());
		model.put("server", server);

		return "evaluation_event_academic/server-form";
	}

	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/server/new", method=RequestMethod.POST)
	public String create(@Valid Server server, BindingResult bindingResult, Map<String, Object> model, RedirectAttributes redirectAttrs,
			@ModelAttribute("successMessage") String successMessage,  HttpServletRequest request,
	        HttpServletResponse response) {


		if(bindingResult.hasErrors()) {
			return "evaluation_center/evaluation-center-form";
		} else {
			try {
				serverService.save(server);
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
			} catch (Exception ex) {
				Throwable t = ex.getCause();
			    if (t instanceof ConstraintViolationException) {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Ya existe un servidor con ese código");
			    } else {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Error al crear");
			    }
			    return "redirect:/server/new";
			}
		}

		return "redirect:/server";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/server/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable Long id, Map<String, Object> model, RedirectAttributes redirectAttrs) {

		try {
			serverService.delete(id);
			redirectAttrs.addFlashAttribute("successMessage", "Eliminado correctamente");
		} catch(PersistenceException e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("dangerMessage", "No se ha podido eliminar, compruebe que no exista ninguna referencia a esta entidad");
		}

		return "redirect:/server";
	}
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	
}
