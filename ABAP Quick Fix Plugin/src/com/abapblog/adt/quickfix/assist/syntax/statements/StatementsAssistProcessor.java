package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;

import com.abapblog.adt.quickfix.assist.formatter.AlignOperators;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
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
		AbapCodeReader.getInstance(context);
		assists = new ArrayList<>();
		assists.add(new ReadTableAssigningIndex());
		assists.add(new ReadTableIndexAssigning());
		assists.add(new ReadTableWithKeyAssigning());
		assists.add(new ReadTableAssigningWithKey());
		assists.add(new ReadTableReferenceIntoIndex());
		assists.add(new ReadTableIndexReferenceInto());
		assists.add(new ReadTableWithKeyReferenceInto());
		assists.add(new ReadTableReferenceIntoWithKey());
		assists.add(new ReadTableIndexInto());
		assists.add(new ReadTableWithKeyInto());
		assists.add(new ReadTableIntoIndex());
		assists.add(new ReadTableIntoWithKey());
		assists.add(new ReadTableTransportingNoFieldsWithKey());
		assists.add(new ReadTableWithKeyTransportingNoFields());
		assists.add(new ReadTableTransportingNoFieldsIndex());
		assists.add(new ReadTableIndexTransportingNoFields());
		assists.add(new CallMethod());
		assists.add(new Move());
		assists.add(new MoveExact());
		assists.add(new Data());
		assists.add(new Check());
		assists.add(new Types());
		assists.add(new Constants());
		assists.add(new FieldSymbols());
		assists.add(new Clear());
		assists.add(new Refresh());
		assists.add(new Free());
		assists.add(new Parameters());
		assists.add(new SelectOptions());
		assists.add(new Methods());
		assists.add(new ClassMethods());
		assists.add(new AppendToAppendValueTo());
		assists.add(new AppendToInsertValueInto());
		assists.add(new InsertIntoInsertValueInto());
		assists.add(new LineBreakAtEnd());
		assists.add(new LineBreakAtEndOfMethod());
		assists.add(new SplitToSeveralStatements());
		assists.add(new MethodOmitReceiving());
		assists.add(new MethodOmitExporting());
		assists.add(new CreateObjectToNEW());
		assists.add(new CreateObjectExportingToNEW());
		assists.add(new GetReferenceToRef());
		assists.add(new RemoveFullLineCommentsFromStatement());
		assists.add(new MethodOmitSelfReference());
		assists.add(new Eq());
		assists.add(new Ge());
		assists.add(new Gt());
		assists.add(new Le());
		assists.add(new Lt());
		assists.add(new Ne());
		assists.add(new AlignOperators());
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
