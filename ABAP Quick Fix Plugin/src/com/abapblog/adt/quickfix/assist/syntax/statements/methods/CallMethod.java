package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class CallMethod extends StatementAssist implements IAssistRegex {

	private static final String callMethodPattern = "(?s)(call)\\s+.*(method)\\s+(\\w+(->)*(=>)*\\w+)(.*)";
	private static final String replaceCallMethodPattern = "$3( $6 )";

	public CallMethod() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return callMethodPattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceCallMethodPattern;
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Replace CALL METHOD with direct call";
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
