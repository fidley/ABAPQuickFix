package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Le extends Operators {

	public Le(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "LE";
		replacement = ">=";
	}

}
