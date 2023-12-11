package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.utility.QuickFixIcon;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public abstract class StatementAssist implements IAssist {
	private static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	protected AbapCodeReader CodeReader;

	public StatementAssist() {
		CodeReader = AbapCodeReader.getInstance();
	}

	@Override
	public Image getAssistIcon() {
		return QuickFixIcon.get();
	}

	@Override
	public Boolean getCallPrettyPrintOnBlock() {
		return store.getBoolean(PreferenceConstants.CALL_PRETTY_PRINTER_AFTER_QF);
	}

}
