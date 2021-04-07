package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;

public class Ne extends Operators {

	public Ne(IQuickAssistInvocationContext context) {
		super(context);
		operatorName = "NE";
		replacement = "<>";
	}

}
