package es.grammata.evaluation.evs.services.httpservices.client;  

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.Department;
import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.data.services.repository.DepartmentService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTypeService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationTypeService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.TestService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.controller.util.BankEditPeriod;
import es.grammata.evaluation.evs.mvc.controller.util.BankEditTeacherPeriod;
import es.grammata.evaluation.evs.services.httpservices.wrapper.TestWrap;
import siette.interfaces.BancosPreguntasServicios;
import siette.interfaces.TestsServicios;
import siette.models.BancoPreguntas;
import siette.models.DatosSesion;
import siette.models.RangoAsignatura;
import siette.models.RangoProfesorAsignatura;
import siette.models.Tests;
  
@org.springframework.stereotype.Component
@Configurable
public class BankClient {  
	
	private static org.apache.log4j.Logger log = Logger
			.getLogger(BankClient.class);
	
	@Autowired
	public BankService bankService;
	
	@Autowired
	public TestService testService;
	
	@Autowired
	public DepartmentService departmentService;
	
	@Autowired
	public EvaluationTypeService evaluationTypeService;
	
	@Autowired
	public BancosPreguntasServicios bancosPreguntasServicios;
	
	@Autowired
	public TestsServicios testsServicios;
	
	@Autowired
	public UserService userService;
	
	@Autowired 
	public EvaluationEventTypeService evaluationEventTypeService;
	
	@Autowired
	public MatterTestStudentService matterTestStudentService;
	
	
    public List<Bank> loadAndCreateBanks() {    
    	List<BancoPreguntas> bancosPreguntas = bancosPreguntasServicios.getListaBancosPreguntas();
    	if (!bancosPreguntas.isEmpty()) {
    		bankService.disableAll();
    	}
    	List<Bank> banks = new ArrayList<Bank>();
    	for(BancoPreguntas banco : bancosPreguntas) {
    		Bank bank = bankService.findByExternalId(banco.getId());
    		try {
	    		if (bank == null) {
	    			bank = new Bank();
	    			bank.setCreateDate(banco.getFechaCreacion());
	    			bank.setModifiedDate(banco.getFechaActualizacion());
	    			bank.setName(banco.getNombre());
	    			bank.setCurrentNumber(banco.getNumActual());
	    			bank.setQuestionNumber(banco.getNumeroPreguntas());
	    			bank.setExternalId(banco.getId());
	    			bank.setEvaluationEventType(evaluationEventTypeService.findById((long)banco.getTipoEventoEvaluacion()));
	    			if(banco.getEstado() == 1) {
	    				bank.setState(Bank.STATE_ACTIVO);
	    			} else {
	    				bank.setState(Bank.STATE_INACTIVO);
	    			}
	    			bank.setState(String.valueOf(banco.getEstado()));
	    			
	    			String departmentName = banco.getDepartamento();
	    			if(departmentName != null && !departmentName.equals("")) {
		    			Department department = departmentService.findByName(departmentName);
		    			if (department == null) {
		    				department = new Department();
		    				department.setName(departmentName);
		    				departmentService.save(department);
		    			}
		    			bank.setDepartment(department);
	    			}
	    			
	    			bankService.save(bank);
	    		} else {
	    			bank.setName(banco.getNombre());
	    			if(banco.getEstado() == 1) {
	    				bank.setState(Bank.STATE_ACTIVO);
	    			} else {
	    				bank.setState(Bank.STATE_INACTIVO);
	    			}
	    			bank.setCurrentNumber(banco.getNumActual());
	    			bank.setQuestionNumber(banco.getNumeroPreguntas());	
	    			
	    			String departmentName = banco.getDepartamento();
	    			if(departmentName != null && !departmentName.equals("")) {
		    			Department department = departmentService.findByName(departmentName);
		    			if (department == null) {
		    				department = new Department();
		    				department.setName(departmentName);
		    				departmentService.save(department);
		    			}
		    			bank.setDepartment(department);
	    			}
	    			
	    			bankService.update(bank);
	    		}
	    		
	    		loadAndCreateTests(bank);
	
	    		banks.add(bank);
    		
        	} catch (Exception ex) {
        		log.error("Error cargando de Siette banco: " + banco.getId() + "; ex: "  + ex.getMessage());
        	}
     	}

    	
    	return banks;
    }  
    
    
    public List<Test> loadAndCreateTests(Bank bank) {  	
    	List<Test> testsLoaded = testService.findByBank(bank.getId());
    	Set<Test> bankTests = new HashSet<Test>();
    	if(testsLoaded != null)
    		bankTests = new HashSet<Test>(testsLoaded);
    	List<Tests> tests = testsServicios.getTestsBanco(bank.getExternalId());
    	List<Test> evsTests = new ArrayList<Test>();
    	for(Tests test : tests) {
    		Test evsTest = testService.findByExternalId(test.getId());  		
    		if (evsTest == null) {
    			evsTest = new Test();
    			evsTest.setCreateDate(test.getFechaCreacion());
    			evsTest.setExternalId(test.getId());
    			evsTest.setMaxQuestionNum(test.getNumPreguntasMax());
    			evsTest.setMinQuestionNum(test.getNumPreguntasMin());
    			evsTest.setTime(test.getTiempo());
    			evsTest.setName(test.getNombre());
    			int active = (test.getEstaActivo()) ? 1 : 0;
    			evsTest.setActive(active);
    			evsTest.setBank(bank);
    			
    			EvaluationType evaluationType = getEvaluationTypeByTestName(evsTest.getName());
    			if (evaluationType != null) {
    				evsTest.setEvaluationType(evaluationType);
    			}
    			
    			testService.save(evsTest);
    		} else {
    			evsTest.setName(test.getNombre());
    			evsTest.setMaxQuestionNum(test.getNumPreguntasMax());
    			evsTest.setMinQuestionNum(test.getNumPreguntasMin());
    			evsTest.setTime(test.getTiempo());
    			int active = (test.getEstaActivo()) ? 1 : 0;
    			evsTest.setActive(active);
    			
    			if (evsTest.getEvaluationType() == null) {
    				EvaluationType evaluationType = getEvaluationTypeByTestName(evsTest.getName());
        			if (evaluationType != null) {
        				evsTest.setEvaluationType(evaluationType);
        			}
    			}
    			
    			testService.update(evsTest);
    		}
    		evsTests.add(evsTest);
    		
    		bankTests.remove(evsTest);
     	}
    	
    	// los que quedan ya no estan en siette hay que marcarlos como inactivos
    	for(Test test : bankTests) {
    		test.setActive(0);
    		testService.update(test);
    	}
    	
    	
    	return evsTests;
    }
    
    private EvaluationType getEvaluationTypeByTestName(String testName) {
    	String evaluationTypeCode = testName.split(" ")[0];
    	EvaluationType evaluationType = evaluationTypeService.findByCode(evaluationTypeCode.trim());
    	if (evaluationType != null) {
    		return evaluationType;
    	} else {
    		evaluationType = evaluationTypeService.findByCodeWs(evaluationTypeCode.trim());
    		if (evaluationType != null) {
        		return evaluationType;
        	}
    	}
    	
    	if (testName.contains("-")) {
    		evaluationTypeCode = testName.split("-")[0];
        	evaluationType = evaluationTypeService.findByCode(evaluationTypeCode.trim());
        	if (evaluationType != null) {
        		return evaluationType;
        	} else {
        		evaluationType = evaluationTypeService.findByCodeWs(evaluationTypeCode.trim());
        		if (evaluationType != null) {
            		return evaluationType;
            	}
        	}
    	}
    	
    	if (testName.contains(":")) {
    		evaluationTypeCode = testName.split(":")[0];
        	evaluationType = evaluationTypeService.findByCode(evaluationTypeCode.trim());
        	if (evaluationType != null) {
        		return evaluationType;
        	} else {
        		evaluationType = evaluationTypeService.findByCodeWs(evaluationTypeCode.trim());
        		if (evaluationType != null) {
            		return evaluationType;
            	}
        	}
    	}
    	
    	return null;
    }
    
    public List<TestWrap> getResults(Test test, Long evaluationEventId) {
    	List<DatosSesion> datosSesion = testsServicios.getSesionesTest(test.getExternalId());
    	
    	List<TestWrap> tests = new ArrayList<TestWrap>();
    	for(DatosSesion datos : datosSesion) {
    		int nSuccess = datos.getnAciertos();
    		int nFails = datos.getnFallos();
    		int nBlanks = datos.getnSinContestar();
    		String token = datos.getToken();
    		int idSesion = datos.getIdSesion();
    		String username = datos.getNick();
    		
    		/*********************** TEMPORAL PARA NOMBRES DE SIETTE QUE LLEVAN UN CARACTER ADICIONAL AL FINAL **********/
			//if(username != null && username.length() > 0) {
			//	username = username.substring(0, username.length()-1);
			//}
			/************************************************************************************************************/
			
    		
    		User user = userService.findByUsername(username);
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date testDate = null;
    		try {
				testDate = df.parse(datos.getFecha());
			} catch (ParseException e) {
				
			}
    		
    		if(user != null) {
    			boolean isIntoEE = matterTestStudentService.checkEvaluationEventByParameters(evaluationEventId, user.getId(), test.getId(), testDate);
    			// si pertenece a este evento de evaluacion
    			if(isIntoEE) {
	    			TestWrap testWrap = new TestWrap(user, test, nSuccess, nFails, nBlanks, token, idSesion, testDate);
	    			tests.add(testWrap);
    			}
    		}
    	}
    	
    	return tests;
    }
    
    public TestWrap getResultsStudent(Test test, String username) {

    	DatosSesion datosSesion = testsServicios.getSesionTestAlumno(test.getExternalId(), username);
    	
		int nSuccess = datosSesion.getnAciertos();
		int nFails = datosSesion.getnFallos();
		int nBlanks = datosSesion.getnSinContestar();
		String token = datosSesion.getToken();
		int idSesion = datosSesion.getIdSesion();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date testDate = null;
		try {
			testDate = df.parse(datosSesion.getFecha());
		} catch (ParseException e) {
			
		}
		
		User user = userService.findByUsername(datosSesion.getNick());
    		
    	TestWrap testWrap = new TestWrap(user, test, nSuccess, nFails, nBlanks, token, idSesion, testDate);
    	
    	return testWrap;
    }
    

    public List<BankEditPeriod> loadPeriodsBanks(Bank bank) {    
    	List<BankEditPeriod> periods = new ArrayList<BankEditPeriod>();
    	List<RangoAsignatura> listPeriods = bancosPreguntasServicios.getRangosModificacionAsignatura(bank.getExternalId());
    	for(RangoAsignatura period :listPeriods){
    		BankEditPeriod bp = new BankEditPeriod();
    		bp.setEndDate(period.getFechaFin());
    		bp.setInitDate(period.getFechaInicio());
    		bp.setId(period.getIdPermiso());
    		
    		periods.add(bp);
    	}
    	return periods;
    }  

    public void addTeachersPeriodsBanks(Bank bank, User user, Date startDate, Date endDate) {  
    	bancosPreguntasServicios.setRangoPermisosProfesorBancoPreguntas(bank.getExternalId(),startDate, endDate, user.getUsername());
    } 
    
    public void addPeriodsBanks(Bank bank, Date startDate, Date endDate) {  
    	bancosPreguntasServicios.setRangoPermisosBancoPreguntas(bank.getExternalId(),startDate, endDate);
    } 
    
    public void modifyPeriodBank(int periodId, int bankId, Date initDate, Date endDate) {  
    	bancosPreguntasServicios.modificarRangoModificacionAsignatura(periodId, bankId, initDate, endDate);
    } 
    
    public void deletePeriodBank(int periodId) {  
    	bancosPreguntasServicios.borrarRangoModificacionAsignatura(periodId);
    } 
    
    public List<BankEditTeacherPeriod> loadTeachersPeriodsBanks(Bank bank) {    
    	List<BankEditTeacherPeriod> periods = new ArrayList<BankEditTeacherPeriod>();
    	List<RangoProfesorAsignatura> listPeriods = bancosPreguntasServicios.getRangosModificacionProfesorAsignatura(bank.getExternalId());
    	for(RangoProfesorAsignatura period :listPeriods){
    		BankEditTeacherPeriod bankEditTeacherPeriod = new BankEditTeacherPeriod();
    		bankEditTeacherPeriod.setEndDate(period.getFechaFin());
    		bankEditTeacherPeriod.setInitDate(period.getFechaInicio());
    		bankEditTeacherPeriod.setId(period.getIdPermiso());
    		bankEditTeacherPeriod.setTeacher(userService.findByUsername(period.getIdProfesor()));
    		periods.add(bankEditTeacherPeriod);
    	}
    	return periods;
    } 
    
    public BancoPreguntas loadBank(int bankExternalId) {
    	BancoPreguntas banco = bancosPreguntasServicios.getBancoPreguntas(bankExternalId);
    	return banco;
    }
    
    
    public int getUsablesQuestions(int testId) {
    	int nquestions = 0;
    	try {
    		nquestions = testsServicios.getNumeroPreguntasUsablesTest(testId);
    	} catch(Exception ex) {
    		log.error("Error obteniendo preguntas usables de test: " + testId);
    	}
    	return nquestions;
    }
    
  
}  