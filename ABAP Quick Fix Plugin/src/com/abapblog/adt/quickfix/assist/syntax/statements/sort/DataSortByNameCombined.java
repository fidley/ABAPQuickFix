package com.abapblog.adt.quickfix.assist.syntax.statements.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapStatement;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;

public class DataSortByNameCombined extends AbstractDataSortByName implements IAssistRegex {

	public DataSortByNameCombined() {
		super();
	}

	@Override
	public String getChangedCode() {
		return mapToCode(declMap, order, spaces);
	}

	@Override
	protected void split() {
		declMap = statementToMap(matchedStatements.get(0), order);
	}
	
	public static String mapToCode(HashMap<String, List<String>> map, List<String> order, String spaces) {
		// store starting DATA:
		String code = dataDoubleBegin;
		
		for (int i = 0; i < dataDoubleBegin.length(); i++) {
			spaces = spaces + " ";
		}

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
					code = code + decl + "," + newLine;
				} else {
					code = code + spaces + decl + "," + newLine;
				}
			}

			if (additionalRow) {
				code = code + newLine;
			}
		}

		// delete last comma and set point
		code = code.substring(0, code.lastIndexOf(",")) + ".";

		return code;
	}

	public static HashMap<String, List<String>> statementToMap(AbapStatement statement, List<String> order) {
		HashMap<String, List<String>> map = new HashMap<>();

		String code = statement.getStatement();
		// delete starting DATA:
		code = code.substring(dataDoubleBegin.length(), code.length());
		List<String> allDeclars = new ArrayList<>();
		allDeclars.addAll(Arrays.asList(code.split(",")));

		for (String prefix : order) {
			List<String> declarsForMap = new ArrayList<>();
			List<String> curDeclars = new ArrayList<>();

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
			map.put(prefix, declarsForMap);
		}

		return map;
	}

	@Override
	public String getAssistShortText() {
		return "Sort combined DATA statement";
	}

	@Override
	public String getAssistLongText() {
		return null;
	}

	@Override
	public boolean canAssist() {
		if (matchedStatements.size() == 1) {
			if (isCombinedStatement(matchedStatements.get(0))) {
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
		return null;
	}

	@Override
	public String getReplacePattern() {
		return null;
	}

	@Override
	protected void getMatchedStatements() {
		AbapStatement curStatement = CodeReader.CurrentStatement;

		if (isCombinedStatement(curStatement)) {
			matchedStatements.add(curStatement);
		}
	}
}
