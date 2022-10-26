package es.grammata.evaluation.evs.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import es.grammata.evaluation.evs.mvc.controller.util.StudentLoadCsvBean;
import es.grammata.evaluation.evs.mvc.controller.util.StudentScheduleCsvBean;
import es.grammata.evaluation.evs.mvc.controller.util.StudentScheduleCsvList;

public class CsvService {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CsvService.class);
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	
	
	public static void writeStudentCsv(File file, StudentScheduleCsvList sscl) throws IOException {   
      List<StudentScheduleCsvBean> studentsSchedules = new ArrayList<StudentScheduleCsvBean>();
        ICsvListWriter listWriter = null;
        try {
        	listWriter = new CsvListWriter(new FileWriter(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                
        	 
        	// mediante el campo mattersNum añadir tantos cmapos "Asignatura #" como sea necesario a la cabecera
        	        	
        	// recorrer lista de studentSchedulecsvBean y rellenar las asignaturas en funcion de sus numero de materias, las sobrantes se rellenan en blanco
        	
        	String[] headerTmp = new String[] { "Identificador de alumno",  "Código de centro de evaluación",  
        			"Período académico",  "Modalidad",  "Tipo de evaluación",  
        			"Número de asignaturas", "Código de aula", "Sistema de conexión", 
        			"Fecha test (dd/MM/yyyy)",  "Hora de inicio",  "Hora de fin",  
        			"Código del servidor"};
        	
        	//List<String> headerList = Arrays.asList(headerTmp);
        	Collection headerListColl = new ArrayList(Arrays.asList(headerTmp));
        	List<String> headerList = new ArrayList(headerListColl);
        	for(int i = 0; i < headerTmp.length; i++) {
	        	if(i == 6) {
	        		int index = i - 5;
	        		
	        		headerList.add(i, "Asignatura " + index);
	        	}
	        }
        	String[] header = headerList.toArray(new String[headerList.size()]);
             
            listWriter.writeHeader(header);
                       
			for (StudentScheduleCsvBean sscb : sscl.getStudentsCsv()) {
				listWriter.write(sscb);
			}
                
        } catch (Exception e) {
            // handle exceptions here
            e.printStackTrace();
            log.error("Error al leer CSV: " + e.getMessage());
        } finally {
            // close readers here
        	if(listWriter != null) {
        		listWriter.close();
        	}
        }
	}
	
	public static void writeMatterStudentCsv(Writer writer, List<StudentLoadCsvBean> slcl) throws Exception {
 
        ICsvBeanWriter beanWriter = null;
        try {
                beanWriter = new CsvBeanWriter(writer,
                        CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                //  beanWriter = new CsvBeanWriter(new FileWriterWithEncoding(file, StandardCharsets.UTF_8), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                
                // the header elements are used to map the bean values to each column (names must match)
                final String[] header = new String[] { "Identificador", "Cod. centro", "Periodo", "Modalidad",
                        "Cod. asignatura", "Nom. asignatura", "Cod. aula", "Conexion", "F. test", "H. inicio", "H. fin",
                        "Cod. servidor"};
                final CellProcessor[] processors = getProcessors(2);
                
                String[] mapping = new String[]{"identification", "evaluationCenterCode", 
                		"academicPeriodCode", "mode", "matterCode", "matterName", "classroomCode",
                		"connection", "testDate", "startTime", "endTime", "serverCode"};
                
                // write the header
                beanWriter.writeHeader(header);
                
    			for (StudentLoadCsvBean sscb : slcl) {
    				beanWriter.write(sscb, mapping, processors);
    			}
                
        } catch (Exception ex) {
        	ex.printStackTrace();
        	log.error(ex.getMessage());
        } finally {
            if( beanWriter != null ) {
                   beanWriter.close();
            }
        }
	}
	

	private static CellProcessor[] getWriteProcessors(int columnsSize) {
		
		return null;
	}
	
	
	public static void readStudentCsv(File file) throws IOException {    
        List<StudentScheduleCsvBean> studentsSchedules = new ArrayList<StudentScheduleCsvBean>();
        ICsvListReader listReader = null;
        try {
            listReader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            
            String[] header = listReader.getHeader(true); 
            while (listReader.read() != null) {
                CellProcessor[] processors;
                int length = listReader.length();
                
                processors = getDynamicProcessors(length);
                final List<Object> studentsList = listReader.executeProcessors(processors);
                System.out.println(String.format("lineNo=%s, rowNo=%s, columns=%s, customerList=%s",
                        listReader.getLineNumber(), listReader.getRowNumber(), studentsList.size(), studentsList));
        
                studentsSchedules.add(populatesStudentSchedule(studentsList, header.length));
            }
        } catch (Exception e) {
            // handle exceptions here
            e.printStackTrace();
            log.error("Error al leer CSV: " + e.getMessage());
        } finally {
            // close readers here
        	if(listReader != null) {
        		listReader.close();
        	}
        }
    }
	
	

	private static StudentScheduleCsvBean populatesStudentSchedule(List<Object> csvRow, int headerSize) throws Exception {
		/*1.	Identificador de alumno.
    	2.	Código de centro de evaluación.
    	3.	Período académico.
    	4.	Modalidad.
    	5.	Tipo de evaluación.
    	6.	Número de asignaturas
    	7.	A continuación hay una cantidad de columnas variable indicado por el número de asignaturas que contendrán los nombres de asignatura de las que ha de examinarse el alumno.
    	8.	Código de aula: vacía
    	9.	Sistema de conexión: vacío.
    	10.	Fecha de tests: vacía.
    	11.	Hora de inicio: vacía.
    	12.	Hora de fin: vacía.
    	13.	Código del servidor: vacía.*/
		
		StudentScheduleCsvBean sscb = new StudentScheduleCsvBean();
		sscb.setIdentifier((String)csvRow.get(0));
		sscb.setEvaluationCenterCode((String)csvRow.get(1));
		sscb.setAcademicPeriod((String)csvRow.get(2));
		sscb.setMode((String)csvRow.get(3));
		sscb.setEvaluationType((String)csvRow.get(4));
		
		Integer matterNumber = Integer.valueOf(((String)csvRow.get(5)));
		sscb.setMattersNumber(matterNumber.intValue());
		
		
		String[] matters = new String[matterNumber];
		List<String> mattersList = new ArrayList<String>();
		for(int i = 0; i < matterNumber; i++) {
			mattersList.add((String)csvRow.get(6 + i));
		}
		matters = mattersList.toArray(new String[matterNumber]);
		
		sscb.setMatters(matters);
		
		int currentPos = 6 + matterNumber;
		// numero de columnas mazimo - numero de columnas con contenido (puede haber columnas de asignaturs vacias)
		int rowUsableCols = headerSize - matterNumber - 12;
		int count = currentPos + rowUsableCols;
		sscb.setClassroomCode((String)csvRow.get(count));
		sscb.setConnectionType((String)csvRow.get(++count));
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String testsDateFormatted = (String)csvRow.get(++count);
		Date testsDate = sdf.parse(testsDateFormatted);
		sscb.setTestsDate(testsDate);
		sscb.setStartTime((String)csvRow.get(++count));
		sscb.setEndTime((String)csvRow.get(++count));
		sscb.setServerCode((String)csvRow.get(++count));
		
		
		return sscb;
	}
	
	
	
	private static CellProcessor[] getDynamicProcessors(int headerSize){
    	/*1.	Identificador de alumno.
    	2.	Código de centro de evaluación.
    	3.	Período académico.
    	4.	Modalidad.
    	5.	Tipo de evaluación.
    	6.	Número de asignaturas
    	7.	A continuación hay una cantidad de columnas variable indicado por el número de asignaturas que contendrán los nombres de asignatura de las que ha de examinarse el alumno.
    	8.	Código de aula: vacía
    	9.	Sistema de conexión: vacío.
    	10.	Fecha de tests: vacía.
    	11.	Hora de inicio: vacía.
    	12.	Hora de fin: vacía.
    	13.	Código del servidor: vacía.*/
		
		
		List<CellProcessor> cellProccessorList  = new ArrayList<CellProcessor>();
		int numSubj = headerSize - 12;
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new ParseInt());
		for(int i = 0; i < numSubj; i++) {
			cellProccessorList.add(new Optional());
		}
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new ParseDate("dd/MM/yyyy"));
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		
		CellProcessor[] cps = new CellProcessor[cellProccessorList.size()];

    	return cps;
    }
	

	public static List<StudentLoadCsvBean> readMatterStudentCsv(File file) throws IOException {

		List<StudentLoadCsvBean> slcsvs = new ArrayList<StudentLoadCsvBean>(); 
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(false);
            final CellProcessor[] processors = getProcessors(1);
            
            String[] mapping = new String[]{"identification", "evaluationCenterCode", 
            		"academicPeriodCode", "mode", "matterCode", "matterName", "classroomCode",
            		"connection", "testDate", "startTime", "endTime", "serverCode"};

            StudentLoadCsvBean slcsv = null;
            while ((slcsv = beanReader.read(StudentLoadCsvBean.class, mapping, processors)) != null) {
                // process course
                //System.out.println(slcsv);
                slcsvs.add(slcsv);
            }   
        } catch (Exception ex) {
        	ex.printStackTrace();   
        	slcsvs = null;
        } finally { 
            if (beanReader != null) {
                beanReader.close();
            }
        }
        
        return slcsvs;
    }
	
	
	/*
	private CellProcessor[] getDynamicProcessors(int headerSize){

		
		
		List<CellProcessor> cellProccessorList  = new ArrayList<CellProcessor>();
		int numSubj = headerSize - 12;
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new ParseInt());
		for(int i = 0; i < numSubj; i++) {
			cellProccessorList.add(new Optional());
		}
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new ParseDate("dd/MM/yyyy"));
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		cellProccessorList.add(new NotNull());
		

    	return (CellProcessor[])cellProccessorList.toArray();
    	
    	

    }*/
	
	

    private static CellProcessor[] getProcessors(int type){ 	
    	/*1.	Identificador de alumno.
    	2.	Código de centro de evaluación.
    	3.	Período académico.
    	4.	Modalidad.
    	5.	Código de asignatura.
    	6.	Nombre asignatura.
    	7.	Código de aula: vacía
    	8.	Sistema de conexión: vacío.
    	9.	Fecha de tests: vacía.
    	10.	Hora de inicio: vacía.
    	11.	Hora de fin: vacía.
    	12.	Código del servidor: vacía.*/

    	if(type == 1) {
	        return new CellProcessor[] {
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new NotNull(),
	                new ParseDate("dd/MM/yyyy"),
	                new NotNull(),
	                new NotNull(),
	                new NotNull()
	        };
    	} else {       
    		return new CellProcessor[] {
	                new NotNull(),
	                new NotNull(),
	                new Optional(),
	                new Optional(),
	                new NotNull(),
	                new NotNull(),
	                new Optional(),
	                new Optional(),
	                new Optional(),
	                new Optional(),
	                new Optional(),
	                new Optional()
	        };
    	}
    }
	
	
	/*private void readCSV(File file) throws Exception {
		
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), 
					CsvPreference.STANDARD_PREFERENCE);
			beanReader.getHeader(false);
			List<StudentLoadCsvBean> rows = new ArrayList<StudentLoadCsvBean>();
			
			// CsvAnnotationBeanParser helper = new CsvAnnotationBeanParser();
			 //CsvBeanMapping<SampleBean1> mappingBean = helper.parse(SampleBean1.class);
		        
		       // String[] nameMapping = mappingBean.getNameMapping();
		        //CellProcessor[] processors = mappingBean.getInputCellProcessor();
			
			StudentLoadCsvBean row;
			while ((row = beanReader.read(StudentLoadCsvBean.class)) != null) {
				rows.add(row);
			}
			
			if(rows == null) {
				int i = 0;
			}
			//return customers;
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		
	}*/

}
