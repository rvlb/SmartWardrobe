package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTagActivity extends AppCompatActivity {

    List<Wearable> wearablesList;
    ListView lvWearables;

    String tag;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);

        tag = getIntent().getExtras().getString("tag");
        Log.d("tag", tag);

        userLocalStore = new UserLocalStore(this);

        wearablesList = new ArrayList<>();

        ArrayAdapter<Wearable> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wearablesList);

        lvWearables = (ListView) findViewById(R.id.lvWearables);
        lvWearables.setAdapter(adapter);
        lvWearables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Wearable selectedWearable = (Wearable) lvWearables.getItemAtPosition(position);
                //Misc.showAlertMsg("Clicked on " + selectedWearable, "Ok", CreateTagActivity.this);
                registerTag(selectedWearable);
            }
        });
    }

    private void registerTag(Wearable wearable) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeTagDataInBackground(tag, userLocalStore.getLoggedUser(), wearable, new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseTag", serverResponse);
                if (serverResponse.contains("error".replace(" ", ""))) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", CreateTagActivity.this);
                    finish();
                } else if (serverResponse.contains("failure".replace(" ", ""))) {
                    Misc.showAlertMsg("An error occurred.", "Ok", CreateTagActivity.this);
                } else if(serverResponse.contains("success".replace(" ", ""))) {
                    Misc.showAlertMsg("Tag successfully created!", "Ok", CreateTagActivity.this);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWearablesList();
    }

    private void loadWearablesList() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchWearableDataInBackground(new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseW", serverResponse);
                if (serverResponse.contains("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", CreateTagActivity.this);
                } else if (serverResponse.contains("no wearables")) {
                    Log.d("no wearable", "No wearable found");
                    //Misc.showAlertMsg("No wearable found.", "Ok", CreateTagActivity.this);
                } else {
                    buildWearablesList(serverResponse);
                    ((BaseAdapter) lvWearables.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    private void buildWearablesList(String wearablesData) {

        wearablesList.clear();

        Log.d("debugTest", "Started building");

        Log.d("wearablesData", wearablesData);

        List<String> wearableStrings = Arrays.asList(wearablesData.split("\\$"));

        for (String wearable: wearableStrings) {

            List<String> wearableData = Arrays.asList(wearable.split("#"));

            Wearable newWearable = new Wearable();

            newWearable.id = wearableData.get(0);
            newWearable.colors = (wearableData.get(1)).replace("&", ", ");
            newWearable.type = wearableData.get(2);
            newWearable.brand = wearableData.get(3);
            newWearable.description = wearableData.get(4);

            wearablesList.add(newWearable);
        }

        Log.d("wearablesStrings", wearableStrings.toString());

        Log.d("debugTest", "Finished building");
    }
}
