package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.tools.abapsource.ui.AbapSourceUi;
import com.sap.adt.tools.abapsource.ui.IAbapSourceUi;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;
import com.sap.adt.tools.abapsource.ui.sources.editors.AbapSourcePage;

public abstract class StatementAssistAdt extends StatementAssist {
	protected IDocument document;
	protected IAbapSourceUi sourceUi;
	protected IAbapSourceScannerServices scannerServices;
	protected AbapSourcePage sourcePage;
	protected List<Token> statementTokens;
	protected int beginning;
	protected int end;

	public StatementAssistAdt(IQuickAssistInvocationContext context) {
		super(context);
		initialize(context);
	}

	private void initialize(IQuickAssistInvocationContext context) {
		document = context.getSourceViewer().getDocument();

		sourceUi = AbapSourceUi.getInstance();
		scannerServices = sourceUi.getSourceScannerServices();
		sourcePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
				.getAdapter(AbapSourcePage.class);

		beginning = scannerServices.goBackToDot(document, context.getOffset()) + 1;
		end = scannerServices.goForwardToDot(document, beginning);
		statementTokens = scannerServices.getStatementTokens(context.getSourceViewer().getDocument(), beginning);
	}

}
