package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.swt.graphics.Image;

public interface IAssist {

	public String getChangedCode();

	public String getAssistShortText();

	public String getAssistLongText();

	public Image getAssistIcon();

	public boolean canAssist();

	public int getStartOfReplace();

	public int getReplaceLength();

}
