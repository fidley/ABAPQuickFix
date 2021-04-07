package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Ge extends Operators {

	public Ge(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "GE";
		replacement = ">=";
	}

}
