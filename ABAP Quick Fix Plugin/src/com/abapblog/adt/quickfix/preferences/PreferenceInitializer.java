package com.abapblog.adt.quickfix.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.quickfix.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final List<String> ORDER_PREFIX = Arrays.asList("GO", "GT", "GS", "GV", "LO", "LT", "LS", "LV");

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_COMMENT_SIGNS, false);
		store.setDefault(PreferenceConstants.P_TCTE_REMOVE_LINE_BREAKS, false);
		store.setDefault(PreferenceConstants.P_TCTE_ALLOWED, true);
		store.setDefault(PreferenceConstants.HideRemoveAllComments, false);
		store.setDefault(PreferenceConstants.ADDITIONAL_EMPTY_ROW_DECL_PREFIX, true);
		store.setDefault(PreferenceConstants.ADDITIONAL_EMPTY_ROW_DECL_TYPE, true);
		store.setDefault(PreferenceConstants.ORDER_PREFIX_DECL, buildString(ORDER_PREFIX));
	}

	private String buildString(List<String> list) {
		String string = "";
		for (String prefix : ORDER_PREFIX) {
			string = string + prefix + ", ";
		}
		// replace last comma
		if (string.endsWith(", ")) {
			string = string.substring(0, string.length() - 2);
		}

		return string;
	}

}
