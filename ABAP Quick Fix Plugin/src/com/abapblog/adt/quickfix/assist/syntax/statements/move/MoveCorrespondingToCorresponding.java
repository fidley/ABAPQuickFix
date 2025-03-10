package com.abapblog.adt.quickfix.assist.syntax.statements.move;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public class MoveCorrespondingToCorresponding extends StatementAssist {

	private static final String MoveCorrenspondingToken = "MOVE-CORRESPONDING";
	private static final String ExpandingToken = "EXPANDING";
	private static final String KeepingToken = "KEEPING";
	private static final String ToToken = "TO";

	@Override
	public String getChangedCode() {
		Boolean isExpandingNestedTables = false;
		Boolean isKeepingTargetLines = false;
		String source = "";
		String target = "";
		Boolean ToTokenFound = false;

		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);

			if (currentToken.name.toUpperCase().equals(ToToken) && AbapCodeReader.isKeyword(currentToken.offset + 1)) {
				ToTokenFound = true;
			}

			if (!AbapCodeReader.isKeyword(currentToken.offset + 1) || (currentToken.name.toUpperCase().equals("]"))) {
				if (ToTokenFound) {
					target = target + " " + currentToken.name;
				} else {
					source = source + " " + currentToken.name;
				}
			}

			if (currentToken.name.toUpperCase().equals(ExpandingToken)
					&& AbapCodeReader.isKeyword(currentToken.offset + 1)) {
				isExpandingNestedTables = true;
			}

			if (currentToken.name.toUpperCase().equals(KeepingToken)
					&& AbapCodeReader.isKeyword(currentToken.offset + 1)) {
				isKeepingTargetLines = true;
			}

		}

		return StringCleaner.clean(target.trim() + " = CORRESPONDING #(" + (isExpandingNestedTables ? " DEEP " : "")
				+ (isKeepingTargetLines ? " APPENDING" : "") + " BASE ( " + target.trim() + " ) " + source.trim()
				+ " )");
	}

	@Override
	public String getAssistShortText() {
		return "Replace MOVE-CORRESPONDING with CORRESPONDING #( )";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		for (int i = 0; i < CodeReader.CurrentStatement.statementTokens.size(); i++) {
			Token currentToken = CodeReader.CurrentStatement.statementTokens.get(i);
			if (currentToken.name.toUpperCase().equals(MoveCorrenspondingToken)
					&& AbapCodeReader.isKeyword(currentToken.offset + 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatementReplacement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

}
