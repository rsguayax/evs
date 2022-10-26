package es.grammata.evaluation.evs.mvc.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.grammata.evaluation.evs.data.model.repository.AdmissionGrade;
import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Session;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.AdmissionGradeService;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.SessionService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationReport;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.Messages;
import es.grammata.evaluation.evs.mvc.controller.util.ResultEvalutionReportRequest;
import es.grammata.evaluation.evs.mvc.controller.util.ResultReport;
import es.grammata.evaluation.evs.services.httpservices.client.BankClient;
import es.grammata.evaluation.evs.services.restservices.EvaluationClient;
import es.grammata.evaluation.evs.util.StringUtil;
import es.grammata.evaluation.evs.util.StudentResultsMailer;
import org.siette.api.EvsService;
import org.siette.api.domain.RealizedTestSummary;

@Controller
//@PropertySource("classpath:/spring/appServlet/props/application.properties")
public class EvaluationEventResultsController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(EvaluationEventResultsController.class);

	@Autowired
	public EvaluationEventMatterTestService evaluationEventMatterTestService;

	@Autowired
	public EvaluationEventService evaluationEventService;

	@Autowired
	private BankClient bankClient;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private AdmissionGradeService admissionGradeService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;
	
	@Autowired
	private CorrectionRuleService correctionRuleService;
	
	//@Autowired
	//private Environment env;

	@Autowired
	private StudentResultsMailer mailer;
	
	@Autowired
	private EvaluationClient evaluationClient;
	
	@Autowired
	private EvsService evsService;

	@Autowired
	public UserService userService;
	
	@Value("${siette.url}")
	private String sietteUrl;
	
	private static HashMap<Integer, String> publishWSResultCodes = null;
	
	static {
		publishWSResultCodes = new HashMap<Integer, String>();
		publishWSResultCodes.put(0, "Usuario incorrecto");
		publishWSResultCodes.put(1, "Procesamiento exitoso");
		publishWSResultCodes.put(2, "Periodo incorrecto");
		publishWSResultCodes.put(3, "Identificacion incorracta");
		publishWSResultCodes.put(4, "Componente incorrecto 	");	 
		publishWSResultCodes.put(5, "Periodo Guid incorrecto");
		publishWSResultCodes.put(6, "Estudiante Guid incorrecto"); 
		publishWSResultCodes.put(7, "Paralelo incorrecto"); 
		publishWSResultCodes.put(8, "Item Evaluación incorrecto"); 
		publishWSResultCodes.put(9, "Resultado incorrecto ");
		publishWSResultCodes.put(10, "Determínate publicar nota incorrecto"); 
		publishWSResultCodes.put(500, "Error interno del servidor");
		publishWSResultCodes.put(50, "Se requiere valores en parámetros obligatorios");
		publishWSResultCodes.put(1001, "El test no tiene tipo de evaluación asignado");
	}
	
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'results_manager')")
	@RequestMapping(value="/evaluationevent/results/{id}/publish", method=RequestMethod.GET)
	public String showList(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Publicaci\u00f3n de resultados");
		model.put("evaluationEvent", evaluationEventService.findById(id));

		return "evaluation_event/results/publish";
	}

	@RequestMapping(value = "/evaluationevent/results/{id}/tests")
	public @ResponseBody List<EvaluationEventMatterTest> listTest(@PathVariable Long id, @RequestParam(required=false) Long eemid) {
		
		List<EvaluationEventMatterTest> eemts = new ArrayList<EvaluationEventMatterTest>();
		if(eemid != null) {
			eemts = evaluationEventMatterTestService.findByEvaluationEventMatter(eemid);
		} else {
			eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		}

		return eemts;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'results_manager')")
	@RequestMapping(value = "/evaluationevent/results/{id}/publish", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody Messages publish(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> selected) {

		Messages messages = new Messages();

		boolean error = false;
		String messageText = "";
		for(EvaluationEventMatterTest eemt : selected) {
			// podriamos no cargarlo y coger los datos que viene del listado pero podria pasar que no
			// estuviesen actualizados aunque la posibilidad sea remota
			eemt = evaluationEventMatterTestService.findById(eemt.getId());

			// solo se pueden publicar los evaluables y los recalificados, los demas no tendria sentido haccerlo de nuevo.
			// el estado en espera debe ser calificado antes
			if((eemt.getState().equals(eemt.STATE_EVALUABLE) || eemt.getState().equals(eemt.STATE_MODIFIED)) && eemt.getEnablePublish() == 1) {
				//obtener todos los mts de este test
				try {
					List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTest(eemt, true);
					for(MatterTestStudent mts : mtss) {

						// cambiar el estado de sus sesion a publicado y obtener los datos de la sesion
						// para cada mts obtener sesion y cambiar estado a PUBLICADO
						// si ya estaba publicado no notificamos de nuevo
						Session session = mts.getSession();
						if(!session.getState().equals(Session.STATE_PUBLISHED)) {

							EvaluationAssignmentMatter eam = mts.getEvaluationAssignmentMatter();
							User user = eam.getEvaluationAssignment().getUser();

							Integer res = 0;
							if(eemt.getTest().getEvaluationType() != null) {							
								// Peticion a sistema academico para informar de la publicacion
								res = evaluationClient.publishResult(user.getUsername(), 
									eam.getEvaluationEventMatter().getAcademicPeriod().getCode(), 
									user.getIdentification(), eam.getEvaluationEventMatter().getMatter().getCode(), 
									eam.getEvaluationEventMatter().getAcademicPeriod().getExternalId(), 
									user.getExternalId(), "", eemt.getTest().getEvaluationType().getCode(), String.valueOf(session.getRate()), "true");
							} else {
								res = 1001;
							}
							
							if(res != 1) {
								Message message = new Message();
								messageText = "Error al publicar en SGA - Usuario: " + user.getUsername() +
										"; test: " + eemt.getTest().getName()  + " - Respuesta: " + publishWSResultCodes.get(res);
								message.setMessage(messageText);
								message.setType(Message.TYPE_ERROR);
								messages.getMessages().add(message);
								error = true;
							} else {
								session.setState(Session.STATE_PUBLISHED);
								sessionService.update(session);
							}
						}
					}

					// cambiar estado de eemt a PUBLICADO
					if(!error) {
						eemt.setState(EvaluationEventMatterTest.STATE_PUBLISHED);
						evaluationEventMatterTestService.update(eemt);
					}

				} catch(Exception ex) {
					Message message = new Message();
					messageText = "Error al publicar la asignatura: " + eemt.getEvaluationEventMatter().getMatter().getCode() +
							" y el test: " + eemt.getTest().getName();
					message.setMessage(messageText);
					message.setType(Message.TYPE_ERROR);
					messages.getMessages().add(message);
					error = true;
				}
			} else {
				Message message = new Message();
				messageText = "En la asignatura " + eemt.getEvaluationEventMatter().getMatter().getCode() + " el test " + eemt.getTest().getName() + 
						" no tiene el estado adecuado o no esta habilitado para publicar";
				message.setMessage(messageText);
				message.setType(Message.TYPE_WARNING);
				messages.getMessages().add(message);
				error = true;
			}
		}
		
		if(!error) {
			Message message = new Message();
			message.setMessage("Asignaturas publicadas correctamente");
			message.setType(Message.TYPE_SUCCESS);
			messages.getMessages().add(message);

		}

		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		messages.setData(eemts);

		return messages;
	}
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'results_manager')")
	@RequestMapping(value = "/evaluationevent/results/{id}/enablepublish", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody Message enablePublish(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> selected) {

		Message message = new Message();
		message.setMessage("Asignaturas publicadas correctamente");
		message.setType(Message.TYPE_SUCCESS);


		for(EvaluationEventMatterTest eemt : selected) {
			// podriamos no cargarlo y coger los datos que viene del listado pero podria pasar que no
			// estuviesen actualizados aunque la posibilidad sea remota
			eemt = evaluationEventMatterTestService.findById(eemt.getId());
			eemt.setEnablePublish(1);
			evaluationEventMatterTestService.update(eemt);
		}

		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		message.setData(eemts);

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value = "/evaluationevent/results/{id}/qualify", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody Message qualify(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> selected) {
		Message message = new Message();
		message.setMessage("Asignaturas calificadas correctamente");
		message.setType(Message.TYPE_SUCCESS);
		
		// Obtenemos el evento de evaluación
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);

		int error = 0;
		for(EvaluationEventMatterTest eemt : selected) {
			try {
				// podriamos no cargarlo y coger los datos que viene del listado pero podria pasar que no
				// estuviesen actualizados aunque la posibilidad sea remota
				eemt = evaluationEventMatterTestService.findById(eemt.getId());
				// obtenemos el test
				Test test = eemt.getTest();

				// obtenemos para ese test todos los tests realizados
				CorrectionRule correctionRule = correctionRuleService.getEvaluationEventMatterTestCorrectionRule(eemt);
				org.siette.api.domain.CorrectionRule sietteCorrectionRule = correctionRule.getType().equals("UTPL_RULE") ? org.siette.api.domain.CorrectionRule.UTPL_RULE : org.siette.api.domain.CorrectionRule.CANONICAL_RULE;
				List<RealizedTestSummary> realizedTests = evsService.getRealizedTests(Long.valueOf(test.getExternalId()), sietteCorrectionRule, correctionRule.getMinGrade(), correctionRule.getMaxGrade());
				
				// para cada test realizado obtener el materteststudent y su sesion
				for(RealizedTestSummary testSummary : realizedTests) {
					User user = userService.findByUsername(testSummary.getStudentUsername());
					
					if (user != null) {
						EvaluationAssignmentMatter eam = evaluationAssignmentMatterService.find(id, user.getId(), eemt.getEvaluationEventMatter().getId());
						
						if (eam != null) {
							MatterTestStudent mts = matterTestStudentService.findByUnique(eemt, eam);
							Session session = new Session();
							session.setnBlanks(testSummary.getBlankAnswerCount());
							session.setnFails(testSummary.getIncorrectAnswerCount());
							session.setnSuccesses(testSummary.getCorrectAnswerCount());
							session.setUrlToken(testSummary.getToken());
							session.setExternalId(testSummary.getId());
							session.setTestDate(testSummary.getStarted());
							session.setRate((float) testSummary.getGrade());
	
							Float maxRate = correctionRule.getMaxGrade();
							Float result = session.getRate();
	
							Session sessionOld = mts.getSession();
							if(sessionOld != null) {
								// si es menor hay que actualizarla, si no es menor lo dejamos como estaba
								if(sessionOld.getRate() < session.getRate()) {
									sessionOld.setnBlanks(testSummary.getBlankAnswerCount());
									sessionOld.setnFails(testSummary.getIncorrectAnswerCount());
									sessionOld.setnSuccesses(testSummary.getCorrectAnswerCount());
									sessionOld.setRate((float) testSummary.getGrade());
									sessionOld.setState(Session.STATE_EVALUABLE);
									// si ya ha sido publicado o notificado hay que marcar la session y todo el test como recalificado
									if(!(eemt.getState() == null || eemt.getState().equals(EvaluationEventMatterTest.STATE_WAITING) || eemt.getState().equals(EvaluationEventMatterTest.STATE_EVALUABLE))) {
										eemt.setState(EvaluationEventMatterTest.STATE_MODIFIED);
										sessionOld.setState(Session.STATE_MODIFIED);
									} else {
										eemt.setState(EvaluationEventMatterTest.STATE_EVALUABLE);
									}
									sessionService.update(sessionOld);
								}
							} else {
								// si es nueva sesion comprobamos si se le ha de aplicar la nota adicional
								if(eemt.getExtraScoreApplied() > 0 && eemt.getExtraScore() != null) {
									if((result + eemt.getExtraScore() < maxRate)) {
										session.setRate(result + eemt.getExtraScore());
									} else {
										session.setRate(maxRate);
									}
								}
	
								eemt.setState(EvaluationEventMatterTest.STATE_EVALUABLE);
								session.setState(Session.STATE_EVALUABLE);
								sessionService.save(session);
								mts.setSession(session);
								matterTestStudentService.update(mts);
							}
							
							// Si el evento de evaluación es de admisión o complexivo, obtenemos la nota de admisión
							if (evaluationEvent.isAdmissionOrComplexiveType()) {
								AdmissionGrade admissionGrade = mts.getAdmissionGrade();
								
								if (admissionGrade == null) {
									admissionGrade = new AdmissionGrade();
									admissionGrade.setCreated(new Date());
									admissionGrade.setEvaluationEvent(evaluationEvent);
									admissionGrade.setUser(user);
									admissionGrade.setMatter(mts.getEvaluationEventMatterTest().getEvaluationEventMatter().getMatter());
									admissionGrade.setGrade(testSummary.getGrade());
									admissionGradeService.save(admissionGrade);
									
									mts.setAdmissionGrade(admissionGrade);
									matterTestStudentService.update(mts);
								}
							}
						}
					}
				}
				
				evaluationEventMatterTestService.update(eemt);
			} catch(Exception ex) {
				String messageText = message.getMessage();
				if(message.getType().equals(Message.TYPE_SUCCESS)) {
					messageText = "";
				}
				messageText += "- Error al publicar la asignatura: " + eemt.getEvaluationEventMatter().getMatter().getCode() +
						" y el test: " + eemt.getTest().getName();
				
				if(error == 1) {
					messageText += " - El test no tiene 'Tipo de test' asociado.";
				}
				
				messageText += "<br>";
				
				message.setMessage(messageText);
				message.setType(Message.TYPE_ERROR);
			}
		}
		
		if (evaluationEvent.isAdmissionOrComplexiveType()) {
			evaluationEvent.setState(EvaluationEvent.STATE_QUALIFIED);
			evaluationEventService.update(evaluationEvent);
		}

		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		message.setData(eemts);

		return message;
	}




	@RequestMapping(value = "/evaluationevent/results/{id}/extrascoreapplied", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody List<EvaluationEventMatterTest> addExtraScore(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> selected) {

		for(EvaluationEventMatterTest eemt : selected) {
			// podriamos no cargarlo y coger los datos que viene del listado pero podria pasar que no
			// estuviesen actualizados aunque la posibilidad sea remota
			eemt = evaluationEventMatterTestService.findById(eemt.getId());
			CorrectionRule correctionRule = correctionRuleService.getEvaluationEventMatterTestCorrectionRule(eemt);
			
			if(correctionRule != null) {
				Float maxRate = correctionRule.getMaxGrade();
					
				if(eemt.getExtraScoreApplied() == 0) {
					// si el estado es en espera se deja como esta
					if(eemt.getState().equals(EvaluationEventMatterTest.STATE_WAITING)) {
					} else {
						List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTest(eemt, false);
						for(MatterTestStudent mts : mtss) {
							Session session = mts.getSession();
							Float rate = session.getRate();
	
							Float extraScore = eemt.getExtraScore();
							if((rate + extraScore < maxRate)) {
								rate += extraScore;
							} else {
								rate = maxRate;
							}
	
							session.setRate(rate);
							// si es publicado o notificado marcamos como recalificado
							if(eemt.getState().equals(EvaluationEventMatterTest.STATE_PUBLISHED) || eemt.getState().equals(EvaluationEventMatterTest.STATE_NOTIFIED)) {
								session.setState(Session.STATE_MODIFIED);
								eemt.setState(EvaluationEventMatterTest.STATE_MODIFIED);
							}
							sessionService.update(session);
						}
					}
				}
	
				eemt.setExtraScoreApplied(1);
				evaluationEventMatterTestService.update(eemt);
			}
		}

		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		return eemts;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'results_manager')")
	@RequestMapping(value="/evaluationevent/results/{id}/matters", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<EvaluationEventMatter> listMatters(@PathVariable Long id) {
		List<EvaluationEventMatter> matters = evaluationEventMatterService.findByEvaluationEventWithBank(id);

		return matters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'results_manager')")
	@RequestMapping(value="/evaluationevent/results/{id}/tests/{idTest}/extrascore", method=RequestMethod.POST)
	public @ResponseBody Message changeExtraScore(@PathVariable Long id, @PathVariable Long idTest, HttpServletRequest request) {
		Message message = new Message();
		try {
			EvaluationEventMatterTest evaluationEventMatterTest = evaluationEventMatterTestService.findById(idTest);
			evaluationEventMatterTest = evaluationEventMatterTestService.findById(evaluationEventMatterTest.getId());
			String extraScore = request.getParameter("extraScore");
			evaluationEventMatterTest.setExtraScore(Float.valueOf(extraScore.replace(',', '.')));
			evaluationEventMatterTestService.update(evaluationEventMatterTest);
			message.setMessage("Nota adicional modificada correctamente");
			message.setType(Message.TYPE_SUCCESS);
		} catch(Exception ex) {
			message.setMessage("Error al asignar nota adicional");
			message.setType(Message.TYPE_ERROR);
		}

		return message;
	}


	@RequestMapping(value="/evaluationevent/results/{id}/notify", method=RequestMethod.POST)
	public @ResponseBody Message notify(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> selected) {
		Message message = new Message();

		message.setMessage("Notificación completada correctamente");
		message.setType(Message.TYPE_SUCCESS);
		
		EvaluationEvent ev = evaluationEventService.findById(id);


		HashMap<Long, HashMap<String, HashMap<String, String>>> students = new HashMap<Long, HashMap<String, HashMap<String, String>>>();
		boolean eemtError = false;

		try {
			for(EvaluationEventMatterTest eemt : selected) {
				// podriamos no cargarlo y coger los datos que viene del listado pero podria pasar que no
				// estuviesen actualizados aunque la posibilidad sea remota
				eemt = evaluationEventMatterTestService.findById(eemt.getId());

				if(eemt.getState().equals(EvaluationEventMatterTest.STATE_PUBLISHED)) {

						//obtener todos los mts de este test
						try {
							HashMap<String, HashMap<String, String>> hashTests = null;
							List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTest(eemt, true);

							String code = eemt.getEvaluationEventMatter().getMatter().getCode();
							String name = eemt.getEvaluationEventMatter().getMatter().getName();
							String evType = eemt.getTest().getEvaluationType().getCode();
							Float rate = 0F;
							for(MatterTestStudent mts : mtss) {

								// Este hash debe contener por test.... 1: Nombre del test y la asignatura y tipo, 2: Nota 3 link a siette
								HashMap<String, String> testData = new HashMap<String, String>();
								User user = mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser();
								if(students.containsKey(user.getId())) {
									hashTests = students.get(user.getId());
									//testData = hashTests.get(eemt.getTest().getId());
								} else {
									hashTests = new HashMap<String, HashMap<String, String>>();
									//hashTests.put(eemt.getTest().getId(), testData);
									//students.put(user.getId(), hashTests);
								}


								rate = mts.getSession().getRate();
								testData.put("fullName", StringUtil.replaceToHTML(mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser().getFullName()));
								testData.put("matterCode", code);
								testData.put("matterName", StringUtil.replaceToHTML(name));
								testData.put("evaluationType", evType);
								testData.put("rate", rate.toString());
								testData.put("evaluationEventName", StringUtil.replaceToHTML(ev.getName()));
								testData.put("email", user.getEmail());

								Session session = mts.getSession();
								testData.put("sessionId", session.getId().toString());

								String urlToken = "";
								if(session.getUrlToken() != null) {
									urlToken = sietteUrl + session.getUrlToken();
								}

								testData.put("testUrl", urlToken);

								hashTests.put(eemt.getId().toString(), testData);		
								
								students.put(user.getId(), hashTests);
							}
						} catch(Exception ex) {
							message.setMessage("Error al notificar");
							message.setType(Message.TYPE_ERROR);
							eemtError = true;
						}
					}

				eemt.setState(EvaluationEventMatterTest.STATE_NOTIFIED);
				evaluationEventMatterTestService.update(eemt);

			}
		} catch(Exception ex) {
			message.setMessage("Error al notificar");
			message.setType(Message.TYPE_ERROR);
			eemtError = true;
		}


		if(!eemtError) {
			// una vez construida al matriz se recorre y se envia el cuadernillo completo pr estudiante
			for (Map.Entry<Long, HashMap<String, HashMap<String, String>>> entry : students.entrySet()) {
				Map.Entry<String, HashMap<String, String>> firstTestEntry = entry.getValue().entrySet().iterator().next();
				if(firstTestEntry != null) {
					mailer.setTo(firstTestEntry.getValue().get("email")); 
					try {
						mailer.send(entry.getValue());
						for (Map.Entry<String, HashMap<String, String>> testEntry : entry.getValue().entrySet()) {
							Session session = sessionService.findById(Long.parseLong(testEntry.getValue().get("sessionId")));
							session.setState(Session.STATE_NOTIFIED);
							sessionService.update(session);
						}
					} catch(Exception ex) {
						log.error("ERROR AL ENVIAR CORREO: " + ex.getMessage());
						message.setMessage("Se ha producido un error al notificar");
						message.setType(Message.TYPE_ERROR);
						for (Map.Entry<String, HashMap<String, String>> testEntry : entry.getValue().entrySet()) {
							EvaluationEventMatterTest eemtToChange = evaluationEventMatterTestService.findById(Long.parseLong(testEntry.getKey()));
							eemtToChange.setState(EvaluationEventMatterTest.STATE_PUBLISHED);
							evaluationEventMatterTestService.update(eemtToChange);
						}
						
					}
				}
			}
		}


		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEvent(id, false);
		message.setData(eemts);

		return message;
	}



	@RequestMapping(value="/evaluationevent/results/{id}/resultsreport", consumes="application/json")
	public void generateResultsReport(@PathVariable Long id, @RequestBody List<EvaluationEventMatterTest> tests, HttpServletResponse response) {

		if(tests != null && tests.size() > 0) {
	       String csvFileName = "resultados.csv";

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


				String[] header = {"CEDULA", "NOMBRE", "CENTRO DE ESTUDIO", "CENTRO DE EVALUACION",
		        		"TITULACION DEL ESTUDIANTE", "MODALIDAD DEL ESTUDIANTE", "ORIGEN DE EVALUACION",
		        		"CODIGO ASIGNATURA", "NOMBRE DE ASIGNATURA", "TIPO DE EVALUACION", "ACIERTOS", "ERRORES",
		        		"BLANCOS", "NUMERO DE PREGUNTAS TEST", "NUMERO DE PREGUNTAS REALIZADAS POR EL ESTUDIANTE",
		        		"CALIFICACION"};

				String[] mapping = {"cedula", "nombre", "centroEstudio", "centroEvaluacion",
		        		"titulacionEstudiante", "modalidadEstudiante", "origenEvaluacion",
		        		"codigoMateria", "nombreMateria", "tipoEvaluacion", "aciertos", "errores",
		        		"blancos", "numeroPreguntasTest", "numeroPreguntasRespondidas",
		        		"calificacion"};

		        csvWriter.writeHeader(header);

		        for(EvaluationEventMatterTest test : tests) {
		        	List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTestWithSession(test.getId());
		        	for(MatterTestStudent mts : mtss) {
		        		ResultReport resultReport = new ResultReport();
		        		String cedula = mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser().getIdentification();
		        		resultReport.setCedula(cedula);
		        		String nombre =  mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser().getFullName();
		        		resultReport.setNombre(nombre);
		        		String centroEstudio = "";
		        		if(mts.getEvaluationAssignmentMatter().getCenter() != null) {
		        			centroEstudio = mts.getEvaluationAssignmentMatter().getCenter().getName();
		        		}
		        		resultReport.setCentroEstudio(centroEstudio);

		        		if(mts.getEvaluationAssignmentMatter().getCenter() != null && mts.getEvaluationAssignmentMatter().getCenter().getEvaluationCenter() != null) {
		        			resultReport.setCentroEvaluacion( mts.getEvaluationAssignmentMatter().getCenter().getEvaluationCenter().getName());
		        		} else {
		        			resultReport.setCentroEstudio("");
		        		}

		        		resultReport.setTitulacionEstudiante(mts.getEvaluationAssignmentMatter().getCareerCode());
		        		resultReport.setModalidadEstudiante(mts.getEvaluationAssignmentMatter().getEvaluationEventMatter().getMode().getName());
		        		resultReport.setOrigenEvaluacion(mts.getEvaluationAssignmentMatter().getChannel());
		        		resultReport.setCodigoMateria(mts.getEvaluationAssignmentMatter().getEvaluationEventMatter().getMatter().getCode());
		        		resultReport.setNombreMateria(mts.getEvaluationAssignmentMatter().getEvaluationEventMatter().getMatter().getName());
		        		resultReport.setTipoEvaluacion(mts.getEvaluationEventMatterTest().getTest().getEvaluationType().getName());
		        		resultReport.setAciertos(String.valueOf(mts.getSession().getnSuccesses()));
		        		resultReport.setErrores(String.valueOf(mts.getSession().getnFails()));
		        		resultReport.setBlancos(String.valueOf(mts.getSession().getnBlanks()));
		        		resultReport.setNumeroPreguntasTest(String.valueOf(mts.getSession().getnSuccesses()+mts.getSession().getnFails()+mts.getSession().getnBlanks()));
		        		resultReport.setNumeroPreguntasRespondidas(String.valueOf(mts.getSession().getnSuccesses()+mts.getSession().getnFails()));
		        		resultReport.setCalificacion(String.valueOf(mts.getSession().getRate()));


		        		csvWriter.write(resultReport, mapping);
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
	}


	@RequestMapping(value="/evaluationevent/results/{id}/evaluationreport", consumes="application/json")
	public void generateEvaluationsReport(@PathVariable Long id, @RequestBody ResultEvalutionReportRequest result, HttpServletResponse response) {

		// ajustar el encoding para las tildes: se puede ajustar al abrir el csv y se ueda guardado par el siguiente
		List<EvaluationEventMatter> matters = new ArrayList<EvaluationEventMatter>();
		if(!result.getAll()) {
			matters = result.getMatters();
		} else {
			matters = evaluationEventMatterService.findByEvaluationEventWithBank(id);
		}

		if(matters != null && matters.size() > 0) {
	       String csvFileName = "evaluacion.csv";

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


				String[] header = {"CODIGO ASIGNATURA", "NOMBRE DE MATERIA", "ORIGEN DE EVALUACION", "NOMBRE TEST", "TIPO DE EVALUACION", 
						"HABILITADO PUBLICACION", "Nº EXAMENES", "EXAMENES CALIFICADOS", "EXAMENES PUBLICADOS", "PORCENTAJE PUBLICACION"};

				String[] mapping = {"codigoMateria", "nombreMateria", "origenEvaluacion", "nombreTest", "tipoEvaluacion", "habilitadoPublicacion", 
						"nExamenes", "examenesCalificados", "examenesPublicados", "porcentajePublicacion"};

		        csvWriter.writeHeader(header);

		        List<Long> matterIds = new ArrayList<Long>();
		        for(EvaluationEventMatter matter : matters) {
		        	matterIds.add(matter.getId());
		        }
	        		

	        	List<EvaluationReport> evaluationReports = evaluationEventMatterTestService.resportEvaluationTests(id, matterIds);

	        	for(EvaluationReport er : evaluationReports) {
	        		csvWriter.write(er, mapping);
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
	}
}
