package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Methods extends CombineAbstract implements IAssistRegex {

	public Methods() {
		super();
		BeginningOfStatement = "METHODS: ";
		MatchPattern = "(?s)^\\s*METHODS\\s*:*\\s+(.*)";
	}

}
