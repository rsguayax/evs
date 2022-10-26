package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.data.services.repository.WeekDayService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.BankEditPeriod;
import es.grammata.evaluation.evs.mvc.controller.util.BankEditTeacherPeriod;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.validator.MatterValidator;
import es.grammata.evaluation.evs.services.httpservices.client.BankClient;
import es.grammata.evaluation.evs.services.restservices.MattersClient;


@Controller
public class MatterBankController extends BaseController {

	@Autowired
	private MatterService matterService;
	
	@Autowired
	MatterValidator matterValidator;

	@Autowired
	private BankService bankService;

	@Autowired
	private BankClient bankClient;

	@Autowired
	private UserService userService;

	@Autowired
	private AcademicLevelService academicLevelService;

	@Autowired
	private TestService testService;
	
	@Autowired
	private MattersClient mattersClient;
	
	@Autowired
	private WeekDayService weekDayService;


	private List<WeekDay> daysAllowed;
	
	private Map<String, WeekDay> weekDaysCache;

	@PostConstruct
	public void init() {
		daysAllowed = loadDays();
		
		//academicPeriods = academicPeriodService.findAll();
		//modes = modeService.findAll();
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping("/bank")
	public String showList(Map<String, Object> model, @ModelAttribute("successMessage") String successMessage) {
		model.put("headText", "Bancos de preguntas");

		List<Bank> banks = bankService.findAll();

		model.put("banks", banks);

		return "evaluation_event_academic/bank-list";
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/banks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Bank> listBanks(@RequestParam boolean showAll) {
		List<Bank> banks = showAll ? bankService.findAll() : bankService.findActive();
		for (Bank bank : banks) {
			Hibernate.initialize(bank.getTests());
		}
		return banks;
	}
	
	@RequestMapping(value="/bank/countactivetestswithoutevaluationtype", method=RequestMethod.GET)
	public @ResponseBody int activeTestsWithoutEvaluationType() {
		return testService.countWithoutEvaluationType();
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/loadexternalbanks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Bank> loadExternalBanks() {
		
		// banks contiene los modificados
		
		bankClient.loadAndCreateBanks();
		List<Bank> allBanks = bankService.findAll();
		
		return allBanks;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}", method=RequestMethod.GET)
	public String showEdit(@PathVariable Long id, Map<String, Object> model) {

		Bank bank = bankService.findById(id);
		model.put("headText", "Edici\u00f3n del banco de preguntas " + bank.getName());
		model.put("bank", bank);
		Map<String,String> states = new LinkedHashMap<String,String>();
		states.put(Bank.STATE_EDIT, "Edicion");
		states.put(Bank.STATE_END, "Finalizado");

		Map<Integer, String> isCompleteopts = new LinkedHashMap<Integer, String>();
		isCompleteopts.put(0, "No Completo");
		isCompleteopts.put(1, "Completo");

		model.put("states", states);
		model.put("isCompleteOpts", isCompleteopts);

		return "evaluation_event_academic/bank-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/*", method=RequestMethod.POST)
	public String edit(@Valid Bank bank, BindingResult bindingResult, Map<String, Object> model, RedirectAttributes redirectAttrs,
			@ModelAttribute("successMessage") String successMessage,  HttpServletRequest request,
	        HttpServletResponse response) {

		if(bindingResult.hasErrors()) {
			return "evaluation_event_academic/bank-form";
		} else {
			bankService.update(bank);
			redirectAttrs.addFlashAttribute("successMessage", "Modificado correctamente");
		}

		return "redirect:/bank";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping("/matter")
	public String showMatterList(Map<String, Object> model, @ModelAttribute("successMessage") String successMessage) {
		model.put("headText", "Asignaturas");

		List<Matter> matters = matterService.findAll();

		model.put("matters", matters);

		return "evaluation_event_academic/matter-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/matters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Matter> listMatters() {

		List<Matter> matters = matterService.findAll();

		return matters;
	}
	
	@RequestMapping(value="/matter/loadmatters", method=RequestMethod.GET)
	public @ResponseBody Message loadMatters() {
		Message responseMessage = new Message();

		try {
			mattersClient.loadAllmatters();
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Asignaturas actualizadas correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al actualizar las asignaturas: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@RequestMapping(value="/matter/assignbanks", method=RequestMethod.GET)
	public @ResponseBody Message assignBanks() {
		Message responseMessage = new Message();

		try {
			List<Matter> mattersWithoutBank = matterService.getMattersWithoutBank();
			for (Matter matter : mattersWithoutBank) {
				List<Bank> banks = bankService.findByName(matter.getName());
				if (banks.size() > 0) {
					matter.setBanks(new HashSet<Bank>(banks));
					matterService.update(matter);
				}
			}
			
			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Bancos de preguntas asignados a las asignaturas correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al asignar los bancos de preguntas a las asignaturas: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
	
	@RequestMapping(value="/matter/countmatterswithoutbank", method=RequestMethod.GET)
	public @ResponseBody int mattersWithoutBank() {
		return matterService.countWithoutBank();
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/new", method = {RequestMethod.POST, RequestMethod.GET})
	public String create(HttpServletRequest request, Map<String, Object> model, @Valid Matter matter, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
		if (request.getMethod().equals("POST")) {
			matterValidator.validate(matter, bindingResult);

			if(!bindingResult.hasErrors()) {
				matterService.save(matter);
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
				return "redirect:/matter/edit/" + matter.getId();
			}		
		} else {
			matter = new Matter();
		}

		model.put("matter", matter);
		model.put("headText", "Nueva asignatura");
		addAuxToModel(model);

		return "evaluation_event_academic/matter-form";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/matter/edit/{id}", method = {RequestMethod.POST, RequestMethod.GET})
	public String edit(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id, @Valid Matter matter, BindingResult bindingResult, RedirectAttributes redirectAttrs, @ModelAttribute("successMessage") String successMessage) {
		if (request.getMethod().equals("POST")) {
			matterValidator.validate(matter, bindingResult);

			if (!bindingResult.hasErrors()) {
				matterService.updateData(matter);
				redirectAttrs.addFlashAttribute("successMessage", "Modificado correctamente");
				return "redirect:/matter";
			}
		} else {
			matter = matterService.findById(id);
		}

		model.put("headText", "Configuraci\u00f3n de la asignatura " + matter.getName());
		model.put("matter", matter);
		addAuxToModel(model);

		return "evaluation_event_academic/matter-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable Long id, Map<String, Object> model, RedirectAttributes redirectAttrs) {
		try {
			matterService.delete(id);
			redirectAttrs.addFlashAttribute("successMessage", "Eliminada correctamente");
		} catch(Exception ex) {
			Throwable t = ex.getCause();
		    while ((t != null) && !(t instanceof ConstraintViolationException)) {
		        t = t.getCause();
		    }
		    
		    if (t instanceof ConstraintViolationException) {
		    	redirectAttrs.addFlashAttribute("dangerMessage", "No se ha podido eliminar, compruebe que la asignatura no esté asignada a algún evento o tenga entidades asociadas.");
		    } else {
		    	redirectAttrs.addFlashAttribute("dangerMessage", "Error al eliminar evento de evaluación");
		    }
		}

		return "redirect:/matter";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/edit/{id}/banks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Bank> listBanks(@PathVariable Long id) {
		Matter matter = matterService.findById(id);
		Set<Bank> banks = matter.getBanks();

		return banks;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/edit/{id}/banks/delete", method=RequestMethod.GET)
	public @ResponseBody void deleteBank(@PathVariable Long id, @RequestParam Long idBank) {
		Matter matter = matterService.findById(id);
		Bank bank = bankService.findById(idBank);
		matter.getBanks().remove(bank);
		matterService.update(matter);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/edit/{id}/allbanks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Bank> listAllBanks(@PathVariable Long id) {
		List<Bank> banks = bankService.findAll();

		return banks;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/matter/edit/{id}/banks/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody void addBanks(@PathVariable Long id, @RequestBody List<Bank> banks) {
		Matter matter = matterService.findById(id);
		matter.getBanks().addAll(banks);
		matterService.update(matter);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping("/bank/loadexternal")
	public String loadExternal(Map<String, Object> model, RedirectAttributes redirectAttrs) {
		model.put("headText", "Bancos de preguntas");

		List<Bank> banks = bankClient.loadAndCreateBanks();

		model.put("banks", banks);

		redirectAttrs.addFlashAttribute("successMessage", "Cargado correctamente");

		return "redirect:/bank";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/test", method=RequestMethod.GET)
	public String showTestList(@PathVariable Long id, Map<String, Object> model) {
		Bank bank = bankService.findById(id);
		model.put("bank", bank);
		model.put("headText", "Tests");

		return "evaluation_event_academic/test-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/test/tests", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Test> listTestsByBank(@PathVariable Long id) {
//		Bank bank = bankService.findById(id);
//		Hibernate.initialize(bank.getTests());

//		// Se listan todos los tests no solo los activos
		List<Test> tests = testService.findByBankOrderByActive(id);

		return tests;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/period/periods", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<BankEditPeriod> listPeriodsByBank(@PathVariable Long id) {
		Bank bank = bankService.findById(id);
		List<BankEditPeriod> periods = bankClient.loadPeriodsBanks(bank);

		return periods;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/teacherperiod/periods", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<BankEditTeacherPeriod> listTeacherPeriodsByBank(@PathVariable Long id) {
		Bank bank = bankService.findById(id);
		List<BankEditTeacherPeriod> periods = bankClient.loadTeachersPeriodsBanks(bank);

		return periods;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/teacherperiod/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addTeacherPeriod(@PathVariable Long id, @RequestBody BankEditTeacherPeriod teacherPeriod) {
		//TODO: Esta pendiente de cambiar el formato de español a internacional para que automaticamente recoja los datos del JSON y los inicialice en el objeto. Esto habrá que hacerlo también en la creación/edición de los periodos globales para el banco
		
		
		Bank bank = bankService.findById(id);
		List<BankEditTeacherPeriod> periods = bankClient.loadTeachersPeriodsBanks(bank);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/period/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void addPeriod(@PathVariable Long id, @RequestBody BankEditPeriod bankPeriod) {
		Bank bank = bankService.findById(id);
		bankClient.addPeriodsBanks(bank, bankPeriod.getInitDate(), bankPeriod.getEndDate());
	}
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/period/modify", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void modifyPeriod(@PathVariable Long id, @RequestBody BankEditPeriod bankPeriod) {	
		Bank bank = bankService.findById(id);
		bankClient.modifyPeriodBank(bankPeriod.getId(), bank.getExternalId(), bankPeriod.getInitDate(), bankPeriod.getEndDate());
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/period/delete", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void deletePeriod(@PathVariable Long id, @RequestBody int bankPeriodId) {	
		Bank bank = bankService.findById(id);
		bankClient.deletePeriodBank(bankPeriodId);
	}


	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/teachers", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<User> unselectedListTeachers(@PathVariable Long id) {
		Bank bank = bankService.findById(id);
		List<User> teachers = userService.findByRole(Role.TEACHER);
		return teachers;
	}

	// TODO al cambiar un test de tipo hay que comprobar qu eno este asignado aun a ningun alumno, si no lo esta se cambia el tipo del test y si hay un
	// evaluationmattertest se elimna??
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'banks_manager')")
	@RequestMapping(value="/bank/edit/{id}/test/{idTest}/edit", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message modifyTest(@PathVariable Long id, @PathVariable Long idTest, @RequestBody Test test) {
		Bank bank = bankService.findById(id);
		test.setBank(bank);
		
		boolean isAssigned = false;
		//boolean isAssigned = evaluationEventMatterTestService.isAssignedTest(test);
		Message message = new Message();
		try {
			if(isAssigned) {
				message.setType("warning");
				message.setMessage("El test ya esta asociado a alguna asignatura, no se puede modificar");
			} else {
				testService.update(test);
				message.setType("success");
				message.setMessage("El test ha sido modificado");
			}
		} catch (Exception e) {
			message.setType(Message.TYPE_ERROR);
			message.setMessage("Se ha producido un error al modificar el test: <br /><br />" + e.getMessage());
		}
		return message;
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
					WeekDay weekDay = MatterBankController.this.weekDaysCache.get(element);
					return weekDay;
				}
				return null;
			}
		});
	}
	

	private void addAuxToModel(Map<String, Object> model) {
		model.put("academicLevels", academicLevelService.findNotDeleted());
		//model.put("academicPeriods", academicPeriods);
		//model.put("modes", modes);
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
}
