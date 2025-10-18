package com.abapblog.adt.quickfix.assist.formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.text.BadLocationException;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.AbapCodeReader;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssist;
import com.sap.adt.tools.abapsource.sources.scanner.IBaseAbapSourceScannerServices.Token;

public class AlignOperators extends StatementAssist {

	protected String[] operatorNames = { "EQ", "BT", "BETWEEN", "NE", "IN", "NOT", "IS", "CP", "CN", "CS", "NS", "LT",
			"LE", "GE", "GT", "=", "<>", ">", "<", ">=", "<=" };;
	private int biggestOffset = 0;
	protected int selectionStart = 0;
	protected int selectionEnd = 0;

	protected List<Token> tokensToChange = new ArrayList();

	public AlignOperators() {
		selectionStart = getStartOfReplace();
		selectionEnd = selectionStart + getReplaceLength();
	}

	@Override
	public String getChangedCode() {
		String sourceCode = CodeReader.getCode();
		int addedOffset = 0;
		int currentOffset = 0;
		int currentLine = 0;
		int previousLine = 0;
		for (int i = 0; i < tokensToChange.size(); i++) {
			Token currentToken = tokensToChange.get(i);
			try {
				currentLine = CodeReader.document.getLineOfOffset(currentToken.offset);
				currentOffset = currentToken.offset - CodeReader.document.getLineOffset(currentLine);

			} catch (BadLocationException e) {
				e.printStackTrace();
				currentOffset = 0;
			}

			if (currentOffset < biggestOffset && currentLine != previousLine) {
				sourceCode = sourceCode.substring(0, currentToken.offset + addedOffset)
						+ getSpaces(biggestOffset - currentOffset)
						+ sourceCode.substring(currentToken.offset + addedOffset);
				addedOffset += biggestOffset - currentOffset;
				previousLine = currentLine;
			}
		}
		return sourceCode.substring(selectionStart, selectionEnd + addedOffset);
	}

	private String getSpaces(int length) {
		StringBuffer outputBuffer = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			outputBuffer.append(" ");
		}
		return outputBuffer.toString();
	}

	private String getOperatorNames() {
		return escapeHTML(String.join(", ", operatorNames));
	}

	public static String escapeHTML(String str) {
		return str.codePoints()
				.mapToObj(c -> c > 127 || "\"'<>&".indexOf(c) != -1 ? "&#" + c + ";" : new String(Character.toChars(c)))
				.collect(Collectors.joining());
	}

	@Override
	public String getAssistShortText() {
		return "Align operators";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return "Aligns operators to the position of the last one.<br>"
				+ "<strong>Works only on selected code</strong><br>" + "Full list of concerned operators is: <br>"
				+ getOperatorNames();
	}

	@Override
	public boolean canAssist() {
		int currentOffset = 0;
		int currentLine = 0;
		int previousLine = 0;
		int offsetChanges = 0;
		Boolean canAssist = false;
		int loopOffset = selectionStart;
		while (loopOffset <= selectionEnd) {
			Token currentToken = CodeReader.scannerServices.getNextToken(CodeReader.document, loopOffset);
			if (currentToken == null) {
				break;
			}
			loopOffset = currentToken.offset + currentToken.name.length();
			if (isTokenAnOperator(currentToken)) {

				tokensToChange.add(currentToken);
				try {
					currentLine = CodeReader.document.getLineOfOffset(currentToken.offset);
					currentOffset = currentToken.offset - CodeReader.document
							.getLineOffset(CodeReader.document.getLineOfOffset(currentToken.offset));

				} catch (BadLocationException e) {
					e.printStackTrace();
					currentOffset = 0;
				}
				offsetChanges = setNumberOfOffsetChanges(currentOffset, offsetChanges, currentLine, previousLine);
				setBiggestOffset(currentOffset, currentLine, previousLine);
				if (previousLine != 0 && currentLine != previousLine && offsetChanges > 0) {
					canAssist = true;
				}
				previousLine = currentLine;
			}
		}
		return canAssist;
	}

	private boolean isTokenAnOperator(Token currentToken) {
		return Arrays.stream(operatorNames).anyMatch(currentToken.name.toUpperCase()::equals)
				&& currentToken.offset >= selectionStart && currentToken.offset <= selectionEnd
				&& (AbapCodeReader.isKeyword(currentToken.offset + 1) || AbapCodeReader.isKeyword(currentToken.offset));
	}

	private int setNumberOfOffsetChanges(int currentOffset, int offsetChanges, int currentLine, int previousLine) {
		if (currentOffset != biggestOffset && biggestOffset != 0 && currentLine != previousLine) {
			offsetChanges += 1;
		}
		return offsetChanges;
	}

	private void setBiggestOffset(int currentOffset, int currentLine, int previousLine) {
		if (currentOffset > biggestOffset && currentLine != previousLine) {
			biggestOffset = currentOffset;
		}
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.getContext().getSourceViewer().getSelectedRange().x;
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.getContext().getSourceViewer().getSelectedRange().y;
	}

}
