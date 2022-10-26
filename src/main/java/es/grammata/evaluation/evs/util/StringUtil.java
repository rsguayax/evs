package es.grammata.evaluation.evs.util;

public class StringUtil {
	public static String replaceToHTML(String source) {
		source = source.replace("á", "&aacute;").replace("Á", "&Aacute;").replace("é", "&eacute;").replace("É", "&Eacute;")
				.replace("í", "&iacute;").replace("Í", "&Iacute;").replace("ó", "&oacute;").replace("Ó", "&Oacute;").replace("ú", "&uacute;").replace("Ú", "&Uacute;")
				.replace("ñ", "&ntilde;").replace("Ñ", "&Ntilde;");
		return source;
	}
	
	public static String dropAccentMark(String source) {
		source = source.replace("á", "a").replace("Á", "A").replace("é", "e").replace("É", "E")
				.replace("í", "i").replace("Í", "I").replace("ó", "o").replace("Ó", "O").replace("ú", "u")
				.replace("Ú", "U").replace("ñ", "ñ").replace("Ñ", "Ñ");
		return source;
	}

}
