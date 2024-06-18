package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Constants extends CombineAbstract implements IAssistRegex {

	public Constants() {
		super();
		BeginningOfStatement = "CONSTANTS: ";
		MatchPattern = "(?s)\\s*constants\\s*:*\\s+(.*)";
	}

}
