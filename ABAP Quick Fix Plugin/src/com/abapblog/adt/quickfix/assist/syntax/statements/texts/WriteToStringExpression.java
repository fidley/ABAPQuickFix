package com.abapblog.adt.quickfix.assist.syntax.statements.texts;

import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public class WriteToStringExpression extends StatementAssistRegex {

	private static final String TOKEN = "WRITE";
	private static final String MatchPattern = "WRITE\\s*(\\w*)\\s*TO\\s*(\\S*)";
	private static final String ReplacementPattern = "$2 = |{ $1 }|";

	@Override
	public String getAssistShortText() {
		return "Replace WRITE x TO y with y = |{ x }|";
	}

	@Override
	public String getAssistLongText() {
		return " ";
	}

	@Override
	public boolean canAssist() {
		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(TOKEN)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)
					&& CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatement();
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
