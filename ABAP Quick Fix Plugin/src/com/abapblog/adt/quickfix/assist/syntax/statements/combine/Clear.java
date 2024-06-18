package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Clear extends CombineAbstract implements IAssistRegex {

	public Clear() {
		super();
		BeginningOfStatement = "CLEAR: ";
		MatchPattern = "(?s)\\s*clear\\s*:*\\s+(.*)";
	}

}
