package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public abstract class Operators extends StatementAssist {

	protected String operatorName;
	protected String replacement;

	public Operators() {
		super();
	}

	@Override
	public String getChangedCode() {
		String code = CodeReader.sourcePage.getSource();
		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(operatorName)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
				code = code.substring(0, currentToken.offset) + replacement + code.substring(currentToken.offset + 2);
			}
		}
		return StringCleaner.clean(code.substring(CodeReader.CurrentStatement.getBeginOfStatementReplacement(),
				CodeReader.CurrentStatement.getEndOfStatement()));

	}

	@Override
	public String getAssistShortText() {
		return "Replace " + operatorName + " with " + replacement;
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public boolean canAssist() {

		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(operatorName)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
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

}
