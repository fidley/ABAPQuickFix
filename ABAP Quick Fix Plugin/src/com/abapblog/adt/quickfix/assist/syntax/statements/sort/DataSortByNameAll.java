package com.abapblog.adt.quickfix.assist.syntax.statements.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public class DataSortByNameAll extends AbstractDataSortByName implements IAssistRegex {
	List<AbapStatement> singleStatements;
	List<AbapStatement> combinedStatements;

	public DataSortByNameAll() {
		super();

		singleStatements = getSingleStatements();
		combinedStatements = getCombinedStatements();
	}

	protected List<AbapStatement> getSingleStatements() {
		List<AbapStatement> statements = new ArrayList<>();

		for (AbapStatement curStatement : matchedStatements) {
			if (isSingleStatement(curStatement)) {
				statements.add(curStatement);
			}
		}

		return statements;
	}

	protected List<AbapStatement> getCombinedStatements() {
		List<AbapStatement> statements = new ArrayList<>();

		for (AbapStatement curStatement : matchedStatements) {
			if (isCombinedStatement(curStatement)) {
				statements.add(curStatement);
			}
		}

		return statements;
	}

	protected List<AbapStatement> getInlineStatements() {
		List<AbapStatement> statements = new ArrayList<>();

		for (AbapStatement curStatement : matchedStatements) {
			if (isInlineStatement(curStatement)) {
				statements.add(curStatement);
			}
		}

		return statements;
	}

	@Override
	public String getChangedCode() {
		boolean extraRow = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.ADDITIONAL_EMPTY_LINE_DECL_TYPE);

		// singles
		HashMap<String, List<String>> singlesMap = DataSortByNameSingle.statementsToMap(singleStatements, order);
		String singlesCode = DataSortByNameSingle.mapToCode(singlesMap, order, spaces);

		// all combined statements
		String combinedStatementsCode = "";
		for (int i = 0; i < combinedStatements.size(); i++) {
			AbapStatement curCombStatement = combinedStatements.get(i);
			HashMap<String, List<String>> map = DataSortByNameCombined.statementToMap(curCombStatement, order);
			if (i == 0 && singlesCode.isBlank()) {
				combinedStatementsCode = combinedStatementsCode + DataSortByNameCombined.mapToCode(map, order, spaces);
			} else if (i == 0 && !singlesCode.isBlank()) {
				combinedStatementsCode = combinedStatementsCode + spaces
						+ DataSortByNameCombined.mapToCode(map, order, spaces);
			} else {
				combinedStatementsCode = combinedStatementsCode + newLine + spaces
						+ DataSortByNameCombined.mapToCode(map, order, spaces);
			}

			if (extraRow && !combinedStatementsCode.isBlank() && i != combinedStatements.size() - 1) {
				combinedStatementsCode = combinedStatementsCode + newLine;
			}
		}

		// inline
		String inlineCode = "";
		List<AbapStatement> inlines = getInlineStatements();
		for (int i = 0; i < inlines.size(); i++) {
			inlineCode = inlineCode + spaces + inlines.get(i).getStatement() + "." + newLine;
		}

		String newCode = "";

		if (!singlesCode.isBlank()) {
			newCode = singlesCode;
		}

		if (!combinedStatementsCode.isBlank()) {
			if (newCode.isBlank()) {
				newCode = combinedStatementsCode;
			} else {
				if (extraRow) {
					newCode = newCode + newLine + newLine + combinedStatementsCode;
				} else {
					newCode = newCode + newLine + combinedStatementsCode;
				}
			}
		}

		if (!inlineCode.isBlank()) {
			if (newCode.isBlank()) {
				newCode = inlineCode;
			} else {
				if (extraRow) {
					newCode = newCode + newLine + newLine + inlineCode;
				} else {
					newCode = newCode + newLine + inlineCode;
				}
			}
		}

		if (newCode.endsWith(newLine)) {
			newCode = newCode.substring(0, newCode.length() - newLine.length());
		}

		return newCode;
	}

	@Override
	public String getAssistShortText() {
		return "Sort all DATA statements";
	}

	@Override
	public String getAssistLongText() {
		return "Sort all DATA statements (single & combined)";
	}

	@Override
	protected void split() {

	}

	@Override
	protected void getMatchedStatements() {
		AbapStatement curStatement = CodeReader.CurrentStatement;

		// previous
		while (curStatement.getPreviousAbapStatement().getStatement().startsWith(data)) {
			curStatement = curStatement.getPreviousAbapStatement();
			matchedStatements.add(curStatement);
		}
		// invert to have the right order
		Collections.reverse(matchedStatements);

		// add cursor statement
		curStatement = CodeReader.CurrentStatement;
		matchedStatements.add(CodeReader.CurrentStatement);

		// next
		while (curStatement.getNextAbapStatement().getStatement().startsWith(data)) {
			curStatement = curStatement.getNextAbapStatement();
			matchedStatements.add(curStatement);
		}
	}

	@Override
	public String getMatchPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReplacePattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		int singlesCount = singleStatements.size();
		int combinedCount = combinedStatements.size();

		if (singlesCount > 1 || combinedCount > 1) {
			return true;
		} else {
			return false;
		}
	}
}
