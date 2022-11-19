package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class CreateObjectWithTypeToNEW extends StatementAssist implements IAssistRegex {
	private String createObjectPattern = "(?s)create\\s+object\\s+(.+)TYPE\\s(.+)";
	private String replaceCreateObjectPattern = "$1 = NEW $2(  )";

	public CreateObjectWithTypeToNEW() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return createObjectPattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceCreateObjectPattern;
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
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern()) && !new CreateObjectExportingToNEW().canAssist()
				&& !new CreateObjectWithTypeExportingToNEW().canAssist()) {
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
