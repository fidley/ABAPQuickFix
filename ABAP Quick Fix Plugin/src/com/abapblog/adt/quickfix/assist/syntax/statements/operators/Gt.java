package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Gt extends Operators {

	public Gt(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "GT";
		replacement = "> ";
	}

}
