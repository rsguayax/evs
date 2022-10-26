package es.grammata.evaluation.evs.mvc.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import siette.models.BancoPreguntas;
import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.Department;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;
import es.grammata.evaluation.evs.data.services.repository.DepartmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.BankListElementReport;
import es.grammata.evaluation.evs.mvc.controller.util.BankListReport;
import es.grammata.evaluation.evs.services.httpservices.client.BankClient;


@Controller
public class ReportsController extends BaseController {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(ReportsController.class);

	@Autowired
	private EvaluationEventService evaluationEventService;
	
	@Autowired
	private MatterService matterService;

	@Autowired
	private BankService bankService;

	@Autowired
	private BankClient bankClient;
	
	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;
	
	@Autowired
	private CorrectionRuleService correctionRuleService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping("/reports")
	public String showMatterList(Map<String, Object> model, @ModelAttribute("successMessage") String successMessage) {
		model.put("headText", "Informes");

		return "evaluation_event_academic/reports";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/reports/evaluationevents", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEvent> loadEvaluationEvents() {
		List<EvaluationEvent> evaluationEvents = evaluationEventService.findAll();
		return evaluationEvents;
	}
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/reports/departments", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Department> loadDepartments() {
		List<Department> departments = departmentService.findAllOrder("name asc");
		return departments;
	}


	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/reports/listado-validacion-bloques-{evaluationEventId}-{departmentId}.pdf", method = RequestMethod.GET)
	public ModelAndView bankListingsPdf(
			@PathVariable Long evaluationEventId,
			@PathVariable Long departmentId) throws Exception {
		
		ModelAndView mav = new ModelAndView("bankListingsPdfView");
		BankListReport blr = this.getListReport(evaluationEventId, departmentId, 1); 
		blr.setTitle("Validación de bloques de bancos");
		
		mav.addObject("bankListReport", blr);

		return mav;
	}
	
	
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/reports/listado-validacion-tests-{evaluationEventId}-{departmentId}.pdf", method = RequestMethod.GET)
	public ModelAndView testListingsPdf(
			@PathVariable Long evaluationEventId,
			@PathVariable Long departmentId) throws Exception {
		
		ModelAndView mav = new ModelAndView("bankListingsPdfView");
		BankListReport blr = this.getListReport(evaluationEventId, departmentId, 2); 
		blr.setTitle("Validación de tests");
		
		
		mav.addObject("bankListReport", blr);

		return mav;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/reports/listado-validacion-bloques-{evaluationEventId}-{departmentId}", method = RequestMethod.GET)
	public void generateCsvReport(@PathVariable Long evaluationEventId,
			@PathVariable Long departmentId, 
			HttpServletResponse response) {

		BankListReport blr = this.getListReport(evaluationEventId, departmentId, 1);

		String csvFileName = "listado-validacion-bloques-" + evaluationEventId + "-" + departmentId + ".csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        ICsvBeanWriter csvWriter = null;
		try {
			csvWriter = new CsvBeanWriter(response.getWriter(),
			        CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);


			String[] header = {"BANCO", "RESPONSABLE", "DEPARTAMENTO", "ASIGNATURA", "P. ACTIVAS",
	        		"P. ENSAYO", "P. INACTIVAS", "P. INGRESADAS",
	        		"P. ACT. NO VALID.", "P. OBJETIVAS", "P.BAJA", "P. ACTIVAS VALIDADAS", "ESTADO"};

			String[] mapping = {"bankName", "manager", "departmentName", "matterName", "activeQuestions",
	        		"ensayQuestions", "inactiveQuestions", "insertQuestions",
        		"noValActiveQuestions", "objQuestions", "unsubscribeQuestions", "valActiveQuestions", "state"};

			if(evaluationEventId != null && evaluationEventId > 0) {
				EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
				csvWriter.writeComment("EVENTO: " + evaluationEvent.getName());
			}
			if(departmentId != null && departmentId > 0) {
				Department department = departmentService.findById(departmentId);
				csvWriter.writeComment("DEPARTAMENTO: " + department.getName());
			}

	        csvWriter.writeHeader(header);

	        if(blr.getElements() != null && blr.getElements().size() > 0) {
		        for(BankListElementReport bl : blr.getElements()) {
		        	csvWriter.write(bl, mapping);
		        }
	        }
		} catch (Exception e) {
			log.error("Al generar CSV");
			e.printStackTrace();
		} finally {
			if(csvWriter != null) {
		        try {
					csvWriter.close();
				} catch (IOException e) {
					log.error("Al cerrar CSV");
					e.printStackTrace();
				}

			}
		}	
	}
	
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/reports/listado-validacion-tests-{evaluationEventId}-{departmentId}", method = RequestMethod.GET)
	public void generateTestssvReport(@PathVariable Long evaluationEventId,
			@PathVariable Long departmentId, 
			HttpServletResponse response) {

		BankListReport blr = this.getListReport(evaluationEventId, departmentId, 2);

		String csvFileName = "listado-validacion-tests-" + evaluationEventId + "-" + departmentId + ".csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        ICsvBeanWriter csvWriter = null;
		try {
			csvWriter = new CsvBeanWriter(response.getWriter(),
			        CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);


			String[] header = { "BANCO", "RESPONSABLE", "DEPARTAMENTO", "ASIGNATURA", "COD. ASIGNATURA", "TEST",
					"EVAL.", "P. TEST", "PUNT.", "P. ACT.",
					"ESTADO"};

			String[] mapping = {"bankName", "manager", "departmentName", "matterName", "matterCode", "testName",
	        		"evaluationType", "testQuestions", "maxRate",
        		"testActiveQuestions", "testState"};

			if(evaluationEventId != null && evaluationEventId > 0) {
				EvaluationEvent evaluationEvent = evaluationEventService.findById(evaluationEventId);
				csvWriter.writeComment("EVENTO: " + evaluationEvent.getName());
			}
			if(departmentId != null && departmentId > 0) {
				Department department = departmentService.findById(departmentId);
				csvWriter.writeComment("DEPARTAMENTO: " + department.getName());
			}

	        csvWriter.writeHeader(header);

	        if(blr.getElements() != null && blr.getElements().size() > 0) {
		        for(BankListElementReport bl : blr.getElements()) {
		        	csvWriter.write(bl, mapping);
		        }
	        }
		} catch (Exception e) {
			log.error("Al generar CSV");
			e.printStackTrace();
		} finally {
			if(csvWriter != null) {
		        try {
					csvWriter.close();
				} catch (IOException e) {
					log.error("Al cerrar CSV");
					e.printStackTrace();
				}

			}
		}	
	}
	
	private BankListReport getListReport(Long evaluationEventId, Long departmentId, int type) {
		

		BankListReport blr = new BankListReport();
		if(evaluationEventId > 0) {
			EvaluationEvent evaluationEvent = this.evaluationEventService
					.findById(evaluationEventId);
			blr.setEvaluationEventName(evaluationEvent.getName());
		} else {
			blr.setEvaluationEventName("");
		}
		if(departmentId > 0) {
			Department department = this.departmentService
					.findById(departmentId);
			blr.setDepartmentName(department.getName());
		} else {
			blr.setDepartmentName("");
		}
		
		blr.setType(type);
		
		if(type == 1) {
			List<EvaluationEventMatter> eems = evaluationEventMatterService.getEvalMattersByEvaluationEventAndBankDepartment(evaluationEventId, departmentId);
			for(EvaluationEventMatter eem : eems) {
				Bank bankTmp = eem.getBank();
				BancoPreguntas banco = bankClient.loadBank(bankTmp.getExternalId());
				BankListElementReport bl = new BankListElementReport();
				bl.setActiveQuestions(banco.getNumeroPreguntasActivas());
				bl.setBankName(bankTmp.getName());
				if(bankTmp.getDepartment() != null) {
					bl.setDepartmentName(bankTmp.getDepartment().getName());
				}
				bl.setEnsayQuestions(banco.getNumeroPreguntasNoObjetivas()); // ENSAYO?
				bl.setInactiveQuestions(banco.getNumeroPreguntasInactivas());
				bl.setInsertQuestions(banco.getNumeroPreguntas()); // INGRESADAS?
				if(banco.getCreador() != null) {
					String firstName = (banco.getCreador().getNombre()==null?"":banco.getCreador().getNombre());
					String lastName = (banco.getCreador().getApellidos()==null?"":banco.getCreador().getApellidos());
					bl.setManager(firstName + " " + lastName);
				}
				bl.setMatterName(eem.getMatter().getName());
				bl.setNoValActiveQuestions(banco.getNumeroPreguntasActivasNoValidadas());
				bl.setObjQuestions(banco.getNumeroPreguntasObjetivas());
				bl.setUnsubscribeQuestions(banco.getNumeroPreguntasNoValidadas()); // DADAS DE BAJA?
				bl.setValActiveQuestions(banco.getNumeroPreguntasActivasValidadas());
				blr.getElements().add(bl);	
			} 
		} else {
			List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.getTestsByEvaluationEventAndBankDepartment(evaluationEventId, departmentId);
			for(EvaluationEventMatterTest eemt : eemts) {
				Bank bankTmp = eemt.getEvaluationEventMatter().getBank();
				Test testTmp = eemt.getTest();
				CorrectionRule correctionRule = correctionRuleService.getEvaluationEventMatterTestCorrectionRule(eemt);
				BancoPreguntas banco = bankClient.loadBank(bankTmp.getExternalId());
				BankListElementReport bl = new BankListElementReport();
				bl.setEvaluationType(testTmp.getEvaluationType().getCode());
				bl.setBankName(bankTmp.getName());
				if(bankTmp.getDepartment() != null) {
					bl.setDepartmentName(bankTmp.getDepartment().getName());
				}
				if(banco.getCreador() != null) {
					String firstName = (banco.getCreador().getNombre()==null?"":banco.getCreador().getNombre());
					String lastName = (banco.getCreador().getApellidos()==null?"":banco.getCreador().getApellidos());
					bl.setManager(firstName + " " + lastName);
				}
				bl.setMatterName(eemt.getEvaluationEventMatter().getMatter().getName());
				bl.setTestQuestions(testTmp.getMaxQuestionNum()); 
				bl.setTestActiveQuestions(bankClient.getUsablesQuestions(testTmp.getExternalId())); 
				bl.setMaxRate(correctionRule.getMaxGrade());
				bl.setMatterCode(eemt.getEvaluationEventMatter().getMatter().getCode());
				bl.setTestName(testTmp.getName());
				
				blr.getElements().add(bl);	
			} 
		}
		
		return blr;
	}


}
