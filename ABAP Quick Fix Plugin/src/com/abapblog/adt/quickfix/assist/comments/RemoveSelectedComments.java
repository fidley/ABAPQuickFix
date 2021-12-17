package com.abapblog.adt.quickfix.assist.comments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.utility.QuickFixIcon;

public class RemoveSelectedComments implements IQuickAssistProcessor {
	AbapQuickFixRemoveCommentsCodeParser commentParser;

	@Override
	public boolean canAssist(IQuickAssistInvocationContext context) {
		commentParser = new AbapQuickFixRemoveCommentsCodeParser();
		String sourceCode = context.getSourceViewer().getDocument().get();
		int length = context.getSourceViewer().getSelectedRange().y;
		int offset = context.getSourceViewer().getSelectedRange().x;
		return commentParser.hasComments(sourceCode, offset, offset + length);
	}

	@Override
	public boolean canFix(Annotation arg0) {
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		List<ICompletionProposal> proposals = new ArrayList<>();
		if (canAssist(context)) {
			int length = context.getSourceViewer().getSelectedRange().y;
			int offset = context.getSourceViewer().getSelectedRange().x;

			String sourceCode = context.getSourceViewer().getDocument().get();

			Image image = QuickFixIcon.get();
			CompletionProposal cPropSelectedComments = new CompletionProposal(
					commentParser.parse(sourceCode.substring(offset, offset + length)), offset, length, 0, image,
					"Remove ABAP Comments in selection", null,
					"Removes all ABAP Comments from the selected code. Please think twice before using it.");
			proposals.add(cPropSelectedComments);
			return proposals.toArray(new ICompletionProposal[1]);
		}
		;
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
