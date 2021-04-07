package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class MethodOmitSelfReference extends StatementAssistRegex implements IAssistRegex {

	private static final String omitSelfReferencePattern = "me(->)(\\w*)"; // me(->)(\\S+)\\((.*)";
	private static final String replaceOmitSelfReferencePattern = "$2";

	public MethodOmitSelfReference(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getMatchPattern() {
		return omitSelfReferencePattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceOmitSelfReferencePattern;
	}

	@Override
	public String getChangedCode() {
		String Code = CodeReader.CurrentStatement.replaceAllPattern(getMatchPattern(), getReplacePattern());
		return StringCleaner.clean(Code);
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Omit self reference ME->";
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
