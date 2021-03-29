package com.abapblog.adt.quickfix.assist.syntax.statements.operators;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.sap.adt.tools.abapsource.ui.AbapSourceUi;
import com.sap.adt.tools.abapsource.ui.IAbapSourceUi;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;
import com.sap.adt.tools.abapsource.ui.sources.editors.AbapSourcePage;

public class Eq extends StatementAssist implements IAssistRegex {

	private IDocument document;
	private IAbapSourceUi sourceUi;
	private IAbapSourceScannerServices scannerServices;
	private AbapSourcePage sourcePage;
	private List<Token> statementTokens;
	private int beginning;
	private int end;

	@SuppressWarnings("restriction")
	public Eq(IQuickAssistInvocationContext context) {
		super(context);
		document = context.getSourceViewer().getDocument();

		sourceUi = AbapSourceUi.getInstance();
		scannerServices = sourceUi.getSourceScannerServices();
		sourcePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
				.getAdapter(AbapSourcePage.class);

		beginning = scannerServices.goBackToDot(document, context.getOffset()) + 1;
		end = scannerServices.goForwardToDot(document, beginning);
		statementTokens = scannerServices.getStatementTokens(context.getSourceViewer().getDocument(), beginning);
	}

	@Override
	public String getMatchPattern() {
		return "";
	}

	@Override
	public String getReplacePattern() {
		return "";
	}

	@Override
	public String getChangedCode() {
		String code = sourcePage.getSource();
		for (int i = 0; i < statementTokens.size(); i++) {
			Token currentToken = statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals("EQ")
					&& scannerServices.isKeyword(sourcePage, currentToken.offset + 1, true)) {
				code = code.substring(0, currentToken.offset) + "= " + code.substring(currentToken.offset + 2);
			}
		}
		return StringCleaner.clean(code.substring(beginning, end));

	}

	@Override
	public String getAssistShortText() {
		return "Replace EQ with =";
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public Image getAssistIcon() {
		return null;
	}

	@Override
	public boolean canAssist() {

		for (int i = 0; i < statementTokens.size(); i++) {
			Token currentToken = statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals("EQ")
					&& scannerServices.isKeyword(sourcePage, currentToken.offset + 1, true)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return beginning;
	}

	@Override
	public int getReplaceLength() {
		return end - beginning;
	}

}
