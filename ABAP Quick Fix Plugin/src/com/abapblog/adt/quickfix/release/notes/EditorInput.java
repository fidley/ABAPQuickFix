package com.abapblog.adt.quickfix.release.notes;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

public class EditorInput implements IEditorInput, IPersistableElement {

	private final URL url;

	public EditorInput(URL url) {
		this.url = url;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TEST"; // getUrl().toExternalForm();
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void saveState(IMemento memento) {
		if (memento != null) {
			memento.putString("plugin", "com.abapblog.adt.quickfix");
			memento.putString("path", url.toExternalForm());
		}
	}

	@Override
	public String getFactoryId() {
		return EditorInputPersistant.getFactoryId();
	}

	public URL getUrl() {
		return url;
	}

}
