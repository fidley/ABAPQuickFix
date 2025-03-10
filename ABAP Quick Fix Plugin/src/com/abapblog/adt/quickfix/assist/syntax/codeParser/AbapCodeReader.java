package com.abapblog.adt.quickfix.assist.syntax.codeParser;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.tools.abapsource.ui.AbapSourceUi;
import com.sap.adt.tools.abapsource.ui.IAbapSourceUi;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices;
import com.sap.adt.tools.abapsource.ui.sources.editors.AbapSourcePage;

public class AbapCodeReader {
	public static IDocument document = null;
	public static IAbapSourceUi sourceUi = null;
	public static IAbapSourceScannerServices scannerServices = null;
	public static AbapSourcePage sourcePage;
	private static String Code;
	public AbapStatement CurrentStatement;
	public AbapStatement NextStatement;
	public AbapStatement PreviousStatement;
	private static AbapCodeReader CodeReader = null;
	private static IQuickAssistInvocationContext Context = null;

	private AbapCodeReader(IQuickAssistInvocationContext context) {
		Context = context;
		document = context.getSourceViewer().getDocument();
		sourceUi = AbapSourceUi.getInstance();
		scannerServices = sourceUi.getSourceScannerServices();
		sourcePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
				.getAdapter(AbapSourcePage.class);
		Code = document.get();
		CurrentStatement = createStatement(context.getOffset());
		try {
			NextStatement = createStatement(CurrentStatement.getEndOfStatement() + 2);
		} catch (Exception e) {

		}
		try {
			PreviousStatement = createStatement(CurrentStatement.getBeginOfStatement() - 2);
		} catch (Exception e) {

		}
	}

	private AbapStatement createStatement(int offset) {
		return new AbapStatement(offset);
	}

	public static AbapCodeReader getInstance(IQuickAssistInvocationContext context) {
		if (CodeReader == null) {
			CodeReader = new AbapCodeReader(context);
			Context = context;
		}
		if (getContext() != context) {
			CodeReader = new AbapCodeReader(context);
			Context = context;
		}

		return CodeReader;
	}

	public static String getCode() {
		return Code;
	}

	public static IQuickAssistInvocationContext getContext() {
		return Context;
	}

	public static AbapCodeReader getInstance() {
		return CodeReader;
	}

	public static boolean isKeyword(int offset) {
		boolean keyword = false;
		try {
			keyword = scannerServices.isKeyword(sourcePage, offset, true);
		} catch (Exception e) {
			keyword = false;
		}
		return keyword;
	}
}
