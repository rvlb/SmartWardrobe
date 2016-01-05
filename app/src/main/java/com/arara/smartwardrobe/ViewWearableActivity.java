package com.arara.smartwardrobe;

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
    Wearable selectedWearable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wearable);

        userLocalStore = new UserLocalStore(this);

        bAddToMyWishList = (Button) findViewById(R.id.bAddToMyWishList);
        bAddToMyWishList.setOnClickListener(this);

        selectedWearable = (Wearable) getIntent().getSerializableExtra("selectedWearable");

        tvOwner = (TextView) findViewById(R.id.tvOwner);
        tvType = (TextView) findViewById(R.id.tvType);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvColors = (TextView) findViewById(R.id.tvColors);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        String nameToShow = selectedWearable.toString();

        if(getIntent().getExtras().getBoolean("showName")) {
            //Se a activity foi chamada de algum lugar que não a Wish List, o nome do
            //dono da wearable é exibido.
            nameToShow = (selectedWearable.owner + "'s wearable").toUpperCase();
        }

        tvOwner.setText(nameToShow);
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
                addToWishList();
                break;
        }
    }

    private void addToWishList() {
        final ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeWishListDataInBackground(userLocalStore.getLoggedUser(), selectedWearable, new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseVW", serverResponse);
                if (serverResponse.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", ViewWearableActivity.this);
                    finish();
                } else if(serverResponse.equals("wishlist entry exists")) {
                    Misc.showAlertMsg("This wearable is already in your WishList.", "Ok", ViewWearableActivity.this);
                } else if(serverResponse.equals("failure")) {
                    Misc.showAlertMsg("An error occurred.", "Ok", ViewWearableActivity.this);
                } else if(serverResponse.equals("success")) {
                    Misc.showAlertMsg("Wearable successfully added to your WishList!", "Ok", ViewWearableActivity.this);
                }
            }
        });
    }
}
