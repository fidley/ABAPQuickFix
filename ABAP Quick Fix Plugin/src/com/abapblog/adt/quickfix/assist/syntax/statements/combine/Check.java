package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Check extends CombineAbstract implements IAssistRegex {

	public Check() {
		super();
		BeginningOfStatement = "CHECK: ";
		MatchPattern = "(?s)\\s*check\\s*:*\\s+(.*)";
	}

}
