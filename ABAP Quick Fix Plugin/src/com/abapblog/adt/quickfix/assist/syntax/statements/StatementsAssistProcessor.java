package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;

import com.abapblog.adt.quickfix.assist.comments.ReplaceFullLineWithInlineComment;
import com.abapblog.adt.quickfix.assist.formatter.AlignOperators;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendToAppendValueTo;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendToInsertValueInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Check;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.ClassMethods;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Clear;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Constants;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Data;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.FieldSymbols;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Free;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Methods;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Parameters;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Refresh;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.SelectOptions;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.SplitToSeveralStatements;
import com.abapblog.adt.quickfix.assist.syntax.statements.combine.Types;
import com.abapblog.adt.quickfix.assist.syntax.statements.global.RemoveFullLineCommentsFromStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.insert.InsertIntoInsertValueInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.lineEnd.LineBreakAtEnd;
import com.abapblog.adt.quickfix.assist.syntax.statements.lineEnd.LineBreakAtEndOfMethod;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CallMethod;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectExportingToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitExporting;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitReceiving;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitSelfReference;
import com.abapblog.adt.quickfix.assist.syntax.statements.move.Move;
import com.abapblog.adt.quickfix.assist.syntax.statements.move.MoveExact;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Eq;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Ge;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Gt;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Le;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Lt;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Ne;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableAssigningIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableAssigningWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIndexAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIndexInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIndexReferenceInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIndexTransportingNoFields;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableReferenceIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableReferenceIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableTransportingNoFieldsIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableTransportingNoFieldsWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableWithKeyAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableWithKeyInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableWithKeyReferenceInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.readTable.ReadTableWithKeyTransportingNoFields;
import com.abapblog.adt.quickfix.assist.syntax.statements.reference.GetReferenceToRef;

public class StatementsAssistProcessor implements IQuickAssistProcessor {

	private List<IAssist> assists;
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

		Iterator<IAssist> assistIterator = assists.iterator();
		while (assistIterator.hasNext()) {
			IAssist assist = assistIterator.next();
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
		assists.add(new ReadTableTransportingNoFieldsWithKey(context));
		assists.add(new ReadTableWithKeyTransportingNoFields(context));
		assists.add(new ReadTableTransportingNoFieldsIndex(context));
		assists.add(new ReadTableIndexTransportingNoFields(context));
		assists.add(new CallMethod(context));
		assists.add(new Move(context));
		assists.add(new MoveExact(context));
		assists.add(new Data(context));
		assists.add(new Check(context));
		assists.add(new Types(context));
		assists.add(new Constants(context));
		assists.add(new FieldSymbols(context));
		assists.add(new Clear(context));
		assists.add(new Refresh(context));
		assists.add(new Free(context));
		assists.add(new Parameters(context));
		assists.add(new SelectOptions(context));
		assists.add(new Methods(context));
		assists.add(new ClassMethods(context));
		assists.add(new AppendToAppendValueTo(context));
		assists.add(new AppendToInsertValueInto(context));
		assists.add(new InsertIntoInsertValueInto(context));
		assists.add(new LineBreakAtEnd(context));
		assists.add(new LineBreakAtEndOfMethod(context));
		assists.add(new SplitToSeveralStatements(context));
		assists.add(new MethodOmitReceiving(context));
		assists.add(new MethodOmitExporting(context));
		assists.add(new CreateObjectToNEW(context));
		assists.add(new CreateObjectExportingToNEW(context));
		assists.add(new GetReferenceToRef(context));
		assists.add(new RemoveFullLineCommentsFromStatement(context));
		assists.add(new MethodOmitSelfReference(context));
		assists.add(new Eq(context));
		assists.add(new Ge(context));
		assists.add(new Gt(context));
		assists.add(new Le(context));
		assists.add(new Lt(context));
		assists.add(new Ne(context));
		assists.add(new AlignOperators(context));
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
