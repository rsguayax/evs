package es.grammata.evaluation.evs.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.TestType;
import es.grammata.evaluation.evs.data.services.repository.EvaluationTypeService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.TestTypeService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Messages;
import es.grammata.evaluation.evs.services.httpservices.client.BankClient;
import es.grammata.evaluation.evs.services.httpservices.wrapper.TestWrap;
import es.grammata.evaluation.evs.services.restservices.CentrosUniversitariosClient;
import es.grammata.evaluation.evs.services.restservices.ModalidadClient;
import es.grammata.evaluation.evs.services.restservices.NivelAcademicoClient;
import es.grammata.evaluation.evs.services.restservices.PeriodosAcademicosClient;


@Controller
public class AuxController extends BaseController {

	@Autowired
	private EvaluationTypeService evaluationTypeService;
	
	@Autowired
	private TestTypeService testTypeService;

	@Autowired
	private ModalidadClient modalidadClient;

	@Autowired
	private NivelAcademicoClient nivelAcademicoClient;

	@Autowired
	private PeriodosAcademicosClient periodosAcademicosClient;

	@Autowired
	private BankClient bankClient;

	@Autowired
	private TestService testService;

	@Autowired
	private CentrosUniversitariosClient centrosUniversitariosClient;



	@RequestMapping("/aux/loadevaluationtypes")
	public @ResponseBody List<EvaluationType> loadEvaluationTypes() {

		List<EvaluationType> evaluationTypes = evaluationTypeService.findAll();

		return evaluationTypes;
	}

	@RequestMapping("/aux/loadtesttypes")
	public @ResponseBody List<TestType> loadTestTypes() {

		List<TestType> testTypes = testTypeService.findAll();

		return testTypes;
	}

	@RequestMapping(value = "/aux/getResults", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody List<TestWrap> getTest() {
		Test test = testService.findByExternalId(125);


		return bankClient.getResults(test, 125L);
	}

	@RequestMapping(value = "/aux/getTestStudent", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody TestWrap getTestStudent() {
		Test test = testService.findByExternalId(85);


		return bankClient.getResultsStudent(test, "cdbernal");
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/aux/updateauxdata", method = RequestMethod.GET)
	public @ResponseBody Messages updateAuxEntities() {
		Messages messages = new Messages();
		boolean error = false;
		try {
			modalidadClient.loadAndCreateModes();
		} catch (Exception ex) {
			Message message = new Message();
			message.setMessage("Error al obtener modos académicos");
			message.setType(Message.TYPE_ERROR);
			messages.getMessages().add(message);
			error = true;
		}
			
		try {
			nivelAcademicoClient.loadAndCreateLevels();		
		} catch (Exception ex) {
			Message message = new Message();
			message.setMessage("Error al obtener niveles académicos");
			message.setType(Message.TYPE_ERROR);
			messages.getMessages().add(message);
			error = true;
		}
		
		try {
			periodosAcademicosClient.loadAndCreatePeriods();			
		} catch (Exception ex) {
			Message message = new Message();
			message.setMessage("Error al obtener períodos académicos");
			message.setType(Message.TYPE_ERROR);
			messages.getMessages().add(message);
			error = true;
		}
		
		if (!error) {
			Message message = new Message();
			message.setMessage("Actualización realizada correctamente");
			message.setType(Message.TYPE_SUCCESS);
			messages.getMessages().add(message);
		}
		
		return messages;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/aux/updatecenters", method = RequestMethod.GET)
	public @ResponseBody int updateCenters() {
		try {
			centrosUniversitariosClient.loadAndCreateCenters();
		} catch (Exception ex) {
			return 0;
		}

		return 1;
	}

}
