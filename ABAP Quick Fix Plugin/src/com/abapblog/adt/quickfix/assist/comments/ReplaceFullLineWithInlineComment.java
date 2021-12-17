package com.abapblog.adt.quickfix.assist.comments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;

import com.abapblog.adt.quickfix.assist.utility.QuickFixIcon;

public class ReplaceFullLineWithInlineComment implements IQuickAssistProcessor {
	AbapQuickFixRemoveCommentsCodeParser commentParser;

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public boolean canFix(Annotation annotation) {
		return false;
	}

	@Override
	public boolean canAssist(IQuickAssistInvocationContext invocationContext) {
		commentParser = new AbapQuickFixRemoveCommentsCodeParser();
		String sourceCode = invocationContext.getSourceViewer().getDocument().get();
		int length = invocationContext.getSourceViewer().getSelectedRange().y;
		int offset = invocationContext.getSourceViewer().getSelectedRange().x;

		return commentParser.hasFullLineComments(sourceCode, offset, offset + length);
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		if (!canAssist(context)) {
			return null;
		}
		List<ICompletionProposal> proposals = new ArrayList<>();
		int length = context.getSourceViewer().getSelectedRange().y;
		int offset = context.getSourceViewer().getSelectedRange().x;
		String sourceCode = context.getSourceViewer().getDocument().get();

		CompletionProposal cPropSelectedComments = new CompletionProposal(
				commentParser.fullLineCommentsToInLineComments(sourceCode.substring(offset, offset + length)), offset,
				length, 0, QuickFixIcon.get(), "Replace full-line with in-line comments in selection", null,
				"Replaces all full-line comments in the selected code. Please think twice before using it.");
		proposals.add(cPropSelectedComments);
		return proposals.toArray(new ICompletionProposal[1]);
	}

}