package com.abapblog.adt.quickfix.assist.syntax.statements.texts;

import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class EmptyToSpace extends StatementAssistRegex {

	private static final String MatchPattern = "(\\w*)\\s*('\\s?')";
	private static final String ReplacementPattern = "$1 space";

	@Override
	public String getAssistShortText() {
		return "Replace ' ' with space";
	}

	@Override
	public String getAssistLongText() {
		return " ";
	}

	@Override
	public boolean canAssist() {

		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {
			return true;
		}

		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatementReplacement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

	@Override
	public String getMatchPattern() {
		return MatchPattern;
	}

	@Override
	public String getReplacePattern() {
		return ReplacementPattern;
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

}
