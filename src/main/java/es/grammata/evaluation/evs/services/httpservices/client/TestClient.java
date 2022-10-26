package es.grammata.evaluation.evs.services.httpservices.client;  

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import siette.interfaces.GruposServicios;
import siette.models.Grupo;
import siette.models.Usuario;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.mvc.controller.util.ExtendedUser;
import es.grammata.evaluation.evs.mvc.controller.util.TestGroup;
  
@org.springframework.stereotype.Component
@Configurable
public class TestClient { 
	
	private static final String DATE_FORMAT_SIETTE = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	public GruposServicios gruposServicios;
	
	
	
	public boolean createGroupsWithCap(List<TestGroup> testGroups, EvaluationEvent evaluationEvent) throws Exception {
		boolean res = true;
		for(TestGroup testGroup : testGroups) {
			List<Usuario> listaAlumnos = new ArrayList<Usuario>();
			List<ExtendedUser> users = testGroup.getUsers();
			for(ExtendedUser extendedUser : users) {
				User user = extendedUser.getUser();
				// hasta que se cambie la descripcion del constructor en nombre va el nombre completo y en apellidos el identificador
				String fullName = user.getFirstName();
				if(user.getLastName() != null) {
					fullName = user.getFirstName() + " " + user.getLastName();
				}
				
				if (user.getUsername() == null || user.getUsername().isEmpty()) {
					throw new Exception("El usuario con ID " + user.getId() + " no tiene nombre de usuario");
				}
				
				Usuario usuario = new Usuario(fullName, user.getIdentification(), user.getUsername(), extendedUser.getExternalPassword(), user.getEmail());
				listaAlumnos.add(usuario);
			}
			
			// idProfesor 1 para root en siette
			int groupId = callSetGroupWithCap(testGroup.getExternalTestId(), listaAlumnos, testGroup.getInitDate(),  testGroup.getEndDate(), 1, 
					testGroup.getBankExternalId(), testGroup.getEvaluationEventCode(), testGroup.getCapSsid(), testGroup.getMatterCode(), 
					testGroup.getEvaluationType(), testGroup.getEvalCenter(), testGroup.getClassroom(), testGroup.getBlockId(), evaluationEvent);
			
			if(groupId == -1) {
				res = false;
				break;
			}
		}
		
		return res;
	}
	
	private int callSetGroupWithCap(Integer testId, List<Usuario> listaAlumnos, Date initDate, Date endDate, int idProfesor, 
			int idBanco, String evaluationEventCode, String capSsid, String matterCode, String evalType, 
			String evalCenter, String classroom, String blockId, EvaluationEvent evaluationEvent) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SIETTE);
		String initDateFormat = sdf.format(initDate);
		String endDateFormat = sdf.format(endDate);
		
		SimpleDateFormat grupoFormat = new SimpleDateFormat("ddMMyy");
		
		String groupName = evaluationEventCode + "_" + evalCenter + "_" + classroom + "_" + matterCode + "_" + evalType + "_" + testId + "_" + blockId + "_" + grupoFormat.format(evaluationEvent.getStartDate());
		groupName = groupName.toUpperCase();
	
		Grupo grupo = new Grupo(groupName, idProfesor, idBanco, null, listaAlumnos);
		
		// setGrupoConUsuariosYTest(Grupo grupoEntrada, int idTest, String disponibleDesde, String disponibleHasta, String codigoEventoEvaluacion, String cap);
		return gruposServicios.setGrupoConUsuariosYTest(grupo, testId, initDateFormat, endDateFormat, evaluationEventCode, capSsid);	
	}
	
	public int callSetGroupWithoutCap(EvaluationEvent evaluationEvent, Integer testId, List<Usuario> listaAlumnos, Date startDate, Date endDate, int idProfesor, int idBanco, String matterCode, String evaluationType) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SIETTE);
		String initDateFormat = sdf.format(startDate);
		String endDateFormat = sdf.format(endDate);
		String capSsid = "NoCapSsid";
		
		SimpleDateFormat grupoFormat = new SimpleDateFormat("ddMMyy");
		
		String groupName = evaluationEvent.getCode() + "_"  + matterCode + "_" + evaluationType + "_" + testId +  "_" + grupoFormat.format(evaluationEvent.getStartDate());
		groupName = groupName.toUpperCase();
	
		Grupo grupo = new Grupo(groupName, idProfesor, idBanco, null, listaAlumnos);
		
		// setGrupoConUsuariosYTest(Grupo grupoEntrada, int idTest, String disponibleDesde, String disponibleHasta, String codigoEventoEvaluacion, String cap);
		return gruposServicios.setGrupoConUsuariosYTest(grupo, testId, initDateFormat, endDateFormat, evaluationEvent.getCode(), capSsid);	
	}
}  