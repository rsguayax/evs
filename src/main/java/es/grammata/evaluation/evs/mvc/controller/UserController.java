package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class UserController {

	@Autowired 
	private UserService userService;
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping("/user")
	public String userHome(Map<String, Object> model) {
		model.put("headText", "Usuarios");

		return "user/user-list";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'university_center_supporter')")
	@RequestMapping(value="/user/users", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<User> userList() {
		List<User> adminUsers = userService.findByRole(Role.ADMIN);
		adminUsers.addAll(userService.findByRole(Role.STAFF));
		
		return adminUsers;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/user/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message addUser(@RequestBody User user) {
		Message responseMessage = new Message();

		try {
			if (user.getFirstName() != null && user.getFirstName().trim().length() > 0) {
				if (user.getLastName() != null && user.getLastName().trim().length() > 0) {
					if (user.getRoles() != null && !user.getRoles().isEmpty()) {
						user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
						user.setEnabled(1);
						userService.save(user);
						
						responseMessage.setType(Message.TYPE_SUCCESS);
						responseMessage.setMessage("Usuario creado correctamente");
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("Seleccione un rol");
					} 
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca los apellidos");
				} 
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Introduzca un nombre");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al guardar el usuario: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/user/edit", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editUser(@RequestBody User user) {
		Message responseMessage = new Message();

		try {
			if (user.getFirstName() != null && user.getFirstName().trim().length() > 0) {
				if (user.getLastName() != null && user.getLastName().trim().length() > 0) {
					if (user.getRoles() != null && !user.getRoles().isEmpty()) {
						User originalUser = userService.findById(user.getId());
						user.setPassword(originalUser.getPassword());
						userService.update(user);
						
						responseMessage.setType(Message.TYPE_SUCCESS);
						responseMessage.setMessage("Usuario modificado correctamente");
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("Seleccione un rol");
					} 
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("Introduzca los apellidos");
				} 
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Introduzca un nombre");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al guardar el usuario: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/user/edit-password", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message editPassword(@RequestBody String json) {
		Message responseMessage = new Message();

		try {
			JSONObject formData = new JSONObject(json);
			
			if (formData.has("userId") && formData.has("password")) {
				User user = userService.findById(formData.getLong("userId"));
				
				if (user != null) {
					user.setPassword(new BCryptPasswordEncoder().encode(formData.getString("password")));
					userService.update(user);
					
					responseMessage.setType(Message.TYPE_SUCCESS);
					responseMessage.setMessage("Contraseña modificada correctamente");
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("El usuario no existe");
				}
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Faltan parámetros");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al modificar la contraseña del usuario: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/user/{userId}/disable", method=RequestMethod.GET)
	public @ResponseBody Message disableUser(@PathVariable Long userId) {
		Message responseMessage = new Message();

		try {
			User user = userService.findById(userId);
			user.setEnabled(0);
			userService.update(user);

			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Usuario deshabilitado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al deshabilitar el usuario: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/user/{userId}/enable", method=RequestMethod.GET)
	public @ResponseBody Message enableUser(@PathVariable Long userId) {
		Message responseMessage = new Message();

		try {
			User user = userService.findById(userId);
			user.setEnabled(1);
			userService.update(user);
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Usuario habilitado correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al habilitar el usuario: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
}
