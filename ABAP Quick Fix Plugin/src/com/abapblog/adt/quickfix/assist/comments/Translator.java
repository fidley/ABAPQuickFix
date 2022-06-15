package com.abapblog.adt.quickfix.assist.comments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.eclipse.json.provisonnal.com.eclipsesource.json.JsonArray;

import com.abapblog.adt.quickfix.Activator;
import com.abapblog.adt.quickfix.assist.syntax.codeParser.StringCleaner;
import com.abapblog.adt.quickfix.preferences.PreferenceConstants;

public class Translator {
	public static Boolean DO_NOT_CALL_AGAIN = false;

	public static String main(String args) throws IOException {
		return translate("en", args);
	}

	private static String translate(String langTo, String text) throws IOException {
		String urlStr = "http://translate.google.com/translate_a/single?client=dict-chrome-ex&sl=auto&tl=en&dt=t&q="
				+ URLEncoder.encode(text, "UTF-8");
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
		String result = parseJson(response);
		result = removeLineBreaks(result);
		result = removeCommentSigns(result);
		return StringCleaner.clean(result);
	}

	private static String parseJson(StringBuilder response) {
		String jsonTranslation = "";
		JsonArray jsonArray = JsonArray.readFrom(response.toString());

		int len = jsonArray.get(0).asArray().size();
		if (jsonArray.get(0).asArray() != null) {
			for (int i = 0; i < len; i++) {
				jsonTranslation = jsonTranslation + jsonArray.get(0).asArray().get(i).asArray().get(0).asString();
			}
		}
		return jsonTranslation.toString();
	}

	private static String removeLineBreaks(String code) {
		if (checkRemoveLineBreaksNeeded() == false) {
			return code;
		}
		return code.replaceAll("\r\n", "");
	}

	private static String removeCommentSigns(String code) {
		if (checkRemoveCommentSignsNeeded() == false) {
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
