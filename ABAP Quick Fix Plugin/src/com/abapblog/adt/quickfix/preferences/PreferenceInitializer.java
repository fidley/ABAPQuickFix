package com.abapblog.adt.quickfix.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.quickfix.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_COMMENT_SIGNS, false);
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_LINE_BREAKS, false);
		store.setDefault(PreferenceConstants.P_TCTE_ALLOWED, true);
		store.setDefault(PreferenceConstants.HideRemoveAllComments, false);
	}

}
