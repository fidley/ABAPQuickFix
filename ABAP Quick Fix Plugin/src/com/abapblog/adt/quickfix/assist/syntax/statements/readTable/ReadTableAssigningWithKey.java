package com.abapblog.adt.quickfix.assist.syntax.statements.readTable;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class ReadTableAssigningWithKey extends StatementAssist implements IAssistRegex {

	public ReadTableAssigningWithKey() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return IReadTablePatterns.readTableAssigningWithKey;
	}

	@Override
	public String getReplacePattern() {
		return IReadTablePatterns.replaceReadTableAssigningWithKey;
	}

	@Override
	public String getAssistShortText() {
		return "Replace READ TABLE with ASSIGN";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return "";
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
		return CodeReader.CurrentStatement.getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

}
