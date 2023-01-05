package com.abapblog.adt.quickfix.assist.syntax.statements.loop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.abapblog.adt.quickfix.assist.utility.RegularExpressionUtils;

public class LoopAtItabWithHeaderLineAsFieldSymbol extends StatementAssist {
	protected String internalTableName = "";
	private AbapStatement currentStatement;
	private int finalStatementOffset;
	private static final String modifyPattern = ".*(MODIFY\\s*ITAB\\s*\\.)";
	private static final String deletePattern = ".*(DELETE\\s*ITAB\\s*\\.)";
	private static final String replacePattern = "\"$1";

	public LoopAtItabWithHeaderLineAsFieldSymbol() {
		super();
		currentStatement = CodeReader.CurrentStatement;
	}

	@Override
	public String getChangedCode() {
		getReplaceLength();
		String updatedCode = CodeReader.getCode().substring(currentStatement.getBeginOfStatementWithoutLeadingComment(),
				finalStatementOffset);
		updatedCode = updatedCode.replaceAll(internalTableName + "-", "<" + internalTableName + ">-");
		updatedCode = updatedCode.replaceFirst(internalTableName,
				internalTableName + " ASSIGNING FIELD-SYMBOL(<" + internalTableName + ">)");
		if (matchPattern(modifyPattern.replaceFirst("ITAB", internalTableName), updatedCode)) {
			updatedCode = replacePattern(modifyPattern.replaceFirst("ITAB", internalTableName), replacePattern,
					updatedCode);
		}
		if (matchPattern(deletePattern.replaceFirst("ITAB", internalTableName), updatedCode)) {
			updatedCode = replacePattern(deletePattern.replaceFirst("ITAB", internalTableName), replacePattern,
					updatedCode);
		}
		return updatedCode;
	}

	@Override
	public String getAssistShortText() {
		return "Replace Loop At ITAB with Header Line with Loop at FS";
	}

	@Override
	public String getAssistLongText() {
		return "Replaces the old fashion LOOP AT ITAB. into LOOP AT ITAB ASSIGNING FIELD-SYMBOL, and replaces all "
				+ " occurences of the ITAB in the loop with field-symbol variable.\n MODIFY and DELETE statemens are commented out.";

	}

	@Override
	public boolean canAssist() {
		if (currentStatement.statementTokens.size() == 3
				&& currentStatement.statementTokens.get(0).name.toUpperCase().equals("LOOP")) {
			internalTableName = currentStatement.statementTokens.get(2).name;
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int getStartOfReplace() {
		return currentStatement.getBeginOfStatementWithoutLeadingComment();
	}

	@Override
	public int getReplaceLength() {
		int numberOfLoops = 1;
		int numberOfEndloops = 0;
		int intialStatementStart = currentStatement.getBeginOfStatementWithoutLeadingComment();
		finalStatementOffset = 0;
		AbapStatement LastStatement = currentStatement;
		while (numberOfLoops != numberOfEndloops) {
			AbapStatement checkedStatement = LastStatement.getNextAbapStatement();
			if (checkedStatement.statementTokens.get(0).name.toUpperCase().equals("LOOP")) {
				numberOfLoops = numberOfLoops + 1;
			} else if (checkedStatement.statementTokens.get(0).name.toUpperCase().equals("ENDLOOP")) {
				numberOfEndloops = numberOfEndloops + 1;
				finalStatementOffset = checkedStatement.getEndOfStatement();
			}
			LastStatement = checkedStatement;
		}

		return finalStatementOffset - intialStatementStart;
	}

	public Boolean matchPattern(String pattern, String code) {

		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(code, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public String replacePattern(String pattern, String replace, String code) {

		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(code, redexPattern, 1000);
		while (regexPatterMatcher.find()) {

			return regexPatterMatcher.replaceFirst(replace);
		}

		return code;

	}

	public String getMatchGroup(String pattern, int group, String code) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(code, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return regexPatterMatcher.group(group);
		}

		return "";
	}

}
