package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;

@Controller
public class EvaluationEventAdminController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private UserService userService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/admin", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("edit", true);
		model.put("evaluationEvent", evaluationEvent);
		return "evaluation_event/evaluation-event-admin-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/admin/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<User> listAdmins(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<User> admins = evaluationEvent.getAdmins();

		return admins;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/admin/unselectedlist", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<User> unselectedListAdmins(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<User> selectedAdmins = evaluationEvent.getAdmins();
		List<User> unselectedAdmins = userService.findByRole(Role.EVENT_ADMIN);
		unselectedAdmins.removeAll(selectedAdmins);
		return unselectedAdmins;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/admin/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addAdmins(@PathVariable Long id, @RequestBody List<User> admins) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);

		for (User admin : admins) {
			evaluationEvent.getAdmins().add(userService.findById(admin.getId()));
		}

		evaluationEventService.update(evaluationEvent);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/admin/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteAdmin(@PathVariable Long id, @RequestParam Long adminId) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		User admin = userService.findById(adminId);
		evaluationEvent.getAdmins().remove(admin);
		evaluationEventService.update(evaluationEvent);
	}
}
