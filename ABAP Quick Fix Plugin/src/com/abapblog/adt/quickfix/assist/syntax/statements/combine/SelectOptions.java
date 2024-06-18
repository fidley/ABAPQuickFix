package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class SelectOptions extends CombineAbstract implements IAssistRegex {

	public SelectOptions() {
		super();
		BeginningOfStatement = "SELECT-OPTIONS: ";
		MatchPattern = "(?s)\\s*select-options\\s*:*\\s+(.*)";
	}

}
