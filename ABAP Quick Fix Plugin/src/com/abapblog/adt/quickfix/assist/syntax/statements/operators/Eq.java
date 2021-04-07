package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Eq extends Operators {

	public Eq(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "EQ";
		replacement = "= ";
	}

}
