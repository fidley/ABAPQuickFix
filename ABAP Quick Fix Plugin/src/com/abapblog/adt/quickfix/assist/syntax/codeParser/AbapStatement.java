package com.abapblog.adt.quickfix.assist.syntax.codeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;

import com.abapblog.adt.quickfix.assist.utility.RegularExpressionUtils;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public class AbapStatement {
	private static final String fullLineCommentPattern = "^(\\*.*)|^((\\r\\n)+\\*.*)";
	private int beginOfStatement = 0;
	private int beginOfStatementReplacement = 0;
	private int endOfStatement = 0;
	private String Statement = "";
	private String leadingCharacters = "";
	public List<Token> statementTokens = new ArrayList<>();
	private boolean FullLineComment = false;

	public AbapStatement(int offset) {
		int moveOffsetLeftForDot = 0;
		try {
			if (AbapCodeReader.scannerServices.isDot(AbapCodeReader.document.getChar(offset))) {
				moveOffsetLeftForDot = 1;
			}
		} catch (BadLocationException e) {
			return;
		}

		beginOfStatement = AbapCodeReader.scannerServices.goBackToDot(AbapCodeReader.document,
				offset - moveOffsetLeftForDot) + 1;
		if (beginOfStatement == 1)
			beginOfStatement = 0;
		endOfStatement = AbapCodeReader.scannerServices.goForwardToDot(AbapCodeReader.document, offset);
		if (endOfStatement == -1)
			endOfStatement = offset;
		statementTokens = AbapCodeReader.scannerServices.getStatementTokens(AbapCodeReader.document, beginOfStatement);
		if (statementTokens.size() > 0) {
			for (int i = 0; i < statementTokens.size(); i++) {
				if (AbapCodeReader.scannerServices.isComment(AbapCodeReader.document, statementTokens.get(i).offset))
					continue;

				beginOfStatementReplacement = statementTokens.get(i).offset;
				break;
			}
			leadingCharacters = AbapCodeReader.getCode().substring(beginOfStatement, getBeginOfStatementReplacement());
			Statement = AbapCodeReader.getCode().substring(getBeginOfStatementReplacement(), getEndOfStatement());
		} else {
			Statement = AbapCodeReader.getCode().substring(beginOfStatement, getEndOfStatement());

		}
		if (beginOfStatementReplacement == 0) {
			beginOfStatementReplacement = beginOfStatement;
		}
		checkFullLineComment();

	}

	private void checkFullLineComment() {
		if (matchPatternSingleLine(fullLineCommentPattern) && statementTokens.size() == 0)
			setFullLineComment(true);
	}

	public AbapStatement getNextAbapStatement() {
		return new AbapStatement(endOfStatement + 2);

	}

	public AbapStatement getPreviousAbapStatement() {
		return new AbapStatement(beginOfStatement - 2);
	}

	public String getStatement() {
		return Statement;
	}

	public int getEndOfStatement() {
		return endOfStatement;
	}

	public int getBeginOfStatement() {
		return beginOfStatement;
	}

	public int getStatementLength() {
		return endOfStatement - getBeginOfStatementReplacement();
	}

	public Boolean matchPattern(String pattern) {

		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public Boolean matchPatternSingleLine(String pattern) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return true;
		}
		return false;
	}

	public String replacePattern(String pattern, String replace) {

		String StatementCode = Statement;
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {

			return regexPatterMatcher.replaceFirst(replace);
		}

		return StatementCode;

	}

	public String replaceAllPattern(String pattern, String replace) {

		String StatementCode = Statement;
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {

			StatementCode = regexPatterMatcher.replaceAll(replace);
		}

		return StatementCode;

	}

	public String getMatchGroup(String pattern, int group) {
		Pattern redexPattern = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);

		Matcher regexPatterMatcher = RegularExpressionUtils.createMatcherWithTimeout(Statement, redexPattern, 1000);
		while (regexPatterMatcher.find()) {
			return regexPatterMatcher.group(group);
		}

		return "";
	}

	public boolean isFullLineComment() {
		return FullLineComment;
	}

	public void setFullLineComment(boolean isFullLineComment) {
		this.FullLineComment = isFullLineComment;
	}

	public int getBeginOfStatementWithoutLeadingComment() {
		return getBeginOfStatementReplacement();
	}

	public int getBeginOfStatementReplacement() {
		return beginOfStatementReplacement;
	}

	public String getLeadingCharacters() {
		return leadingCharacters;
	}

}
