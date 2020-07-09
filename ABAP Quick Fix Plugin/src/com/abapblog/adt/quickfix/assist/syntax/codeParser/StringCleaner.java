package com.abapblog.adt.quickfix.assist.syntax.codeParser;

public class StringCleaner {

	public static String doubleSpacePattern = "(?s)( {2,})";
	public static String doubleEnterPattern = "(?s)((\\r?\\n){2,})";
	public static String EnterPattern = "(?s)((\\r?\\n){1,})";
	public static String lineBreakAtEndPattern = "(?s)\\s*(\\r?\\n)+\\s*$";
	public static String lineBreakAtEndOfMethodPattern = "(?s)(\\s)*(\\r?\\n)+(\\s)*(\\))(\\s)*(\\r?\\n)*(\\s)*$";

	public static String clean(String code) {
		String CleanedCode = code.replaceAll(doubleEnterPattern, "\r\n");
		CleanedCode = CleanedCode.replaceAll(doubleSpacePattern, " ");
		CleanedCode = CleanedCode.replaceAll(lineBreakAtEndOfMethodPattern, " )");
		CleanedCode = CleanedCode.replaceAll(lineBreakAtEndPattern, "");
		return CleanedCode;

	}

	public static String clean(String code, boolean removeFirstCLRF) {

		String CleanedCode = code.replaceAll(doubleEnterPattern, "\r\n");
		if (removeFirstCLRF)
			CleanedCode = CleanedCode.replaceFirst(EnterPattern, "");
		CleanedCode = CleanedCode.replaceAll(doubleSpacePattern, " ");
		CleanedCode = CleanedCode.replaceAll(lineBreakAtEndOfMethodPattern, " )");
		CleanedCode = CleanedCode.replaceAll(lineBreakAtEndPattern, "");
		return CleanedCode;

	}

}
