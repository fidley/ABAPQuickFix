package com.abapblog.adt.quickfix.assist.syntax.statements.readTable;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class ReadTableIndexTransportingNoFields extends StatementAssistRegex implements IAssistRegex {

	private static final String NOT = "NOT";
	private static final String CODE = "Code";

	public ReadTableIndexTransportingNoFields(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getMatchPattern() {
		return IReadTablePatterns.readTableIndexTransportingNoFields;
	}

	@Override
	public String getReplacePattern() {
		return IReadTablePatterns.replaceReadTableIndexTransportingNoFields;
	}

	@Override
	public String getAssistShortText() {
		return "Replace READ TABLE with LINE_EXISTS( )";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Image getAssistIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		try {
			if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
					&& (CodeReader.NextStatement.matchPattern(IReadTablePatterns.ifSySubrcEq0)
							|| CodeReader.NextStatement.matchPattern(IReadTablePatterns.ifSySubrcNe0))) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.NextStatement.getEndOfStatement() - CodeReader.CurrentStatement.getBeginOfStatement() + 1;
	}

	@Override
	public String getChangedCode() {
		String replacePattern = IReadTablePatterns.replaceIfSySubrc;
		if (CodeReader.NextStatement.matchPattern(IReadTablePatterns.ifSySubrcEq0)) {
			replacePattern = replacePattern.replace(CODE, StringCleaner
					.clean(CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern()), true));
			return StringCleaner
					.clean(CodeReader.NextStatement.replacePattern(IReadTablePatterns.ifSySubrcEq0, replacePattern));
		} else {
			replacePattern = replacePattern.replace(CODE, NOT + StringCleaner
					.clean(CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern()), true));
			return StringCleaner
					.clean(CodeReader.NextStatement.replacePattern(IReadTablePatterns.ifSySubrcNe0, replacePattern));

		}

	}

}
