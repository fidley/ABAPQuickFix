package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import java.util.ArrayList;
import java.util.Iterator;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Data extends CombineAbstract implements IAssistRegex {

	private String InlineDeclarationAtBeginning = "(?s)\\s*data\\(\\w*\\)\\s+ =";
	private String InlineDeclaration = "(?s)data\\s*:*\\s+(.*) =";

	public Data() {
		super();
		BeginningOfStatement = "DATA: ";
		MatchPattern = "(?s)\\s*data\\s*:*\\s+(.*)";
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
					ChangedCode = BeginningOfStatement + ChangedCode;
				} else {
					if (!ChangedCode.endsWith(NewLineString)) {
						ChangedCode = ChangedCode + NewLineString;
					}

					ChangedCode = ChangedCode + statement.replacePattern(getMatchPattern(), getReplacePattern())
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
		return "Combine DATA statements";
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

	private boolean checkMatch(AbapStatement statement) {
		if (statement.matchPattern(getMatchPattern()) && !statement.matchPattern(InlineDeclaration)
				&& !statement.matchPattern(InlineDeclarationAtBeginning)) {
			return true;
		}
		return false;
	}

	private boolean canAssistWithPrevious() {
		if (CodeReader.PreviousStatement == null)
			return false;
		if (checkMatch(CodeReader.CurrentStatement) && checkMatch(CodeReader.PreviousStatement))
			assistWithPrevious = true;
		return assistWithPrevious;
	}

	private boolean canAssistWithNext() {
		if (CodeReader.NextStatement == null)
			return false;
		if (checkMatch(CodeReader.CurrentStatement) && checkMatch(CodeReader.NextStatement))
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

	@Override
	public void checkPreviousStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		try {
			AbapStatement previousStatement = statement.getPreviousAbapStatement();
			if (checkMatch(previousStatement)) {
				if (previousStatement.isFullLineComment() == false)
					matchedStatements.add(0, previousStatement);
				checkPreviousStatements(previousStatement);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void checkNextStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();
		try {
			AbapStatement nextStatement = statement.getNextAbapStatement();
			if (checkMatch(nextStatement)) {
				if (nextStatement.isFullLineComment() == false)
					matchedStatements.add(nextStatement);
				checkNextStatements(nextStatement);
			}
		} catch (Exception e) {

		}
	}

}
