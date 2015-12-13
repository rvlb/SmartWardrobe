package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RFIDActivity extends AppCompatActivity implements View.OnClickListener {

    Button bScanLabel, bCreateLabel;
    //Button bAddWearable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);

        bScanLabel = (Button) findViewById(R.id.bScanLabel);
        bCreateLabel = (Button) findViewById(R.id.bCreateLabel);
        //bAddWearable = (Button) findViewById(R.id.bAddWearable);

        bScanLabel.setOnClickListener(this);
        bCreateLabel.setOnClickListener(this);
        //bAddWearable.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bScanLabel:
                startActivity(new Intent(this, ScanLabelActivity.class));
                break;

            case R.id.bCreateLabel:
                startActivity(new Intent(this, CreateLabelActivity.class));
                break;

            /*case R.id.bAddWearable:
                startActivity(new Intent(this, AddWearableActivity.class));
                break;*/
        }
    }
}
