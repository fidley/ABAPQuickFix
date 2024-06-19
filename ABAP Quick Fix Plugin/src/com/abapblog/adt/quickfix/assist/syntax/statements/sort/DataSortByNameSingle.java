package com.abapblog.adt.quickfix.assist.syntax.statements.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class DataSortByNameSingle extends AbstractDataSortByName implements IAssistRegex {

	public DataSortByNameSingle() {
		super();
	}

	@Override
	public String getChangedCode() {
		return mapToCode(declMap, order, spaces);
	}

	@Override
	public String getAssistShortText() {
		return "Sort single DATA statements";
	}

	@Override
	public String getAssistLongText() {
		return "Sort single DATA statements in the current area";
	}

	@Override
	public String getMatchPattern() {
		return null;
	}

	@Override
	public String getReplacePattern() {
		return null;
	}

	@Override
	protected void split() {
		declMap = statementsToMap(matchedStatements, order);
	}

	public static String mapToCode(HashMap<String, List<String>> map, List<String> order, String spaces) {
		String code = "";
		int count = 0;
		for (String prefix : order) {
			List<String> decls = map.get(prefix);
			if (decls.isEmpty()) {
				continue;
			}
			count++;
			for (int i = 0; i < decls.size(); i++) {
				String decl = decls.get(i);
				if (count == 1 && i == 0) {
					code = code + decl;
				} else {
					code = code + spaces + decl;
				}
				if (!code.endsWith(newLine)) {
					code = code + newLine;
				}
			}
		}

//		// delete last empty line
//		if (code.endsWith(newLine)) {
//			code = code.substring(0, code.length() - newLine.length());
//		}

		return code;
	}

	public static HashMap<String, List<String>> statementsToMap(List<AbapStatement> allDeclars, List<String> order) {
		HashMap<String, List<String>> map = new HashMap<>();

		for (String prefix : order) {
			List<String> declarsForMap = new ArrayList<>();
			List<AbapStatement> curDeclars = new ArrayList<>();

			for (int i = 0; i < allDeclars.size(); i++) {
				AbapStatement curStatement = allDeclars.get(i);
				String variable = getVariableName(curStatement.getStatement());
				if (variable.trim().toUpperCase().startsWith(prefix)) {
					declarsForMap.add(curStatement.getStatement() + "." + curStatement.getInlineComment());
				} else {
					curDeclars.add(curStatement);
				}
			}
			map.put(prefix, declarsForMap);

			allDeclars = curDeclars;
		}

		return map;
	}

	@Override
	protected void getMatchedStatements() {
		// get local single statements

		if (!isSingleStatement(CodeReader.CurrentStatement)) {
			return;
		}

		AbapStatement curStatement = CodeReader.CurrentStatement;

		// prev
		while (isSingleStatement(curStatement.getPreviousAbapStatement())) {
			curStatement = curStatement.getPreviousAbapStatement();
			matchedStatements.add(curStatement);
		}
		// invert to have the right order
		Collections.reverse(matchedStatements);

		// add the cursor statement
		curStatement = CodeReader.CurrentStatement;
		matchedStatements.add(curStatement);

		// next
		while (isSingleStatement(curStatement.getNextAbapStatement())) {
			curStatement = curStatement.getNextAbapStatement();
			matchedStatements.add(curStatement);
		}
	}

	@Override
	public boolean canAssist() {
		if (this.matchedStatements.size() > 1) {
			return true;
		} else {
			return false;
		}
	}

}
