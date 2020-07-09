package com.abapblog.adt.quickfix.assist.syntax.codeParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abapblog.adt.quickfix.assist.utility.RegularExpressionUtils;

public class AbapStatement {
	private static final String fullLineCommentPattern = "^(\\*.*)|^((\\r\\n)+\\*.*)";
	private int beginOfStatement;
	private int endOfStatement;
	private String Statement;
	private String Code;
	private boolean FullLineComment;

	public AbapStatement(String code, int offset) {
		Code = code;
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
					if (Code.charAt(i) == '.') {
						endOfStatement = i - 1;
						break;
					}

					Suffix = Suffix + Code.charAt(i);
					endOfStatement = i;
				} catch (StringIndexOutOfBoundsException e) {
					break;
				}
			}
			Statement = Prefix + Suffix;
			checkFullLineComment();

		}
	}

	private void checkFullLineComment() {
		if (matchPatternSingleLine(fullLineCommentPattern))
			setFullLineComment(true);
	}

	public AbapStatement getNextAbapStatement() {
		return new AbapStatement(Code, endOfStatement + 2);

	}

	public AbapStatement getPreviousAbapStatement() {
		return new AbapStatement(Code, beginOfStatement - 2);
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

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public Boolean matchPatternSingleLine(String pattern) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public String replacePattern(String pattern, String replace) {

		String StatementCode = Statement;
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {

			return regexPatterMatcher.replaceFirst(replace);
		}

		return StatementCode;

	}

	public String replaceAllPattern(String pattern, String replace) {

		String StatementCode = Statement;
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {

			StatementCode = regexPatterMatcher.replaceAll(replace);
		}

		return StatementCode;

	}

	public String getMatchGroup(String pattern, int group) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return regexPatterMatcher.group(group);
		}

		return "";
	}

	public boolean isFullLineComment() {
		return FullLineComment;
	}

	public void setFullLineComment(boolean isFullLineComment) {
		this.FullLineComment = isFullLineComment;
	}

}
