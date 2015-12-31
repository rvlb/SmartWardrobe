package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class ScanLabelActivity extends AppCompatActivity {

    /*
    Activity que permite ler o conteúdo de uma label, verifica se ela já foi cadastrada
    no banco de dados e caso não tenha sido, permite que isso seja feito.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_label);

        checkLabel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkLabel();
    }

    private void checkLabel() {
        String formattedTag = getFormattedTag("12-3A-BC-DE");
        Log.d("formattedTag", formattedTag);
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchTagDataInBackground(formattedTag, new Callback() {
            @Override
            public void done(ServerResponse serverResponse) {
                Log.d("serverResponseTag", serverResponse.response);
                if (serverResponse.response.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", ScanLabelActivity.this);
                    finish();
                } else if (serverResponse.response.equals("no tag")) {
                    Misc.showAlertMsg("Tag not found.", "Ok", ScanLabelActivity.this);
                    //Registrar tag
                } else {
                    Intent intent = new Intent(ScanLabelActivity.this, ViewWearableActivity.class);
                    intent.putExtra("selectedWearable", buildWearable(serverResponse.response));
                    startActivity(intent);
                }
            }
        });
    }

    private String getFormattedTag(String inputTag) {

        return (inputTag.toLowerCase()).replace("-", "");
    }

    private Wearable buildWearable(String wearableString) {

        List<String> wearableData = Arrays.asList(wearableString.split("#"));

        Wearable newWearable = new Wearable();

        newWearable.id = wearableData.get(0);
        newWearable.colors = (wearableData.get(1)).replace("&", ", ");
        newWearable.type = wearableData.get(2);
        newWearable.brand = wearableData.get(3);
        newWearable.description = wearableData.get(4);
        newWearable.owner = wearableData.get(5);

        return newWearable;
    }
}
