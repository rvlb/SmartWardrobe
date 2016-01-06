package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyWearablesActivity extends AppCompatActivity implements View.OnClickListener {

    List<Wearable> wearablesList;
    ListView lvWearables;
    Button bMyWishList;

    TextView tvWearablesOwnerName;

    String wearablesOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wearables);

        wearablesOwner = getIntent().getExtras().getString("owner");
        Log.d("wearablesOwner", wearablesOwner);

        tvWearablesOwnerName = (TextView) findViewById(R.id.tvWearablesOwnerName);
        tvWearablesOwnerName.setText((wearablesOwner + "'s wearables").toUpperCase());

        bMyWishList = (Button) findViewById(R.id.bMyWishList);
        bMyWishList.setText(wearablesOwner + "'s WishList");
        bMyWishList.setOnClickListener(this);

        wearablesList = new ArrayList<>();

        ArrayAdapter<Wearable> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wearablesList);

        lvWearables = (ListView) findViewById(R.id.lvWearables);
        lvWearables.setAdapter(adapter);
        lvWearables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Wearable selectedWearable = (Wearable) lvWearables.getItemAtPosition(position);
                //Misc.showAlertMsg("Clicked on " + selectedWearable, "Ok", MyWearablesActivity.this);
                Intent intent = new Intent(MyWearablesActivity.this, ViewWearableActivity.class);
                intent.putExtra("selectedWearable", selectedWearable);
                intent.putExtra("showName", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWearablesList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bMyWishList:
                Intent intent = new Intent(this, MyWishListActivity.class);
                intent.putExtra("wearablesOwner", wearablesOwner);
                startActivity(intent);
                break;
        }
    }

    private void loadWearablesList() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserWearableDataInBackground(new User(wearablesOwner), new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseMyW", serverResponse);
                if (serverResponse.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", MyWearablesActivity.this);
                    finish();
                } else if (serverResponse.equals("no wearables")) {
                    Log.d("no wearable", "No wearable found");
                    //Misc.showAlertMsg("No wearable found.", "Ok", MyWearablesActivity.this);
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
            newWearable.owner = wearablesOwner;

            wearablesList.add(newWearable);
        }

        Log.d("wearablesStrings", wearableStrings.toString());

        Log.d("debugTest", "Finished building");
    }
}
