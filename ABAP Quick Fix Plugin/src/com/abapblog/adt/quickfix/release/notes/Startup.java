package com.abapblog.adt.quickfix.release.notes;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.FrameworkUtil;

public class Startup implements IStartup {

	public static final String RELEASE_NOTES_PLUGIN = "com.abapblog.adt.quickfix.release.notes.content";
	private static final String RELEASE_NOTES_PATH = "/html/changelog/latestReleaseNotes.html";
	private static final String RELEASE_NOTES_ID = "adt.quickfix.releaseNotesId";

	@Override
	public void earlyStartup() {

		IEclipsePreferences preferences = getEclipsePreferences();
		String oldReleaseNotesId = preferences.get(RELEASE_NOTES_ID, null);

		if (!Objects.equals(getLatestReleaseNoteId(), oldReleaseNotesId)) {
			preferences.put(RELEASE_NOTES_ID, getLatestReleaseNoteId());
			try {
				preferences.flush();
			} catch (Exception e) {

			}
			openReleaseNotesAsync();
		}
	}

	private IEclipsePreferences getEclipsePreferences() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE
				.getNode(FrameworkUtil.getBundle(Startup.class).getSymbolicName());
		return preferences;
	}

	private String getLatestReleaseNoteId() {
		return "1.1.5";
	}

	private void openReleaseNotesAsync() {
		UIJob uiJob = new UIJob("Open ADT Quick Fix release notes") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					Startup.this.openReleaseNotes();
				} catch (Exception e) {

				}
				return Status.OK_STATUS;
			}
		};
		uiJob.setSystem(true);
		uiJob.schedule();
	}

	private void openReleaseNotes() throws IOException, PartInitException {
		String html = RELEASE_NOTES_PATH;
		URL url = FileLocator.find(Platform.getBundle(RELEASE_NOTES_PLUGIN), new Path(html), null);
		try {
			url = FileLocator.toFileURL(url);
		} catch (Exception e) {
			// TODO: handle exception
		}
		EditorInput editorInput = new EditorInput(url);
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IDE.openEditor(activePage, editorInput, "com.abapblog.adt.quickfix.release.notes.editor", true);
	}
}
