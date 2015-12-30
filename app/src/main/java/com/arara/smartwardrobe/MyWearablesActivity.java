package com.arara.smartwardrobe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class MyWearablesActivity extends AppCompatActivity {

    List<String> wearablesList;
    ListView lvWearables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wearables);

        String wearablesOwner = getIntent().getExtras().getString("owner");

        Log.d("wearablesOwner", wearablesOwner);
    }
}
