package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public abstract class CombineAbstract extends StatementAssist implements IAssistRegex {

	protected String BeginningOfStatement = "";
	protected static final String NewLineWithTabAndSpaceString = "\r\n\t  ";
	protected static final String NewLineString = "\r\n";
	protected static final String NewLinePattern = "\\r\\n";
	protected static final String NewLinePatternWithSpaces = "\\r\\n\\s*";
	protected String MatchPattern = "";
	protected String ReplacePattern = "$1";
	protected boolean assistWithNext;
	protected boolean assistWithPrevious;
	protected List<AbapStatement> matchedStatements;

	public CombineAbstract() {
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
							.replaceAll(multipleEmptyLines, NewLineString);
					ChangedCode = statement.getLeadingCharacters() + BeginningOfStatement + ChangedCode;
				} else {
					if (!ChangedCode.endsWith(NewLineString)) {
						ChangedCode = ChangedCode + NewLineString;
					}
					ChangedCode = ChangedCode + statement.getLeadingCharacters()
							+ statement.replacePattern(getMatchPattern(), getReplacePattern())
									.replaceAll(multipleEmptyLines, NewLineString);

				}
				if (statementIterator.hasNext()) {
					ChangedCode = ChangedCode + "," + statement.getInlineComment();
				} else {
					ChangedCode = ChangedCode + "." + statement.getInlineComment();
				}
			}

		}

		return StringCleaner.clean(ChangedCode);
	}

	@Override
	public String getAssistShortText() {
		return "Combine " + BeginningOfStatement + " statements";
	}

	@Override
	public String getAssistLongText() {
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
		if (CodeReader.PreviousStatement == null)
			return false;
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& CodeReader.PreviousStatement.matchPattern(getMatchPattern()))
			assistWithPrevious = true;
		return assistWithPrevious;
	}

	private boolean canAssistWithNext() {
		if (CodeReader.NextStatement == null)
			return false;
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& CodeReader.NextStatement.matchPattern(getMatchPattern()))
			assistWithNext = true;
		return assistWithNext;
	}

	@Override
	public int getStartOfReplace() {
		return matchedStatements.get(0).getBeginOfStatementReplacement();
	}

	@Override
	public int getReplaceLength() {
		int lastItem = matchedStatements.size() - 1;
		return matchedStatements.get(lastItem).getEndOfStatement() - getStartOfReplace() + 1;
	}

	public void checkPreviousStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		try {
			AbapStatement previousStatement = statement.getPreviousAbapStatement();
			if (previousStatement.matchPattern(MatchPattern)) {
				if (previousStatement.isFullLineComment() == false)
					matchedStatements.add(0, previousStatement);
				checkPreviousStatements(previousStatement);
			}
		} catch (Exception e) {

		}
	}

	public void checkNextStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		try {
			AbapStatement nextStatement = statement.getNextAbapStatement();
			if (nextStatement.matchPattern(MatchPattern)) {
				if (nextStatement.isFullLineComment() == false)
					matchedStatements.add(nextStatement);
				checkNextStatements(nextStatement);
			}
		} catch (Exception e) {

		}
	}

}
