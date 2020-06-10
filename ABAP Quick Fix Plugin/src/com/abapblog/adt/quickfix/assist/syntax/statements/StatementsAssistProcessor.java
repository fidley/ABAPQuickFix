package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;

import com.abapblog.adt.quickfix.assist.syntax.statements.Move.Move;
import com.abapblog.adt.quickfix.assist.syntax.statements.Move.MoveExact;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableAssigningIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableAssigningWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableIndexAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableIndexInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableIndexReferenceInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableReferenceIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableReferenceIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableWithKeyAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableWithKeyInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.ReadTable.ReadTableWithKeyReferenceInto;

public class StatementsAssistProcessor implements IQuickAssistProcessor {

	private List<IAssistRegex> assists;
	private List<ICompletionProposal> proposals;
	private IQuickAssistInvocationContext context;

	@Override
	public boolean canAssist(IQuickAssistInvocationContext context) {
		return true;
	}

	@Override
	public boolean canFix(Annotation arg0) {
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		this.context = context;
		createAssistsList();
		createProposals();
		if (proposals.isEmpty()) {
			return null;
		} else {
			return proposals.toArray(new ICompletionProposal[1]);
		}

	}

	private void createProposals() {
		proposals = new ArrayList<>();

		Iterator<IAssistRegex> assistIterator = assists.iterator();
		while (assistIterator.hasNext()) {
			IAssistRegex assist = assistIterator.next();
			if (assist.canAssist()) {
				proposals.add(new CompletionProposal(assist.getChangedCode(), assist.getStartOfReplace(),
						assist.getReplaceLength(), 0, assist.getAssistIcon(), assist.getAssistShortText(), null,
						assist.getAssistLongText()));

			}
		}
	}

	private void createAssistsList() {
		assists = new ArrayList<>();
		assists.add(new ReadTableAssigningIndex(context));
		assists.add(new ReadTableIndexAssigning(context));
		assists.add(new ReadTableWithKeyAssigning(context));
		assists.add(new ReadTableAssigningWithKey(context));
		assists.add(new ReadTableReferenceIntoIndex(context));
		assists.add(new ReadTableIndexReferenceInto(context));
		assists.add(new ReadTableWithKeyReferenceInto(context));
		assists.add(new ReadTableReferenceIntoWithKey(context));
		assists.add(new ReadTableIndexInto(context));
		assists.add(new ReadTableWithKeyInto(context));
		assists.add(new ReadTableIntoIndex(context));
		assists.add(new ReadTableIntoWithKey(context));
		assists.add(new CallMethod(context));
		assists.add(new Move(context));
		assists.add(new MoveExact(context));
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
