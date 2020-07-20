package com.abapblog.adt.quickfix.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import com.abapblog.adt.quickfix.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_COMMENT_SIGNS, false);
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_LINE_BREAKS, false);
		store.setDefault(PreferenceConstants.P_TCTE_ALLOWED, true);
		store.setDefault(PreferenceConstants.P_TCTE_ALLOW_REMOVE_ALL_COMMENTS, false);
	}



}
