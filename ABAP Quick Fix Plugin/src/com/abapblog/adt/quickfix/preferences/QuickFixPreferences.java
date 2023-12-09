package com.abapblog.adt.quickfix.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.abapblog.adt.quickfix.Activator;

public class QuickFixPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private IPreferenceStore store;

	public QuickFixPreferences() {
		super(GRID);
		store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Settings for ABAP QuickFix Plugin");
	}

	@Override
	public void createFieldEditors() {
		Composite top = new Composite(getFieldEditorParent(), SWT.LEFT);

		// Sets the layout data for the top composite's
		// place in its parent's layout.
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Sets the layout for the top composite's
		// children to populate.
		top.setLayout(new GridLayout());

		Group tcte_group = new Group(top, SWT.SHADOW_ETCHED_IN);
		tcte_group.setText("Translate comment into English settings");
		addField(new BooleanFieldEditor(PreferenceConstants.P_TCTE_ALLOWED,
				"&Translate comment into English QuickFix allowed", tcte_group));
		addField(new BooleanFieldEditor(PreferenceConstants.P_TCTE_REMOVE_COMMENT_SIGNS,
				"&Remove comment sings \" and * from translated text", tcte_group));
		addField(new BooleanFieldEditor(PreferenceConstants.P_TCTE_REMOVE_LINE_BREAKS,
				"&Remove line breaks from translated text", tcte_group));
		addField(new BooleanFieldEditor(PreferenceConstants.HideRemoveAllComments,
				"&Hide Remove All Comments Quick Fix", getFieldEditorParent()));
		addField(new OrderEditor(PreferenceConstants.ORDER_PREFIX_DECL,
				"Order of prefixes (Sort DATA statements)", getFieldEditorParent()));
		Composite bot = new Composite(getFieldEditorParent(), SWT.LEFT);
		bot.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bot.setLayout(new GridLayout());
		Group tcte_sort_group = new Group(bot, SWT.SHADOW_ETCHED_IN);
		tcte_sort_group.setText("Additional empty row between each");
		addField(new BooleanFieldEditor(PreferenceConstants.ADDITIONAL_EMPTY_ROW_DECL_PREFIX,
				"prefix in combined DATA statement", tcte_sort_group));
		addField(new BooleanFieldEditor(PreferenceConstants.ADDITIONAL_EMPTY_ROW_DECL_TYPE,
				"DATA statement (Sort all DATA statements)", tcte_sort_group));

	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void performApply() {
		super.performApply();
	}

//Apply&Close
	@Override
	public boolean performOk() {

		Boolean ApplyClose = super.performOk();
		return ApplyClose;
	}

}