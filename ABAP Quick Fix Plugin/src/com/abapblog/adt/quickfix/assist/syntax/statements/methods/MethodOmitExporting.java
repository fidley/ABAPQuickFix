package com.abapblog.adt.quickfix.assist.syntax.statements.methods;

import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.swt.graphics.Image;

import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.assist.syntax.statements.IAssistRegex;
import com.abapblog.adt.quickfix.assist.syntax.statements.StatementAssistRegex;

public class MethodOmitExporting extends StatementAssistRegex implements IAssistRegex {

	private static final String omitExportingPattern = "(?s)(.*(->|=>).*)EXPORTING(.*\\s+(\\r?\\n)*(.*))\\)";
	private static final String importing = "importing";
	private static final String changing = "changing";
	private static final String receiving = "receiving";
	private static final String replaceOmitExportingPattern = "\r\n$1 = $3 )";
	private static final String NewLineString = "\r\n";
	private static final String NewLinePatternWithSpaces = "\\r\\n\\s*";

	public MethodOmitExporting(IQuickAssistInvocationContext context) {
		super(context);
	}

	@Override
	public String getMatchPattern() {
		return omitExportingPattern;
	}

	@Override
	public String getReplacePattern() {
		return replaceOmitExportingPattern;
	}

	@Override
	public String getChangedCode() {
		String Code = "\r\n"
				+ CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 1)
						.replaceFirst(NewLinePatternWithSpaces, "").replaceFirst(NewLineString, "")
				+ CodeReader.CurrentStatement.getMatchGroup(getMatchPattern(), 3)
						.replaceFirst(NewLinePatternWithSpaces, "").replaceFirst(NewLineString, "")
				+ " )";
		return StringCleaner.clean(Code);
	}

	@Override
	public String getAssistShortText() {
		// TODO Auto-generated method stub
		return "Omit EXPORTING";
	}

	@Override
	public String getAssistLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getAssistIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAssist() {
		if (CodeReader.CurrentStatement.matchPattern(getMatchPattern())
				&& !CodeReader.CurrentStatement.matchPattern(importing)
				&& !CodeReader.CurrentStatement.matchPattern(receiving)
				&& !CodeReader.CurrentStatement.matchPattern(changing)) {
			return true;
		}
		return false;
	}

	@Override
	public int getStartOfReplace() {
		return CodeReader.CurrentStatement.getBeginOfStatement();
	}

	@Override
	public int getReplaceLength() {
		return CodeReader.CurrentStatement.getStatementLength();
	}

}
