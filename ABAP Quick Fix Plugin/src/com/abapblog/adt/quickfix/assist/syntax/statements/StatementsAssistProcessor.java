package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;

import com.abapblog.adt.quickfix.IFixAppender;
import com.abapblog.adt.quickfix.assist.formatter.AlignOperators;
import com.abapblog.adt.quickfix.assist.formatter.AlignTypes;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendToAppendValueTo;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendToInsertValueInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendWithHeaderLineToAppendValueTo;
import com.abapblog.adt.quickfix.assist.syntax.statements.append.AppendWithHeaderLineToInsertValueInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.Add;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.AddShort;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.Divide;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.DivideShort;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.Multiply;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.MultiplyShort;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.Subtract;
import com.abapblog.adt.quickfix.assist.syntax.statements.calculation.SubtractShort;
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
import com.abapblog.adt.quickfix.assist.syntax.statements.loop.LoopAtItabWithHeaderLineAsFieldSymbol;
import com.abapblog.adt.quickfix.assist.syntax.statements.loop.LoopAtItabWithHeaderLineAsIntoWa;
import com.abapblog.adt.quickfix.assist.syntax.statements.loop.LoopAtItabWithHeaderLineAsRefInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CallMethod;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.Cast;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectExportingToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectWithTypeExportingToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.CreateObjectWithTypeToNEW;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitExporting;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitReceiving;
import com.abapblog.adt.quickfix.assist.syntax.statements.methods.MethodOmitSelfReference;
import com.abapblog.adt.quickfix.assist.syntax.statements.move.Move;
import com.abapblog.adt.quickfix.assist.syntax.statements.move.MoveCorrespondingToCorresponding;
import com.abapblog.adt.quickfix.assist.syntax.statements.move.MoveExact;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Eq;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Ge;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Gt;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Le;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Lt;
import com.abapblog.adt.quickfix.assist.syntax.statements.operators.Ne;
import com.abapblog.adt.quickfix.assist.syntax.statements.reference.GetReferenceToRef;
import com.abapblog.adt.quickfix.assist.syntax.statements.sort.DataSortByNameAll;
import com.abapblog.adt.quickfix.assist.syntax.statements.sort.DataSortByNameCombined;
import com.abapblog.adt.quickfix.assist.syntax.statements.sort.DataSortByNameSingle;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.DescribeTableLines;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableAssigningIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableAssigningWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIndexAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIndexInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIndexReferenceInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIndexTransportingNoFields;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableReferenceIntoIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableReferenceIntoWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableTransportingNoFieldsIndex;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableTransportingNoFieldsWithKey;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableWithKeyAssigning;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableWithKeyInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableWithKeyReferenceInto;
import com.abapblog.adt.quickfix.assist.syntax.statements.table.readTable.ReadTableWithKeyTransportingNoFields;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.BooleanEmptyToAbapFalse;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.BooleanXToAbapTrue;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.EmptyToSpace;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.TranslateToLowerCase;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.TranslateToUpperCase;
import com.abapblog.adt.quickfix.assist.syntax.statements.texts.WriteToStringExpression;

public class StatementsAssistProcessor implements IQuickAssistProcessor {

	private List<IAssist> assists;
	private List<ICompletionProposal> proposals;
	private IQuickAssistInvocationContext context;
	private static final String IFIXAPPENDER_ID = "com.abapblog.additional_quickfixes";

	@Override
	public boolean canAssist(IQuickAssistInvocationContext context) {
		return true;
	}

	@Override
	public boolean canFix(Annotation annotation) {
//		if (annotation instanceof MarkerAnnotation) {
//			MarkerAnnotation markerAnnotation = (MarkerAnnotation) annotation;
//			try {
//				markerAnnotation.getMarker().getAttribute(IMarker.MESSAGE);
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//			IMarker marker = markerAnnotation.getMarker();
////			String markerId = RefactoringUiUtilInternal.getQuickfixMarkerAttributeValue(marker);
////			boolean hasResolutions = (markerId != null);
////			if (hasResolutions) {
////				return true;
////			}
//		}
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		this.context = context;
		try {
			AbapCodeReader.getInstance(context);
			createAssistsList();
			createProposals();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (proposals == null || proposals.isEmpty()) {
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
			try {
				if (assist.canAssist()) {

					proposals.add(new QuickFIxProposal(assist.getChangedCode(), assist.getStartOfReplace(),
							assist.getReplaceLength(), 0, assist.getAssistIcon(), assist.getAssistShortText(), null,
							assist.getAssistLongText(), assist.getCallPrettyPrintOnBlock()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void createAssistsList() {

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
		assists.add(new AppendWithHeaderLineToAppendValueTo());
		assists.add(new AppendToInsertValueInto());
		assists.add(new AppendWithHeaderLineToInsertValueInto());
		assists.add(new InsertIntoInsertValueInto());
		assists.add(new LineBreakAtEnd());
		assists.add(new LineBreakAtEndOfMethod());
		assists.add(new SplitToSeveralStatements());
		assists.add(new MethodOmitReceiving());
		assists.add(new MethodOmitExporting());
		assists.add(new CreateObjectToNEW());
		assists.add(new CreateObjectWithTypeToNEW());
		assists.add(new CreateObjectExportingToNEW());
		assists.add(new CreateObjectWithTypeExportingToNEW());
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
		assists.add(new AlignTypes());
		assists.add(new LoopAtItabWithHeaderLineAsFieldSymbol());
		assists.add(new LoopAtItabWithHeaderLineAsRefInto());
		assists.add(new LoopAtItabWithHeaderLineAsIntoWa());
		try {
			assists.add(new DataSortByNameCombined());
			assists.add(new DataSortByNameAll());
			assists.add(new DataSortByNameSingle());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assists.add(new Add());
		assists.add(new AddShort());
		assists.add(new Subtract());
		assists.add(new SubtractShort());
		assists.add(new Multiply());
		assists.add(new MultiplyShort());
		assists.add(new Divide());
		assists.add(new DivideShort());
		assists.add(new DescribeTableLines());
		assists.add(new WriteToStringExpression());
		assists.add(new MoveCorrespondingToCorresponding());
		assists.add(new TranslateToLowerCase());
		assists.add(new TranslateToUpperCase());
		assists.add(new Cast());
		assists.add(new BooleanXToAbapTrue());
		assists.add(new BooleanEmptyToAbapFalse());
		assists.add(new EmptyToSpace());
		// assists.add(new SelectSingle());

		IConfigurationElement[] config = RegistryFactory.getRegistry().getConfigurationElementsFor(IFIXAPPENDER_ID);

		ArrayList<StatementAssist> list = new ArrayList<StatementAssist>();
		try {
			for (IConfigurationElement e : config) {
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IFixAppender) {
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable e) {
							System.out.println("Exception in client");
						}

						@Override
						public void run() throws Exception {
							list.addAll(((IFixAppender) o).additional_fixes(context));
						}
					};

					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		for (StatementAssist statementAssist : list) {
			assists.add(statementAssist);
		}
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
