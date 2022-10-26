package es.grammata.evaluation.evs.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationTypeService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ModeService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventMatterInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterEvent;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Messages;
import es.grammata.evaluation.evs.mvc.controller.util.Progress;
import es.grammata.evaluation.evs.mvc.controller.util.Report;
import es.grammata.evaluation.evs.mvc.controller.util.Report.Cell;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;

@Controller
public class EvaluationEventMatterController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(EvaluationEventMatterController.class);

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private MatterService matterService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	@Autowired
	private EvaluationClient evaluationClient;

	@Autowired
	private AcademicPeriodService academicPeriodService;

	@Autowired
	private AcademicLevelService academicLevelService;

	@Autowired
	private ModeService modeService;

	@Autowired
	private EvaluationTypeService evaluationTypeService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private BankService bankService;
	
	@Autowired
	private TestService testService;

	private Map<Long, EvaluationType> evaluationTypes = null;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Asignaci\u00f3n de usuarios");
		model.put("edit", true);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);

		model.put("academicPeriods", academicPeriodService.findActive());
		model.put("academicLevels", academicLevelService.findNotDeleted());
		model.put("modes", modeService.findNotDeleted());

		return "evaluation_event/evaluation-event-matter-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventMatterInfo> listMatters(@PathVariable Long id, @RequestParam Map<String, String> allRequestParams) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		List<EvaluationEventMatterInfo> matters = new ArrayList<EvaluationEventMatterInfo>();
		
		if (evaluationEvent.isAdmissionOrComplexiveType()) {
			matters = evaluationEventMatterService.findInfoByAdmissionEvaluationEvent(id);
		} else {
			matters = evaluationEventMatterService.findInfoByEvaluationEvent(id);
		}
		
		return matters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/days", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<WeekDay> getMatterDays(@PathVariable Long id, @RequestParam Long matterId, @RequestParam Map<String, String> allRequestParams) {
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(matterId);
		return evaluationEventMatter.getDaysAllowed();
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/delete", method=RequestMethod.GET)
	public @ResponseBody Message deleteMatter(@PathVariable Long id, @RequestParam Long idMatter) {
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(idMatter);
		Message message = new Message();

		int cont = 0;
		List<MatterTestStudent> mtss = matterTestStudentService.findByMatter(id, idMatter);
		if(mtss != null && mtss.size() > 0) {
			cont++;
		} else if (mtss != null && mtss.size() == 0) {
			try {
				evaluationEventMatterService.deleteAll(id, evaluationEventMatter);
			} catch (Exception ex) {
				message.setMessage("Se ha producido un error");
				message.setType("danger");
				ex.printStackTrace();
				log.error("Eliminando asociación asignatura evento: " + ex.getMessage());
				return message;
			}
		}

		if(cont > 0) {
			message.setMessage("Algunas asignaturas tienen estudiantes con tests asociados");
			message.setType("warning");
		} else {
			message.setMessage("Eliminadas correctamente");
			message.setType("success");
		}

		return null;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/allmatters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Matter> listAllMatters(@PathVariable Long id) {
		List<Matter> matters = matterService.findAll();
		//List<Matter> eventMatters = matterService.getMattersByEvaluationEvent(id, false);
		//matters.removeAll(eventMatters);

		return matters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody void addMatters(@PathVariable Long id, @RequestBody MatterEvent matterEvent, HttpServletRequest request) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);

		if(matterEvent.getModeAdd() != null && matterEvent.getAcademicPeriodAdd() != null
				&& !matterEvent.getModeAdd() .equals("") && !matterEvent.getAcademicPeriodAdd().equals("")) {
			AcademicPeriod academicPeriod = academicPeriodService.findByCode(matterEvent.getAcademicPeriodAdd());
			Mode mode = modeService.findByCode(matterEvent.getModeAdd());
			for(Matter matter : matterEvent.getMatters()) {
				EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findByUnique(id, matter.getId(), academicPeriod.getId(), mode.getId());
				if(evaluationEventMatter == null) {
					evaluationEventMatter = new EvaluationEventMatter(evaluationEvent, matter, academicPeriod, mode);
					Set<EvaluationType> matterTypes = new HashSet<EvaluationType>();
					evaluationEventMatter.setEvaluationTypes(matterTypes);
					evaluationEventMatter.getEvaluationTypes().addAll(evaluationEvent.getEvaluationTypes());
					Set<WeekDay> daysAllowed = matter.getDaysAllowed();
					if(daysAllowed != null && daysAllowed.size() > 0) {
						evaluationEventMatter.setDaysAllowed(daysAllowed);
					}
					evaluationEventMatterService.save(evaluationEventMatter);
				}
			}
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter/{matterId}/days/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody Message addDays(@PathVariable Long id, @PathVariable Long matterId, @RequestBody List<WeekDay> daysAllowed) {

		// TODO antes de añadir los dias comprobar que no hay test ya asociados

		Message message = new Message();
		message.setMessage("Se han añadido los días correctamente");
		message.setType(Message.TYPE_SUCCESS);
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(matterId);
		try {
			evaluationEventMatter.setDaysAllowed(new HashSet<WeekDay>());
			evaluationEventMatter.getDaysAllowed().addAll(daysAllowed);
			evaluationEventMatterService.update(evaluationEventMatter);
			message.setData(evaluationEventMatter.getMatter());
		} catch(Exception ex) {
			message.setMessage("Se ha producido un error");
			message.setType(Message.TYPE_ERROR);
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/{idMatter}/extrascore", method=RequestMethod.POST)
	public @ResponseBody void changeExtraScore(@PathVariable Long id, @PathVariable Long idMatter, HttpServletRequest request) {
		//EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(idMatter);
		//EvaluationEventMatter evaluationEventMatter = new EvaluationEventMatter(evaluationEvent, matter);
		evaluationEventMatter = evaluationEventMatterService.findById(evaluationEventMatter.getId());
		String extraScore = request.getParameter("extraScore");
		evaluationEventMatter.setExtraScore(Float.valueOf(extraScore.replace(',', '.')));
		evaluationEventMatterService.update(evaluationEventMatter);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter/{idMatter}/banks", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<Bank> listMatterBanks(@PathVariable Long id, @PathVariable Long idMatter, @RequestParam Map<String, String> allRequestParams) {
		EvaluationEventMatter evaluationeventMatter = evaluationEventMatterService.findById(idMatter);
		Set<Bank> banks = evaluationeventMatter.getMatter().getBanks();
		return banks;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter/{idMatter}/banks/selected", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message selectBank(@PathVariable Long id, @PathVariable Long idMatter, @RequestBody Bank bank,
			@RequestParam Map<String, String> allRequestParams) {
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(idMatter);
		
		bank = bankService.findById(bank.getId());

		Message message = new Message();
		message.setMessage("Asociado correctamente");
		message.setType(Message.TYPE_SUCCESS);
		message.setError(0);

		// MODIFICADO: usando test activos
		List<Test> activeTests = testService.findByBankAndActive(bank.getId());
		boolean success = bankService.checkTests(bank) && activeTests != null && activeTests.size() > 0;

		if (success) {
			if (bank != null && bank.getId() != null) {
				evaluationEventMatter.setBank(bank);
				evaluationEventMatterService.update(evaluationEventMatter);
			} else {
				message.setMessage("Se ha producido un error");
				message.setType(Message.TYPE_ERROR);
				message.setError(1);
			}
		} else {
			message.setMessage("El banco no tiene tests activos o existen tests activos asociados al banco que no tienen tipo de evaluación");
			message.setType(Message.TYPE_WARNING);
			message.setError(1);
		}


		return message;

		/*if (bank == null) {
			Set<Bank> banks = matter.getBanks();
			if (banks == null || banks.size() == 0) {
				log.warn("La asignatura " + matter.getId() + " no tiene banco asociado");
			} else if (banks.size() == 1) {
				evaluationEventMatter.setBank(banks.iterator().next());
				evaluationEventMatterService.update(evaluationEventMatter);
			} else {
				log.warn("La asignatura " + matter.getId() + " no tiene banco seleccionado para el evento");
			}
		}*/
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter/{idMatter}/banks/loadselected", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Bank loadSelectedBank(@PathVariable Long id, @PathVariable Long idMatter,
			@RequestParam Map<String, String> allRequestParams) {
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(idMatter);

		return evaluationEventMatter.getBank();
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/loadmatters", method=RequestMethod.GET)
	public @ResponseBody Message loadWSMatters(@PathVariable Long id, HttpServletRequest request) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);

		String modeLoad = request.getParameter("modeLoad");
		String academicPeriodLoad = request.getParameter("academicPeriodLoad");
		String academicLevelLoad = request.getParameter("academicLevelLoad");

		Message message = evaluationClient.loadComponents(evaluationEvent.getCode(), academicPeriodLoad, modeLoad, academicLevelLoad, true);

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/loadmatterspr", method=RequestMethod.GET)
	public @ResponseBody Progress mattersProgress(@PathVariable Long id, HttpServletRequest request) {
		Progress progress = new Progress();
		progress.setRunning((evaluationClient.RUNNING?1:0));
		progress.setProgress(evaluationClient.COMPONENT_PROGRESS);
		progress.setMessage(evaluationClient.MESSAGE);
		progress.setMessage_type(evaluationClient.MESSAGE_TYPE);
		return progress;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/loadmattersreset", method=RequestMethod.GET)
	public @ResponseBody void loadReset(@PathVariable Long id, HttpServletRequest request) {
		evaluationClient.RUNNING = false;
		evaluationClient.COMPONENT_PROGRESS = 0;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/deletematters", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Message deleteMatters(@PathVariable Long id, HttpServletRequest request) {
		List<EvaluationEventMatter> evaluationEventMatters = evaluationEventMatterService.findByEvaluationEvent(id);
		Message message = new Message();
		int cont = 0;
		for(EvaluationEventMatter evaluationEventMatter : evaluationEventMatters) {
			List<MatterTestStudent> eams = matterTestStudentService.findByMatter(id, evaluationEventMatter.getMatter().getId());
			if(eams != null && eams.size() > 0) {
				cont++;
			} else if (eams != null && eams.size() == 0) {
				try {
					evaluationEventMatterService.deleteAll(id, evaluationEventMatter);
				} catch (Exception ex) {
					message.setMessage("Se ha producido un error");
					message.setType("danger");
					ex.printStackTrace();
					log.error("Eliminado asociación asignatura evento: " + ex.getMessage());
					return message;
				}
			}
		}

		if(cont > 0) {
			message.setMessage("Algunas asignaturas tienen estudiantes con tests asociados");
			message.setType("warning");
		} else {
			message.setMessage("Eliminadas correctamente");
			message.setType("success");
		}


		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/evaluationtypes", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<EvaluationType> listEvaluationEventTypes(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<EvaluationType> types = evaluationEvent.getEvaluationTypes();
		return types;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matter/{idMatter}/evaluationtypes/selected", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationType> listEvaluationEventTypes(@PathVariable Long id, @PathVariable Long idMatter, @RequestParam Map<String, String> allRequestParams) {
		List<Long> ids = evaluationEventMatterService.getEvaluationTypes(idMatter);
		List<EvaluationType> types = new ArrayList<EvaluationType>();
		for(Long evId : ids) {
			types.add(geEvaluationType(evId));
		}
		return types;
	}

	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/matters/{idMatter}/evaluationtypes/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody Messages addEvaluationTypes(@PathVariable Long id, @PathVariable Long idMatter, @RequestBody List<EvaluationType> types) {
		Messages messages = new Messages();
		EvaluationEventMatter evm = evaluationEventMatterService.findById(idMatter);
		Set<EvaluationType> ets = evm.getEvaluationTypes();

		Set<EvaluationType> typesSet = new HashSet<EvaluationType>(types);
		List<EvaluationType> resTypes = new ArrayList<EvaluationType>();
		for(EvaluationType et : ets) {
			if(!typesSet.contains(et)) {
				// comprobar si tiene mts antes de quitarlo
				Long mtssCount = matterTestStudentService.countByEvaluationEventMatterTestEvaluationType(evm.getId(), et.getId());
				if(mtssCount > 0) {
					// no se puede eliminar ya tiene mtss asociados
					Message message = new Message();
					message.setMessage("No se puede suprimir el tipo: " + et.getCode() + " ya hay alumnos con tests asociados");
					message.setType(Message.TYPE_WARNING);
					messages.getMessages().add(message);
					types.add(et);
				}
			}
		}
		
		evm.getEvaluationTypes().clear();
		evaluationEventMatterService.update(evm);

		// para cada tipo nuevo crear los evaluationmattertest
		evm.getEvaluationTypes().addAll(types);
		evaluationEventMatterService.update(evm);
		
		Message message = new Message();
		message.setMessage("Modificación de tipos finalizada");
		message.setType(Message.TYPE_SUCCESS);
		messages.getMessages().add(message);
		
		
		return messages;
	}

	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/matters/listado-asignaturas-banco-{eventId}-{currentDate}.pdf", method = RequestMethod.GET)
	public ModelAndView matterBankReport(@PathVariable Long id, @PathVariable Long eventId, @PathVariable String currentDate) {
		EvaluationEvent ev = evaluationEventService.findById(id);
		
		Report report = new Report();
		report.setTitle("Banco por asignatura - " + ev.getName());
		List<String> headers = new ArrayList<String>();
		headers.add("Asignatura");
		headers.add("Per. académ.");
		headers.add("Modalidad");
		headers.add("Banco");
		headers.add("Nº preg.");
		headers.add("Estado");
		report.setHeaders(headers);
		
		report.setColumnWidths(new float[] {30f, 15f, 15f, 30f, 10f, 15f});

		List<Report.Cell> cells = new ArrayList<Report.Cell>();

		List<EvaluationEventMatter> evaluationEventMatters = evaluationEventMatterService.findByEvaluationEvent(id);

		for(EvaluationEventMatter evaluationEventMatter : evaluationEventMatters) {
			Cell cell = report.new Cell();
			List<String> cols = new ArrayList<String>();
			cols.add(evaluationEventMatter.getMatter().getName());
			cols.add(evaluationEventMatter.getAcademicPeriod().getName());

			cols.add(evaluationEventMatter.getMode().getName());

			if(evaluationEventMatter.getBank() != null) {
				cols.add(evaluationEventMatter.getBank().getName());
				cols.add(evaluationEventMatter.getBank().getQuestionNumber().toString());
				cols.add(evaluationEventMatter.getBank().getState());	
			} else {
				// no mostramos las que no tienen banco
				continue;
				//cols.add("-Sin banco-");
				//cell.setColor(Report.HIGHLIGHT_COLOR);
			}

			cell.setCols(cols);
			cells.add(cell);

		}

		report.setCells(cells);

		return new ModelAndView("PdfRevenueSummary", "report", report);

	}



	private EvaluationType geEvaluationType(Long id) {
		if(evaluationTypes == null) {
			List<EvaluationType> typesList = evaluationTypeService.findAll();
			evaluationTypes = new HashMap<Long, EvaluationType>();
			for(EvaluationType et : typesList) {
				evaluationTypes.put(et.getId(), et);
			}
		}

		return evaluationTypes.get(id);
	}
}
