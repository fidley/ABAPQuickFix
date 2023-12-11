package com.abapblog.adt.quickfix.preferences;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;


public class OrderEditor extends ListEditor {

	public OrderEditor() {

	}

	public OrderEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		init(name, labelText);
		createControl(parent);
	}

	@Override
	protected String createList(String[] items) {
		StringBuilder orderString = new StringBuilder("");
		for (String item : items) {
			orderString.append(item);
			orderString.append(", ");
		}
		return orderString.toString();
	}

	@Override
	protected String getNewInputObject() {
		String keyword = null;
		PrefixDialog dialog = new PrefixDialog(getShell());
		dialog.create();

		if (dialog.open() == Window.OK) {
			System.out.println(dialog.getFilter());
			keyword = dialog.getFilter();
		}
		return keyword;
	}

	@Override
	protected String[] parseString(String str) {

		return str.split(", ");
	}

}
