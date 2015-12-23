package com.arara.smartwardrobe;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class ScanLabelActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    static final String ADDRESS = "20:15:03:30:01:97";
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_label);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
}
