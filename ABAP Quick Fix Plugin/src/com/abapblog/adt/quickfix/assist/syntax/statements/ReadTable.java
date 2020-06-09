package com.abapblog.adt.quickfix.assist.syntax.statements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;

public class ReadTable implements IQuickAssistProcessor {

	private static final String readTableAssigningWithKey = "(?s)(read).*(table)\\s+(\\S*)\\s+.*(assigning)\\s+(\\S*)\\s+.*(with)\\s+(key)\\s+(.*)";
	private static final String replaceReadTableAssigningWithKey = "ASSIGN $3[ $8 ] TO $5";
	private static final String readTableWithKeyAssigning = "(?s)(read).*(table)\\s+(\\S*)\\s+.*(with)\\s+(key)\\s+(.*)(assigning)\\s+(.*)";
	private static final String replaceReadTableWithKeyAssigning = "ASSIGN $3[ $6 ] TO $8";
	private static final String readTableAssigningIndex = "(read).*(table)\\s+(\\S*)\\s+.*(assigning)\\s+(\\S*)\\s+.*(index)\\s+(\\S*)";
	private static final String replaceReadTableAssigningIndex = "ASSIGN $3[ $7 ] TO $5";
	private static final String readTableIndexAssigning = "(read).*(table)\\s+(\\S*)\\s+.*(index)\\s+(\\S*)\\s+.*(assigning)\\s+(\\S*)";
	private static final String replacereadTableIndexAssigning = "ASSIGN $3[ $5 ] TO $7";

	private static final String readTableRefereceWithKey = "(?s)(read).*(table)\\s+(\\S*)\\s+.*(reference)\\s+(into)\\s+(\\S*)\\s+.*(with)\\s+(key)\\s+(.*)";
	private static final String replaceReadTableRefereceWithKey = "$6 = REF #( $3[ $9 ] )";
	private static final String readTableWithKeyReferece = "(?s)(read).*(table)\\s+(\\S*)\\s+.*(with)\\s+(key)\\s+(.*)(assigning)\\s+(.*)";
	private static final String replaceReadTableWithKeyReferece = "ASSIGN $3[ $6 ] TO $8";
	private static final String readTableRefereceIndex = "(read).*(table)\\s+(\\S*)\\s+.*(assigning)\\s+(\\S*)\\s+.*(index)\\s+(\\S*)";
	private static final String replaceReadTableRefereceIndex = "ASSIGN $3[ $7 ] TO $5";
	private static final String readTableIndexReferece = "(read).*(table)\\s+(\\S*)\\s+.*(index)\\s+(\\S*)\\s+.*(assigning)\\s+(\\S*)";
	private static final String replacereadTableIndexReferece = "ASSIGN $3[ $5 ] TO $7";
	private AbapCodeReader CodeReader;
	private String matchedPattern;
	private String matchedReplacePattern;
	private String assistShortText;
	private String assistLongText;

	@Override
	public boolean canAssist(IQuickAssistInvocationContext context) {
		CodeReader = AbapCodeReader.getInstance(context);

		return checkMatchPatterns(CodeReader.CurrentStatement);
	}

	private boolean checkMatchPatterns(AbapStatement currentStatement) {
		if (currentStatement.matchPattern(readTableAssigningIndex)) {
			matchedPattern = readTableAssigningIndex;
			matchedReplacePattern = replaceReadTableAssigningIndex;
			assistShortText = "Replace READ TABLE with ASSIGN";
			return true;
		}
		if (currentStatement.matchPattern(readTableIndexAssigning)) {
			matchedPattern = readTableIndexAssigning;
			matchedReplacePattern = replacereadTableIndexAssigning;
			assistShortText = "Replace READ TABLE with ASSIGN";
			return true;
		}

		if (currentStatement.matchPattern(readTableAssigningWithKey)) {
			matchedPattern = readTableAssigningWithKey;
			matchedReplacePattern = replaceReadTableAssigningWithKey;
			assistShortText = "Replace READ TABLE with ASSIGN";
			return true;
		}

		if (currentStatement.matchPattern(readTableWithKeyAssigning)) {
			matchedPattern = readTableWithKeyAssigning;
			matchedReplacePattern = replaceReadTableWithKeyAssigning;
			assistShortText = "Replace READ TABLE with ASSIGN";
			return true;
		}

		if (currentStatement.matchPattern(readTableRefereceWithKey)) {
			matchedPattern = readTableRefereceWithKey;
			matchedReplacePattern = replaceReadTableRefereceWithKey;
			assistShortText = "Replace READ TABLE with REF #( )";
			return true;
		}

		return false;
	}

	@Override
	public boolean canFix(Annotation arg0) {
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		List<ICompletionProposal> proposals = new ArrayList<>();
		if (canAssist(context)) {
			CompletionProposal cPropAllComments = createProposal();
			proposals.add(cPropAllComments);
			return proposals.toArray(new ICompletionProposal[1]);

		}
		;

		return null;

	}

	private CompletionProposal createProposal() {
		Image image = null;
		CompletionProposal cPropAllComments = new CompletionProposal(
				CodeReader.CurrentStatement.replacePattern(matchedPattern, matchedReplacePattern),
				CodeReader.CurrentStatement.getBeginOfStatement(), CodeReader.CurrentStatement.getStatementLength(), 0,
				image, assistShortText, null, assistLongText);
		return cPropAllComments;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
