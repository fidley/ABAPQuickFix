package com.abapblog.adt.quickfix;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbapQuickFixRemoveCommentsCodeParser {
	List<Match> commentMatches = new ArrayList<Match>();

	public Boolean haveComment(String codeText, int start, int end)
	{
		String NewCodeText = codeText.substring(start, end);
		matchFullLineComments(NewCodeText);
		matchFlexibleComments(NewCodeText);

		return !commentMatches.isEmpty();

	}

	public String parse(String codeText) {

		matchFullLineComments(codeText);
		matchFlexibleComments(codeText);
		return removeComments(codeText);

	}

	public String parse(String codeText, int start, int end) {

		matchFullLineComments(codeText);
		matchFlexibleComments(codeText);
		return removeComments(codeText, start, end);

	}

	private String removeComments(String codeText) {
		String NewCodeText = codeText;
		for (Match comment : commentMatches) {
			NewCodeText = NewCodeText.replace(comment.text, "");

		}

		NewCodeText = NewCodeText.replaceAll("(\\r?\\n\\r?\\n\\r?\\n)+", "\r\n");
		return NewCodeText;
	}

	private String removeComments(String codeText, int start, int end) {
		String NewCodeText = codeText.substring(start, end);
		for (Match comment : commentMatches) {
			if (comment.start >= start && comment.start <= end) {

				NewCodeText = NewCodeText.replace(comment.text, "");		}
		}

		return NewCodeText.replaceAll("(\\r?\\n\\r?\\n)+", "\r\n");
	}

	private void matchFlexibleComments(String codeText) {
		Pattern flexibleCommentPattern = Pattern.compile("(\\\".*)");

		Matcher flexibleCommentMatcher = flexibleCommentPattern.matcher(codeText);
		while (flexibleCommentMatcher.find()) {
			Match match = new Match();
			match.start = flexibleCommentMatcher.start();
			match.text = flexibleCommentMatcher.group();
			System.out.println(match.text);
			commentMatches.add(match);
		}
	}

	private void matchFullLineComments(String codeText) {
		Pattern fullLineCommentsPattern = Pattern.compile("^(\\*.*)", Pattern.MULTILINE);

		Matcher fullLineCommentsMatcher = fullLineCommentsPattern.matcher(codeText);
		while (fullLineCommentsMatcher.find()) {
			Match match = new Match();
			match.start = fullLineCommentsMatcher.start();
			match.text = fullLineCommentsMatcher.group();
			commentMatches.add(match);
			System.out.println(match.text);
		}
	}

	static class Match {
		int start;
		String text;
	}
}