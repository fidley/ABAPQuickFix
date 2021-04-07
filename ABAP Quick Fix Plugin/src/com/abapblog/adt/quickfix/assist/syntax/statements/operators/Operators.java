package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistAdt;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public abstract class Operators extends StatementAssistAdt {

	protected String operatorName;
	protected String replacement;

	public Operators(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getChangedCode() {
		String code = sourcePage.getSource();
		for (int i = 0; i < statementTokens.size(); i++) {
			Token currentToken = statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(operatorName)
					&& scannerServices.isKeyword(sourcePage, currentToken.offset + 1, true)) {
				code = code.substring(0, currentToken.offset) + replacement + code.substring(currentToken.offset + 2);
			}
		}
		return StringCleaner.clean(code.substring(beginning, end));

	}

	@Override
	public String getAssistShortText() {
		return "Replace " + operatorName + " with " + replacement;
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public Image getAssistIcon() {
		return null;
	}

	@Override
	public boolean canAssist() {

		for (int i = 0; i < statementTokens.size(); i++) {
			Token currentToken = statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(operatorName)
					&& scannerServices.isKeyword(sourcePage, currentToken.offset + 1, true)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return beginning;
	}

	@Override
	public int getReplaceLength() {
		return end - beginning;
	}

}
