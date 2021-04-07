package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public abstract class StatementAssist implements IAssist {

	protected IQuickAssistInvocationContext context;

	public StatementAssist(IQuickAssistInvocationContext context) {
		this.context = context;

	}

}
