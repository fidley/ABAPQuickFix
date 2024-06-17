package com.abapblog.adt.quickfix.assist.syntax.statements.move;

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

			if (currentToken.name.toUpperCase().equals(ToToken)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
				ToTokenFound = true;
			}

			if (!CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)
					|| (currentToken.name.toUpperCase().equals("]"))) {
				if (ToTokenFound) {
					target = target + " " + currentToken.name;
				} else {
					source = source + " " + currentToken.name;
				}
			}

			if (currentToken.name.toUpperCase().equals(ExpandingToken)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
				isExpandingNestedTables = true;
			}

			if (currentToken.name.toUpperCase().equals(KeepingToken)
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
				isKeepingTargetLines = true;
			}

		}

		return StringCleaner.clean("\r\n" + target.trim() + " = CORRESPONDING #("
				+ (isExpandingNestedTables ? " DEEP " : "") + (isKeepingTargetLines ? " APPENDING" : "") + " BASE ( "
				+ target.trim() + " ) " + source.trim() + " )");
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
					&& CodeReader.scannerServices.isKeyword(CodeReader.sourcePage, currentToken.offset + 1, true)) {
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
