package com.abapblog.adt.quickfix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {

    public static String main(String args) throws IOException {
       return translate("en", args);
    }

    private static String translate(String langTo, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlStr = "http://abapblog.com/eclipse/plugin/translateAPI.php" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo ;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String result = response.toString().replaceAll("\"", "\r\n\"");
        result = result.replaceAll("\\*", "\r\n\\*");
        result = result.replaceFirst("\r\n", "");
        return result.substring(0, result.length() - 1);
    }

}
