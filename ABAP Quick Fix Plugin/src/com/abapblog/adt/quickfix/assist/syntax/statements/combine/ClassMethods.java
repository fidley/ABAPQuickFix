package com.abapblog.adt.quickfix.assist.syntax.statements.combine;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class ClassMethods extends CombineAbstract implements IAssistRegex {

	public ClassMethods() {
		super();
		BeginningOfStatement = "CLASS-METHODS: ";
		MatchPattern = "(?s)\\s*class-methods\\s*:*\\s+(.*)";
	}

}
