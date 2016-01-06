package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class AddWearableActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etBrand, etDescription;
    Button bRegister;
    CheckBox cbBlue, cbBlack, cbWhite, cbRed, cbYellow, cbGreen, cbPink;
    RadioGroup radioGroup;
    List<String> selectedColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wearable);

        selectedColors = new ArrayList<>();

        cbBlue = (CheckBox) findViewById(R.id.cbBlue);
        cbBlack = (CheckBox) findViewById(R.id.cbBlack);
        cbWhite = (CheckBox) findViewById(R.id.cbWhite);
        cbRed = (CheckBox) findViewById(R.id.cbRed);
        cbYellow = (CheckBox) findViewById(R.id.cbYellow);
        cbGreen = (CheckBox) findViewById(R.id.cbGreen);
        cbPink = (CheckBox) findViewById(R.id.cbPink);

        radioGroup = (RadioGroup) findViewById(R.id.rgType);

        etBrand = (EditText) findViewById(R.id.etBrand);
        etDescription = (EditText) findViewById(R.id.etDescription);
        bRegister = (Button) findViewById(R.id.bRegister);

        cbBlue.setOnClickListener(this);
        cbBlack.setOnClickListener(this);
        cbWhite.setOnClickListener(this);
        cbRed.setOnClickListener(this);
        cbYellow.setOnClickListener(this);
        cbGreen.setOnClickListener(this);
        cbPink.setOnClickListener(this);
        bRegister.setOnClickListener(this);

        radioGroup.clearCheck();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String colors = buildColors();
                String type = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                String brand = etBrand.getText().toString();
                String description = etDescription.getText().toString();
                Wearable newWearable = new Wearable(colors, type, brand, description);
                addWearable(newWearable);
                break;
            default:
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()) {
                    addColor(cb.getText().toString());
                } else {
                    removeColor(cb.getText().toString());
                }
                break;
        }
    }

    private void addWearable(Wearable wearable) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeWearableDataInBackground(wearable, new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseAddW", serverResponse);
                if(serverResponse.contains("blank fields")) {
                    Misc.showAlertMsg("Please fill all the fields.", "Ok", AddWearableActivity.this);
                } else if(serverResponse.contains("wearable exists")) {
                    Misc.showAlertMsg("Wearable already exists.", "Ok", AddWearableActivity.this);
                } else if(serverResponse.contains("full table")) {
                    Misc.showAlertMsg("Full table.", "Ok", AddWearableActivity.this);
                } else if(serverResponse.contains("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", AddWearableActivity.this);
                    finish();
                } else if(serverResponse.contains("failure")) {
                    Misc.showAlertMsg("An error occurred.", "Ok", AddWearableActivity.this);
                } else if(serverResponse.contains("success")) {
                    Misc.showAlertMsg("Wearable successfully added!", "Ok", AddWearableActivity.this);
                    finish();
                }
            }
        });
    }

    private void addColor(String c) {
        selectedColors.add(c);
    }

    private void removeColor(String c) {
        selectedColors.remove(c);
    }

    private String buildColors() {
        String colors = "";
        for(int i = 0; i < selectedColors.size() ; i++) {
            if(colors.isEmpty()) {
                colors = selectedColors.get(i);
            } else {
                colors = colors + "&" + selectedColors.get(i);
            }
        }
        return colors;
    }
}
