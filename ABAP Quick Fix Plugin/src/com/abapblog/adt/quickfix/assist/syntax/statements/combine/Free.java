package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class Free extends StatementAssistRegex implements IAssistRegex {

	private static final String BeginningOfStatement = "\r\n\tFREE: ";
	private static final String NewLineWithTabAndSpaceString = "\r\n\t  ";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePattern = "\\r\\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";
	private String MatchPattern = "(?s)\s+free\\s*:*\\s+(.*)";
	private String ReplacePattern = "$1";
	private boolean assistWithNext;
	private boolean assistWithPrevious;
	private List<AbapStatement> matchedStatements;

	public Free(IQuickAssistInvocationContext context) {
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
		String multipleEmptyLines = "(\\r?\\n?(\\r?\\n))+";
		String ChangedCode = "";
		if (assistWithPrevious == true || assistWithNext == true) {

			checkPreviousStatements(CodeReader.CurrentStatement);
			matchedStatements.add(CodeReader.CurrentStatement);
			checkNextStatements(CodeReader.CurrentStatement);

			Iterator<AbapStatement> statementIterator = matchedStatements.iterator();
			while (statementIterator.hasNext()) {
				AbapStatement statement = statementIterator.next();
				if (statement == matchedStatements.get(0)) {
					ChangedCode = statement.replacePattern(getMatchPattern(), getReplacePattern())
							.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces, "");
					ChangedCode = BeginningOfStatement + ChangedCode;
				} else {
					ChangedCode = ChangedCode + statement.replacePattern(getMatchPattern(), getReplacePattern())
							.replaceAll(multipleEmptyLines, NewLineString)
							.replaceFirst(NewLinePattern, NewLineWithTabAndSpaceString);

				}
				if (statementIterator.hasNext())
					ChangedCode = ChangedCode + "," + NewLineString;
			}

		}

		return ChangedCode;
	}

	@Override
	public String getAssistShortText() {
		return "Combine FREE statements";
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
		if (CodeReader.CurrentStatement.isFullLineComment())
			return false;
		canAssistWithNext();
		canAssistWithPrevious();
		if (assistWithPrevious || assistWithNext) {
			return true;
		}
		return false;
	}

	private boolean canAssistWithPrevious() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& CodeReader.PreviousStatement.matchPattern(getMatchPattern()))
			assistWithPrevious = true;
		return assistWithPrevious;
	}

	private boolean canAssistWithNext() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& CodeReader.NextStatement.matchPattern(getMatchPattern()))
			assistWithNext = true;
		return assistWithNext;
	}

	@Override
	public int getStartOfReplace() {
		return matchedStatements.get(0).getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		int lastItem = matchedStatements.size() - 1;
		return matchedStatements.get(lastItem).getEndOfStatement() - getStartOfReplace() + 1;
	}

	public void checkPreviousStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		AbapStatement previousStatement = statement.getPreviousAbapStatement();
		if (previousStatement.matchPattern(MatchPattern)) {
			if (previousStatement.isFullLineComment() == false)
				matchedStatements.add(0, previousStatement);
			checkPreviousStatements(previousStatement);
		}

	}

	public void checkNextStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		AbapStatement nextStatement = statement.getNextAbapStatement();
		if (nextStatement.matchPattern(MatchPattern)) {
			if (nextStatement.isFullLineComment() == false)
				matchedStatements.add(nextStatement);
			checkNextStatements(nextStatement);
		}

	}

}
