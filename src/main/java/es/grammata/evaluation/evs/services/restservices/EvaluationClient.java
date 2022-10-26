package es.grammata.evaluation.evs.services.restservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ModeService;
import es.grammata.evaluation.evs.data.services.repository.RoleService;
import es.grammata.evaluation.evs.data.services.repository.StudentTypeService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@org.springframework.stereotype.Component
@Configurable
public class EvaluationClient extends UtplRestClient {

	private static org.apache.log4j.Logger log = Logger
			.getLogger(EvaluationClient.class);

	@Value("${evaluation.remote.service.uri}")
	private String evaluationRemoteServiceUri;
	
	@Value("${evaluation.remote.service.pro.uri}")
	private String evaluationRemoteServiceProUri;
	
	@Value("${service.utpl.rest.evaluacioncomponentes.uri}")
	private String evaluationComponentsUri;
	
	@Value("${service.utpl.rest.evaluacionstudiantescentro.uri}")
	private String evaluationCenterStudentsUri;
	
	private static final String WS_USER = "eval_online";
	
	private static final String RESULT_PUBLICATION_PATH = "publicarResultado";

	public static int COMPONENT_PROGRESS = 0;

	public static int STUDENTS_PROGRESS = 0;

	public static boolean RUNNING = false;

	public static boolean STUDENTS_RUNNING = false;

	public static String MESSAGE = "";

	public static String MESSAGE_TYPE;

	public static String STUDENTS_MESSAGE = "";

	public static String STUDENTS_MESSAGE_TYPE;
	
	@Value("${security.remote.service.uri}")
	private String securityRemoteServiceUri;
	
	@Value("${security.remote.service.api.key}")
	private String securityRemoteServiceApiKey;
	
	@Value("${security.remote.service.app.id}")
	private String securityRemoteServiceAppId;

	@Autowired
	private AcademicPeriodService academicPeriodService;

	@Autowired
	private MatterService matterService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	@Autowired
	private ModeService modeService;

	@Autowired
	private AcademicLevelService academicLevelService;

	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private StudentTypeService studentTypeService;
	
	@Autowired
	private EvaluationEventEvaluationCenterCenterService evaluationEventEvaluationCenterCenterService;
	

	private Map<String, Mode> modesMap;
	private Map<String, AcademicLevel> academicLevelsMap;
	private Map<String, AcademicPeriod> academicPeriodsMap;
	private Map<String, Role> rolesMap;

	@PostConstruct
	private void init() {
		loadMaps();
	}
	
	private void loadMaps() {
		modesMap = this.loadModes();
		academicLevelsMap = this.loadAcademicLevels();
		academicPeriodsMap = this.loadAcademicPeriods();
		rolesMap = this.loadRoles();
	}

	@Transactional
	public Message loadComponents(String evnCode, String pacCode,
			String modCode, String nacCode, boolean firstLoad) {
		Message message = new Message();
		if (!RUNNING) {
			RUNNING = true;
			COMPONENT_PROGRESS = 1;
			MESSAGE_TYPE = Message.TYPE_WARNING;
			MESSAGE = "El proceso no ha terminado con éxito";

			String js = "{\"evn_codigo\":\"" + evnCode + "\",\"mod_codigo\":\""
					+ modCode + "\",\"nac_codigo\":\"" + nacCode
					+ "\",\"pac_codigo\":\"" + pacCode + "\"}";

			EvaluationEvent evaluationEvent = evaluationEventService
					.findByCode(evnCode);

			Set<EvaluationType> evaluationTypes = evaluationEvent
					.getEvaluationTypes();

		
			loadMaps();
			
			try {
				String response = callServiceApim(evaluationComponentsUri, js);
				String jsonStr = response; // .getEntity(String.class);
				if (jsonStr != null && !jsonStr.equals("")) {
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> map = mapper.readValue(jsonStr,
							Map.class);
					if (map.containsKey("Tipo")
							&& map.get("Tipo").equals("Data")) {
						ArrayList contenido = (ArrayList) map.get("Contenido");
						int componentSize = contenido.size();
						for (int i = 0; i < componentSize; i++) {
							LinkedHashMap<String, String> contenidoElem = (LinkedHashMap) contenido
									.get(i);
							String code = contenidoElem.get("Codigo");
							String name = contenidoElem.get("Nombre");
							String aPeriodCode = contenidoElem
									.get("PeriodoAcademicoCodigo");
							String modeCode = contenidoElem
									.get("ModalidadCodigo");
							String levelCode = contenidoElem
									.get("NivelAcademicoCodigo");
							String managerId = contenidoElem
									.get("ResponsableIdentificacion");
							String managerName = contenidoElem
									.get("ResponsableNombre");

							AcademicPeriod aPeriod = academicPeriodsMap
									.get(aPeriodCode);

							if (managerId != null && !managerId.equals("")) {
								// buscar por identificador y crear docente
							}

							try {
								// crear asignatura asociada
								Matter matter = new Matter();
								matter.setName(name);
								matter.setAcademicLevel(academicLevelsMap
										.get(levelCode));
								//matter.setMode(modesMap.get(modeCode));
								//matter.setAcademicPeriod(aPeriod);
								matter.setCode(code);
								matter.setType(Matter.TYPE_WS);
								log.debug("Creando asignatura: "
										+ matter.getName());
								matter = matterService.saveOrLoadByCode(matter);

								// creamos asignacion asignatura-evento
								EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService
										.findByUnique(evaluationEvent.getId(),
												matter.getId(),
												aPeriod.getId(),
												modesMap.get(modeCode).getId());
								if (evaluationEventMatter == null) {
									evaluationEventMatter = new EvaluationEventMatter(
											evaluationEvent, matter, aPeriod,
											modesMap.get(modeCode));
									
									Set<WeekDay> daysAllowed = matter.getDaysAllowed();
									if(daysAllowed != null && daysAllowed.size() > 0) {
										ArrayList<WeekDay> daysTmp = new ArrayList<WeekDay>();
										daysTmp.addAll(daysAllowed);
										Set<WeekDay> daysSetTmp = new HashSet<WeekDay>(daysTmp);
										evaluationEventMatter.setDaysAllowed(daysSetTmp);
									}

									evaluationEventMatterService
											.save(evaluationEventMatter);

									// cargamos los tipos solo si no existia, si
									// esta actualizando dejamos los que tenia
									// (el usuario pudo hacer alguna
									// modificacion)
									Set<EvaluationType> matterTypes = new HashSet<EvaluationType>();
									evaluationEventMatter
											.setEvaluationTypes(matterTypes);
									evaluationEventMatter.getEvaluationTypes()
											.addAll(evaluationTypes);
									
									// creamos relacion con banco, la relacion con test se hara al cargar los alumnos
									// Nos quedamos con el último banco añadido del tipo de evento de evaluación con test activos
									if (evaluationEventMatter.getBank() == null) {
										Set<Bank> banks = matter.getBanks();
										for (Bank bank : banks) {
											if (bank.getEvaluationEventType().getId() == evaluationEvent.getEvaluationEventTypes().getId()) {
												List<Test> activeTests = testService.findByBankAndActive(bank.getId());
//												boolean success = bankService.checkTests(bank) && activeTests != null && activeTests.size() > 0;
												boolean success = activeTests != null && activeTests.size() > 0;
												if (success) {
													if (evaluationEventMatter.getBank() == null || bank.getCreateDate().after(evaluationEventMatter.getBank().getCreateDate())) {
														evaluationEventMatter.setBank(bank);
													}
												}
											}
										}
									}

									evaluationEventMatterService.update(evaluationEventMatter);
								}

								COMPONENT_PROGRESS = ((((i * 100) / componentSize) == 0) ? 1
										: ((i * 100) / componentSize));

								message.setMessage("Carga masiva completada correctamente");
								message.setType(Message.TYPE_SUCCESS);

								MESSAGE = "Carga masiva completada correctamente";
								MESSAGE_TYPE = Message.TYPE_SUCCESS;

								// log.info("Entrando en proceso de creación de usuarios por asignatura...");
								// loadStudents(evaluationEvent, matter,
								// pacCode, modCode, nacCode, matter.getCode(),
								// true);
							} catch (Exception ex) {
								log.error(ex.getMessage());
							}
						}
					} else if (map.containsKey("Tipo")
							&& map.get("Tipo").equals("Error")) {
						LinkedHashMap<String, String> contenido = (LinkedHashMap) map
								.get("Contenido");
						String codigo = String.valueOf(contenido.get("Codigo"));
						String descripcion = contenido.get("Descripcion");
						log.error("Error validación: " + codigo + " - "
								+ descripcion);
						message.setMessage("Error al obtener datos del servicio remoto: " + descripcion);
						message.setType(Message.TYPE_ERROR);
						MESSAGE = "Error al obtener datos del servicio remoto: "+ descripcion;
						MESSAGE_TYPE = Message.TYPE_ERROR;
						log.error("Error al obtener datos del servicio remoto");
					} else {
						message.setMessage("Error en el servicio remoto");
						message.setType(Message.TYPE_ERROR);
						MESSAGE = "Error en el servicio remoto";
						MESSAGE_TYPE = Message.TYPE_ERROR;
						log.error("Error en el servicio remoto");
					}
				} else {
					message.setMessage("Error en el servicio remoto");
					message.setType(Message.TYPE_ERROR);
					MESSAGE = "Error en el servicio remoto";
					MESSAGE_TYPE = Message.TYPE_ERROR;
					log.error("Error en el servicio remoto");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage());
				MESSAGE = "Se ha producido un error";
				MESSAGE_TYPE = Message.TYPE_ERROR;
			} finally {
				RUNNING = false;
				COMPONENT_PROGRESS = 100;
			}
		}
		return message;
	}

	//@Transactional
	public Message loadStudents(EvaluationEvent evaluationEvent, String matterCode, String pacCode, String modCode, String nacCode, boolean firstLoad) {
		Message message = new Message();
		if(!STUDENTS_RUNNING) {
			STUDENTS_RUNNING = true;
			STUDENTS_PROGRESS = 1;
			message.setMessage("No se han cargado estudiantes");
			message.setType(Message.TYPE_WARNING);
			
			STUDENTS_MESSAGE = "No se han cargado estudiantes";
			STUDENTS_MESSAGE_TYPE = Message.TYPE_WARNING;
			
			AcademicPeriod academicPeriod = academicPeriodService.findByCode(pacCode);
			//Mode mode = modeService.findByCode(modCode);
			Acumulator wsTotalTime = new Acumulator();
			Acumulator totalCalls = new Acumulator();
			Long startTotalTime = System.currentTimeMillis();
			int it = 0;
			try {
				List<EvaluationEventMatter> eems = new ArrayList<>(); // evaluationEventMatterService.findByParams(evaluationEvent.getId(), academicPeriod.getId(), mode.getId());
				
				HashMap<String, StudentType> studentTypes = new HashMap<String, StudentType>();
				for (StudentType studenType : studentTypeService.findAll()) {
					studentTypes.put(studenType.getValue(), studenType);
				}
				
				eems = evaluationEventMatterService.findByEvaluationEventWithBank(evaluationEvent.getId());
				
				if(eems == null || eems.size() == 0) {
					message.setMessage("No existen asignaturas en el sistema asociadas a los parámetros de carga o no tienen banco de preguntas asociado");
					message.setType(Message.TYPE_WARNING);
					
					STUDENTS_MESSAGE = "No existen asignaturas en el sistema asociadas a los parámetros de carga o no tienen banco de preguntas asociado";
					STUDENTS_MESSAGE_TYPE = Message.TYPE_WARNING;

					log.warn("No existen asignaturas en el sistema asociadas a los parámetros de carga");
				}
				
				Set<Long> centersUsed = new HashSet<Long>();
				List<Center> centers = evaluationEventEvaluationCenterCenterService.findCentersByEvaluationEventId(evaluationEvent.getId());
				Integer centersSize = centers.size();
				Integer centerCount = 1;
				float centersSizeF = (float)centersSize;
				float partialCenterSize = (centersSizeF==0)?1:(100/centersSizeF);
				HashMap<Long,  HashMap<Long, EvaluationEventMatterTest>> mattersTestProcessed = new HashMap<Long,  HashMap<Long, EvaluationEventMatterTest>>();
				HashSet<Long> mattersProcessed = new HashSet<Long>();
				for(Center center : centers) {
//					System.out.println("CENTRO: " + center.getName());
					String centerCode = center.getUniqueCode();
					if(centersUsed.contains(center.getId())){
						continue;
					}
					centersUsed.add(center.getId());
					
					Integer mattersSize = eems.size();
					Integer currentMatter = 0;
					for(EvaluationEventMatter evaluationEventMatter : eems) {
						long startStudentTotalTime = System.currentTimeMillis();
						currentMatter++;
						HashMap<Long, EvaluationEventMatterTest> eemts = new HashMap<Long, EvaluationEventMatterTest>();
						// si  se han generado los test para la matera los cargamos
						if(mattersTestProcessed.containsKey(evaluationEventMatter.getId())) {
							eemts = mattersTestProcessed.get(evaluationEventMatter.getId());
						} else { // si no se han generado los test para la matera los generamos
							eemts = createEvaluationEventMatterTests(evaluationEventMatter);
							if(eemts != null) {
								mattersTestProcessed.put(evaluationEventMatter.getId(), eemts);
							} else {
								mattersTestProcessed.put(evaluationEventMatter.getId(), new HashMap<Long, EvaluationEventMatterTest>());
							}
						}

						this.processEvaluationEventMatter(currentMatter, evaluationEventMatter, evaluationEvent, center, centerCode, 
								pacCode, modCode, nacCode, totalCalls, wsTotalTime, mattersSize, centerCount, centersSize, startTotalTime, message, 
								eemts, studentTypes, false);

						long endStudentTotalTime = System.currentTimeMillis();
						it += 1;
//						System.out.println(it + " Tiempo total PROCESO ASIGNATURA " + (endStudentTotalTime - startStudentTotalTime) + " milisegundos");
					}
					
					centerCount++;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage());
				STUDENTS_MESSAGE = "Se ha producido un error";
				STUDENTS_MESSAGE_TYPE = Message.TYPE_ERROR;
			} finally {
				STUDENTS_RUNNING = false;
				STUDENTS_PROGRESS = 100;
			}
		}
		
		return null;
	}
	
	
	private HashMap<Long, EvaluationEventMatterTest> createEvaluationEventMatterTests(EvaluationEventMatter evaluationEventMatter) {
		Matter matter = evaluationEventMatter.getMatter(); //matterService.findByCode(matterCode);
		log.info("Creando asociacion para asignatura: " + matter.getCode());
		
		Set<EvaluationType> evaluationTypes = evaluationEventMatter.getEvaluationTypes();
		
		// crear asociacion asignatura banco si no existe (si solo tiene uno con ese)
		Bank bank = evaluationEventMatter.getBank();
		Set<Bank> banks = matter.getBanks();
		HashMap<Long, EvaluationEventMatterTest> eemts = null;
		if (banks == null || banks.size() == 0) {
			log.warn("La asignatura " + matter.getId() + " no tiene banco asociado");
		} else if (banks.size() == 1 || evaluationEventMatter.getBank() != null) {
			// si no tiene ya se lo asignamos
			if (evaluationEventMatter.getBank() == null) {
				List<Test> activeTests = testService.findByBankAndActive(bank.getId());
				boolean success = bankService.checkTests(bank) && activeTests != null && activeTests.size() > 0;
				if (success) {
					evaluationEventMatter.setBank(banks.iterator().next());
					evaluationEventMatterService.update(evaluationEventMatter);
				}
			}
			
			// ha de tener banco asociado
			if (evaluationEventMatter.getBank() != null) {
				// MODIFICADO: usando test activos
				List<Test> activeTests = testService.findByBankAndActive(bank.getId());								
				if(activeTests != null) {
					eemts = new HashMap<Long, EvaluationEventMatterTest>();
					log.info("Creando asociacion de banco " + bank.getName() + " con tests");
					for(Test test : activeTests) {
						log.info("Creando asociacion con test : " + test.getName());
						EvaluationType evaluationType = test.getEvaluationType();
						// si el tipo del test esta entre los tipos de la asignatura se crea la asociacion, puede haber otros tests seleccionados pero tendrán que ser a mano
						if(evaluationTypes.contains(evaluationType)) {
							// crear asociacion test del tipo actual de la asignatura con test								
							EvaluationEventMatterTest evaluationEventMatterTest = evaluationEventMatterTestService.findByUnique(test, evaluationEventMatter);
							if(evaluationEventMatterTest == null) {
								evaluationEventMatterTest = new EvaluationEventMatterTest();
								evaluationEventMatterTest.setTest(test);
								evaluationEventMatterTest.setEvaluationEventMatter(evaluationEventMatter);
								evaluationEventMatterTestService.save(evaluationEventMatterTest);
							}
							eemts.put(evaluationEventMatterTest.getId(), evaluationEventMatterTest);
						}
					}
				} else {
					log.warn("El banco " + bank.getName() +  " no tiene test asociados");
				}
			} else {
				log.warn("La asignatura no tiene banco asociado");
			}
			
			log.debug("Generados evaluationEventMatterTest");
		} else {
			log.warn("La asignatura " + matter.getId() + " no tiene banco seleccionado para el evento");
		}
	
		return eemts;
	}
	
	
	
	
	public void processEvaluationEventMatter(Integer currentMatter, EvaluationEventMatter evaluationEventMatter, EvaluationEvent evaluationEvent,
			Center center, String centerCode, String pacCode, String modCode, String nacCode, Acumulator totalCalls, Acumulator wsTotalTime, Integer mattersSize, 
			Integer centerCount, Integer centersSize, Long startTotalTime, Message message, HashMap<Long, EvaluationEventMatterTest> eemts, HashMap<String, StudentType> studentTypes, boolean processMatterTests) throws Exception {

		
		try {
			Matter matter = evaluationEventMatter.getMatter(); //matterService.findByCode(matterCode);
			log.info("Creando asociacion para asignatura: " + matter.getCode());
			
			Set<EvaluationType> evaluationTypes = evaluationEventMatter.getEvaluationTypes();
			
			// crear asociacion asignatura banco si no existe (si solo tiene uno con ese)
			//Bank bank = evaluationEventMatter.getBank();
			//Set<Bank> banks = matter.getBanks();
			//if (banks == null || banks.size() == 0) {
			//	log.warn("La asignatura " + matter.getId() + " no tiene banco asociado");
			//} else if (banks.size() == 1 || evaluationEventMatter.getBank() != null) {
				// si no tiene ya se lo asignamos
				/*if (evaluationEventMatter.getBank() == null) {
					evaluationEventMatter.setBank(banks.iterator().next());
					evaluationEventMatterService.update(evaluationEventMatter);
				}
				
				
				HashMap<Long, EvaluationEventMatterTest> eemts = new HashMap<Long, EvaluationEventMatterTest>();
				// ha de tener banco asociado
				if (evaluationEventMatter.getBank() != null) {
					// MODIFICADO: usando test activos
					List<Test> activeTests = bankService.getActiveTests(bank.getId());								
					if(activeTests != null) {
						log.info("Creando asociacion de banco " + bank.getName() + " con tests");
						for(Test test : activeTests) {
							log.info("Creando asociacion con test : " + test.getName());
							EvaluationType evaluationType = test.getEvaluationType();
							// si el tipo del test esta entre los tipos de la asignatura se crea la asociacion, puede haber otros tests seleccionados pero tendrán que ser a mano
							if(evaluationTypes.contains(evaluationType)) {
								// crear asociacion test del tipo actual de la asignatura con test								
								EvaluationEventMatterTest evaluationEventMatterTest = evaluationEventMatterTestService.findByUnique(test, evaluationEventMatter);
								if(evaluationEventMatterTest == null) {
									evaluationEventMatterTest = new EvaluationEventMatterTest();
									evaluationEventMatterTest.setTest(test);
									evaluationEventMatterTest.setEvaluationEventMatter(evaluationEventMatter);
									evaluationEventMatterTestService.save(evaluationEventMatterTest);
								}
								eemts.put(evaluationEventMatterTest.getId(), evaluationEventMatterTest);
							}
						}
					} else {
						log.warn("El banco " + bank.getName() +  " no tiene test asociados");
					}
				} else {
					log.warn("La asignatura no tiene banco asociado");
				}
				
				log.debug("Generados evaluationEventMatterTest");*/
				
				Map<String, User> usersMap = new HashMap();
				
				// peticion para cada tipo
				int evaluationTypesSize = evaluationTypes.size();
				int currentType = 0;
			
				for(EvaluationType type : evaluationTypes) {
					currentType++;
					
					Long startTime = System.currentTimeMillis();
					
					String js = "{\"evn_codigo\":\"" + evaluationEvent.getCode() + "\",\"mod_codigo\":\"" + modCode + 
							"\",\"nac_codigo\":\"" + nacCode + "\",\"pac_codigo\":\"" + pacCode + "\",\"coe_codigo\":\"" + matter.getCode() + "\",\"tipo_evaluacion\":\"" + type.getCodeWS() + "\", " +
									"\"cen_codigo\":\"" + centerCode + "\"}";
					
					log.debug("Paso previo a peticion a servicio de carga de alumnos");
					String response = callServiceApim(evaluationCenterStudentsUri, js);
					log.debug("Peticion realizada");
					
					totalCalls.setCurrentValue(totalCalls.getCurrentValue()+1);
					Long endTimeTmp = System.currentTimeMillis();
					wsTotalTime.setCurrentValue(wsTotalTime.getCurrentValue() + (endTimeTmp - startTime));
//					System.out.println("Tiempo total llamada " + (endTimeTmp - startTime) + " milisegundos");
//					System.out.println("Tiempo total ACUMULADO LLAMADAS " + wsTotalTime.getCurrentValue() + " milisegundos; LLamadas ACUMULADAS: " + totalCalls.getCurrentValue());
					
					log.info("Petición a WS: " + js);
					String jsonStr = response; // response.getEntity(String.class);
					if(jsonStr != null && !jsonStr.equals("")) {
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Object> map = mapper.readValue(jsonStr, Map.class);
						if(map.containsKey("Tipo") && map.get("Tipo").equals("Data")) {
							ArrayList contenido = (ArrayList)map.get("Contenido");
							log.info("Obtenidos: " + contenido.size() + " elementos");
							float componentSize = contenido.size();
							for(int i = 0; i < contenido.size(); i++) {
								this.loadStudent(contenido, i, usersMap, evaluationEvent, center, evaluationEventMatter, eemts, studentTypes, message, type);
								
								try {
									float sizeTmp = ((i*100)/componentSize);
									float evaluationTypesSizeTmp = (float)evaluationTypesSize;
									float mattersSizeTmp = (float)mattersSize;
									float currentMatterTmp = (float)currentMatter;
									
									float total = (sizeTmp/evaluationTypesSizeTmp) + ((100/evaluationTypesSizeTmp)*(currentType-1));
									float totalMatters = (total/mattersSizeTmp) + ((100/mattersSizeTmp)*(currentMatterTmp-1));
									
									float centerSizeTmp = (float)centersSize;
									float partialMatters = (totalMatters/centerSizeTmp);
									float partialCenterSize = 100/centerSizeTmp;
									
									float totalcentersTmp = partialMatters + (partialCenterSize*(centerCount-1)); 
									int totalcenters = Math.round(totalcentersTmp);
									
									STUDENTS_PROGRESS = (totalcenters==0)?1:totalcenters;
									
									
									message.setMessage("Carga masiva completada correctamente");
									message.setType(Message.TYPE_SUCCESS);
									
									STUDENTS_MESSAGE = "Carga masiva completada correctamente";
									STUDENTS_MESSAGE_TYPE = Message.TYPE_SUCCESS;
								} catch (Exception ex) {
									log.error("Actualizando progreso de proceso de carga de alumnos: " + ex.getMessage());
								}
							}			
						} else if(map.containsKey("Tipo") && map.get("Tipo").equals("Error")) {
							LinkedHashMap<String, String> contenido = (LinkedHashMap)map.get("Contenido");
							String codigo = String.valueOf(contenido.get("Codigo")); 
							String descripcion = contenido.get("Descripcion");

							// no hay resultados, seguimos
							if(!codigo.equals("400")) {
								log.error("Error validación: " + codigo + " - " + descripcion);
								STUDENTS_MESSAGE = "Error al obtener datos del servicio remoto";
								STUDENTS_MESSAGE_TYPE = Message.TYPE_ERROR;
								break;
							} else {
								log.info("La petición NO DEVUELVE RESULTADOS");
							}
						} else {
							log.error("Error al obtener datos del servicio remoto");
							STUDENTS_MESSAGE = "Error al obtener datos del servicio remoto";
							STUDENTS_MESSAGE_TYPE = Message.TYPE_ERROR;
							if(jsonStr != null && !jsonStr.equals("")) {
								log.error("Respuesta del WS: " + jsonStr);
							} if(jsonStr == null) {
								log.error("La respuesta del WS es NULL");
							} else {
								log.error("La respuesta del WS es VACIA");
							}
							break;
						}
					} else {
						log.error("Error al obtener datos del servicio remoto");
						STUDENTS_MESSAGE = "Error al obtener datos del servicio remoto";
						STUDENTS_MESSAGE_TYPE = Message.TYPE_ERROR;
						if(jsonStr == null) {
							log.error("La respuesta del WS es NULL");
						} else {
							log.error("La respuesta del WS es VACIA");
						}
						break;
					}	
				}			
			//} else {
			//	log.warn("La asignatura " + matter.getId() + " no tiene banco seleccionado para el evento");
			//}
		
			long endTotalTimeTmp = System.currentTimeMillis();
//			System.out.println("Tiempo TOTAL " + (endTotalTimeTmp - startTotalTime) + " milisegundos");
		} catch (Exception ex) {
			log.error("Error procesando carga de alumnos: " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
										
	}
	
	@Transactional
	public void loadStudent(ArrayList contenido, int i, Map<String, User> usersMap, EvaluationEvent evaluationEvent, Center center,
			EvaluationEventMatter evaluationEventMatter, HashMap<Long, EvaluationEventMatterTest> eemts, HashMap<String, StudentType> studentTypes,
			Message message, EvaluationType type) throws Exception {
		
		LinkedHashMap<String, String> contenidoElem = (LinkedHashMap)contenido.get(i);
		String username = contenidoElem.get("Usuario"); 																		
		String lastName = contenidoElem.get("Apellido"); 
		String name = contenidoElem.get("Nombre"); 
		String par = contenidoElem.get("Paralelo"); 	
		String career = contenidoElem.get("Titulacion"); 
		String careerCode = contenidoElem.get("TitulacionCodigo"); 	
		String channel = contenidoElem.get("Canal");
		String identification = contenidoElem.get("Identificacion");
		String email = contenidoElem.get("Correos");
		String studentTypeValue = contenidoElem.get("TipoEstudiante");
		
		StudentType studentType = null;
		if(studentTypeValue != null) {
			studentType = studentTypes.get(studentTypeValue);
			if(studentType == null) {
				studentType = new StudentType();
				studentType.setValue(studentTypeValue);
				studentTypeService.save(studentType);
				studentTypes.put(studentType.getValue(), studentType);
			}
		}
		
		// TODO por ahora nos quedamos con el primero
		if (email != null) {
			email = email.replace(" ", "");
			String[] emails = email.split(",");
			email = emails[0];										
		} else {
			email = "";
		}
		
		User user = usersMap.get(username);
		if (user != null) {
			// Si la identificación o el email cambia, lo actualizamos
			if (!user.getIdentification().equals(identification) || !user.getEmail().equals(email)) {
				user = userService.findByUsername(username);
				if (email.length() > 0) {
					user.setEmail(email);
				}
				if (identification.length() > 0) {
					user.setIdentification(identification);	
				}
				userService.update(user);
				
				usersMap.put(username, user);
			}
		} else {
			user = new User();
			user.setEnabled(1);
			user.setFirstName(name);
			user.setLastName(lastName);
			user.setUsername(username);
			user.setIdentification(identification);
			user.setEmail(email);
			
			Set<Role> roles = new HashSet<Role>();
			roles.add(rolesMap.get(Role.STUDENT));
			user.setRoles(roles);
			
			// Creamos el usuario
			user = userService.saveOrLoadByUsername(user);
			usersMap.put(username, user);
		}
		
		// Obtenemos la matrícula y si no existe, la creamos
		EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUnique(evaluationEvent, user);
		if (evaluationAssignment == null) {
			evaluationAssignment = new EvaluationAssignment();
			evaluationAssignment.setEvaluationEvent(evaluationEvent);
			evaluationAssignment.setUser(user);
			evaluationAssignment.setStudentType(studentType);
			evaluationAssignmentService.save(evaluationAssignment);
		}
		
		// Si la matrícula tiene tipo de estudiante nulo, lo actualizamos
		if (evaluationAssignment.getStudentType() == null && studentType != null) {
			evaluationAssignment.setStudentType(studentType);
			evaluationAssignmentService.update(evaluationAssignment);
		}
		
		// Creación asociacion matricula a asignatura
		EvaluationAssignmentMatter evaluationAssignmentMatter = evaluationAssignmentMatterService.find(evaluationEvent.getId(), user.getId(), evaluationEventMatter.getId());
		if(evaluationAssignmentMatter == null) {
			evaluationAssignmentMatter = new EvaluationAssignmentMatter();
			evaluationAssignmentMatter.setEvaluationAssignment(evaluationAssignment);
			evaluationAssignmentMatter.setEvaluationEventMatter(evaluationEventMatter);
			evaluationAssignmentMatter.setChannel(channel);
			evaluationAssignmentMatter.setCareer(career);
			evaluationAssignmentMatter.setCareerCode(careerCode);
			evaluationAssignmentMatter.setCenter(center);
			evaluationAssignmentMatterService.save(evaluationAssignmentMatter);													
		}
		
		// Creación tests por estudiante
		for (EvaluationEventMatterTest evaluationEventMatterTest : eemts.values()) {
		    if(evaluationEventMatterTest.getTest().getEvaluationType().getId() == type.getId()) {
			    MatterTestStudent matterTestStudent = matterTestStudentService.findByUnique(evaluationEventMatterTest, evaluationAssignmentMatter);
				if(matterTestStudent == null) {
					matterTestStudent = new MatterTestStudent();
					matterTestStudent.setEvaluationAssignmentMatter(evaluationAssignmentMatter);
					matterTestStudent.setEvaluationEventMatterTest(evaluationEventMatterTest);
					matterTestStudentService.save(matterTestStudent);
				}
		    }
		}
	}
	
	
	
	@Transactional
	public Integer publishResult(String username, String period, String identification, String matter, String periodGuid, 
			String studentGuid, String paralelGuid, String itemEvaluation, String result, String maxRatePublish) {
		
		result = result.replace(".", ",");
			
		Integer status = null;
		String js = "{\"usuario\": \"" + WS_USER + "\", \"periodo\":  \"" + period + "\", \"identificacion\": \"" + identification + "\", " +
				"\"componente\": \"" + matter + "\", " +
				"\"itemEvaluacion\": \"" + itemEvaluation + "\", \"resultado\": \"" + result + "\", \"publicaMayor\": \"" + maxRatePublish + "\"}";
		

		String response = callService(evaluationRemoteServiceUri + RESULT_PUBLICATION_PATH,
				securityRemoteServiceApiKey, securityRemoteServiceAppId, js);

		String message = null;
		
		try {
			String jsonStr = response; // .getEntity(String.class);
			if (jsonStr != null && !jsonStr.equals("")) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = mapper.readValue(jsonStr,
						Map.class);
				if (map.containsKey("Status")) {
					status = (Integer)map.get("Status");
					message = (String)map.get("Message");
					log.info("WS publicación resultados - Código: " + status + " - " + message);
				}
			}
		} catch(Exception ex) {
			if(status != null && message != null) {
				log.error("WS publicación resultados - Código: " + status + " - " + message);
			} else {
				log.error("WS publicación resultados");
			}
		}
		
		return status;
	}
			
		

	public Map<String, Mode> loadModes() {
		List<Mode> modes = modeService.findAll();
		Map<String, Mode> modesMap = new HashMap<String, Mode>();
		for (Mode mode : modes) {
			modesMap.put(mode.getCode(), mode);
		}

		return modesMap;
	}

	public Map<String, AcademicLevel> loadAcademicLevels() {
		List<AcademicLevel> academicLevels = academicLevelService.findAll();
		Map<String, AcademicLevel> academicLevelsMap = new HashMap<String, AcademicLevel>();
		for (AcademicLevel academicLevel : academicLevels) {
			academicLevelsMap.put(academicLevel.getCode(), academicLevel);
		}

		return academicLevelsMap;
	}

	public Map<String, AcademicPeriod> loadAcademicPeriods() {
		List<AcademicPeriod> academicPeriods = academicPeriodService.findAll();
		Map<String, AcademicPeriod> academicPeriodsMap = new HashMap<String, AcademicPeriod>();
		for (AcademicPeriod academicPeriod : academicPeriods) {
			academicPeriodsMap.put(academicPeriod.getCode(), academicPeriod);
		}

		return academicPeriodsMap;
	}

	public Map<String, Role> loadRoles() {
		List<Role> roles = roleService.findAll();
		Map<String, Role> rolesMap = new HashMap<String, Role>();
		for (Role role : roles) {
			rolesMap.put(role.getCode(), role);
		}

		return rolesMap;
	}

	public HashMap<String, EvaluationType> loadEvaluationTypes(
			Set<EvaluationType> evaluationTypes) {
		HashMap<String, EvaluationType> evalTypesMap = new HashMap<String, EvaluationType>();
		for (EvaluationType evaluationType : evaluationTypes) {
			evalTypesMap.put(evaluationType.getCode(), evaluationType);
		}
		return evalTypesMap;
	}
	

	public static void main(String[] args) {
		EvaluationClient evaluationClient = new EvaluationClient();
		// evaluationClient.callService();

		// {'pac_codigo': '00249', 'evn_codigo': 'PAC2BIM1', 'nac_codigo':
		// 'PRE01', 'mod_codigo': 'PRESENCIAL'}
		// evaluationClient.loadComponents("PAC2BIM1", "00249", "PRESENCIAL",
		// "PRE01", true);

		EvaluationEvent evaluationEvent = evaluationClient.evaluationEventService
				.findByCode("PAC2BIM1");
		Matter matter = evaluationClient.matterService
				.findByCode("PRE-TNIA036");
		// {"pac_codigo": "00249", "evn_codigo": "", "nac_codigo": "PRE01",
		// "mod_codigo": "PRESENCIAL", "coe_codigo": "PRE-TNIA036"}
		// evaluationClient.loadStudents(evaluationEvent, matter, "00249",
		// "PRESENCIAL", "PRE01", true);
	}
	
	protected class Acumulator {
		public long initValue = 0;
		public long currentValue = 0; 
		void Acumulator(){}
		public long getInitValue() {
			return initValue;
		}
		public void setInitValue(long initValue) {
			this.initValue = initValue;
		}
		public long getCurrentValue() {
			return currentValue;
		}
		public void setCurrentValue(long currentValue) {
			this.currentValue = currentValue;
		}	
	}

}
