package com.abapblog.adt.quickfix.assist.utility;

import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.Activator;

public class QuickFixIcon {
	private static Image icon;

	public static Image get() {
		if (icon == null) {
			icon = Activator.getDefault().imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/qf16.png")
					.createImage();
		}
		return icon;
	}

}
