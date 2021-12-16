package com.abapblog.adt.quickfix.assist.formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistAdt;
import com.sap.adt.tools.abapsource.ui.sources.IAbapSourceScannerServices.Token;

public class AlignOperators extends StatementAssistAdt {

	protected String[] operatorNames = { "EQ", "BT", "BETWEEN", "NE", "IN", "NOT", "IS", "CP", "CN", "CS", "NS", "LT",
			"LE", "GE", "GT", "=", "<>", ">", "<", ">=", "<=" };;
	private int biggestOffset = 0;
	protected int selectionStart = 0;
	protected int selectionEnd = 0;
	private String sourceCode = "";

	protected List<Token> tokensToChange = new ArrayList();

	public AlignOperators(IQuickAssistInvocationContext context) {
		super(context);
		selectionStart = getStartOfReplace();
		selectionEnd = selectionStart + getReplaceLength();
		sourceCode = context.getSourceViewer().getDocument().get();
	}

	@Override
	public String getChangedCode() {
		int addedOffset = 0;
		int currentOffset = 0;
		int currentLine = 0;
		int previousLine = 0;
		for (int i = 0; i < tokensToChange.size(); i++) {
			Token currentToken = tokensToChange.get(i);
			try {
				currentLine = document.getLineOfOffset(currentToken.offset);
				currentOffset = currentToken.offset - document.getLineOffset(currentLine);

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
	public Image getAssistIcon() {
		// TODO Auto-generated method stub
		return null;
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
			Token currentToken = scannerServices.getNextToken(document, loopOffset);
			if (currentToken == null) {
				break;
			}
			loopOffset = currentToken.offset + currentToken.name.length();
			if (isTokenAnOperator(currentToken)) {

				tokensToChange.add(currentToken);
				try {
					currentLine = document.getLineOfOffset(currentToken.offset);
					currentOffset = currentToken.offset
							- document.getLineOffset(document.getLineOfOffset(currentToken.offset));

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
				&& scannerServices.isKeyword(sourcePage, currentToken.offset + 1, true);
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
		return context.getSourceViewer().getSelectedRange().x;
	}

	@Override
	public int getReplaceLength() {
		return context.getSourceViewer().getSelectedRange().y;
	}

}
