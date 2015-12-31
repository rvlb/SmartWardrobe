package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewWearableActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvOwner, tvType, tvBrand, tvColors, tvDescription;

    Button bAddToMyWishList;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wearable);

        userLocalStore = new UserLocalStore(this);

        bAddToMyWishList = (Button) findViewById(R.id.bAddToMyWishList);
        bAddToMyWishList.setOnClickListener(this);

        Wearable selectedWearable = (Wearable) getIntent().getSerializableExtra("selectedWearable");

        tvOwner = (TextView) findViewById(R.id.tvOwner);
        tvType = (TextView) findViewById(R.id.tvType);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvColors = (TextView) findViewById(R.id.tvColors);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvOwner.setText((selectedWearable.owner + "'s wearable").toUpperCase());
        tvType.setText(selectedWearable.type);
        tvBrand.setText(selectedWearable.brand);
        tvColors.setText(selectedWearable.colors);
        tvDescription.setText(selectedWearable.description);

        Log.d("VWid", selectedWearable.id);
        Log.d("VWtype", selectedWearable.type);
        Log.d("VWbrand", selectedWearable.brand);
        Log.d("VWcolors", selectedWearable.colors);
        Log.d("VWdescription", selectedWearable.description);
        Log.d("VWowner", selectedWearable.owner);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddToMyWishList:
                //Adicionar na wishlist
                break;
        }
    }
}
