package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;

public abstract class StatementAssistRegex extends StatementAssist implements IAssistRegex {

	protected AbapCodeReader CodeReader;

	public StatementAssistRegex(IQuickAssistInvocationContext context) {
		super(context);
		CodeReader = AbapCodeReader.getInstance(context);
	}

}
