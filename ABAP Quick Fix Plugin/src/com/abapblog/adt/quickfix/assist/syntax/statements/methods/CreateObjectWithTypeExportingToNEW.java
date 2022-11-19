package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class CreateObjectWithTypeExportingToNEW extends StatementAssist implements IAssistRegex {

	private String createObjectExportingPattern = "(?s)create\\s+object\\s+(.+)\\s+(type)\\s+(.*)\\s+(exporting)\\s+(.*)";
	private String replaceCreateObjectExportingPattern = "$1 = NEW $3( $5 )";

	public CreateObjectWithTypeExportingToNEW() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return createObjectExportingPattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceCreateObjectExportingPattern;
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Replace CREATE OBJECT with NEW";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
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

}
