package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewWearableActivity extends AppCompatActivity {

    TextView tvType, tvBrand, tvColors, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wearable);

        Wearable selectedWearable = (Wearable) getIntent().getSerializableExtra("selectedWearable");

        tvType = (TextView) findViewById(R.id.tvType);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvColors = (TextView) findViewById(R.id.tvColors);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvType.setText(selectedWearable.type);
        tvBrand.setText(selectedWearable.brand);
        tvColors.setText(selectedWearable.colors);
        tvDescription.setText(selectedWearable.description);

        Log.d("VWid", selectedWearable.id);
        Log.d("VWtype", selectedWearable.type);
        Log.d("VWbrand", selectedWearable.brand);
        Log.d("VWcolors", selectedWearable.colors);
        Log.d("VWdescription", selectedWearable.description);
    }
}
