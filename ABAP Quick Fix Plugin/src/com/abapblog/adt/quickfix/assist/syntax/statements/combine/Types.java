package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Types extends CombineAbstract implements IAssistRegex {

	public Types() {
		super();
		BeginningOfStatement = "TYPES: ";
		MatchPattern = "(?s)\\s*types\\s*:*\\s+(.*)";
	}

}
