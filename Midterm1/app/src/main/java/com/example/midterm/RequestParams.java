package com.example.midterm;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class RequestParams {
    private HashMap<String, String> params;
    private StringBuilder stringBuilder;

    public RequestParams() {
        params = new HashMap<>();
        stringBuilder = new StringBuilder();
    }
    public RequestParams addParameter(String key, String value) {
        try {
            //changes + into %2B
            params.put(key, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }


    public String getEncodedParameters() {

        for(String key : params.keySet()) {

            if(stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }

            stringBuilder.append(key + "=" + params.get(key));
        }

        return stringBuilder.toString();
    }

    public String getEncodedUrl(String url) {
        return url + "?" + getEncodedParameters();
    }

    public void encodePostParameters(HttpURLConnection connection) throws IOException {

        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(getEncodedParameters());
        writer.flush();
    }
}
