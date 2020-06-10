package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;

public class StatementAssist {

	protected AbapCodeReader CodeReader;
	protected IQuickAssistInvocationContext context;

	public StatementAssist(IQuickAssistInvocationContext context) {
		CodeReader = AbapCodeReader.getInstance(context);
		this.context = context;

	}

}
