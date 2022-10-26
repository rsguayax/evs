package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EvaluationEventMatterTestController extends BaseController {

	@Autowired
	private EvaluationEventService evaluationEventService;


	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;
	
	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private TestService testService;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/mattertest", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Temáticas");
		model.put("edit", true);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);

		return "evaluation_event/evaluation-event-matter-test-list";
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/mattertest/{matterId}/test", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody EvaluationEventMatterTest loadTest(@PathVariable Long id, @PathVariable Long matterId) {
		List<EvaluationEventMatterTest> eemt = evaluationEventMatterTestService.findByEvaluationEventMatter(matterId);
		EvaluationEventMatterTest test = null;
		if (eemt.size() > 0) {
			test = eemt.get(0);
		}
		
		return test;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/mattertest/{matterId}/tests", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Test> loadTests(@PathVariable Long id, @PathVariable Long matterId) {
		EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(matterId);
		List<Test> tests = testService.findByBankAndActive(evaluationEventMatter.getBank().getId());
		
		return tests;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/edit/{id}/mattertest/checkallmatterswithtest", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody boolean checkAllMattersWithTest(@PathVariable Long id) {
		List<EvaluationEventMatter> evaluationEventMatters = evaluationEventMatterService.findByEvaluationEvent(id);
		for (EvaluationEventMatter evaluationEventMatter : evaluationEventMatters) {
			List<EvaluationEventMatterTest> eemt = evaluationEventMatterTestService.findByEvaluationEventMatter(evaluationEventMatter.getId());
			if (eemt.size() == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@Transactional
	@RequestMapping(value="/evaluationevent/edit/{id}/mattertest/{matterId}/selecttest", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Message selectTest(@PathVariable Long id, @PathVariable Long matterId, @RequestBody String json) {
		Message responseMessage = new Message();

		try {
			JSONObject formData = new JSONObject(json);
			
			if (formData.has("selectedTestId")) {
				Test selectedTest = testService.findById(formData.getLong("selectedTestId"));
				EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService.findById(matterId);
				
				// Seleccionamos el test antiguo
				List<EvaluationEventMatterTest> evaluationEventMatterTests = evaluationEventMatterTestService.findByEvaluationEventMatter(matterId);
				
				// Si existe, lo actualizamos, en caso contrario, lo creamos
				if (evaluationEventMatterTests.size() > 0) {
					EvaluationEventMatterTest evaluationEventMatterTest = evaluationEventMatterTests.get(0);
					evaluationEventMatterTest.setTest(selectedTest);
					evaluationEventMatterTestService.update(evaluationEventMatterTest);
				} else {
					EvaluationEventMatterTest evaluationEventMatterTest = new EvaluationEventMatterTest();
					evaluationEventMatterTest.setTest(selectedTest);
					evaluationEventMatterTest.setEvaluationEventMatter(evaluationEventMatter);
					evaluationEventMatterTestService.save(evaluationEventMatterTest);
				}
				
				responseMessage.setType(Message.TYPE_SUCCESS);
				responseMessage.setMessage("Test modificado correctamente");
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha producido un error al seleccionar el test: <br /><br />No se ha seleccionado ningún test");
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al seleccionar el test: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}
}
