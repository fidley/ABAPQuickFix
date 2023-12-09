package com.abapblog.adt.quickfix.assist.syntax.statements.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public abstract class AbstractDataSortByName extends StatementAssist {
	protected String spaces = "";
	protected HashMap<String, List<String>> declMap = new HashMap<>();
	protected List<String> order = new ArrayList<>();
	protected List<AbapStatement> matchedStatements = new ArrayList<>();

	protected final static String dataDoubleBegin = "DATA: ";
	protected final static String dataBegin = "DATA ";
	protected final static String dataInline = "DATA(";
	protected final static String data = "DATA";
	protected final static String newLine = "\r\n";
	protected static boolean additionalRow;

	protected AbstractDataSortByName() {
		super();

		additionalRow = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.ADDITIONAL_EMPTY_ROW_DECL_PREFIX);

		getMatchedStatements();

		if (!matchedStatements.isEmpty()) {
			buildOrder();
			split();
			getSpaces();
		}
	}

	protected void getSpaces() {
		// get the spaces in the first statement
		int lineNr = 0;
		int lineStartOffset = 0;
		int nextLineStartOffset = 0;
		int offsetStatement = matchedStatements.get(0).getBeginOfStatementWithoutLeadingComment();

		try {
			lineNr = AbapCodeReader.document.getLineOfOffset(offsetStatement);
			lineStartOffset = AbapCodeReader.document.getLineOffset(lineNr);
			nextLineStartOffset = AbapCodeReader.document.getLineOffset(lineNr + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}
		String line = AbapCodeReader.document.get().substring(lineStartOffset, nextLineStartOffset - 1)
				.replaceFirst("\\s++$", "");
		int amountOfSpaces = line.length() - line.strip().length();

		for (int i = 0; i < amountOfSpaces; i++) {
			spaces = spaces + " ";
		}

	}

	protected abstract void getMatchedStatements();

	protected void buildOrder() {
		String orderString = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.ORDER_PREFIX_DECL);

		List<String> orderSplit = Arrays.asList(orderString.split(","));

		for (String prefix : orderSplit) {
			order.add(prefix.trim());
		}
	}

	protected abstract void split();

	protected boolean isInlineStatement(AbapStatement statement) {
		String curStmString = statement.getStatement().toUpperCase();
		if (curStmString.startsWith(dataInline)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isCombinedStatement(AbapStatement statement) {
		String curStmString = statement.getStatement().toUpperCase();
		if (curStmString.startsWith(dataDoubleBegin)) {
			// check if single or not
			List<String> splits = Arrays.asList(curStmString.split(","));
			if (splits.size() > 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected boolean isSingleStatement(AbapStatement statement) {
		String curStmString = statement.getStatement().toUpperCase();
		if (curStmString.startsWith(dataDoubleBegin)) {
			// check if single or not
			List<String> splits = Arrays.asList(curStmString.split(","));
			if (splits.size() == 1) {
				return true;
			} else {
				return false;
			}
		} else if (curStmString.startsWith(dataBegin)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int getStartOfReplace() {
		return matchedStatements.get(0).getBeginOfStatementWithoutLeadingComment();
	}

	@Override
	public int getReplaceLength() {
		int end = matchedStatements.get(matchedStatements.size()-1).getEndOfStatement();
		int start = getStartOfReplace();
		return end - start + 1;
	}

	protected static String getVariableName(String code) {
		if (code.startsWith(dataDoubleBegin)) {
			code = code.replaceFirst(dataDoubleBegin, "");
		} else if (code.startsWith(dataBegin)) {
			code = code.replaceFirst(dataBegin, "");
		} else if (code.startsWith(dataInline)) {
			code = code.replaceFirst(dataInline, "");
		}
		List<String> splits = Arrays.asList(code.split(" "));
		if (splits.isEmpty()) {
			return null;
		} else {
			return splits.get(0);
		}
	}
}