package com.abapblog.adt.quickfix;

import java.io.IOException;
import java.util.ArrayList;


import java.util.List;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;

public class TranslateCommentToEnglish implements IQuickAssistProcessor {
	AbapQuickFixRemoveCommentsCodeParser commentParser;

	@Override
	public boolean canAssist(IQuickAssistInvocationContext context) {
		commentParser = new AbapQuickFixRemoveCommentsCodeParser();
		String sourceCode = context.getSourceViewer().getDocument().get();
		int lenght = context.getSourceViewer().getSelectedRange().y;
		int offset = context.getSourceViewer().getSelectedRange().x;
		return commentParser.haveComment(sourceCode, offset, offset + lenght);

	}

	@Override
	public boolean canFix(Annotation arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context)  {
		List<ICompletionProposal> proposals = new ArrayList<>();
		if (canAssist(context)) {
			int lenght = context.getSourceViewer().getSelectedRange().y;
			int offset = context.getSourceViewer().getSelectedRange().x;

			String sourceCode = context.getSourceViewer().getDocument().get();

			Image image = null;
			CompletionProposal cPropSelectedComments;
			try {
				String translatedText = Translator.main( sourceCode.substring( offset, offset + lenght) );
				cPropSelectedComments = new CompletionProposal(
						translatedText , offset, lenght, 0, image,
						"Translate selection to English", null,
						translatedText);
				proposals.add(cPropSelectedComments);
				return proposals.toArray(new ICompletionProposal[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
