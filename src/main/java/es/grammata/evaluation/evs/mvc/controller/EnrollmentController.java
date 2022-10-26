package es.grammata.evaluation.evs.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegreeSubject;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeSubjectService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.data.services.repository.WeekDayService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.DegreeF;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentFachada;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.Message;

@Controller
public class EnrollmentController extends BaseController {

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private DegreeService degreeService;
	@Autowired
	private EvaluationEventDegreeService eedegreeService;

	@Autowired
	private EvaluationEventService evaluationEventService;

	@Autowired
	private WeekDayService weekDayService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EvaluationCenterService evaluationCenterService;

	@Autowired
	private EvaluationAssignmentService evaluationAssignmentService;

	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	@Autowired
	private EvaluationEventDegreeSubjectService evaluationEventDegreeSubjectService;

	@Autowired
	private EvaluationEventMatterService evaluationEventMatterService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;

	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;
	
	@Autowired
	private EvaluationEventEvaluationCenterService evaluationEventEvaluationCenterService;
	
	@Autowired
	private EvaluationEventEvaluationCenterCenterService evaluationEventEvaluationCenterCenterService;

	private List<AcademicLevel> academicLevels;

	private List<WeekDay> daysAllowed;

	private Map<String, WeekDay> weekDaysCache;

	private Map<String, Degree> degreeCache;

	@Autowired
	ServletContext context;

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value = "/enrollment/new", method = { RequestMethod.POST, RequestMethod.GET })
	public String createDegree(HttpServletRequest request, Map<String, Object> model, @Valid Enrollment enrollment,
			BindingResult bindingResult, RedirectAttributes redirectAttrs) {

		if (request.getMethod().equals("POST")) {

			if (!bindingResult.hasErrors()) {
				enrollment.setCreated(new Date());
				enrollment.setModified(new Date());
				enrollment.setActive(true);
				User u = this.userLogin();
				enrollment.setCreated_By(u.getId().intValue());
				enrollment.setModified_By(u.getId().intValue());
				// degreeService.save(enrollment);
				enrollmentService.save(enrollment);
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
				return "redirect:/enrollment/edit/" + enrollment.getId();
			}
		} else {
			enrollment = new Enrollment();
		}

		model.put("enrollment", enrollment);
		model.put("headText", "Nueva inscripcion");
		addAuxToModel(model);

		return "evaluation_event_academic/enrollment-form";
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/enrollments", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Enrollment> loadEnrollments(@PathVariable Long id) {
		List<Enrollment> enrollments = enrollmentService.findByEvaluationEvent(id);

		return enrollments;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollments", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EnrollmentInfo> listEnrollments(@PathVariable Long id,
			@RequestParam Map<String, String> allRequestParams) {

		List<EnrollmentInfo> enrollments = enrollmentService.findInfoByEvaluationEventOrderByUser(id);
		return enrollments;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/enrollments2", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Degree> loadEnrollments2(@PathVariable Long id) {
		// List<Degree> enrollments = enrollmentService.findByEvaluationEvent(id);
		List<Degree> ds = null;
		List<EvaluationEventDegree> eed = eedegreeService.findByEvaluationEvent(id);

		for (int i = 0; i < eed.size(); i++) {
			ds.add(eed.get(i).getDegree());
		}
		return ds;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment", method = RequestMethod.GET)
	public String showList1(HttpServletRequest request, Map<String, Object> model, @PathVariable Long id) {
		model.put("headText", "Inscripcion");
		model.put("edit", true);

		EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
		model.put("evaluationEvent", evaluationEvent);
		List<Enrollment> enrollments = enrollmentService.findByEvaluationEvent(id);

		model.put("enrollments", enrollments);
		// enrollmentsF= enrollments;
		return "evaluation_event/enrollment-form";
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
					WeekDay weekDay = EnrollmentController.this.weekDaysCache.get(element);
					return weekDay;
				}
				return null;
			}
		});
	}

	private void addAuxToModel(Map<String, Object> model) {
		model.put("academicLevels", academicLevels);
		// model.put("academicPeriods", academicPeriods);
		// model.put("modes", modes);
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

	public User userLogin() {
		User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return loggedUser;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/new", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String createEnrollmentF(@PathVariable Long id, HttpServletRequest request, Map<String, Object> model,
			@Valid Enrollment enrollment, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
		if (request.getMethod().equals("POST")) {
			Degree d = new Degree();
			d.setId((long) 2);
			enrollment.setDegree(d);
			EvaluationEvent evnt = new EvaluationEvent();
			evnt.setId(id);
			User u = this.userLogin();
			User userinscrpition = enrollment.getUser();
			userinscrpition.setUsername(userinscrpition.getIdentification());
			userinscrpition.setEnabled(1);
			userService.save(userinscrpition);

			enrollment.setCreated_By(u.getId().intValue());
			enrollment.setModified_By(u.getId().intValue());

			enrollment.setCreated(new Date());
			enrollment.setModified(new Date());
			enrollment.setActive(true);
			enrollment.setEvaluationEvent(evnt);
			enrollment.setPriority(1);
			enrollment.setGrade(0.5);

			enrollmentService.saveT(enrollment);
			Enrollment enrollmentAux = new Enrollment();
			enrollmentAux.setCreated_By(u.getId().intValue());
			enrollmentAux.setModified_By(u.getId().intValue());

			enrollmentAux.setCreated(new Date());
			// enrollmentAux.setDegrees(enrollment.getDegrees());
			enrollmentAux.setModified(new Date());
			enrollmentAux.setActive(true);
			enrollmentAux.setEvaluationEvent(evnt);
			enrollmentAux.setPriority(1);
			enrollmentAux.setGrade(0.5);

			if (!bindingResult.hasErrors()) {
				redirectAttrs.addFlashAttribute("successMessage", "Creado correctamente");
			}
			enrollmentService.save(enrollment);
		} else {
			enrollment = new Enrollment();
		}
		model.put("degree", loadDegrees());
		model.put("enrollment", enrollment);
		model.put("headText", "Nueva inscripcion");
		addAuxToModel(model);

		return "evaluation_event/enrollment-form";
	}

	private List<Degree> loadDegrees() {
		List<Degree> degrees = this.degreeService.findAll();
		this.degreeCache = new HashMap<String, Degree>();
		for (Degree degree : degrees) {
			this.degreeCache.put(degree.getIdAsString(), degree);
		}
		return degrees;
	}

	@InitBinder
	protected void initDegreesBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "degrees", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof Degree) {
					return element;
				}
				if (element instanceof String) {
					Degree degree = EnrollmentController.this.degreeCache.get(element);
					return degree;
				}

				return null;
			}
		});
	}

	@Transactional
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/add", method = RequestMethod.POST)
	public @ResponseBody Message createEnrollmentADD(@PathVariable Long id, @RequestBody EnrollmentFachada enrollment,
			HttpServletRequest request, Model model) {
		Message responseMessage = new Message();

		try {
			if (enrollment.hasValidData()) {
				User userExist = this.userService.findByIdentification(enrollment.getIdentification());
				EvaluationEvent evaluationEvent = this.evaluationEventService.findById(id);
				EvaluationCenter evaluationCenter = evaluationCenterService.findById(enrollment.getEvaluationCenterId());
				EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = evaluationEventEvaluationCenterService.findByEvaluationEventAndEvaluationCenter(evaluationEvent.getId(), evaluationCenter.getId());
				List<Center> centers = evaluationEventEvaluationCenterCenterService.findCentersByEvaluationEventEvaluationCenterId(evaluationEventEvaluationCenter.getId());
				
				if (centers.size() > 0) {
					// Comprobamos si el usuario ya esta inscrito en alguna titulación
					Enrollment existingEnrollment = null;
					if (userExist != null) {
						for (DegreeF degree : enrollment.getDegrees()) {
							Enrollment enroll = enrollmentService.findbyEvaluationEventAndUserAndDegree(id, userExist.getId(), (long)degree.getDegree().getId());
							if (enroll != null) {
								existingEnrollment = enroll;
								break;
							}
						}
					}
					
					if (existingEnrollment == null) {
						Enrollment enrollmen = new Enrollment();
						if (userExist == null) {
							userExist = new User();
							userExist.setFirstName(enrollment.getFirstName());
							userExist.setLastName(enrollment.getLastName());
							userExist.setIdentification(enrollment.getIdentification());
							userExist.setUsername(enrollment.getIdentification());
							userExist.setEmail(enrollment.getEmail());
							userExist.setEnabled(1);
	
							this.userService.save(userExist);
						}
	
						User u1 = this.userService.findByIdentification(userExist.getIdentification());
	
						int idD = enrollment.getDegrees().get(0).getDegree().getId();
						Integer idDe = idD;
						Long idDegree = Long.valueOf(idDe.longValue());
						List<Enrollment> userEnrollments = enrollmentService.findbyEvaluationEventAndUser(id, u1.getId());
	
						// Comprobamos si el usuario está inscrito en dos titulaciones
						if (userEnrollments.size() < 2) {
							if (userEnrollments.size() == 1 && userEnrollments.get(0).getDegree().getId() != idDegree) {
								Degree d = new Degree();
								int ide = enrollment.getDegrees().get(0).getDegree().getId();
								Integer idee = ide;
								Long l = Long.valueOf(idee.longValue());
		
								User userLogin = this.userLogin();
								d = this.degreeService.findById(l);
								enrollmen.setCreated(new Date());
		
								enrollmen.setCreated_By(userLogin.getId().intValue());
								enrollmen.setModified_By(userLogin.getId().intValue());
								enrollmen.setModified(new Date());
								enrollmen.setUser(u1);
								enrollmen.setDegree(d);
								enrollmen.setEvaluationCenter(evaluationCenter);
								enrollmen.setActive(true);
								enrollmen.setPriority(2);
								// enrollmen.setGrade(0.0);
								enrollmen.setEvaluationEvent(evaluationEvent);
								this.enrollmentService.saveT(enrollmen);
							}
		
							if (enrollment.getDegrees().size() < 3 && userEnrollments.size() == 0) {
								for (int i = 0; i < enrollment.getDegrees().size(); i++) {
									Degree d = new Degree();
									int ide = enrollment.getDegrees().get(i).getDegree().getId();
									Integer idee = ide;
									Long l = Long.valueOf(idee.longValue());
									User userLogin = this.userLogin();
									d = this.degreeService.findById(l);
									enrollmen.setCreated(new Date());
									enrollmen.setCreated_By(userLogin.getId().intValue());
									enrollmen.setModified_By(userLogin.getId().intValue());
									enrollmen.setModified(new Date());
									enrollmen.setUser(u1);
									enrollmen.setDegree(d);
									enrollmen.setEvaluationCenter(evaluationCenter);
									enrollmen.setActive(true);
									enrollmen.setPriority(i + 1);
									enrollmen.setEvaluationEvent(this.evaluationEventService.findById(id));
		
									this.enrollmentService.saveT(enrollmen);
								}
							}
		
							// Creamos asociación entre Evento de Evaluación y Usuario
							EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUnique(evaluationEvent, u1);
							if (evaluationAssignment == null) {
								evaluationAssignment = new EvaluationAssignment();
								evaluationAssignment.setEvaluationEvent(evaluationEvent);
								evaluationAssignment.setUser(u1);
								evaluationAssignmentService.save(evaluationAssignment);
							}
		
							// Para cada una de las Asignaturas de cada Titulación, creamos una asociación
							// entre Evento de Evaluación, Usuario y Asignatura del evento
							userEnrollments = enrollmentService.findbyEvaluationEventAndUser(id, u1.getId());
							for (Enrollment userEnrollment : userEnrollments) {
								List<EvaluationEventDegreeSubject> eeds = evaluationEventDegreeSubjectService
										.findByEvaluationEventAndDegree(evaluationEvent.getId(), userEnrollment.getDegree().getId());
								Set<EvaluationAssignmentMatter> evaluationAssignmentMatters = new HashSet<EvaluationAssignmentMatter>();
		
								for (EvaluationEventDegreeSubject degreeSubject : eeds) {
									EvaluationEventMatter evaluationEventMatter = evaluationEventMatterService
											.findByEvaluationEventAndMatter(evaluationEvent.getId(), degreeSubject.getSubject().getId());
									EvaluationAssignmentMatter evaluationAssignmentMatter = evaluationAssignmentMatterService
											.findByEvaluationAssignmentAndEvalutionEventMatter(evaluationAssignment.getId(),
													evaluationEventMatter.getId());
		
									if (evaluationAssignmentMatter == null) {
										evaluationAssignmentMatter = new EvaluationAssignmentMatter();
										evaluationAssignmentMatter.setEvaluationAssignment(evaluationAssignment);
										evaluationAssignmentMatter.setEvaluationEventMatter(evaluationEventMatter);
										evaluationAssignmentMatter.setCenter(centers.get(0));
										evaluationAssignmentMatter.setChannel("EVS");
										evaluationAssignmentMatterService.save(evaluationAssignmentMatter);
		
										// Creamos la asociación Evento de Evaluación, Usuario, Asignatura del evento
										// y Test, usando el test asignado a la asignatura en el evento
										List<EvaluationEventMatterTest> evaluationEventMatterTests = evaluationEventMatterTestService
												.findByEvaluationEventMatter(evaluationEventMatter.getId());
										MatterTestStudent matterTestStudent = new MatterTestStudent();
										matterTestStudent.setEvaluationAssignmentMatter(evaluationAssignmentMatter);
										matterTestStudent.setEvaluationEventMatterTest(evaluationEventMatterTests.get(0));
										matterTestStudentService.save(matterTestStudent);
									}
		
									evaluationAssignmentMatters.add(evaluationAssignmentMatter);
								}
		
								userEnrollment.setEvaluationAssignmentMatters(evaluationAssignmentMatters);
								enrollmentService.update(userEnrollment);
							}
							
							responseMessage.setType(Message.TYPE_SUCCESS);
							responseMessage.setMessage("Inscripción creada correctamente");
						} else {
							responseMessage.setType(Message.TYPE_ERROR);
							responseMessage.setMessage("El usuario ya está inscrito dos titulaciones");
						}	
					} else {
						responseMessage.setType(Message.TYPE_ERROR);
						responseMessage.setMessage("El usuario ya está inscrito en la titulación " + existingEnrollment.getDegree().getName());
					}	
				} else {
					responseMessage.setType(Message.TYPE_ERROR);
					responseMessage.setMessage("El centro de evaluación seleccionado no tiene centros asociados");
				}	
			} else {
				responseMessage.setType(Message.TYPE_ERROR);
				responseMessage.setMessage("Se ha producido un error al crear la inscripción:" + enrollment.getErrorMessage());
			}
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al crear la inscripción: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/{enrollmentId}/matters", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<EvaluationAssignmentMatter> getMatters(@PathVariable Long id,
			@PathVariable Long enrollmentId) {
		Enrollment enrollment = enrollmentService.findById(enrollmentId);
		List<EvaluationAssignmentMatter> evaluationAssignmentMatters = new ArrayList<EvaluationAssignmentMatter>();

		for (EvaluationAssignmentMatter evaluationAssignmentMatter : enrollment.getEvaluationAssignmentMatters()) {
			evaluationAssignmentMatter.getEvaluationEventMatter().getMatter().setBanks(null);
			evaluationAssignmentMatter.setEvaluationAssignment(null);
			evaluationAssignmentMatters.add(evaluationAssignmentMatter);
		}

		return evaluationAssignmentMatters;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/{enrollmentId}/matter/{evaluationAssignmentMatterId}/testname", method = RequestMethod.GET, produces = "text/plain")
	public @ResponseBody String loadTestName(@PathVariable Long id, @PathVariable Long enrollmentId, @PathVariable Long evaluationAssignmentMatterId) {
		List<MatterTestStudentInfo> matterTestStudents = matterTestStudentService.findInfoByEvaluationAssignmentMatter(evaluationAssignmentMatterId);
		String testName = "";
		if (matterTestStudents.size() > 0) {
			testName = matterTestStudents.get(0).getTestName();
		}

		return testName;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/delete/{enrollmentId}", method = RequestMethod.GET)
	public @ResponseBody Message deleteEnrollmentLogic(@PathVariable Long id, @PathVariable Long enrollmentId) {
		Message responseMessage = new Message();

		try {
			Enrollment enrollment = enrollmentService.findById(enrollmentId);
			EvaluationEvent evaluationEvent = evaluationEventService.findById(id);
			
			// Si la inscripción es de prioridad 1, eliminamos todas las inscripciones del usuario y todas las relaciones del usuario con el evento, materias y tests
			if (enrollment.getPriority() == 1) {
				EvaluationAssignment evaluationAssignment = evaluationAssignmentService.findByUnique(evaluationEvent, enrollment.getUser());
				List<Enrollment> userEnrollments = enrollmentService.findbyEvaluationEventAndUser(evaluationEvent.getId(), enrollment.getUser().getId());
				
				for (Enrollment userEnrollment : userEnrollments) {
					userEnrollment.setActive(false);
					userEnrollment.setEvaluationAssignmentMatters(new HashSet<EvaluationAssignmentMatter>());
					enrollmentService.update(userEnrollment);
				}
				
				// Eliminamos todos los MatterTestStudent del usuario
				List<MatterTestStudent> matterTestStudents = matterTestStudentService.findByEvaluationAssignment(evaluationAssignment);
				for (MatterTestStudent matterTestStudent : matterTestStudents) {
					matterTestStudentService.delete(matterTestStudent.getId());
				}
				
				// Eliminamos todos los EvaluationAssignmentMatter del usuario
				List<EvaluationAssignmentMatter> evaluationAssignmentMatters = evaluationAssignmentMatterService.findByEvaluationAssignment(evaluationAssignment.getId());
				for (EvaluationAssignmentMatter evaluationAssignmentMatter : evaluationAssignmentMatters) {
					evaluationAssignmentMatterService.delete(evaluationAssignmentMatter.getId());
				}
				
				//Eliminamos la asociación entre Evento de Evaluación y Usuario
				evaluationAssignmentService.delete(evaluationAssignment.getId());
			}
			
			// Si la inscripción es de prioridad 2, eliminamos la inscripción y todas materias de la titulación que no estén en la inscripción con prioridad 1
			if (enrollment.getPriority() == 2) {
				Enrollment enrollmentPriority1 = enrollmentService.findbyEvaluationEventAndUserWithPriority1(evaluationEvent.getId(), enrollment.getUser().getId());
				
				// Obtenemmos los EvaluationAssignmentMatter de la inscripción antes de eliminar la relación inscripción - EvaluationAssignmentMatters
				Set<EvaluationAssignmentMatter> enrollmentEvaluationAssignmentMatters = enrollment.getEvaluationAssignmentMatters();
				
				// Eliminamos la inscripción
				enrollment.setActive(false);
				enrollment.setEvaluationAssignmentMatters(new HashSet<EvaluationAssignmentMatter>());
				enrollmentService.update(enrollment);
				
				// Recorremos todas las materias de la inscripción y comprobamos si la materia está en la inscripción con prioridad 1
				for (EvaluationAssignmentMatter evaluationAssignmentMatter : enrollmentEvaluationAssignmentMatters) {
					boolean foundInEnrollmentPriority1 = false;
					
					for (EvaluationAssignmentMatter eamEnrollmentPriority1 : enrollmentPriority1.getEvaluationAssignmentMatters()) {
						if (evaluationAssignmentMatter.getId().equals(eamEnrollmentPriority1.getId())) {
							foundInEnrollmentPriority1 = true;
							break;
						}
					}
					
					// Si la materia no está en la inscripción con prioridad 1, eliminamos el MatterTestStudent y el EvaluationAssignmentMatter
					if (!foundInEnrollmentPriority1) {
						List<MatterTestStudent> matterTestStudents = matterTestStudentService.findByEvaluationAssignmentMatter(evaluationAssignmentMatter);
						for (MatterTestStudent matterTestStudent : matterTestStudents) {
							matterTestStudentService.delete(matterTestStudent.getId());
						}
						
						evaluationAssignmentMatterService.delete(evaluationAssignmentMatter.getId());
					}
				}
			}

			responseMessage.setType(Message.TYPE_SUCCESS);
			responseMessage.setMessage("Inscripción eliminada correctamente");
		} catch (Exception e) {
			responseMessage.setType(Message.TYPE_ERROR);
			responseMessage.setMessage("Se ha producido un error al eliminar la inscripción: <br /><br />" + e.getMessage());
		}

		return responseMessage;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/user/identification/{idI}", method = RequestMethod.GET)
	public @ResponseBody User showEnrollmentUser(HttpServletRequest request, Map<String, Object> model,
			@PathVariable Long id, @PathVariable String idI) {
		User userEnrollment = this.userService.findByIdentification(idI);
		model.put("userEnrollment", userEnrollment);
		return userEnrollment;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/edit1/{idE}/degree/{idD}", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String edithEnrollmentLogic(HttpServletRequest request, Map<String, Object> model,
			@PathVariable Long id, @PathVariable Long idE, @PathVariable Long idD, @Valid Enrollment enrollment,
			BindingResult bindingResult, RedirectAttributes redirectAttrs,
			@ModelAttribute("successMessage") String successMessage) {
		int contador = 0;
		String retorno = "";
		Enrollment enrollment1 = this.enrollmentService.findById(idE);
		Degree d = this.degreeService.findById(idD);
		enrollment1.setDegree(d);
		List<Enrollment> enrollmentList = this.enrollmentService.findByEvaluationEvent(id);
		for (int i = 0; i < enrollmentList.size(); i++) {
			if (enrollmentList.get(i).getEvaluationEvent().getId().equals(enrollment1.getEvaluationEvent().getId())
					&& enrollmentList.get(i).getUser().getId().equals(enrollment1.getUser().getId())
					&& enrollmentList.get(i).getDegree().getId().equals(enrollment1.getDegree().getId())) {

				contador = contador + 1;
				retorno = "No se puede actualizar";
			}
		}
		if (contador == 0) {
			this.enrollmentService.update(enrollment1);
			retorno = "Actualizado correctamente";
		}

		// this.enrollmentService.delete(idE);

		// return "evaluation_event_academic/enrollment-list";
		return retorno;
	}

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value = "/evaluationevent/edit/{id}/enrollment/{enrollmentId}/matter/{matterId}/tests", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Test> loadMatterTests(@PathVariable Long id, @PathVariable Long enrollmentId,
			@PathVariable Long matterId) {
		Enrollment enrollment = enrollmentService.findById(enrollmentId);
		EvaluationAssignmentMatter evaluationAssignmentMatter = evaluationAssignmentMatterService.find(id,
				enrollment.getUser().getId(), matterId);
		List<MatterTestStudent> matterTestStudents = matterTestStudentService
				.findByEvaluationAssignmentMatter(evaluationAssignmentMatter);
		List<Test> tests = new ArrayList<Test>();
		for (MatterTestStudent matterTestStudent : matterTestStudents) {
			tests.add(matterTestStudent.getEvaluationEventMatterTest().getTest());
		}

		return tests;
	}

}