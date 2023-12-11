package com.abapblog.adt.quickfix.assist.syntax.statements;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

public class QuickFIxProposal implements ICompletionProposal {

	/** The string to be displayed in the completion proposal popup. */
	private String fDisplayString;
	/** The replacement string. */
	private String fReplacementString;
	/** The replacement offset. */
	private int fReplacementOffset;
	/** The replacement length. */
	private int fReplacementLength;
	/** The cursor position after this proposal has been applied. */
	private int fCursorPosition;
	/** The image to be displayed in the completion proposal popup. */
	private Image fImage;
	/** The context information of this proposal. */
	private IContextInformation fContextInformation;
	/** The additional info of this proposal. */
	private String fAdditionalProposalInfo;

	private Boolean fcallPrettyPrintForBlock;

	/**
	 * Creates a new completion proposal based on the provided information. The
	 * replacement string is considered being the display string too. All remaining
	 * fields are set to <code>null</code>.
	 *
	 * @param replacementString the actual string to be inserted into the document
	 * @param replacementOffset the offset of the text to be replaced
	 * @param replacementLength the length of the text to be replaced
	 * @param cursorPosition    the position of the cursor following the insert
	 *                          relative to replacementOffset
	 */
	public QuickFIxProposal(String replacementString, int replacementOffset, int replacementLength,
			int cursorPosition) {
		this(replacementString, replacementOffset, replacementLength, cursorPosition, null, null, null, null, false);
	}

	/**
	 * Creates a new completion proposal. All fields are initialized based on the
	 * provided information.
	 *
	 * @param replacementString      the actual string to be inserted into the
	 *                               document
	 * @param replacementOffset      the offset of the text to be replaced
	 * @param replacementLength      the length of the text to be replaced
	 * @param cursorPosition         the position of the cursor following the insert
	 *                               relative to replacementOffset
	 * @param image                  the image to display for this proposal
	 * @param displayString          the string to be displayed for the proposal
	 * @param contextInformation     the context information associated with this
	 *                               proposal
	 * @param additionalProposalInfo the additional information associated with this
	 *                               proposal
	 */
	public QuickFIxProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString, IContextInformation contextInformation, String additionalProposalInfo,
			Boolean callPrettyPrintForBlock) {
		Assert.isNotNull(replacementString);
		Assert.isTrue(replacementOffset >= 0);
		Assert.isTrue(replacementLength >= 0);
		Assert.isTrue(cursorPosition >= 0);

		fReplacementString = replacementString;
		fReplacementOffset = replacementOffset;
		fReplacementLength = replacementLength;
		fCursorPosition = cursorPosition;
		fImage = image;
		fDisplayString = displayString;
		fContextInformation = contextInformation;
		fAdditionalProposalInfo = additionalProposalInfo;
		fcallPrettyPrintForBlock = callPrettyPrintForBlock;
	}

	@Override
	public void apply(IDocument document) {
		try {
			document.replace(fReplacementOffset, fReplacementLength, fReplacementString);
			callPrettyPrintOfBlock(document);

		} catch (BadLocationException x) {
			// ignore
		}
	}

	private void callPrettyPrintOfBlock(IDocument document) {
		if (fcallPrettyPrintForBlock) {
			try {
				setSelectionOnDocument(document);
				callPrettyPrintBlockCommand();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void setSelectionOnDocument(IDocument document) {
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		IAbapSourcePage aSP = getTextEditor(activeEditor);
		TextSelection tS = new TextSelection(document, fReplacementOffset, fReplacementString.length());
		aSP.getSelectionProvider().setSelection(tS);
	}

	private void callPrettyPrintBlockCommand() {
		IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand("com.sap.adt.tools.abapsource.ui.prettyPrintBlock", null);
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			e.printStackTrace();
		}
	}

	public IAbapSourcePage getTextEditor(IEditorPart editor) {
		IAbapSourcePage textEditor = null;
		if (editor instanceof MultiPageEditorPart) {
			MultiPageEditorPart multiPageEditor = (MultiPageEditorPart) editor;
			IEditorPart activePage = (IEditorPart) multiPageEditor.getSelectedPage();
			if (activePage instanceof IAbapSourcePage) {
				textEditor = (IAbapSourcePage) activePage;
			} else if (multiPageEditor instanceof IAdtFormEditor) {

				IEditorPart ed = ((IAdtFormEditor) multiPageEditor).getActiveEditor();
				if (ed instanceof IAbapSourcePage) {
					textEditor = (IAbapSourcePage) ed;
				}
			}
		} else if (editor instanceof IAbapSourcePage) {
			textEditor = (IAbapSourcePage) editor;
		}
		return textEditor;
	}

	@Override
	public Point getSelection(IDocument document) {
		return new Point(fReplacementOffset + fCursorPosition, 0);
	}

	@Override
	public IContextInformation getContextInformation() {
		return fContextInformation;
	}

	@Override
	public Image getImage() {
		return fImage;
	}

	@Override
	public String getDisplayString() {
		if (fDisplayString != null)
			return fDisplayString;
		return fReplacementString;
	}

	@Override
	public String getAdditionalProposalInfo() {
		return fAdditionalProposalInfo;
	}

}
