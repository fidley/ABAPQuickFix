package com.abapblog.adt.quickfix.assist.comments;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class ReplaceFullLineWithInlineComment extends StatementAssistRegex implements IAssistRegex {

	private final String FIRST_CHAR_OF_FULLLINE_COMMENT = "(^\\*)";

	public ReplaceFullLineWithInlineComment(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replaceAllPattern(getMatchPattern(), getReplacePattern());
	}

	@Override
	public String getAssistShortText() {
		return "Replace full line comment with in line comment";
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public Image getAssistIcon() {
		return null;
	}

	@Override
	public boolean canAssist() {
		return CodeReader.CurrentStatement.matchPattern(getMatchPattern());
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

	@Override
	public String getMatchPattern() {
		return FIRST_CHAR_OF_FULLLINE_COMMENT;
	}

	@Override
	public String getReplacePattern() {
		return "\"";
	}

}
