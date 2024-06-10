package com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class ReadTableReferenceIntoIndex extends StatementAssist implements IAssistRegex {

	public ReadTableReferenceIntoIndex() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return IReadTablePatterns.readTableRefereceIndex;
	}

	@Override
	public String getReplacePattern() {
		return IReadTablePatterns.replaceReadTableRefereceIndex;
	}

	@Override
	public String getAssistShortText() {
		return "Replace READ TABLE with REF #( )";
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
