package com.abapblog.adt.quickfix.preferences;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class PrefixDialog extends TitleAreaDialog {

	private String prefix;
	private Text prefixValue;

	public PrefixDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Add prefix");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);
		createPrefix(container);
		return area;
	}

	private void createPrefix(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Add prefix:");
		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;
		prefixValue = new Text(container, SWT.BORDER);
		prefixValue.setLayoutData(dataFirstName);
	}

	public String getFilter() {
		return prefix;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private void saveInput() {
		prefix = prefixValue.getText().toUpperCase();
	}

}
