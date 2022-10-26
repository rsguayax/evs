package es.grammata.evaluation.evs.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.grammata.evaluation.evs.data.model.repository.Address;
import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.DocFile;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventType;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.model.repository.TimeBlock;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.DocFileService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventConfigurationService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTypeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationTypeService;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.data.services.repository.TimeBlockService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.CenterReport;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomReport;
import es.grammata.evaluation.evs.mvc.controller.util.Journey;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentExtendedDocReport;
import es.grammata.evaluation.evs.mvc.controller.util.StudentExtendedMatterDocReport;
import es.grammata.evaluation.evs.mvc.controller.util.StudentListElementReport;
import es.grammata.evaluation.evs.mvc.controller.util.StudentReport;
import es.grammata.evaluation.evs.mvc.controller.util.TimeBlockReport;
import es.grammata.evaluation.evs.mvc.validator.EvaluationEventValidator;
import es.grammata.evaluation.evs.util.FileUtil;
import es.grammata.evaluation.evs.util.GenerateDocumentsPdf;
import es.grammata.evaluation.evs.util.MailUtil;

@Controller
public class EvaluationEventController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(EvaluationEventController.class);

	private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("es", "ES"));

	private static final SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm");

	private static int DOC_PROCESS_ACTIVE = 0;
	private static int DOC_PROCESS_ACTIVE2 = 0;

	@Autowired
	EvaluationEventValidator evaluationEventValidator;

	@Autowired
	private EvaluationEventTypeService evaluationEventTypeService;

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private EvaluationCenterService evaluationCenterService;

	@Autowired
	private EvaluationTypeService evaluationTypeService;

	@Autowired
	private EvaluationEventConfigurationService evaluationEventConfigurationService;

	@Autowired
	EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;

	@Autowired
	private ClassroomTimeBlockService classroomTimeBlockService;

	@Autowired
	private EvaluationEventClassroomService evaluationEventClassroomService;

	@Autowired
	private TimeBlockService timeBlockService;

	@Autowired
	private StudentEvaluationService studentEvaluationService;

	@Autowired
	private DocFileService docFileService;

	@Autowired
	private UserService userService;

	@Autowired
	ServletContext context;

	@Value("${doc.path}")
	private String DOC_PATH;

	@Value("${doc.base_path}")
	private String DOC_BASE_PATH;

	@Value("${mail.email}")
	private String EMAIL_FROM;

	@Autowired
	private JavaMailSender mailSender;

	private Map<String, EvaluationType> evaluationTypeCache;
	private Map<String, EvaluationEventType> evaluationEventTypeCache;

	// List<EvaluationEventType> evaluationEventTypesList;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping("/evaluationevent")
	public String showList(Map<String, Object> model) {
		model.put("headText", "Eventos de evaluaci\u00f3n");

		List<Object[]> evaluationEvents = this.evaluationEventService.findAllWithStudentCount();

		model.put("evaluationEvents", evaluationEvents);

		return "evaluation_event/evaluation-event-list";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/evaluationevents", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Object[]> loadEvaluationEvents() {
		List<Object[]> evaluationEvents = this.evaluationEventService.findAllWithStudentCount();
		return evaluationEvents;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/new", method = { RequestMethod.POST, RequestMethod.GET })
	public String create(HttpServletRequest request, Map<String, Object> model, @Valid EvaluationEvent evaluationEvent,
			BindingResult bindingResult) {
		if (request.getMethod().equals("POST")) {
			evaluationEventValidator.validate(evaluationEvent, bindingResult);

			if (!bindingResult.hasErrors()) {
				this.evaluationEventService.save(evaluationEvent);
				EvaluationEventConfiguration evaluationEventConfiguration = new EvaluationEventConfiguration(
						evaluationEvent);
				evaluationEventConfigurationService.save(evaluationEventConfiguration);

				return "redirect:/evaluationevent/edit/" + evaluationEvent.getId() + "/config?create=1";
			}
		} else {
			evaluationEvent = new EvaluationEvent();
		}

		model.put("evaluationEvent", evaluationEvent);
		model.put("evaluationCenters", this.evaluationCenterService.findAll());
		model.put("edit", false);
		model.put("headText", "Nuevo evento de evaluaci\u00f3n");
		model.put("evaluationTypes", loadTypes());
		model.put("evaluationEventTypes", loadEvaluationEventTypes());

		return "evaluation_event/evaluation-event-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	public String edit(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id,
			@Valid EvaluationEvent evaluationEvent, BindingResult bindingResult) {
		if (request.getMethod().equals("POST")) {
			evaluationEventValidator.validate(evaluationEvent, bindingResult);

			if (!bindingResult.hasErrors()) {
				this.evaluationEventService.update(evaluationEvent);
				return "redirect:/evaluationevent";
			}
		} else {
			evaluationEvent = this.evaluationEventService.findById(id);
		}

		model.put("evaluationEvent", evaluationEvent);
		model.put("evaluationCenters", this.evaluationCenterService.findAll());
		model.put("edit", true);
		model.put("headText", "Edici\u00f3n del evento de evaluaci\u00f3n \"" + evaluationEvent.getName() + "\"");
		model.put("evaluationTypes", loadTypes());
		model.put("evaluationEventTypes", loadEvaluationEventTypes());

		return "evaluation_event/evaluation-event-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/student-listings", method = RequestMethod.GET)
	public String studentListings(@PathVariable Long id, Map<String, Object> model) throws Exception {
		EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
		if (evaluationEvent != null) {
			model.put("evaluationEvent", evaluationEvent);
		}
		return "evaluation_event/student-listings";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/delete", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Message deleteEvaluationEvent(@PathVariable Long id) {
		Message message = new Message();
		try {
			this.evaluationEventService.delete(id);
			message.setMessage("Evento eliminado correctamente");
			message.setType(Message.TYPE_SUCCESS);
		} catch (Exception ex) {
			Throwable t = ex.getCause();
			while ((t != null) && !(t instanceof ConstraintViolationException)) {
				t = t.getCause();
			}

			message.setType(Message.TYPE_ERROR);
			if (t instanceof ConstraintViolationException) {
				message.setMessage("No se ha podido eliminar, compruebe que no existan entidades asociadas.");
			} else {
				message.setMessage("Error al eliminar evento de evaluación");
			}
		}

		return message;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/evaluationtypes", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Set<EvaluationType> listEvaluationEventTypes(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Set<EvaluationType> types = evaluationEvent.getEvaluationTypes();
		return types;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/evaluationcenters", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Set<EvaluationCenter> listEvaluationEventCenters(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		Hibernate.initialize(evaluationEvent.getEvaluationEventEvaluationCenters());
		Set<EvaluationCenter> evaluationCenters = evaluationEvent.getEvaluationCenters();
		return evaluationCenters;
	}

	@Transactional
	@RequestMapping(value = "/evaluationevent/{id}/claasroomjourneys", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Journey> listClassroomJourneys(@PathVariable Long id) {
		List<ClassroomTimeBlock> crtms = classroomTimeBlockService.findByEvaluationEvent(id);

		List<Journey> journeys = new ArrayList<Journey>();
		for (ClassroomTimeBlock crtb : crtms) {
			Journey journey = new Journey();
			journey.setId(crtb.getId());
			journey.setStartDate(crtb.getStartDate());
			journey.setEndDate(crtb.getEndDate());
			if (journeys.size() == 0) {
				journeys.add(journey);
			} else {
				boolean exists = false;
				for (Journey journeyAux : journeys) {
					if (journeyAux.getStartDate().compareTo(journey.getStartDate()) == 0
							&& journeyAux.getEndDate().compareTo(journey.getEndDate()) == 0) {
						exists = true;
					}
				}
				if (!exists) {
					journeys.add(journey);
				}
			}
		}

		return journeys;
	}

	@Transactional
	@RequestMapping(value = "/evaluationevent/{id}/journeys", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<TimeBlock> listJourneys(@PathVariable Long id) {
		List<TimeBlock> timeBlocks = timeBlockService.findRelatedByEvaluationEvent(id);
		return timeBlocks;
	}

	@RequestMapping(value = "/evaluationevent/{id}/doc/zipfile", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody DocFile loadZipfile(@PathVariable Long id) {
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		DocFile docFile = docFileService.findEventFile(evaluationEvent.getCode());
		return docFile;
	}

	@RequestMapping(value = "/evaluationevent/{id}/eventcenter/{eventCenterId}/files", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<DocFile> listCenterFiles(@PathVariable Long id, @PathVariable Long eventCenterId) {
		EvaluationEventEvaluationCenter eeec = evaluationEventEvaluationCenterService.findById(eventCenterId);
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		List<DocFile> docFiles = docFileService.findByCenterCode(evaluationEvent.getCode(),
				eeec.getEvaluationCenter().getCode());
		return docFiles;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/listado-estudiantes-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}.pdf", method = RequestMethod.GET)
	public ModelAndView studentListingsPdf(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) throws Exception {
		ModelAndView mav = new ModelAndView("studentListingsPdfView");

		EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
		if (evaluationEvent != null) {
			String evaluationEventName = evaluationEvent.getName();
			mav.addObject("evaluationEventName", evaluationEventName); // 1

			EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
					.findById(eventCenterId);
			if (evaluationEventEvaluationCenter != null) {
				mav.addObject("evaluationCenterName", evaluationEventEvaluationCenter.getEvaluationCenter().getName()); // 2

				EvaluationEventClassroom evaluationEventClassroom = evaluationEventClassroomService
						.findById(eventClassroomId);
				if (evaluationEventClassroom != null) {
					mav.addObject("classroomName", evaluationEventClassroom.getClassroom().getName()); // 3

					ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService.findById(classroomTimeBlockId);
					if (classroomTimeBlock != null) {
						Date startDate = classroomTimeBlock.getStartDate();
						String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
						mav.addObject("strDate", strDate); // 4

						String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";
						mav.addObject("strStartTime", strStartTime); // 5

						Date endDate = classroomTimeBlock.getEndDate();
						String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";
						mav.addObject("strEndTime", strEndTime); // 6

						Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
						Set<StudentEvaluation> studentEvaluations = classroomTimeBlock.getStudentEvaluations();
						if (studentEvaluations != null) {
							List<Object[]> rows = new ArrayList<Object[]>();
							int i = 0;
							for (StudentEvaluation studentEvaluation : studentEvaluations) {
								User student = studentEvaluation.getEvaluationAssignment().getUser();
								Object[] row = new Object[] { i + 1, student.getIdentification(),
										student.getFullNameForListings().toUpperCase(), "" };
								rows.add(row);
								i++;
							}
							mav.addObject("rows", rows); // 7
						}
					}
				}
			}
		}

		return mav;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/listado-estudiantes-extendido-no-order-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}.pdf", method = RequestMethod.GET)
	public ModelAndView studentListingsPdfExtendedNoOrder(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) throws Exception {
		ModelAndView mav = new ModelAndView("studentListingsPdfView");

		EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
		if (evaluationEvent != null) {
			String evaluationEventName = evaluationEvent.getName();
			mav.addObject("evaluationEventName", evaluationEventName); // 1

			if (classroomTimeBlockId > 0 && eventCenterId == 0 && eventClassroomId == 0) {
				mav.addObject("type", 2);
				Map<String, CenterReport> centersHash = new HashMap<String, CenterReport>();

				ClassroomTimeBlock crtbParameter = classroomTimeBlockService.findById(classroomTimeBlockId);

				List<ClassroomTimeBlock> classroomTimeBlocks = classroomTimeBlockService
						.findByDates(crtbParameter.getStartDate(), crtbParameter.getEndDate(), id);

				for (ClassroomTimeBlock classroomTimeBlock : classroomTimeBlocks) {
					String centerName = classroomTimeBlock.getEvaluationCenter().getName();
					String classroomName = classroomTimeBlock.getEvaluationEventClassroom().getClassroom().getName();
					List<TimeBlockReport> timeBlocks = new ArrayList<TimeBlockReport>();

					List<ClassroomReport> classrooms = null;
					CenterReport centerReport = null;
					if (centersHash.containsKey(centerName)) {
						centerReport = centersHash.get(centerName);
						classrooms = centerReport.getClassrooms();
					} else {
						centerReport = new CenterReport();
						centerReport.setName(centerName);
						classrooms = new ArrayList<ClassroomReport>();
						centerReport.setClassrooms(classrooms);
						centersHash.put(centerName, centerReport);
					}

					TimeBlockReport timeBlock = new TimeBlockReport();

					Date startDate = classroomTimeBlock.getStartDate();
					String strDate = startDate != null ? DATE_SDF.format(startDate) : "";

					timeBlock.setDate(strDate);

					String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";

					timeBlock.setInitDate(strStartTime);

					Date endDate = classroomTimeBlock.getEndDate();
					String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";

					timeBlock.setEndDate(strEndTime);

					Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
					Set<StudentEvaluation> studentEvaluations = classroomTimeBlock.getStudentEvaluations();
					if (studentEvaluations != null) {
						List<Object[]> rows = new ArrayList<Object[]>();
						int i = 0;
						for (StudentEvaluation studentEvaluation : studentEvaluations) {
							User student = studentEvaluation.getEvaluationAssignment().getUser();
							Object[] row = new Object[] { i + 1, student.getIdentification(),
									student.getFullNameForListings().toUpperCase(), "" };
							rows.add(row);
							i++;
						}

						Collections.sort(rows, new Comparator<Object[]>() {
							public int compare(Object[] o1, Object[] o2) {
								String cad1 = ((String) o1[2]).toUpperCase();
								String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
								String cad2 = ((String) o2[2]).toUpperCase();
								String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

								return (cad1Clean).compareTo(cad2Clean);
							}
						});

						timeBlock.setUsers(rows);
					}

					ClassroomReport crr = new ClassroomReport();
					crr.setName(classroomName);
					timeBlocks.add(timeBlock);
					crr.setTimeblocks(timeBlocks);
					classrooms.add(crr);

					centerReport.setClassrooms(classrooms);

				}

				List<CenterReport> centers = new ArrayList<CenterReport>(centersHash.values());
				mav.addObject("centers", centers);

			} else if (eventCenterId > 0 && eventClassroomId > 0) {
				mav.addObject("type", 1);

				EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
						.findById(eventCenterId);
				if (evaluationEventEvaluationCenter != null) {
					mav.addObject("evaluationCenterName",
							evaluationEventEvaluationCenter.getEvaluationCenter().getName()); // 2

					EvaluationEventClassroom evaluationEventClassroom = evaluationEventClassroomService
							.findById(eventClassroomId);
					if (evaluationEventClassroom != null) {
						mav.addObject("classroomName", evaluationEventClassroom.getClassroom().getName()); // 3

						ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService
								.findById(classroomTimeBlockId);
						if (classroomTimeBlock != null) {
							Date startDate = classroomTimeBlock.getStartDate();
							String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
							mav.addObject("strDate", strDate); // 4

							String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";
							mav.addObject("strStartTime", strStartTime); // 5

							Date endDate = classroomTimeBlock.getEndDate();
							String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";
							mav.addObject("strEndTime", strEndTime); // 6

							Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
							Set<StudentEvaluation> studentEvaluations = classroomTimeBlock.getStudentEvaluations();
							if (studentEvaluations != null) {
								List<Object[]> rows = new ArrayList<Object[]>();
								int i = 0;
								for (StudentEvaluation studentEvaluation : studentEvaluations) {
									User student = studentEvaluation.getEvaluationAssignment().getUser();
									Object[] row = new Object[] { i + 1, student.getIdentification(),
											student.getFullNameForListings().toUpperCase(), "" };
									rows.add(row);
									i++;
								}

								Collections.sort(rows, new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String cad1 = ((String) o1[2]).toUpperCase();
										String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
										String cad2 = ((String) o2[2]).toUpperCase();
										String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

										return (cad1Clean).compareTo(cad2Clean);
									}
								});
								mav.addObject("rows", rows); // 7
							}
						}
					}
				}
			} else {

				mav.addObject("type", 2);

				List<EvaluationEventEvaluationCenter> evaluationEventEvaluationCenters = new ArrayList<EvaluationEventEvaluationCenter>();
				if (eventCenterId == 0) {
					evaluationEventEvaluationCenters = evaluationEventEvaluationCenterService.findByEvaluationEvent(id);
				} else {
					EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
							.findById(eventCenterId);
					if (evaluationEventEvaluationCenter != null) {
						evaluationEventEvaluationCenters.add(evaluationEventEvaluationCenter);
					}
				}
				List<CenterReport> centers = new ArrayList<CenterReport>();
				for (EvaluationEventEvaluationCenter eeec : evaluationEventEvaluationCenters) {
					CenterReport center = new CenterReport();
					center.setName(eeec.getEvaluationCenter().getName());

					List<EvaluationEventClassroom> evaluationEventClassrooms = evaluationEventClassroomService
							.findByEventCenter(eeec.getId());
					List<ClassroomReport> classRooms = new ArrayList<ClassroomReport>();
					for (EvaluationEventClassroom eec : evaluationEventClassrooms) {
						if (eeec != null) {
							String classroomName = eec.getClassroom().getName(); // mav.addObject("classroomName",
																					// eec.getClassroom().getName()); //
																					// 3

							ClassroomReport classroom = new ClassroomReport();
							classroom.setName(classroomName);

							Set<ClassroomTimeBlock> classroomTimeBlocksSet = eec.getClassroomTimeBlocks();
							List<ClassroomTimeBlock> classroomsTimeBlocks = new ArrayList<ClassroomTimeBlock>();
							classroomsTimeBlocks.addAll(classroomTimeBlocksSet);
							List<TimeBlockReport> timeBlocks = new ArrayList<TimeBlockReport>();
							for (ClassroomTimeBlock classroomTimeBlock : classroomsTimeBlocks) {
								if (classroomTimeBlock != null) {
									TimeBlockReport timeBlock = new TimeBlockReport();

									Date startDate = classroomTimeBlock.getStartDate();
									String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
									mav.addObject("strDate", strDate); // 4

									timeBlock.setDate(strDate);

									String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";
									mav.addObject("strStartTime", strStartTime); // 5

									timeBlock.setInitDate(strStartTime);

									Date endDate = classroomTimeBlock.getEndDate();
									String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";
									mav.addObject("strEndTime", strEndTime); // 6

									timeBlock.setEndDate(strEndTime);

									Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
									Set<StudentEvaluation> studentEvaluations = classroomTimeBlock
											.getStudentEvaluations();
									if (studentEvaluations != null) {
										List<Object[]> rows = new ArrayList<Object[]>();
										int i = 0;
										for (StudentEvaluation studentEvaluation : studentEvaluations) {
											User student = studentEvaluation.getEvaluationAssignment().getUser();
											Object[] row = new Object[] { i + 1, student.getIdentification(),
													student.getFullNameForListings().toUpperCase(), "" };
											rows.add(row);
											i++;
										}

										Collections.sort(rows, new Comparator<Object[]>() {
											public int compare(Object[] o1, Object[] o2) {
												String cad1 = ((String) o1[2]).toUpperCase();
												String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
												String cad2 = ((String) o2[2]).toUpperCase();
												String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

												return (cad1Clean).compareTo(cad2Clean);
											}
										});

										timeBlock.setUsers(rows);
										// mav.addObject("rows", rows); // 7
									}

									timeBlocks.add(timeBlock);
								}
							}

							classroom.setTimeblocks(timeBlocks);
							classRooms.add(classroom);
						}
					}
					center.setClassrooms(classRooms);
					centers.add(center);
				}

				mav.addObject("centers", centers);
			}
		}

		List<CenterReport> centers = (List<CenterReport>) ((Map<String, Object>) mav).get("centers");

		boolean hasRows = false;

		for (CenterReport center : centers) {
			String evaluationCenterName = center.getName();
			List<ClassroomReport> classrooms = center.getClassrooms();

			for (ClassroomReport classroom : classrooms) {
				String classroomName = classroom.getName();
				List<TimeBlockReport> timeblocks = classroom.getTimeblocks();

				for (TimeBlockReport timeblock : timeblocks) {
					String strDate = timeblock.getDate();
					String strStartTime = timeblock.getInitDate();
					String strEndTime = timeblock.getEndDate();
					List<Object[]> rows = timeblock.getUsers();
				}
			}
		}

		return mav;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/listado-estudiantes-extendido-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}.pdf", method = RequestMethod.GET)
	public ModelAndView studentListingsPdfExtended(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId) throws Exception {
		ModelAndView mav = new ModelAndView("studentListingsPdfView");
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		mav = processListing(mav, 1, evaluationEvent, eventCenterId, eventClassroomId, classroomTimeBlockId, null);

		return mav;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/listado-passwords-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}", method = RequestMethod.GET)
	public void studentListingsPasswords(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId, HttpServletResponse response)
			throws Exception {

		ModelAndView mav = new ModelAndView();
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		mav = processListing(mav, 2, evaluationEvent, eventCenterId, eventClassroomId, classroomTimeBlockId, null);
		List<StudentListElementReport> elements = (List<StudentListElementReport>) mav.getModel().get("elements");

		String csvFileName = "listado-passwords-" + id + "-" + eventCenterId + "-" + eventClassroomId + "-"
				+ classroomTimeBlockId + " .csv";

		response.setContentType("text/csv");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);
		response.setHeader("Content-Type", "text/xml; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		ICsvBeanWriter csvWriter = null;
		try {
			csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

			String[] header = { "Nº", "IDENTIFICACION", "APELLIDOS Y NOMBRE", "PASSWORD" };

			String[] mapping = { "cont", "identification", "fullName", "password" };

			if (id != null && id > 0) {
				csvWriter.writeComment("EVENTO: " + evaluationEvent.getName());
			}
			if (eventCenterId != null && eventCenterId > 0) {
				EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
						.findById(eventCenterId);
				EvaluationCenter evaluationCenter = evaluationEventEvaluationCenter.getEvaluationCenter();
				csvWriter.writeComment("CENTRO EVALUACION: " + evaluationCenter.getName());
			}
			if (eventClassroomId != null && eventClassroomId > 0) {
				EvaluationEventClassroom evaluationEventClassroom = evaluationEventClassroomService
						.findById(eventClassroomId);
				csvWriter.writeComment("AULA: " + evaluationEventClassroom.getClassroom().getName());
			}
			if (classroomTimeBlockId != null && classroomTimeBlockId > 0) {
				ClassroomTimeBlock crtbParameter = classroomTimeBlockService.findById(classroomTimeBlockId);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String initDate = sdf.format(crtbParameter.getStartDate());
				String endDate = sdf.format(crtbParameter.getEndDate());
				csvWriter.writeComment("HORARIO: " + initDate + " - " + endDate);
			}

			csvWriter.writeHeader(header);

			if (elements != null && elements.size() > 0) {
				for (StudentListElementReport element : elements) {
					List<Object[]> users = element.getUsers();
					for (int i = 0; i < users.size(); i++) {
						Object[] user = users.get(i);
						StudentReport sr = new StudentReport();
						sr.setCont(i + 1);
						sr.setIdentification((String) user[1]);
						sr.setFullName((String) user[2]);
						sr.setPassword((String) user[3]);
						csvWriter.write(sr, mapping);
					}
				}
			}

		} catch (Exception e) {
			log.error("Al generar CSV");
			e.printStackTrace();
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (IOException e) {
					log.error("Al cerrar CSV");
					e.printStackTrace();
				}

			}
		}
	}

	private ModelAndView processListing(ModelAndView mav, int type, EvaluationEvent evaluationEvent, Long eventCenterId,
			Long eventClassroomId, Long classroomTimeBlockId, String identification) {
		if (evaluationEvent != null) {
			String evaluationEventName = evaluationEvent.getName();
			mav.addObject("evaluationEventName", evaluationEventName); // 1

			List<StudentListElementReport> studentsElement = new ArrayList<StudentListElementReport>();

			if (classroomTimeBlockId > 0 && eventCenterId == 0 && eventClassroomId == 0) { // por jornada
				mav.addObject("type", 2);

				ClassroomTimeBlock crtbParameter = classroomTimeBlockService.findById(classroomTimeBlockId);
				List<ClassroomTimeBlock> classroomTimeBlocks = classroomTimeBlockService
						.findByDates(crtbParameter.getStartDate(), crtbParameter.getEndDate(), evaluationEvent.getId());

				for (ClassroomTimeBlock classroomTimeBlock : classroomTimeBlocks) {
					String centerName = classroomTimeBlock.getEvaluationCenter().getName();
					String classroomName = classroomTimeBlock.getEvaluationEventClassroom().getClassroom().getName();
					List<TimeBlockReport> timeBlocks = new ArrayList<TimeBlockReport>();

					Date startDate = classroomTimeBlock.getStartDate();
					String strDate = startDate != null ? DATE_SDF.format(startDate) : "";

					String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";

					Date endDate = classroomTimeBlock.getEndDate();
					String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";

					Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
					Set<StudentEvaluation> studentEvaluations = classroomTimeBlock.getStudentEvaluations();
					if (studentEvaluations != null) {
						List<Object[]> rows = new ArrayList<Object[]>();
						List<StudentExtendedDocReport> extReports = new ArrayList<StudentExtendedDocReport>();
						int i = 0;
						for (StudentEvaluation studentEvaluation : studentEvaluations) {
							StudentExtendedDocReport extReport = new StudentExtendedDocReport();
							User student = studentEvaluation.getEvaluationAssignment().getUser();
							String passwd = studentEvaluation.getEvaluationAssignment().getExternalPassword();

							String careers = "";
							SortedSet<String> careersSet = new TreeSet<String>();
							for (MatterTestStudent mts : studentEvaluation.getMatterTests()) {
								StudentExtendedMatterDocReport extMatterReport = new StudentExtendedMatterDocReport();
								extMatterReport.setMatterName(mts.getEvaluationAssignmentMatter()
										.getEvaluationEventMatter().getMatter().getName());
								extMatterReport.setMatterAcademicPeriod(mts.getEvaluationAssignmentMatter()
										.getEvaluationEventMatter().getAcademicPeriod().getCode());
								extMatterReport.setMatterMode(mts.getEvaluationAssignmentMatter()
										.getEvaluationEventMatter().getMode().getCode());
								String career = mts.getEvaluationAssignmentMatter().getCareer();
								extMatterReport.setCareer(career == null ? "" : career);
								extReport.getExtendedMatters().add(extMatterReport);
								if (career != null && career.length() > 0) {
									careersSet.add(career);
								}
							}
							for (String career : careersSet) {
								careers += ", " + career;
							}
							if (careers != null && careers.length() > 0) {
								careers = careers.substring(2);
							}

							Object[] row = null;
							if (type == 1) {
								row = new Object[] { i + 1, student.getIdentification(),
										student.getFullNameForListings().toUpperCase(), careers };
							} else {
								row = new Object[] { i + 1, student.getIdentification(),
										student.getFullNameForListings().toUpperCase(), passwd };
							}
							rows.add(row);
							i++;

							extReport.setCareers(careers);
							extReport.setEvaluationEventName(evaluationEventName);
							extReport.setFullName(student.getFullNameForListings());
							extReport.setIdentification(student.getIdentification());
							extReports.add(extReport);

						}

						Collections.sort(rows, new Comparator<Object[]>() {
							public int compare(Object[] o1, Object[] o2) {
								String cad1 = ((String) o1[2]).toUpperCase();
								String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
								String cad2 = ((String) o2[2]).toUpperCase();
								String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

								return (cad1Clean).compareTo(cad2Clean);
							}
						});

						Collections.sort(extReports, new Comparator<StudentExtendedDocReport>() {
							public int compare(StudentExtendedDocReport o1, StudentExtendedDocReport o2) {
								String cad1 = (o1.getFullName()).toUpperCase();
								String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
								String cad2 = (o2.getFullName()).toUpperCase();
								String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

								return (cad1Clean).compareTo(cad2Clean);
							}
						});

						if (rows != null && rows.size() > 0) {
							StudentListElementReport sler = new StudentListElementReport();

							String cityName = null;
							Set<Address> addresses = classroomTimeBlock.getEvaluationCenter().getAddresses();
							if (addresses != null && addresses.size() > 0) {
								for (Address address : addresses) {
									if (address.getCity() != null && !address.getCity().equals("")) {
										cityName = address.getCity();
										sler.setCityName(cityName);
										break;
									}
								}
							}
							sler.setCenterName(centerName);
							sler.setClassRoomName(classroomName);
							sler.setDateStr(strDate);
							sler.setDate(startDate);
							sler.setInitDateStr(strStartTime);
							sler.setEndDateStr(strEndTime);
							sler.setUsers(rows);
							sler.setExtendedUsersInfo(extReports);

							studentsElement.add(sler);
						}
					}

				}

				mav.addObject("elements", studentsElement);

			} else if (eventCenterId > 0 && eventClassroomId > 0) { // centro, aula y horario
				mav.addObject("type", 2);

				EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
						.findById(eventCenterId);
				if (evaluationEventEvaluationCenter != null) {

					EvaluationEventClassroom evaluationEventClassroom = evaluationEventClassroomService
							.findById(eventClassroomId);
					if (evaluationEventClassroom != null) {

						ClassroomTimeBlock classroomTimeBlock = classroomTimeBlockService
								.findById(classroomTimeBlockId);
						if (classroomTimeBlock != null) {
							Date startDate = classroomTimeBlock.getStartDate();
							String strDate = startDate != null ? DATE_SDF.format(startDate) : "";

							String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";

							Date endDate = classroomTimeBlock.getEndDate();
							String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";

							Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
							Set<StudentEvaluation> studentEvaluations = classroomTimeBlock.getStudentEvaluations();
							List<StudentEvaluation> sevs = new ArrayList<StudentEvaluation>(studentEvaluations);
							if (studentEvaluations != null) {
								List<Object[]> rows = new ArrayList<Object[]>();
								List<StudentExtendedDocReport> extReports = new ArrayList<StudentExtendedDocReport>();
								int i = 0;
								for (StudentEvaluation studentEvaluation : studentEvaluations) {
									StudentExtendedDocReport extReport = new StudentExtendedDocReport();
									User student = studentEvaluation.getEvaluationAssignment().getUser();
									String passwd = studentEvaluation.getEvaluationAssignment().getExternalPassword();

									String careers = "";
									SortedSet<String> careersSet = new TreeSet<String>();
									for (MatterTestStudent mts : studentEvaluation.getMatterTests()) {
										StudentExtendedMatterDocReport extMatterReport = new StudentExtendedMatterDocReport();
										extMatterReport.setMatterName(mts.getEvaluationAssignmentMatter()
												.getEvaluationEventMatter().getMatter().getName());
										extMatterReport.setMatterAcademicPeriod(mts.getEvaluationAssignmentMatter()
												.getEvaluationEventMatter().getAcademicPeriod().getName());
										extMatterReport.setMatterMode(mts.getEvaluationAssignmentMatter()
												.getEvaluationEventMatter().getMode().getCode());
										extMatterReport.setTest(mts.getEvaluationEventMatterTest().getTest().getName());

										String career = mts.getEvaluationAssignmentMatter().getCareer();
										extMatterReport.setCareer(career == null ? "" : career);
										extReport.getExtendedMatters().add(extMatterReport);
										if (career != null && career.length() > 0) {
											careersSet.add(career);
										}
									}
									for (String career : careersSet) {
										careers += ", " + career;
									}
									if (careers != null && careers.length() > 0) {
										careers = careers.substring(2);
									}

									Object[] row = null;
									if (type == 1) {
										row = new Object[] { i + 1, student.getIdentification(),
												student.getFullNameForListings().toUpperCase(), careers };
									} else {
										row = new Object[] { i + 1, student.getIdentification(),
												student.getFullNameForListings().toUpperCase(), passwd };
									}
									rows.add(row);
									i++;

									extReport.setCareers(careers);
									extReport.setEvaluationEventName(evaluationEventName);
									extReport.setCenterName(
											evaluationEventEvaluationCenter.getEvaluationCenter().getName());
									extReport.setFullName(student.getFullNameForListings());
									extReport.setIdentification(student.getIdentification());
									extReport.setStrDate(strDate);
									extReport.setTime(strStartTime + " - " + strEndTime);
									extReport.setUserName(student.getUsername());
									extReport.setPassword(passwd);

									String netName = "";
									String netPassword = "";
									if (studentEvaluation.getNet() == null) {
										if (evaluationEventClassroom.getCap() != null) {
											netName = evaluationEventClassroom.getCap().getSsid();
											netPassword = evaluationEventClassroom.getCap().getKey();
										}
									} else {
										netName = studentEvaluation.getNet().getName();
										netPassword = studentEvaluation.getNet().getPassword();
									}

									extReport.setNetName(netName);
									extReport.setNetPassword(netPassword);

									extReports.add(extReport);
								}

								Collections.sort(rows, new Comparator<Object[]>() {
									public int compare(Object[] o1, Object[] o2) {
										String cad1 = ((String) o1[2]).toUpperCase();
										String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
										String cad2 = ((String) o2[2]).toUpperCase();
										String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

										return (cad1Clean).compareTo(cad2Clean);
									}
								});

								Collections.sort(extReports, new Comparator<StudentExtendedDocReport>() {
									public int compare(StudentExtendedDocReport o1, StudentExtendedDocReport o2) {
										String cad1 = (o1.getFullName()).toUpperCase();
										String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
										String cad2 = (o2.getFullName()).toUpperCase();
										String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

										return (cad1Clean).compareTo(cad2Clean);
									}
								});

								StudentListElementReport sler = new StudentListElementReport();

								String cityName = null;
								Set<Address> addresses = evaluationEventEvaluationCenter.getEvaluationCenter()
										.getAddresses();
								if (addresses != null && addresses.size() > 0) {
									for (Address address : addresses) {
										if (address.getCity() != null && !address.getCity().equals("")) {
											cityName = address.getCity();
											sler.setCityName(cityName);
											break;
										}
									}
								}

								sler.setCenterName(evaluationEventEvaluationCenter.getEvaluationCenter().getName());
								sler.setClassRoomName(evaluationEventClassroom.getClassroom().getName());
								sler.setDateStr(strDate);
								sler.setDate(startDate);
								sler.setInitDateStr(strStartTime);
								sler.setEndDateStr(strEndTime);
								sler.setUsers(rows);
								sler.setExtendedUsersInfo(extReports);

								studentsElement.add(sler);
							}
						}
					}
				}
				mav.addObject("elements", studentsElement);
			} else if (identification != null && !identification.equals("") && !identification.equals("0")) { // por
																												// cedula
				mav.addObject("type", 3);
				List<StudentEvaluation> studentEvaluations = studentEvaluationService
						.findByEvaluationEventAndUser(evaluationEvent.getId(), identification);
				if (studentEvaluations != null) {
					int i = 0;
					for (StudentEvaluation studentEvaluation : studentEvaluations) {
						StudentListElementReport sler = new StudentListElementReport();
						List<Object[]> rows = new ArrayList<Object[]>(); // en este caso si esta fuera del bucle puede
																			// contarse varias veces si hay varias
																			// evaluaciones para ese alumno
						List<StudentExtendedDocReport> extReports = new ArrayList<StudentExtendedDocReport>();

						sler.setCenterName(studentEvaluation.getEvaluationCenter().getName());
						sler.setClassRoomName(studentEvaluation.getClassroom().getName());
						String cityName = null;
						Set<Address> addresses = studentEvaluation.getEvaluationCenter().getAddresses();
						if (addresses != null && addresses.size() > 0) {
							for (Address address : addresses) {
								if (address.getCity() != null && !address.getCity().equals("")) {
									cityName = address.getCity();
									sler.setCityName(cityName);
									break;
								}
							}
						}

						Date startDate = studentEvaluation.getClassroomTimeBlock().getStartDate();
						String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
						sler.setDateStr(strDate);
						sler.setDate(startDate);

						String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";
						sler.setInitDateStr(strStartTime);

						Date endDate = studentEvaluation.getClassroomTimeBlock().getEndDate();
						String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";

						sler.setEndDateStr(strEndTime);

						StudentExtendedDocReport extReport = new StudentExtendedDocReport();
						User student = studentEvaluation.getEvaluationAssignment().getUser();
						String passwd = studentEvaluation.getEvaluationAssignment().getExternalPassword();

						String careers = "";
						SortedSet<String> careersSet = new TreeSet<String>();
						for (MatterTestStudent mts : studentEvaluation.getMatterTests()) {
							StudentExtendedMatterDocReport extMatterReport = new StudentExtendedMatterDocReport();
							extMatterReport.setMatterName(mts.getEvaluationAssignmentMatter().getEvaluationEventMatter()
									.getMatter().getName());
							extMatterReport.setMatterAcademicPeriod(mts.getEvaluationAssignmentMatter()
									.getEvaluationEventMatter().getAcademicPeriod().getName());
							extMatterReport.setMatterMode(
									mts.getEvaluationAssignmentMatter().getEvaluationEventMatter().getMode().getCode());
							extMatterReport.setTest(mts.getEvaluationEventMatterTest().getTest().getName());

							String career = mts.getEvaluationAssignmentMatter().getCareer();
							extMatterReport.setCareer(career == null ? "" : career);
							extReport.getExtendedMatters().add(extMatterReport);
							if (career != null && career.length() > 0) {
								careersSet.add(career);
							}
						}
						for (String career : careersSet) {
							careers += ", " + career;
						}
						if (careers != null && careers.length() > 0) {
							careers = careers.substring(2);
						}

						Object[] row = null;
						if (type == 1) {
							row = new Object[] { i + 1, student.getIdentification(),
									student.getFullNameForListings().toUpperCase(), careers };
						} else {
							row = new Object[] { i + 1, student.getIdentification(),
									student.getFullNameForListings().toUpperCase(), passwd };
						}
						rows.add(row);
						i++;

						extReport.setCareers(careers);
						extReport.setEvaluationEventName(evaluationEventName);
						extReport.setCenterName(studentEvaluation.getEvaluationCenter().getName());
						extReport.setFullName(student.getFullNameForListings());
						extReport.setIdentification(student.getIdentification());
						extReport.setStrDate(strDate);
						extReport.setTime(strStartTime + " - " + strEndTime);
						extReport.setUserName(student.getUsername());
						extReport.setPassword(passwd);

						String netName = "";
						String netPassword = "";
						if (studentEvaluation.getNet() == null) {
							Cap cap = studentEvaluation.getClassroomTimeBlock().getEvaluationEventClassroom().getCap();
							if (cap != null) {
								netName = cap.getSsid();
								netPassword = cap.getKey();
							}
						} else {
							netName = studentEvaluation.getNet().getName();
							netPassword = studentEvaluation.getNet().getPassword();
						}

						extReport.setNetName(netName);
						extReport.setNetPassword(netPassword);

						extReports.add(extReport);

						sler.setUsers(rows);
						sler.setExtendedUsersInfo(extReports);

						studentsElement.add(sler);
					}

				}
				mav.addObject("elements", studentsElement);

			} else { // ningún filtro o centro

				mav.addObject("type", 2);

				List<EvaluationEventEvaluationCenter> evaluationEventEvaluationCenters = new ArrayList<EvaluationEventEvaluationCenter>();
				if (eventCenterId == 0) {
					evaluationEventEvaluationCenters = evaluationEventEvaluationCenterService
							.findByEvaluationEvent(evaluationEvent.getId());
				} else {
					EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService
							.findById(eventCenterId);
					if (evaluationEventEvaluationCenter != null) {
						evaluationEventEvaluationCenters.add(evaluationEventEvaluationCenter);
					}
				}

				for (EvaluationEventEvaluationCenter eeec : evaluationEventEvaluationCenters) {
					List<EvaluationEventClassroom> evaluationEventClassrooms = evaluationEventClassroomService
							.findByEventCenter(eeec.getId());
					for (EvaluationEventClassroom eec : evaluationEventClassrooms) {
						if (eeec != null) {
							String classroomName = eec.getClassroom().getName();

							Set<ClassroomTimeBlock> classroomTimeBlocksSet = eec.getClassroomTimeBlocks();
							List<ClassroomTimeBlock> classroomsTimeBlocks = new ArrayList<ClassroomTimeBlock>();
							classroomsTimeBlocks.addAll(classroomTimeBlocksSet);
							List<TimeBlockReport> timeBlocks = new ArrayList<TimeBlockReport>();
							for (ClassroomTimeBlock classroomTimeBlock : classroomsTimeBlocks) {
								if (classroomTimeBlock != null) {
									StudentListElementReport sler = new StudentListElementReport();

									sler.setCenterName(eeec.getEvaluationCenter().getName());
									sler.setClassRoomName(classroomName);

									String cityName = null;
									Set<Address> addresses = eeec.getEvaluationCenter().getAddresses();
									if (addresses != null && addresses.size() > 0) {
										for (Address address : addresses) {
											if (address.getCity() != null && !address.getCity().equals("")) {
												cityName = address.getCity();
												sler.setCityName(cityName);
												break;
											}
										}
									}

									Date startDate = classroomTimeBlock.getStartDate();
									String strDate = startDate != null ? DATE_SDF.format(startDate) : "";
									sler.setDateStr(strDate);
									sler.setDate(startDate);

									String strStartTime = startDate != null ? TIME_SDF.format(startDate) : "";
									sler.setInitDateStr(strStartTime);

									Date endDate = classroomTimeBlock.getEndDate();
									String strEndTime = startDate != null ? TIME_SDF.format(endDate) : "";

									sler.setEndDateStr(strEndTime);

									Hibernate.initialize(classroomTimeBlock.getStudentEvaluations());
									Set<StudentEvaluation> studentEvaluations = classroomTimeBlock
											.getStudentEvaluations();
									if (studentEvaluations != null) {
										List<Object[]> rows = new ArrayList<Object[]>();
										List<StudentExtendedDocReport> extReports = new ArrayList<StudentExtendedDocReport>();
										int i = 0;
										for (StudentEvaluation studentEvaluation : studentEvaluations) {
											StudentExtendedDocReport extReport = new StudentExtendedDocReport();
											User student = studentEvaluation.getEvaluationAssignment().getUser();
											String passwd = studentEvaluation.getEvaluationAssignment()
													.getExternalPassword();

											String careers = "";
											SortedSet<String> careersSet = new TreeSet<String>();
											for (MatterTestStudent mts : studentEvaluation.getMatterTests()) {
												StudentExtendedMatterDocReport extMatterReport = new StudentExtendedMatterDocReport();
												extMatterReport.setMatterName(mts.getEvaluationAssignmentMatter()
														.getEvaluationEventMatter().getMatter().getName());
												extMatterReport.setMatterAcademicPeriod(
														mts.getEvaluationAssignmentMatter().getEvaluationEventMatter()
																.getAcademicPeriod().getName());
												extMatterReport.setMatterMode(mts.getEvaluationAssignmentMatter()
														.getEvaluationEventMatter().getMode().getCode());
												extMatterReport.setTest(
														mts.getEvaluationEventMatterTest().getTest().getName());
												String career = mts.getEvaluationAssignmentMatter().getCareer();
												extMatterReport.setCareer(career == null ? "" : career);
												extReport.getExtendedMatters().add(extMatterReport);
												if (career != null && career.length() > 0) {
													careersSet.add(career);
												}
											}
											for (String career : careersSet) {
												careers += ", " + career;
											}
											if (careers != null && careers.length() > 0) {
												careers = careers.substring(2);
											}

											Object[] row = null;
											if (type == 1) {
												row = new Object[] { i + 1, student.getIdentification(),
														student.getFullNameForListings().toUpperCase(), careers };
											} else {
												row = new Object[] { i + 1, student.getIdentification(),
														student.getFullNameForListings().toUpperCase(), passwd };
											}
											rows.add(row);
											i++;

											extReport.setCareers(careers);
											extReport.setEvaluationEventName(evaluationEventName);
											extReport.setCenterName(eeec.getEvaluationCenter().getName());
											extReport.setFullName(student.getFullNameForListings());
											extReport.setIdentification(student.getIdentification());
											extReport.setStrDate(strDate);
											extReport.setTime(strStartTime + " - " + strEndTime);
											extReport.setUserName(student.getUsername());
											extReport.setPassword(passwd);

											String netName = "";
											String netPassword = "";
											if (studentEvaluation.getNet() == null) {
												if (eec.getCap() != null) {
													netName = eec.getCap().getSsid();
													netPassword = eec.getCap().getKey();
												}
											} else {
												netName = studentEvaluation.getNet().getName();
												netPassword = studentEvaluation.getNet().getPassword();
											}

											extReport.setNetName(netName);
											extReport.setNetPassword(netPassword);

											extReports.add(extReport);
										}

										Collections.sort(rows, new Comparator<Object[]>() {
											public int compare(Object[] o1, Object[] o2) {
												String cad1 = ((String) o1[2]).toUpperCase();
												String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
												String cad2 = ((String) o2[2]).toUpperCase();
												String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

												return (cad1Clean).compareTo(cad2Clean);
											}
										});

										Collections.sort(extReports, new Comparator<StudentExtendedDocReport>() {
											public int compare(StudentExtendedDocReport o1,
													StudentExtendedDocReport o2) {
												String cad1 = (o1.getFullName()).toUpperCase();
												String cad1Clean = Normalizer.normalize(cad1, Normalizer.Form.NFD);
												String cad2 = (o2.getFullName()).toUpperCase();
												String cad2Clean = Normalizer.normalize(cad2, Normalizer.Form.NFD);

												return (cad1Clean).compareTo(cad2Clean);
											}
										});

										sler.setUsers(rows);
										sler.setExtendedUsersInfo(extReports);

									}

									studentsElement.add(sler);
								}
							}
						}
					}

				}

				mav.addObject("elements", studentsElement);
			}
		}

		List<StudentListElementReport> elements = (List<StudentListElementReport>) mav.getModel().get("elements");
		if (elements != null && elements.size() > 0) {
			Collections.sort(elements, new Comparator<StudentListElementReport>() {
				public int compare(StudentListElementReport o1, StudentListElementReport o2) {

					String centerName1 = Normalizer.normalize(o1.getCenterName().toUpperCase(), Normalizer.Form.NFD);
					String centerName2 = Normalizer.normalize(o2.getCenterName().toUpperCase(), Normalizer.Form.NFD);
					int res = centerName1.compareTo(centerName2);
					// hay que colocar antes
					if (res < 0) {
						return -1;
					} else if (res > 0) { // colocar despues
						return 1;
					} else { // son iguales comprobar el resto de parametros
						Date date1 = o1.getDate();
						Date date2 = o2.getDate();
						int res2 = date1.compareTo(date2);
						if (res2 < 0) {
							return -1;
						} else if (res2 > 0) { // colocar despues
							return 1;
						} else { // son iguales comprobar el resto de parametros
							String jour1 = Normalizer.normalize(o1.getInitDateStr().toUpperCase(), Normalizer.Form.NFD);
							String jour2 = Normalizer.normalize(o2.getEndDateStr().toUpperCase(), Normalizer.Form.NFD);
							int res3 = jour1.compareTo(jour2);
							if (res3 < 0) {
								return -1;
							} else if (res3 > 0) { // colocar despues
								return 1;
							} else { // son iguales comprobar el resto de parametros
								String ClassroomName1 = Normalizer.normalize(o1.getClassRoomName().toUpperCase(),
										Normalizer.Form.NFD);
								String ClassroomName2 = Normalizer.normalize(o2.getClassRoomName().toUpperCase(),
										Normalizer.Form.NFD);
								int res4 = jour1.compareTo(jour2);
								if (res4 < 0) {
									return -1;
								} else if (res4 > 0) { // colocar despues
									return 1;
								} else { // son iguales comprobar el resto de parametros
									return 0;
								}
							}
						}
					}

				}
			});
		}

		return mav;
	}

	/***
	 * Generacion online
	 */
	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/generate-onlinedocs-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}-{identification}", method = RequestMethod.GET)
	public ModelAndView generateDocuments(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId,
			@PathVariable String identification) throws Exception {

		ModelAndView mav = new ModelAndView("generateDocumentsPdfView");
		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		mav = processListing(mav, 1, evaluationEvent, eventCenterId, eventClassroomId, classroomTimeBlockId,
				identification);

		return mav;
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/generate-docs-{eventCenterId}-{eventClassroomId}-{classroomTimeBlockId}-{identification}", method = RequestMethod.GET)
	public @ResponseBody Integer generatePersistDocuments(@PathVariable Long id, @PathVariable Long eventCenterId,
			@PathVariable Long eventClassroomId, @PathVariable Long classroomTimeBlockId,
			@PathVariable String identification) throws Exception {

		String email = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof es.grammata.evaluation.evs.data.model.repository.User) {
			email = ((es.grammata.evaluation.evs.data.model.repository.User) authentication.getPrincipal()).getEmail();
		} else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
			// buscamos por username
			User user = userService.findByUsername(
					((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
			if (user != null) {
				email = user.getEmail();
			}
		}
		Integer res = 1;
		try {
			DOC_PROCESS_ACTIVE = 1;
			DOC_PROCESS_ACTIVE2 = 1;

			EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
			ModelAndView mav = new ModelAndView();
			if (eventCenterId.equals(0L) && (identification == null || identification.equals("0"))) { // si no hay
																										// filtros
																										// generamos un
																										// documento por
																										// centro
				List<EvaluationEventEvaluationCenter> eeecs = evaluationEventEvaluationCenterService
						.findByEvaluationEvent(id);
				for (EvaluationEventEvaluationCenter eeec : eeecs) {
					mav = new ModelAndView();
					this.generateDocument(mav, identification, eventClassroomId, classroomTimeBlockId, evaluationEvent,
							eeec.getId(), email);
				}
			} else {
				this.generateDocument(mav, identification, eventClassroomId, classroomTimeBlockId, evaluationEvent,
						eventCenterId, email);
			}

			if (eventCenterId == null || eventCenterId.equals(0L)) {
				this.saveFullDocument(evaluationEvent.getCode(), email);
			}
			this.notifyDocs(evaluationEvent, email);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			res = 0;
		} finally {
			DOC_PROCESS_ACTIVE = 0;
			DOC_PROCESS_ACTIVE2 = 0;
		}

		return res;
	}

	// guarda el fichero general
	private void saveFullDocument(String evaluationEventCode, String email) throws Exception {
		// eliminar el ya existente
		DocFile eventDocFile = docFileService.findEventFile(evaluationEventCode);
		if (eventDocFile != null) {
			FileUtil.deleteFile(DOC_BASE_PATH + File.separator + eventDocFile.getFullFileName());
			docFileService.delete(eventDocFile.getId());
		}

		List<DocFile> docFiles = docFileService.findByParams(evaluationEventCode, null, null, null, null);
		String rootPath = DOC_PATH + File.separator + evaluationEventCode;
		String fullRootPath = DOC_BASE_PATH + File.separator + rootPath + File.separator;
		File dir = FileUtil.createDir(fullRootPath);
		String fileName = DocFile.generateFileName(evaluationEventCode, null, null, null, null, "zip");
		List<String> fileNames = new ArrayList<String>();
		for (DocFile docFile : docFiles) {
			fileNames.add(docFile.getFileName());
		}
		FileUtil.createZipfile(fullRootPath + fileName, fileNames, fullRootPath);
		DocFile docFile = new DocFile(evaluationEventCode, null, null, null, null, fileName, rootPath + File.separator,
				email);
		docFileService.save(docFile);
	}

	private void notifyDocs(EvaluationEvent evaluationEvent, String email) {
		MailUtil mailUtil = new MailUtil(mailSender);
		String subject = "UTPL - Notificaci\u00f3n de documentaci\u00f3n generada para evento '"
				+ evaluationEvent.getName() + "'";
		String text = "La documentaci\u00f3n requerida para el evento de evaluaci\u00f3n '" + evaluationEvent.getName()
				+ "' ha sido generada y est\u00E1 disponible en la zona de descargas.\n\n";
		String from = EMAIL_FROM;
		mailUtil.sendSimpleMail(evaluationEvent, email, subject, text, from);
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/{id}/files/{fileId}/download-doc", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getFile(@PathVariable Long id, @PathVariable Long fileId,
			HttpServletResponse response) {

		InputStream fin = null;
		DocFile docFile = docFileService.findById(fileId);
		try {
			File file = new File(DOC_BASE_PATH + File.separator + docFile.getFilePath() + docFile.getFileName());
			HttpHeaders respHeaders = new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/pdf");
			respHeaders.setContentType(mediaType);
			respHeaders.setContentLength(file.length());
			respHeaders.setContentDispositionFormData("attachment", file.getName());
			InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
			return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);

		} catch (IOException ex) {
			log.error("Error cargando fichero: " + docFile.getFileName(), ex);
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/checkdocprocessactive", method = RequestMethod.GET)
	public @ResponseBody int checkDocProcessActiveForView() {
		return DOC_PROCESS_ACTIVE;
	}

	///

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/checkdocprocessactive2", method = RequestMethod.GET)
	public @ResponseBody int checkDocProcessActiveForView2() {
		return DOC_PROCESS_ACTIVE2;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/evaluationevent/forcedocprocessinactive", method = RequestMethod.GET)
	public void forceDocProcessInactiveForView() {
		DOC_PROCESS_ACTIVE = 0;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/evaluationevent/forcedocprocessinactive2", method = RequestMethod.GET)
	public void forceDocProcessInactiveForView2() {
		DOC_PROCESS_ACTIVE2 = 0;
	}

	private synchronized void generateDocument(ModelAndView mav, String identification, Long eventClassroomId,
			Long classroomTimeBlockId, EvaluationEvent evaluationEvent, Long eventCenterId, String email)
			throws Exception {

		String rootPath = DOC_PATH + File.separator + evaluationEvent.getCode();
		File dir = FileUtil.createDir(DOC_BASE_PATH + File.separator + rootPath);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

		String fileName = "error.pdf";

		String ecCode = "0";
		if (eventCenterId > 0) {
			EvaluationCenter evaluationCenter = evaluationEventEvaluationCenterService
					.findEvaluationCenter(eventCenterId);
			ecCode = evaluationCenter.getCode();
		}
		String crCode = null;
		if (eventClassroomId > 0) {
			Classroom classroom = evaluationEventClassroomService.loadClassroom(eventClassroomId);
			crCode = classroom.getName();
		}
		String time = null;
		String startTime = null;
		String endTime = null;
		Date startDate = null;
		Date endDate = null;
		if (classroomTimeBlockId > 0) {
			ClassroomTimeBlock crtb = classroomTimeBlockService.findById(classroomTimeBlockId);
			startDate = crtb.getStartDate();
			endDate = crtb.getEndDate();
			startTime = sdf.format(crtb.getStartDate());
			endTime = sdf.format(crtb.getEndDate());
			time = startTime + "-" + endTime;
		}

		fileName = DocFile.generateFileName(evaluationEvent.getCode(), ecCode, crCode, startTime, endTime, "pdf");

		File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

		mav = processListing(mav, 1, evaluationEvent, eventCenterId, eventClassroomId, classroomTimeBlockId,
				identification);

		GenerateDocumentsPdf generateDocumentsPdf = new GenerateDocumentsPdf();
		generateDocumentsPdf.buildPdfDocument(mav.getModel(), context, serverFile);

		DocFile docFile = new DocFile(evaluationEvent.getCode(), ecCode, crCode, startDate, endDate, fileName,
				rootPath + File.separator, email);
		// hay que buscar por campos ya que se añade el timestamp al final para no
		// solapar ficheros
		List<DocFile> files = docFileService.findByParams(evaluationEvent.getCode(), ecCode, crCode, startDate,
				endDate);
		for (DocFile file : files) {
			FileUtil.deleteFile(DOC_BASE_PATH + File.separator + rootPath + File.separator + file.getFileName());
			docFileService.delete(file.getId());
		}
		docFileService.save(docFile);

	}

	private List<EvaluationType> loadTypes() {
		List<EvaluationType> evaluationTypes = this.evaluationTypeService.findAll();
		this.evaluationTypeCache = new HashMap<String, EvaluationType>();
		for (EvaluationType evaluationType : evaluationTypes) {
			this.evaluationTypeCache.put(evaluationType.getIdAsString(), evaluationType);
		}
		return evaluationTypes;
	}

	private List<EvaluationEventType> loadEvaluationEventTypes() {
		List<EvaluationEventType> evaluationEventTypes = this.evaluationEventTypeService.findAllAtive();
		this.evaluationEventTypeCache = new HashMap<String, EvaluationEventType>();
		for (EvaluationEventType evaluationEventType : evaluationEventTypes) {
			this.evaluationEventTypeCache.put(evaluationEventType.getIdAsString(), evaluationEventType);
		}
		return evaluationEventTypes;
	}

	@InitBinder
	protected void initTypesBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "evaluationTypes", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof EvaluationType) {
					return element;
				}
				if (element instanceof String) {
					EvaluationType type = EvaluationEventController.this.evaluationTypeCache.get(element);
					return type;
				}
				return null;
			}
		});
	}
}
