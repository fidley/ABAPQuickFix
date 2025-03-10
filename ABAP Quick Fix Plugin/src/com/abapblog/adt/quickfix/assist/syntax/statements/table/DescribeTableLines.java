package com.abapblog.adt.quickfix.assist.syntax.statements.table;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public class DescribeTableLines extends StatementAssistRegex {

	private static final String TOKEN = "DESCRIBE";
	private static final String MatchPattern = "DESCRIBE\\s*TABLE\\s*(\\w*)\\s*LINES\\s*(\\S*)";
	private static final String ReplacementPattern = "$2 = LINES( $1 )";

	@Override
	public String getAssistShortText() {
		return "Replace DESCRIBE TABLE with LINES";
	}

	@Override
	public String getAssistLongText() {
		return "Replace DESCRIBE TABLE x LINES y with y = LINES( x )";
	}

	@Override
	public boolean canAssist() {
		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(TOKEN) && AbapCodeReader.isKeyword(currentToken.offset)
					&& CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {
				return true;
			}
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
