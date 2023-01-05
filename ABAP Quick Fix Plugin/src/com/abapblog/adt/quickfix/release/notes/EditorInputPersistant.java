package com.abapblog.adt.quickfix.release.notes;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class EditorInputPersistant implements IElementFactory {

	@Override
	public IAdaptable createElement(IMemento memento) {
		if (memento == null) {
			return null;
		}
		String path = memento.getString("path");
		if (path == null || path.isEmpty()) {
			return null;
		}

		try {
			URL url = new URL(path);
			EditorInput editorInput = new EditorInput(url);
			return editorInput;
		} catch (Exception e) {

			return null;
		}
	}

	public static String getFactoryId() {
		return "com.abapblog.adt.quickfix.release.notes.EditorInputPersistant";
	}

}
