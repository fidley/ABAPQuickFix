package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class FieldSymbols extends CombineAbstract implements IAssistRegex {

	public FieldSymbols() {
		super();
		BeginningOfStatement = "FIELD-SYMBOLS: ";
		MatchPattern = "(?s)\\s*field-symbols\\s*:*\\s+(.*)";
	}

}
