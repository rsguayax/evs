package es.grammata.evaluation.evs.util;

public class EnrollmentUtils {
	public static String getStatusString(int status) {
		if (status == 1) {
			return "APTO";
		} else if (status == 2) {
			return "NO CONSUMIDA";
		} else if (status == 3) {
			return "CURSO 0";
		} else if (status == 4) {
			return "NO APTO";
		} else if (status == 5) {
			return "ADMITIDO MANUALMENTE";
		} else if (status == 6) {
			return "EN PROCESO";
		} else if (status == 7) {
			return "NO APLICABLE";
		} else if (status == 8) {
			return "DENEGADO";
		} else {
			return "DESCONOCIDO";
		} 
	}
}
