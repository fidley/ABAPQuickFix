package com.abapblog.adt.quickfix.assist.syntax.statements.reference;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;

public class GetReferenceToRef extends StatementAssist implements IAssistRegex {

	private static final String movePattern = "(?s)\\s*get\\s+reference\\s+of\\s+(.*)\\s+into\\s+(.*)";
	private static final String replaceMovePattern = "\r\n$2 = REF #( $1 )";

	public GetReferenceToRef(IQuickAssistInvocationContext context) {
		super(context);
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
		return "Change to REF #( )";
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
