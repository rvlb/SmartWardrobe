package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyWearablesActivity extends AppCompatActivity {

    List<String> wearablesList;
    ListView lvWearables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wearables);

        wearablesList = new ArrayList<>();

        loadWearablesList();
    }

    private void loadWearablesList() {
        String wearablesOwner = getIntent().getExtras().getString("owner");
        Log.d("wearablesOwner", wearablesOwner);
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserWearableDataInBackground(new User(wearablesOwner), new Callback() {
            @Override
            public void done(ServerResponse serverResponse) {
                Log.d("serverResponseMyW", serverResponse.response);
                if (serverResponse.response.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", MyWearablesActivity.this);
                    finish();
                } else if (serverResponse.response.equals("no wearables")) {
                    Misc.showAlertMsg("No wearable found.", "Ok", MyWearablesActivity.this);
                    finish();
                } else {
                    buildWearablesList(serverResponse.response);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MyWearablesActivity.this,
                            android.R.layout.simple_list_item_1, wearablesList);
                    Log.d("adapterSize", adapter.getCount() + "");
                    lvWearables = (ListView) findViewById(R.id.lvWearables);
                    lvWearables.setAdapter(adapter);
                    lvWearables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedWearable = (String) lvWearables.getItemAtPosition(position);
                            Misc.showAlertMsg("Clicked on " + selectedWearable, "Ok", MyWearablesActivity.this);
                        }
                    });
                }
            }
        });
    }

    private void buildWearablesList(String wearablesData) {

        Log.d("debugTest", "Started building");

        Log.d("wearablesData", wearablesData);

        //

        /*
        TEST CASES FOR CHECKING THE CAPACITY OF INSERTING INTO THE VIEW
        wearablesList.add("Roupa Teste 1");
        wearablesList.add("Roupa Teste 2");
        wearablesList.add("Roupa Teste 3");
        wearablesList.add("Roupa Teste 4");
        wearablesList.add("Roupa Teste 5");
        wearablesList.add("Roupa Teste 6");
        wearablesList.add("Roupa Teste 7");
        wearablesList.add("Roupa Teste 8");
        wearablesList.add("Roupa Teste 9");
        wearablesList.add("Roupa Teste 10");
        wearablesList.add("Roupa Teste 11");
        wearablesList.add("Roupa Teste 12");
        wearablesList.add("Roupa Teste 13");
        wearablesList.add("Roupa Teste 14");
        wearablesList.add("Roupa Teste 15");
        wearablesList.add("Roupa Teste 16");
        wearablesList.add("Roupa Teste 17");
        wearablesList.add("Roupa Teste 18");
        wearablesList.add("Roupa Teste 19");
        wearablesList.add("Roupa Teste 20");
        */

        Log.d("debugTest", "Finished building");
    }
}
