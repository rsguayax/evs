package es.grammata.evaluation.evs.mvc.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;
import es.grammata.evaluation.evs.mvc.base.BaseController;
import es.grammata.evaluation.evs.mvc.controller.util.Message;
import es.grammata.evaluation.evs.mvc.controller.util.StudentLoadCsvBean;
import es.grammata.evaluation.evs.mvc.controller.util.StudentTestsSchedules;
import es.grammata.evaluation.evs.util.CsvService;



@Controller
public class FileSchedulesController extends BaseController {

	private static org.apache.log4j.Logger log = Logger.getLogger(FileSchedulesController.class);
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Autowired
	private EvaluationAssignmentMatterService evaluationAssignmentMatterService;
	
	@Autowired
	private EvaluationCenterService evaluationCenterService;
	
	@Autowired
	private StudentTestsSchedules studentTestsSchedules;
	
	@Value("${doc.base_path}")
	private String DOC_BASE_PATH;
	
	@Value("${doc.tmp_files}")
	private String DOC_TMP_FILES;
	
	public static boolean RUNNING = false;
	
	/*
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/evaluationevent/{evaluationEventId}/unifyse", method = RequestMethod.GET)
	public void unify(@PathVariable Long evaluationEventId) {
		studentTestsSchedules.unifyStudentEvaluation(evaluationEventId);
	}
	*/
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/evaluationevent/{evaluationEventId}/uploadschedules", method = RequestMethod.POST)
	public @ResponseBody Message uploadSchedules(@RequestParam("file") MultipartFile file, @PathVariable Long evaluationEventId) {
		
		Message message = new Message();
		message.setMessage("Archivo procesado correctamente");
		message.setType(Message.TYPE_SUCCESS);
		
		if(!RUNNING) {
			try {
				RUNNING = true;
				TimeUnit.SECONDS.sleep(2);
				if (!file.isEmpty()) {
					String name = file.getOriginalFilename();
					byte[] bytes = file.getBytes();
	
					// Creating the directory to store file
					String rootPath = DOC_BASE_PATH;
					File dir = new File(rootPath + File.separator + DOC_TMP_FILES);
					if (!dir.exists())
						dir.mkdirs();
	
					// Create the file on server
					File serverFile = new File(dir.getAbsolutePath()
							+ File.separator + name);
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
	
					log.debug("Server File Location="
							+ serverFile.getAbsolutePath());
					
					log.debug("File name="
							+ name);
					
					/* File convFile = new File(name);
				    convFile.createNewFile(); 
				    FileOutputStream fos = new FileOutputStream(convFile); 
				    fos.write(bytes);
				    fos.close(); */
					
				    List<StudentLoadCsvBean> slcsvs = CsvService.readMatterStudentCsv(serverFile);
				    if(slcsvs == null) {
				    	message.setMessage("Error al procesar fichero, asegúrese que los campos son correctos");
						message.setType(Message.TYPE_ERROR);
				    } else {
				    	studentTestsSchedules.assignScheduleToTestManual(slcsvs, evaluationEventId);
				    }
				} else {
					message.setMessage("Archivo vacío");
					message.setType(Message.TYPE_ERROR);	
				}
			} catch (Exception ex) {
				message.setMessage("Se ha producido un error al procesaro el archivo");
				message.setType(Message.TYPE_ERROR);	
				ex.printStackTrace();
				log.error(ex.getMessage());
			}  finally {
				RUNNING = false;
			}
		} else {
			message.setType(Message.TYPE_WARNING);
			message.setMessage("Ya existe un proceso de asignación en curso, espere unos momentos");
		}
		
		return message;
	}
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator')")
	@RequestMapping(value = "/evaluationevent/{id}/downloadschedules", method = RequestMethod.POST)
	public @ResponseBody void downloadSchedules(@PathVariable Long id, HttpServletResponse response) {
		
		try {
			/***********************************
			// Creating the directory to store file
			String rootPath = System.getProperty("catalina.home");
			File dir = new File(rootPath + File.separator + "tmpFiles");
			if (!dir.exists())
				dir.mkdirs();

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath()
					+ File.separator + name);
		
			log.info("Server File Location="
					+ serverFile.getAbsolutePath());
			**********************************/

			List<StudentLoadCsvBean> list = new ArrayList<StudentLoadCsvBean>();

			
			list = generateStudentMatters(id);
			
		    String csvFileName = "asignacion_horarios.csv";

	        response.setContentType("text/csv");

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                csvFileName);
	        response.setHeader(headerKey, headerValue);
	        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");

	        CsvService.writeMatterStudentCsv(response.getWriter(), list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private List<StudentLoadCsvBean> generateStudentMatters(Long evaluationEventId) {
		List<EvaluationAssignmentMatter> eams = evaluationAssignmentMatterService.findByEvent(evaluationEventId);
		List<StudentLoadCsvBean> list = new ArrayList<StudentLoadCsvBean>();
		for(EvaluationAssignmentMatter eam : eams) {
			// crear un objeto StudentLoadCsvBean a partir de cada eam para pasar la lista al writer
			StudentLoadCsvBean slcb = new StudentLoadCsvBean();
			EvaluationAssignment ea = eam.getEvaluationAssignment();
			User user = ea.getUser();
			slcb.setIdentification(user.getIdentification());
			if (eam.getEvaluationEventMatter().getAcademicPeriod() != null) {
				slcb.setAcademicPeriodCode(eam.getEvaluationEventMatter().getAcademicPeriod().getCode());
			}
			if(eam.getCenter() == null) {
				continue;
			}
			
		
			// slcb.setEvaluationCenterCode(eam.getCenter().getEvaluationCenter().getCode());
			
			// un centro educativo puede estar asignado a varios centros de evaluacion en el evento
			// si solo esta asociado a uno asignamos a ese, en caso contrario asignamos el primero que encontremos
			// durante el proceso de revisión manual del fichero por parte del usuario lo cambiará si es necesario
			List<EvaluationCenter> evaluationCentersAllowed = evaluationCenterService.findByEvaluationEventAndCenter(ea.getEvaluationEvent().getId(), eam.getCenter().getId());
			if(evaluationCentersAllowed != null && evaluationCentersAllowed.size() > 0) {
				slcb.setEvaluationCenterCode(evaluationCentersAllowed.get(0).getCode());
			} else { // si no tuviese asignamos el que tiene por defecto (no debería darse este caso jamas)
				slcb.setEvaluationCenterCode(eam.getCenter().getEvaluationCenter().getCode());
			}
			
			slcb.setMatterCode(eam.getEvaluationEventMatter().getMatter().getCode());
			slcb.setMatterName(eam.getEvaluationEventMatter().getMatter().getName());
			if (eam.getEvaluationEventMatter().getMode() != null) {
				slcb.setMode(eam.getEvaluationEventMatter().getMode().getCode());
			}
			list.add(slcb);
		}
		
		return list;
	}
	
	
	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager', 'evaluation_center_supporter')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assign/isrunningmanual", method=RequestMethod.GET)
	public @ResponseBody boolean isRunning(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		return RUNNING;
	}
	

	@PreAuthorize("@evsSecurityHandler.hasMProfile('administrator', 'logistics_manager')")
	@RequestMapping(value="/evaluationevent/{evaluationEventId}/tests/assignresetmanual", method=RequestMethod.GET)
	public @ResponseBody void loadReset(@PathVariable Long evaluationEventId, HttpServletRequest request) {
		RUNNING = false;
	}
	
}
