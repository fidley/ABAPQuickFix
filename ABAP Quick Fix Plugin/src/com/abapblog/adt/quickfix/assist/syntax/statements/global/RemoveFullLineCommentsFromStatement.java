package com.abapblog.adt.quickfix.assist.syntax.statements.global;

import com.abapblog.adt.quickfix.assist.comments.AbapQuickFixRemoveCommentsCodeParser;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class RemoveFullLineCommentsFromStatement extends StatementAssist implements IAssistRegex {

	public RemoveFullLineCommentsFromStatement() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return AbapQuickFixRemoveCommentsCodeParser.FULL_LINE_COMMENT_PATERN;
	}

	@Override
	public String getReplacePattern() {
		return "";
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replaceAllPattern(getMatchPattern(), getReplacePattern());
	}

	@Override
	public String getAssistShortText() {
		return "Remove full line comment from statement";
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
