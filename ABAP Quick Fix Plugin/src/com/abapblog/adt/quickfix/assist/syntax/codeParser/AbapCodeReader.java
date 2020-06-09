package com.abapblog.adt.quickfix.assist.syntax.codeParser;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class AbapCodeReader {

	private String Code;
	public AbapStatement CurrentStatement;
	public AbapStatement NextStatement;
	public AbapStatement PreviousStatement;
	private static AbapCodeReader CodeReader = null;
	private static IQuickAssistInvocationContext Context = null;

	private AbapCodeReader(IQuickAssistInvocationContext context) {
		Code = context.getSourceViewer().getDocument().get();
		CurrentStatement = createStatement(context.getOffset());
		NextStatement = createStatement(CurrentStatement.getEndOfStatement() + 1);
		PreviousStatement = createStatement(CurrentStatement.getBeginOfStatement() - 1);
	}

	private AbapStatement createStatement(int offset) {
		return new AbapStatement(Code, offset);
	}

	public static AbapCodeReader getInstance(IQuickAssistInvocationContext context) {
		if (CodeReader == null) {
			CodeReader = new AbapCodeReader(context);
			Context = context;
		}
		if (Context != context) {
			CodeReader = new AbapCodeReader(context);
			Context = context;
		}

		return CodeReader;
	}
}
