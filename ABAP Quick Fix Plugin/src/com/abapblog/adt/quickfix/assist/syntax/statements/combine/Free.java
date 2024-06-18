package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class Free extends CombineAbstract implements IAssistRegex {

	public Free() {
		super();
		BeginningOfStatement = "FREE: ";
		MatchPattern = "(?s)\\s*free\\s*:*\\s+(.*)";

	}

}
