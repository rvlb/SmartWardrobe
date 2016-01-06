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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyWishListActivity extends AppCompatActivity {

    List<Wearable> wishList;
    TextView tvWishListOwnerName;

    ListView lvWishList;

    String wearablesOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_list);

        wearablesOwner = getIntent().getExtras().getString("wearablesOwner");
        Log.d("wearablesOwner", wearablesOwner);

        tvWishListOwnerName = (TextView) findViewById(R.id.tvWishListOwnerName);
        tvWishListOwnerName.setText((wearablesOwner + "'s WishList").toUpperCase());

        wishList = new ArrayList<>();

        ArrayAdapter<Wearable> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wishList);

        lvWishList = (ListView) findViewById(R.id.lvWishList);
        lvWishList.setAdapter(adapter);
        lvWishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Wearable selectedWearable = (Wearable) lvWishList.getItemAtPosition(position);
                //Misc.showAlertMsg("Clicked on " + selectedWearable, "Ok", MyWearablesActivity.this);
                Intent intent = new Intent(MyWishListActivity.this, ViewWearableActivity.class);
                intent.putExtra("selectedWearable", selectedWearable);
                intent.putExtra("showName", false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishList();
    }

    private void loadWishList() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchWishListDataInBackground(new User(wearablesOwner), new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseWL", serverResponse);
                if (serverResponse.contains("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", MyWishListActivity.this);
                    finish();
                } else if (serverResponse.contains("no wearables")) {
                    Log.d("no wearable", "No wearable found");
                    //Misc.showAlertMsg("No wearable found.", "Ok", MyWishListActivity.this);
                } else {
                    buildWishList(serverResponse);
                    ((BaseAdapter) lvWishList.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    private void buildWishList(String wearablesData) {

        wishList.clear();

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

            wishList.add(newWearable);
        }

        Log.d("wearablesStrings", wearableStrings.toString());

        Log.d("debugTest", "Finished building");
    }
}
