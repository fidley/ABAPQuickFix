package com.abapblog.adt.quickfix.assist.comments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbapQuickFixRemoveCommentsCodeParser {
	public static final String MULTIPLE_EMPTY_LINES = "(\\r?\\n\\r?\\n?(\\r?\\n))+";
	public static final String SINGLE_LINE_COMMENT_PATTERN = "(?!\\\"\\!)(?!\\\"#EC )(\\\".*)";
	public static final String FULL_LINE_COMMENT_PATERN = "(^(\\*.*)|(\\r\\n\\*.*))";
	public static final String FIRST_CHAR_OF_FULL_LINE_COMMENT = "(^\\*)";

	public Boolean hasComments(String codeText, int start, int end) {
		String NewCodeText = codeText.substring(start, end);
		if (matchFullLineComments(NewCodeText) == true || matchFlexibleComments(NewCodeText) == true) {
			return true;
		}
		return false;
	}
	
	public Boolean hasFullLineComments(String codeText, int start, int end) {
		String selectedCode = codeText.substring(start,end);
		Pattern pattern = Pattern.compile(FIRST_CHAR_OF_FULL_LINE_COMMENT, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(selectedCode);
		return matcher.find();
	}
	
	public String parse(String codeText) {
		return removeComments(codeText);
	}

	private String removeComments(String codeText) {
		String NewCodeText = codeText;
		NewCodeText = NewCodeText.replaceAll(FULL_LINE_COMMENT_PATERN, "");
		NewCodeText = NewCodeText.replaceAll(SINGLE_LINE_COMMENT_PATTERN, "");
		NewCodeText = NewCodeText.replaceAll(MULTIPLE_EMPTY_LINES, "\r\n\r\n");
		return NewCodeText;
	}
	
	public String fullLineCommentsToInLineComments(String codeText) {
		Pattern pattern = Pattern.compile(FIRST_CHAR_OF_FULL_LINE_COMMENT, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(codeText);
		
		final String replaceWith = "\"";
		return matcher.replaceAll(replaceWith);
	}

	private Boolean matchFlexibleComments(String codeText) {
		Pattern flexibleCommentPattern = Pattern.compile(SINGLE_LINE_COMMENT_PATTERN);

		Matcher flexibleCommentMatcher = flexibleCommentPattern.matcher(codeText);
		while (flexibleCommentMatcher.find()) {
			return true;
		}
		return false;
	}

	private Boolean matchFullLineComments(String codeText) {
		Pattern fullLineCommentsPattern = Pattern.compile(FULL_LINE_COMMENT_PATERN, Pattern.MULTILINE);

		Matcher fullLineCommentsMatcher = fullLineCommentsPattern.matcher(codeText);
		while (fullLineCommentsMatcher.find()) {
			return true;
		}
		return false;
	}
}