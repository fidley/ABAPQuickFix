package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Refresh extends CombineAbstract implements IAssistRegex {

	public Refresh() {
		super();
		BeginningOfStatement = "REFRESH: ";
		MatchPattern = "(?s)\\s*refresh\\s*:*\\s+(.*)";
	}

}
