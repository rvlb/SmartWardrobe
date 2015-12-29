package com.arara.smartwardrobe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyWearablesActivity extends AppCompatActivity {

    //Passar um parametro para saber quem eh o usuario escolhido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wearables);
    }
}
