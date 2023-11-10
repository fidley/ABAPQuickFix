package com.abapblog.adt.quickfix.assist.syntax.statements.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public class DataSortByName extends StatementAssist implements IAssistRegex {
	private boolean additionalRow;
	private String MatchPattern = "(?s)\\s*data\\s*:*\\s+(.*)";
	private String ReplacePattern = "$1";
	private List<String> order = new ArrayList<String>();
	private String spaces = "";
	private static final String dataBegin = "DATA: ";
	HashMap<String, List<String>> declMap = new HashMap<String, List<String>>();

	public DataSortByName() {
		super();

		additionalRow = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.ADDITIONAL_EMPTY_ROW_SORT_DECL);

		buildOrder();
		getSpaces();
	}

	private void getSpaces() {
		int lineNr = 0;
		int lineStartOffset = 0;
		int nextLineStartOffset = 0;
		int offsetStatement = CodeReader.CurrentStatement.getBeginOfStatementWithoutLeadingComment();

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
		amountOfSpaces = amountOfSpaces + dataBegin.length();

		for (int i = 0; i < amountOfSpaces; i++) {
			spaces = spaces + " ";
		}
	}

	private void buildOrder() {
		String orderString = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.ORDER_PREFIX_DECL);

		List<String> orderSplit = Arrays.asList(orderString.split(","));

		for (String prefix : orderSplit) {
			order.add(prefix.trim());
		}

		// rest
		order.add("");
	}

	@Override
	public String getChangedCode() {
		int count = 0;
		String statement = CodeReader.CurrentStatement.getStatement();

		// store starting DATA:
		String newCode = statement.substring(0, dataBegin.length());

		statement = statement.substring(dataBegin.length(), statement.length());

		fillMap(statement);

		for (String dicType : order) {
			List<String> decls = declMap.get(dicType);

			if (decls.isEmpty()) {
				continue;
			}

			count++;

			for (int i = 0; i < decls.size(); i++) {
				String decl = decls.get(i);

				if (count == 1 && i == 0) {
					newCode = newCode + decl + ",\r\n";
				} else {
					newCode = newCode + spaces + decl + ",\r\n";
				}
			}

			if (additionalRow) {
				newCode = newCode + "\r\n";
			}
		}

		// delete last comma and set point
		newCode = newCode.substring(0, newCode.lastIndexOf(",")) + ".";

		return newCode;
	}

	private void fillMap(String code) {
		List<String> allDeclars = new ArrayList<String>();
		allDeclars.addAll(Arrays.asList(code.split(",")));

		for (String prefix : order) {
			List<String> declarsForMap = new ArrayList<String>();
			List<String> curDeclars = new ArrayList<String>();

			for (String curDecl : allDeclars) {

				curDecl = curDecl.strip();

				// upper case -> startsWithIgnoreCase()
				if (curDecl.trim().toUpperCase().startsWith(prefix.trim().toUpperCase())) {
					declarsForMap.add(curDecl.strip());
				} else {
					curDeclars.add(curDecl);
				}
			}
			allDeclars = curDeclars;
			declMap.put(prefix, declarsForMap);
		}

		// fill outstanding
		declMap.put("", allDeclars);
	}

	@Override
	public String getAssistShortText() {
		return "Sort DATA statements";
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public boolean canAssist() {
		if (CodeReader.CurrentStatement.isFullLineComment()) {
			return false;
		}

		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())) {

			// check if there are at least two declarations
			if (CodeReader.CurrentStatement.getStatement().split(",").length > 1) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatementWithoutLeadingComment();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getEndOfStatement()
				- CodeReader.CurrentStatement.getBeginOfStatementWithoutLeadingComment() + 1;
	}

	@Override
	public String getMatchPattern() {
		return MatchPattern;
	}

	@Override
	public String getReplacePattern() {
		return ReplacePattern;
	}

}
