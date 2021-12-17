package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.utility.QuickFixIcon;

public abstract class StatementAssist implements IAssist {
	protected AbapCodeReader CodeReader;

	public StatementAssist() {
		CodeReader = AbapCodeReader.getInstance();
	}

	@Override
	public Image getAssistIcon() {
		return QuickFixIcon.get();
	}

}
