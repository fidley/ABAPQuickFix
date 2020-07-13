package com.abapblog.adt.quickfix.assist.comments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbapQuickFixRemoveCommentsCodeParser {
	public static final String multipleEmptyLines = "(\\r?\\n\\r?\\n?(\\r?\\n))+";
	public static final String singleLineCommentPattern = "(?!\\\"\\!)(?!\\\"#EC )(\\\".*)";
	public static final String fullLineCommentPattern = "(^(\\*.*)|(\\r\\n\\*.*))";

	public Boolean haveComment(String codeText, int start, int end) {
		String NewCodeText = codeText.substring(start, end);
		if (matchFullLineComments(NewCodeText) == true || matchFlexibleComments(NewCodeText) == true) {
			return true;
		}

		return false;

	}

	public String parse(String codeText) {

		return removeComments(codeText);

	}

	private String removeComments(String codeText) {
		String NewCodeText = codeText;
		NewCodeText = NewCodeText.replaceAll(fullLineCommentPattern, "");
		NewCodeText = NewCodeText.replaceAll(singleLineCommentPattern, "");
		NewCodeText = NewCodeText.replaceAll(multipleEmptyLines, "\r\n\r\n");
		return NewCodeText;
	}

	private Boolean matchFlexibleComments(String codeText) {
		Pattern flexibleCommentPattern = Pattern.compile(singleLineCommentPattern);

		Matcher flexibleCommentMatcher = flexibleCommentPattern.matcher(codeText);
		while (flexibleCommentMatcher.find()) {
			return true;
		}
		return false;
	}

	private Boolean matchFullLineComments(String codeText) {
		Pattern fullLineCommentsPattern = Pattern.compile(fullLineCommentPattern, Pattern.MULTILINE);

		Matcher fullLineCommentsMatcher = fullLineCommentsPattern.matcher(codeText);
		while (fullLineCommentsMatcher.find()) {
			return true;
		}
		return false;
	}

	static class Match {
		int start;
		String text;
	}
}