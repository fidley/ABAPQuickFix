package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class MethodOmitReceiving extends StatementAssist implements IAssistRegex {

	private static final String omitReceivingPattern = "(?s)(.*(->|=>).*)RECEIVING.*=\\s+(\\r?\\n)*(.*)\\)";
	private static final String replaceOmitReceivingPattern = "\r\n$4 = $1 )";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";

	public MethodOmitReceiving() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return omitReceivingPattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceOmitReceivingPattern;
	}

	@Override
	public String getChangedCode() {
		String Code = "\r\n"
				+ CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 4)
						.replaceFirst(NewLinePatternWithSpaces, "").replaceFirst(NewLineString, "")
				+ " = " + CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 1)
						.replaceFirst(NewLinePatternWithSpaces, "").replaceFirst(NewLineString, "")
				+ ")";
		return StringCleaner.clean(Code);
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Omit RECEIVING Statement";
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
