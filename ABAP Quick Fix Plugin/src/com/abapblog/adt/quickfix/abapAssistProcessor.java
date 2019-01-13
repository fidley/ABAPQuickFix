package com.abapblog.adt.quickfix;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class abapAssistProcessor implements IQuickAssistProcessor {

	@Override
	public boolean canAssist(IQuickAssistInvocationContext arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canFix(Annotation arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		int lenght = context.getLength();
		int offset = context.getOffset();
		int x = context.getSourceViewer().getSelectedRange().x;
		int y = context.getSourceViewer().getSelectedRange().y;
		ITextOperationTarget target = context.getSourceViewer().getTextOperationTarget();
		IFindReplaceTarget FRTarget = context.getSourceViewer().getFindReplaceTarget();
		String SelectionText = FRTarget.getSelectionText();
		int topIndex = context.getSourceViewer().getTopIndex();
		int topIndexSO = context.getSourceViewer().getTopIndexStartOffset();
		int bottomIndex = context.getSourceViewer().getBottomIndex();
		int bottomIndexEO = context.getSourceViewer().getBottomIndexEndOffset();
		String targetString = target.toString();
		String sourceCode = context.getSourceViewer().getDocument().get();
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {

			IWorkbenchPage page = window.getActivePage();
			IEditorPart editor = page.getActiveEditor();
			IEditorInput input = editor.getEditorInput();
			ISelection Selection = editor.getEditorSite().getSelectionProvider().getSelection();
			String classString = Selection.getClass().toString();
		}
		sourceCode.length();
		List<ICompletionProposal> proposals = new ArrayList<>();
		proposals.add( new CompletionProposal("abapBlogTest",1,4,4) );
		return proposals.toArray(new ICompletionProposal[0]);
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}


}
