package com.arara.smartwardrobe;

import android.app.AlertDialog;
import android.content.Context;

public class AlertMessage {

    public static void show(String msg, String bMsg, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(bMsg, null);
        dialogBuilder.show();
    }
}
