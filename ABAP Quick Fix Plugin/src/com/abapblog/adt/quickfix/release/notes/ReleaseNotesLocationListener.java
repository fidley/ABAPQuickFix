package com.abapblog.adt.quickfix.release.notes;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

public class ReleaseNotesLocationListener extends LocationAdapter {
	private Browser browser;

	public ReleaseNotesLocationListener(Browser browser) {
		this.browser = browser;
	}

	@Override
	public void changing(LocationEvent event) {
		try {
			if (event.location.startsWith("http:") || event.location.startsWith("https:")) {
				IWebBrowser webbrowser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
				URL url = new URL(event.location);
				webbrowser.openURL(url);
				event.doit = false;
			} else if (event.location.startsWith("file:")) {
				URL url = new URL(event.location);
				Pattern pattern = Pattern.compile(".*/RELEASENOTESHTML.*(/html/.*.html).*");
				Matcher matcher = pattern.matcher(url.getPath());
				if (matcher.find()) {
					String path = matcher.group(1);
					url = FileLocator.find(Platform.getBundle(Startup.RELEASE_NOTES_PLUGIN), new Path(path), null);
					try {
						url = FileLocator.toFileURL(url);
						event.doit = false;
						browser.setUrl(url.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}

					event.doit = false;
				}
			}
		} catch (Exception exception) {
		}
	}
}
