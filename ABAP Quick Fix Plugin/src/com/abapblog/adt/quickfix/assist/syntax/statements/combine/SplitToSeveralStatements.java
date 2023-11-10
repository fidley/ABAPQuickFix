package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class SplitToSeveralStatements extends StatementAssist implements IAssistRegex {

	private static final String BeginningOfStatement = "\r\n";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";
	private String multipleEmptyLines = "(\\r?\\n?(\\r?\\n))+";
	private String MatchPattern = "(?s)(.*?):\\s+(\\r?\\n)*(.*\\s*,.*)";
	private String ReplacePattern = "$3";
	private String MatchedStatement;

	public SplitToSeveralStatements() {
		super();
	}

	@Override
	public String getMatchPattern() {
		return MatchPattern;
	}

	@Override
	public String getReplacePattern() {
		return ReplacePattern;
	}

	@Override
	public String getChangedCode() {

		String ChangedCode = "";
		String CodeToSplit = CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 3);
		String[] SplittedCode = CodeToSplit.split("\r\n");
		for (int i = 0; i < SplittedCode.length; i++) {
			String codeLine = SplittedCode[i].replaceAll(multipleEmptyLines, NewLineString)
					.replaceFirst(NewLinePatternWithSpaces, "").replaceFirst(",", ".");
			if (codeLine.trim().equals("")) {
				// skip empty rows
				continue;
			}
			if (codeLine.startsWith("  ") || codeLine.startsWith("\t\t"))
				codeLine = codeLine.replaceFirst("[ \t]{2,}", "");
			ChangedCode = ChangedCode + BeginningOfStatement + MatchedStatement + " " + codeLine;

		}
		ChangedCode = ChangedCode.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces,
				"");
		return BeginningOfStatement + ChangedCode.substring(0, ChangedCode.length());
	}

	@Override
	public String getAssistShortText() {
		return "Split " + MatchedStatement.toUpperCase() + " statements";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		if (!CodeReader.CurrentStatement.isFullLineComment())

			if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {
				MatchedStatement = CodeReader.CurrentStatement.getMatchGroup(MatchPattern, 1)
						.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces, "");
				return true;
			}
		;

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
