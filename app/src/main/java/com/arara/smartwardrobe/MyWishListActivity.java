package com.arara.smartwardrobe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class MyWishListActivity extends AppCompatActivity {

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

        //
    }
}
