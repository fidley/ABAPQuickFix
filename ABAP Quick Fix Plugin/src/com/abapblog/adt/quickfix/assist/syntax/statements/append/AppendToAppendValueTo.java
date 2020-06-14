package com.abapblog.adt.quickfix.assist.syntax.statements.append;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class AppendToAppendValueTo extends StatementAssist implements IAssistRegex {

	private static final String BeginningOfStatement = "\r\n\tAPPEND VALUE #(  ";
	private String EndOfStatement = " ) TO internalTable";
	private static final String NewLineWithTabAndSpaceString = "\r\n\t  ";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";
	private String MatchPattern = "(?s)append\\s*(.*)to\\s*(.*)";
	private String MatchDirectAssignmentPattern = "(?s)structureName-(\\w*)\\s+=\\s+(.*)";
	private String ReplacementPattern = "$1 = $2";

	private boolean assistWithPrevious;
	private List<AbapStatement> matchedStatements;

	public AppendToAppendValueTo(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getMatchPattern() {
		return MatchPattern;
	}

	@Override
	public String getReplacePattern() {
		return ReplacementPattern;
	}

	@Override
	public String getChangedCode() {
		String multipleEmptyLines = "(\\r?\\n?(\\r?\\n))+";
		String ChangedCode = "";
		EndOfStatement = EndOfStatement.replaceAll("internalTable",
				CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 2));
		checkPreviousStatements(CodeReader.CurrentStatement);

		Iterator<AbapStatement> statementIterator = matchedStatements.iterator();
		while (statementIterator.hasNext()) {
			AbapStatement statement = statementIterator.next();
			ChangedCode = ChangedCode + NewLineWithTabAndSpaceString
					+ statement.replacePattern(MatchDirectAssignmentPattern, getReplacePattern())
							.replaceAll(multipleEmptyLines, NewLineString).replaceFirst(NewLinePatternWithSpaces, "");
		}

		return BeginningOfStatement + ChangedCode + EndOfStatement;
	}

	@Override
	public String getAssistShortText() {
		return "Change to APPEND VALUE #( ) TO";
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
		MatchDirectAssignmentPattern = MatchDirectAssignmentPattern
				.replaceAll("structureName", CodeReader.CurrentStatement.getMatchGroup(MatchPattern, 1))
				.replace(" ", "");
		return canAssistWithPrevious();
	}

	private boolean canAssistWithPrevious() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& CodeReader.PreviousStatement.matchPattern(MatchDirectAssignmentPattern))
			assistWithPrevious = true;
		return assistWithPrevious;
	}

	@Override
	public int getStartOfReplace() {
		return matchedStatements.get(0).getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getEndOfStatement() - getStartOfReplace() + 1;
	}

	public void checkPreviousStatements(AbapStatement statement) {
		if (matchedStatements == null)
			matchedStatements = new ArrayList<>();

		AbapStatement previousStatement = statement.getPreviousAbapStatement();
		if (previousStatement.matchPattern(MatchDirectAssignmentPattern)) {
			if (previousStatement.isFullLineComment() == false)
				matchedStatements.add(0, previousStatement);
			checkPreviousStatements(previousStatement);
		}

	}

}
