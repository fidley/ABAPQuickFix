package com.abapblog.adt.quickfix.assist.syntax.statements.move;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class Move extends StatementAssist implements IAssistRegex {

	private static final String movePattern = "(?s)(move)\\s*:?\\s+(.*)\\s+(to)\\s+(.*)";
	private static final String replaceMovePattern = "$4 = $2";

	public Move() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return movePattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceMovePattern;
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Replace MOVE with direct assignment";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern()) && !(new MoveExact().canAssist())) {
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
