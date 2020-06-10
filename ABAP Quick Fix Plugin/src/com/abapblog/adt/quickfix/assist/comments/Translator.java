package com.abapblog.adt.quickfix.assist.comments;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public class Translator {

    public static String main(String args) throws IOException {
       return translate("en", args);
    }

    private static String translate(String langTo, String text) throws IOException {
        String urlStr = "http://abapblog.com/eclipse/plugin/translateAPI.php" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo ;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	inputLine = new String(inputLine.getBytes(), 0, inputLine.length(), StandardCharsets.UTF_8);
            response.append(inputLine);
        }
        in.close();
        String result = response.toString().replaceAll("\"", "\r\n\"");
        result = result.replaceAll("\\*", "\r\n\\*").replaceFirst("\r\n", "");
        result = removeLineBreaks( result );
        result = removeCommentSigns( result );
        return result.substring(0, result.length() - 1);
    }

    private static String removeLineBreaks(String code) {
    	if ( checkRemoveLineBreaksNeeded() == false )
    	{
    		return code;
    	}
    	return code.replaceAll("\r\n", "");
    }

    private static String removeCommentSigns(String code) {
    	if ( checkRemoveCommentSignsNeeded() == false )
    	{
    		return code;
    	}
    	return code.replaceAll("\"", "").replaceAll("\\*", "");
    }

	private static boolean checkRemoveLineBreaksNeeded() {
		return Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_TCTE_REMOVE_LINE_BREAKS);
	}

	private static boolean checkRemoveCommentSignsNeeded() {
		return Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_TCTE_REMOVE_COMMENT_SIGNS);
	}

}

