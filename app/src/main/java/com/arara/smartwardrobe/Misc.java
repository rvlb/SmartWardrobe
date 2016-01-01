package com.arara.smartwardrobe;

import android.app.AlertDialog;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Misc {

    public static void showAlertMsg(String msg, String bMsg, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(bMsg, null);
        dialogBuilder.show();
    }

    public static String getFormattedServerResponse(String response) {
        return (response.replace("\"","")).replace("<br />","");
    }

    public static String getPostDataString(HashMap<String, String> data) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }
}
