package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Parameters extends CombineAbstract implements IAssistRegex {

	public Parameters() {
		super();
		BeginningOfStatement = "PARAMETERS: ";
		MatchPattern = "(?s)\\s*parameters\\s*:*\\s+(.*)";
	}

}
