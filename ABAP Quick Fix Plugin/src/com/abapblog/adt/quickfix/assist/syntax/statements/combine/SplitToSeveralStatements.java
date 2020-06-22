package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class SplitToSeveralStatements extends StatementAssist implements IAssistRegex {

	private static final String BeginningOfStatement = "\r\n";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";
	private String multipleEmptyLines = "(\\r?\\n?(\\r?\\n))+";
	private String MatchPattern = "(?s)(.*):\\s+(\\r?\\n)*(.*\\s*,.*)";
	private String ReplacePattern = "$3";
	private String MatchedStatement;

	public SplitToSeveralStatements(IQuickAssistInvocationContext context) {
		super(context);
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
		String CodeToSplit = CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
		String[] SplittedCode = CodeToSplit.split(",");
		for (int i = 0; i < SplittedCode.length; i++)
			ChangedCode = ChangedCode
					+ BeginningOfStatement + MatchedStatement + " " + SplittedCode[i]
							.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces, "")
					+ ".";

		ChangedCode = ChangedCode.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces,
				"");
		return BeginningOfStatement + ChangedCode.substring(0, ChangedCode.length() - 1);
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
	public Image getAssistIcon() {
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
