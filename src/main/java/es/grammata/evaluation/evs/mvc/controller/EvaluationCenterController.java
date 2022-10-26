package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.grammata.evaluation.evs.data.model.repository.Address;
import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.services.repository.AddressService;
import es.grammata.evaluation.evs.data.services.repository.CapService;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.NetService;
import es.grammata.evaluation.evs.data.services.repository.ServerService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.ExtendedNet;
import es.grammata.evaluation.evs.mvc.controller.util.Message;


@Controller
public class EvaluationCenterController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(EvaluationCenterController.class);

	@Autowired
	private EvaluationCenterService evaluationCenterService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ClassroomService classroomService;

	@Autowired
	private CapService capService;

	@Autowired
	private CenterService centerService;
	
	@Autowired
	private NetService netService;
	
	@Autowired
	private ServerService serverService;
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	//@PreAuthorize("hasPermission('test', 'do_something')")
	@RequestMapping("/evaluationcenter")
	public String showList(Map<String, Object> model) {
		model.put("headText", "Centros de evaluaci\u00f3n");

		List<EvaluationCenter> evaluationCenters = evaluationCenterService.findAll();

		//model.put("evaluationCenters", evaluationCenters);


		return "evaluation_center/evaluation-center-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/evaluationcenters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationCenter> loadEvaluationCenters() {

		List<EvaluationCenter> evaluationCenters = evaluationCenterService.findAll();

		return evaluationCenters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}", method=RequestMethod.GET)
	public String showEdit(@PathVariable Long id, Map<String, Object> model) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		model.put("headText", "Edici\u00f3n del centro de evaluaci\u00f3n " + evaluationCenter.getName());
		model.put("evaluationCenter", evaluationCenter);

		List<Center> centers = new ArrayList<Center>();
		if (evaluationCenter != null) {
			centers = new ArrayList<Center>(evaluationCenter.getRegistrationCenters());
		}
		model.put("centers", centers);

		return "evaluation_center/evaluation-center-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/*", method=RequestMethod.POST)
	public String edit(@Valid EvaluationCenter evaluationCenter, BindingResult bindingResult, Map<String, Object> model, RedirectAttributes redirectAttrs,
			@ModelAttribute("successMessage") String successMessage,  HttpServletRequest request,
	        HttpServletResponse response) {

		if(bindingResult.hasErrors()) {
			return "evaluation_center/evaluation-center-form";
		} else {
			try {
				evaluationCenterService.update(evaluationCenter);
				redirectAttrs.addFlashAttribute("successMessage", "Modificado correctamente");
			} catch (Exception ex) {
				Throwable t = ex.getCause();
			    if (t instanceof ConstraintViolationException) {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Ya existe un centro con ese nombre o código");
			    } else {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Error al crear");
			    }
			    return "redirect:/evaluationcenter/edit/" + evaluationCenter.getId();
			}

		}

		return "redirect:/evaluationcenter";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/new", method=RequestMethod.GET)
	public String showCreatePage(Map<String, Object> model) {

		model.put("evaluationCenter", new EvaluationCenter());
		model.put("headText", "Nuevo centro de evaluaci\u00f3n");

		return "evaluation_center/evaluation-center-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/new", method=RequestMethod.POST)
	public String create(@Valid EvaluationCenter evaluationCenter, BindingResult bindingResult, RedirectAttributes redirectAttrs) {

		if(bindingResult.hasErrors()) {
			return "evaluation_center/evaluation-center-form";
		} else {
			try {
				evaluationCenterService.save(evaluationCenter);
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
			} catch (Exception ex) {
				Throwable t = ex.getCause();
			    if (t instanceof ConstraintViolationException) {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Ya existe un centro con ese nombre o código");
			    } else {
			    	redirectAttrs.addFlashAttribute("dangerMessage", "Error al crear");
			    }
			    return "redirect:/evaluationcenter/new";
			}
		}

		return "redirect:/evaluationcenter/edit/" + evaluationCenter.getId();
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable Long id, Map<String, Object> model, RedirectAttributes redirectAttrs) {

		boolean exists = evaluationCenterService.existsEvaluationEventRelated(id);
		try {
			if(!exists) {
				evaluationCenterService.delete(id);
				redirectAttrs.addFlashAttribute("successMessage", "Eliminado correctamente");
			} else {
				redirectAttrs.addFlashAttribute("dangerMessage", "No se ha podido eliminar, compruebe que no exista ninguna referencia al centro de evaluación");
			}
		} catch(PersistenceException e) {
			e.printStackTrace();
			log.error("Error al eliminar centro de evaluación");
			redirectAttrs.addFlashAttribute("dangerMessage", "No se ha podido eliminar, compruebe que no exista ninguna referencia a esta entidad");
		}

		return "redirect:/evaluationcenter";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/address", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Address> listAddresses(@PathVariable Long id) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		Set<Address> addresses = evaluationCenter.getAddresses(); // evaluationCenterService.findAddressesByEvaluationCenter(evaluationCenter, 0, 1000);

		return addresses;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/address/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Address createAddress(@PathVariable Long id, @RequestBody Address address) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		address.setEvaluationCenter(evaluationCenter);

		addressService.save(address);

		return address;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/address/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteAddress(@RequestParam String id) {
		addressService.delete(Long.valueOf(id));
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/address/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Address editAddress(@PathVariable Long id, @RequestBody Address address) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		address.setEvaluationCenter(evaluationCenter);
		addressService.update(address);

		return address;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/classroom", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Classroom> listClassrooms(@PathVariable Long id) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		Set<Classroom> classrooms = evaluationCenter.getClassrooms();

		return classrooms;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/classroom/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message createClassroom(@PathVariable Long id, @RequestBody Classroom classroom) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		classroom.setEvaluationCenter(evaluationCenter);

		Message message = new Message();
		classroom.setEvaluationCenter(evaluationCenter);
		boolean exists = classroomService.existsClassroom(classroom.getName(), evaluationCenter.getId());
		if(!exists) {
			classroomService.save(classroom);
			message.setMessage("Creado correctamente");
			message.setType(Message.TYPE_SUCCESS);
			message.setError(0);
			message.setData(classroom);
		} else {
			message.setMessage("Ya existe un aula con ese nombre");
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
		}
		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/classroom/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteClassroom(@RequestParam String id) {
		Message message = new Message();
		message.setMessage("Se ha eliminado con éxito");
		message.setType(Message.TYPE_SUCCESS);
		message.setError(0);
		try {
			classroomService.delete(Long.valueOf(id));
		} catch (Exception ex) {
			message.setMessage("No se ha podido eliminar, compruebe que el aula no está asociada a otra entidad");
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/classroom/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message editClassroom(@PathVariable Long id, @RequestBody Classroom classroom) {

		Message message = new Message();
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		classroom.setEvaluationCenter(evaluationCenter);
		boolean exists = classroomService.existsClassroomUpdate(classroom.getName(), evaluationCenter.getId(), classroom.getId());
		if(!exists) {
			classroomService.update(classroom);
			message.setMessage("Modificado correctamente");
			message.setType(Message.TYPE_SUCCESS);
		} else {
			message.setMessage("Ya existe un aula con ese nombre");
			message.setType(Message.TYPE_ERROR);
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/cap", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Cap> listCaps(@PathVariable Long id) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		Set<Cap> caps = evaluationCenter.getCaps();

		return caps;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/capunassigned", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Cap> listCapsUnassigned(@PathVariable Long id, @RequestParam(required = false) Long capId) {
		List<Cap> caps = capService.findUnassigned(id, capId);

		return caps;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/cap/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Cap createCap(HttpServletResponse response, @PathVariable Long id, @RequestBody Cap cap) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		cap.setEvaluationCenter(evaluationCenter);

		try {
			capService.save(cap);
			return cap;
		} catch (PersistenceException e) {
			try {
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
				response.getOutputStream().write(("{\"error\":\"Ya existe un CAP con el número de serie '" + cap.getSerialNumber() + "'\"}").getBytes("UTF-8"));
				response.flushBuffer();
			}catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/cap/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Cap editCap(HttpServletResponse response, @PathVariable Long id, @RequestBody Cap cap) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		cap.setEvaluationCenter(evaluationCenter);

		try {
			capService.update(cap);
			return cap;
		} catch (PersistenceException e) {
			try {
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
				response.getOutputStream().write(("{\"error\":\"Ya existe un CAP con el número de serie '" + cap.getSerialNumber() + "'\"}").getBytes("UTF-8"));
				response.flushBuffer();
			}catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/cap/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteCap(@RequestParam Long id) {
		capService.delete(id);
	}

	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/centers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Center> listCenters(@PathVariable Long id, @RequestParam Map<String, String> allRequestParams) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		Set<Center> centers = evaluationCenter.getRegistrationCenters();
		return centers;
	}

	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/centers/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteCenter(@PathVariable Long id, @RequestParam Long idcenter) {
		Center center = centerService.findById(idcenter);
		center.setEvaluationCenter(null);
		centerService.update(center);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/allcenters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Center> listAllcenters(@PathVariable Long id) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		List<Center> centers = centerService.findUnassigned();
		Set<Center> eventCenterCenters = evaluationCenter.getRegistrationCenters();
		centers.removeAll(eventCenterCenters);

		return centers;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/centers/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody void addCenters(@PathVariable Long id, @RequestBody List<Center> centers) {
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		for(Center center: centers) {
			center = centerService.findById(center.getId());
			center.setEvaluationCenter(evaluationCenter);
			centerService.update(center);
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/center/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message createCenter(@PathVariable Long id, @RequestBody Center center) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		center.setEvaluationCenter(evaluationCenter);

		Message message = new Message();
		try {
			centerService.save(center);
			message.setMessage("Creado correctamente");
			message.setType("success");
			message.setData(center);
			message.setError(0);
		} catch (PersistenceException e) {
			message.setMessage("El codigo ya existe");
			message.setType("danger");
			message.setError(1);
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/center/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message editCenter(@PathVariable Long id, @RequestBody Center center) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		center.setEvaluationCenter(evaluationCenter);
		Message message = new Message();
		try {
			centerService.update(center);
			message.setMessage("Actualizado correctamente");
			message.setType("success");
			message.setData(center);
			message.setError(0);
		} catch (PersistenceException e) {
			message.setMessage("El codigo ya existe");
			message.setType("danger");
			message.setError(1);
		}

		return message;
	}

	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Net> listNets(@PathVariable Long id) {

		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		Set<Net> nets = evaluationCenter.getNets();

		return nets;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net/new", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message createNet(@PathVariable Long id, @RequestBody ExtendedNet extendedNet) {

		Net net = extendedNet.getNet();
		List<Server> servers = extendedNet.getServers();
		
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);

		net.setEvaluationCenter(evaluationCenter);

		Message message = new Message();
		net.setEvaluationCenter(evaluationCenter);
		boolean exists = netService.existsNet(net.getName(), net.getCode(), evaluationCenter.getId());
		if(!exists) {
			netService.saveNet(net, servers);
			message.setMessage("Creado correctamente");
			message.setType(Message.TYPE_SUCCESS);
			message.setError(0);
			message.setData(net);
		} else {
			message.setMessage("Ya existe una red con ese código");
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
		}
		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteNet(@RequestParam String id) {
		Message message = new Message();
		message.setMessage("Se ha eliminado con éxito");
		message.setType(Message.TYPE_SUCCESS);
		message.setError(0);
		try {
			netService.deleteNet(Long.valueOf(id));
		} catch (Exception ex) {
			message.setMessage("No se ha podido eliminar, compruebe que la red no está asociada a otra entidad");
			message.setType(Message.TYPE_ERROR);
			message.setError(1);
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message editNet(@PathVariable Long id, @RequestBody ExtendedNet extendedNet) {

		Net net = extendedNet.getNet();
		List<Server> servers = extendedNet.getServers();
		Message message = new Message();
		EvaluationCenter evaluationCenter = evaluationCenterService.findById(id);
		net.setEvaluationCenter(evaluationCenter);
		boolean exists = netService.existsNetUpdate(net.getName(), net.getCode(), evaluationCenter.getId(), net.getId());
		if(!exists) {
			netService.updateNet(net, servers);
			message.setMessage("Modificado correctamente");
			message.setType(Message.TYPE_SUCCESS);
		} else {
			message.setMessage("Ya existe una red con ese código");
			message.setType(Message.TYPE_ERROR);
		}

		return message;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net/{netId}/allservers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Server> listAllServers(@PathVariable Long id, @PathVariable Long netId) {
		/*List<Server> servers = serverService.findUnassigned();
		List<Server> netServers = serverService.findByNet(netId);
		if(netServers != null) {
			servers.removeAll(netServers);
		}*/
		
		List<Server> servers = serverService.findAll();

		return servers;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/net/{netId}/selectedservers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Server> listNetServers(@PathVariable Long id, @PathVariable Long netId) {
		//List<Server> netServers = serverService.findByNet(netId);
		Net net = netService.findById(netId);
		List<Server> netServers = new ArrayList<Server>(net.getServers());
		return netServers;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationcenter/edit/{id}/servers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Server> listServers(@PathVariable Long id) {
		List<Server> servers = serverService.findAll();

		return servers;
	}

}
