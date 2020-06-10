package com.abapblog.adt.quickfix.assist.syntax.codeParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbapStatement {
	private int beginOfStatement;
	private int endOfStatement;
	private String Statement;

	public AbapStatement(String Code, int offset) {
		String Prefix = "";
		String Suffix = "";
		if (offset >= 0) {
			for (int i = offset; i > 0; i--) {
				if (Code.charAt(i) == '.')
					break;
				Prefix = Code.charAt(i) + Prefix;
				beginOfStatement = i;
			}

			for (int i = offset + 1; i > 0; i++) {
				try {
					if (Code.charAt(i) == '.')
						break;
					Suffix = Suffix + Code.charAt(i);
					endOfStatement = i;
				} catch (StringIndexOutOfBoundsException e) {
					break;
				}
			}
			Statement = Prefix + Suffix;
		}
	}

	public String getStatement() {
		return Statement;
	}

	public int getEndOfStatement() {
		return endOfStatement;
	}

	public int getBeginOfStatement() {
		return beginOfStatement;
	}

	public int getStatementLength() {
		return endOfStatement - beginOfStatement + 1;
	}

	public Boolean matchPattern(String pattern) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = redexPattern.matcher(Statement);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public String replacePattern(String pattern, String replace) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = redexPattern.matcher(Statement);
		while (regexPatterMatcher.find()) {
			return regexPatterMatcher.replaceFirst(replace);
		}

		return "";

	}

}
