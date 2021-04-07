package com.abapblog.adt.quickfix.assist.syntax.statements.readTable;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class ReadTableWithKeyInto extends StatementAssistRegex implements IAssistRegex {

	public ReadTableWithKeyInto(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getMatchPattern() {
		return IReadTablePatterns.readTableWithKeyInto;
	}

	@Override
	public String getReplacePattern() {
		return IReadTablePatterns.replaceReadTableWithKeyInto;
	}

	@Override
	public String getAssistShortText() {
		return "Replace READ TABLE with Table Expression";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Image getAssistIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& !(new ReadTableWithKeyReferenceInto(context).canAssist())) {
			return true;
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

	@Override
	public String getChangedCode() {
		return CodeReader.CurrentStatement.replacePattern(getMatchPattern(), getReplacePattern());
	}

}
