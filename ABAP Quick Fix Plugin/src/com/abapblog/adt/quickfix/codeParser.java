package com.abapblog.adt.quickfix;

import java.util.ArrayList;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class codeParser {
    private static final String S_S_S_S_S_N = "(\\r?\\n\\r?\\n\\r?\\n)";
	static List<Match> commentMatches = new ArrayList<Match>();

    public static String parse(String codeText) {
        Pattern commentsPattern = Pattern.compile("^(\\*.*)", Pattern.MULTILINE);
        Pattern stringsPattern = Pattern.compile("(\\\".*)" );
        Pattern doubleEmptyLines = Pattern.compile(S_S_S_S_S_N );

        Matcher commentsMatcher = commentsPattern.matcher(codeText);
        while (commentsMatcher.find()) {
            Match match = new Match();
            match.start = commentsMatcher.start();
            match.text = commentsMatcher.group();
            commentMatches.add(match);
            System.out.println(match.text);
        }

        List<Match> commentsToRemove = new ArrayList<Match>();

        Matcher stringsMatcher = stringsPattern.matcher(codeText);
        while (stringsMatcher.find()) {
        	 Match match = new Match();
        	 match.start = stringsMatcher.start();
        	 match.text = stringsMatcher.group();
        	 System.out.println(match.text);
        	 commentMatches.add(match);
            for (Match comment : commentMatches) {


                //if  (comment.start > stringsMatcher.start() && comment.start < stringsMatcher.end())
                //{
                	//commentsToRemove.add(comment);
                //}
                //else
                //{
                //}
            }
        }
        for (Match comment : commentsToRemove) {
        	//System.out.println(comment.text);
            commentMatches.remove(comment);
        }
        String NewCodeText = codeText;
        for (Match comment : commentMatches)
        {	NewCodeText = NewCodeText.replace(comment.text, ""); }


        NewCodeText =  NewCodeText.replaceAll("(\\r?\\n\\r?\\n\\r?\\n)+", "\r\n");
        return NewCodeText;

    }

    static class Match {
        int start;
        String text;
    }
}