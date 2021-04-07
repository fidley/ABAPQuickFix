package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Lt extends Operators {

	public Lt(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "LT";
		replacement = "< ";
	}

}
