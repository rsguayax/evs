package es.grammata.evaluation.evs.mvc.controller.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.GenericScheduleLog;
import es.grammata.evaluation.evs.data.model.repository.Log;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Net;
import es.grammata.evaluation.evs.data.model.repository.Server;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.model.repository.StudentTestScheduleLog;
import es.grammata.evaluation.evs.data.model.repository.StudentType;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;
import es.grammata.evaluation.evs.data.services.repository.CenterService;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.GenericScheduleLogService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.data.services.repository.StudentTestScheduleLogService;


@Service
public class StudentTestsSchedules {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(StudentTestsSchedules.class);


	private EvaluationEvent evaluationEvent;
	private EvaluationAssignment evaluationAssignment;
	private EvaluationEventConfiguration evaluationEventConfiguration;
	private List<DayTimeBlocks> daysTimeBlocks;
	private List<StudentTestScheduleLog> testScheduleLog;

	@Autowired
	private StudentEvaluationService studentEvaluationService;

	@Autowired
	private AvailableStateService availableStateService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private StudentTestScheduleLogService studentTestScheduleLogService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private EvaluationCenterService evaluationCenterService;
	
	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;
	
	@Autowired
	private GenericScheduleLogService genericScheduleLogService;
	
	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private CenterService centerService;
	
	public void setEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		this.evaluationEvent = evaluationAssignment.getEvaluationEvent();
		this.evaluationAssignment = evaluationAssignment;
		this.evaluationEventConfiguration = evaluationAssignment.getEvaluationEvent().getConfiguration();
		this.daysTimeBlocks = new ArrayList<DayTimeBlocks>();
		this.testScheduleLog = new ArrayList<StudentTestScheduleLog>();

		List<StudentEvaluation> studentEvaluations = studentEvaluationService.findByEvaluationAssignment(evaluationAssignment.getId());
		for (StudentEvaluation studentEvaluation : studentEvaluations) {
			ClassroomTimeBlock timeBlock = studentEvaluation.getClassroomTimeBlock();
			DayTimeBlocks dayTimeBlocks = getDayTimeBlocks(timeBlock.getStartDate());

			if (dayTimeBlocks == null) {
				dayTimeBlocks = new DayTimeBlocks(timeBlock.getStartDate(), timeBlock.getEvaluationCenter());
				daysTimeBlocks.add(dayTimeBlocks);
			}

			TimeBlockTests timeBlockTests = new TimeBlockTests(timeBlock);
			Hibernate.initialize(studentEvaluation.getMatterTests());
			timeBlockTests.getTests().addAll(studentEvaluation.getMatterTestInfos());

			dayTimeBlocks.getTimeBlocksTests().add(timeBlockTests);
		}
	}

	public EvaluationAssignment getEvaluationAssignment() {
		return evaluationAssignment;
	}

	public List<DayTimeBlocks> getDaysTimeBlocks() {
		return daysTimeBlocks;
	}

	public List<StudentTestScheduleLog> getTestScheduleLog() {
		return testScheduleLog;
	}

	private DayTimeBlocks getDayTimeBlocks(Date day) {
		day = setTimeToMidnight(day);
		for (DayTimeBlocks dayTimeBlocks : daysTimeBlocks) {
			if (day.equals(dayTimeBlocks.getDay())) {
				return dayTimeBlocks;
			}
		}

		return null;
	}

	public boolean assignScheduleToTest(MatterTestStudentInfo matterTestStudent, List<WeekDay> matterDaysAllowed, List<EvaluationCenter> evaluationCentersAllowed, HashMap<Long, EvaluationCenterTimeBloks> evaluationCentersTimeBloks) {
		boolean scheduleAssigned = false;

		// Comprobamos si alguno de los bloques horarios ya asignados es válido para el test
		forDaysTimeBlocks:
		for (DayTimeBlocks dayTimeBlocks : daysTimeBlocks) {
			// Comprobamos si el día es uno de los días permitidos para la asignatura
			if (dayTimeBlocks.isDayAllowed(matterDaysAllowed)) {
				// Comprobamos si el centro de evaluación asociado al día es uno de los centros permitidos
				if (dayTimeBlocks.isEvaluationCenterAllowed(evaluationCentersAllowed)) {
					// Comprobamos si se pueden añadir tests al día
					if (dayTimeBlocks.canAddTests()) {
						for (TimeBlockTests timeBlockTests : dayTimeBlocks.getTimeBlocksTests()) {
							// Comprobamos si el bloque horario permite el tipo de estudiante
							if(timeBlockTests.allowsStudentType()) {
								// Comprobamos si se pueden añadir el test al bloque horario
								if (timeBlockTests.canAddTest()) {
									timeBlockTests.getTests().add(matterTestStudent);

									scheduleAssigned = true;
									break forDaysTimeBlocks;
								} else {
									addTestScheduleLog(matterTestStudent, dayTimeBlocks, timeBlockTests.getTimeBlock(), "No se pueden a\u00f1adir m\u00e1s ex\u00e1menes a la jornada de evaluaci\u00f3n. Se ha alcanzado el n\u00famero m\u00e1ximo de ex\u00e1menes por jornada de evaluaci\u00f3n.");
								}
							} else {
								String tipo = evaluationAssignment.getStudentType() != null ? evaluationAssignment.getStudentType().getValue() : "Sin especificar";
								addTestScheduleLog(matterTestStudent, dayTimeBlocks, timeBlockTests.getTimeBlock(), "No se pueden a\u00f1adir estudiantes del tipo \"" + tipo + "\" a la jornada de evaluaci\u00f3n.");
							}
						}
					} else {
						addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "No se pueden a\u00f1adir m\u00e1s ex\u00e1menes al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(dayTimeBlocks.getDay()) + ". Se ha alcanzado el n\u00famero m\u00e1ximo de ex\u00e1menes por d\u00eda.");
					}
				} else {
					addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "El centro de evaluaci\u00f3n \"" + dayTimeBlocks.getEvaluationCenter().getName() + "\" no es uno de los centros permitidos para examinarse de la asignatura.");
				}
			} else {
				addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "El " + dayTimeBlocks.getWeekDay() + " no es uno de los d\u00edas permitidos para examinarse de la asignatura.");
			}
		}

		// Si no hemos encontrado ningún bloque horario válido para el test, buscamos uno en la lista de bloques horarios de los centros de evaluación
		if (!scheduleAssigned) {
			forEvaluationCenter:
			for (EvaluationCenter evaluationCenter : evaluationCentersAllowed) {
				for (ClassroomTimeBlock timeBlock : evaluationCentersTimeBloks.get(evaluationCenter.getId()).getAvailableStateTimeBlocks()) {
					// Comprobamos que el bloque horario tenga plazas disponibles
					Hibernate.initialize(timeBlock.getStudentEvaluations());
					if (timeBlock.getAvailableState().getValue().equals(AvailableState.AVAILABLE) && timeBlock.getAvailableSeats() > 0) {
						// Comprobamos si el bloque horario está en uno de los días permitidos para la asignatura
						if (isDayAllowed(getDateWeekDayCode(timeBlock.getStartDate()), matterDaysAllowed)) {
							// Comprobamos si el bloque horario permite el tipo de estudiante
							if (timeBlockAllowsStudentType(timeBlock)) {
								DayTimeBlocks dayTimeBlocks = getDayTimeBlocks(timeBlock.getStartDate());

								if (dayTimeBlocks == null) {
									dayTimeBlocks = new DayTimeBlocks(timeBlock.getStartDate(), timeBlock.getEvaluationCenter());
									daysTimeBlocks.add(dayTimeBlocks);
								}

								// Comprobamos si el bloque horario cumple todas las condiciones para poder añadirlo al día
								if (dayTimeBlocks.canAddTimeBlock(timeBlock, matterTestStudent)) {
									TimeBlockTests timeBlockTests = new TimeBlockTests(timeBlock);
									timeBlockTests.getTests().add(matterTestStudent);
									dayTimeBlocks.getTimeBlocksTests().add(timeBlockTests);

									scheduleAssigned = true;
									break forEvaluationCenter;
								}
							} else {
								String tipo = evaluationAssignment.getStudentType() != null ? evaluationAssignment.getStudentType().getValue() : "Sin especificar";
								addTestScheduleLog(matterTestStudent, null, timeBlock, "No se pueden a\u00f1adir estudiantes del tipo \"" + tipo + "\" a la jornada de evaluaci\u00f3n.");
							}
						} else {
							addTestScheduleLog(matterTestStudent, null, timeBlock, "El " + getDateWeekDayCode(timeBlock.getStartDate()) + " no es uno de los d\u00edas permitidos para examinarse de la asignatura.");
						}
					} else {
						addTestScheduleLog(matterTestStudent, null, timeBlock, "La jornada de evaluaci\u00f3n no tiene plazas disponibles.");
					}
				}
			}
		}

		return scheduleAssigned;
	}

	public boolean assignTestToTimeBlock(MatterTestStudentInfo matterTestStudent, ClassroomTimeBlock timeBlock) {
		List<WeekDay> matterDaysAllowed = evaluationEventMatterService.getDaysAllowed(matterTestStudent.getEvaluationEventMatterId());
		List<EvaluationCenter> evaluationCentersAllowed = evaluationCenterService.findByEvaluationEventAndCenter(evaluationEvent.getId(), matterTestStudent.getCenterId());
		DayTimeBlocks dayTimeBlocks = getDayTimeBlocks(timeBlock.getStartDate());
		Hibernate.initialize(timeBlock.getStudentEvaluations());

		// Comprobamos si el estudiante esta asignado al bloque horario y si el bloque horario es válido para el test
		if (dayTimeBlocks != null) {
			for (TimeBlockTests timeBlockTests : dayTimeBlocks.getTimeBlocksTests()) {
				if (timeBlockTests.getTimeBlock().equals(timeBlock)) {
					// Comprobamos si el día es uno de los días permitidos para la asignatura
					if (dayTimeBlocks.isDayAllowed(matterDaysAllowed)) {
						// Comprobamos si el centro de evaluación asociado al día es uno de los centros permitidos
						if (dayTimeBlocks.isEvaluationCenterAllowed(evaluationCentersAllowed)) {
							// Comprobamos si se pueden añadir tests al día
							if (dayTimeBlocks.canAddTests()) {
								// Comprobamos si el bloque horario permite el tipo de estudiante
								if(timeBlockTests.allowsStudentType()) {
									// Comprobamos si se pueden añadir el test al bloque horario
									if (timeBlockTests.canAddTest()) {
										timeBlockTests.getTests().add(matterTestStudent);
										return true;
									}else {
										addTestScheduleLog(matterTestStudent, dayTimeBlocks, timeBlockTests.getTimeBlock(), "No se pueden a\u00f1adir m\u00e1s ex\u00e1menes a la jornada de evaluaci\u00f3n. Se ha alcanzado el n\u00famero m\u00e1ximo de ex\u00e1menes por jornada de evaluaci\u00f3n.");
										return false;
									}
								}  else {
									String tipo = evaluationAssignment.getStudentType() != null ? evaluationAssignment.getStudentType().getValue() : "Sin especificar";
									addTestScheduleLog(matterTestStudent, dayTimeBlocks, timeBlockTests.getTimeBlock(), "No se pueden a\u00f1adir estudiantes del tipo \"" + tipo + "\" a la jornada de evaluaci\u00f3n.");
									return false;
								}
							} else {
								addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "No se pueden a\u00f1adir m\u00e1s ex\u00e1menes al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(dayTimeBlocks.getDay()) + ". Se ha alcanzado el n\u00famero m\u00e1ximo de ex\u00e1menes por d\u00eda.");
								return false;
							}
						}  else {
							addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "El centro de evaluaci\u00f3n \"" + dayTimeBlocks.getEvaluationCenter().getName() + "\" no es uno de los centros permitidos para examinarse de la asignatura.");
							return false;
						}
					} else {
						addTestScheduleLog(matterTestStudent, dayTimeBlocks, null, "El " + dayTimeBlocks.getWeekDay() + " no es uno de los d\u00edas permitidos para examinarse de la asignatura.");
						return false;
					}
				}
			}
		}

		// Si el estudiante no esta asignado al bloque horario, comprobamos si podemos asignarlo y si el bloque horario es válido para el test
		// Comprobamos que el bloque horario tenga plazas disponibles
		if (timeBlock.getAvailableState().getValue().equals(AvailableState.AVAILABLE) && timeBlock.getAvailableSeats() > 0) {
			// Comprobamos si el bloque horario está en uno de los días permitidos para la asignatura
			if (isDayAllowed(getDateWeekDayCode(timeBlock.getStartDate()), matterDaysAllowed)) {
				// Comprobamos si el bloque horario permite el tipo de estudiante
				if (timeBlockAllowsStudentType(timeBlock)) {
					if (dayTimeBlocks == null) {
						dayTimeBlocks = new DayTimeBlocks(timeBlock.getStartDate(), timeBlock.getEvaluationCenter());
						daysTimeBlocks.add(dayTimeBlocks);
					}

					// Comprobamos si el bloque horario cumple todas las condiciones para poder añadirlo al día
					if (dayTimeBlocks.canAddTimeBlock(timeBlock, matterTestStudent)) {
						TimeBlockTests timeBlockTests = new TimeBlockTests(timeBlock);
						timeBlockTests.getTests().add(matterTestStudent);
						dayTimeBlocks.getTimeBlocksTests().add(timeBlockTests);

						return true;
					}
				} else {
					String tipo = evaluationAssignment.getStudentType() != null ? evaluationAssignment.getStudentType().getValue() : "Sin especificar";
					addTestScheduleLog(matterTestStudent, null, timeBlock, "No se pueden a\u00f1adir estudiantes del tipo \"" + tipo + "\" a la jornada de evaluaci\u00f3n.");
					return false;
				}
			} else {
				addTestScheduleLog(matterTestStudent, null, timeBlock, "El " + getDateWeekDayCode(timeBlock.getStartDate()) + " no es uno de los d\u00edas permitidos para examinarse de la asignatura.");
				return false;
			}
		} else {
			addTestScheduleLog(matterTestStudent, null, timeBlock, "La jornada de evaluaci\u00f3n no tiene plazas disponibles.");
			return false;
		}

		return false;
	}
	
	
	public void unifyStudentEvaluation(Long evaluationEventId) {
		List<MatterTestStudent> mtssFinal = new ArrayList<MatterTestStudent>();
		List<EvaluationAssignment> eas = evaluationAssignmentService.findByEvaluationEvent(evaluationEventId);
		int cont = 0;
		for(EvaluationAssignment ea : eas) {
			cont++;
//			if(cont%100==0) {
//				log.error("Proceso UN " + cont);
//				System.out.println("Proceso UN " + cont);
//			}
			List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationAssignment(ea);
			for(MatterTestStudent mts : mtss) {
				StudentEvaluation se = mts.getStudentEvaluation();
				if(se != null) {
					if(se.getClassroomTimeBlock() != null) {
						List<StudentEvaluation> svTmpList = studentEvaluationService.findByEvaluationAssignmentAndClassroomTimeBlockList(ea.getId(),
								se.getClassroomTimeBlock().getId());
						if(svTmpList != null && svTmpList.size() > 1) {
							StudentEvaluation mainSe = svTmpList.get(0);
							for(int i = 1; i < svTmpList.size(); i++) {
								List<MatterTestStudent> mtssToUpdate = matterTestStudentService.findByStudentEvaluation(svTmpList.get(i).getId());
								for(MatterTestStudent mtsToUpdate : mtssToUpdate) {
									mtsToUpdate.setStudentEvaluation(mainSe);
									mtssFinal.add(mtsToUpdate);
									matterTestStudentService.update(mtsToUpdate);
								}
							}
							// una vez actualizados los borramos
							for(int i = 1; i < svTmpList.size(); i++) {
								studentEvaluationService.delete(svTmpList.get(i).getId());
							}
						}
					}
				}
			}
		}
//		log.error("Proceso UN terminado");
//		System.out.println("Proceso UN terminado");
	}		
	

	public void assignScheduleToTestManual(List<StudentLoadCsvBean> scsvs, Long evaluationEventId) {
		
		try {
			EvaluationEvent evaluationEvent = new EvaluationEvent();
			evaluationEvent.setId(evaluationEventId);
			this.testScheduleLog = new ArrayList<StudentTestScheduleLog>();
			//this.markAllGenericScheduleLogAsReadByEvaluationEvent(evaluationEventId);
			this.deleteAllGenericScheduleLogByEvaluationEvent(evaluationEventId);
			int cont = 0;
			for(StudentLoadCsvBean scsv : scsvs) {
				cont++;
				// cargar el evaluationAssignmentMatter
				String identification = scsv.getIdentification();
				String matterCode = scsv.getMatterCode();
				String periodCode = scsv.getAcademicPeriodCode();
				String modeCode = scsv.getMode();
				
				EvaluationAssignmentMatter eam = evaluationAssignmentMatterService.findEvaluationEventMattersByStudentIdentification(evaluationEventId, identification, matterCode, periodCode, modeCode);
				// Existe la matricula
				if(eam != null) {
					// siguiente paso: cargar el bloque horario para esa hora esa fecha ese aula y ese centro de evaluacion
					String classroomCode = scsv.getClassroomCode();
					String startTime = scsv.getStartTime();
					String endTime = scsv.getEndTime();
					Date testDate = scsv.getTestDate();
					String centerCode = scsv.getEvaluationCenterCode();
					String conType = scsv.getConnection();
					String serverCode = scsv.getServerCode();
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:ss");
					SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
					String day = sdf2.format(testDate);
					String initDateStr = day + " " + startTime;
					String endDateStr = day + " " + endTime;
					
					Date startDate = null;
					Date endDate = null;
					
					try {
						startDate = sdf1.parse(initDateStr);
						endDate = sdf1.parse(endDateStr);
					} catch (Exception ex) {
						log.error(cont + "#: Error al parsear las fechas " + initDateStr + " - " + endDateStr);
						saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: Error al parsear las fechas " + initDateStr + " - " + endDateStr);
						continue;
					}
							
					ClassroomTimeBlock ctb = classroomTimeBlockService.findByClassroomTimeBlock(evaluationEventId, centerCode, classroomCode, startDate, endDate);			
					if(ctb != null) {
						// obtenemos todos los matterteststudent
						List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationAssignmentMatter(eam);
						// existe el ctb: comprobar que los parametros de red, cap y servidor son correctos, 
						// si no apuntmaos en el log y saltamos al siguiente
						Map<String, Object> connParams = this.checkConnectionParams(evaluationEventId, conType, classroomCode, serverCode, ctb, cont);
						

						if(mtss != null && mtss.size() > 0) {
							// si los parametros de conexion no son corrctos pasamos a la siguiente entrada
							if(connParams != null) {
								Net netParam = (Net)connParams.get("net");
								Server serverParam = (Server)connParams.get("server");
								
								// al ser carga manual no existen restricciones que comprobar asi que asignamos a todos ese ctb
								StudentEvaluation studentEvaluation = null;
								List<StudentEvaluation> studentEvaluationsToDelete = new ArrayList<StudentEvaluation>();
								for(MatterTestStudent mts : mtss) {
									// comprobamos si existia ya la asociacion, si no existe la creamos, en caso contrario sustituimos el bloque horario
									if(studentEvaluation == null && mts.getStudentEvaluation() == null) {
										// comprobamos si ya hay un studentevaluation creado para ese horario
										StudentEvaluation svTmp = studentEvaluationService.findByEvaluationAssignmentAndClassroomTimeBlock(eam.getEvaluationAssignment().getId(),
												ctb.getId());
										// si no hay cremos uno nuevo
										if(svTmp == null) {										
											studentEvaluation = new StudentEvaluation();
											studentEvaluation.setServer(serverParam);
											studentEvaluation.setNet(netParam);
											studentEvaluation.setClassroomTimeBlock(ctb);
											studentEvaluation.setEvaluationAssignment(eam.getEvaluationAssignment());
											studentEvaluationService.save(studentEvaluation);		
										} else { // si ya existe asignamos el mts a ese
											studentEvaluation = svTmp;
										}
									} else if (mts.getStudentEvaluation() != null) {	
										// comprobamos si el que tiene asociado es el mismo que se especifica con lo cual no habria que hacer nada
										StudentEvaluation svTmp = studentEvaluationService.findByEvaluationAssignmentAndClassroomTimeBlock(eam.getEvaluationAssignment().getId(),
												ctb.getId());
										// solo cambiamos si el SE no existe y no es el que esta asociado al mts
										// si no existe el nuevo bloque hay que crearlo
										if(svTmp == null) {
											studentEvaluation = new StudentEvaluation();
											studentEvaluation.setServer(serverParam);
											studentEvaluation.setNet(netParam);
											studentEvaluation.setClassroomTimeBlock(ctb);
											studentEvaluation.setEvaluationAssignment(eam.getEvaluationAssignment());
											studentEvaluationService.save(studentEvaluation);
										} else if (!svTmp.getId().equals(mts.getStudentEvaluation().getId())) { 
											// si hay bloque pero no es igual asignar el nuevo y borrar antiguo
											studentEvaluationsToDelete.add(mts.getStudentEvaluation());
											studentEvaluation = svTmp;
										} else { // si existe y es igual no hay que hacer nada
											studentEvaluation = svTmp;
											continue;
										}
									
									}
									
									mts.setStudentEvaluation(studentEvaluation);
									matterTestStudentService.update(mts);	
								}
								
								for(StudentEvaluation se : studentEvaluationsToDelete) {
									try {
										studentEvaluationService.delete(se.getId());
									} catch (Exception ex) {
										// es posible que exista
									}
								}
							}
						} else {
							log.error( cont + "#: El alumno " + identification  + " no tiene tests asignados" +
									" en la asignatura " + matterCode);						
							saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: El alumno " + identification  + " no tiene tests asignados" +
									" en la asignatura " + matterCode);
						}
	
					} else {
						// no existe ese bloque en ese centro en ese aula
						log.error( cont + "#: No existe la jornada " + initDateStr  + " - " + endDateStr + 
								" en el centro " + centerCode + " y el aula " + classroomCode);
						saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: No existe la jornada " + initDateStr  + " - " + endDateStr + 
								" en el centro " + centerCode + " y el aula " + classroomCode);
					}
					
				} else {
					// no hay matricula de este alumno en esa asignatura				
					log.error( cont + "#: No existe matrícula del alumno " + identification  + " en la asignatura " +
							matterCode );
					saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR,  cont + "#: No existe matr\u00edcula del alumno " + identification  + " en la asignatura " +
							matterCode);
				}
				
			}
			
			this.saveTestScheduleLog();
			this.clearTestScheduleLog();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
	}
	
	
	private Map<String, Object> checkConnectionParams(Long evaluationEventId, String conType, String classroomCode, String serverCode, ClassroomTimeBlock ctb, int cont) {
		EvaluationEvent evaluationEvent = new EvaluationEvent();
		evaluationEvent.setId(evaluationEventId);
		Map<String, Object> connParams = null;
		if(conType.equals(StudentLoadCsvBean.NET_CON_TYPE)) {
			Server serverFound = null;
			// si es de tipo RED comprobamos que el aula tenga una RED y esa red el servidor indicado
			Net net = ctb.getEvaluationEventClassroom().getNet();
			// el aula no tiene red asociada
			if(net == null) {
				log.error("El aula " + classroomCode + " no tiene red asociada");
				saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, "El aula " + classroomCode + " no tiene red asociada");
			} else {
				Set<Server> servers = net.getServers();
				for(Server serverTmp : servers) {
					if(serverTmp.getCode().equals(serverCode)) {
						serverFound = serverTmp;
						break;
					}
				}
				
				if(serverFound != null) {
					connParams = new HashMap<String, Object>();
					connParams.put("net", net);
					connParams.put("server", serverFound);
				} else {
					log.error(cont + "#: La red del aula " + classroomCode + " no tiene el servidor " + serverCode + " asociado");
					saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: La red del aula " + classroomCode + " no tiene el servidor " + serverCode + " asociado");
				}
			}				
		} else if (conType.equals(StudentLoadCsvBean.CAP_CON_TYPE) ){ // si es de tipo CAP
			// si es de tipo CAP no hace falta cargar el servidor, un CAP solo tiene uno						
			Cap cap = ctb.getEvaluationEventClassroom().getCap();
			if(cap == null) {
				log.error(cont + "#: El aula " + classroomCode + " no tiene cap asociado");
				saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: El aula " + classroomCode + " no tiene cap asociado");
			} else {
				if(!(cap.getServer() != null && cap.getServer().getCode().equals(serverCode))) {
					log.error(cont + "#: El servidor dado: " + serverCode + ", no coincide con el que tiene asociado el CAP del aula " + classroomCode);
					saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: El servidor dado: " + serverCode + ", no coincide con el que tiene asociado el CAP del aula " + classroomCode);
				} else {
					connParams = new HashMap<String, Object>();
					connParams.put("net", null);
					connParams.put("server", cap.getServer());
				}
			}
		} else {
			log.error(cont + "#: El campo conexión no tiene un valor correcto");
			saveGenericScheduleLog(evaluationEvent, Log.TYPE_ERROR, cont + "#: El campo 'Conexion' tiene un valor no válido");
		}
		
		// almacenamos red y servidor
		//studentEvaluationService.update(studentEvaluation);
		
		return connParams;
	}
	

	public boolean modifyTestsSchedules(List<TestScheduleModification> testsSchedulesModifications) {
		// Eliminamos todos los tests de los bloques horarios antiguos
		for (TestScheduleModification testScheduleModification : testsSchedulesModifications) {
			boolean removed = testScheduleModification.removeTestFromOldTimeBlock();

			if (!removed) {
				addTestScheduleLog(testScheduleModification.getTest(), null, testScheduleModification.getOldTimeBlock(), "No se puede eliminar el test de la jornada de evaluaci\u00f3n.");
				return false;
			}
		}

		// Asignamos todos los tests a los nuevos bloques horarios
		for (TestScheduleModification testScheduleModification : testsSchedulesModifications) {
			boolean assigned = testScheduleModification.assignTestToNewTimeBlock();

			if (!assigned) {
				return false;
			}
		}

		return true;
	}

	public boolean removeTestFromTimeBlock(MatterTestStudentInfo matterTestStudent, ClassroomTimeBlock timeBlock) {
		DayTimeBlocks dayTimeBlocks = getDayTimeBlocks(timeBlock.getStartDate());

		if (dayTimeBlocks != null) {
			for (TimeBlockTests timeBlockTests : dayTimeBlocks.getTimeBlocksTests()) {
				if (timeBlockTests.getTimeBlock().equals(timeBlock)) {
					for (MatterTestStudentInfo test : timeBlockTests.getTests()) {
						if (test.getId().equals(matterTestStudent.getId())) {
							timeBlockTests.getTests().remove(test);
							timeBlockTests.getRemovedTests().add(test);
							matterTestStudent.setStudentEvaluationId(null);

							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static Date setTimeToMidnight(Date date) {
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return calendar.getTime();
	}

	private boolean isDayAllowed(String weekDayCode, List<WeekDay> daysAllowed) {
		if (daysAllowed.isEmpty()) {
			return true;
		}

		for (WeekDay dayAllowed : daysAllowed) {
			if (dayAllowed.getCode().equals(weekDayCode)) {
				return true;
			}
		}

		return false;
	}

	private boolean timeBlockAllowsStudentType(ClassroomTimeBlock timeBlock) {
		if (timeBlock.getStudentTypes().isEmpty()) {
			return true;
		}

		for (StudentType studentType : timeBlock.getStudentTypes()) {
			if (studentType.equals(evaluationAssignment.getStudentType())) {
				return true;
			}
		}

		return false;
	}

	private String getDateWeekDayCode(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		    case Calendar.MONDAY: return WeekDay.LUNES;
		    case Calendar.TUESDAY: return WeekDay.MARTES;
		    case Calendar.WEDNESDAY: return WeekDay.MIERCOLES;
		    case Calendar.THURSDAY: return WeekDay.JUEVES;
		    case Calendar.FRIDAY: return WeekDay.VIERNES;
		    case Calendar.SATURDAY: return WeekDay.SABADO;
		    case Calendar.SUNDAY: return WeekDay.DOMINGO;
		    default: return null;
		}
	}

	private String getDateWeekDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		    case Calendar.MONDAY: return "Lunes";
		    case Calendar.TUESDAY: return "Martes";
		    case Calendar.WEDNESDAY: return "Miércoles";
		    case Calendar.THURSDAY: return "Jueves";
		    case Calendar.FRIDAY: return "Viernes";
		    case Calendar.SATURDAY: return "Sábado";
		    case Calendar.SUNDAY: return "Domingo";
		    default: return null;
		}
	}

	private void addTestScheduleLog(MatterTestStudentInfo matterTestStudent, DayTimeBlocks dayTimeBlocks, ClassroomTimeBlock timeBlock, String message) {
		JSONObject extra = new JSONObject();
		if (dayTimeBlocks != null) {
			extra.put("day", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dayTimeBlocks.getDay()));
			extra.put("dayEvaluationCenterId", dayTimeBlocks.getEvaluationCenter().getId());
			extra.put("dayEvaluationCenterName", dayTimeBlocks.getEvaluationCenter().getName());
		}

		MatterTestStudent matterTestStudentAux = new MatterTestStudent();
		matterTestStudentAux.setId(matterTestStudent.getId());

		StudentTestScheduleLog log = new StudentTestScheduleLog();
		log.setEvaluationAssignment(evaluationAssignment);
		log.setMatterTestStudent(matterTestStudentAux);
		log.setClassroomTimeBlock(timeBlock);
		log.setMessage(message);
		if (extra.length() > 0) {
			log.setExtra(extra.toString());
		}
		log.setType(Log.TYPE_INFO);
		testScheduleLog.add(log);
	}
	
	// si es manual el EA viene por parametro
	private void addTestScheduleLogManual(MatterTestStudent matterTestStudent, EvaluationAssignment evaluationAssignment, 
			DayTimeBlocks dayTimeBlocks, ClassroomTimeBlock timeBlock, String message) {
		JSONObject extra = new JSONObject();
		if (dayTimeBlocks != null) {
			extra.put("day", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dayTimeBlocks.getDay()));
			extra.put("dayEvaluationCenterId", dayTimeBlocks.getEvaluationCenter().getId());
			extra.put("dayEvaluationCenterName", dayTimeBlocks.getEvaluationCenter().getName());
		}

		MatterTestStudent matterTestStudentAux = new MatterTestStudent();
		matterTestStudentAux.setId(matterTestStudent.getId());

		StudentTestScheduleLog log = new StudentTestScheduleLog();
		log.setEvaluationAssignment(evaluationAssignment);
		log.setMatterTestStudent(matterTestStudentAux);
		log.setClassroomTimeBlock(timeBlock);
		log.setMessage(message);
		if (extra.length() > 0) {
			log.setExtra(extra.toString());
		}
		log.setType(Log.TYPE_INFO);
		testScheduleLog.add(log);
	}

	
	

	public void printTestsSchedules() {
		//System.out.println("");
		//System.out.println("Horario de ex\u00e1menes de " + evaluationAssignment.getUser().getFullName());

		for (DayTimeBlocks dayTimeblocks : daysTimeBlocks) {
			System.out.println("Dia: " + new SimpleDateFormat("dd-MM-yyyy").format(dayTimeblocks.getDay()) + " en el centro de evaluaci\u00f3n \"" + dayTimeblocks.getEvaluationCenter().getName() + "\"");

			for (TimeBlockTests timeBlockTests : dayTimeblocks.getTimeBlocksTests()) {
				if (timeBlockTests.hasTests()) {
					System.out.println("\t Hora: " + new SimpleDateFormat("HH:mm").format(timeBlockTests.getTimeBlock().getStartDate()) + " a " + new SimpleDateFormat("HH:mm").format(timeBlockTests.getTimeBlock().getEndDate()) + " en el aula " + timeBlockTests.getTimeBlock().getEvaluationEventClassroom().getClassroom().getName());

					for (MatterTestStudentInfo test : timeBlockTests.getTests()) {
						System.out.println("\t\t" + test.getMatterName());
					}
				}
			}
		}
	}

	public void saveTestsSchedules(HashMap<Long, EvaluationCenterTimeBloks> evaluationCentersTimeBloks) {
		// Eliminamos de la base de datos los horarios de los tests eliminados
		for (DayTimeBlocks dayTimeBlocks : daysTimeBlocks) {
			for (TimeBlockTests timeBlockTests : dayTimeBlocks.getTimeBlocksTests()) {
				List<MatterTestStudentInfo> removedTests = timeBlockTests.getRemovedTests();

				if (!removedTests.isEmpty()) {
					StudentEvaluation studentEvaluation = studentEvaluationService.findByEvaluationAssignmentAndClassroomTimeBlock(evaluationAssignment.getId(), timeBlockTests.getTimeBlock().getId());
					Hibernate.initialize(studentEvaluation.getMatterTests());

					for (MatterTestStudentInfo removedTest : removedTests) {
						matterTestStudentService.deleteStudentEvaluation(removedTest.getId());
						removedTest.setStudentEvaluationId(null);
						studentEvaluation.getMatterTests().remove(matterTestStudentService.findById(removedTest.getId()));
					}

					if (studentEvaluation.getMatterTests().isEmpty()) {
						// Comprobamos si hay que actualizar el estado del bloque horario
						ClassroomTimeBlock timeBlock = timeBlockTests.getTimeBlock();
						Hibernate.initialize(timeBlock.getStudentEvaluations());
						timeBlock.getStudentEvaluations().remove(studentEvaluation);
						if (timeBlock.getAvailableState().getValue().equals(AvailableState.FULL) && timeBlock.getAvailableSeats() > 0) {
							timeBlock.setAvailableState(availableStateService.findByState(AvailableState.AVAILABLE));
							classroomTimeBlockService.update(timeBlock);
							
							if (evaluationCentersTimeBloks != null) {
								evaluationCentersTimeBloks.get(timeBlock.getEvaluationCenter().getId()).updateTimeBlockState(timeBlock);
							}
						}

						studentEvaluationService.delete(studentEvaluation.getId());
					}
				}
			}
		}

		// Guardamos en base de datos los horarios de los tests
		for (DayTimeBlocks dayTimeBlocks : daysTimeBlocks) {
			for (TimeBlockTests timeBlockTests : dayTimeBlocks.getTimeBlocksTests()) {
				List<MatterTestStudentInfo> testsWithoutSchedule = timeBlockTests.getTestsWithoutSchedule();

				if (!testsWithoutSchedule.isEmpty()) {
					ClassroomTimeBlock timeBlock = timeBlockTests.getTimeBlock();
					StudentEvaluation studentEvaluation = studentEvaluationService.findByEvaluationAssignmentAndClassroomTimeBlock(evaluationAssignment.getId(), timeBlock.getId());

					if (studentEvaluation == null) {
						studentEvaluation = new StudentEvaluation();
						studentEvaluation.setClassroomTimeBlock(timeBlock);
						studentEvaluation.setEvaluationAssignment(evaluationAssignment);
						studentEvaluationService.save(studentEvaluation);

						// Comprobamos si hay que actualizar el estado del bloque horario
						Hibernate.initialize(timeBlock.getStudentEvaluations());
						timeBlock.getStudentEvaluations().add(studentEvaluation);
						if (timeBlock.getAvailableSeats() <= 0) {
							timeBlock.setAvailableState(availableStateService.findByState(AvailableState.FULL));
							classroomTimeBlockService.update(timeBlock);
							
							if (evaluationCentersTimeBloks != null) {
								evaluationCentersTimeBloks.get(timeBlock.getEvaluationCenter().getId()).updateTimeBlockState(timeBlock);
							}
						}
					}

					matterTestStudentService.updateStudentEvaluation(studentEvaluation, testsWithoutSchedule);
				}
			}
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processAssignment(EvaluationAssignment evaluationAssignment, 
			HashMap<Long, EvaluationCenterTimeBloks> evaluationCentersTimeBloks,
			List<MatterTestStudentInfo> evaluationAssignmentMatterTestStudents,
			int count, int totalAssignments) throws Exception {
		
//		long startTime = System.currentTimeMillis();
		this.setEvaluationAssignment(evaluationAssignment);
		
		if (evaluationAssignmentMatterTestStudents != null) {
			for (MatterTestStudentInfo matterTestStudent : evaluationAssignmentMatterTestStudents) {
				if (matterTestStudent.getCenterId() != null) {
					List<EvaluationCenter> evaluationCentersAllowed = evaluationCenterService.findByEvaluationEventAndCenter(evaluationEvent.getId(), matterTestStudent.getCenterId());
					
					if (evaluationCentersHaveTimeBlocks(evaluationCentersAllowed, evaluationCentersTimeBloks)) {
						List<WeekDay> matterDaysAllowed = evaluationEventMatterService.getDaysAllowed(matterTestStudent.getEvaluationEventMatterId());

						boolean scheduleAssigned = this.assignScheduleToTest(matterTestStudent, matterDaysAllowed, evaluationCentersAllowed, evaluationCentersTimeBloks);
						
						if (!scheduleAssigned) {
							this.saveTestScheduleLog();
						}
						
						this.clearTestScheduleLog();
					} else {
						Center center = centerService.findById(matterTestStudent.getCenterId());
						throw new Exception("El centro \"" + center.getName() + "\" no tiene asociado ningún centro de evaluación con horarios definidos");
					}
				} else {
					throw new Exception("El estudiante \"" + evaluationAssignment.getUser().getFullName() + "\" no está asociado a ningún centro para la asignatura \"" + matterTestStudent.getMatterName() + "\"");
				}
			}
			
			this.saveTestsSchedules(evaluationCentersTimeBloks);
		}

//		long endTime = System.currentTimeMillis();
//		System.out.println("Procesado de matricula " + count + " de " + totalAssignments + " en " + (endTime - startTime) + " milisegundos");
	}
	
	private boolean evaluationCentersHaveTimeBlocks(List<EvaluationCenter> evaluationCenters, HashMap<Long, EvaluationCenterTimeBloks> evaluationCentersTimeBloks) {
		for (EvaluationCenter evaluationCenter : evaluationCenters) {
			if (evaluationCentersTimeBloks.get(evaluationCenter.getId()).hasTimeBlocks()) {
				return true;
			}
		}
		
		return false;
	}

	public void clearTestScheduleLog() {
		testScheduleLog = new ArrayList<StudentTestScheduleLog>();
	}

	public void saveTestScheduleLog() {
		for (StudentTestScheduleLog log : testScheduleLog) {
			studentTestScheduleLogService.save(log);
		}
	}
	
	public void saveGenericScheduleLog(EvaluationEvent evaluationEvent, String type, String message) {
		GenericScheduleLog gsl = new GenericScheduleLog();
		gsl.setEvaluationEvent(evaluationEvent);
		gsl.setMessage(message);
		gsl.setType(type);
		genericScheduleLogService.save(gsl);
	}

	public void markAllTestScheduleLogAsRead() {
		studentTestScheduleLogService.markAllAsRead();
	}
	
	public void deleteLogByEvaluationEvent(Long evaluationEventId) {
		studentTestScheduleLogService.deleteByEvaluationEvent(evaluationEventId);
	}
	
	public void markAllTestScheduleLogAsReadByEvaluationEvent(Long evaluationEventId) {
		studentTestScheduleLogService.markAsReadByEvaluationEvent(evaluationEventId);
	}
	
	public void markAllGenericScheduleLogAsReadByEvaluationEvent(Long evaluationEventId) {
		genericScheduleLogService.markAsReadByEvaluationEvent(evaluationEventId);
	}
	
	public void deleteAllGenericScheduleLogByEvaluationEvent(Long evaluationEventId) {
		genericScheduleLogService.deleteByEvaluationEvent(evaluationEventId);
	}

	public class DayTimeBlocks {

		private Date day;
		private EvaluationCenter evaluationCenter;
		private List<TimeBlockTests> timeBlocksTests;

		public DayTimeBlocks(Date day, EvaluationCenter evaluationCenter) {
			this.day = setTimeToMidnight(day);
			this.evaluationCenter = evaluationCenter;
			this.timeBlocksTests = new ArrayList<TimeBlockTests>();
		}

		public Date getDay() {
			return setTimeToMidnight(day);
		}

		public List<TimeBlockTests> getTimeBlocksTests() {
			return timeBlocksTests;
		}

		public String getWeekDay() {
			return getDateWeekDay(day);
		}

		public String getWeekDayCode() {
			return getDateWeekDayCode(day);
		}

		public EvaluationCenter getEvaluationCenter() {
			return evaluationCenter;
		}

		public int getNumTests() {
			int numTests = 0;

			for (TimeBlockTests timeBlockTests : timeBlocksTests) {
				numTests += timeBlockTests.getNumTests();
			}

			return numTests;
		}

		public boolean hasTimeBlock(ClassroomTimeBlock timeBlock) {
			for (TimeBlockTests timeBlockTests : timeBlocksTests) {
				if (timeBlockTests.getTimeBlock().equals(timeBlock)) {
					return true;
				}
			}

			return false;
		}

		public boolean isTimeBlockOverLapped(ClassroomTimeBlock timeBlock) {
			Interval interval = new Interval(new DateTime(timeBlock.getStartDate()), new DateTime(timeBlock.getEndDate()));

			for (TimeBlockTests timeBlockTests : timeBlocksTests) {
				if (timeBlockTests.hasTests()) {
					Interval interval2 = new Interval(new DateTime(timeBlockTests.getTimeBlock().getStartDate()), new DateTime(timeBlockTests.getTimeBlock().getEndDate()));

					if (interval.overlaps(interval2)) {
						return true;
					}
				}
			}

			return false;
		}

		public boolean isDayAllowed(List<WeekDay> daysAllowed) {
			return StudentTestsSchedules.this.isDayAllowed(getWeekDayCode(), daysAllowed);
		}

		public boolean isEvaluationCenterAllowed(List<EvaluationCenter> evaluationCentersAllowed) {
			for (EvaluationCenter evaluationCenterAllowed : evaluationCentersAllowed) {
				if (evaluationCenterAllowed.equals(evaluationCenter)) {
					return true;
				}
			}

			return false;
		}

		public boolean canAddTests() {
			// Comprobamos que no se haya superado el numero máximo de tests para el día
			if (getNumTests() < evaluationEventConfiguration.getMaxDailyTests()) {
				return true;
			}

			return false;
		}

		public boolean canAddTimeBlock(ClassroomTimeBlock timeBlock, MatterTestStudentInfo matterTestStudent) {
			// Comprobamos si puede haber varios bloques horarios el mismo dia o si no hay bloques horarios
			if (evaluationEventConfiguration.isTestsInSeveralTimeBlocksSameDay() || timeBlocksTests.isEmpty()) {
				// Comprobamos que el centro de evaluación es el mismo que el del bloque horario
				if (evaluationCenter.equals(timeBlock.getEvaluationCenter())){
					// Comprobamos que se pueden añadir tests al día
					if (canAddTests()){
						// Comprobamos que el bloque horario no está ya asignado al día
						if(!hasTimeBlock(timeBlock)) {
							// Comprobamos que el bloque horario no se solape con otros bloques horarios
							if (!isTimeBlockOverLapped(timeBlock)) {
								return true;
							} else {
								addTestScheduleLog(matterTestStudent, this, timeBlock, "No se puede a\u00f1adir la jornada de evaluaci\u00f3n al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(day) + ". La jornada de evaluaci\u00f3n coincide con otro ya asignado al d\u00eda.");
							}
						}
					} else {
						addTestScheduleLog(matterTestStudent, this, timeBlock, "No se pueden a\u00f1adir m\u00e1s ex\u00e1menes al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(day) + ". Se ha alcanzado el n\u00famero m\u00e1ximo de ex\u00e1menes por d\u00eda.");
					}
				} else {
					addTestScheduleLog(matterTestStudent, this, timeBlock, "No se puede a\u00f1adir la jornada de evaluaci\u00f3n al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(day) + ". El centro de evaluaci\u00f3n \"" + timeBlock.getEvaluationCenter().getName() + "\" es distinto al centro asignado al d\u00eda.");
				}
			} else {
				addTestScheduleLog(matterTestStudent, this, timeBlock, "No se puede a\u00f1adir la jornada de evaluaci\u00f3n al d\u00eda " + new SimpleDateFormat("dd-MM-yyyy").format(day) + ". No puede haber varios bloques horarios el mismo d\u00eda.");
			}

			return false;
		}
	}

	public class TimeBlockTests {

		private ClassroomTimeBlock timeBlock;
		private List<MatterTestStudentInfo> tests;
		private List<MatterTestStudentInfo> removedTests;

		public TimeBlockTests(ClassroomTimeBlock timeBlock) {
			this.timeBlock = timeBlock;
			this.tests = new ArrayList<MatterTestStudentInfo>();
			this.removedTests = new ArrayList<MatterTestStudentInfo>();
		}

		public ClassroomTimeBlock getTimeBlock() {
			return timeBlock;
		}

		public List<MatterTestStudentInfo> getTests() {
			return tests;
		}

		public List<MatterTestStudentInfo> getRemovedTests() {
			return removedTests;
		}

		public int getNumTests() {
			return tests.size();
		}

		public boolean hasTests() {
			return !tests.isEmpty();
		}

		public boolean allowsStudentType() {
			return timeBlockAllowsStudentType(timeBlock);
		}

		public boolean canAddTest() {
			// Comprobamos si el número de tests del bloque horario no supera el máximo permitido por la configuración del evento
			if (getNumTests() < evaluationEventConfiguration.getMaxTimeBlockTests()) {
				// Comprobamos que el tiempo total de los tests no supera el tiempo del bloque horario
				if ((getNumTests() + 1) * evaluationEvent.getTestDuration() <= timeBlock.getMinutes()) {
					return true;
				}
			}

			return false;
		}

		private List<MatterTestStudentInfo> getTestsWithoutSchedule() {
			List<MatterTestStudentInfo> testsWithoutSchedule = new ArrayList<MatterTestStudentInfo>();

			for (MatterTestStudentInfo test : tests) {
				if (test.getStudentEvaluationId() == null) {
					testsWithoutSchedule.add(test);
				}
			}

			return testsWithoutSchedule;
		}
	}

	public class TestScheduleModification {

		private ClassroomTimeBlock oldTimeBlock;
		private ClassroomTimeBlock newTimeBlock;
		private MatterTestStudentInfo test;

		public TestScheduleModification(MatterTestStudentInfo test, ClassroomTimeBlock oldTimeBlock, ClassroomTimeBlock newTimeBlock) {
			this.test = test;
			this.oldTimeBlock = oldTimeBlock;
			this.newTimeBlock = newTimeBlock;
		}

		public MatterTestStudentInfo getTest() {
			return test;
		}

		public ClassroomTimeBlock getOldTimeBlock() {
			return oldTimeBlock;
		}

		public ClassroomTimeBlock getNewTimeBlock() {
			return newTimeBlock;
		}

		public boolean removeTestFromOldTimeBlock() {
			return removeTestFromTimeBlock(test, oldTimeBlock);
		}

		public boolean assignTestToNewTimeBlock() {
			return assignTestToTimeBlock(test, newTimeBlock);
		}
	}
}
